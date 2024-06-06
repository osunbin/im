package com.bin.im.common.converter;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

/**
 * Converter for {@link ZonedDateTime} type.
 */
public final class ZonedDateTimeConverter extends AbstractTimestampWithTimezoneConverter {

    public static final ZonedDateTimeConverter INSTANCE = new ZonedDateTimeConverter();

    private ZonedDateTimeConverter() {
        super(ID_ZONED_DATE_TIME);
    }

    @Override
    public Class<?> getValueClass() {
        return ZonedDateTime.class;
    }

    @Override
    public OffsetDateTime asTimestampWithTimezone(Object val) {
        return ((ZonedDateTime) val).toOffsetDateTime();
    }
}
