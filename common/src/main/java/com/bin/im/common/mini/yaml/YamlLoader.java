package com.bin.im.common.mini.yaml;

import org.snakeyaml.engine.v2.api.Load;
import org.snakeyaml.engine.v2.api.LoadSettings;

import java.io.InputStream;

public class YamlLoader {




    public static YamlNode load(InputStream inputStream) {
        try {
            Object document = getLoad().loadFromInputStream(inputStream);
            return buildDom(document);
        } catch (Exception ex) {
            throw new YamlException("An error occurred while loading and parsing the YAML stream", ex);
        }
    }

    public static YamlNode load(InputStream inputStream, String rootName) {
        try {
            Object document = getLoad().loadFromInputStream(inputStream);
            return buildDom(rootName, document);
        } catch (Exception ex) {
            throw new YamlException("An error occurred while loading and parsing the YAML stream", ex);
        }
    }


    public static YamlNode load(String yaml) {
        try {
            Object document = getLoad().loadFromString(yaml);
            return buildDom(document);
        } catch (Exception ex) {
            throw new YamlException("An error occurred while loading and parsing the YAML string", ex);
        }
    }


    private static Load getLoad() {
        LoadSettings settings = LoadSettings.builder().build();
        return new Load(settings);
    }

    private static YamlNode buildDom(String rootName, Object document) {
        return YamlDomBuilder.build(document, rootName);
    }

    private static YamlNode buildDom(Object document) {
        return YamlDomBuilder.build(document);
    }
}
