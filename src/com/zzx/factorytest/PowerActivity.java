package com.zzx.factorytest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class PowerActivity extends TestItemBaseActivity {

    private TextView tv_message;
    private final int AUTO_TEST_TIMEOUT = 3;// 自动测试超时时间
    private final float AUTO_TEST_RANGE = 3.5f;// 自动测试界限值
    private final int AUTO_TEST_MINI_SHOW_TIME = 2;// 自动测试超时时间

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.power_layout);
        tv_message = (TextView) findViewById(R.id.tv_message);
        registerReceiver(mBatInfoReceiver, new IntentFilter(
                Intent.ACTION_BATTERY_CHANGED));

        super.onCreate(savedInstanceState);
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
                float current=0;
                try {
                    BufferedReader br=new BufferedReader(new FileReader("/sys/devices/platform/mt6329-battery/FG_Battery_CurrentConsumption"));
                    String value=br.readLine();
                    if(value!=null&&!"".equals(value)){
                        current=Float.parseFloat(value)/10;
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
                tv_message.setText("电压: " + v + "mV\n\n 电流: "+current+"mA\n\n 电量 : " + f1 + "%\n");

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
