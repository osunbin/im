package com.bin.im.common.converter;

import com.bin.im.common.converter.sql.QueryDataTypeFamily;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;

/**
 * Converter for {@link LocalDateTime} type.
 */
public final class LocalDateTimeConverter extends AbstractTemporalConverter {

    public static final LocalDateTimeConverter INSTANCE = new LocalDateTimeConverter();

    private LocalDateTimeConverter() {
        super(ID_LOCAL_DATE_TIME, QueryDataTypeFamily.TIMESTAMP);
    }

    @Override
    public Class<?> getValueClass() {
        return LocalDateTime.class;
    }

    @Override
    public String asVarchar(Object val) {
        return cast(val).toString();
    }

    @Override
    public LocalDate asDate(Object val) {
        return cast(val).toLocalDate();
    }

    @Override
    public LocalTime asTime(Object val) {
        return cast(val).toLocalTime();
    }

    @Override
    public LocalDateTime asTimestamp(Object val) {
        return cast(val);
    }

    @Override
    public OffsetDateTime asTimestampWithTimezone(Object val) {
        return timestampToTimestampWithTimezone(cast(val));
    }

    @Override
    public Object convertToSelf(Converter valConverter, Object val) {
        return valConverter.asTimestamp(val);
    }

    private LocalDateTime cast(Object val) {
        return ((LocalDateTime) val);
    }
}