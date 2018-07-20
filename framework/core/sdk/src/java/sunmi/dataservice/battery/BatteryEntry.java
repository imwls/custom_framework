/*
 * Copyright (C) 2014 The Android Open Source Project
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

package sunmi.dataservice.battery;

import android.os.BatteryStats.Uid;

/**
 * Wraps the power usage data of a BatterySipper with information about package name
 * and icon image.
 * @author wangjiaxi
 */
public class BatteryEntry {
    private static final String TAG = "wjx";

    public int userId;
    public Uid uidObj;
    public double totalPowerMah;

    /**
     * BatteryEntry constructed function
     * @param uid
     * @param value
     */
    public BatteryEntry(int uid, double value) {
        this.totalPowerMah = value;
        userId = uid;
    }

    /**
     * get uid
     * @return int
     */
    public int getUid() {
        return userId;
    }

    /**
     * get total power mah
     * @return double
     */
    public double getTotalPowerMah() {
        return totalPowerMah;
    }
}
