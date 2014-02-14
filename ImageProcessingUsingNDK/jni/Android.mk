LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := ImageProcessingUsingNDK
LOCAL_SRC_FILES := ImageProcessingUsingNDK.cpp
LOCAL_SRC_FILES += coffeecatch.c coffeejni.c
LOCAL_LDLIBS := -llog
LOCAL_LDFLAGS +=-ljnigraphics
LOCAL_CPP_FEATURES += exceptions

include $(BUILD_SHARED_LIBRARY)
