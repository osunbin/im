package com.bin.im.common.converter;

import com.bin.im.common.converter.sql.QueryDataTypeFamily;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Converter for {@link java.math.BigInteger} type.
 */
public final class BigIntegerConverter extends AbstractDecimalConverter {

    public static final BigIntegerConverter INSTANCE = new BigIntegerConverter();

    private BigIntegerConverter() {
        super(ID_BIG_INTEGER);
    }

    @Override
    public Class<?> getValueClass() {
        return BigInteger.class;
    }

    @Override
    public byte asTinyint(Object val) {
        BigInteger casted = cast(val);
        try {
            return casted.byteValueExact();
        } catch (ArithmeticException e) {
            throw numericOverflowError(QueryDataTypeFamily.TINYINT);
        }
    }

    @Override
    public short asSmallint(Object val) {
        BigInteger casted = cast(val);
        try {
            return casted.shortValueExact();
        } catch (ArithmeticException e) {
            throw numericOverflowError(QueryDataTypeFamily.SMALLINT);
        }
    }

    @Override
    public int asInt(Object val) {
        BigInteger casted = cast(val);
        try {
            return casted.intValueExact();
        } catch (ArithmeticException e) {
            throw numericOverflowError(QueryDataTypeFamily.INTEGER);
        }
    }

    @Override
    public long asBigint(Object val) {
        BigInteger casted = cast(val);
        try {
            return casted.longValueExact();
        } catch (ArithmeticException e) {
            throw numericOverflowError(QueryDataTypeFamily.BIGINT);
        }
    }

    @Override
    public BigDecimal asDecimal(Object val) {
        return new BigDecimal(cast(val), DECIMAL_MATH_CONTEXT);
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

    private BigInteger cast(Object val) {
        return (BigInteger) val;
    }

}
