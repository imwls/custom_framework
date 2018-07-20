#
# This makefile is used to generate extra images for SUNMI targets
#

#----------------------------------------------------------------------
# Generate persist image (persist_qcom7.img)
#----------------------------------------------------------------------
ifeq ($(strip $(SUNMI_ONE_QCOM7)),true)

TARGET_OUT_PERSIST_SUNMI_EN := vendor/sunmi/prebuilts/persist/sunmi/device_uk.prop

TARGET_OUT_PERSIST_SUNMI_ZH := $(PRODUCT_OUT)/persist/sunmi/device.prop

TARGET_OUT_PERSIST_QCOM7 := $(PRODUCT_OUT)/persist

INTERNAL_PERSISTIMAGE_QCOM7_FILES := \
	$(filter $(TARGET_OUT_PERSIST_QCOM7)/%,$(ALL_DEFAULT_INSTALLED_MODULES))

INSTALLED_PERSISTIMAGE_TARGET_QCOM7 := $(PRODUCT_OUT)/persist_qcom7.img

define build-persistimage-qcom7-target
    $(call pretty,"Target qcom7 persist fs image: $(INSTALLED_PERSISTIMAGE_TARGET_QCOM7)")
    @mkdir -p $(TARGET_OUT_PERSIST_QCOM7)
    @echo ---TARGET_OUT_PERSIST_SUNMI_EN = $(TARGET_OUT_PERSIST_SUNMI_EN)
    @echo ---TARGET_OUT_PERSIST_SUNMI_ZH = $(TARGET_OUT_PERSIST_SUNMI_ZH)
    $(ACP) $(TARGET_OUT_PERSIST_SUNMI_EN) $(TARGET_OUT_PERSIST_SUNMI_ZH)
    $(hide) $(MKEXTUSERIMG) -s $(TARGET_OUT_PERSIST_QCOM7) $@ ext4 persist $(BOARD_PERSISTIMAGE_PARTITION_SIZE)
    $(hide) chmod a+r $@
    $(hide) $(call assert-max-image-size,$@,$(BOARD_PERSISTIMAGE_PARTITION_SIZE),yaffs)
endef
$(INSTALLED_PERSISTIMAGE_TARGET_QCOM7): $(INSTALLED_PERSISTIMAGE_TARGET) $(MKEXTUSERIMG) $(MAKE_EXT4FS) $(INTERNAL_PERSISTIMAGE_QCOM7_FILES) | $(ACP)
	$(build-persistimage-qcom7-target)

ALL_DEFAULT_INSTALLED_MODULES += $(INSTALLED_PERSISTIMAGE_TARGET_QCOM7)
ALL_MODULES.$(LOCAL_MODULE).INSTALLED += $(INSTALLED_PERSISTIMAGE_TARGET_QCOM7)

endif

###################################################################################################

.PHONY: persistqcomimages
persistqcomimages: $(INSTALLED_PERSISTIMAGE_TARGET_QCOM7)
