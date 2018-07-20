package sunmi.reflex;

import android.telephony.SignalStrength;

/**
 * Contains phone signal strength related information.
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
