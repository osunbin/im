package com.bin.im.common.mini.yaml;

import static com.bin.im.common.mini.yaml.YamlUtil.constructPath;

public abstract class AbstractYamlNode implements MutableYamlNode {
    private final YamlNode parent;
    private String nodeName;
    private String path;

    AbstractYamlNode(YamlNode parent, String nodeName) {
        this.parent = parent;
        this.nodeName = nodeName;
        this.path = constructPath(parent, nodeName);
    }

    @Override
    public String nodeName() {
        return nodeName != null ? nodeName : UNNAMED_NODE;
    }

    @Override
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
        this.path = constructPath(parent, nodeName);
    }

    @Override
    public YamlNode parent() {
        return parent;
    }

    @Override
    public String path() {
        return path;
    }
}