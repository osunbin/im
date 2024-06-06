package com.bin.im.server.spi.impl;

import com.bin.im.common.internal.ServiceLoader;
import com.bin.im.common.internal.concurrent.CopyOnWriteMap;
import com.bin.im.common.internal.utils.StringUtils;
import com.bin.im.server.common.ReflectionUtils;
import com.bin.im.server.common.type.ServiceType;
import com.bin.im.server.core.BaseHandler;
import com.bin.im.server.event.EventService;
import com.bin.im.server.event.MessageListener;
import com.bin.im.server.spi.ServiceDescriptor;
import com.bin.im.server.spi.ServiceDescriptorProvider;
import com.bin.im.server.spi.ServiceInfo;
import com.bin.im.server.spi.annotation.ServiceMetadata;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.ServiceConfig;
import org.apache.dubbo.rpc.model.ApplicationModel;
import org.apache.dubbo.rpc.service.GenericService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;


public class ServiceManagerImpl implements ServiceManager {

    private static Logger logger = LoggerFactory.getLogger(ServiceManagerImpl.class);

    private static final String PROVIDER_ID = ServiceDescriptorProvider.class.getName();

    private final ImEngineImpl imEngine;

    private final ConcurrentMap<String, ServiceInfo> services = new CopyOnWriteMap<>();


    public ServiceManagerImpl(final ImEngineImpl imEngine) {
        this.imEngine = imEngine;
    }

    public synchronized void start() {
        List<BaseHandler> initService = new ArrayList<>();
        registerServices(initService);

        initServices(initService);
    }


    private void registerServices(List<BaseHandler> initService) {

        Map<ServiceType, List<ServiceMetadata>> serviceTypeListMap = ReflectionUtils.serviceOf("com.bin.im.server");

        Set<ServiceType> serviceTypes = serviceTypeListMap.keySet();

        for (ServiceType serviceType : serviceTypes) {
            List<ServiceMetadata> serviceMetadataList = serviceTypeListMap.get(serviceType);
            handle(serviceType, serviceMetadataList, initService);
        }

        // readServiceDescriptors();
    }

    private void handle(ServiceType serviceType, List<ServiceMetadata> serviceMetadataList, List<BaseHandler> initService) {

        switch (serviceType) {
            case NORMAL -> {
                registerCoreServices(serviceMetadataList, initService);
                break;
            }
            case DUBBO -> {
             //   registerDubboServices(serviceMetadataList, initService);
                break;
            }
            case EVENT -> {
                registerEventServices(serviceMetadataList, initService);
                break;
            }
        }
    }



    private void registerCoreServices(List<ServiceMetadata> serviceMetadatas, List<BaseHandler> initService) {
        for (ServiceMetadata serviceMetadata : serviceMetadatas) {
            BaseHandler service = serviceMetadata.getService();
            String serviceName = service.getServiceName();
            if (StringUtils.isEmpty(serviceName)) {
                serviceName = serviceMetadata.getName();
            }

            registerService(serviceName, service);
            initService.add(service);
        }
    }


    private void registerEventServices(List<ServiceMetadata> serviceMetadatas, List<BaseHandler> initService) {
        EventService eventService = imEngine.getEventService();
        for (ServiceMetadata serviceMetadata : serviceMetadatas) {
            BaseHandler service = serviceMetadata.getService();
            String serviceName = service.getServiceName();
            if (StringUtils.isEmpty(serviceName)) {
                serviceName = serviceMetadata.getName();
            }
            String url = serviceMetadata.getUrl();
            if (StringUtils.isEmpty(url)) continue;

            Object src = serviceMetadata.getSrc();
            // 后续扩展 tag ...
            eventService.registerListener(url, (MessageListener<?>) src);

            initService.add(service);
        }
    }

    private void registerDubboServices(List<ServiceMetadata> serviceMetadataList, List<BaseHandler> initService) {


        ApplicationModel applicationModel = ApplicationModel.defaultModel();
        applicationModel.setModelName("im-server");
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("zookeeper://127.0.0.1:2181");
        for (ServiceMetadata serviceMetadata : serviceMetadataList) {

            String url = serviceMetadata.getUrl(); // 类全路径
            ServiceConfig<GenericService> service = new ServiceConfig<>();
            service.setScopeModel(applicationModel);
            service.setRegistry(registryConfig);

            service.setInterface(url);
           // service.setRef();

            service.setGeneric("true");
            service.export();

        }

    }
    private void readServiceDescriptors() {
        try {
            ClassLoader classLoader = ServiceManagerImpl.class.getClassLoader();
            Iterator<Class<ServiceDescriptorProvider>> iterator
                    = ServiceLoader.classIterator(ServiceDescriptorProvider.class, PROVIDER_ID, classLoader);

            while (iterator.hasNext()) {
                Class<ServiceDescriptorProvider> clazz = iterator.next();
                Constructor<ServiceDescriptorProvider> constructor = clazz.getDeclaredConstructor();
                ServiceDescriptorProvider provider = constructor.newInstance();
                ServiceDescriptor[] services = provider.createServiceDescriptors();

                for (ServiceDescriptor serviceDescriptor : services) {

                    registerService(serviceDescriptor.getServiceName(), serviceDescriptor.getService(imEngine));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public synchronized void registerService(String serviceName, Object service) {
        if (logger.isDebugEnabled()) {
            logger.debug("Registering service: '" + serviceName + "'");
        }
        final ServiceInfo serviceInfo = new ServiceInfo(serviceName, service);
        final ServiceInfo currentServiceInfo = services.putIfAbsent(serviceName, serviceInfo);
        if (currentServiceInfo != null) {
            logger.warn("Replacing " + currentServiceInfo + " with " + serviceInfo);
            services.put(serviceName, serviceInfo);
        }
    }


    private void initServices(List<BaseHandler> initService) {
        for (BaseHandler handler : initService) {
            handler.init(imEngine);
        }
    }

    @Override
    public ServiceInfo getServiceInfo(String serviceName) {
        return services.get(serviceName);
    }

    @Override
    public List<ServiceInfo> getServiceInfos(Class serviceClass) {
        final LinkedList<ServiceInfo> result = new LinkedList<>();
        for (ServiceInfo serviceInfo : services.values()) {
            if (serviceInfo.isInstanceOf(serviceClass)) {
                result.addLast(serviceInfo);
            }
        }
        return result;
    }

    @Override
    public <T> T getService(String serviceName) {
        final ServiceInfo serviceInfo = getServiceInfo(serviceName);
        return serviceInfo != null ? (T) serviceInfo.getService() : null;
    }

    @Override
    public <S> List<S> getServices(Class<S> serviceClass) {
        final LinkedList<S> result = new LinkedList<>();
        for (ServiceInfo serviceInfo : services.values()) {
            if (serviceInfo.isInstanceOf(serviceClass)) {
                final S service = serviceInfo.getService();
                result.addLast(service);
            }
        }
        return result;
    }


}
