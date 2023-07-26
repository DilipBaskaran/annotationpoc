package com.e5.annotation.services;


public abstract class SuperAbsClass {
    private Class<?> parameter1;
    private Class<?> parameter2;
    private boolean parameter3;
    private boolean parameter4;

    public SuperAbsClass (){
        System.out.println("SuperClass constructor");
    }

    public Class<?> getParameter1 () {
        return parameter1;
    }

    public void setParameter1 (Class<?> parameter1) {
        this.parameter1 = parameter1;
    }

    public Class<?> getParameter2 () {
        return parameter2;
    }

    public void setParameter2 (Class<?> parameter2) {
        this.parameter2 = parameter2;
    }

    public boolean isParameter3 () {
        return parameter3;
    }

    public void setParameter3 (boolean parameter3) {
        this.parameter3 = parameter3;
    }

    public boolean isParameter4 () {
        return parameter4;
    }

    public void setParameter4 (boolean parameter4) {
        this.parameter4 = parameter4;
    }
}
