package com.bin.im.common.converter;

import com.bin.im.common.converter.sql.QueryDataTypeFamily;

/**
 * Converter for {@link java.lang.Boolean} type.
 */
public final class BooleanConverter extends Converter {

    public static final BooleanConverter INSTANCE = new BooleanConverter();

    static final String TRUE = "true";
    static final String FALSE = "false";

    private BooleanConverter() {
        super(ID_BOOLEAN, QueryDataTypeFamily.BOOLEAN);
    }

    @Override
    public Class<?> getValueClass() {
        return Boolean.class;
    }

    @Override
    public boolean asBoolean(Object val) {
        return ((Boolean) val);
    }

    @Override
    public String asVarchar(Object val) {
        boolean val0 = (Boolean) val;

        return val0 ? TRUE : FALSE;
    }

    @Override
    public Object convertToSelf(Converter valConverter, Object val) {
        return valConverter.asBoolean(val);
    }
}