package com.bin.im.common.converter;

import com.bin.im.common.converter.sql.QueryDataTypeFamily;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;

public abstract class Converter implements Serializable {

    public static final int MAX_DECIMAL_PRECISION = 76;
    public static final MathContext DECIMAL_MATH_CONTEXT = new MathContext(MAX_DECIMAL_PRECISION, RoundingMode.HALF_UP);



    protected static final int ID_BOOLEAN = 0;
    protected static final int ID_BYTE = 1;
    protected static final int ID_SHORT = 2;
    protected static final int ID_INTEGER = 3;
    protected static final int ID_LONG = 4;
    protected static final int ID_BIG_INTEGER = 5;
    protected static final int ID_BIG_DECIMAL = 6;
    protected static final int ID_FLOAT = 7;
    protected static final int ID_DOUBLE = 8;
    protected static final int ID_CHARACTER = 9;
    protected static final int ID_STRING = 10;
    protected static final int ID_DATE = 11;
    protected static final int ID_CALENDAR = 12;
    protected static final int ID_LOCAL_DATE = 13;
    protected static final int ID_LOCAL_TIME = 14;
    protected static final int ID_LOCAL_DATE_TIME = 15;
    protected static final int ID_INSTANT = 16;
    protected static final int ID_OFFSET_DATE_TIME = 17;
    protected static final int ID_ZONED_DATE_TIME = 18;
    protected static final int ID_OBJECT = 19;
    protected static final int ID_NULL = 20;
    protected static final int ID_INTERVAL_YEAR_MONTH = 21;
    protected static final int ID_INTERVAL_DAY_SECOND = 22;
    protected static final int ID_MAP = 23;

    private final int id;
    private final QueryDataTypeFamily typeFamily;

    private final boolean convertToBoolean;
    private final boolean convertToTinyint;
    private final boolean convertToSmallint;
    private final boolean convertToInt;
    private final boolean convertToBigint;
    private final boolean convertToDecimal;
    private final boolean convertToReal;
    private final boolean convertToDouble;
    private final boolean convertToVarchar;
    private final boolean convertToDate;
    private final boolean convertToTime;
    private final boolean convertToTimestamp;
    private final boolean convertToTimestampWithTimezone;
    private final boolean convertToObject;

    protected Converter(int id, QueryDataTypeFamily typeFamily) {
        this.id = id;
        this.typeFamily = typeFamily;

        try {
            Class<? extends Converter> clazz = getClass();

            convertToBoolean = canConvert(clazz.getMethod("asBoolean", Object.class));
            convertToTinyint = canConvert(clazz.getMethod("asTinyint", Object.class));
            convertToSmallint = canConvert(clazz.getMethod("asSmallint", Object.class));
            convertToInt = canConvert(clazz.getMethod("asInt", Object.class));
            convertToBigint = canConvert(clazz.getMethod("asBigint", Object.class));
            convertToDecimal = canConvert(clazz.getMethod("asDecimal", Object.class));
            convertToReal = canConvert(clazz.getMethod("asReal", Object.class));
            convertToDouble = canConvert(clazz.getMethod("asDouble", Object.class));
            convertToVarchar = canConvert(clazz.getMethod("asVarchar", Object.class));
            convertToDate = canConvert(clazz.getMethod("asDate", Object.class));
            convertToTime = canConvert(clazz.getMethod("asTime", Object.class));
            convertToTimestamp = canConvert(clazz.getMethod("asTimestamp", Object.class));
            convertToTimestampWithTimezone = canConvert(clazz.getMethod("asTimestampWithTimezone", Object.class));
            convertToObject = canConvert(clazz.getMethod("asObject", Object.class));
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to initialize converter: " + getClass().getName(), e);
        }
    }

    public final int getId() {
        return id;
    }

    public final QueryDataTypeFamily getTypeFamily() {
        return typeFamily;
    }

    /**
     * @return Class of the value that is handled by this converter.
     */
    public abstract Class<?> getValueClass();

