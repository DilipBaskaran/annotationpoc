package com.e5;
import com.e5.annotation.processor.BuilderProperty;

public class Person {

    private int age;

    private String name;

    @BuilderProperty
    public void setAge(int age) {
        this.age = age;
    }

    @BuilderProperty
    public void setName(String name) {
        this.name = name;
    }

    // getters …

}