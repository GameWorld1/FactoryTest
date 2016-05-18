package com.zzx.factorytest.manager;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;

public class SystemUtils {


	public static String getSoftWareVersion(Context context) {
		String sVersion = null;
		try {
			SimpleDateFormat date = new SimpleDateFormat("MM dd yyyy HH:mm:ss");
			sVersion = date.format(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).lastUpdateTime);
		} catch (Exception e) {
			sVersion = "unknown";
			e.printStackTrace();
		}
		return sVersion;
	}

	
	/**
	 * 
	 *
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static String getAppVersion(Context context,String packageName){
		try{
			PackageManager pm = context.getPackageManager();
			PackageInfo packageInfo;
			packageInfo = pm.getPackageInfo(packageName, 0);// ȡ�õ�ǰ�İ汾��Ϣ
			String versionName = packageInfo.versionName;
			return versionName;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * ����������ȡϵͳ����
	 * @param field
	 * @return
	 */
	public static String getSystemProperties(String field) {
		String platform = null;
		try {
			Class<?> classType = Class.forName("android.os.SystemProperties");
			Method getMethod = classType.getDeclaredMethod("get", new Class<?>[] { String.class });
			platform = (String) getMethod.invoke(classType, new Object[] { field });
		} catch (Exception e) {
			e.printStackTrace();
		}
		return platform;
	}

	/**
	 * 
	 * ����������ȡFlash��С
	 * @return
	 */
	public static String getTotalFlash(Context context) {
		File root = Environment.getDataDirectory();
		StatFs sf = new StatFs(root.getPath());
		long blockSize = sf.getBlockSize();
		long blockCount = sf.getBlockCount();
		return Formatter.formatFileSize(context, blockSize * blockCount);
	}

	/**
	 * 
	 * ����������ȡ�ڴ�����С
	 * @return
	 */
	public static String getTotalMemory(Context context) {
		String[] arrayOfString;
		long initial_memory = 0;
		try {
			FileReader localFileReader = new FileReader("/proc/meminfo"); // ϵͳ�ڴ���Ϣ�ļ�
			BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
			String meminfo = localBufferedReader.readLine();// ��ȡmeminfo��һ�У�ϵͳ���ڴ��С
			arrayOfString = meminfo.split("\\s+");
			initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// ���ϵͳ���ڴ棬��λ��KB������1024ת��ΪByte
			localBufferedReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Formatter.formatFileSize(context, initial_memory);// Byteת��ΪKB����MB���ڴ��С���
	}



	/**
	 * @return ϵͳ�汾��Ϣ
	 */
	public static String[] getVersion() {
		String[] version = { "null", "null", "null", "null", "null" };
		String str1 = "/proc/version";
		String str2;
		String[] arrayOfString;
		try {
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
			str2 = localBufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
			version[0] = arrayOfString[2];// KernelVersion
			localBufferedReader.close();
		} catch (IOException e) {
		}
		version[1] = Build.VERSION.RELEASE;// firmware version
		version[2] = Build.MODEL;// model
		version[3] = Build.DISPLAY;// system version
		version[4] = Build.BRAND;// brand
		return version;
	}
}
