#include "com_mazaiting_jni_Test.h"

JNIEXPORT jstring JNICALL Java_com_mazaiting_jni_Test_getString
        (JNIEnv *env, jobject){
    return (*env).NewStringUTF("This is test jni!");
}