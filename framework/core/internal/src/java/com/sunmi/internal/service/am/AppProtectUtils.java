package com.sunmi.internal.service.am;

import java.util.ArrayList;
import java.util.List;

import android.content.*;
import android.database.ContentObserver;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

public class AppProtectUtils {

    private static final String SUNMI_APP_PROTECT = "sunmi_app_protect";

    private static final int DEFAULT_ADJ = 1;

    private static final List<String> mApps = new ArrayList<String>();

    private static Context mContext;

    public static final void init(Context context) {
        Log.v("Gank", "AppProtectUtils init");
        //1: register observer
        mContext = context;
        Uri uri = Settings.Global.getUriFor(SUNMI_APP_PROTECT);
        mContext.getContentResolver().registerContentObserver(uri, false, mAppObserver);
        //2: Init data
        initData();
    }

    private static synchronized final void initData(){
        String pkgs = android.provider.Settings.Global.getString(mContext.getContentResolver(), SUNMI_APP_PROTECT);
        mApps.clear();
        if(pkgs != null) {
            String[] pkgArray = pkgs.split(":");
            if(pkgArray != null) {
                for(String pkg : pkgArray) {
                    mApps.add(pkg);
                    Log.v("Gank", "AppProtect PKG: " + pkg);
                }
            }
        }
    }

    public static final int computeAppOomAdj(int adj, String pkg) {
        if(pkg != null) {
            pkg = pkg.substring(0, pkg.contains(":") ? pkg.indexOf(":") : pkg.length());
        }
        if(mApps.contains(pkg)) {
            return adj < DEFAULT_ADJ ? adj : DEFAULT_ADJ;
        }else {
            return adj;
        }
    }

    private static ContentObserver mAppObserver = new ContentObserver(null) {
        @Override
        public void onChange(boolean selfChange) {
            Log.v("Gank", "mAppObserver onChange");
            initData();
        }
    };

}
