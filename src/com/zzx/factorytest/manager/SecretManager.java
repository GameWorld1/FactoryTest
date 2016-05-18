package com.zzx.factorytest.manager;

import java.io.File;

/**
 *
 * @author bin
 *
 */
public class SecretManager {

	//public boolean native validateAuthority(String screntCode);
	//�ж��Ƿ������������Ȩ��
	public static boolean isSoftWareAuthority(){
		
		try {
			
			File file=new File("sys/devices/platform/zzxcomm-factorytest");
			if(file.exists()){
				return true;
			}else
			{
				return false;
			}
//			FileInputStream fis=new FileInputStream(file);
//			Properties properties=new Properties();
//			properties.load(fis);
//			fis.close();
//			String testEnable=properties.getProperty("ro.config.factory");
//			if("1".equals(testEnable)){
//				return true;
//			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;//TODO
		
		
	}
}
