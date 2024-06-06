package com.bin.im.server.spi.annotation;

import com.bin.im.server.common.type.ServiceType;
import com.bin.im.server.core.BaseHandler;

public class ServiceMetadata {

    private String name;
    private ServiceType type;
    private String module;
    private String group;
    private String url;
    private BaseHandler service;
    private Object src;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ServiceType getType() {
        return type;
    }

    public void setType(ServiceType type) {
        this.type = type;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public BaseHandler getService() {
        return service;
    }

    public void setService(BaseHandler service) {
        this.service = service;
    }

    public Object getSrc() {
        return src;
    }

    public void setSrc(Object src) {
        this.src = src;
    }
}
