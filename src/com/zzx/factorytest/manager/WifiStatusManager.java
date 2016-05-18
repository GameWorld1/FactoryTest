package com.zzx.factorytest.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;

import com.zzx.factorytest.bean.WifiStrength;

/**
 *
 * @{# WifiChangeManager.java Create on 2012-11-28 上午10:13:33
 *
 *     类功能说明:Wifi操作管理类
 * @Version 1.0
 * @Author 李伟豪
 *
 *
 */
public class WifiStatusManager {
    private Context mContext;
    private WifiManager mWifiManager;
    private WifiInfo mWifiInfo;
    private List<ScanResult> mWifiList;// 扫描出的网络连接列表
    private List<WifiConfiguration> mWifiConfiguration;// 网络连接列表
    private WifiLock mWifiLock = null;// wifi锁
    private static final String LOCKNAME = "hsj_wifi";
    private final static ArrayList<Integer> channelsFrequency = new ArrayList<Integer>(
            Arrays.asList(0, 2412, 2417, 2422, 2427, 2432, 2437, 2442, 2447,
                    2452, 2457, 2462, 2467, 2472, 2484));
    // 定义几种加密方式，一种是WEP，一种是WPA，还有没有密码的情况
    public enum WifiCipherType {
        WIFICIPHER_WEP, WIFICIPHER_WPA, WIFICIPHER_NOPASS, WIFICIPHER_INVALID
    }

    public WifiStatusManager(Context mContext) {
        this.mContext = mContext;
        this.mWifiManager = (WifiManager) mContext
                .getSystemService(Context.WIFI_SERVICE);

        this.mWifiInfo = mWifiManager.getConnectionInfo();

    }

    public boolean isWifiEnabled() {
        return mWifiManager.isWifiEnabled();
    }

    /**
     *
     * 方法描述：打开wifi
     */
    public void openWifi() {
        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);
        }
    }

    /**
     *
     * 方法描述：关闭wifi
     */
    public void closeWifi() {
        if (mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
        }
    }

    /**
     *
     * 方法描述：锁定wifi
     */
    public void lockWifi() {
        mWifiLock.acquire();
    }

    /**
     *
     * 方法描述：解锁wifi
     */
    public void rlockWifi() {
        if (mWifiLock.isHeld()) {
            mWifiLock.acquire();
        }
    }

    /**
     *
     * 方法描述：创建一个wifilock
     */
    public void createWifiLock() {
        mWifiLock = mWifiManager.createWifiLock(LOCKNAME);
    }

    /**
     *
     * 方法描述：扫描wifi
     */
    public void startScan() {
        mWifiManager.startScan();
        // 得到扫描结果
        mWifiList = mWifiManager.getScanResults();
        // 得到配置好的网络连接
        mWifiConfiguration = mWifiManager.getConfiguredNetworks();
    }

    /**
     *
     * 方法描述：得到网络列表
     *
     * @return
     */
    public List<ScanResult> getWifiList() {
        return mWifiList;
    }

    /**
     *
     * 方法描述：查看扫描结果
     *
     * @return
     */
    public StringBuilder lookUpScan() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < mWifiList.size(); i++) {
            stringBuilder
                    .append("Index_" + new Integer(i + 1).toString() + ":");
            // 将ScanResult信息转换成一个字符串包
            // 其中把包括：BSSID、SSID、capabilities、frequency、level
            stringBuilder.append((mWifiList.get(i)).toString());
            stringBuilder.append("\n");
        }
        return stringBuilder;
    }

    /**
     *
     * 方法描述： 得到MAC地址
     *
     * @return
     */
    public String getMacAddress() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
    }

    /**
     *
     * 方法描述：得到接入点的BSSID
     *
     * @return
     */
    public String getBSSID() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
    }

    /**
     *
     * 方法描述：得到IP地址
     *
     * @return
     */
    public int getIPAddress() {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
    }

    /**
     *
     * 方法描述：得到WifiInfo的所有信息包
     *
     * @return
     */
    public String getWifiInfo() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.toString();
    }

    /**
     * Wifi的连接速度及信号强度：
     */
    public List<WifiStrength> getWifiInfoStrength() {
        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();// 当前wifi连接信息
        List<WifiStrength> list = new ArrayList<WifiStrength>();
        WifiStrength wifiStrength = new WifiStrength();
        // if (wifiInfo.getBSSID() != null) {

        List<ScanResult> scanResults = mWifiManager.getScanResults();// 搜索到的设备列表
        if (scanResults != null) {
            for (ScanResult scanResult : scanResults) {

                wifiStrength = new WifiStrength();
                wifiStrength.label = scanResult.SSID;
                wifiStrength.strength = scanResult.level;
                wifiStrength.channel =getChannelFromFrequency(scanResult.frequency);//mWifiManager.calculateSignalLevel(scanResult.level, 5);
                list.add(wifiStrength);

            }
        }
        return list;

    }
    public static int getChannelFromFrequency(int frequency) {
        return channelsFrequency.indexOf(Integer.valueOf(frequency));
    }

}
