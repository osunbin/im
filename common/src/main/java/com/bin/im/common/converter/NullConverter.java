package com.bin.im.common.converter;

import com.bin.im.common.converter.sql.QueryDataTypeFamily;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;

public final class NullConverter extends Converter {

    public static final NullConverter INSTANCE = new NullConverter();

    private NullConverter() {
        super(ID_NULL, QueryDataTypeFamily.NULL);
    }

    @Override
    public Class<?> getValueClass() {
        return Void.class;
    }

    @Override
    public boolean asBoolean(Object val) {
        throw new UnsupportedOperationException("must never be called");
    }

    @Override
    public byte asTinyint(Object val) {
        throw new UnsupportedOperationException("must never be called");
    }

    @Override
    public short asSmallint(Object val) {
        throw new UnsupportedOperationException("must never be called");
    }

    @Override
    public int asInt(Object val) {
        throw new UnsupportedOperationException("must never be called");
    }

    @Override
    public long asBigint(Object val) {
        throw new UnsupportedOperationException("must never be called");
    }

    @Override
    public BigDecimal asDecimal(Object val) {
        throw new UnsupportedOperationException("must never be called");
    }

    @Override
    public float asReal(Object val) {
        throw new UnsupportedOperationException("must never be called");
    }

    @Override
    public double asDouble(Object val) {
        throw new UnsupportedOperationException("must never be called");
    }

    @Override
    public String asVarchar(Object val) {
        throw new UnsupportedOperationException("must never be called");
    }

    @Override
    public LocalDate asDate(Object val) {
        throw new UnsupportedOperationException("must never be called");
    }

    @Override
    public LocalTime asTime(Object val) {
        throw new UnsupportedOperationException("must never be called");
    }

    @Override
    public LocalDateTime asTimestamp(Object val) {
        throw new UnsupportedOperationException("must never be called");
    }

    @Override
    public OffsetDateTime asTimestampWithTimezone(Object val) {
        throw new UnsupportedOperationException("must never be called");
    }

    @Override
    public Object asObject(Object val) {
        throw new UnsupportedOperationException("must never be called");
    }

    @Override
    public Object convertToSelf(Converter converter, Object value) {
        throw new UnsupportedOperationException("must never be called");
    }

}
