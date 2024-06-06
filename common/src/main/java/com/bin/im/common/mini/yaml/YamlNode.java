package com.bin.im.common.mini.yaml;

public interface YamlNode {

    String UNNAMED_NODE = "<unnamed>";

    /**
     * Returns the parent of the given node
     *
     * @return the parent node if exists, <code>null</code> otherwise
     */
    YamlNode parent();

    /**
     * Returns the name of the node
     *
     * @return the name of the node or {@link #UNNAMED_NODE} if not available
     */
    String nodeName();

    /**
     * Returns the path of the node from the root of the YAML structure.
     *
     * @return the path of the node
     */
    String path();
}
