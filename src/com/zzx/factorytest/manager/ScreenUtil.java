package com.zzx.factorytest.manager;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.os.PowerManager;
import android.util.DisplayMetrics;

public class ScreenUtil {
	Context mContext;
	PowerManager pm;
	PowerManager.WakeLock mWakelock = null;

	public ScreenUtil(Context context) {
		pm = (PowerManager) context.getApplicationContext().getSystemService(Context.POWER_SERVICE);
		this.mContext = context;
	}

	public void lockScreen() {
		DevicePolicyManager mDPM = (DevicePolicyManager) mContext.getSystemService(Context.DEVICE_POLICY_SERVICE);
		mDPM.lockNow();
	}

	public static void wakeUpScreen(Context mContext,int time) {
		PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "My Tag");
		mWakeLock.acquire(time);//

	}
	
	
	/**
	 *
	 * Author:
	 * 2013-3-1
	 */
	public void releaseScreenOn() {
//		if (mWakelock != null) {
//			mWakelock.release();
//		}
	}

	public boolean isScreenOn() {
		return pm.isScreenOn();
	}

	public static int getScreenWidth(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		dm = context.getResources().getDisplayMetrics();
		return dm.widthPixels;
	}

	public static int getScreenHeight(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		dm = context.getResources().getDisplayMetrics();
		return dm.heightPixels;
	}

}
