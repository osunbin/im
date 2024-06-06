package com.bin.im.common.converter;

/**
 * Converter for {@link java.lang.String} type.
 */
public final class StringConverter extends AbstractStringConverter {

    public static final StringConverter INSTANCE = new StringConverter();

    private StringConverter() {
        super(ID_STRING);
    }

    @Override
    public Class<?> getValueClass() {
        return String.class;
    }

    @Override
    protected String cast(Object val) {
        return (String) val;
    }
}
