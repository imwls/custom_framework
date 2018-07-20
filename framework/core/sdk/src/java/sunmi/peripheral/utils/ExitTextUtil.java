package sunmi.peripheral.utils;

import android.content.Context;
import android.hardware.input.InputManager;
import android.os.SystemClock;
import android.view.InputDevice;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;

/**
 * Author : kaltin
 * Create : 2018/3/13 13:50
 * Describe :
 */

public class ExitTextUtil {

    /**
	 * 
	 * @param context
	 * @param keycode
	 */
	public static void injectInputEvent(Context context, int keycode) {
		final long downTime = SystemClock.uptimeMillis();
		injectKeyEvent(downTime, KeyEvent.ACTION_DOWN, keycode, 0);
		injectKeyEvent(downTime, KeyEvent.ACTION_UP, keycode, 0);
	}

	private static void injectKeyEvent(long time, int action, int keycode, int repeat) {
		KeyEvent keyEvent = KeyEvent.obtain(time, time, action, keycode, repeat, 0, KeyCharacterMap.VIRTUAL_KEYBOARD, 0,
				KeyEvent.FLAG_FROM_SYSTEM, InputDevice.SOURCE_HDMI, null);
		InputManager.getInstance().injectInputEvent(keyEvent, InputManager.INJECT_INPUT_EVENT_MODE_ASYNC);
		keyEvent.recycle();
	}

	/**
	 * 
	 * @param context
	 * @param text
	 */
	public static void injectText(Context context, String text) {
	    //InputMethodManager.getInstance().sendTextToCurClient(text);
	}

}
