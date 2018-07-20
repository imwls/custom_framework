===========================================================
Code
1: framework/base
	* aapt
	* framework.jar
	* libandroidfw.so
	* libandroid_runtime.so
2: external/sepolicy/
    * Support USER [sunmi_app]
    * Support Sunmi Keystore

===========================================================
Makefile
1: Device
	* include sunmi.mk
	* PRODUCT_RESTRICT_VENDOR_FILES := false

2: Build
	* include vendor/sunmi/build/config.mk
	* LOCAL_IGNORE_SUBDIR -- (/system/framework/sunmi.apk)

===========================================================
Build
1: Docs
	* make  sunmi-sdk-docs
	* vendor/sunmi/build/docs/tools/droiddoc/templates-sdk/assets/js/docs.js -- var API_LEVELS = ['1', '2'];



