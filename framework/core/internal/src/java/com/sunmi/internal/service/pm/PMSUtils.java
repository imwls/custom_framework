package com.sunmi.internal.service.pm;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageParser;
import android.database.Cursor;
import android.net.Uri;
import android.util.Slog;
import sunmi.os.LocaleUtils;
import sunmi.pm.PMUtils;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.provider.Settings.SettingNotFoundException;
import android.os.SystemProperties;
import sunmi.os.SuperApp;
import android.provider.Settings;
import android.app.ActivityManagerNative;
import android.util.Log;

public class PMSUtils {

	private static final String TAG = "PMUtils";

        //grant permissions for special apps
        final static List<String> mInitApp = new ArrayList<String> ();
        static {
            mInitApp.add("com.alipay.zoloz.smile");
        }

	/**
	 *
	 * @param packageName
	 * @param context
	 * @return false: not allow; true: allow.
	 */
	public static boolean allowUninstall(String packageName, Context context, int flags) {
		Slog.d(TAG, "allowUninstall PKG: " + packageName + " Flags: 0x" + Integer.toHexString(flags));
		if ((flags & PackageManager.DELETE_KEEP_DATA) == PackageManager.DELETE_KEEP_DATA) {// Replace APK
			return true;
		}
		return !ChannelUtils.inChannel(packageName, context);
	}

	/**
	 * For Sec Devices
	 * 
	 * @param context
	 * @param marketPKG
	 * @param pkg
	 * @param hashMessage
	 * @param signData
	 * @param platformPkg
	 * @param installFlags PackageManager.INSTALL_FROM_ADB;
	 * @return
	 */
	public static int allowSecInstall(Context context, String marketPKG, PackageParser.Package pkg, byte[] hashMessage,
			byte[] signData, PackageParser.Package platformPkg, int installFlags) {
		Slog.d(TAG, "allowSecInstall marketPKG: " + marketPKG + " installPKG: " + pkg.packageName);
		if (SunmiPMUtils.isSecMode(context)) {
			if (!SunmiPMUtils.allowSec(hashMessage, signData)) {
				Slog.d(TAG, "Forbid by Sunmi Sign");
				return PMUtils.INSTALL_FAILED_FORBID_BY_SUNMI_SIGN;
			}
			return allowInstall(context, marketPKG, pkg, platformPkg, installFlags);
		}
		return PackageManager.INSTALL_SUCCEEDED;
	}

	/**
	 * 0: Allow From ADB
	 * 1: Filter Android App 
	 * 2: Filter Market 
	 * 3: Filter Server
	 * 4: Filter Platform Key
	 * 
	 * @param context
	 * @param marketPKG
	 * @param pkg
	 * @param platformPkg
	 * @param installFlags PackageManager.INSTALL_FROM_ADB;
	 * @return
	 */
	public static int allowInstall(Context context, String marketPKG, PackageParser.Package pkg,
			PackageParser.Package platformPkg, int installFlags) {
		Slog.d(TAG, "allowInstall marketPKG: " + marketPKG + " installPKG: " + pkg.packageName);
		if ((installFlags&PackageManager.INSTALL_FROM_ADB) == PackageManager.INSTALL_FROM_ADB){
			return PackageManager.INSTALL_SUCCEEDED;
		}
		if (!SunmiPMUtils.allowAndroidApp(pkg.packageName)) {
			Slog.d(TAG, "Forbid Install Android App");
			return PMUtils.INSTALL_FAILED_FORBID_AOSP_APP;
		}
		if (SunmiPMUtils.allowMarket(marketPKG)) {
			return PackageManager.INSTALL_SUCCEEDED;
		}
		if (!SunmiPMUtils.allowServer(context)) {
			Slog.d(TAG, "Forbid By Sunmi Server");
			return PMUtils.INSTALL_FAILED_FORBID_BY_SERVER;
		}
		if (SunmiSignUtils.compareSignatures(pkg.mSignatures, platformPkg.mSignatures) == PackageManager.SIGNATURE_MATCH) {
			Slog.d(TAG, "Forbid By Platform Key");
			return PMUtils.INSTALL_FAILED_FORBID_BY_PLATFORM;
		}
		return PackageManager.INSTALL_SUCCEEDED;
	}

