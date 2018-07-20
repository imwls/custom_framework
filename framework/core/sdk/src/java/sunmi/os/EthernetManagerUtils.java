package sunmi.os;

import android.net.EthernetManager;
import android.net.IpConfiguration;
import android.net.LinkAddress;
import android.net.ProxyInfo;
import android.net.NetworkUtils;
import android.content.Context;
import android.net.NetworkUtils;
import android.net.StaticIpConfiguration;
import android.util.Log;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.regex.Pattern;

/**
 * Created by jie on 18-5-29.
 * 设置有线网接口
 */
public class EthernetManagerUtils {

    public static final String TAG = "EthernetManagerUtils";

    /**
     * 有线网ip 类型为静态ip
     */
    public static final int IP_ASSIGNMENT_STATIC = 0;
    /**
     * 有线网ip 类型为动态ip
     */
    public static final int IP_ASSIGNMENT_DHCP = 1;

    /**
     * 代理类型
     */
    public static final int PROXY_SETTINGS_TYPE_NONE = 0;
    /**
     * 代理类型
     */
    public static final int PROXY_SETTINGS_TYPE_STATIC = 1;
    /**
     * 代理类型
     */
    public static final int PROXY_SETTINGS_TYPE_UNASSIGNED = 2;
    /**
     * 代理类型
     */
    public static final int PROXY_SETTINGS_TYPE_PAC = 3;

    /**
     * 设置有线网IP
     * @param context        上下文，不能为null
     * @param ipAssignment   static ip/dhcp  只能是 EthernetManagerUtils.IP_ASSIGNMENT_STATIC或则EthernetManagerUtils.IP_ASSIGNMENT_DHCP
     * @param proxySettings  代理方式，一般为 EthernetManagerUtils.PROXY_SETTINGS_TYPE_NONE
     * @param ip             static ip 设置时需要传入IP值,否则null
     * @param preFixLen      static ip 设置时子网掩码（前缀长度）,否则0
     * @param gateway        static ip 设置时需要传入网关,否则null
     * @param dnsServers     static ip 设置时需要传入多个dns用逗号','隔开，否则为null
     * @param proxyInfo      一般情况下为null,如有特殊需要请联系ROM开发
     */
    public static void setConfiguration(Context context,int ipAssignment,int proxySettings, String ip, int preFixLen, String gateway, String dnsServers, ProxyInfo proxyInfo){

        if (null == context){
	        Log.e(TAG,"context is null!");
            return ;
        }

        if (ipAssignment <0 || ipAssignment>IP_ASSIGNMENT_DHCP){
            Log.e(TAG,"ipAssignment is error:"+ipAssignment);
            return ;
        }

        if (proxySettings <0 || proxySettings>PROXY_SETTINGS_TYPE_PAC){
            Log.e(TAG,"proxySettings is error:"+proxySettings);
            return ;
        }        

        EthernetManager mEthernetManager = (EthernetManager)context.getSystemService(Context.ETHERNET_SERVICE);

        if (ipAssignment == IP_ASSIGNMENT_DHCP) {
            mEthernetManager.setConfiguration(new IpConfiguration(IpConfiguration.IpAssignment.DHCP, IpConfiguration.ProxySettings.NONE,
                    null, null));

            return;
        }

        StaticIpConfiguration staticIpCfg = new StaticIpConfiguration();

        InetAddress inetIP = getInetAddress(ip);
        if (null == inetIP){
            Log.e(TAG,"inetIP is error:"+ip);
            return;
        }
         
        if (preFixLen < 0 || preFixLen > 128){
            Log.e(TAG,"preFixLen is error:"+preFixLen);
            return;
        }

        //设置IP
        staticIpCfg.ipAddress = new LinkAddress(inetIP, preFixLen);

        //设置gateway
        InetAddress inetGateway = getInetAddress(gateway);
        if (null == inetGateway){
            Log.e(TAG,"inet4Gateway is error:"+gateway);
            return;
        }
        staticIpCfg.gateway = inetGateway;

        //设置DNS
        if (null == dnsServers || dnsServers.isEmpty()) {
            Log.e(TAG,"dnsServers is error:"+dnsServers);
            return ;
        }

        //DNS 组
        String dnsGroup[] = dnsServers.split(",");
        if (null == dnsGroup || dnsGroup.length == 0) {
            Log.e(TAG,"dnsGroup is null");
            return ;
        }

        InetAddress dnsAddr = null;
        for (int i = 0; i < dnsGroup.length; i++) {
            dnsAddr = getInetAddress(dnsGroup[i]);
            if (dnsAddr == null) {
                Log.e(TAG,"dnsGroup dnsAddr is null");
                return ;
            }

            staticIpCfg.dnsServers.add(dnsAddr);
        }


        IpConfiguration.ProxySettings proxyType = IpConfiguration.ProxySettings.NONE;
        if (1 == proxySettings){
            proxyType = IpConfiguration.ProxySettings.STATIC;
        }else if (2 == proxySettings){
            proxyType = IpConfiguration.ProxySettings.UNASSIGNED;
        }else if (3 == proxySettings){
            proxyType = IpConfiguration.ProxySettings.PAC;
        }

        mEthernetManager.setConfiguration(new IpConfiguration(IpConfiguration.IpAssignment.STATIC, proxyType,
                staticIpCfg, proxyInfo));
        
    }

    /**
     * 根据IP String获取InetAddress
     * @param text  IP 字符串
     * @return      网络地址对象
     */
    public static InetAddress getInetAddress(String text) {
        try {
            return NetworkUtils.numericToInetAddress(text);
        } catch (IllegalArgumentException | ClassCastException e) {
            return null;
        }
    }


    /**
     * 判断是否为掩码
     * @param mask 掩码字符串
     * @return     true : 是掩码  false：不是正确的掩码格式
     */
    public static boolean isValidMask(String mask) {
        Pattern pattern = Pattern.compile("(254|252|248|240|224|192|128|0)\\.0\\.0\\.0|255\\.(254|252|248|240|224|192|128|0)\\.0\\.0|255\\.255\\.(254|252|248|240|224|192|128|0)\\.0|255\\.255\\.255\\.(255|254|252|248|240|224|192|128|0)|^[1-9]$|^2\\d$|^3[0-2]$");

        return pattern.matcher(mask).matches();
    }

    /**
     * ipv4中通过mask计算前缀的长度
     * @param mask 子网掩码 例如:255.255.255.0
     * @return     前缀长度
     */
    public static int getPrefixLenByMask(String mask) {

        int preFix = -1;

        if (!isValidMask(mask)) {
            return preFix;
        }

        try {
            InetAddress inetAddress = NetworkUtils.numericToInetAddress(mask);
            if (inetAddress instanceof Inet4Address) {
                preFix = NetworkUtils.netmaskToPrefixLength((Inet4Address) inetAddress);
            }
        } catch (IllegalArgumentException e) {
            Log.d(TAG, "getPrefixLenByMask IllegalArgumentException error" + e);
        } catch (Exception e) {
            Log.d(TAG, "getPrefixLenByMask Exception error" + e);
        }
        Log.d(TAG, "getPrefixLenByMask prefix:" + preFix);
        return preFix;
    }
    
}
