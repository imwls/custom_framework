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
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Build;
import android.text.format.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import sunmi.dataservice.appusage.AppInformation;

/**
 * count the application time of the day
 * @author wangjiaxi
 */
public class AppUsageTime {
    private static final String TAG = "wjx";

    final public static int DAY = 0;
//    final public static int WEEK = 1;
//    final public static int MONTH = 2;
//    final public static int YEAR = 3;

    final public static int TODAY = 0;
    final public static int YESTODAY = 1;

    private ArrayList<AppInformation> showList;
    private ArrayList<AppInformation> appInfoList;
    private List<UsageStats> result;
    private long totalTime;
    private int totalTimes;
    private int style;
    private Context mContext;

    long beginTime;
    long endTime;

    /**
     * AppUsageTime constructed functiony
     * @param context
     */
    public AppUsageTime(Context context) {
        //default get app usage time by day
        this.style = AppUsageTime.DAY;
        mContext = context;
    }

    /**
     * AppUsageTime constructed function
     * @param context
     * @param style
     */
    public AppUsageTime(Context context, int style) {
        //default get app usage time by day
        this.style = style;
        mContext = context;
    }

    /**
     * count the application time of the day
     * @exception NoSuchFieldException
     */
    public void setUsageStatsList() throws NoSuchFieldException {
        setResultList(mContext, AppUsageTime.TODAY);
        List<UsageStats> mergeResult = mergeList(this.result, beginTime);
        appInfoList = new ArrayList<>();

        for(UsageStats usageStats:mergeResult) {
            appInfoList.add(new AppInformation(usageStats , mContext));
        }
        setShowList();
    }

    /**
     * filter the application
     */
    private void setShowList() {
        showList = new ArrayList<>();

        totalTime = 0;

        for(int i = 0; i< appInfoList.size(); i++) {
            if(appInfoList.get(i).getUsedTimebyDay() > 0 ) {
                showList.add(appInfoList.get(i));
                totalTime += appInfoList.get(i).getUsedTimebyDay();
                totalTimes += appInfoList.get(i).getTimes();
            }
        }

        //将显示列表中的应用按显示顺序排序
        for(int i = 0; i< showList.size() - 1; i++) {
            for(int j = 0; j< showList.size() - i - 1; j++) {
                if(showList.get(j).getUsedTimebyDay() < showList.get(j+1).getUsedTimebyDay()) {
                    AppInformation temp = showList.get(j);
                    showList.set(j, showList.get(j+1));
                    showList.set(j+1,temp);
                }
            }
        }
    }

    /**
     * query usage stats by day
     * @param context
     * @param type
     */
    public void setResultList(Context context, int type) {
        UsageStatsManager m = (UsageStatsManager)context.getSystemService(Context.USAGE_STATS_SERVICE);

        if(m != null) {
            Calendar calendar = Calendar.getInstance();
            endTime = calendar.getTimeInMillis();
            beginTime = getBeginTime();
            
            result = m.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, beginTime, endTime);
            /*if(style == DAY)
                result = m.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, beginTime, endTime);
            else if(style == WEEK)
                result = m.queryUsageStats(UsageStatsManager.INTERVAL_WEEKLY,beginTime, endTime);
            else if(style == MONTH)
                result = m.queryUsageStats(UsageStatsManager.INTERVAL_MONTHLY, beginTime, endTime);
            else if(style == YEAR)
                result = m.queryUsageStats(UsageStatsManager.INTERVAL_YEARLY, beginTime, endTime);
            else {
                result = m.queryUsageStats(UsageStatsManager.INTERVAL_BEST, beginTime, endTime);
            }*/
        }
    }

    /**
     * get begin time of the day
     * @return long
     */
    public long getBeginTime() {
        Calendar calendar = Calendar.getInstance();
        long begintime;
        /*if(style == WEEK) {
            //int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
            calendar.add(Calendar.DATE,-7);
            begintime = calendar.getTimeInMillis();
        }
        else if(style == MONTH) {
            //int mounthDay = calendar.get(Calendar.DAY_OF_MONTH);
            calendar.add(Calendar.DATE,-30);
            begintime = calendar.getTimeInMillis();
        }
        else if(style == YEAR) {
            calendar.add(Calendar.YEAR,-1);
            begintime = calendar.getTimeInMillis();
        }
        else{
            //剩下的输入均显示当天的数据
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            int second = calendar.get(Calendar.SECOND);

            calendar.add(Calendar.SECOND, -1 * second);
            calendar.add(Calendar.MINUTE, -1 * minute);
            calendar.add(Calendar.HOUR, -1 * hour);

            begintime = calendar.getTimeInMillis();

        }*/
        //剩下的输入均显示当天的数据
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        calendar.add(Calendar.SECOND, -1 * second);
        calendar.add(Calendar.MINUTE, -1 * minute);
        calendar.add(Calendar.HOUR, -1 * hour);

        begintime = calendar.getTimeInMillis();
        return begintime;
    }

    /**
     * merge the list of usage stats
     * @param result
     * @param beginTime
     * @return List
     */
    public List<UsageStats> mergeList(List<UsageStats> result, long beginTime) {
        List<UsageStats> mergeResult = new ArrayList<>();

        for(int i=0;i<result.size();i++) {

            long begintime = beginTime;

            if(result.get(i).getFirstTimeStamp() > begintime) {
                int num = foundUsageStats(mergeResult, result.get(i));
                if (num >= 0) {
                    UsageStats u = mergeResult.get(num);
                    u.add(result.get(i));
                    mergeResult.set(num, u);
                } else mergeResult.add(result.get(i));
            }
        }
        return mergeResult;
    }

    /**
     * found the position of the specified packageName in the usage stats
     * @param mergeResult
     * @param usageStats
     * @return int
     */
    public int foundUsageStats(List<UsageStats> mergeResult, UsageStats usageStats) {
        for(int i=0;i<mergeResult.size();i++) {
            if(mergeResult.get(i).getPackageName().equals(usageStats.getPackageName())) {
                return i;
            }
        }
        return -1;
    }

    /**
     * get the total time of the app has used
     * @return long
     */
    public long getTotalTime() {
        return totalTime;
    }

    /**
     * get the total times of the app has used
     * @return int
     */
    public int getTotalTimes() {
        return totalTimes;
    }

    /**
     * get app list which can show how much time the app has used
     * @return ArrayList
     */
    public ArrayList<AppInformation> getShowList() {
        return showList;
    }
}
