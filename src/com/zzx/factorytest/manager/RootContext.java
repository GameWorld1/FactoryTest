package com.zzx.factorytest.manager;

import java.io.OutputStream;

import android.content.Context;

public class RootContext {
	private static RootContext instance = null;
	String mShell;
	OutputStream outputStream;
//	DataInputStream inputStream;
//	DataInputStream errorInputStream;
	Process process;

	private RootContext(String paramString) throws Exception {
		this.mShell = paramString;
		init();
	}

	public static RootContext getInstance(Context paramContext) {
		
		RootContext localRootContext=null;
		if (instance != null) {
			localRootContext = instance;
			return localRootContext;
		}

		try {
			instance = new RootContext("su");
			localRootContext = instance;
		} catch (Exception localException2) {
			try {
				instance = new RootContext("/system/xbin/su");
				localRootContext = instance;
			} catch (Exception localException3) {
				try {
					instance = new RootContext("/system/bin/su");
					localRootContext = instance;
				} catch (Exception localException1) {
					localException1.printStackTrace();
				}
			}
		}
		return localRootContext;
		
	}

	private void init() throws Exception {
		if ((this.process != null) && (this.outputStream != null)) {
			this.outputStream.flush();
			this.outputStream.close();
			this.process.destroy();
		}
		
		this.process = Runtime.getRuntime().exec(this.mShell);
//		ProcessBuilder builder = new ProcessBuilder(this.mShell);  
//		builder.redirectErrorStream(false);
//		this.process=builder.start();
		this.outputStream = this.process.getOutputStream();
//		this.inputStream=new DataInputStream(this.process.getInputStream());
//		this.errorInputStream=new DataInputStream(process.getErrorStream());
		
	}

	private String system(String paramString) {
		try {
			this.outputStream.write(("LD_LIBRARY_PATH=/vendor/lib:/system/lib " + paramString + "\n").getBytes("ASCII"));
		
//			process.waitFor();
//			String error=errorInputStream.readLine();
//			if(error!=null){
//				return error;
//			}
//			String result=inputStream.readLine();
//			this.process.waitFor();
			return null;
		
		} catch (Exception localException2) {
			try {
				init();
			} catch (Exception localException1) {
				localException1.printStackTrace();
			}
		}
		return null;
	}

	public String runCommand(String command) {
		return system(command);
	}
}

