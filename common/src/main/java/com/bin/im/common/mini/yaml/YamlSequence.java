package com.bin.im.common.mini.yaml;

public interface YamlSequence extends YamlCollection {

    /**
     * Gets a child node by its index
     *
     * @param index the index of the child node
     * @return the child node with the given index if exists,
     * {@code null} otherwise
     */
    YamlNode child(int index);

    /**
     * Gets a child mapping node by its index
     *
     * @param index the index of the child node
     * @return the child mapping node with the given index if exists,
     * {@code null} otherwise
     */
    YamlMapping childAsMapping(int index);

    /**
     * Gets a child sequence node by its index
     *
     * @param index the index of the child node
     * @return the child sequence node with the given index if exists,
     * {@code null} otherwise
     */
    YamlSequence childAsSequence(int index);

    /**
     * Gets a child scalar node by its index
     *
     * @param index the index of the child node
     * @return the child scalar node with the given index if exists,
     * {@code null} otherwise
     */
    YamlScalar childAsScalar(int index);

    /**
     * Gets a child scalar node's value by its index
     * <p>
     * See {@link YamlScalar} for the possible types
     * <p>
     * Please note that if the scalar's type is not the expected type T,
     * a {@link ClassCastException} is thrown <strong>at the call site</strong>.
     *
     * @param index the index of the child node
     * @return the child scalar node's value with the given index if exists,
     * {@code null} otherwise
     * @see YamlScalar
     */
    <T> T childAsScalarValue(int index);

    /**
     * Gets a child scalar node's value by its name with type hinting
     * <p>
     * See {@link YamlScalar} for the possible types
     *
     * @param index the index of the child node
     * @param type  the type that the scalar's value type to be validated
     *              against
     * @return the child scalar node's value with the given name if exists,
     * {@code null} otherwise
     * @throws YamlException if the scalar's value is not a type of T
     * @see YamlScalar
     */
    <T> T childAsScalarValue(int index, Class<T> type);

}
