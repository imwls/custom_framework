LOCAL_PATH:= $(call my-dir)

#######################################
# init.sunmi.rc
include $(CLEAR_VARS)

LOCAL_MODULE := init.sunmi.rc
LOCAL_SRC_FILES := $(LOCAL_MODULE)
LOCAL_MODULE_CLASS := ETC
LOCAL_MODULE_PATH := $(TARGET_ROOT_OUT)

include $(BUILD_PREBUILT)


#######################################
