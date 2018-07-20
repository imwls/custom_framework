package com.sunmi.internal.service.time;

import android.util.NtpTrustedTime;
import android.util.TrustedTime;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.os.SystemClock;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.ArrayList;

public class NetworkTimeServer {
    private Context mContext;
    private TrustedTime mTime;
    private static NetworkTimeServer mNetworkTimeServer;
    
    private ArrayList<String> mNtpServers = new ArrayList<String>();
    private ArrayList<String> mReTryNtpServers = new ArrayList<String>();
    private String mDefaultServer;
    private static final String[] SERVERLIST =  new String[]{
            "ntp3.aliyun.com", "cn.ntp.org.cn", "cn.pool.ntp.org"
            };
    private static final String[] SERVERLIST_OVERSEAS =  new String[]{            
        "dns2.synet.edu.cn",
        "ntp-sz.chl.lantp.gwadar.cn",
        "time.android.com"
        };

    private NetworkTimeServer(Context context, TrustedTime time) {
        mContext = context;
        mTime = time;

        initNtpServer();
    }

    public static NetworkTimeServer getInstance(Context context, TrustedTime time) {
        if (mNetworkTimeServer == null) {
            mNetworkTimeServer = new NetworkTimeServer(context,  time);
         }

        return mNetworkTimeServer;
    }

    public static NetworkTimeServer getInstance() {
        return mNetworkTimeServer;
    }
    
    private void initNtpServer() {
        Locale locale = mContext.getResources().getConfiguration().locale;
        String language = locale.getLanguage(); 

        mDefaultServer = getServer();
        mNtpServers.add(mDefaultServer);
        if (language != null && "zh".equals(language)) {            
            for (String str : SERVERLIST)  {
               mNtpServers.add(str);
            }
        } else {
            for (String str : SERVERLIST_OVERSEAS)
            {
               mNtpServers.add(str);
            }
        }
    }

    public void refreshServer (int tryCount) {
        int index = tryCount % mNtpServers.size();
        Log.d("NetworkTimeServer", "mNtpServers.size() = " + mNtpServers.size() + "; index = " + index + "; mNtpServers = " + mNtpServers.get(index));
        if (mTime instanceof NtpTrustedTime) {
            setServer(mNtpServers.get(index));
            mReTryNtpServers.add(getServer());
            Log.d("Gank", "Ntp Server = " + getServer());
            mTime.forceRefresh();
            setServer(mDefaultServer);
        }
        else  {
            mReTryNtpServers.add(mDefaultServer);
            mTime.forceRefresh();
        }
    }

    public void sendBroadcast(boolean isSucess, int event) {
        Intent intent = new Intent();
        if (isSucess) {
            intent.setAction("com.sunmi.ntp.SUCCESS");
        } else {
            intent.setAction("com.sunmi.ntp.FAIL");
        }
        intent.putExtra("time", SystemClock.elapsedRealtime());
        intent.putExtra("event", event);
        String failServer = "";
        String succServer = "";
        for(int i = 0; i < mReTryNtpServers.size(); i++){
            if((i + 1 == mReTryNtpServers.size()) && isSucess){
                succServer = "[" + mReTryNtpServers.get(i) + "]";
            }else{
                failServer += "[" + mReTryNtpServers.get(i) + "]";
            }
        }
        intent.putExtra("failServer", failServer);
        intent.putExtra("succServer", succServer);
        intent.putExtra("currnet_time", System.currentTimeMillis());
        
        Log.d("ZHANGZEYUAN", (isSucess ? "Success":"Failed") + "Broad Cast time = " + SystemClock.elapsedRealtime() + ", event = " + event
            + ", failServer = " + failServer + ", succServer = " + succServer + ", currenttime = " + System.currentTimeMillis());
        mContext.sendBroadcast(intent);

        if (!isSucess) {
            mReTryNtpServers.clear();
        }
    } 

    private String getServer() {
        Log.d("JerryLiu", "getServer enter");
        String server = "";
        
        try {
            Field field = mTime.getClass().getDeclaredField("mServer");
            field.setAccessible(true);    
            server = (String)field.get(mTime);
        } catch (NoSuchFieldException e) {  
            e.printStackTrace();  
        } catch (IllegalArgumentException e) {  
            e.printStackTrace();  
        } catch (IllegalAccessException e) {  
            e.printStackTrace();  
        } 

        Log.d("JerryLiu", "getServer server = " + server);

        return server;
    }

    private void setServer(String server) {
        Log.d("JerryLiu", "setServer server = " + server);
        try {        
            Field field = mTime.getClass().getDeclaredField("mServer");
            field.setAccessible(true);    
            field.set(mTime, server); 
         } catch (NoSuchFieldException e) {  
            e.printStackTrace();  
        } catch (IllegalArgumentException e) {  
            e.printStackTrace();  
        } catch (IllegalAccessException e) {  
            e.printStackTrace();  
        } 
    }
}
