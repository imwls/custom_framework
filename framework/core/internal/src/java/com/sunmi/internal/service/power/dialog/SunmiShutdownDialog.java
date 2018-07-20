package com.sunmi.internal.service.power.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.plugins.ViewPlugin;

import sunmi.R;

public class SunmiShutdownDialog extends Dialog {

    private View mShutdown;
    private View mRestart;
    private View mShutdownLayout;
    private View mRestartLayout;
    private View mProgressbar;

    private View mMiddle;
    private View mSuperScreen;
    private View mQuitSuper;

    public SunmiShutdownDialog(Context context) {
        super(context);
    }

    public SunmiShutdownDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | ViewPlugin.SYSTEM_UI_FLAG_IMMERSIVE_GESTURE_ISOLATED);
        }
    }

    public static class Builder {
        private Context mContext;

        public Builder(Context context) {
            this.mContext = context;
        }

        public SunmiShutdownDialog create() {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final SunmiShutdownDialog dialog = new SunmiShutdownDialog(mContext, R.style.SunmiDialogTheme);
            dialog.getWindow().setType(2028);
            
            View layout = inflater.inflate(R.layout.dialog_sunmi_shutdown_confirm, null);
            dialog.mRestart = layout.findViewById(R.id.restart);
            dialog.mShutdown = layout.findViewById(R.id.shutdown);
            dialog.mShutdownLayout = layout.findViewById(R.id.shutdown_layout);
            dialog.mRestartLayout = layout.findViewById(R.id.restart_layout);
            dialog.mProgressbar = layout.findViewById(R.id.prograssbar);
            dialog.mMiddle = layout.findViewById(R.id.middle_icon);
            dialog.mSuperScreen = layout.findViewById(R.id.super_screen);
            dialog.mQuitSuper = layout.findViewById(R.id.quit_super);
                           
            dialog.mShutdownLayout.setVisibility(View.GONE);
            dialog.mRestartLayout.setVisibility(View.GONE);
            dialog.mMiddle.setVisibility(View.GONE);
            dialog.mSuperScreen.setVisibility(View.GONE);
            dialog.mQuitSuper.setVisibility(View.GONE);
            dialog.mProgressbar.setVisibility(View.VISIBLE);
            
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
            lp.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_NOSENSOR;
            lp.dimAmount = 1.0f;
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.addContentView(layout, lp);
            return dialog;
        }
    }
}
