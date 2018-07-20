package sunmi.os;

import java.util.Locale;

import com.android.internal.app.LocalePicker;

import android.os.RemoteException;
import android.os.SystemProperties;

/**
 * 语言二合一接口
 * @author gank
 *
 */
public class LocaleUtils {

	private static final String AOSP_PROP_LOCALE 	= "ro.product.locale";
	private static final String DEFAULE_NULL 		= "null";

	private static final String PROP_LOCALE 		= "ro.sunmi.locale";
	private static final String PROP_TIMEZONE 		= "ro.sunmi.timezone";

	/**
	 * 商米默认地区（中国）
	 */
	public static final String DEFAULE_LOCALE 		= "zh-CN";
	private static final String DEFAULE_LOCALE_ORI 	= "zh_CN";
	private static final String DEFAULE_LOCALE_EN 	= "en-US";
	private static final String DEFAULE_LOCALE_EN_ORI 	= "en_US";
	/**
	 * 商米默认时区（上海）
	 */
	public static final String DEFAULE_TIMEZONE 	= "Asia/Shanghai";


	/**
	 * 获取系统默认时区
	 * @return zh-CN or en-US
	 */
	public static String getDefaultLocale() {
		String locale = SystemProperties.get(PROP_LOCALE, DEFAULE_NULL);
		//1: Sunmi Locale: zh_CN or en_US
		if(DEFAULE_LOCALE_ORI.equals(locale)) {
			return DEFAULE_LOCALE;
		}
		if(DEFAULE_LOCALE_EN_ORI.equals(locale)) {
			return DEFAULE_LOCALE_EN;
		}
		//2: Sunmi Locale: zh-CN or en-US
		if(!DEFAULE_NULL.equals(locale)){
			return locale;
		}
		//3: AOSP Locale: zh-CN or en-US
		return SystemProperties.get(AOSP_PROP_LOCALE, DEFAULE_LOCALE);
	}

	/**
	 * 获取系统默认时区
	 * @return Asia/Shanghai
	 */
	public static String getDefaultTimezone() {
		String timezone = SystemProperties.get(PROP_TIMEZONE, DEFAULE_TIMEZONE);
		return timezone;
	}

	/**
	 * App 根据默认语言决定自己的逻辑， 此接口不推荐使用
	 * @deprecated comment
	 * @return [国内 : true] [国际 : false]
	 */
	public static boolean blockChangeLocale() {
		return DEFAULE_LOCALE.equals(getDefaultLocale());
	}

	/**
	 * 更新系统 Locale
	 * @param locale
	 * @throws RemoteException
	 */
	public static void updateLocale(Locale locale) throws RemoteException {
		LocalePicker.updateLocale(locale);
	}

}
