package com.zzx.factorytest.manager;

import android.content.Context;
import android.location.LocationManager;
import android.os.PowerManager;
import android.preference.CheckBoxPreference;
import android.provider.Settings;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class GPSManager {
    public static final String TAG = "GPSManager";
    public static final String GPS = "GPS";
    public static final String BEIDOU = "BEIDOU";
    public static final String UART2_POWER = "uart2_power";


    private Context mContext;
    private LocationManager mLocationManager;
    private PowerManager mPowerManager;
    private CheckBoxPreference mCbp;
    private boolean isGps;
    private boolean isNetWork;

    public GPSManager(Context context) {

        mLocationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        mPowerManager = (PowerManager) context
                .getSystemService(Context.POWER_SERVICE);
        this.mContext = context;

        isGps = isGPSEnable();
    }

    public LocationManager getLocationManager() {
        return mLocationManager;
    }

    public boolean isGPSEnable() {
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public void startGPS() {
        boolean gpsEnabled = Settings.Secure.isLocationProviderEnabled(
                mContext.getContentResolver(), LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            // 启动GPS
            Settings.Secure.setLocationProviderEnabled(mContext.getContentResolver(), LocationManager.GPS_PROVIDER, true);
            //           Settings.Secure.setLocationProviderEnabled(mContext.getContentResolver(), LocationManager.NETWORK_PROVIDER, true);
            Log.d(TAG, "GPS启动：" + isGPSEnable());
        }

    }

    public void stopGPS() {
        boolean gpsEnabled = Settings.Secure.isLocationProviderEnabled(
                mContext.getContentResolver(), LocationManager.GPS_PROVIDER);
        if (!isGps && gpsEnabled) {
            // 关闭GPS
            Settings.Secure.setLocationProviderEnabled(mContext.getContentResolver(), LocationManager.GPS_PROVIDER, false);
            //         Settings.Secure.setLocationProviderEnabled(mContext.getContentResolver(), LocationManager.NETWORK_PROVIDER, false);
            Log.d(TAG, "GPS关闭：" + isGPSEnable());
        }

    }

    public void startBeiDou() {

        try {
            Class<?> forName = Class.forName("android.os.PowerManager");

            Method method = forName.getMethod("uartPowerOpen", int.class,
                    boolean.class);
            if (method != null) {
                method.setAccessible(false);
                method.invoke(mPowerManager, 2, true);
                boolean b = Settings.System.putInt(mContext.getContentResolver(), UART2_POWER, 1);
                Log.d(TAG, "北斗启动：" + b);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    public void stopBeiDou() {
        try {
            Class<?> forName = Class.forName("android.os.PowerManager");

            Method method = forName.getMethod("uartPowerOpen", int.class,
                    boolean.class);
            if (method != null) {
                method.setAccessible(false);
                method.invoke(mPowerManager, 2, false);
                boolean b = Settings.System.putInt(mContext.getContentResolver(), UART2_POWER, 0);
                Log.d(TAG, "北斗关闭：" + b);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    // /**
    // * �л�GPS״̬
    // *
    // * ��ΰ�� 2012-2-10
    // */
    // public void toggleGPS() {
    //
    // boolean gpsEnabled = Settings.Secure.isLocationProviderEnabled(
    // mContext.getContentResolver(), LocationManager.GPS_PROVIDER);
    // if (gpsEnabled) {
    //
    // // �ر�GPS
    // Settings.Secure.setLocationProviderEnabled(
    // mContext.getContentResolver(),
    // LocationManager.GPS_PROVIDER, false);
    // } else {
    // // ��GPS
    // Settings.Secure.setLocationProviderEnabled(
    // mContext.getContentResolver(),
    // LocationManager.GPS_PROVIDER, true);
    //
    // }
    // }
}