	/**
	 * 默认启动器 SunmiLauncher
	 *
	 * @param intent
	 * @param mContext
	 * @param query
	 * @return
	 */
	public static ResolveInfo defaultLauncher(Intent intent, Context mContext, List<ResolveInfo> query) {
		return SunmiPMUtils.getDefaultLauncher(intent, mContext, query);
	}

	private static class SunmiPMUtils {

		public static final String ALLOW_INSTALL = "ALLOW_INSTALL";

		public static List<String> mSunmiAppMarkets = new ArrayList<String>();

		public static final String PKG_CHROME = "com.android.chrome";
		public static final String PKG_FILTER = "com.android.";

		static {
			mSunmiAppMarkets.add("woyou.market");
			mSunmiAppMarkets.add("sunmi.silence");
		}

		public static boolean allowAndroidApp(String installPKG) {
			if (PKG_CHROME.equals(installPKG)) {
				return true;
			} else if (installPKG != null && installPKG.startsWith(PKG_FILTER)) {
				return false;
			} else {
				return true;
			}
		}

		public static boolean allowMarket(String marketPKG) {
			return mSunmiAppMarkets.contains(marketPKG);
		}

		/**
		 * 服务器是否允许正常安装
		 *
		 * @param context
		 * @return
		 */
		public static boolean allowServer(Context context) {
			// 1 关闭 2 开启
			int allowInt = android.provider.Settings.Global.getInt(context.getContentResolver(), ALLOW_INSTALL, 1);
			boolean allow = false;
			if (allowInt == 2) {
				allow = true;
			}
			return allow;
		}

		public static boolean allowSec(byte[] hashMessage, byte[] signData) {
			if (hashMessage == null || signData == null || signData.length != 256) {
				return false;
			}
			return RsaPemUtils.verifyApkSign(hashMessage, signData);
		}

		/**
		 * debug = 0
		 * 
		 * @param context
		 * @return
		 */
		public static boolean isSecMode(Context context) {
			return android.provider.Settings.Global.getInt(context.getContentResolver(), "p1_security_trans_mode",
					1) != 0;
		}

		public static final String DEFAULT_LAUNCHER = "com.woyou.launcher";
		public static final String CUSTOM_LAUNCHER_PERF = "custom_launcher";
		public static String CUSTOM_LAUNCHER = null;

		/**
		 * 默认启动器 SunmiLauncher
		 *
		 * @param intent
		 * @param mContext
		 * @param query
		 * @return
		 */
		public static ResolveInfo getDefaultLauncher(Intent intent, Context mContext, List<ResolveInfo> query) {
			try {
				if (intent.hasCategory(Intent.CATEGORY_HOME)
						&& android.provider.Settings.Global.getInt(mContext.getContentResolver(),
								android.provider.Settings.Global.DEVICE_PROVISIONED) == 1) {
					CUSTOM_LAUNCHER = android.provider.Settings.Global.getString(mContext.getContentResolver(),
							CUSTOM_LAUNCHER_PERF);
					Slog.e(TAG, "CUSTOM_LAUNCHER: " + CUSTOM_LAUNCHER);
					ResolveInfo defaultRi = null;
					for (ResolveInfo rvi : query) {
						if (DEFAULT_LAUNCHER != null && DEFAULT_LAUNCHER.equals(rvi.activityInfo.packageName)) {
							defaultRi = rvi;
							continue;
						}
						if (CUSTOM_LAUNCHER != null && CUSTOM_LAUNCHER.equals(rvi.activityInfo.packageName)) {
							return rvi;
						}
					}
					if (defaultRi != null) {
						return defaultRi;
					}
				}
			} catch (SettingNotFoundException e) {
				Slog.e(TAG, "===========" + e.getMessage());
			}
			return null;
		}
	}

	/**
	 * 渠道工具
	 */
	private static class ChannelUtils {
		/**
		 * ChannelInfoContentProvider中的anthority
		 */
		public static final String AUTHORITY = "com.sunmi.channelinfo";

		/**
		 * 默启launcher、app，预装app表名
		 */
		public static final String APP_TABLE_NAME = "appinfo";
		/**
		 * appinfo表type字段
		 */
		public static final String TYPE = "type";

		/**
		 * appinfo表packagename字段
		 */
		public static final String PACKAGE_NAME = "packagename";

		/**
		 * appinfo表的uri
		 */
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/appinfo");
		
