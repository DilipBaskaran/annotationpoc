package com.e5;

import com.e5.annotation.processor.BaseAbsClassBuilder;
import com.e5.annotation.services.ClassLevel1;
import com.e5.annotation.services.ClassLevel2;
import com.e5.annotation.services.ClassLevel3;

@BaseAbsClassBuilder(classType1 = String.class,
        flag1 = false,
        integerValue = 100,
        classLevel2Type = ClassLevel2.class,
        classLevel2Types = {ClassLevel2.class, ClassLevel3.class},
        classLevel1Types = {ClassLevel1.class, ClassLevel2.class, ClassLevel3.class},
        method = {ClassLevel2.class, ClassLevel3.class})
public class SubClass{
}
