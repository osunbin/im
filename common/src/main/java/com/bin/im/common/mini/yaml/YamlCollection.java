package com.bin.im.common.mini.yaml;

public interface YamlCollection extends YamlNode {

    /**
     * Returns the children nodes
     *
     * @return the children nodes
     */
    Iterable<YamlNode> children();

    /**
     * Returns the number of the children that the collection has
     *
     * @return the number of the children
     */
    int childCount();
}