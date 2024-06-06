package com.bin.im.common.converter;

import java.time.OffsetDateTime;
import java.util.Date;

/**
 * Converter for {@link java.util.Date} type.
 */
public final class DateConverter extends AbstractTimestampWithTimezoneConverter {

    public static final DateConverter INSTANCE = new DateConverter();

    private DateConverter() {
        super(ID_DATE);
    }

    @Override
    public Class<?> getValueClass() {
        return Date.class;
    }

    @Override
    public OffsetDateTime asTimestampWithTimezone(Object val) {
        return OffsetDateTime.ofInstant(((Date) val).toInstant(), DEFAULT_ZONE);
    }
}
