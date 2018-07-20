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

package sunmi.dataservice.battery;

import android.content.Context;
import android.os.Bundle;
import android.os.UserManager;
import android.os.BatteryStats;
import android.os.BatteryManager;
import android.os.Process;
import android.os.UserHandle;
import android.content.Intent;
import com.android.internal.os.BatteryStatsHelper;
import com.android.internal.os.BatterySipper;
import com.android.internal.os.BatterySipper.DrainType;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.SparseArray;

import sunmi.dataservice.battery.BatteryEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * get installed app battery information
 * @author wangjiaxi
 */
public class BatteryInfo {
    private static final String TAG = "wjx";

    protected BatteryStatsHelper mStatsHelper;
    protected UserManager mUm;
    private BatteryStats mStats;
    private Intent mBatteryBroadcast;
    private int temperature;
    private int level;
    private int scale;
    private Context mContext;

    private ArrayList<BatteryEntry> usageList;

    /**
     * BatteryInfo constructor function
     * @param context
     */
    public BatteryInfo (Context context) {
        mContext = context;
        mStatsHelper = new BatteryStatsHelper(context, true);
        mUm = (UserManager) context.getSystemService(Context.USER_SERVICE);
    }

    /**
     * init BatteryInfo before we get battery stats
     */
    public void init() {
        mStatsHelper.create((Bundle) null);
        mStatsHelper.clearStats();
        mStatsHelper.refreshStats(BatteryStats.STATS_SINCE_CHARGED, mUm.getUserProfiles());
        mStats = mStatsHelper.getStats();
        mBatteryBroadcast = mStatsHelper.getBatteryBroadcast();
    }

    /**
     * get battery temperature
     * @return int
     */
    public int getBatteryTemperature() {
        temperature = mBatteryBroadcast.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
        return temperature;
    }

    /**
     * get battery level
     * @return int
     */
    public int getBatteryLevel() {
        level = mBatteryBroadcast.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        return level;
    }

    /**
     * get battery scal
     * @return int
     */
    public int getBatteryScal() {
        scale = mBatteryBroadcast.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
        return scale;
    }

    /**
     * get battery usage information as list
     * @return ArrayList
     */
    public ArrayList<BatteryEntry> getBatteryUsage() {
        usageList = new ArrayList<>();
        //usageList = mStatsHelper.getUsageList();
        List<BatterySipper> list = getCoalescedUsageList(mStatsHelper.getUsageList());
        final int numSippers = list.size();
        for (int i = 0; i < numSippers; i++) {
            final BatterySipper sipper = list.get(i);
            if (sipper.getUid() != 0 && !isSystemUid(sipper.getUid())) {
                usageList.add(new BatteryEntry(sipper.getUid(), sipper.totalPowerMah));
            }
        }
        return usageList;
    }

    /**
     * We want to coalesce some UIDs. For example, dex2oat runs under a shared gid that
     * exists for all users of the same app. We detect this case and merge the power use
     * for dex2oat to the device OWNER's use of the app.
     * @param sippers
     * @return A sorted list of apps using power.
     */
    private static List<BatterySipper> getCoalescedUsageList(final List<BatterySipper> sippers) {
        final SparseArray<BatterySipper> uidList = new SparseArray<>();

        final ArrayList<BatterySipper> results = new ArrayList<>();
        final int numSippers = sippers.size();
        for (int i = 0; i < numSippers; i++) {
            BatterySipper sipper = sippers.get(i);
            if (sipper.getUid() > 0) {
                int realUid = sipper.getUid();

                // Check if this UID is a shared GID. If so, we combine it with the OWNER's
                // actual app UID.
                if (isSharedGid(sipper.getUid())) {
                    realUid = UserHandle.getUid(UserHandle.USER_OWNER,
                            UserHandle.getAppIdFromSharedAppGid(sipper.getUid()));
                }

                // Check if this UID is a system UID (mediaserver, logd, nfc, drm, etc).
                if (isSystemUid(realUid)
                        && !"mediaserver".equals(sipper.packageWithHighestDrain)) {
                    // Use the system UID for all UIDs running in their own sandbox that
                    // are not apps. We exclude mediaserver because we already are expected to
                    // report that as a separate item.
                    realUid = Process.SYSTEM_UID;
                }

                // if (realUid != sipper.getUid()) {
                //     // Replace the BatterySipper with a new one with the real UID set.
                //     BatterySipper newSipper = new BatterySipper(sipper.drainType,
                //             new FakeUid(realUid), 0.0);
                //     newSipper.add(sipper);
                //     newSipper.packageWithHighestDrain = sipper.packageWithHighestDrain;
                //     newSipper.mPackages = sipper.mPackages;
                //     sipper = newSipper;
                // }

                int index = uidList.indexOfKey(realUid);
                if (index < 0) {
                    // New entry.
                    uidList.put(realUid, sipper);
                } else {
                    // Combine BatterySippers if we already have one with this UID.
                    final BatterySipper existingSipper = uidList.valueAt(index);
                    existingSipper.add(sipper);
                    if (existingSipper.packageWithHighestDrain == null
                            && sipper.packageWithHighestDrain != null) {
                        existingSipper.packageWithHighestDrain = sipper.packageWithHighestDrain;
                    }

                    final int existingPackageLen = existingSipper.mPackages != null ?
                            existingSipper.mPackages.length : 0;
                    final int newPackageLen = sipper.mPackages != null ?
                            sipper.mPackages.length : 0;
                    if (newPackageLen > 0) {
                        String[] newPackages = new String[existingPackageLen + newPackageLen];
                        if (existingPackageLen > 0) {
                            System.arraycopy(existingSipper.mPackages, 0, newPackages, 0,
                                    existingPackageLen);
                        }
                        System.arraycopy(sipper.mPackages, 0, newPackages, existingPackageLen,
                                newPackageLen);
                        existingSipper.mPackages = newPackages;
                    }
                }
            } else {
                results.add(sipper);
            }
        }

        final int numUidSippers = uidList.size();
        for (int i = 0; i < numUidSippers; i++) {
            results.add(uidList.valueAt(i));
        }

        // The sort order must have changed, so re-sort based on total power use.
        Collections.sort(results, new Comparator<BatterySipper>() {
            @Override
            public int compare(BatterySipper a, BatterySipper b) {
                return Double.compare(b.totalPowerMah, a.totalPowerMah);
            }
        });
        return results;
    }

    /**
     * get if the gid is shared
     * @param uid
     * @return boolean
     */
    private static boolean isSharedGid(int uid) {
        return UserHandle.getAppIdFromSharedAppGid(uid) > 0;
    }

    /**
     * get if the uid is system uid or not
     * @param uid
     * @return boolean
     */
    private static boolean isSystemUid(int uid) {
        return uid >= Process.SYSTEM_UID && uid < Process.FIRST_APPLICATION_UID;
    }
}