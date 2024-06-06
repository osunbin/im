package com.bin.im.common.mini.yaml;

public interface MutableYamlMapping extends YamlMapping, MutableYamlNode {

    /**
     * Adds a new child node to the mapping with the provided name
     *
     * @param name The name of the new child
     * @param node The child node
     */
    void addChild(String name, YamlNode node);

    /**
     * Removes a child with the given name if exists
     *
     * @param name The name of the child to remove
     */
    void removeChild(String name);
}