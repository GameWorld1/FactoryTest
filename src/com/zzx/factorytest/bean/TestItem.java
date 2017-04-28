package com.zzx.factorytest.bean;

import com.zzx.factorytest.TestItemBaseActivity;

public class TestItem {

    public Class<? extends TestItemBaseActivity> activityCls;
    public int iconId;
    public String label;
    public String itemName;
    public boolean enable = true;
    public boolean isAutoSupport;//

    public TestItem(Class<? extends TestItemBaseActivity> activityCls, int iconId,
                    String itemName, String label, boolean isAutoSupport) {

        this.activityCls = activityCls;
        this.iconId = iconId;
        this.label = label;
        this.itemName = itemName;
        this.isAutoSupport = isAutoSupport;
    }


    public boolean isEnable() {

        return enable;
    }
}
