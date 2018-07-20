package com.sunmi.internal.service.policy.impl.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import sunmi.R;

public class SunmiExclusiveModeDialog extends Dialog implements SunmiUpdateInterface ,View.OnClickListener{

	private Context mContext;

	private Button mBT1 ,mBT2,mBT3,mBT4,mBT5,mBT6,mBT7,mBT8,mBT9,mBT0;
	private Button mBTND,mBack;
	private SunmiCheckPwd mSunmiCheckPwd;
	private ImageView mPwdDotA,mPwdDotB,mPwdDotC,mPwdDotD;
	private LinearLayout mAnimation;

	public SunmiExclusiveModeDialog(Context context) {
		super(context,R.style.SunmiExclusiveDialogStyle);

		mContext = context;

		initDialog(context);
	}

	private void initDialog(Context context){
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.exclusive_dialog, null);
		getWindow().setType(2028);

		//getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

		//getWindow().getAttributes().privateFlags |=
		//		WindowManager.LayoutParams.PRIVATE_FLAG_SHOW_FOR_ALL_USERS;

                //getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,WindowManager.LayoutParams.FLAG_BLUR_BEHIND);


		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		lp.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_NOSENSOR;
		lp.dimAmount = 0.5f;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		addContentView(layout, lp);

		initView(layout);


            //setContentView(R.layout.exclusive_dialog);
	}

	@Override
	public void show() {
		super.show();

		setCanceledOnTouchOutside(false);

		/*WindowManager.LayoutParams lp = getWindow().getAttributes();

		WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		int height = wm.getDefaultDisplay().getHeight();

		lp.width = width;
		lp.height = height;
		getWindow().setAttributes(lp);*/
	}

	@Override
	public void dismiss() {
		super.dismiss();
	}


	////////////////////////////////////////////////////////////////////////////////////////////////////

	private void initView(View root)
	{
		mSunmiCheckPwd = new SunmiCheckPwd(this,mContext);
		mBT1 = (Button)root.findViewById(R.id.m_n1);
		mBT1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mSunmiCheckPwd.onClick(view);
			}
		});
		mBT2 = (Button)root.findViewById(R.id.m_n2);
		mBT2.setOnClickListener(this);
		mBT3 = (Button)findViewById(R.id.m_n3);
		mBT3.setOnClickListener(this);

		mBT4 = (Button)root.findViewById(R.id.m_n4);
		mBT4.setOnClickListener(this);
		mBT5 = (Button)root.findViewById(R.id.m_n5);
		mBT5.setOnClickListener(this);
		mBT6 = (Button)root.findViewById(R.id.m_n6);
		mBT6.setOnClickListener(this);


		mBT7 = (Button)root.findViewById(R.id.m_n7);
		mBT7.setOnClickListener(this);
		mBT8 = (Button)root.findViewById(R.id.m_n8);
		mBT8.setOnClickListener(this);
		mBT9 = (Button)root.findViewById(R.id.m_n9);
		mBT9.setOnClickListener(this);

		mBT0 = (Button)root.findViewById(R.id.m_n0);
		mBT0.setOnClickListener(this);


		mBTND = (Button)root.findViewById(R.id.m_nd);
		mBTND.setOnClickListener(this);
		mBack = (Button)root.findViewById(R.id.m_back_bt);
		mBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view)
			{
				dismiss();
			}
		});
		mPwdDotA = (ImageView)root.findViewById(R.id.m_pwd_dot_a);
		mPwdDotB = (ImageView)root.findViewById(R.id.m_pwd_dot_b);
		mPwdDotC = (ImageView)root.findViewById(R.id.m_pwd_dot_c);
		mPwdDotD = (ImageView)root.findViewById(R.id.m_pwd_dot_d);
		mAnimation = (LinearLayout)root.findViewById(R.id.m_pwd_dot_line);
	}


	@Override
	public void onClick(View view)
	{
		mSunmiCheckPwd.onClick(view);
	}

	private final Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			if(msg!=null)
			{
				if(msg.what==0xF1)
				{
					update_pwd_dot(mSunmiCheckPwd.getPwdInputNum());
				}
			}

		}
	};


	public void updatepwd(int num,int res)
	{

		update_pwd_dot(num);

		if(res==1)
		{
//			Toast.makeText(this,"密码正确",Toast.LENGTH_SHORT).show();
//			mHandler.sendEmptyMessageDelayed(0xF1,100);
			update_pwd_dot(0);
			dismiss();

			Intent intent =  new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
			mContext.startActivity(intent);
		}

		else if (res==2)
		{
			Toast.makeText(mContext,"密码错误",Toast.LENGTH_SHORT).show();

			TranslateAnimation animation = new TranslateAnimation(20, -20, 0, 0);
			animation.setInterpolator(new OvershootInterpolator());
			animation.setDuration(20);
			animation.setRepeatCount(5);
			animation.setRepeatMode(Animation.REVERSE);
			animation.setAnimationListener(new Animation.AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
					mPwdDotA.setBackgroundResource(R.drawable.pwd_dot_add);
					mPwdDotB.setBackgroundResource(R.drawable.pwd_dot_add);
					mPwdDotC.setBackgroundResource(R.drawable.pwd_dot_add);
					mPwdDotD.setBackgroundResource(R.drawable.pwd_dot_add);
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					mHandler.sendEmptyMessageDelayed(0xF1,300);
				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}
			});
			mAnimation.startAnimation(animation);
		}




	}

	private void update_pwd_dot(int num)
	{
		switch (num)
		{
			case 0:
			{
				mPwdDotA.setBackgroundResource(R.drawable.pwd_dot_empty);
				mPwdDotB.setBackgroundResource(R.drawable.pwd_dot_empty);
				mPwdDotC.setBackgroundResource(R.drawable.pwd_dot_empty);
				mPwdDotD.setBackgroundResource(R.drawable.pwd_dot_empty);
				break;
			}
			case 1:
			{
				mPwdDotA.setBackgroundResource(R.drawable.pwd_dot_normal);
				mPwdDotB.setBackgroundResource(R.drawable.pwd_dot_empty);
				mPwdDotC.setBackgroundResource(R.drawable.pwd_dot_empty);
				mPwdDotD.setBackgroundResource(R.drawable.pwd_dot_empty);
				break;
			}
			case 2:
			{
				mPwdDotA.setBackgroundResource(R.drawable.pwd_dot_normal);
				mPwdDotB.setBackgroundResource(R.drawable.pwd_dot_normal);
				mPwdDotC.setBackgroundResource(R.drawable.pwd_dot_empty);
				mPwdDotD.setBackgroundResource(R.drawable.pwd_dot_empty);
				break;
			}
			case 3:
			{
				mPwdDotA.setBackgroundResource(R.drawable.pwd_dot_normal);
				mPwdDotB.setBackgroundResource(R.drawable.pwd_dot_normal);
				mPwdDotC.setBackgroundResource(R.drawable.pwd_dot_normal);
				mPwdDotD.setBackgroundResource(R.drawable.pwd_dot_empty);
				break;
			}
			case 4:
			{
				mPwdDotA.setBackgroundResource(R.drawable.pwd_dot_normal);
				mPwdDotB.setBackgroundResource(R.drawable.pwd_dot_normal);
				mPwdDotC.setBackgroundResource(R.drawable.pwd_dot_normal);
				mPwdDotD.setBackgroundResource(R.drawable.pwd_dot_normal);
				break;
			}
		}
	}

}
