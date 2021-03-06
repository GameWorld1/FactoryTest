LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_PRELINK_MODULE := false

LOCAL_MODULE_PATH := $(TARGET_OUT_SHARED_LIBRARIES)
LOCAL_MODULE    := libfactorytest
LOCAL_SRC_FILES := nativeManager.c
LOCAL_MODULE_TAGS := optional
LOCAL_LDLIBS := -llog # 如果不包含这一句的话，会提示：__android_log_print 未定义
LOCAL_C_INCLUDES += \
	$(JNI_H_INCLUDE) \
	libcore/include

LOCAL_SHARED_LIBRARIES := \
	libcutils \
	libutils \
	liblog

include $(BUILD_SHARED_LIBRARY)

LOCAL_MODULE    := serial_port
LOCAL_SRC_FILES := SerialPort.c
LOCAL_LDLIBS    := -llog

include $(BUILD_SHARED_LIBRARY)