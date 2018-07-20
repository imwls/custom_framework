# Copyright (C) 2015 The CyanogenMod Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
LOCAL_PATH := $(call my-dir)

# We have a special case here where we build the library's resources
# independently from its code, so we need to find where the resource
# class source got placed in the course of building the resources.
# Thus, the magic here.
# Also, this module cannot depend directly on the R.java file; if it
# did, the PRIVATE_* vars for R.java wouldn't be guaranteed to be correct.
# Instead, it depends on the R.stamp file, which lists the corresponding
# R.java file as a prerequisite.
sunmi_platform_res := APPS/sunmi-res_intermediates/src

# The Sunmi Platform Framework Library
# ============================================================
include $(CLEAR_VARS)

sunmi_sdk_src := framework/core/sdk/src/java
sunmi_internal_src := framework/core/internal/src/java
sunmi_plugins_src := framework/core/plugins/src/java

LOCAL_MODULE := sunmi
LOCAL_MODULE_TAGS := optional
LOCAL_MODULE_OWNER := sunmi

LOCAL_SRC_FILES := \
    $(call all-java-files-under, $(sunmi_sdk_src)) \
    $(call all-java-files-under, $(sunmi_plugins_src)) \
    $(call all-java-files-under, $(sunmi_internal_src))

## READ ME: ########################################################
##
## When updating this list of aidl files, consider if that aidl is
## part of the SDK API.  If it is, also add it to the list below that
## is preprocessed and distributed with the SDK.  This list should
## not contain any aidl files for parcelables, but the one below should
## if you intend for 3rd parties to be able to send those objects
## across process boundaries.
##
## READ ME: ########################################################
LOCAL_SRC_FILES += \
    $(call all-Iaidl-files-under, $(sunmi_sdk_src)) \
    $(call all-Iaidl-files-under, $(sunmi_plugins_src)) \
    $(call all-Iaidl-files-under, $(sunmi_internal_src))

sunmi_LOCAL_INTERMEDIATE_SOURCES := \
    $(sunmi_platform_res)/sunmi/R.java \
    $(sunmi_platform_res)/sunmi/Manifest.java

LOCAL_INTERMEDIATE_SOURCES := \
    $(sunmi_LOCAL_INTERMEDIATE_SOURCES)

# Include aidl files from cyanogenmod.app namespace as well as internal src aidl files
#LOCAL_AIDL_INCLUDES := $(LOCAL_PATH)/framework/core/sdk/src/java
#LOCAL_AIDL_FLAGS := -n

include $(BUILD_JAVA_LIBRARY)

sunmi_framework_module := $(LOCAL_INSTALLED_MODULE)

# Make sure that R.java and Manifest.java are built before we build
# the source for this library.
sunmi_framework_res_R_stamp := \
    $(call intermediates-dir-for,APPS,sunmi-res,,COMMON)/src/R.stamp
$(full_classes_compiled_jar): $(sunmi_framework_res_R_stamp)
$(built_dex_intermediate): $(sunmi_framework_res_R_stamp)

$(sunmi_framework_module): | $(dir $(sunmi_framework_module))sunmi-res/sunmi-res.apk

sunmi_framework_built := $(call java-lib-deps,sunmi)

# the sdk
# ============================================================
include $(CLEAR_VARS)

LOCAL_MODULE:= sunmi.sdk
LOCAL_MODULE_TAGS := optional
LOCAL_JACK_ENABLED := disabled

LOCAL_SRC_FILES := \
    $(call all-java-files-under, $(sunmi_sdk_src)) \
    $(call all-Iaidl-files-under, $(sunmi_sdk_src))

# Included aidl files from sunmi.app namespace
#LOCAL_AIDL_INCLUDES := $(LOCAL_PATH)/framework/core/sdk/src/java

sunmisdk_LOCAL_INTERMEDIATE_SOURCES := \
    $(sunmi_platform_res)/sunmi/R.java \
    $(sunmi_platform_res)/sunmi/Manifest.java

LOCAL_INTERMEDIATE_SOURCES := \
    $(sunmisdk_LOCAL_INTERMEDIATE_SOURCES)

LOCAL_JAVA_LIBRARIES := sunmi

