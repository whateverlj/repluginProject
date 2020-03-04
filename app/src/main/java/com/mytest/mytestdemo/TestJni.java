package com.mytest.mytestdemo;

public class TestJni {

    static {
        System.loadLibrary("TestJni");
    }
    public static native String getString(String test);
}
