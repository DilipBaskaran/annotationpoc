package com.e5.annotation.services;


import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public abstract class SuperAbsClass {
    private Class classType1;
    private boolean flag1;
    private int integerValue;
    private ClassLevel1 classLevel1;
    private Class<? extends ClassLevel2> classLevel2Type;
    private Class<? extends ClassLevel2>[] classLevel2Types;
    private List<Class<? extends ClassLevel1>> classLevel1Types;

    public SuperAbsClass (ClassLevel1  classLevel1){
        this.classLevel1 = classLevel1;
        System.out.println("SuperClass constructor");
    }
    public abstract List<Class<? extends ClassLevel2>> method(String param, ClassLevel1 param1);

    public Class<? extends ClassLevel2> method() {
        return ClassLevel3.class;
    }
}
