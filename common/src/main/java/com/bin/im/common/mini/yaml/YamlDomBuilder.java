package com.bin.im.common.mini.yaml;

import java.util.List;
import java.util.Map;

public class YamlDomBuilder {

    static YamlNode build(Object document, String rootName) {
        if (document == null) {
            throw new YamlException("The provided document is null");
        }

        if (rootName != null && !(document instanceof Map)) {
            throw new YamlException("The provided document is not a Map, and rootName is defined.");
        }

        final Object rootNode;
        if (rootName != null) {
            rootNode = ((Map) document).get(rootName);

            if (rootNode == null) {
                throw new YamlException("The required " + rootName
                        + " root node couldn't be found in the document root");
            }
        } else {
            rootNode = document;
        }

        return buildNode(null, rootName, rootNode);
    }

    public static YamlNode build(Object document) {
        return build(document, null);
    }

    @SuppressWarnings("unchecked")
    private static YamlNode buildNode(YamlNode parent, String nodeName, Object sourceNode) {
        if (sourceNode == null) {
            return null;
        }

        if (sourceNode instanceof Map) {
            YamlMappingImpl node = new YamlMappingImpl(parent, nodeName);
            buildChildren(node, (Map<String, Object>) sourceNode);
            return node;
        } else if (sourceNode instanceof List) {
            YamlSequenceImpl node = new YamlSequenceImpl(parent, nodeName);
            buildChildren(node, (List<Object>) sourceNode);
            return node;
        } else if (isSupportedScalarType(sourceNode)) {
            return buildScalar(parent, nodeName, sourceNode);
        } else {
            throw new YamlException("An unsupported scalar type is encountered: " + nodeName + " is an instance of "
                    + sourceNode.getClass().getName()
                    + ". The supported types are String, Integer, Long, Double and Boolean.");
        }
    }

    private static boolean isSupportedScalarType(Object sourceNode) {
        return sourceNode instanceof String
                || sourceNode instanceof Integer
                || sourceNode instanceof Long
                || sourceNode instanceof Double
                || sourceNode instanceof Boolean;
    }

    private static void buildChildren(YamlMappingImpl parentNode, Map<String, Object> mapNode) {
        for (Map.Entry<String, Object> entry : mapNode.entrySet()) {
            String childNodeName = entry.getKey();
            Object childNodeValue = entry.getValue();
            YamlNode child = buildNode(parentNode, childNodeName, childNodeValue);
            parentNode.addChild(childNodeName, child);
        }
    }

    private static void buildChildren(YamlSequenceImpl parentNode, List<Object> listNode) {
        for (Object value : listNode) {
            YamlNode child = buildNode(parentNode, null, value);
            parentNode.addChild(child);
        }
    }

    private static YamlNode buildScalar(YamlNode parent, String nodeName, Object value) {
        return new YamlScalarImpl(parent, nodeName, value);
    }
}
