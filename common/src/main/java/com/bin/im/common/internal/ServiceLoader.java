package com.bin.im.common.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

public final class ServiceLoader {
    // kill-switch for URLDefinition#equals fix to take into account classloader
    private static final boolean URLDEFINITION_COMPAT = false;

    private static final Logger logger = LoggerFactory.getLogger(ServiceLoader.class);


    private static final String IGNORED_GLASSFISH_MAGIC_CLASSLOADER =
            "com.sun.enterprise.v3.server.APIClassLoaderServiceImpl$APIClassLoader";

    private ServiceLoader() {
    }

    public static <T> T load(Class<T> clazz, String factoryId, ClassLoader classLoader) throws Exception {
        Iterator<T> iterator = iterator(clazz, factoryId, classLoader);
        if (iterator.hasNext()) {
            return iterator.next();
        }
        return null;
    }

    public static <T> Iterator<T> iterator(Class<T> expectedType, String factoryId, ClassLoader classLoader) throws Exception {
        Iterator<Class<T>> classIterator = classIterator(expectedType, factoryId, classLoader);
        return new NewInstanceIterator<>(classIterator);
    }

    public static <T> Iterator<Class<T>> classIterator(Class<T> expectedType, String factoryId, ClassLoader classLoader)
            throws Exception {
        Set<ServiceDefinition> serviceDefinitions = getServiceDefinitions(factoryId, classLoader);
        return new ClassIterator<>(serviceDefinitions, expectedType);
    }

    private static Set<ServiceDefinition> getServiceDefinitions(String factoryId, ClassLoader classLoader) {
        List<ClassLoader> classLoaders = selectClassLoaders(classLoader);

        Set<URLDefinition> factoryUrls = new HashSet<>();
        for (ClassLoader selectedClassLoader : classLoaders) {
            factoryUrls.addAll(collectFactoryUrls(factoryId, selectedClassLoader));
        }

        Set<ServiceDefinition> serviceDefinitions = new HashSet<>();
        for (URLDefinition urlDefinition : factoryUrls) {
            serviceDefinitions.addAll(parse(urlDefinition));
        }
        if (serviceDefinitions.isEmpty()) {
            logger.warn(
                    "Service loader could not load 'META-INF/services/" + factoryId + "'. It may be empty or does not exist.");
        }
        return serviceDefinitions;
    }

    private static Set<URLDefinition> collectFactoryUrls(String factoryId, ClassLoader classLoader) {
        String resourceName = "META-INF/services/" + factoryId;
        try {
            Enumeration<URL> configs = classLoader.getResources(resourceName);

            Set<URLDefinition> urlDefinitions = new HashSet<>();
            while (configs.hasMoreElements()) {
                URL url = configs.nextElement();
                if (!classLoader.getClass().getName().equals(IGNORED_GLASSFISH_MAGIC_CLASSLOADER)) {
                    urlDefinitions.add(new URLDefinition(url, classLoader));
                }
            }
            return urlDefinitions;

        } catch (Exception e) {
            logger.error("collectFactoryUrls ", e);
        }
        return Collections.emptySet();
    }

    private static Set<ServiceDefinition> parse(URLDefinition urlDefinition) {
        try {
            Set<ServiceDefinition> names = new HashSet<>();
            BufferedReader r = null;
            try {
                URL url = urlDefinition.url;
                r = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
                while (true) {
                    String line = r.readLine();
                    if (line == null) {
                        break;
                    }
                    int comment = line.indexOf('#');
                    if (comment >= 0) {
                        line = line.substring(0, comment);
                    }
                    String name = line.trim();
                    if (name.length() == 0) {
                        continue;
                    }
                    names.add(new ServiceDefinition(name, urlDefinition.classLoader));
                }
            } finally {
                if (r != null) {
                    r.close();
                }
            }
            return names;
        } catch (Exception e) {
            logger.error("parse", e);
        }
        return Collections.emptySet();
    }

