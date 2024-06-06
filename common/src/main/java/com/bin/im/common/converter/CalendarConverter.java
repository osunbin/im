package com.bin.im.common.converter;

import java.time.OffsetDateTime;
import java.util.Calendar;

/**
 * Converter for {@link java.util.Calendar} type.
 */
public final class CalendarConverter extends AbstractTimestampWithTimezoneConverter {

    public static final CalendarConverter INSTANCE = new CalendarConverter();

    private CalendarConverter() {
        super(ID_CALENDAR);
    }

    @Override
    public Class<?> getValueClass() {
        return Calendar.class;
    }

    @Override
    public OffsetDateTime asTimestampWithTimezone(Object val) {
        Calendar c = (Calendar) val;
        return OffsetDateTime.ofInstant(c.toInstant(), c.getTimeZone().toZoneId());
    }
}
