################################################################################################
#                                                                                              #
#                                         One ROM                                              #
#                                                                                              #
################################################################################################

############################
#                          #
#           Qcom7          #
#                          #
############################
ifeq ($(SUNMI_CHIP_INFO),qcom)
  ifeq ($(PLATFORM_SDK_VERSION),25)
  	SUNMI_ONE_QCOM7 := true
  	PRODUCT_COPY_FILES += vendor/sunmi/prebuilts/persist/sunmi/device.prop:persist/sunmi/device.prop
  endif
endif