# Make sure that R.java and Manifest.java are built before we build
# the source for this library.
sunmi_framework_res_R_stamp := \
    $(call intermediates-dir-for,APPS,sunmi-res,,COMMON)/src/R.stamp
$(full_classes_compiled_jar): $(sunmi_framework_res_R_stamp)
$(built_dex_intermediate): $(sunmi_framework_res_R_stamp)
$(full_target): $(sunmi_framework_built) $(gen)
include $(BUILD_STATIC_JAVA_LIBRARY)

# the sdk as an aar for publish, not built as part of full target
# DO NOT LINK AGAINST THIS IN BUILD
# ============================================================
include $(CLEAR_VARS)

LOCAL_MODULE := sunmi.sdk.aar

LOCAL_JACK_ENABLED := disabled

# just need to define this, $(TOP)/dummy should not exist
LOCAL_SRC_FILES := $(call all-java-files-under, dummy)

LOCAL_CONSUMER_PROGUARD_FILE := $(LOCAL_PATH)/framework/core/sdk/proguard.txt

LOCAL_RESOURCE_DIR := $(addprefix $(LOCAL_PATH)/, framework/res/res)
LOCAL_MANIFEST_FILE := framework/core/sdk/AndroidManifest.xml

sunmisdk_exclude_files := 'sunmi/library'
LOCAL_JAR_EXCLUDE_PACKAGES := $(sunmisdk_exclude_files)
LOCAL_JAR_EXCLUDE_FILES := none

LOCAL_STATIC_JAVA_LIBRARIES := sunmi.sdk

include $(BUILD_STATIC_JAVA_LIBRARY)
$(LOCAL_MODULE) : $(built_aar)

# full target for use by platform apps
# ============================================================
include $(CLEAR_VARS)

LOCAL_MODULE:= sunmi.internal
LOCAL_MODULE_TAGS := optional

LOCAL_SRC_FILES := \
    $(call all-java-files-under, $(sunmi_sdk_src)) \
    $(call all-java-files-under, $(sunmi_plugins_src)) \
    $(call all-java-files-under, $(sunmi_internal_src)) \
    $(call all-Iaidl-files-under, $(sunmi_sdk_src)) \
    $(call all-Iaidl-files-under, $(sunmi_plugins_src)) \
    $(call all-Iaidl-files-under, $(sunmi_internal_src))

sunmisdk_LOCAL_INTERMEDIATE_SOURCES := \
    $(sunmi_platform_res)/sunmi/R.java \
    $(sunmi_platform_res)/sunmi/Manifest.java

LOCAL_JAVA_LIBRARIES:= sunmi

LOCAL_INTERMEDIATE_SOURCES := \
    $(sunmisdk_LOCAL_INTERMEDIATE_SOURCES)

$(full_target): $(sunmi_framework_built) $(gen)
include $(BUILD_STATIC_JAVA_LIBRARY)

# ===========================================================
# Common Droiddoc vars
intermediates.COMMON := $(call intermediates-dir-for,$(LOCAL_MODULE_CLASS),sunmi.sdk,,COMMON)

sunmi_stub_packages := sunmi:sunmi.os:sunmi.reflex:sunmi.hardware.input:sunmi.pm:sunmi.telephony:sunmi.dataservice.appusage:sunmi.dataservice.battery:sunmi.dataservice.cpu:sunmi.dataservice.dateflow

framework_docs_LOCAL_DROIDDOC_OPTIONS := \
    -knowntags frameworks/base/docs/knowntags.txt \
    -exclude com.sunmi.internal \
    -hidePackage com.sunmi.internal \
    -stubpackages $(sunmi_stub_packages) \
	-werror -hide 111 -hide 113 \
	-overview $(LOCAL_PATH)/framework/core/sdk/src/java/overview.html

framework_docs_LOCAL_DROIDDOC_OPTIONS += \
		-hdf sdk.version 2.0 \
		-hdf sdk.rel.id 2 \
		-hdf sdk.preview 0 \
		-since $(SUNMI_SRC_API_DIR)/1.txt 1 \
        -since $(SUNMI_SRC_API_DIR)/2.txt 2 

# ====  the api stubs and current.xml ===========================
include $(CLEAR_VARS)

