/*
 * Copyright (C) 2017 The Sunmi Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sunmi.os;

import android.os.SystemProperties;

/**
 * Information about the current Sunmi build, extracted from system properties.
 */
public class Build {
    /** Value used for when a build property is unknown. */
    public static final String UNKNOWN = "unknown";

    /** Value used for when a build property is unknown. */
    public static final String UNKNOWN1 = "unknown";

    /** Value used for when a build property is unknown. */
    public static final String UNKNOWN2 = "unknown";

    /** A build ID utilized to distinguish Sunmi versions */
    public static final String SUNMI_VERSION_NAME = getString("ro.version.sunmi_versionname");

    /** A build ID utilized to distinguish Sunmi version code */
    public static final String SUNMI_VERSION_CODE = getString("ro.version.sunmi_versioncode");

    /**
     * Sunmi Device Code for Hardward
     */
    public static final String SUNMI_DEVICE_CODE = getString("ro.sunmi.devicecode");

    /**
     * Sunmi Product Name for Software
     */
    public static final String SUNMI_PRODUCT_NAME = getString("ro.sunmi.productname");

    /**
     * Sunmi Hardware Name for Software
     */
    public static final String SUNMI_HARDWARE = getString("ro.sunmi.hardware");

    /**
     * Normal Serial No
     */
    public static final String SUNMI_SERIAL_NO = getString("ro.serialno");

    /**
     * Second Serial No
     */
    public static final String SUNMI_SECOND_SERIAL_NO = getString("gsm.serial1");

    /**
     * Third Serial No
     */
    public static final String SUNMI_THIRD_SERIAL_NO = getString("gsm.serial2");



    private static String getString(String property) {
        return SystemProperties.get(property, UNKNOWN);
    }
}
