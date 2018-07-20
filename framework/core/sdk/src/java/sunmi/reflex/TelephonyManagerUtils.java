package sunmi.reflex;

import android.telephony.TelephonyManager;

/**
 * TelephonyManager 反射调用
 * @author gank
 */
public class TelephonyManagerUtils {
	
	/**
	 * Op mobile network
	 * @param tm
	 * @param enable
	 */
	public static void setDataEnabled(TelephonyManager tm, boolean enable) {
		tm.setDataEnabled(enable);
	}
	
	/**
	 * Get mobile network enable
	 * @param tm
	 * @return
	 */
	public boolean getDataEnabled(TelephonyManager tm) {
		return tm.getDataEnabled();
	}

}