    /**
     * @return Class the value should be converted to as a result of {@link #convertToSelf(Converter, Object)} call.
     */
    public Class<?> getNormalizedValueClass() {
        return getValueClass();
    }

    
    public boolean asBoolean(Object val) {
        throw cannotConvertError(QueryDataTypeFamily.BOOLEAN);
    }

    
    public byte asTinyint(Object val) {
        throw cannotConvertError(QueryDataTypeFamily.TINYINT);
    }

    
    public short asSmallint(Object val) {
        throw cannotConvertError(QueryDataTypeFamily.SMALLINT);
    }

    
    public int asInt(Object val) {
        throw cannotConvertError(QueryDataTypeFamily.INTEGER);
    }

    
    public long asBigint(Object val) {
        throw cannotConvertError(QueryDataTypeFamily.BIGINT);
    }

    
    public BigDecimal asDecimal(Object val) {
        throw cannotConvertError(QueryDataTypeFamily.DECIMAL);
    }

    
    public float asReal(Object val) {
        throw cannotConvertError(QueryDataTypeFamily.REAL);
    }

    
    public double asDouble(Object val) {
        throw cannotConvertError(QueryDataTypeFamily.DOUBLE);
    }

    
    public String asVarchar(Object val) {
        throw cannotConvertError(QueryDataTypeFamily.VARCHAR);
    }

    
    public LocalDate asDate(Object val) {
        throw cannotConvertError(QueryDataTypeFamily.DATE);
    }

    
    public LocalTime asTime(Object val) {
        throw cannotConvertError(QueryDataTypeFamily.TIME);
    }

    
    public LocalDateTime asTimestamp(Object val) {
        throw cannotConvertError(QueryDataTypeFamily.TIMESTAMP);
    }

    
    public OffsetDateTime asTimestampWithTimezone(Object val) {
        throw cannotConvertError(QueryDataTypeFamily.TIMESTAMP_WITH_TIME_ZONE);
    }

    public Object asObject(Object val) {
        return val;
    }

    public final boolean canConvertToBoolean() {
        return convertToBoolean;
    }

    public final boolean canConvertToTinyint() {
        return convertToTinyint;
    }

    public final boolean canConvertToSmallint() {
        return convertToSmallint;
    }

    public final boolean canConvertToInt() {
        return convertToInt;
    }

    public final boolean canConvertToBigint() {
        return convertToBigint;
    }

    public final boolean canConvertToDecimal() {
        return convertToDecimal;
    }

    public final boolean canConvertToReal() {
        return convertToReal;
    }

    public final boolean canConvertToDouble() {
        return convertToDouble;
    }

    public final boolean canConvertToVarchar() {
        return convertToVarchar;
    }

    public final boolean canConvertToDate() {
        return convertToDate;
    }

    public final boolean canConvertToTime() {
        return convertToTime;
    }

    public final boolean canConvertToTimestamp() {
        return convertToTimestamp;
    }

    public final boolean canConvertToTimestampWithTimezone() {
        return convertToTimestampWithTimezone;
    }

    public final boolean canConvertToObject() {
        return convertToObject;
    }

    @SuppressWarnings({"checkstyle:CyclomaticComplexity", "checkstyle:ReturnCount"})
    public final boolean canConvertTo(QueryDataTypeFamily typeFamily) {
         return true;
    }

    public abstract Object convertToSelf(Converter converter, Object val);

    protected final QueryException cannotConvertError(QueryDataTypeFamily target) {
        String message = "Cannot convert " + typeFamily + " to " + target;

        return QueryException.error(ErrorCode.DATA_EXCEPTION, message);
    }

    protected final QueryException numericOverflowError(QueryDataTypeFamily target) {
        String message = "Numeric overflow while converting " + typeFamily + " to " + target;

        return QueryException.error(ErrorCode.DATA_EXCEPTION, message);
    }

    protected final QueryException infiniteValueError(QueryDataTypeFamily target) {
        String message = "Cannot convert infinite " + typeFamily + " to " + target;

        return QueryException.error(ErrorCode.DATA_EXCEPTION, message);
    }

    protected final QueryException nanValueError(QueryDataTypeFamily target) {
        String message = "Cannot convert NaN to " + target;

        return QueryException.error(ErrorCode.DATA_EXCEPTION, message);
    }

    private static boolean canConvert(Method method) {
        return true;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
