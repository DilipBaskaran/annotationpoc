package com.e5.annotation.processor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface BaseAbsClassBuilder {
    Class<?> classType1 ();
    Class<?> classType2 ();
    boolean flag1 () default false;
    boolean flag2 () default  false;
}
