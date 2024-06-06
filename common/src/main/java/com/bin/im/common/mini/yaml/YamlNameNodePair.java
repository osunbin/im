package com.bin.im.common.mini.yaml;

public class YamlNameNodePair {
    private final String nodeName;
    private final YamlNode childNode;

    YamlNameNodePair(String nodeName, YamlNode childNode) {
        this.nodeName = nodeName;
        this.childNode = childNode;
    }

    /**
     * Returns the name of the node
     *
     * @return the name of the node
     */
    public String nodeName() {
        return nodeName;
    }

    /**
     * The {@link YamlNode} instance
     *
     * @return the node instance if present or {@code null} if the node
     * is explicitly defined as {@code !!null} in the YAML document
     */
    public YamlNode childNode() {
        return childNode;
    }
}