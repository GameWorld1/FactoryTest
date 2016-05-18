package com.zzx.factorytest.bean;

import com.zzx.factorytest.TestItemBaseActivity;
import com.zzx.factorytest.manager.FactoryTestManager;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class TestItem {

	public Class<? extends TestItemBaseActivity>  activityCls;
	public int iconId;
	public String label;
	public String itemName;
	private boolean enable = true;
	public boolean isAutoSupport;//
	public TestItem(Class<? extends TestItemBaseActivity> activityCls, int iconId,
			String itemName, String label,boolean isAutoSupport) {

		this.activityCls = activityCls;
		this.iconId = iconId;
		this.label = label;
		this.itemName = itemName;
		this.isAutoSupport=isAutoSupport;
	}


	public boolean isEnable() {

		boolean result = true;
		try {
			File inFile = new File("/system/etc/factory.cfg");
			if (!inFile.exists()) {
				return true;
			}
			FileInputStream fis = new FileInputStream(inFile);
			Properties properties = new Properties();
			properties.load(fis);

			if (properties.getProperty(FactoryTestManager.CONFIG_PRE_STRING + this.itemName).equals(
					"yes")) { 
				result = true;
			} else {
				result = false;
			}
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}
}
