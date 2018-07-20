package com.sunmi.internal.service.policy.impl.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManagerPolicy.WindowManagerFuncs;
import android.view.Window;
import android.view.WindowManager;
import android.view.MotionEvent;
import android.view.View;
import android.provider.Settings;
import sunmi.R;

public class SunmiShutdownDialog extends Dialog {

	private View mShutdown;
	private View mRestart;
	private View mShutdownLayout;
	private View mRestartLayout;
	private View mProgressbar;         

    //liuqiong@sunmi.com add 2017-12-06 begin
    private View mRestartImage;
    private View mShutDownImage;
    private View mMiddle;
    private View mSuperScreen;
    private View mQuitSuper;
    //liuqiong@sunmi.com add 2017-12-06 end

	WindowManagerFuncs mWindowManagerFuncs;

	public SunmiShutdownDialog(Context context) {
		super(context);
	}

	public SunmiShutdownDialog(Context context, int theme, WindowManagerFuncs funcs) {
		super(context, theme);
		mWindowManagerFuncs = funcs;
	}

	public static class Builder {
		private Context mContext;
                  private View layout;

		public Builder(Context context) {
			this.mContext = context;
		}

		public SunmiShutdownDialog create(WindowManagerFuncs funcs) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// instantiate the dialog with the custom Theme
			final SunmiShutdownDialog dialog = new SunmiShutdownDialog(mContext, R.style.SunmiDialogTheme, funcs);//R.style.SunmiDialogTheme
			dialog.getWindow().setType(2028);
            
			layout = inflater.inflate(R.layout.dialog_sunmi_shutdown_confirm, null);
			layout.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			dialog.mRestart = layout.findViewById(R.id.restart);
			dialog.mRestart.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
				         layout.setClickable(false);
					dialog.mWindowManagerFuncs.reboot(false);
					dialog.mShutdownLayout.setVisibility(View.GONE);
					dialog.mRestartLayout.setVisibility(View.GONE);
                                             dialog.mMiddle.setVisibility(View.GONE);
                                             dialog.mSuperScreen.setVisibility(View.GONE);
                                             dialog.mQuitSuper.setVisibility(View.GONE);
					dialog.mProgressbar.setVisibility(View.VISIBLE);
					WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
					lp.alpha = 1.00f;
					dialog.getWindow().setAttributes(lp);
				}
			});

                        //liuqiong@sunmi.com add 2017-12-06 begin
                        dialog.mRestartImage = layout.findViewById(R.id.restart_image);
                        dialog.mRestart.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                switch (motionEvent.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    dialog.mShutDownImage.setVisibility(View.VISIBLE);
                                    dialog.mShutdown.setVisibility(View.INVISIBLE);
                                    break;
                                case MotionEvent.ACTION_UP:
                                    dialog.mShutDownImage.setVisibility(View.INVISIBLE);
                                    dialog.mShutdown.setVisibility(View.VISIBLE);
                                    break;
                            }

                                return false;
                            }
                        });
                        //liuqiong@sunmi.com add 2017-12-06 end
                        
            			dialog.mShutdown = layout.findViewById(R.id.shutdown);
            			dialog.mShutdown.setOnClickListener(new View.OnClickListener() {
            				@Override
            				public void onClick(View v) {
            				         layout.setClickable(false);
            					dialog.mWindowManagerFuncs.shutdown(false /* confirm */);
            					dialog.mShutdownLayout.setVisibility(View.GONE);
            					dialog.mRestartLayout.setVisibility(View.GONE);
                                                         dialog.mMiddle.setVisibility(View.GONE);
                                                         dialog.mSuperScreen.setVisibility(View.GONE);
                                                         dialog.mQuitSuper.setVisibility(View.GONE);
            					dialog.mProgressbar.setVisibility(View.VISIBLE);
            					WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            					lp.alpha = 1.00f;
            					dialog.getWindow().setAttributes(lp);
            				}
            			});

                        //liuqiong@sunmi.com add 2017-12-06 begin
                        dialog.mShutDownImage = layout.findViewById(R.id.shutdown_image);
                        dialog.mShutdown.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                switch (motionEvent.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    dialog.mRestartImage.setVisibility(View.VISIBLE);
                                    dialog.mRestart.setVisibility(View.INVISIBLE);
                                    break;
                                case MotionEvent.ACTION_UP:
                                    dialog.mRestartImage.setVisibility(View.INVISIBLE);
                                    dialog.mRestart.setVisibility(View.VISIBLE);
                                    break;
                            }

                                return false;
                            }
                        });
                        //liuqiong@sunmi.com add 2017-12-06 end
                        dialog.mQuitSuper = layout.findViewById(R.id.quit_super);
                        dialog.mSuperScreen = layout.findViewById(R.id.super_screen);
                        if (1 == Settings.Global.getInt(mContext.getContentResolver(), "sunmi_super_app_mode", 0)) {
                            dialog.mSuperScreen.setVisibility(View.VISIBLE);
                            dialog.mQuitSuper.setVisibility(View.VISIBLE);
                            dialog.mQuitSuper.setOnClickListener(new View.OnClickListener() {
                				@Override
                				public void onClick(View v) {
                				    new SunmiExclusiveModeDialog(mContext).show();
                				    dialog.dismiss();
                				}
                			});
                           } else {
                            dialog.mQuitSuper.setVisibility(View.GONE);
                            dialog.mSuperScreen.setVisibility(View.GONE);
                           }
            
			dialog.mShutdownLayout = layout.findViewById(R.id.shutdown_layout);
			dialog.mRestartLayout = layout.findViewById(R.id.restart_layout);
			dialog.mProgressbar = layout.findViewById(R.id.prograssbar);
                           dialog.mMiddle = layout.findViewById(R.id.middle_icon);
                           
			dialog.mShutdownLayout.setVisibility(View.VISIBLE);
			dialog.mRestartLayout.setVisibility(View.VISIBLE);
			dialog.mProgressbar.setVisibility(View.GONE);
                           dialog.mMiddle.setVisibility(View.VISIBLE);
                           
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
