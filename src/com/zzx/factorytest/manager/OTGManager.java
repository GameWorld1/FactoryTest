package com.zzx.factorytest.manager;

import android.content.Context;
import android.os.PowerManager;
import android.provider.Settings;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class OTGManager {
    private static OTGManager mThis;

    private PowerManager mPowerManager;

    private Context mContext;

    public static final String OTG_POWER = "otg_power";


    private OTGManager(Context context) {
        mPowerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        mContext = context;
    }

    public static OTGManager getInstance(Context context) {
        if (mThis == null) {
            mThis = new OTGManager(context);
        }
        return mThis;
    }

    public void startOTG() {

        try {
            Class<?> forName = Class.forName("android.os.PowerManager");

            Method method = forName.getMethod("otgPowerOpen", boolean.class);
            if (method != null) {
                method.setAccessible(false);
                method.invoke(mPowerManager, true);
                boolean b = Settings.System.putInt(mContext.getContentResolver(), OTG_POWER, 1);
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

    public void stopOTG() {
        try {
            Class<?> forName = Class.forName("android.os.PowerManager");

            Method method = forName.getMethod("otgPowerOpen", boolean.class);
            if (method != null) {
                method.setAccessible(false);
                method.invoke(mPowerManager, false);
                boolean b = Settings.System.putInt(mContext.getContentResolver(), OTG_POWER, 0);
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

    public static final String UART1_POWER = "uart1_power";


    public void startUART1() {

        try {
            Class<?> forName = Class.forName("android.os.PowerManager");

            Method method = forName.getMethod("uartPowerOpen", int.class,
                    boolean.class);
            if (method != null) {
                method.setAccessible(false);
                method.invoke(mPowerManager, 1, true);
                boolean b = Settings.System.putInt(mContext.getContentResolver(), UART1_POWER, 1);
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

    public void stopUART1() {

        try {
            Class<?> forName = Class.forName("android.os.PowerManager");

            Method method = forName.getMethod("uartPowerOpen", int.class,
                    boolean.class);
            if (method != null) {
                method.setAccessible(false);
                method.invoke(mPowerManager, 1, false);
                boolean b = Settings.System.putInt(mContext.getContentResolver(), UART1_POWER, 0);
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


}
