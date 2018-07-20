package com.sunmi.internal.service.connectivity;

import android.os.SystemProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jie on 18-3-14.
 * 网络连接服务器地址：
 * 1. 国内版本 默认用小米，连接失败用google
 * 2. 国外版本 默认用google,连接失败用小米
 * 3. 可以添加多个server地址
 *
 * 接口位置： NetworkMonitor.isCaptivePortal
 * 1. 移植参考 P2lite 中该方法修改
 *
 */

public class NetworkMonitorUtils {

    //是否国内版本
    public static final boolean IS_CHINA_REGION = "zh-CN".equals(SystemProperties.get("ro.product.locale","unknow"));
    //小米
    public static final String DEFAULT_HTTPS_URL     = IS_CHINA_REGION ?"https://connect.rom.miui.com/generate_204":"https://www.google.com/generate_204";
    public static final String DEFAULT_HTTP_URL      = IS_CHINA_REGION?"http://connect.rom.miui.com/generate_204":"http://connectivitycheck.gstatic.com/generate_204";

    //qcom
    public static final String DEFAULT_HTTPS_URL_SEC     = IS_CHINA_REGION ?"https://www.qualcomm.cn/generate_204":"https://connect.rom.miui.com/generate_204";
    public static final String DEFAULT_HTTP_URL_SEC      = IS_CHINA_REGION?"http://www.qualcomm.cn/generate_204":"http://connect.rom.miui.com/generate_204";


    public static final String DEFAULT_HTTPS_URL_THIRD     = IS_CHINA_REGION ?"https://www.google.com/generate_204":"https://www.qualcomm.cn/generate_204";
    public static final String DEFAULT_HTTP_URL_THRID      = IS_CHINA_REGION?"http://connectivitycheck.gstatic.com/generate_204":"http://www.qualcomm.cn/generate_204";


    public static final List<String> LIST_HTTPS_URL = new ArrayList<String>(){
        {add(DEFAULT_HTTPS_URL);add(DEFAULT_HTTPS_URL_SEC); add(DEFAULT_HTTPS_URL_THIRD);}
    };
    public static final List<String> LIST_HTTP_URL = new ArrayList<String>(){
        {add(DEFAULT_HTTP_URL); add(DEFAULT_HTTP_URL_SEC); add(DEFAULT_HTTP_URL_THRID);}
    };

}
