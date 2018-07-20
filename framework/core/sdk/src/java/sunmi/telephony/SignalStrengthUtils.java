package sunmi.telephony;

import android.telephony.SignalStrength;

/**
 * Contains phone signal strength related information.
 * 参考 sunmi.reflex.SignalStrengthUtils
 * @deprecated comment
 * @author gank
 */
public class SignalStrengthUtils {
	
	/**
	 * Get the signal strength as dBm
	 * @param ss
	 * @return
	 */
	public static int getDbm(SignalStrength ss) {
		return ss.getDbm();
	}

}
