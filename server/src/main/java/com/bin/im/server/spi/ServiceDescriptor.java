package com.bin.im.server.spi;

import com.bin.im.server.common.type.ServiceType;

public interface ServiceDescriptor {

    ServiceType getServiceType();

    /**
     * @return name of the service;
     */
    String getServiceName();


    Object getService(ImEngine imEngine);
}