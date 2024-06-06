package com.bin.im.common.converter;

import com.bin.im.common.converter.sql.QueryDataTypeFamily;

import java.util.Map;

/**
 * Converter for {@link java.util.Map} type.
 */
public class MapConverter extends Converter {

    public static final MapConverter INSTANCE = new MapConverter();

    public MapConverter() {
        super(ID_MAP, QueryDataTypeFamily.MAP);
    }

    @Override
    public Class<?> getValueClass() {
        return Map.class;
    }

    @Override
    public Object convertToSelf(Converter converter, Object val) {
        Object value = converter.asObject(val);

        if (value == null) {
            return null;
        }

        if (value instanceof Map) {
            return value;
        }

        throw converter.cannotConvertError(converter.getTypeFamily());
    }
}