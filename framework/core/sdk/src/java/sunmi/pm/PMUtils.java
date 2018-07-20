package sunmi.pm;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManagerNative;
import android.app.PackageInstallObserver;
import android.content.Context;
import android.content.pm.IPackageDeleteObserver;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;

/**
 * Silence Install or Uninstall APP
 * <h3>Need Sunmi Sign</h3>
 * 
 * @author gank
 */
public class PMUtils {
    
    /** 
     *  App的包名不能以 com.android. 开始
     */
    public static final int INSTALL_FAILED_FORBID_AOSP_APP = -201;

    /** 
     *  渠道后台不允许安装三方App
     */
    public static final int INSTALL_FAILED_FORBID_BY_SERVER = -202;

    /** 
     * 金融机具上的商米签名验证失败 
     */
    public static final int INSTALL_FAILED_FORBID_BY_SUNMI_SIGN = -203;
    
    /** 
     *  禁止安装平台签名的APP
     */
    public static final int INSTALL_FAILED_FORBID_BY_PLATFORM = -204;

    /**
     * 静默安装 App
     * <h4>Need Sunmi Sign</h4>
     * 
     * @param context
     * @param packageURI
     * @param observer
     */
    public static void installPackage(Context context, Uri packageURI, final InstallObserver observer) {
        PackageManager pm = context.getPackageManager();
        PackageInstallObserver obs = new PackageInstallObserver() {
            public void onPackageInstalled(String basePackageName, int returnCode, String msg, Bundle extras) {
                observer.onPackageInstalled(basePackageName, returnCode, msg, extras);
            }
        };
        pm.installPackage(packageURI, obs, PackageManager.INSTALL_REPLACE_EXISTING, context.getPackageName());
    }

    /**
     * 静默卸载 App
     * <h4>Need Sunmi Sign</h4>
     * 
     * @param context
     * @param packageName
     * @param observer
     */
    public static void deletePackage(Context context, String packageName, final DeleteObserver observer) {
        PackageManager pm = context.getPackageManager();
        IPackageDeleteObserver obs = new IPackageDeleteObserver.Stub() {
            public void packageDeleted(java.lang.String packageName, int returnCode) {
                observer.packageDeleted(packageName, returnCode);
            }
        };
        pm.deletePackage(packageName, obs, PackageManager.DELETE_ALL_USERS);
    }

    /**
     * 获取当前系统顶层 App
     * @param am
     * @return top App's pkg name
     */
    public static String getTopApp(ActivityManager am) {
        List<ActivityManager.RunningTaskInfo> appTasks = null;
        try {
            appTasks = ActivityManagerNative.getDefault().getTasks(1, 0);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (null != appTasks && !appTasks.isEmpty()) {
            return appTasks.get(0).topActivity.getPackageName();
        }else {
            return null;
        }
    }

    /**
     * 获取安装失败原因详细信息
     * @param result
     * @return
     */
    public static String getInstallFailedMsg(int result) {
        String res = "";
        switch (result) {
            case INSTALL_FAILED_FORBID_AOSP_APP:
                res = "Forbid AOSP App";
                break;
            case INSTALL_FAILED_FORBID_BY_SERVER:
                res = "Forbid By Server";
                break;
            case INSTALL_FAILED_FORBID_BY_SUNMI_SIGN:
                res = "Sunmi Sign Error";
                break;
            case INSTALL_FAILED_FORBID_BY_PLATFORM:
                res = "Forbid By Platform Key";
                break;
        }
        return res;
    }

    /**
	 * Uninstall App
	 * <h4>Need Sunmi Sign</h4>
	 * @param pm
	 * @param packageName
	 * @param observer
	 * @deprecated comment
	 */
	public static void deletePackage(PackageManager pm, String packageName, final DeleteObserver observer) {
		IPackageDeleteObserver obs = new IPackageDeleteObserver.Stub() {
			public void packageDeleted(java.lang.String packageName, int returnCode) {
				observer.packageDeleted(packageName, returnCode);
			}
		};
		pm.deletePackage(packageName, obs, PackageManager.DELETE_ALL_USERS);
	}

	/**
	 * Install App
	 * <h4>Need Sunmi Sign</h4>
	 * @param pm
	 * @param packageURI
	 * @param observer
	 * @deprecated comment
	 */
	public static void installPackage(PackageManager pm, Uri packageURI, final InstallObserver observer) {
		PackageInstallObserver obs = new PackageInstallObserver() {
			public void onPackageInstalled(String basePackageName, int returnCode, String msg, Bundle extras) {
				observer.onPackageInstalled(basePackageName, returnCode, msg, extras);
			}
		};
		pm.installPackage(packageURI, obs, PackageManager.INSTALL_REPLACE_EXISTING, "sunmi.silence");
	}
}
