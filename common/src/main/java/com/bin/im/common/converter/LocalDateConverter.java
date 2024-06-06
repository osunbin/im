package com.bin.im.common.converter;

import com.bin.im.common.converter.sql.QueryDataTypeFamily;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

/**
 * Converter for {@link LocalDate} type.
 */
public final class LocalDateConverter extends AbstractTemporalConverter {

    public static final LocalDateConverter INSTANCE = new LocalDateConverter();

    private LocalDateConverter() {
        super(ID_LOCAL_DATE, QueryDataTypeFamily.DATE);
    }

    @Override
    public Class<?> getValueClass() {
        return LocalDate.class;
    }

    @Override
    public String asVarchar(Object val) {
        return cast(val).toString();
    }

    @Override
    public LocalDate asDate(Object val) {
        return cast(val);
    }

    @Override
    public LocalDateTime asTimestamp(Object val) {
        LocalDate date = cast(val);

        return dateToTimestamp(date);
    }

    @Override
    public OffsetDateTime asTimestampWithTimezone(Object val) {
        LocalDate date = cast(val);

        LocalDateTime timestamp = dateToTimestamp(date);

        return timestampToTimestampWithTimezone(timestamp);
    }

    @Override
    public Object convertToSelf(Converter valConverter, Object val) {
        return valConverter.asDate(val);
    }

    private LocalDate cast(Object val) {
        return ((LocalDate) val);
    }
}
