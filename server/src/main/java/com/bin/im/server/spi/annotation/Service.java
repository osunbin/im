package com.bin.im.server.spi.annotation;

import com.bin.im.server.common.type.ServiceType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.bin.im.server.common.type.ServiceType.NORMAL;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PACKAGE, ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
public @interface Service {

    String name() default "";

    ServiceType type() default NORMAL;

    String module() default "";

    String group() default "";

    String url() default "";

}
