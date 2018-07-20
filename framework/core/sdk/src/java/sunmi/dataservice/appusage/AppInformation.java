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

package sunmi.dataservice.appusage;

import android.app.usage.UsageStats;
import android.app.usage.plugins.UsageStatsPlugin;
import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Information of App
 * @author wangjiaxi
 */
public class AppInformation {
    private UsageStats usageStats;
    private String packageName;
    private String label;
    private long UsedTimebyDay;  //milliseconds
    private Context context;
    private int times;

    /**
    * AppInformation Construct
    * <p><pre>{@code
    * Using:
    * new AppInformation(UsageStats , Context);
    * }</pre>
    * @param usageStats type of UsageStats
    * @param context type of Context
    */
    public AppInformation(UsageStats usageStats , Context context) {
        this.usageStats = usageStats;
        this.context = context;

        try {
            GenerateInfo();
        } catch (PackageManager.NameNotFoundException | IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    /**
     * get the app information such as packageName,useTime,lable,etc.
     * @throws NameNotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private void GenerateInfo() throws PackageManager.NameNotFoundException, NoSuchFieldException, IllegalAccessException {
        packageName = usageStats.getPackageName();
        if(packageName != null && !packageName.equals("")) {
            UsedTimebyDay = usageStats.getTotalTimeInForeground();
            label = UsageStatsPlugin.getApplicationLabel(usageStats);
            times = (Integer) usageStats.getClass().getDeclaredField("mLaunchCount").get(usageStats);
        }
    }

    /**
     * get the app usage stats
     * <p><pre>{@code
     * Using:
     * UsageStats stats = new AppInformation(UsageStats , Context).getUsageStats();
     * }</pre>
     * @return UsageStats
     */
    public UsageStats getUsageStats() {
        return usageStats;
    }

    /**
     * get the app usage time
     * <p><pre>{@code
     * Using:
     * int times = new AppInformation(UsageStats , Context).getTimes();
     * }</pre>
     * @return int get from {@link #setTimes(int)}
     */
    public int getTimes() {
        return times;
    }

    /**
     * set app time
     * <p><pre>{@code
     * Using:
     * new AppInformation(UsageStats , Context).setTimes(int);
     * }</pre>
     * @param times times
     */
    public void setTimes(int times) {
        times = times;
    }

    /**
     * set app used time about the day
     * <p><pre>{@code
     * Using:
     * new AppInformation(UsageStats , Context).setUsedTimebyDay(long);
     * }</pre>
     * @param usedTimebyDay the time of day
     */
    public void setUsedTimebyDay(long usedTimebyDay) {
        UsedTimebyDay = usedTimebyDay;
    }

    /**
     * get the app used time in the day
     * <p><pre>{@code
     * Using:
     * long time = new AppInformation(UsageStats , Context).getUsedTimebyDay();
     * }</pre>
     * @return long get from {@link #setUsedTimebyDay(long)}
     */
    public long getUsedTimebyDay() {
        return UsedTimebyDay;
    }

    /**
     * get the app label
     * <p><pre>{@code
     * Using:
     * String label = new AppInformation(UsageStats , Context).getLabel();
     * }</pre>
     * @return String  the label of app
     */
    public String getLabel() {
        return label;
    }

    /**
     * get the app packageName
     * <p><pre>{@code
     * Using:
     * String label = new AppInformation(UsageStats , Context).getPackageName();
     * }</pre>
     * @return String the app package name
     */
    public String getPackageName() {
        return packageName;
    }


}
