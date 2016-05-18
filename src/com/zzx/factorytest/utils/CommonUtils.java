package com.zzx.factorytest.utils;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.EditText;

public class CommonUtils {

	/**
	 * ˵��:�������Ƿ�װ
	 * Author:��ΰ��
	 * 2013-1-4
	 * @param intent �����ó����intent
	 * @param context 
	 * @return
	 */
	public static boolean checkAppInstalled(Intent intent, Context context) {
		PackageManager manager = context.getPackageManager();
		List<ResolveInfo> list = manager.queryIntentActivities(intent, 0);
		if (list == null || list.size() < 1)
			return false;
		return true;
	}

	/**
	 * 
	 * @param context
	 * @param className
	 * @return
	 */
	public static boolean isServiceRunning(Context context, String className) {

		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(Integer.MAX_VALUE);
		if (!(serviceList.size() > 0)) {
			return false;
		}
		for (int i = 0; i < serviceList.size(); i++) {
			if (serviceList.get(i).service.getClassName().equals(className) == true) {
				isRunning = true;
				break;
			}
		}
	
		return isRunning;

	}
//	
//	/**
//	 * ˵��:�ж�SD���Ƿ����
//	 * Author:��ΰ��
//	 * 2012-12-7
//	 * @return
//	 */
//	public static boolean isSDExists(Context context) {
//		int currentMode = LeaderFactory.getCurrentMode(context);
//		if (currentMode == Constants.CurrentMode.EVDO) {// ���������ƽ̨��ѡ��Ĭ�Ϲ���
//			return isExternalStorageAvailable();
//		} else {
//			long availableMemorySize = getAvailableMemorySize();
//			return availableMemorySize > 0;
//		}
//
//	}

	public static boolean isExternalStorageAvailable() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

	/**  
	    * ��ȡ�ֻ�sdcard2���ÿռ��С 
	    * @return 
	    */
	public static long getAvailableMemorySize() {
		String sdcard2 = "/mnt/sdcard2/";
		if (isExternalStorageAvailable()) {
			// File path =
			// Environment.getExternalStorageDirectory();//��ȡSDCard��Ŀ¼
			StatFs stat = new StatFs(sdcard2);
			long blockSize = stat.getBlockSize();
			long availableBlocks = stat.getBlockCount();
			// long availableBlocks = stat.getAvailableBlocks();
			return availableBlocks * blockSize;
		} else {
			return -1;
		}
	}

