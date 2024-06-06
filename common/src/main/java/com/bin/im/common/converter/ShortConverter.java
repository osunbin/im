package com.bin.im.common.converter;

import com.bin.im.common.converter.sql.QueryDataTypeFamily;

import java.math.BigDecimal;

/**
 * Converter for {@link java.lang.Short} type.
 */
public final class ShortConverter extends Converter {

    public static final ShortConverter INSTANCE = new ShortConverter();

    private ShortConverter() {
        super(ID_SHORT, QueryDataTypeFamily.SMALLINT);
    }

    @Override
    public Class<?> getValueClass() {
        return Short.class;
    }

    @Override
    public byte asTinyint(Object val) {
        short casted = cast(val);
        byte converted = (byte) casted;

        if (converted != casted) {
            throw numericOverflowError(QueryDataTypeFamily.TINYINT);
        }

        return converted;
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
        return Short.toString(cast(val));
    }

    @Override
    public Object convertToSelf(Converter valConverter, Object val) {
        return valConverter.asSmallint(val);
    }

    private short cast(Object val) {
        return (short) val;
    }

}
