#
# Prebuilts App
#
ifeq (true,$(strip ($(SUNMI_BUGREPORT))))
PRODUCT_PACKAGES += \
    BugReport
endif
