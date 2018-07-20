package sunmi.os;

import android.content.Context;
import android.provider.Settings;

/**
 * 霸屏功能
 * @author gank
 *
 */
public class SuperApp {

	/**
	 * 霸屏 App 列表名称
	 */
    public static final String SUNMI_SUPER_APP_LIST = "sunmi_super_app_list";
    /**
     * 霸屏解锁密码
     */
    public static final String SUNMI_SUPER_APP_PASSWORD = "sunmi_super_app_password";

    /**
     * 设置霸屏接口
     * @param context
     * @param applist [com.sunmi.appstore:com.android.chrome] 霸屏 App 列表
     * @param passwd 解锁密码
     */
    public static final void setApp(Context context, String applist, String passwd) {
        Settings.Global.putString(context.getContentResolver(), SUNMI_SUPER_APP_LIST, applist);
        Settings.Global.putString(context.getContentResolver(), SUNMI_SUPER_APP_PASSWORD, passwd);
    }

}
