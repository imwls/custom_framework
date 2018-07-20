package com.sunmi.internal.server;

import android.content.Context;
import android.content.Intent;
import android.os.UserHandle;
import android.util.Slog;
import android.content.ComponentName;

public class BootControl {

	private final static String TAG = "BootControl";

	public static void startSunmiApp(Context context) {
		try {
			startSecApp(context);
        } catch (Throwable e) {
            reportWtf("Sunmi boot failed", e);
        }
	}

	private static void startSecApp(Context context) {
		Intent intent = new Intent();
        intent.setAction("sunmi.intent.action.PAY_HARDWARE");
        intent.setPackage("com.sunmi.pay.hardware_v3");
        context.startServiceAsUser(intent, UserHandle.OWNER);
	}

	private static void reportWtf(String msg, Throwable e) {
        Slog.w(TAG, "***********************************************");
        Slog.wtf(TAG, "BOOT FAILURE " + msg, e);
    }

    public static void startVsimApp(Context context) {
        try {
            Intent intent = new Intent();
            intent.setAction("cn.showmac.sdk.START_SEED");
            intent.setComponent(new ComponentName("cn.showmac.sdkforzm",
                    "cn.showmac.sdk.service.SeedService"));
            context.startServiceAsUser(intent, UserHandle.OWNER);
        } catch (Throwable e) {
            reportWtf("Vsim Service boot failed", e);
        }
    }
}
