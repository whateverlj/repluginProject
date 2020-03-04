package com.example.myplugindemo;

public class NDKTools {
    static {
        System.loadLibrary("samplelib_jni");
    }

    private native void nativeMethod();
}
