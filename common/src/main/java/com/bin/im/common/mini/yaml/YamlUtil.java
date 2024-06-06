package com.bin.im.common.mini.yaml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public final class YamlUtil {
    private static final Pattern NAME_APOSTROPHE_PATTERN = Pattern.compile("(.*[\\t\\s,;:]+.*)");

    private YamlUtil() {
    }


    public static Object convert(YamlNode yamlNode) {
        if (yamlNode == null) {
            return null;
        }
        if (yamlNode instanceof YamlMapping) {
            YamlMapping yamlMapping = (YamlMapping) yamlNode;
            Map<String,Object> resultObject = new HashMap<>();
            for (YamlNameNodePair pair : yamlMapping.childrenPairs()) {
                resultObject.put(pair.nodeName(), convert(pair.childNode()));
            }
            return resultObject;
        } else if (yamlNode instanceof YamlSequence) {
            YamlSequence yamlSequence = (YamlSequence) yamlNode;
            List<Object> resultArray = new ArrayList<>();
            for (YamlNode child : yamlSequence.children()) {
                resultArray.add(convert(child));
            }
            return resultArray;
        } else if (yamlNode instanceof YamlScalar) {
            return ((YamlScalar) yamlNode).nodeValue();
        }
        throw new IllegalArgumentException("Unknown type " + yamlNode.getClass().getName());
    }

    /**
     * Takes a generic {@link YamlNode} instance and returns it casted to
     * {@link YamlMapping} if the type of the node is a descendant of
     * {@link YamlMapping}.
     *
     * @param node The generic node to cast
     * @return the casted mapping
     * @throws YamlException if the provided node is not a mapping
     */
    public static YamlMapping asMapping(YamlNode node) {
        if (node != null && !(node instanceof YamlMapping)) {
            String nodeName = node.nodeName();
            throw new YamlException(String.format("Child %s is not a mapping, it's actual type is %s",
                    nodeName, node.getClass()));
        }

        return (YamlMapping) node;
    }

    /**
     * Takes a generic {@link YamlNode} instance and returns it casted to
     * {@link YamlSequence} if the type of the node is a descendant of
     * {@link YamlSequence}.
     *
     * @param node The generic node to cast
     * @return the casted sequence
     * @throws YamlException if the provided node is not a sequence
     */
    public static YamlSequence asSequence(YamlNode node) {
        if (node != null && !(node instanceof YamlSequence)) {
            String nodeName = node.nodeName();
            throw new YamlException(String.format("Child %s is not a sequence, it's actual type is %s",
                    nodeName, node.getClass()));
        }

        return (YamlSequence) node;
    }

    /**
     * Takes a generic {@link YamlNode} instance and returns it casted to
     * {@link YamlScalar} if the type of the node is a descendant of
     * {@link YamlScalar}.
     *
     * @param node The generic node to cast
     * @return the casted scalar
     * @throws YamlException if the provided node is not a scalar
     */
    public static YamlScalar asScalar(YamlNode node) {
        if (node != null && !(node instanceof YamlScalar)) {
            String nodeName = node.nodeName();
            throw new YamlException(String.format("Child %s is not a scalar, it's actual type is %s", nodeName, node.getClass()));
        }

        return (YamlScalar) node;
    }

    /**
     * Takes a generic {@link YamlNode} instance and returns it casted to
     * the provided {@code type} if the node is an instance of that type.
     *
     * @param node The generic node to cast
     * @return the casted node
     * @throws YamlException if the provided node is not the expected type
     */
    @SuppressWarnings("unchecked")
    public static <T> T asType(YamlNode node, Class<T> type) {
        if (node != null && !type.isAssignableFrom(node.getClass())) {
            String nodeName = node.nodeName();
            throw new YamlException(String.format("Child %s is not a %s, it's actual type is %s", nodeName, type.getSimpleName(),
                    node.getClass().getSimpleName()));
        }
        return (T) node;
    }

    /**
     * Constructs the path of the node with the provided {@code parent}
     * node and {@code nodeName}.
     *
     * @param parent    The parent node
     * @param childName The name of the node the path is constructed for
     * @return the constructed path of the node
     */
    public static String constructPath(YamlNode parent, String childName) {
        if (childName != null) {
            childName = NAME_APOSTROPHE_PATTERN.matcher(childName).replaceAll("\"$1\"");
        }

        if (parent != null && parent.path() != null) {
            return parent.path() + "/" + childName;
        }

        return childName;
    }

    /**
     * Checks if the provided {@code node} is a mapping
     *
     * @param node The node to check
     * @return {@code true} if the provided node is a mapping
     */
    public static boolean isMapping(YamlNode node) {
        return node instanceof YamlMapping;
    }

    /**
     * Checks if the provided {@code node} is a sequence
     *
     * @param node The node to check
     * @return {@code true} if the provided node is a sequence
     */
    public static boolean isSequence(YamlNode node) {
        return node instanceof YamlSequence;
    }

    /**
     * Checks if the provided {@code node} is a scalar
     *
     * @param node The node to check
     * @return {@code true} if the provided node is a scalar
     */
    public static boolean isScalar(YamlNode node) {
        return node instanceof YamlScalar;
    }

    /**
     * Checks if the two provided {@code nodes} are of the same type
     *
     * @param left  The left-side node of the check
     * @param right The right-side node of the check
     * @return {@code true} if the provided nodes are of the same type
     */
    public static boolean isOfSameType(YamlNode left, YamlNode right) {
        return left instanceof YamlMapping && right instanceof YamlMapping
                || left instanceof YamlSequence && right instanceof YamlSequence
                || left instanceof YamlScalar && right instanceof YamlScalar;
    }
}
