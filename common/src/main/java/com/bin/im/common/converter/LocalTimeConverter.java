package com.bin.im.common.converter;

import com.bin.im.common.converter.sql.QueryDataTypeFamily;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;

/**
 * Converter for {@link java.time.LocalTime} type.
 */
public final class LocalTimeConverter extends AbstractTemporalConverter {

    public static final LocalTimeConverter INSTANCE = new LocalTimeConverter();

    private LocalTimeConverter() {
        super(ID_LOCAL_TIME, QueryDataTypeFamily.TIME);
    }

    @Override
    public Class<?> getValueClass() {
        return LocalTime.class;
    }

    @Override
    public String asVarchar(Object val) {
        return cast(val).toString();
    }

    @Override
    public LocalTime asTime(Object val) {
        return cast(val);
    }

    @Override
    public LocalDateTime asTimestamp(Object val) {
        LocalTime time = cast(val);

        return timeToTimestamp(time);
    }

    @Override
    public OffsetDateTime asTimestampWithTimezone(Object val) {
        LocalTime time = cast(val);

        LocalDateTime timestamp = timeToTimestamp(time);

        return timestampToTimestampWithTimezone(timestamp);
    }

    @Override
    public Object convertToSelf(Converter valConverter, Object val) {
        return valConverter.asTime(val);
    }

    private LocalTime cast(Object val) {
        return ((LocalTime) val);
    }
}
