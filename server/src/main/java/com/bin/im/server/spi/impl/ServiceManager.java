package com.bin.im.server.spi.impl;

import com.bin.im.server.spi.ServiceInfo;

import java.util.List;

public interface ServiceManager {

    ThreadLocal<ServerContext> requestContext = new ThreadLocal<>();


     static void createContext(ServerContext serverContext) {
        requestContext.set(serverContext);
    }

     static ServerContext getCurrContext() {
        return requestContext.get();
    }

     static void clearCurrContext() {
        requestContext.remove();
    }

    ServiceInfo getServiceInfo(String serviceName);

    List<ServiceInfo> getServiceInfos(Class serviceClass);

    <T> T getService( String serviceName);


    <S> List<S> getServices(Class<S> serviceClass);


}
