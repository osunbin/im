package com.bin.im.common.converter;

/**
 * Converter for {@link java.lang.Character} type.
 */
public final class CharacterConverter extends AbstractStringConverter {

    public static final CharacterConverter INSTANCE = new CharacterConverter();

    private CharacterConverter() {
        super(ID_CHARACTER);
    }

    @Override
    public Class<?> getValueClass() {
        return Character.class;
    }

    @Override
    protected String cast(Object val) {
        Character val0 = (Character) val;

        assert val0 != null;

        return val0.toString();
    }
}
