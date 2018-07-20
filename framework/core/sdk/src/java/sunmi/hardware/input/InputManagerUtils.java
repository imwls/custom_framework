package sunmi.hardware.input;

import android.hardware.input.InputManager;
import android.view.InputEvent;

/**
 * InputManager
 * <h3>Need Sunmi Sign</h3>
 * @deprecated comment
 * @author gank
 */
public class InputManagerUtils {

	/**
	 * Inject key into System
	 * @param im
	 * @param event
	 * @param mode
	 * @return
	 */
	public static boolean injectInputEvent(InputManager im, InputEvent event, int mode) {
		return im.injectInputEvent(event, mode);
	}

}
