package com.bin.im.common.converter;

import com.bin.im.common.converter.sql.QueryDataTypeFamily;

import java.math.BigDecimal;

public abstract class AbstractDecimalConverter extends Converter {
    protected AbstractDecimalConverter(int id) {
        super(id, QueryDataTypeFamily.DECIMAL);
    }

    @Override
    public Class<?> getNormalizedValueClass() {
        return BigDecimal.class;
    }

    @Override
    public final Object asObject(Object val) {
        return asDecimal(val);
    }

    @Override
    public final Object convertToSelf(Converter valConverter, Object val) {
        return valConverter.asDecimal(val);
    }
}