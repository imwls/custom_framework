package sunmi.telephony;

import android.telephony.TelephonyManager;

/**
 * TelephonyManager 反射调用
 * 参考 sunmi.reflex.TelephonyManagerUtils
 * @deprecated comment
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