    static List<ClassLoader> selectClassLoaders(ClassLoader classLoader) {
        // list prevents reordering!
        List<ClassLoader> classLoaders = new ArrayList<>();

        if (classLoader != null) {
            classLoaders.add(classLoader);
        }

        // check if TCCL is same as given classLoader
        ClassLoader tccl = Thread.currentThread().getContextClassLoader();
        if (tccl != null && tccl != classLoader) {
            classLoaders.add(tccl);
        }

        // Hazelcast core classLoader
        ClassLoader coreClassLoader = ServiceLoader.class.getClassLoader();
        if (coreClassLoader != classLoader && coreClassLoader != tccl) {
            classLoaders.add(coreClassLoader);
        }

        // Hazelcast client classLoader
        try {
            Class<?> hzClientClass = Class.forName("com.hazelcast.client.HazelcastClient");
            ClassLoader clientClassLoader = hzClientClass.getClassLoader();
            if (clientClassLoader != classLoader && clientClassLoader != tccl && clientClassLoader != coreClassLoader) {
                classLoaders.add(clientClassLoader);
            }
        } catch (ClassNotFoundException ignore) {

        }

        return classLoaders;
    }

    /**
     * Definition of the internal service based on the classloader that is able to load it
     * and the class name of the service that was found.
     */
    static final class ServiceDefinition {

        private final String className;
        private final ClassLoader classLoader;

        ServiceDefinition(String className, ClassLoader classLoader) {
            this.className = Objects.requireNonNull(className, "className");
            this.classLoader = Objects.requireNonNull(classLoader, "classLoader");
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            ServiceDefinition that = (ServiceDefinition) o;
            if (!classLoader.equals(that.classLoader)) {
                return false;
            }
            if (!className.equals(that.className)) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int result = className.hashCode();
            result = 31 * result + classLoader.hashCode();
            return result;
        }
    }

    /**
     * This class keeps track of available service definition URLs and
     * the corresponding classloaders. It uses the URLs URI for hashing andd
     * equality comparison, rather than the URL itself. The specifications for
     * hashing and equality on URL are unusual, and involve blocking DNS
     * lookups. However, the conversion from URL to URI is lossy, so if there
     * are multiple URLs that map to the same URI, then they may be considered
     * equal even if the URLs are not. If these are put in a HashSet, then the
     * last added element wins.
     */
    private static final class URLDefinition {

        private final URL url;
        private final URI uri;
        private final ClassLoader classLoader;

        private URLDefinition(URL url, ClassLoader classLoader) throws URISyntaxException {
            this.url = url;
            this.uri = url == null ? null : url.toURI();
            this.classLoader = classLoader;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            URLDefinition that = (URLDefinition) o;
            if (uri != null ? !uri.equals(that.uri) : that.uri != null) {
                return false;
            }
            return URLDEFINITION_COMPAT || Objects.equals(classLoader, that.classLoader);
        }

        @Override
        public int hashCode() {
            return uri == null ? 0 : uri.hashCode();
        }
    }

    static class NewInstanceIterator<T> implements Iterator<T> {

        private final Iterator<Class<T>> classIterator;

        NewInstanceIterator(Iterator<Class<T>> classIterator) {
            this.classIterator = classIterator;
        }

        @Override
        public boolean hasNext() {
            return classIterator.hasNext();
        }

