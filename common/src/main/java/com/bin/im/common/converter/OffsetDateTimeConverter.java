package com.bin.im.common.converter;

import java.time.OffsetDateTime;

/**
 * Converter for {@link OffsetDateTime} type.
 */
public final class OffsetDateTimeConverter extends AbstractTimestampWithTimezoneConverter {

    public static final OffsetDateTimeConverter INSTANCE = new OffsetDateTimeConverter();

    private OffsetDateTimeConverter() {
        super(ID_OFFSET_DATE_TIME);
    }

    @Override
    public Class<?> getValueClass() {
        return OffsetDateTime.class;
    }

    @Override
    public OffsetDateTime asTimestampWithTimezone(Object val) {
        return (OffsetDateTime) val;
    }
}