	public static String date2Str(Date date, String splitStr) {

		if (splitStr == null) {
			splitStr = "-";
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy" + splitStr + "MM" + splitStr + "dd");
		return df.format(date);
	}

	/**
	 * ˵��:ȡ�õ�ǰ���е�acitity�İ���
	 * Author:��ΰ��
	 * 2013-1-8
	 * @param context
	 * @return
	 */
	public static String getCurrentActivityPkgName(Context context) {

		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
		// String className=cn.getClassName();
		return cn.getPackageName();
	}
	
	/**
	 * ˵��:ȡ�õ�ǰ���е�acitity������
	 * Author:��ΰ��
	 * 2013-1-8
	 * @param context
	 * @return
	 */
	public static String getCurrentActivityClsName(Context context) {
		
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
		// String className=cn.getClassName();
		return cn.getClassName();
	}

	public static boolean isAtHome(Context context) {

		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
		Intent intent = new Intent();
		intent.setComponent(cn);
		PackageManager pm = context.getPackageManager();
		List<ResolveInfo> list = pm.queryIntentActivities(intent, 0);
		if (list == null || list.size() < 1) {
			return false;
		}
		ResolveInfo resolveInfo = list.get(0);
		Iterator<String> iterable = resolveInfo.filter.categoriesIterator();
		while (iterable.hasNext()) {
			if (Intent.CATEGORY_HOME.equals(iterable.next())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * ˵��:ȡ�÷��ʷ�����������
	 * Author:��ΰ��
	 * 2013-1-7
	 * @return
	 */
	public static String getAccessPassword() {
		int nRand = (int) (Math.random() * 9000 + 1000);// ������λ�����
		String password = nRand + "" + (char) ('A' + (nRand / 1000 - 1) % 26)
				+ (char) ('A' + (nRand / 1000 + nRand % 1000 / 100 - 1) % 26)
				+ (char) ('A' + (nRand / 1000 + nRand % 1000 / 100 + nRand % 100 / 10 - 1) % 26)
				+ (char) ('A' + (nRand / 1000 + nRand % 1000 / 100 + nRand % 100 / 10 + nRand % 10 - 1) % 26);
		return password;
	}

	/**
	 * ˵��:���intent�Ƿ���Ч
	 * Author:��ΰ��
	 * 2013-1-22
	 * @param context
	 * @param intent
	 * @return
	 */
	public static boolean isIntentAvailable(Context context, Intent intent) {

		final PackageManager packageManager = context.getPackageManager();

		List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;

	}

	/**
	* ת���ļ���С������K��M
	* @param fileSize 
	* @return
	*/
	public static String convertFileSize(long fileSize) {
		String newSize = "";
		if (fileSize < 1024) {
			newSize = fileSize + "B";
		} else if (fileSize >= 1024 && fileSize < 1024 * 1024) {
			newSize = String.valueOf(fileSize / 1024) + "K";
		} else {
			DecimalFormat format = new DecimalFormat("0.00");
			String result = format.format((double) fileSize / (1024 * 1024));
			newSize = result + "M";
		}

		return newSize;
	}

	/**
	 * 
	 * �����������ж��Ƿ񱣴���sdcard�����ֻ��ڲ��洢�У�
	 * ����Ҫ��ѹ��Ҫ���浽sdcard����ܽ�ѹ�������ֻ��ڲ��洢��
	 * @param flag
	 * @return
	 */
	static public String getSavePath(boolean flag) {
		String result = "/mnt/sdcard/";
		if (flag == false) {
			if (getAvailableMemorySize() > 0) {
				result = "/mnt/sdcard2/";
			} else {
				result = "/mnt/sdcard/";
			}
		}
		return result;
	}

	public static String getXORString(String str, String encode) {
		try {
			byte[] bt = str.getBytes(encode);
			byte checkValue = bt[0];

			for (int i = 1; i < bt.length; i++) {
				if (bt[i] == 44) {
					continue;
				}
				checkValue ^= bt[i];
			}
			String strRes = Integer.toHexString(checkValue & 0xFF).toUpperCase();
			if (strRes.length() == 1) {
				strRes = "0" + strRes;
			}
			return strRes;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

	

	/**
	 * 
	 * �����������������ļ���ȡ��Ϣ
	 * @param context
	 * @param key
	 * @param value
	 */
	/*
	 * public static String readProperties(Context context,String key){
	 * String strValue = "";
	 * Properties props = new Properties();
	 * try {
	 * props.load(context.getAssets().open("init.properties"));
	 * strValue = props.getProperty (key);
	 * }
	 * catch (FileNotFoundException e) {
	 * }
	 * catch (IOException e) {
	 * }
	 * return strValue;
	 * }
	 * 
	 * 
	 * 
	 * 
	 * /*
	 * 
	 * 
	 * ����������java ��ȡ�ɱ�UUID
	 * 
	 * @return
	 */

	public String getMyUUID() {
		UUID uuid = UUID.randomUUID();
		String uniqueId = uuid.toString();
		Log.d("debug", "----->UUID" + uuid);

		return uniqueId;

	}

	/**
	 * 
	 * ������������ȡ����UUID
	 * @param context
	 * @return
	 */
	public static String getUUID(Context context) {
		final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		final String tmDevice, tmSerial, tmPhone, androidId;
		tmDevice = "" + tm.getDeviceId();
		tmSerial = "" + tm.getSimSerialNumber();
		androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
		UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
		String uniqueId = deviceUuid.toString();
		Log.d("debug", "uuid=" + uniqueId);
		return uniqueId;

	}



	/**
	 * 
	 * ������������ȡEditText������ڵ�λ��
	 * @param mEditText
	 * @return
	 */
	public static int getEditTextCursorIndex(EditText mEditText){  
	 return mEditText.getSelectionStart();  
	} 
	
    public static  String keep2Point(double number) {
    	return String .format("%.2f",number);
    }
	
}
