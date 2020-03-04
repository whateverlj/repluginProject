LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE := Test
LOCAL_SRC_FILES := Test.cpp
include $(BUILD_SHARED_LIBRARY)