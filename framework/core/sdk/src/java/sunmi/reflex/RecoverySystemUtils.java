package sunmi.reflex;

import java.io.IOException;

import android.content.Context;
import android.os.RecoverySystem;
/**
 * 提供 RecoverySystem 反射方法
 * @author gank
 *
 */
public class RecoverySystemUtils {

	/**
	 * Reboots the device and wipes the user data and cache partitions.
	 * @param context
	 * @param shutdown
	 * @throws IOException
	 */
	public static void rebootWipeUserData(Context context, boolean shutdown)
            throws IOException {
		RecoverySystem.rebootWipeUserData(context, shutdown);
	}

}
