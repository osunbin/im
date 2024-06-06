package com.bin.im.common.mini.yaml;

/**
 * Interface for YAML scalar nodes
 * <p>
 * The following types are supported:
 * <ul>
 * <li>String</li>
 * <li>Integer</li>
 * <li>Float</li>
 * <li>Boolean</li>
 * </ul>
 */
public interface YamlScalar extends YamlNode {
    /**
     * Checks if the value of this node is the given type
     *
     * @param type the {@link Class} instance of the type to check
     * @param <T>  the type to check
     * @return true if the value of the node is instance of the given type
     */
    <T> boolean isA(Class<T> type);

    /**
     * Gets the value of the node
     * <p>
     * Please note that if the scalar's type is not the expected type T,
     * a {@link ClassCastException} is thrown <strong>at the call site</strong>.
     *
     * @param <T> the expected type of the node
     * @return the value of the node
     */
    <T> T nodeValue();

    /**
     * Gets the value of the node with validating its type against the
     * provided type
     *
     * @param <T> the expected type of the node
     * @return the value of the node
     * @throws YamlException if the scalar's value is not a type of T
     */
    <T> T nodeValue(Class<T> type);
}