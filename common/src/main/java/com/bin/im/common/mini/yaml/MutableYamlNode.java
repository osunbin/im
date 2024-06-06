package com.bin.im.common.mini.yaml;

public interface MutableYamlNode extends YamlNode {
    /**
     * Sets the name of the node to the provided one
     *
     * @param nodeName The new name of the node
     */
    void setNodeName(String nodeName);
}