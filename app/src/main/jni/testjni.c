#include <jni.h>
#include <stdio.h>
#include <malloc.h>
#include <stdlib.h>
#include<com_mytest_mytestdemo_TestJni.h>
 #include<android/log.h>
 #include <string.h>
 #define TAG "myDemo-jni" // 这个是自定义的LOG的标识
 #define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG ,__VA_ARGS__) // 定义LOGD类型
 #define LOGI(...) __android_log_print(ANDROID_LOG_INFO,TAG ,__VA_ARGS__) // 定义LOGI类型
 #define LOGW(...) __android_log_print(ANDROID_LOG_WARN,TAG ,__VA_ARGS__) // 定义LOGW类型
 #define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG ,__VA_ARGS__) // 定义LOGE类型
 #define LOGF(...) __android_log_print(ANDROID_LOG_FATAL,TAG ,__VA_ARGS__) // 定义LOGF类型
char*  Jstring2CStr(JNIEnv*  env,jstring  jstr)
{
    LOGI("enter CStr2Jstring 1");
    char*   rtn   =   NULL;
    jclass   clsstring   =   (*env)->FindClass(env,"java/lang/String");
    jstring   strencode   =  (*env)->NewStringUTF(env,"GB2312");//转换成Cstring的GB2312，兼容ISO8859-1
    //jmethodID   (*GetMethodID)(JNIEnv*, jclass, const char*, const char*);第二个参数是方法名，第三个参数是getBytes方法签名
    //获得签名：javap -s java/lang/String:   (Ljava/lang/String;)[B
    jmethodID   mid   =   (*env)->GetMethodID(env,clsstring,"getBytes","(Ljava/lang/String;)[B");
    //等价于调用这个方法String.getByte("GB2312");
    //将jstring转换成字节数组
    LOGI("enter CStr2Jstring 2");
    //用Java的String类getByte方法将jstring转换为Cstring的字节数组
    jbyteArray  barr=   (jbyteArray) (*env)->CallObjectMethod(env,jstr,mid,strencode);
    LOGI("enter CStr2Jstring 3");
    jsize   alen   =   (*env)->GetArrayLength(env,barr);
    LOGI("enter CStr2Jstring 4");
    jbyte*   ba   =   (*env)->GetByteArrayElements(env,barr,JNI_FALSE);
    LOGI("alen=%d\n",alen);
    if(alen   >   0)
    {
        rtn   =   (char*)malloc(alen+1+128);
        LOGI("rtn address == %p",&rtn);//输出rtn地址
        memcpy(rtn,ba,alen);
        rtn[alen]=0;            //"\0"
    }
    (*env)->ReleaseByteArrayElements(env,barr,ba,0);
    return rtn;
};

JNIEXPORT jstring JNICALL Java_com_mytest_mytestdemo_TestJni_getString
  (JNIEnv * env, jclass jls, jstring jstr){
     char*  cstr = Jstring2CStr(env,jstr);
     char* str = "123456";
     strcat(cstr,str);
    return (*env)->NewStringUTF(env,cstr);
  };