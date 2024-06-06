package com.bin.im.server.common;

import com.bin.im.common.converter.Converters;
import com.bin.im.common.converter.StringConverter;
import com.bin.im.server.common.type.ServiceType;
import com.bin.im.server.core.BaseHandler;
import com.bin.im.server.event.MessageListener;
import com.bin.im.server.spi.annotation.Service;
import com.bin.im.server.spi.annotation.ServiceMetadata;
import io.github.classgraph.AnnotationInfo;
import io.github.classgraph.AnnotationParameterValueList;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.MethodInfo;
import io.github.classgraph.MethodInfoList;
import io.github.classgraph.ScanResult;


import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static java.lang.Character.toUpperCase;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Stream.concat;

public final class ReflectionUtils {

    private ReflectionUtils() {
    }

    /**
     * Load a class using the current thread's context class loader as a
     * classLoaderHint. Exceptions are sneakily thrown.
     */
    public static Class<?> loadClass(String name) {
        return loadClass(Thread.currentThread().getContextClassLoader(), name);
    }

    /**
     * See {@link ClassLoaderUtil#loadClass(ClassLoader, String)}. Exceptions
     * are sneakily thrown.
     */
    public static Class<?> loadClass(ClassLoader classLoaderHint, String name) {
        try {
            return ClassLoaderUtil.loadClass(classLoaderHint, name);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * See {@link ClassLoaderUtil#newInstance(ClassLoader, String)}. Exceptions
     * are sneakily thrown.
     */
    public static <T> T newInstance(ClassLoader classLoader, String name) {
        try {
            return ClassLoaderUtil.newInstance(classLoader, name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads a value of a static field. In case of any exceptions it returns
     * null.
     */
    public static <T> T readStaticFieldOrNull(String className, String fieldName) {
        try {
            Class<?> clazz = Class.forName(className);
            return readStaticField(clazz, fieldName);
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException | SecurityException e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T readStaticField(Class<?> clazz, String fieldName) throws NoSuchFieldException,
            IllegalAccessException {
        Field field = clazz.getDeclaredField(fieldName);

        field.setAccessible(true);

        return (T) field.get(null);
    }

    /**
     * Return a set-method for a class and a property. The setter must start
     * with "set", must be public, non-static, must return void or the
     * containing class type (a builder-style setter) and take one argument of
     * {@code propertyType}.
     *
     * @param clazz        The containing class
     * @param propertyName Name of the property
     * @param propertyType The propertyType of the property
     * @return The found setter or null if one matching the criteria doesn't exist
     */
    public static Method findPropertySetter(
            Class<?> clazz,
            String propertyName,
            Class<?> propertyType
    ) {
        String setterName = "set" + toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);

        Method method;
        try {
            method = clazz.getMethod(setterName, propertyType);
        } catch (NoSuchMethodException e) {
            return null;
        }

        if (!Modifier.isPublic(method.getModifiers())) {
            return null;
        }

        if (Modifier.isStatic(method.getModifiers())) {
            return null;
        }

        Class<?> returnType = method.getReturnType();
        if (returnType != void.class && returnType != Void.class && returnType != clazz) {
            return null;
        }

        return method;
    }

    /**
     * Return a {@link Field} object for the given {@code fieldName}. The field
     * must be public and non-static.
     *
     * @param clazz     The containing class
     * @param fieldName The field
     * @return The field object or null, if not found or doesn't match the criteria.
     */
    public static Field findPropertyField(Class<?> clazz, String fieldName) {
        Field field;
        try {
            field = clazz.getField(fieldName);
        } catch (NoSuchFieldException e) {
            return null;
        }

        if (!Modifier.isPublic(field.getModifiers()) || Modifier.isStatic(field.getModifiers())) {
            return null;
        }

        return field;
    }


    public static Collection<Class<?>> nestedClassesOf(Class<?>... classes) {
        ClassGraph classGraph = new ClassGraph()
                .enableClassInfo()
                .ignoreClassVisibility();
        stream(classes).map(Class::getClassLoader).distinct().forEach(classGraph::addClassLoader);
        stream(classes).map(ReflectionUtils::toPackageName).distinct().forEach(classGraph::acceptPackages);
        try (ScanResult scanResult = classGraph.scan()) {
            Set<String> classNames = stream(classes).map(Class::getName).collect(toSet());
            return concat(
                    stream(classes),
                    scanResult.getAllClasses()
                            .stream()
                            .filter(classInfo -> classNames.contains(classInfo.getName()))
                            .flatMap(classInfo -> classInfo.getInnerClasses().stream())
                            .map(ClassInfo::loadClass)
            ).collect(toList());
        }
    }

    private static String toPackageName(Class<?> clazz) {
        return Optional.ofNullable(clazz.getPackage()).map(Package::getName).orElse("");
    }

    public static Map<ServiceType,List<ServiceMetadata>> serviceOf(String... packages) {
        if (packages == null) return Collections.emptyMap();
        Map<ServiceType,List<ServiceMetadata>> serviceMetadatas = new HashMap<>();

        ClassGraph classGraph = new ClassGraph()
                .enableAllInfo()
                .acceptPackages(packages)
                .ignoreClassVisibility();
        try (ScanResult scanResult = classGraph.scan()) {
            ClassInfoList allClasses =
                    scanResult.getClassesWithAnnotation(Service.class);
            for (ClassInfo allClass : allClasses) {

                Class<?> clazz = allClass.loadClass();

                boolean handlerExist = allClass.extendsSuperclass(BaseHandler.class);
                if (!handlerExist) continue;

                Object obj = null;
                try {
                    obj = ClassLoaderUtil.newInstance(Thread.currentThread().getContextClassLoader(), clazz);
                } catch (Exception e) {
                    throw new RuntimeException(allClass.getName() + " 初始化失败", e);
                }


                BaseHandler service = (BaseHandler) obj;

                AnnotationInfo serviceInfo = allClass.getAnnotationInfo(Service.class);
                AnnotationParameterValueList parameterValues = serviceInfo.getParameterValues();


                String name = getValueFromAnno(parameterValues, "name");
                if (name == null || "".equals(name.trim())) {
                    name = allClass.getSimpleName();
                }


                String module = getValueFromAnno(parameterValues, "module");
                String group = getValueFromAnno(parameterValues, "group");
                String url = getValueFromAnno(parameterValues, "url");

                Object typeValue = parameterValues.getValue("type");
                ServiceType type = (ServiceType) typeValue;
                if (type == ServiceType.EVENT) {
                    boolean exits = allClass.implementsInterface(MessageListener.class);
                    if (!exits) continue;


                }

                ServiceMetadata serviceMetadata = new ServiceMetadata();

                serviceMetadata.setName(name);
                serviceMetadata.setType(type);
                serviceMetadata.setModule(module);
                serviceMetadata.setGroup(group);
                serviceMetadata.setUrl(url);
                serviceMetadata.setService(service);
                serviceMetadata.setSrc(obj);
                List<ServiceMetadata> serviceMetadataList = serviceMetadatas.get(type);
                if (serviceMetadataList == null) {
                    serviceMetadataList = new ArrayList<>();
                    serviceMetadatas.put(type,serviceMetadataList);
                }
                serviceMetadataList.add(serviceMetadata);
            }

        }
        return serviceMetadatas;
    }


    private static String getValueFromAnno(AnnotationParameterValueList parameterValues, String name) {
        Object obj = parameterValues.getValue(name);
        return Converters.getConverter(String.class).asVarchar(obj);
    }

    public static Resources resourcesOf(String... packages) {
        String[] paths = stream(packages).map(ReflectionUtils::toPath).toArray(String[]::new);
        ClassGraph classGraph = new ClassGraph()
                .acceptPackages(packages)
                .acceptPaths(paths)
                .ignoreClassVisibility();
        try (ScanResult scanResult = classGraph.scan()) {
            Collection<ClassResource> classes =
                    scanResult.getAllClasses().stream().map(ClassResource::new).collect(toList());
            Collection<URL> nonClasses = scanResult.getAllResources().nonClassFilesOnly().getURLs();
            return new Resources(classes, nonClasses);
        }
    }

    private static String toPath(String name) {
        return name.replace('.', '/');
    }

    public static String toClassResourceId(String name) {
        return toPath(name) + ".class";
    }

    public static final class Resources {

        private final Collection<ClassResource> classes;
        private final Collection<URL> nonClasses;

        private Resources(Collection<ClassResource> classes, Collection<URL> nonClasses) {
            this.classes = classes;
            this.nonClasses = nonClasses;
        }

        public Stream<ClassResource> classes() {
            return classes.stream();
        }

        public Stream<URL> nonClasses() {
            return nonClasses.stream();
        }
    }

    public static final class ClassResource {

        private final String id;
        private final URL url;

        private ClassResource(ClassInfo classInfo) {
            this(classInfo.getName(), classInfo.getResource().getURL());
        }

        public ClassResource(String name, URL url) {
            this.id = toClassResourceId(name);
            this.url = url;
        }

        public String getId() {
            return id;
        }

        public URL getUrl() {
            return url;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            ClassResource that = (ClassResource) o;
            return Objects.equals(id, that.id) &&
                    Objects.equals(url, that.url);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, url);
        }
    }
}