        @Override
        public T next() {
            Class<T> clazz = classIterator.next();
            try {
                Constructor<T> constructor = clazz.getDeclaredConstructor();
                if (!constructor.isAccessible()) {
                    constructor.setAccessible(true);
                }
                return constructor.newInstance();
            } catch (InstantiationException
                    | IllegalAccessException
                    | NoSuchMethodException
                    | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }



    public static boolean implementsInterfaceWithSameName(Class<?> clazz, Class<?> iface) {
        Class<?>[] interfaces = getAllInterfaces(clazz);
        for (Class implementedInterface : interfaces) {
            if (implementedInterface.getName().equals(iface.getName())) {
                return true;
            }
        }
        return false;
    }

    public static Class<?>[] getAllInterfaces(Class<?> clazz) {
        Collection<Class<?>> interfaces = new HashSet<Class<?>>();
        addOwnInterfaces(clazz, interfaces);
        addInterfacesOfSuperclasses(clazz, interfaces);
        return interfaces.toArray(new Class<?>[0]);
    }

    private static void addOwnInterfaces(Class<?> clazz, Collection<Class<?>> allInterfaces) {
        Class<?>[] interfaces = clazz.getInterfaces();
        Collections.addAll(allInterfaces, interfaces);
        for (Class cl : interfaces) {
            addOwnInterfaces(cl, allInterfaces);
        }
    }

    private static void addInterfacesOfSuperclasses(Class<?> clazz, Collection<Class<?>> interfaces) {
        Class<?> superClass = clazz.getSuperclass();
        while (superClass != null) {
            addOwnInterfaces(superClass, interfaces);
            superClass = superClass.getSuperclass();
        }
    }

    /**
     * Iterates over services. It skips services which implement an interface with the expected name,
     * but loaded by a different classloader.
     * <p>
     * When a service does not implement an interface with expected name then it throws an exception
     *
     * @param <T>
     */
    static class ClassIterator<T> implements Iterator<Class<T>> {

        private final Iterator<ServiceDefinition> iterator;
        private final Class<T> expectedType;
        private final Set<Class<?>> alreadyProvidedClasses = new HashSet<>();
        private Class<T> nextClass;

        ClassIterator(Set<ServiceDefinition> serviceDefinitions, Class<T> expectedType) {
            iterator = serviceDefinitions.iterator();
            this.expectedType = expectedType;
        }

        @Override
        public boolean hasNext() {
            if (nextClass != null) {
                return true;
            }
            return advance();
        }

        private boolean advance() {
            while (iterator.hasNext()) {
                ServiceDefinition definition = iterator.next();
                String className = definition.className;
                ClassLoader classLoader = definition.classLoader;

                try {
                    Class<?> candidate = loadClass(className, classLoader);
                    if (expectedType.isAssignableFrom(candidate)) {
                        if (!isDuplicate(candidate)) {
                            nextClass = (Class<T>) candidate;
                            return true;
                        }
                    } else {
                        onNonAssignableClass(className, candidate);
                    }
                } catch (ClassNotFoundException e) {
                    onClassNotFoundException(className, classLoader, e);
                }
            }
            return false;
        }

        private boolean isDuplicate(Class<?> candidate) {
            return !alreadyProvidedClasses.add(candidate);
        }

        private Class<?> loadClass(String className, ClassLoader classLoader) throws ClassNotFoundException {
            return classLoader.loadClass(className);
        }

        private void onClassNotFoundException(String className, ClassLoader classLoader, ClassNotFoundException e) {
            if (className.startsWith("com.hazelcast")) {
                logger.info("Failed to load " + className + " by " + classLoader
                        + ". This indicates a classloading issue. It can happen in a runtime with "
                        + "a complicated classloading model (OSGi, Java EE, etc)");
            } else {
                throw new RuntimeException(e);
            }
        }

        private void onNonAssignableClass(String className, Class candidate) {
            if (expectedType.isInterface()) {
                if (implementsInterfaceWithSameName(candidate, expectedType)) {
                    // this can happen in application containers - different Hazelcast JARs are loaded
                    // by different class loaders.
                    logger.info("There appears to be a classloading conflict. "
                            + "Class " + className + " loaded by " + candidate.getClassLoader() + " implements "
                            + expectedType.getName() + " from its own class loader, but it does not implement "
                            + expectedType.getName() + " loaded by " + expectedType.getClassLoader());
                } else {
                    // the class does not implement an interface with the expected name
                    if (candidate.getClassLoader() != expectedType.getClassLoader()) {
                        logger.info("There appears to be a classloading conflict. "
                                + "Class " + className + " loaded by " + candidate.getClassLoader() + " does not "
                                + "implement an interface with name " + expectedType.getName() + " in both class loaders. "
                                + "The interface is currently loaded by " + expectedType.getClassLoader());
                    } else {
                        logger.info("The class " + candidate.getName() + " does not implement the expected "
                                + "interface " + expectedType.getName());
                    }
                }
            }
        }



        @Override
        public Class<T> next() {
            if (nextClass == null) {
                advance();
            }
            if (nextClass == null) {
                throw new NoSuchElementException();
            }
            Class<T> classToReturn = nextClass;
            nextClass = null;
            return classToReturn;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}