package com.e5.annotation.processor;

import com.e5.annotation.services.ClassLevel1;
import com.e5.annotation.services.ClassLevel2;
import com.e5.annotation.services.ClassLevel3;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface BaseAbsClassBuilder {
    Class<?> classType1 ();
    boolean flag1 () default false;
    int integerValue();
    Class<? extends ClassLevel2> classLevel2Type();
    Class<? extends ClassLevel2>[] classLevel2Types();
    Class<? extends ClassLevel1>[] classLevel1Types();
    Class<? extends ClassLevel2>[] method();

}
