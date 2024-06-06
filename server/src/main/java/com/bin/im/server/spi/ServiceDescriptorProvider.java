package com.bin.im.server.spi;

public interface ServiceDescriptorProvider {
    ServiceDescriptor[] createServiceDescriptors();
}