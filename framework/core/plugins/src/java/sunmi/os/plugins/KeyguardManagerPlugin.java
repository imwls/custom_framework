package sunmi.os.plugins;

import android.app.KeyguardManager;
import android.content.Context;
import android.util.Log;

/**
 * Class that can be used to dismiss keyguard/clear keyguard password. 
 */
public class KeyguardManagerPlugin {
    
    public static void disMissKeyguard(Context context,String key){

        if (null == context){
	    return ;
        }

//        KeyguardManager keyguardManager = (KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE);
//        try {
//            keyguardManager.disMissKeyguard(context,key);
//        }catch(Exception e){
//           Log.e("KeyguardManagerUtils","disMissKeyguard exception:"+e);
//        }
    }

    public static boolean clearKeyguardPwd(Context context){

        if (null == context){
	    return false;
        }

//        KeyguardManager keyguardManager = (KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE);
//        try {
//            return keyguardManager.clearKeyguardPwd(context);
//        }catch(Exception e){
//           Log.e("KeyguardManagerUtils","clearKeyguardPwd exception:"+e);
//        }

        return false;
    }
    
}
