package sunmi.os;

import android.content.Context;
import sunmi.os.plugins.KeyguardManagerPlugin;

/**
 * Created by jie on 18-5-29.
 * 解锁/清除锁屏密码 只有sunmi系统应用可以使用该api
 */
public class KeyguardManagerUtils {

    /**
     * 解锁
     * @param context 上下文，不能为null
     * @param key     需要密码时，解锁密码
     */
    public static void disMissKeyguard(Context context, String key) {

        if (null == context) {
            return;
        }

        KeyguardManagerPlugin.disMissKeyguard(context, key);
    }

    /**
     * 清除密码
     * @param context  上下文，不能为null
     * @return true:清除成功  false:清除失败
     */
    public static boolean clearKeyguardPwd(Context context) {

        if (null == context) {
            return false;
        }

        return KeyguardManagerPlugin.clearKeyguardPwd(context);
    }

}
