package com.sunmi.internal.service.policy.impl.dialog;

import android.util.Log;
import android.view.View;
import sunmi.os.SuperApp;
import android.util.Log;
import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;

public class SunmiCheckPwd
{
    private static final String TAG = "SunmiCheckPwd";
    private String mTempPwd;
    private SunmiUpdateInterface mSunmiUpdateInterface;
    private Context mContext;
    public SunmiCheckPwd(SunmiUpdateInterface iter, Context context)
    {
        mTempPwd = "";
        mSunmiUpdateInterface = iter;
        mContext = context;
    }
    public int getPwdInputNum()
    {
        return mTempPwd.length();
    }
    public void onClick(View view)
    {
        if(view==null) return;
        String tag = (String)view.getTag();
        if(tag!=null)
        {
            switch (tag)
            {
                case "N1":
                {
                    mTempPwd+="1";
                    break;
                }
                case "N2":
                {
                    mTempPwd+="2";
                    break;
                }
                case "N3":
                {
                    mTempPwd+="3";
                    break;
                }
                case "N4":
                {
                    mTempPwd+="4";
                    break;
                }
                case "N5":
                {
                    mTempPwd+="5";
                    break;
                }
                case "N6":
                {
                    mTempPwd+="6";
                    break;
                }
                case "N7":
                {
                    mTempPwd+="7";
                    break;
                }
                case "N8":
                {
                    mTempPwd+="8";
                    break;
                }
                case "N9":
                {
                    mTempPwd+="9";
                    break;
                }
                case "N0":
                {
                    mTempPwd+="0";
                    break;
                }
                case "ND":
                {
                     if(mTempPwd.length()>=1)
                     {
                         mTempPwd = mTempPwd.substring(0,mTempPwd.length()-1);
                     }
                    break;
                }
            }
        }

        if(mTempPwd.length()==4)
        {

            String pwd = "1234";
	    try {
		pwd = Settings.Global.getString(mContext.getContentResolver(), SuperApp.SUNMI_SUPER_APP_PASSWORD);
	    }catch (Exception e){
		Log.d(TAG,"Exception e:"+e);
	    }
            
            if (TextUtils.isEmpty(pwd)){
                pwd = "1234";
            }

            if(mTempPwd.equals(pwd))
            {
                mSunmiUpdateInterface.updatepwd(mTempPwd.length(),1);
                mTempPwd="";
            }
            else
            {
                mSunmiUpdateInterface.updatepwd(mTempPwd.length(),2);
                mTempPwd="";
            }
        }
        else
        {
            mSunmiUpdateInterface.updatepwd(mTempPwd.length(),0);
        }
        //Log.e("ZHANGZEYUAN","=======>mTempPwd="+mTempPwd);
    }
}
