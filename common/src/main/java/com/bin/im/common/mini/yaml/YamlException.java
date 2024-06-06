package com.bin.im.common.mini.yaml;

public class YamlException extends RuntimeException {

    public YamlException(final String message) {
        super(message);
    }

    public YamlException(final String message, final Throwable cause) {
        super(message, cause);
    }
}