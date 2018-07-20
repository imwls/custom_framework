package sunmi.os;

import android.os.PowerManager;

/**
 * 提供反射电源管理方法
 * @deprecated comment
 * @author gank
 */
public class PowerManagerUtils {

	/**
     * Turn off the device.
     *
     * @param confirm If true, shows a shutdown confirmation dialog.
     * @param wait If true, this call waits for the shutdown to complete and does not return.
     */
    public void shutdown(PowerManager power, boolean confirm, boolean wait) {
       power.shutdown(confirm, null, wait);
    }

}
