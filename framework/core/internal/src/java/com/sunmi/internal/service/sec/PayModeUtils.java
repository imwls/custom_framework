package com.sunmi.internal.service.sec;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.SystemProperties;
import android.util.Log;

public class PayModeUtils {
	
	public static final int SUNMI_PAY_MODE_NONE = -1;

	public static final int SUNMI_PAY_MODE_NORMAL = 1;

    /**
     * 1: BlockDialog -- Dialog.java
     * 2: BlockActivity -- This Class
     * @param intent
     * @param aInfo
     * @return
     */
    public static boolean shouldBlockActivity(Intent intent, ActivityInfo aInfo) {
        if(aInfo != null && aInfo.applicationInfo != null){
            int uid = aInfo.applicationInfo.uid;
            int pay_mode = SystemProperties.getInt("persist.sys.sunmi_pay_mode", SUNMI_PAY_MODE_NONE);
            if(pay_mode > 1 && pay_mode != uid){
                Log.d("Gank", "!!! Bank Mode -- block Activity : (Pay Uid = " + pay_mode + " Cur Uid = " + uid + ") [Intent: " + intent + "]");
                return true;
            }
        }
        return false;
    }

    public static void resetPayMode(){
        SystemProperties.set("persist.sys.sunmi_pay_mode", "" + SUNMI_PAY_MODE_NONE);
    }
}
