package com.zzx.factorytest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.TextView;

import com.zzx.factorytest.bean.PlatformBean;
import com.zzx.factorytest.help.PlatformHelp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.HashMap;

public class PowerActivity extends TestItemBaseActivity {

    private TextView mMessageTv;
    private final int AUTO_TEST_TIMEOUT = 3;// 自动测试超时时间
    private final float AUTO_TEST_RANGE = 3.5f;// 自动测试界限值
    private final int AUTO_TEST_MINI_SHOW_TIME = 2;// 自动测试超时时间

    private HashMap<String, String> mMaps = new HashMap<String, String>();
    private String mPath;
    private PlatformBean mPlatform;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.power_layout);
        super.onCreate(savedInstanceState);
        push();
        findView();
        initializeView();
        registerReceiver(mBatInfoReceiver, new IntentFilter(
                Intent.ACTION_BATTERY_CHANGED));


    }

    private void initializeView() {
        mPath = mMaps.get(mPlatform.Name);
    }

    private void findView() {

        mMessageTv = (TextView) findViewById(R.id.tv_message);
    }

    private void push() {

        mMaps.put("M3089", "/sys/devices/platform/mt6320-battery/FG_Battery_CurrentConsumption");
        mMaps.put("T80", "");
        mMaps.put("S50", "");
        mMaps.put("M4082", "");
        mMaps.put("T71V3", "/sys/devices/platform/mt-battery/BatteryNotify");
        mMaps.put("P60V2", "/sys/devices/platform/mt-battery/BatteryNotify");

        mPlatform = PlatformHelp.getPlatform(this);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mBatInfoReceiver);
        super.onDestroy();
    }

    @Override
    void executeAutoTest() {
        super.startAutoTest(AUTO_TEST_TIMEOUT, AUTO_TEST_MINI_SHOW_TIME);

    }

    /* 创建广播接收器 */
    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            /*
             * 如果捕捉到的action是ACTION_BATTERY_CHANGED， 就运行onBatteryInfoReceiver()
			 */
            if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
                // int n = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0); //
                // 目前电量
                int v = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0); // 电池电压
                float current = 0;
                try {

                    BufferedReader br = new BufferedReader(new FileReader(mPath));
                    String value = br.readLine();
                    if (value != null && !"".equals(value)) {
                        current = Float.parseFloat(value) / 10;
                    }
                    br.close();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                float level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
                int temperature = intent.getIntExtra(
                        BatteryManager.EXTRA_TEMPERATURE, 0); // 电池温度

                BigDecimal bg = new BigDecimal((level * 100 / scale));
                double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP)
                        .doubleValue();
                mMessageTv.setText("电压: " + v + "mV\n\n 电流: " + current + "mA\n\n 电量 : " + f1 + "%\n");

                if (v >= AUTO_TEST_RANGE) {
                    synchronized (this) {
                        stopAutoTest(true);
                    }
                } else {
                    synchronized (this) {
                        stopAutoTest(false);
                    }
                }
            }
        }
    };
}