		public static boolean inChannel(String pkg, Context context) {
			ContentResolver resolver = context.getContentResolver();
			String[] columns = new String[] { PACKAGE_NAME };
			Cursor cursor = null;
			try {
				cursor = resolver.query(CONTENT_URI, columns, null, null, null);
			} catch (Exception e) {
				Slog.e(TAG, "Error allowUninstall: " + e.getMessage());
				if (cursor != null) {
                    cursor.close();
                }
				return false;
			}
			List<String> list = new ArrayList<String>();
			if (cursor != null && cursor.moveToFirst()) {
				do {
					String pName = cursor.getString(cursor.getColumnIndex(PACKAGE_NAME));
					list.add(pName);
				} while (cursor.moveToNext());
			}
			if (cursor != null) {
	             cursor.close();
	        }
			return list.contains(pkg);
		}
	}

	private static List<String> mGMSFilter = new ArrayList<String>();

	static {
		mGMSFilter.add("GoogleBackupTransport");
		mGMSFilter.add("GoogleFeedback");
		mGMSFilter.add("GoogleLoginService");
		mGMSFilter.add("GooglePartnerSetup");
		mGMSFilter.add("GoogleServicesFramework");
		mGMSFilter.add("Maps");
		mGMSFilter.add("Phonesky");
		mGMSFilter.add("Gmail2");
		mGMSFilter.add("GmsCore");
		mGMSFilter.add("ConfigUpdater");
	}

	/**
	 * 国内版本过滤GMS的安装
	 * @param file
	 * @param parseFlags
	 * @param scanFlags
	 * @return [true : install] [false : block]
	 */
	public static boolean filterGMS(File file,  final int parseFlags, int scanFlags) {
		boolean install = true;
	    String name = file.getName();
	    if(name == null || "".equals(name) || !LocaleUtils.blockChangeLocale()) {
	        return install;
	    }
	    if(mGMSFilter.contains(name)) {
	        if((PackageParser.PARSE_IS_SYSTEM & parseFlags) == PackageParser.PARSE_IS_SYSTEM) {
                install = false;
            }
	    }
        return install;
    }

    /*
    *liuqiong@sunmi.com
    *description:wifi only not scan apks
    *2018-05-09
    *params : fname  file name    
    */
    public static boolean isNoscanAPK(String fname) {
        boolean isWifiOnly = SystemProperties.getBoolean("ro.radio.noril", false);

        if (!isWifiOnly || fname == null || fname.length() <= 0) {
            return false;
        }

        Log.d("JerryLiu", "isNoscanAPK fname = " + fname + ", ro = " +  SystemProperties.getBoolean("ro.radio.noril", false));

        String [] apks = new String[] {"TeleService", "ims"};
        for (int i = 0; i < apks.length; i++) {
            if (fname.contains(apks[i])) {
                return true;
            }
        }

        return false;
    }

    /**
     * grant sunmi app permissions
     * @author liuqiong
    */
    public static boolean isGrantPermissions(Context context,PackageParser.Package pkg) {
        Log.d("JerryLiu", "Sunmi PMSUtils isGrantPermissions pkg.packageName = " + pkg.packageName + ", m = " + SunmiSignUtils.compareSignatures(SunmiSignUtils.sunmi, pkg.mSignatures));
        
        boolean isGrant = false;
        
        if (SunmiSignUtils.compareSignatures(SunmiSignUtils.sunmi, pkg.mSignatures) == PackageManager.SIGNATURE_MATCH) {
            isGrant = true;
        } 

        //checke permission in pre-package names
        if (!isGrant){
            for(String pName : mInitApp){
               Log.d("JerryLiu", "Sunmi PMSUtils isGrantPermissions list pkg.packageName = " + pName);
               if (pName.equals(pkg.packageName)) {
                    isGrant = true;
                    break;
               }
            }
        }

        //checke permission in super apps
        if (!isGrant && ActivityManagerNative.getDefault() != null){
            String superApp = Settings.Global.getString(context.getContentResolver(), SuperApp.SUNMI_SUPER_APP_LIST);
            
            if (superApp != null && superApp.length() > 0) {
                for (String app : superApp.split(":")) {
                    Log.d("JerryLiu", "Sunmi PMSUtils isGrantPermissions superapp pkg.packageName = " + app);
                    if (app.equals(pkg.packageName)) {
                        isGrant = true;
                        break;
                   }
                }
            }
        }

        return isGrant;
    }
}
