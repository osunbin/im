package com.bin.im.common.converter;

import com.bin.im.common.converter.sql.QueryDataTypeFamily;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;

/**
 * Common converter class for TIMESTAMP WITH TIMEZONE type.
 */
public abstract class AbstractTimestampWithTimezoneConverter extends AbstractTemporalConverter {
    protected AbstractTimestampWithTimezoneConverter(int id) {
        super(id, QueryDataTypeFamily.TIMESTAMP_WITH_TIME_ZONE);
    }

    @Override
    public Class<?> getNormalizedValueClass() {
        return OffsetDateTime.class;
    }

    @Override
    public final String asVarchar(Object val) {
        return asTimestampWithTimezone(val).toString();
    }

    @Override
    public final LocalDate asDate(Object val) {
        return asTimestamp(val).toLocalDate();
    }

    @Override
    public final LocalTime asTime(Object val) {
        return asTimestamp(val).toLocalTime();
    }

    @Override
    public final LocalDateTime asTimestamp(Object val) {
        return timestampWithTimezoneToTimestamp(asTimestampWithTimezone(val));
    }

    @Override
    public final Object asObject(Object val) {
        return asTimestampWithTimezone(val);
    }

    @Override
    public final Object convertToSelf(Converter valConverter, Object val) {
        return valConverter.asTimestampWithTimezone(val);
    }
}
