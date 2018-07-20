# --------- Sunmi Framework ------------
# 1: SDK Public Api Dir
SUNMI_SRC_API_DIR := $(TOPDIR)vendor/sunmi/framework/api/public
# 2: Internal Sunmi api.txt
SUNMI_INTERNAL_PLATFORM_API_FILE := $(TARGET_OUT_COMMON_INTERMEDIATES)/PACKAGING/sunmi_public_api.txt
SUNMI_INTERNAL_PLATFORM_REMOVED_API_FILE := $(TARGET_OUT_COMMON_INTERMEDIATES)/PACKAGING/sunmi_removed.txt
# 3: Current Sunmi api.txt
SUNMI_FRAMEWORK_PLATFORM_API_FILE := $(TOPDIR)vendor/sunmi/framework/api/sunmi_current.txt
SUNMI_FRAMEWORK_PLATFORM_REMOVED_API_FILE := $(TOPDIR)vendor/sunmi/framework/api/sunmi_removed.txt
# 4: Sunmi update-api Show
SUNMI_FRAMEWORK_API_NEEDS_UPDATE_TEXT := $(TOPDIR)vendor/sunmi/build/docs/apicheck_msg_current.txt
