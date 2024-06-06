package com.bin.im.common.converter;

import com.bin.im.common.converter.sql.QueryDataTypeFamily;

import java.math.BigDecimal;

/**
 * Converter for {@link java.lang.Integer} type.
 */
public final class IntegerConverter extends Converter {

    public static final IntegerConverter INSTANCE = new IntegerConverter();

    private IntegerConverter() {
        super(ID_INTEGER, QueryDataTypeFamily.INTEGER);
    }

    @Override
    public Class<?> getValueClass() {
        return Integer.class;
    }

    @Override
    public byte asTinyint(Object val) {
        int casted = cast(val);
        byte converted = (byte) casted;

        if (converted != casted) {
            throw numericOverflowError(QueryDataTypeFamily.TINYINT);
        }

        return converted;
    }

    @Override
    public short asSmallint(Object val) {
        int casted = cast(val);
        short converted = (short) casted;

        if (converted != casted) {
            throw numericOverflowError(QueryDataTypeFamily.SMALLINT);
        }

        return converted;
    }

    @Override
    public int asInt(Object val) {
        return cast(val);
    }

    @Override
    public long asBigint(Object val) {
        return cast(val);
    }

    @Override
    public BigDecimal asDecimal(Object val) {
        return new BigDecimal(cast(val), DECIMAL_MATH_CONTEXT);
    }

    @Override
    public float asReal(Object val) {
        return cast(val);
    }

    @Override
    public double asDouble(Object val) {
        return cast(val);
    }

    @Override
    public String asVarchar(Object val) {
        return Integer.toString(cast(val));
    }

    @Override
    public Object convertToSelf(Converter valConverter, Object val) {
        return valConverter.asInt(val);
    }

    private int cast(Object val) {
        return (int) val;
    }

}
