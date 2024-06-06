package com.bin.im.common.converter;

import com.bin.im.common.converter.sql.QueryDataTypeFamily;

import java.math.BigDecimal;

/**
 * Converter for {@link java.lang.Byte} type.
 */
public final class ByteConverter extends Converter {

    public static final ByteConverter INSTANCE = new ByteConverter();

    private ByteConverter() {
        super(ID_BYTE, QueryDataTypeFamily.TINYINT);
    }

    @Override
    public Class<?> getValueClass() {
        return Byte.class;
    }

    @Override
    public byte asTinyint(Object val) {
        return cast(val);
    }

    @Override
    public short asSmallint(Object val) {
        return cast(val);
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
        return Byte.toString(cast(val));
    }

    @Override
    public Object convertToSelf(Converter valConverter, Object val) {
        return valConverter.asTinyint(val);
    }

    private byte cast(Object val) {
        return (byte) val;
    }
}