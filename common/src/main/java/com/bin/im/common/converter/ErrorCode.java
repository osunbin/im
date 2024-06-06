package com.bin.im.common.converter;

public final class ErrorCode {
    /** Generic error. */
    public static final int GENERIC = -1;

    /** A network connection problem between members, or between a client and a member. */
    public static final int CONNECTION_PROBLEM = 1001;

    /** Query was cancelled due to user request. */
    public static final int CANCELLED_BY_USER = 1003;

    /** Query was cancelled due to timeout. */
    public static final int TIMEOUT = 1004;

    /** A problem with partition distribution. */
    public static final int PARTITION_DISTRIBUTION = 1005;

    /** Map loading is not finished yet. */
    public static final int MAP_LOADING_IN_PROGRESS = 1007;

    /** Generic parsing error. */
    public static final int PARSING = 1008;

    /** An error caused by an attempt to query an index that is not valid. */
    public static final int INDEX_INVALID = 1009;

    /** Object (mapping/table) not found. */
    public static final int OBJECT_NOT_FOUND = 1010;

    /** An error with data conversion or transformation. */
    public static final int DATA_EXCEPTION = 2000;

    private ErrorCode() {
        // No-op.
    }
}
