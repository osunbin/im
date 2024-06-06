package com.bin.im.server.spi;

public final class ServiceInfo {

    private final String name;
    private final Object service;

    public ServiceInfo(String name, Object service) {
        this.name = name;
        this.service = service;
    }

    public String getName() {
        return name;
    }

    public <T> T getService() {
        return (T) service;
    }


    @SuppressWarnings("unchecked")
    public boolean isInstanceOf(Class type) {
        return type.isAssignableFrom(service.getClass());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ServiceInfo that = (ServiceInfo) o;

        if (name != null ? !name.equals(that.name) : that.name != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ServiceInfo{name='" + name + "', service=" + service + '}';
    }
}
