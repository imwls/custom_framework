package com.sunmi.internal.service.am;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

public class SuperAppUtils {

    public static final String SUNMI_SUPER_APP_MODE         = "sunmi_super_app_mode";
    public static final int SUNMI_SUPER_APP_MODE_ENABLE     = 1;
    public static final int SUNMI_SUPER_APP_MODE_DISABLE     = 0;

    private static final List<String> sExceptAppList = new ArrayList<String>();

    static {
        sExceptAppList.add("system");
        sExceptAppList.add("android");
        sExceptAppList.add("woyou.aidlservice.jiuiv5");
        sExceptAppList.add("com.google.android.inputmethod.pinyin");
    }

    /**
     * 普通 App 判断是否处于霸屏模式
     * @param context
     * @return [true:禁止弹窗等操作][false:不做操作]
     */
    public static final boolean isSuperMode(Context context) {
        int mode = Settings.Global.getInt(context.getContentResolver(), SUNMI_SUPER_APP_MODE, SUNMI_SUPER_APP_MODE_DISABLE);
        if(mode == SUNMI_SUPER_APP_MODE_ENABLE && !isExceptApp(context.getBasePackageName(), context)) {
            return true;
        }else {
            return false;
        }
    }

    /**
     * 设置获取霸屏模式
     * @param context
     * @param mode
     */
    public static final void setSuperMode(Context context, int mode) {
        Settings.Global.putInt(context.getContentResolver(), SUNMI_SUPER_APP_MODE, mode);
    }

    /**
     * 直接获取霸屏模式
     * @param context
     * @return
     */
    public static final int getSuperMode(Context context) {
        return Settings.Global.getInt(context.getContentResolver(), SUNMI_SUPER_APP_MODE, SUNMI_SUPER_APP_MODE_DISABLE);
    }

    private static final boolean isExceptApp(String pkg, Context context) {
	String superApp = Settings.Global.getString(context.getContentResolver(), "sunmi_super_app_list");
	if (superApp != null && superApp.length() > 0) {
                for (String app : superApp.split(":")) {
                    Log.d("JerryLiu", "Sunmi SuperAppUtils isExceptApp app = " + app + ", pkg = " + pkg);
                    if (app.equals(pkg)) {
			return true;
                   }
                }
            }
    
        if (isCurrentInput(pkg, context)) {
            return true;
        }

        return sExceptAppList.contains(pkg);
    }

    /**
    * the package is InputMethod
    * @param pkg
    * @param contetx
    * @return boolean
    */
    private static final boolean isCurrentInput(String pkg, Context context) {
        String currentInputMethodId = Settings.Secure.getString(context.getContentResolver(), "default_input_method");

        Log.d("JerryLiu", "isCurrentInput 0 currentInputMethodId = " + currentInputMethodId + ", pkg = " + pkg);
        
        if (currentInputMethodId != null && currentInputMethodId.contains(pkg)) {
            return true;
        }

        return false;
    }
}
