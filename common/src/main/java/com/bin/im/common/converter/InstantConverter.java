package com.bin.im.common.converter;

import java.time.Instant;
import java.time.OffsetDateTime;

/**
 * Converter for {@link Instant} type.
 */
public final class InstantConverter extends AbstractTimestampWithTimezoneConverter {

    public static final InstantConverter INSTANCE = new InstantConverter();

    private InstantConverter() {
        super(ID_INSTANT);
    }

    @Override
    public Class<?> getValueClass() {
        return Instant.class;
    }

    @Override
    public OffsetDateTime asTimestampWithTimezone(Object val) {
        return OffsetDateTime.ofInstant(((Instant) val), DEFAULT_ZONE);
    }
}
