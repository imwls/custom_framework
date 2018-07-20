package sunmi.os;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

/**
 * Created by jie on 18-5-29.
 * 设置/获取输入法
 */
public class InputMethodManagerUtils {

    public static final String TAG = "InputMethodManagerUtils";
    /**
     * 获取系统默认输入法
     * @param context  上下文，不能为null
     * @return 默认输入法 ext : com.sohu.inputmethod.sogou/.SogouIME
     */
    public static String getDefaultIME(Context context) {

        String defIME = null;
        String settingProviderPKG = "com.android.providers.settings";
        Context settingProviderContext = null;

        if (null == context){
            Log.e(TAG,"getDefaultIME context is null!!");
            return null;
        }

        try {
            settingProviderContext = context.createPackageContext(settingProviderPKG,Context.CONTEXT_IGNORE_SECURITY|Context.CONTEXT_INCLUDE_CODE);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG,"getDefaultIME PackageManager.NameNotFoundException:"+e);
        }

        if (null != settingProviderContext){
            int id = settingProviderContext.getResources().getIdentifier("def_input_method","string",settingProviderPKG);
            if (0 != id){
                defIME = settingProviderContext.getResources().getString(id);
            }
        }

        Log.d(TAG,"defIME:"+defIME);
        return defIME;
    }

    /**
     * 设置输入法到系统
     * @param context 上下文，不能为null
     * @param ime  例如: com.sohu.inputmethod.sogou/.SogouIME
     * @return  true : 设置成功 false: 设置失败
     */
    public static boolean setCurrentIME(Context context,String ime) {

        if (null == context || null == ime){
            Log.e(TAG,"getDefaultIME context is null!!");
            return false;
        }

        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent("android.view.InputMethod");
        List<ResolveInfo> listResolveInfo =  packageManager.queryIntentServices(intent,0);

        boolean findIME = false;
        for (ResolveInfo resolveInfo:listResolveInfo){
            if (null != resolveInfo.serviceInfo){
                String name = ime.replaceAll("/","");
                if (name.equals(resolveInfo.serviceInfo.name)){
                    findIME = true;
                    break;
                }
            }
        }

        if (!findIME){
            Log.e(TAG,"setCurrentIME findIME failed!!");
            return false;
        }

        String curIME = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.DEFAULT_INPUT_METHOD);
        String enableIME = Settings.Secure.getString(context.getContentResolver(),Settings.Secure.ENABLED_INPUT_METHODS);

        if (!ime.equals(curIME)){
            if (!enableIME.contains(ime)) {
                enableIME = enableIME + ":" + ime;
                Settings.Secure.putString(context.getContentResolver(), Settings.Secure.ENABLED_INPUT_METHODS, enableIME);
            }

            Settings.Secure.putString(context.getContentResolver(), Settings.Secure.DEFAULT_INPUT_METHOD, ime);
            Toast.makeText(context,sunmi.R.string.changed_ime_success,Toast.LENGTH_LONG).show();
            return true;
        }

        return false;
    }

    /**
     * 获取当前使用的输入法
     * @param context 上下文，不能为null
     * @return  输入法字符串
     */
    public static String getCurrentIME(Context context) {

        if (null == context){
            Log.e(TAG,"getDefaultIME context is null!!");
            return null;
        }

        String ime = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.DEFAULT_INPUT_METHOD);
        Log.d(TAG,"getCurrentIME ime:"+ime);

        return ime;
    }
    
}
