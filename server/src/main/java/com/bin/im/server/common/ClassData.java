package com.bin.im.server.common;

import java.util.Collections;
import java.util.Map;

public class ClassData  {

    private Map<String, byte[]> innerClassDefinitions = Collections.emptyMap();

    //this field needed for compatibility with 3.8
    private byte[] mainClassDefinition;

    public ClassData() {
    }

    Map<String, byte[]> getInnerClassDefinitions() {
        return innerClassDefinitions;
    }

    public void setInnerClassDefinitions(Map<String, byte[]> innerClassDefinitions) {
        this.innerClassDefinitions = innerClassDefinitions;
    }

    //this method and related field is for compatibility with 3.8
    void setMainClassDefinition(byte[] mainClassDefinition) {
        this.mainClassDefinition = mainClassDefinition;
    }

    //this method and related field is for compatibility with 3.8
    byte[] getMainClassDefinition() {
        return mainClassDefinition;
    }


}
