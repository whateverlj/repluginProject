LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE    := TestJni
LOCAL_SRC_FILES := testjni.c
LOCAL_LDLIBS :=  -L$(SYSROOT)/usr/lib -llog
include $(BUILD_SHARED_LIBRARY)