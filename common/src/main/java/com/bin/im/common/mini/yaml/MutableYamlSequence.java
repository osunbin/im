package com.bin.im.common.mini.yaml;

public interface MutableYamlSequence extends YamlSequence, MutableYamlNode {

    /**
     * Adds a new child node to the sequence
     *
     * @param node The child node
     */
    void addChild(YamlNode node);
}