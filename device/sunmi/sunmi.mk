################################################################################################
#                                                                                              #
#                                     build.prop                                               #
#                                                                                              #
################################################################################################

############################
#                          #
#     Sunmi OS Version     #
#                          #
############################
#
# ro.sunmi.version_os
#
SUNMI_VERSION := "2.6.2"

#
# Default Android Prop
#
ifneq ($(SUNMI_DEVICE_MODEL),)
  #ro.product.name
  TARGET_OVERLAY_PRODUCT := $(SUNMI_DEVICE_MODEL)
  #ro.product.device
  TARGET_OVERLAY_DEVICE := $(SUNMI_DEVICE_MODEL)
  #ro.product.model
  PRODUCT_MODEL := $(SUNMI_DEVICE_MODEL)
endif
#ro.product.board
TARGET_BOOTLOADER_BOARD_NAME := $(SUNMI_DEVICE_BOARD)
#ro.product.brand
PRODUCT_BRAND := SUNMI
#ro.product.manufacturer
PRODUCT_MANUFACTURER := SUNMI
#ro.build.version.base_os
PLATFORM_BASE_OS := SUNMI

#
# Default Sunmi Prop
#
SUNMI_PRODUCT := $(SUNMI_DEVICE_BOARD)
SUNMI_DEVICECODE := $(SUNMI_DEVICE_HARDWARE)
SUNMI_HARDWARE := $(SUNMI_DEVICE_BOARD)

#
# Sunmi Prop Support Simulate Bluetooth
#
ifeq ($(SUNMI_PRINTER),true)
  SUNMI_SIMULATE_BLUETOOTH := true
else
  SUNMI_SIMULATE_BLUETOOTH := false
endif

#
# Jenkins export Sunmi Prop(Do not Change this Prop)
#
#SUNMI_VERSIONCODE=1.0.0
#SUNMI_VERSIONNAME=1
#SUNMI_BUILDTYPE=Release

#
# Default Locale and Timezone
#
#ro.product.locale
ifeq ($(strip $(SUNMI_FOREIGN)),true)
  PRODUCT_LOCALES := en_US zh_CN en_AU en_IN fr_FR it_IT es_ES et_EE de_DE nl_NL cs_CZ pl_PL ja_JP zh_TW zh_HK ru_RU ko_KR nb_NO es_US da_DK el_GR tr_TR pt_PT pt_BR sv_SE bg_BG ca_ES en_GB fi_FI hi_IN hr_HR hu_HU in_ID iw_IL lt_LT lv_LV ro_RO sk_SK sl_SI sr_RS uk_UA vi_VN tl_PH ar_EG fa_IR th_TH sw_TZ ms_MY af_ZA zu_ZA am_ET en_XA ar_XB fr_CA km_KH lo_LA ne_NP si_LK mn_MN hy_AM az_AZ ka_GE my_MM mr_IN ml_IN is_IS mk_MK ky_KG eu_ES gl_ES bn_BD ta_IN kn_IN te_IN uz_UZ ur_PK kk_KZ sq_AL gu_IN pa_IN be_BY bs_BA
  PRODUCT_PROPERTY_OVERRIDES += persist.sys.timezone=Europe/London
else
  PRODUCT_LOCALES := zh_CN en_US en_AU en_IN fr_FR it_IT es_ES et_EE de_DE nl_NL cs_CZ pl_PL ja_JP zh_TW zh_HK ru_RU ko_KR nb_NO es_US da_DK el_GR tr_TR pt_PT pt_BR sv_SE bg_BG ca_ES en_GB fi_FI hi_IN hr_HR hu_HU in_ID iw_IL lt_LT lv_LV ro_RO sk_SK sl_SI sr_RS uk_UA vi_VN tl_PH ar_EG fa_IR th_TH sw_TZ ms_MY af_ZA zu_ZA am_ET en_XA ar_XB fr_CA km_KH lo_LA ne_NP si_LK mn_MN hy_AM az_AZ ka_GE my_MM mr_IN ml_IN is_IS mk_MK ky_KG eu_ES gl_ES bn_BD ta_IN kn_IN te_IN uz_UZ ur_PK kk_KZ sq_AL gu_IN pa_IN be_BY bs_BA
  PRODUCT_PROPERTY_OVERRIDES += persist.sys.timezone=Asia/Shanghai
