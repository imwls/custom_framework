# Copyright (C) 2008 The Android Open Source Project
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

#
# Rules for running apicheck to confirm that you haven't broken
# api compatibility or added apis illegally.
#

# skip api check for PDK buid
ifeq (,$(filter true, $(WITHOUT_CHECK_API) $(TARGET_BUILD_PDK)))

#--------------------------------------------------
.PHONY: checkapi-sunmi

# Run the checkapi rules by default.
droidcore: checkapi-sunmi

# Validate against previous release platform sdk version api text within prebuilts
sunmi_last_released_sdk_version := $(lastword $(call numerically_sort, \
            $(filter-out current, \
                $(patsubst $(SUNMI_SRC_API_DIR)/%.txt,%, $(wildcard $(SUNMI_SRC_API_DIR)/*.txt)) \
             )\
        ))

.PHONY: check-sunmi-public-api
checkapi-sunmi : check-sunmi-public-api

.PHONY: update-sunmi-api

# SUNMI_INTERNAL_PLATFORM_API_FILE is the one build by droiddoc.
# Note that since SUNMI_INTERNAL_PLATFORM_API_FILE  is the byproduct of api-stubs module,
# (See vendor/sunmi/Android.mk)
# we need to add api-stubs as additional dependency of the api check.

$(SUNMI_INTERNAL_PLATFORM_API_FILE): sunmi-api-stubs-docs

# Check that the API we're building hasn't broken the last-released
# SDK version.
$(eval $(call check-api, \
    checkpublicapi-sunmi-last, \
    $(SUNMI_SRC_API_DIR)/$(sunmi_last_released_sdk_version).txt, \
    $(SUNMI_INTERNAL_PLATFORM_API_FILE), \
    $(SUNMI_FRAMEWORK_PLATFORM_REMOVED_API_FILE), \
    $(SUNMI_INTERNAL_PLATFORM_REMOVED_API_FILE), \
    -hide 2 -hide 3 -hide 4 -hide 5 -hide 6 -hide 24 -hide 25 -hide 26 -hide 27 \
    -error 7 -error 8 -error 9 -error 10 -error 11 -error 12 -error 13 -error 14 -error 15 \
    -error 16 -error 17 -error 18 , \
    cat $(SUNMI_FRAMEWORK_API_NEEDS_UPDATE_TEXT), \
    check-sunmi-public-api, \
    $(call doc-timestamp-for,sunmi-api-stubs) \
    ))

# Check that the API we're building hasn't changed from the not-yet-released
# SDK version.
$(eval $(call check-api, \
    checkpublicapi-sunmi-current, \
    $(SUNMI_FRAMEWORK_PLATFORM_API_FILE), \
    $(SUNMI_INTERNAL_PLATFORM_API_FILE), \
    $(SUNMI_FRAMEWORK_PLATFORM_REMOVED_API_FILE), \
    $(SUNMI_INTERNAL_PLATFORM_REMOVED_API_FILE), \
    -error 2 -error 3 -error 4 -error 5 -error 6 \
    -error 7 -error 8 -error 9 -error 10 -error 11 -error 12 -error 13 -error 14 -error 15 \
    -error 16 -error 17 -error 18 -error 19 -error 20 -error 21 -error 23 -error 24 \
    -error 25 -error 26 -error 27, \
    cat $(SUNMI_FRAMEWORK_API_NEEDS_UPDATE_TEXT), \
    check-sunmi-public-api, \
    $(call doc-timestamp-for,sunmi-api-stubs) \
    ))

.PHONY: update-sunmi-public-api
update-sunmi-public-api: $(SUNMI_INTERNAL_PLATFORM_API_FILE) | $(ACP)
	@echo "Copying sunmi_current.txt"
	$(hide) $(ACP) $(SUNMI_INTERNAL_PLATFORM_API_FILE) $(SUNMI_FRAMEWORK_PLATFORM_API_FILE)
	@echo "Copying sunmi_removed.txt"
	$(hide) $(ACP) $(SUNMI_INTERNAL_PLATFORM_REMOVED_API_FILE) $(SUNMI_FRAMEWORK_PLATFORM_REMOVED_API_FILE)

update-sunmi-api : update-sunmi-public-api

.PHONY: update-sunmi-prebuilts-latest-public-api
current_sdk_release_text_file := $(SUNMI_SRC_API_DIR)/$(sunmi_last_released_sdk_version).txt

update-sunmi-prebuilts-latest-public-api: $(SUNMI_FRAMEWORK_PLATFORM_API_FILE) | $(ACP)
	@echo "Publishing sunmi_current.txt as latest API release"
	$(hide) $(ACP) $(SUNMI_FRAMEWORK_PLATFORM_API_FILE) $(current_sdk_release_text_file)

endif
