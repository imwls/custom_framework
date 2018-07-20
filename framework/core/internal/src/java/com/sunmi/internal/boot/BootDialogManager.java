package com.sunmi.internal.boot;

import android.os.Handler;
import android.view.Window;
import android.view.WindowManagerInternal;
import android.view.WindowManagerPolicy.WindowManagerFuncs;
import android.util.Log;
import com.sunmi.internal.service.policy.impl.dialog.CustomDialog;
import android.content.Context;
import android.app.Dialog;
import android.view.WindowManager;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.hardware.input.InputManager;
import android.view.KeyEvent;
import android.os.SystemClock;
import sunmi.hardware.input.InputManagerUtils;
/**
 * @author: liuqiong
 * @date:2017-09-25
 * @note:boot dialogManager class
 */
public class BootDialogManager {
    //sunmi dialog object
    private static Dialog mSunmiDialog;

    /**
        * @note:Dismiss dialog
        * @param
        * @return boolean
        */
    public static boolean dismiss(Handler handler, int msg) {
        Log.d("JerryLiu", "BootDialogManager dismiss mSunmiDialog = " + mSunmiDialog);
        if (mSunmiDialog != null) {
            mSunmiDialog.dismiss();
            mSunmiDialog = null;
            if (handler != null) {                
                handler.sendEmptyMessage(msg);
            }

            return true;
        } 

        return false;
     }

    /**
        * @note:Show dialog
        * @param
        * @return boolean
        */
    public static boolean show(Context context, WindowManagerFuncs fun) {         
        Log.d("JerryLiu", "BootDialogManager show");
        if (context == null) {
            return false;
        }
        if (mSunmiDialog != null) {
            dismiss(null, 0);
        }
        mSunmiDialog = new com.sunmi.internal.service.policy.impl.dialog.SunmiShutdownDialog.Builder(context).create(fun);
        mSunmiDialog.show();
        final Context mContext = context;
        mSunmiDialog.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mSunmiDialog = null;
            }
        });

        return true;
    }

    /**
        * @note:Show custom dialog
        * @param
        * @return boolean
        */
    public static boolean showCustom(Handler handler, final String msg, final Context context) {
        Log.d("JerryLiu", "BootDialogManager showCustom handler = " + handler);
        if (handler == null || context == null) {
            return false;
        }

        /*if (mSunmiDialog != null) {
            dismiss(null, 0);
        }*/
        
        handler.post(new Runnable() {
            @Override public void run() {
                    if (mSunmiDialog == null) {
                        Log.d("JerryLiu", "BootDialogManager showCustom Create and show");
                        CustomDialog.Builder builder = new CustomDialog.Builder(context);
                        mSunmiDialog = builder.create();
                        mSunmiDialog.show();
                    }
                    Log.d("JerryLiu", "BootDialogManager showCustom Set msg");
                    ((CustomDialog)mSunmiDialog).setMessage(msg);
            }
        });

        return true;
    }

    /**
        * @note:Show simple dialog
        * @param
        * @return boolean
        */
    public static boolean showPower(Context context) {
        Log.d("JerryLiu", "BootDialogManager showPower");
        if (context == null) {
            return false;
        }

        if (mSunmiDialog != null) {
            return true;
        }
        
        mSunmiDialog = new com.sunmi.internal.service.power.dialog.SunmiShutdownDialog.Builder(context).create();
        //mSunmiDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
        mSunmiDialog.show();

        return true;
    }
    
}
