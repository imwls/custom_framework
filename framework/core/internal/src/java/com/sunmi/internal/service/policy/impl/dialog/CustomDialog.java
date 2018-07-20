package com.sunmi.internal.service.policy.impl.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.plugins.ViewPlugin;
import android.widget.ProgressBar;
import android.widget.TextView;

import sunmi.R;

public class CustomDialog extends Dialog {

	private ProgressBar mIndeterminateProgress;
	private AppUpdateProgress mDeterminateProgress;
	private TextView mMsg;

	public CustomDialog(Context context) {
		super(context);
	}

	public CustomDialog(Context context, int theme) {
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

	public void setMessage(String msg) {
		if (msg != null) {
			String[] strs = msg.split("-");
			if (strs.length == 1) {
				// Indeterminate
				mDeterminateProgress.setVisibility(View.GONE);
				mIndeterminateProgress.setVisibility(View.VISIBLE);
				mMsg.setVisibility(View.VISIBLE);
				mMsg.setText(msg);
			} else {
				// Determinate
				mDeterminateProgress.setVisibility(View.VISIBLE);
				mIndeterminateProgress.setVisibility(View.GONE);
				mMsg.setVisibility(View.GONE);
				int progress = Integer.valueOf(strs[0]);
				int max = Integer.valueOf(strs[1]);
				String proStr = getContext().getString(R.string.android_upgrading_apk_title);
				String totStr = getContext().getString(R.string.android_upgrading_apk_summary, progress, max);
				mDeterminateProgress.setInfo(progress, max, proStr, totStr);
			}
		}
	}

	public static class Builder {
		private Context mContext;

		public Builder(Context context) {
			this.mContext = context;
		}

		public CustomDialog create() {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// instantiate the dialog with the custom Theme
			final CustomDialog dialog = new CustomDialog(mContext, android.R.style.Animation);
			dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_BOOT_PROGRESS);
			dialog.getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_DIM_BEHIND | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
			View layout = inflater.inflate(R.layout.dialog_sunmi, null);
			WindowManager.LayoutParams lp = new WindowManager.LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			lp.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_NOSENSOR;
			lp.dimAmount = 1.0f;
			dialog.addContentView(layout, lp);
			dialog.mIndeterminateProgress = (ProgressBar) layout.findViewById(R.id.progress_indeterminate);
			dialog.mDeterminateProgress = (AppUpdateProgress) layout.findViewById(R.id.progress_determinate);
			dialog.mMsg = (TextView) layout.findViewById(R.id.message);
			return dialog;
		}
	}
}