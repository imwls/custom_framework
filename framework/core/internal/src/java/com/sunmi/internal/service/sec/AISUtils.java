package com.sunmi.internal.service.sec;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.Slog;
import android.view.KeyEvent;

/**
 * Alarm Indication Signal
 * 
 * @author gank
 *
 */
public class AISUtils {

	public static final int KEYCODE_AIS = 295;

	public static void interceptAISKeyBeforeDispatching(KeyEvent event, Context context) {
		final int keyCode = event.getKeyCode();
		final boolean down = event.getAction() == KeyEvent.ACTION_DOWN;
		if (keyCode == KEYCODE_AIS && isAISTrigger() && down) {
			sendAISInfo(context);
		}
	}

	public static boolean isAISTrigger() {
		boolean trigger = false;
		try {
			java.io.File file = new java.io.File("/sys/devices/bus/bus:safehold/pay_alert");
			java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file));
			String content = reader.readLine();
			if (content != null && "1".equals(content.trim())) {
				trigger = true;
			}
			reader.close();
		} catch (Exception e) {

		}
		return trigger;
	}

	private static void sendAISInfo(Context context) {
		try {
			Intent intent = new Intent("com.sunmi.pos.ACTION_AIS");
			intent.putExtra("AIS", "1");
            intent.setPackage("com.sunmi.pay.hardware_v3");
			context.startService(intent);
		} catch (Exception e) {
			Slog.d("Gank", "sendAISInfo e=" + e);
		}
	}
}
