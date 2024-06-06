package com.bin.im.common.mini.yaml;

public interface MutableYamlScalar extends YamlScalar {
    /**
     * Sets the value of the scalar node
     *
     * @param newValue The new value of the scalar node
     */
    void setValue(Object newValue);
}