endif

#
# build.prop [build]
#
SUNMIINFO_SH := vendor/sunmi/build/prop/sunmiinfo.sh

################################################################################################
#                                                                                              #
#                                     Sunmi OS                                                 #
#                                                                                              #
################################################################################################

#
# Sunmi framework base modules
#
PRODUCT_PACKAGES += \
    sunmi

PRODUCT_BOOT_JARS += \
    sunmi

PRODUCT_PACKAGES += \
    sunmi-res

#
# Support Custom
#
-include vendor/sunmi_custom/custom.mk

#
# Sunmi Framework Overlay
#
DEVICE_PACKAGE_OVERLAYS += vendor/sunmi/device/custom/overlay \
                           vendor/sunmi/device/sunmi/overlay

#
# Sepolicy
#
BOARD_SEPOLICY_DIRS := \
            $(BOARD_SEPOLICY_DIRS) \
            vendor/sunmi/sepolicy/common \
            vendor/sunmi/sepolicy/custom

#
# add init.sunmi.rc
#
PRODUCT_PACKAGES += init.sunmi.rc

################################################################################################
#                                                                                              #
#                                     Copy Files                                               #
#                                                                                              #
################################################################################################

#
# local.prop
#
ifeq ($(strip $(SUNMI_CHIP_INFO)),rockchip)
  PRODUCT_COPY_FILES += vendor/sunmi/prebuilts/data/prop/local_$(SUNMI_CHIP_INFO).prop:cache/local.prop
else
  PRODUCT_COPY_FILES += vendor/sunmi/prebuilts/data/prop/local_$(SUNMI_CHIP_INFO).prop:data/local.prop
endif
#
# Sunmi APN
#
PRODUCT_COPY_FILES += vendor/sunmi/prebuilts/system/etc/sunmi-apns-conf.xml:system/etc/sunmi-apns-conf.xml

#
# Bootanimation
#
ifeq ($(strip $(SUNMI_CUSTOM)),)
  PRODUCT_COPY_FILES += vendor/sunmi/prebuilts/system/media/bootanimation/sunmi/$(SUNMI_SCREEN_SIZE).zip:system/media/bootanimation.zip
else
  PRODUCT_COPY_FILES += vendor/sunmi/prebuilts/system/media/bootanimation/$(SUNMI_CUSTOM)/$(SUNMI_SCREEN_SIZE).zip:system/media/bootanimation.zip
endif


################################################################################################
#                                                                                              #
#                                     Sunmi Sign                                               #
#                                                                                              #
################################################################################################
#
# Release Key and Verify Boot Key (Not for P1_4G)
#
ifeq ($(strip $(SUNMI_RELEASE_KEY)),true)
  PRODUCT_DEFAULT_DEV_CERTIFICATE := vendor/sunmi/build/security/releasekey
  PRODUCT_VERITY_SIGNING_KEY := vendor/sunmi/build/security/verity
endif



################################################################################################
#                                                                                              #
#                                     Build Include                                            #
#                                                                                              #
################################################################################################
#
# Sunmi ROM Prebuild App
#
-include vendor/sunmi/device/sunmi/prebuild_app.mk
# Sunmi One ROM
#
-include vendor/sunmi/device/sunmi/one.mk
#
# Sunmi FTP App
#
-include vendor/sunmi/app/sunmi.mk
#
# Support GMS
#
ifneq ($(SUNMI_GMS),)
  -include vendor/$(SUNMI_GMS)/products/gms.mk
endif

