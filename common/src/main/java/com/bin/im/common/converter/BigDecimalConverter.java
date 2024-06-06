package com.bin.im.common.converter;

import com.bin.im.common.converter.sql.QueryDataTypeFamily;

import java.math.BigDecimal;

/**
 * Converter for {@link java.math.BigDecimal} type.
 */
public final class BigDecimalConverter extends AbstractDecimalConverter {

    public static final BigDecimalConverter INSTANCE = new BigDecimalConverter();

    private BigDecimalConverter() {
        super(ID_BIG_DECIMAL);
    }

    @Override
    public Class<?> getValueClass() {
        return BigDecimal.class;
    }

    @Override
    public byte asTinyint(Object val) {
        BigDecimal casted = cast(val);
        try {
            return casted.setScale(0, BigDecimal.ROUND_DOWN).byteValueExact();
        } catch (ArithmeticException e) {
            throw numericOverflowError(QueryDataTypeFamily.TINYINT);
        }
    }

    @Override
    public short asSmallint(Object val) {
        BigDecimal casted = cast(val);
        try {
            return casted.setScale(0, BigDecimal.ROUND_DOWN).shortValueExact();
        } catch (ArithmeticException e) {
            throw numericOverflowError(QueryDataTypeFamily.SMALLINT);
        }
    }

    @Override
    public int asInt(Object val) {
        BigDecimal casted = cast(val);
        try {
            return casted.setScale(0, BigDecimal.ROUND_DOWN).intValueExact();
        } catch (ArithmeticException e) {
            throw numericOverflowError(QueryDataTypeFamily.INTEGER);
        }
    }

    @Override
    public long asBigint(Object val) {
        BigDecimal casted = cast(val);
        try {
            return casted.setScale(0, BigDecimal.ROUND_DOWN).longValueExact();
        } catch (ArithmeticException e) {
            throw numericOverflowError(QueryDataTypeFamily.BIGINT);
        }
    }

    @Override
    public BigDecimal asDecimal(Object val) {
        return cast(val);
    }

    @Override
    public float asReal(Object val) {
        return cast(val).floatValue();
    }

    @Override
    public double asDouble(Object val) {
        return cast(val).doubleValue();
    }

    @Override
    public String asVarchar(Object val) {
        return cast(val).toString();
    }

    private BigDecimal cast(Object val) {
        return (BigDecimal) val;
    }

}
