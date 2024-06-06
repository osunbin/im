package com.bin.im.common.converter;

import com.bin.im.common.internal.utils.Address;

import java.util.UUID;

public final class QueryException extends RuntimeException {

    private final int code;
    private final String suggestion;
    private final UUID originatingMemberId;

    private QueryException(int code, String message, Throwable cause, UUID originatingMemberId) {
        this(code, message, cause, null, originatingMemberId);
    }

    private QueryException(
            int code,
            String message,
            Throwable cause,
            String suggestion,
            UUID originatingMemberId
    ) {
        super(message, cause);

        this.code = code;
        this.suggestion = suggestion;
        this.originatingMemberId = originatingMemberId;
    }

    public static QueryException error(String message) {
        return error(message, null);
    }

    public static QueryException error(String message, Throwable cause) {
        return error(ErrorCode.GENERIC, message, cause);
    }

    public static QueryException error(int code, String message) {
        return new QueryException(code, message, null, null);
    }

    public static QueryException error(int code, String message, Throwable cause) {
        return new QueryException(code, message, cause, null);
    }

    public static QueryException error(int code, String message, Throwable cause, String suggestion) {
        return new QueryException(code, message, cause, suggestion, null);
    }

    public static QueryException error(int code, String message, UUID originatingMemberId) {
        return new QueryException(code, message, null, originatingMemberId);
    }

    public static QueryException error(int code, String message, Throwable cause, UUID originatingMemberId) {
        return new QueryException(code, message, cause, originatingMemberId);
    }

    public static QueryException memberConnection(Address address) {
        return error(ErrorCode.CONNECTION_PROBLEM, "Cluster topology changed while a query was executed: "
                + "Member cannot be reached: " + address);
    }

    public static QueryException clientMemberConnection(UUID clientId) {
        return error(ErrorCode.CONNECTION_PROBLEM, "Client cannot be reached: " + clientId);
    }

    public static QueryException timeout(long timeout) {
        return error(ErrorCode.TIMEOUT, "Query has been cancelled due to a timeout (" + timeout + " ms)");
    }

    public static QueryException cancelledByUser() {
        return error(ErrorCode.CANCELLED_BY_USER, "Query was cancelled by the user");
    }

    public static QueryException dataException(String message, Throwable cause) {
        return error(ErrorCode.DATA_EXCEPTION, message, cause);
    }

    public static QueryException dataException(String message) {
        return dataException(message, null);
    }

    /**
     * @return Code of the exception.
     */
    public int getCode() {
        return code;
    }

    /**
     * @return Suggested SQL statement to remediate experienced error.
     */
    public String getSuggestion() {
        return suggestion;
    }

    /**
     * Get originator of the exception.
     *
     * @return ID of the member where the exception occurred or {@code null} if the exception was raised on a local member
     *         and is not propagated yet.
     */
    public UUID getOriginatingMemberId() {
        return originatingMemberId;
    }


    public QueryException wrap() {
        return new QueryException(code, getMessage(), this, suggestion, originatingMemberId);
    }
}