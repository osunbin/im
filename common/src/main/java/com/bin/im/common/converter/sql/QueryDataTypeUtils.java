package com.bin.im.common.converter.sql;

public final class QueryDataTypeUtils {
    /**
     * java.lang.String footprint:
     * COUNT       AVG       SUM   DESCRIPTION
     *     1        32        32   [C
     *     1        24        24   java.lang.String
     *     2                  56   (total)
     */
    public static final int TYPE_LEN_VARCHAR = 56;

    /**
     * java.math.BigDecimal footprint:
     * COUNT       AVG       SUM   DESCRIPTION
     *     1        32        32   [I
     *     1        40        40   java.math.BigDecimal
     *     1        40        40   java.math.BigInteger
     *     3                 112   (total)
     */
    public static final int TYPE_LEN_DECIMAL = 112;

    /**
     * java.time.LocalTime footprint:
     * COUNT       AVG       SUM   DESCRIPTION
     *     1        24        24   java.time.LocalTime
     *     1                  24   (total)
     */
    public static final int TYPE_LEN_TIME = 24;

    /**
     * java.time.LocalDate footprint:
     * COUNT       AVG       SUM   DESCRIPTION
     *     1        24        24   java.time.LocalDate
     *     1                  24   (total)
     */
    public static final int TYPE_LEN_DATE = 24;

    /**
     * java.time.LocalDateTime footprint:
     * COUNT       AVG       SUM   DESCRIPTION
     *     1        24        24   java.time.LocalDate
     *     1        24        24   java.time.LocalDateTime
     *     1        24        24   java.time.LocalTime
     *     3                  72   (total)
     */
    public static final int TYPE_LEN_TIMESTAMP = 72;

    /**
     * java.time.OffsetDateTime footprint:
     * COUNT       AVG       SUM   DESCRIPTION
     *     1        24        24   [C
     *     1        24        24   java.lang.String
     *     1        24        24   java.time.LocalDate
     *     1        24        24   java.time.LocalDateTime
     *     1        24        24   java.time.LocalTime
     *     1        24        24   java.time.OffsetDateTime
     *     1        24        24   java.time.ZoneOffset
     *     7                 168   (total)
     */
    public static final int TYPE_LEN_TIMESTAMP_WITH_TIME_ZONE = 168;

    /**
     * com.hazelcast.sql.impl.type.SqlYearMonthInterval footprint:
     * COUNT       AVG       SUM   DESCRIPTION
     *     1        16        16   com.hazelcast.sql.impl.type.SqlYearMonthInterval
     *     1                  16   (total)
     */
    public static final int TYPE_LEN_INTERVAL_YEAR_MONTH = 16;

    /**
     * com.hazelcast.sql.impl.type.SqlDaySecondInterval footprint:
     * COUNT       AVG       SUM   DESCRIPTION
     *     1        24        24   com.hazelcast.sql.impl.type.SqlDaySecondInterval
     *     1                  24   (total)
     */
    public static final int TYPE_LEN_INTERVAL_DAY_SECOND = 24;

    /**
     * java.util.HashMap footprint:
     * COUNT       AVG       SUM   DESCRIPTION
     *     1        48        48   java.util.HashMap
     *     1                  48   (total)
     *
     * Empty map.
     */
    public static final int TYPE_LEN_MAP = 48;

    /** 12 (hdr) + 36 (arbitrary content). */
    public static final int TYPE_LEN_OBJECT = 12 + 36;

    // With a non-zero value we avoid weird zero-cost columns. Technically, it
    // still costs a single reference now, but reference cost is not taken into
    // account as of now.
    public static final int TYPE_LEN_NULL = 1;

    public static final int PRECEDENCE_NULL = 0;
    public static final int PRECEDENCE_VARCHAR = 100;
    public static final int PRECEDENCE_BOOLEAN = 200;
    public static final int PRECEDENCE_TINYINT = 300;
    public static final int PRECEDENCE_SMALLINT = 400;
    public static final int PRECEDENCE_INTEGER = 500;
    public static final int PRECEDENCE_BIGINT = 600;
    public static final int PRECEDENCE_DECIMAL = 700;
    public static final int PRECEDENCE_REAL = 800;
    public static final int PRECEDENCE_DOUBLE = 900;
    public static final int PRECEDENCE_TIME = 1000;
    public static final int PRECEDENCE_DATE = 1100;
    public static final int PRECEDENCE_TIMESTAMP = 1200;
    public static final int PRECEDENCE_TIMESTAMP_WITH_TIME_ZONE = 1300;
    public static final int PRECEDENCE_OBJECT = 1400;
    public static final int PRECEDENCE_INTERVAL_YEAR_MONTH = 10;
    public static final int PRECEDENCE_INTERVAL_DAY_SECOND = 20;
    public static final int PRECEDENCE_MAP = 30;

    private QueryDataTypeUtils() {
        // No-op.
    }

    public static boolean isNumeric(QueryDataTypeFamily typeFamily) {
        switch (typeFamily) {
            case TINYINT:
            case SMALLINT:
            case INTEGER:
            case BIGINT:
            case DECIMAL:
            case REAL:
            case DOUBLE:
                return true;

            default:
                return false;
        }
    }
}
