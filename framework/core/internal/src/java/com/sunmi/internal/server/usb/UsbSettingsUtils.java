package com.sunmi.internal.server.usb;

import java.util.ArrayList;
import java.util.List;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;

public class UsbSettingsUtils {

	public static int grantPermissionBySunmiDevice(Intent intent, UsbDevice device, String packageName,
			PendingIntent pi, Context userContext, PackageManager packageManager) {
		int uid = -1;
		if (SunmiDevices.isSunmiDevice(device.getVendorId(), device.getProductId())) {
			try {
				Log.d("JerryLiu", "UsbSettingsManager requestPermission Enter");
				ApplicationInfo aInfo = packageManager.getApplicationInfo(packageName, 0);
				uid = aInfo.uid;
			} catch (PackageManager.NameNotFoundException e) {
				throw new IllegalArgumentException("package " + packageName + " not found");
			}
			intent.putExtra(UsbManager.EXTRA_DEVICE, device);
			intent.putExtra(UsbManager.EXTRA_PERMISSION_GRANTED, true);
			try {
				Log.d("JerryLiu", "UsbSettingsManager requestPermission mUid = " + uid + ", packageName = " + packageName);
				pi.send(userContext, 0, intent);
			} catch (PendingIntent.CanceledException e) {
				Log.e("JerryLiu", "requestPermission PendingIntent was cancelled");
			}
		}
		return uid;
	}

	private static class SunmiDevices {

		private static final List<Integer> mVidList = new ArrayList<Integer>();
		private static final List<Integer> mPidList = new ArrayList<Integer>();

		static {
			//--------VID-----------			
			mVidList.add(0x2BC5);
			mVidList.add(0x20d1);
			//--------PID-----------
			mPidList.add(0x0403);
			mPidList.add(0x0402);
			mPidList.add(0x0404);
			mPidList.add(0x0606);
			mPidList.add(0x7008);
                        mPidList.add(0x0501);
		}

		public static boolean isSunmiDevice(int vid, int pid) {
			Log.d("Gank", "UsbSettingsManager requestPermission vid = " + vid + ", pid = " + pid);
			if(mVidList.contains(vid) && mPidList.contains(pid)) {
				return true;
			} else {
				return false;
			}
		}
	}

}