LOCAL_SRC_FILES:= $(call all-java-files-under, $(sunmi_sdk_src))
LOCAL_INTERMEDIATE_SOURCES:= $(sunmi_LOCAL_INTERMEDIATE_SOURCES)
LOCAL_JAVA_LIBRARIES:= sunmi.sdk
LOCAL_MODULE_CLASS:= JAVA_LIBRARIES
LOCAL_DROIDDOC_SOURCE_PATH:= vendor/sunmi/framework/core/sdk/src/java
LOCAL_DROIDDOC_HTML_DIR:= build/docs/html
LOCAL_ADDITIONAL_JAVA_DIR:= $(intermediates.COMMON)
LOCAL_ADDITIONAL_DEPENDENCIES:= vendor/sunmi/build/docs/tools/droiddoc/docs/knowntags.txt libcore/Docs.mk

LOCAL_MODULE := sunmi-api-stubs

LOCAL_DROIDDOC_STUB_OUT_DIR := $(TARGET_OUT_COMMON_INTERMEDIATES)/JAVA_LIBRARIES/sunmisdk_stubs_current_intermediates/src

LOCAL_DROIDDOC_OPTIONS:= \
	$(framework_docs_LOCAL_DROIDDOC_OPTIONS) \
        -stubs $(TARGET_OUT_COMMON_INTERMEDIATES)/JAVA_LIBRARIES/sunmisdk_stubs_current_intermediates/src \
        -api $(SUNMI_INTERNAL_PLATFORM_API_FILE) \
        -removedApi $(SUNMI_INTERNAL_PLATFORM_REMOVED_API_FILE) \
        -nodocs

LOCAL_DROIDDOC_CUSTOM_TEMPLATE_DIR:= vendor/sunmi/build/docs/tools/droiddoc/templates-sdk

LOCAL_UNINSTALLABLE_MODULE := true

include $(BUILD_DROIDDOC)

# $(gen), i.e. framework.aidl, is also needed while building against the current stub.
$(full_target): $(sunmi_framework_built) $(gen)
$(SUNMI_INTERNAL_PLATFORM_API_FILE): $(full_target)
$(call dist-for-goals,sdk,$(SUNMI_INTERNAL_PLATFORM_API_FILE))


# Documentation
# ===========================================================
include $(CLEAR_VARS)

LOCAL_SRC_FILES := $(call all-java-files-under, $(sunmi_sdk_src))
LOCAL_INTERMEDIATE_SOURCES := $(sunmi_LOCAL_INTERMEDIATE_SOURCES)
LOCAL_JAVA_LIBRARIES := sunmi.sdk
LOCAL_MODULE_CLASS := JAVA_LIBRARIES
LOCAL_ADDITONAL_JAVA_DIR := $(intermediates.COMMON)/src
LOCAL_DROIDDOC_SOURCE_PATH:=vendor/sunmi/framework/core/sdk/src/java
#LOCAL_DROIDDOC_HTML_DIR:=docs/html
#LOCAL_ADDITIONAL_DEPENDENCIES:= vendor/sunmi/build/docs/tools/droiddoc/docs/knowntags.txt libcore/Docs.mk

LOCAL_MODULE := sunmi-sdk
LOCAL_MODULE_TAGS := optional

LOCAL_DROIDDOC_OPTIONS := \
        $(framework_docs_LOCAL_DROIDDOC_OPTIONS) \
        -offlinemode \
        -title "Sunmi SDK" \
        -proofread $(OUT_DOCS)/$(LOCAL_MODULE)-proofread.txt \
		-sdkvalues $(OUT_DOCS) \
        -hdf android.whichdoc offline

LOCAL_DROIDDOC_CUSTOM_TEMPLATE_DIR := vendor/sunmi/build/docs/tools/droiddoc/templates-sdk

include $(BUILD_DROIDDOC)

static_doc_index_redirect := $(out_dir)/index.html
$(static_doc_index_redirect): \
	$(LOCAL_PATH)/build/docs/tools/droiddoc/docs/docs-sunmi-preview-index.html | $(ACP)
	$(hide) mkdir -p $(dir $@)
	$(hide) $(ACP) $< $@

$(full_target): $(static_doc_index_redirect)
$(full_target): $(sunmi_framework_built)

include $(call first-makefiles-under,$(LOCAL_PATH))

# Cleanup temp vars
# ===========================================================
intermediates.COMMON :=
