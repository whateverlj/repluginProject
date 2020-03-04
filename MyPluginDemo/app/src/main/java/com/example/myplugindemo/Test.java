package com.example.myplugindemo;

public class Test {

    static {
        System.loadLibrary("Test");
    }
    public native String getString();

}