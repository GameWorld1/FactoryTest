package com.zzx.factorytest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.zzx.factorytest.bean.WifiStrength;
import com.zzx.factorytest.manager.FactoryTestManager;
import com.zzx.factorytest.manager.WifiStatusManager;
import com.zzx.factorytest.view.WifiStrengthView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WifiTestActivity extends TestItemBaseActivity implements Callback {

    private LinearLayout ll_content;
    // private WifiStateReceiver wifiReceiver;
    private List<WifiStrength> scanResultsList;

    private WifiStatusManager manager;
    private Handler mHandler;

    private WifiStrengthView strengthView;
    private Thread wifiStateMonitor;
    private final int AUTO_TEST_TIMEOUT = 10;//
    private final int AUTO_TEST_MINI_SHOW_TIME = 5;//
    private boolean thread_flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.wifi_layout);

        ll_content = (LinearLayout) findViewById(R.id.ll_content);
        // ChartFactory.getCombinedXYChartView(this, dataset, renderer, types);
        strengthView = (WifiStrengthView) findViewById(R.id.strengthView);
        manager = new WifiStatusManager(this);
        manager.openWifi();
        mHandler = new Handler(this);
        mHandler.sendEmptyMessage(0);
        wifiStateMonitor = new WifiStateMonitor();
        wifiStateMonitor.start();
        super.onCreate(savedInstanceState);

    }

    @Override
    void executeAutoTest() {
        super.startAutoTest(AUTO_TEST_TIMEOUT, AUTO_TEST_MINI_SHOW_TIME);

    }

    @Override
    protected void onDestroy() {
        try {
            if (!Thread.interrupted()) {
                thread_flag = false;
                wifiStateMonitor.interrupt();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        manager.closeWifi();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        manager.startScan();
        super.onResume();
    }

    class WifiStateMonitor extends Thread {

        @Override
        public void run() {
            while (thread_flag) {
                try {
                    mHandler.sendEmptyMessage(0);
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public boolean handleMessage(Message msg) {
        scanResultsList = manager.getWifiInfoStrength();
        if (FactoryTestManager.currentTestMode == FactoryTestManager.TestMode.MODE_AUTO_TEST) {
            checkAutoTestResult(scanResultsList);
        }
        strengthView.updateData(scanResultsList);
        manager.startScan();
        Log.i("test", "-------------------scanResultsList");
        return false;
    }

    /**
     * 检测自动测试结果
     */
    private Map<String, Integer> preStrengthMap = new HashMap<String, Integer>();

    private void checkAutoTestResult(List<WifiStrength> list) {

        boolean flag1 = false;// 撝有一个信号大于-90的
        boolean flag2 = false;// 两次信号差值大于20
        WifiStrength wifiStrength;

        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                wifiStrength = list.get(i);
                if (wifiStrength.strength > -90) {
                    flag1 = true;
                }
                if (preStrengthMap.size() > 0) {
                    Integer preStrength = preStrengthMap
                            .get(wifiStrength.label);
                    if (preStrength != null
                            && Math.abs(preStrength - wifiStrength.strength) > 20) {
                        flag2 = true;
                    }
                }
                preStrengthMap.put(wifiStrength.label, wifiStrength.strength);
            }
        }
        if (flag1 && !flag2) {
            if (!Thread.interrupted()) {
//				thread_flag = false;
                //	wifiStateMonitor.interrupt();
            }
            stopAutoTest(true);
        }
        // if (xResult && yResult && zResult) {
        // synchronized (this) {
        // mSensorManager.unregisterListener(this, gravitySensor);
        // stopAutoTest(true);
        // }
        // }
    }
}
