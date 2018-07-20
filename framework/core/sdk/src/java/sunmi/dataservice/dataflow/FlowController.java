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

package sunmi.dataservice.dataflow;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.INetworkStatsService;
import android.net.INetworkStatsSession;
import android.net.NetworkStats;
import android.net.NetworkTemplate;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.telephony.TelephonyManager;

import sunmi.dataservice.dataflow.AppDetailBean;

import java.util.Calendar;
import android.content.pm.ApplicationInfo;
import java.util.ArrayList;

/**
 * 缓存流量信息
 * Created by bps .
 */
public class FlowController {

    private PackageManager mPackageManager;
    private INetworkStatsService mStatsService;
    private INetworkStatsSession mStatsSession;
    private final NetworkTemplate mTemplate;
    private Context context;

    public FlowController(Context context) {
        this.context = context;
        mPackageManager = context.getPackageManager();
        TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        mStatsService = INetworkStatsService.Stub.asInterface(ServiceManager.getService("netstats"));
        try {
            mStatsSession = mStatsService.openSession();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        mTemplate = NetworkTemplate.buildTemplateMobileAll(mTelephonyManager.getSubscriberId());
    }

    /**
     * APP当天使⽤用的3G流量量
     *
     * @param date
     * @return
     */
    public ArrayList<AppDetailBean> getTrafficData() {
        ArrayList<AppDetailBean> appFlowList = new ArrayList<AppDetailBean>();
        long allTraffic = 0;
        try {
            long startTime = getBeginTime(); //当天凌晨的时间戳
            NetworkStats mNetworkStats = mStatsSession.getSummaryForAllUid(
                    mTemplate, startTime, System.currentTimeMillis() - 1, false);// startTime + ONE_DAY - 1
            int count = mNetworkStats.size();
            if (count <= 0) {
                return appFlowList;
            }
            NetworkStats.Entry entry = null;
            for (int i = 0; i < count; i++) {
                entry = mNetworkStats.getValues(i, entry);
                long traffic = entry.rxBytes + entry.txBytes;
                allTraffic = allTraffic + traffic;
                AppDetailBean appTraffic = new AppDetailBean();
                String packName = mPackageManager.getNameForUid(entry.uid);
                if (packName == null
                        || packName.equals("android.uid.system:1000")) {
                    packName = "com.android.systemui";
                }
                appTraffic.packageName = packName;
                appTraffic.appUsedFlow = traffic;
                appTraffic.appName = getAppName(context, packName);
                appFlowList.add(appTraffic);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appFlowList;
    }

    /**
     * 获取当天凌晨时间戳
     *
     * @param appDetailBeenList
     * @return
     */
    public long getBeginTime() {
        Calendar calendar = Calendar.getInstance();
        long begintime;

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        begintime = calendar.getTimeInMillis();
        return begintime;
    }

    public String getAppName(Context context, String packageName) throws PackageManager.NameNotFoundException {
        PackageManager packageManager = context.getPackageManager();
        String label;
        if(packageName != null && !packageName.equals("")) {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
            label = (String) packageManager.getApplicationLabel(applicationInfo);
        } else {
            label = "";
        }
        return label;
    }
}
