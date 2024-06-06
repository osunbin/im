package com.bin.im.common.internal.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.WrongMethodTypeException;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;


public final class ExceptionUtil {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionUtil.class);


    private static final String EXCEPTION_SEPARATOR = "------ submitted from ------";
    private static final MethodHandles.Lookup LOOKUP = MethodHandles.publicLookup();
    // new Throwable(String message, Throwable cause)
    private static final MethodType MT_INIT_STRING_THROWABLE = MethodType.methodType(void.class, String.class, Throwable.class);
    // new Throwable(Throwable cause)
    private static final MethodType MT_INIT_THROWABLE = MethodType.methodType(void.class, Throwable.class);
    // new Throwable(String message)
    private static final MethodType MT_INIT_STRING = MethodType.methodType(void.class, String.class);
    // new Throwable()
    private static final MethodType MT_INIT = MethodType.methodType(void.class);

    private static final BiFunction<Throwable, String, RuntimeException> HAZELCAST_EXCEPTION_WRAPPER = (throwable, message) -> {
        if (message != null) {
            return new RuntimeException(message, throwable);
        } else {
            return new RuntimeException(throwable);
        }
    };

    private ExceptionUtil() {
    }

    /**
     * Converts a Throwable stacktrace to a String.
     *
     * @param cause the Throwable
     * @return the String.
     */
    public static String toString(Throwable cause) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        cause.printStackTrace(pw);
        return sw.toString();
    }

    public static RuntimeException peel(final Throwable t) {
        return (RuntimeException) peel(t, null, null, HAZELCAST_EXCEPTION_WRAPPER);
    }

    /**
     * Processes {@code Throwable t} so that the returned {@code Throwable}'s type matches {@code allowedType} or
     * {@code RuntimeException}. Processing may include unwrapping {@code t}'s cause hierarchy, wrapping it in a
     * {@code HazelcastException} or just returning the same instance {@code t} if it is already an instance of
     * {@code RuntimeException}.
     *
     * @param t           {@code Throwable} to be peeled
     * @param allowedType the type expected to be returned; when {@code null}, this method returns instances
     *                    of {@code RuntimeException}
     * @param message     if not {@code null}, used as the message in the {@code HazelcastException} that
     *                    may wrap the peeled {@code Throwable}
     * @param <T>         expected type of {@code Throwable}
     * @return the peeled {@code Throwable}
     */
    public static <T extends Throwable> Throwable peel(final Throwable t, Class<T> allowedType, String message) {
        return peel(t, allowedType, message, HAZELCAST_EXCEPTION_WRAPPER);
    }

    /**
     * Processes {@code Throwable t} so that the returned {@code Throwable}'s type matches {@code allowedType},
     * {@code RuntimeException} or any {@code Throwable} returned by `exceptionWrapper`
     * Processing may include unwrapping {@code t}'s cause hierarchy, wrapping it in a exception
     * created by using exceptionWrapper or just returning the same instance {@code t}
     * if it is already an instance of {@code RuntimeException}.
     *
     * @param t                {@code Throwable} to be peeled
     * @param allowedType      the type expected to be returned; when {@code null}, this method returns instances
     *                         of {@code RuntimeException} or <W>
     * @param message          if not {@code null}, used as the message in {@code RuntimeException} that
     *                         may wrap the peeled {@code Throwable}
     * @param exceptionWrapper wraps the peeled code using this exceptionWrapper
     * @param <W>              Type of the wrapper exception in exceptionWrapper
     * @param <T>              allowed type of {@code Throwable}
     * @return the peeled {@code Throwable}
     */
    public static <T, W extends Throwable> Throwable peel(final Throwable t, Class<T> allowedType,
                                                          String message, BiFunction<Throwable, String, W> exceptionWrapper) {
        if (t instanceof RuntimeException) {
            return t;
        }

        if (t instanceof ExecutionException || t instanceof InvocationTargetException) {
            final Throwable cause = t.getCause();
            if (cause != null) {
                return peel(cause, allowedType, message, exceptionWrapper);
            } else {
                return exceptionWrapper.apply(t, message);
            }
        }

        if (allowedType != null && allowedType.isAssignableFrom(t.getClass())) {
            return t;
        }

        return exceptionWrapper.apply(t, message);
    }

    public static RuntimeException rethrow(Throwable t) {
        rethrowIfError(t);
        throw peel(t);
    }

    public static RuntimeException rethrow(Throwable t, BiFunction<Throwable, String, RuntimeException> exceptionWrapper) {
        rethrowIfError(t);
        throw (RuntimeException) peel(t, null, null, exceptionWrapper);
    }

    public static <T extends Throwable> RuntimeException rethrow(final Throwable t, Class<T> allowedType) throws T {
        rethrowIfError(t);
        throw (T) peel(t, allowedType, null);
    }

    /**
     * This rethrow the exception providing an allowed Exception in first priority, even it is a Runtime exception
     */
    public static <T extends Throwable> RuntimeException rethrowAllowedTypeFirst(final Throwable t,
                                                                                 Class<T> allowedType) throws T {
        rethrowIfError(t);
        if (allowedType.isAssignableFrom(t.getClass())) {
            throw (T) t;
        } else {
            throw peel(t);
        }
    }

    public static void rethrowIfError(final Throwable t) {
        if (t instanceof Error) {
            throw (Error) t;
        }
    }

    public static RuntimeException rethrowAllowInterrupted(final Throwable t) throws InterruptedException {
        return rethrow(t, InterruptedException.class);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Throwable> RuntimeException sneakyThrow( Throwable t) throws T {
        throw (T) t;
    }

    /**
     * Utility to make sure exceptions inside
     * {@link java.util.concurrent.CompletionStage#whenComplete(BiConsumer)} are not swallowed.
     * Exceptions will be caught and logged using the supplied logger.
     */

    public static <T> BiConsumer<T, ? super Throwable> withTryCatch(BiConsumer<T, ? super Throwable> consumer
    ) {
        return withTryCatch("Exception during callback", consumer);
    }

    /**
     * Utility to make sure exceptions inside
     * {@link java.util.concurrent.CompletionStage#whenComplete(BiConsumer)} are not swallowed.
     * Exceptions will be caught and logged using the supplied logger and message.
     */

    public static <T> BiConsumer<T, ? super Throwable> withTryCatch(String message, BiConsumer<T, ? super Throwable> consumer
    ) {
        return (r, t) -> {
            try {
                consumer.accept(r, t);
            } catch (Throwable e) {
                logger.error(message, e);
            }
        };
    }

    /**
     * Tries to create the exception with appropriate constructor in the following order.
     * In all cases the cause is set (via constructor or via {@code initCause})
     * new Throwable(String message, Throwable cause)
     * new Throwable(Throwable cause)
     * new Throwable(String message)
     * new Throwable()
     *
     * @param exceptionClass class of the exception
     * @param message        message to be pass to constructor of the exception
     * @param cause          cause to be set to the exception
     * @return {@code null} if can not find a constructor as described above, otherwise returns newly constructed exception
     */
    @SuppressWarnings("checkstyle:npathcomplexity")
    public static <T extends Throwable> T tryCreateExceptionWithMessageAndCause(Class<? extends Throwable> exceptionClass,
                                                                                String message,  Throwable cause) {
        MethodHandle constructor;
        try {
            constructor = LOOKUP.findConstructor(exceptionClass, MT_INIT_STRING_THROWABLE);
            T clone = (T) constructor.invokeWithArguments(message, cause);
            return clone;
        } catch (ClassCastException | WrongMethodTypeException
                | IllegalAccessException | SecurityException | NoSuchMethodException ignored) {
        } catch (Throwable t) {
            throw new RuntimeException("Exception creation failed ", t);
        }
        try {
            constructor = LOOKUP.findConstructor(exceptionClass, MT_INIT_THROWABLE);
            T clone = (T) constructor.invokeWithArguments(cause);
            return clone;
        } catch (ClassCastException | WrongMethodTypeException
                | IllegalAccessException | SecurityException | NoSuchMethodException ignored) {
        } catch (Throwable t) {
            throw new RuntimeException("Exception creation failed ", t);
        }
        try {
            constructor = LOOKUP.findConstructor(exceptionClass, MT_INIT_STRING);
            T clone = (T) constructor.invokeWithArguments(message);
            clone.initCause(cause);
            return clone;
        } catch (ClassCastException | WrongMethodTypeException
                | IllegalAccessException | SecurityException | NoSuchMethodException ignored) {
        } catch (Throwable t) {
            throw new RuntimeException("Exception creation failed ", t);
        }
        try {
            constructor = LOOKUP.findConstructor(exceptionClass, MT_INIT);
            T clone = (T) constructor.invokeWithArguments();
            clone.initCause(cause);
            return clone;
        } catch (ClassCastException | WrongMethodTypeException
                | IllegalAccessException | SecurityException | NoSuchMethodException ignored) {
        } catch (Throwable t) {
            throw new RuntimeException("Exception creation failed ", t);
        }
        return null;
    }

    /**
     * @param original exception to be cloned with fixed stack trace
     * @param <T>      type of the original exception
     * @return a cloned exception with the current stacktrace is added on top of the original exceptions stack-trace
     * the cloned exception has the same cause and the message as the original exception
     */
    public static <T extends Throwable> T cloneExceptionWithFixedAsyncStackTrace(T original) {
        StackTraceElement[] fixedStackTrace = getFixedStackTrace(original, Thread.currentThread().getStackTrace());
        Class<? extends Throwable> exceptionClass = original.getClass();
        Throwable clone = tryCreateExceptionWithMessageAndCause(exceptionClass, original.getMessage(), original.getCause());
        if (clone != null) {
            clone.setStackTrace(fixedStackTrace);
            return (T) clone;
        }
        return null;
    }

    private static StackTraceElement[] getFixedStackTrace(Throwable throwable, StackTraceElement[] localSideStackTrace) {
        StackTraceElement[] remoteStackTrace = throwable.getStackTrace();
        StackTraceElement[] newStackTrace = new StackTraceElement[localSideStackTrace.length + remoteStackTrace.length];
        System.arraycopy(remoteStackTrace, 0, newStackTrace, 0, remoteStackTrace.length);
        newStackTrace[remoteStackTrace.length] = new StackTraceElement(EXCEPTION_SEPARATOR, "", "", -1);
        System.arraycopy(localSideStackTrace, 1, newStackTrace, remoteStackTrace.length + 1, localSideStackTrace.length - 1);
        return newStackTrace;
    }
}
