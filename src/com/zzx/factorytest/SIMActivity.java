package com.zzx.factorytest;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * 描述：SIM测试Activity 陈伟斌 2013-4-11
 */
public class SIMActivity extends TestItemBaseActivity {

    private TextView txtSIMState;
    private TextView txt_sim_imsi;
    private TelephonyManager telManager;
    // private TextView txtCheckSum;
    private Handler mHandler;

    private final int AUTO_TEST_TIMEOUT = 3;// 自动测试超时时间
    private final int AUTO_TEST_MINI_SHOW_TIME = 2;// 自动测试超时时间
    private Thread simMonitorThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.sim_layout);
        telManager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);
        txtSIMState = (TextView) findViewById(R.id.txt_sim_state);
        txt_sim_imsi = (TextView) findViewById(R.id.txt_sim_imsi);

        super.onCreate(savedInstanceState);


        //
        // simMonitorThread = new Thread(new MonitorThread());
        // simMonitorThread.start();
    }

    @Override
    protected void onResume() {

        super.onResume();
        checkSIMState();
    }

    @Override
    void executeAutoTest() {
        super.startAutoTest(AUTO_TEST_TIMEOUT, AUTO_TEST_MINI_SHOW_TIME);

    }

    private void checkSIMState() {
        if (isSimCardAccess()) {
            txtSIMState.setText("可用");
            String imsi = telManager.getSubscriberId();
            txt_sim_imsi.setText(telManager.getSubscriberId());

            if (imsi != null) {
                synchronized (this) {

                    stopAutoTest(true);
                }
            }
        } else {
            txtSIMState.setText("不可用");
            synchronized (this) {

                stopAutoTest(false);
            }
        }

    }

    // class MonitorThread implements Runnable {
    //
    // public void run() {
    // while (true) {
    //
    // try {
    // mHandler.sendEmptyMessage(0);// ����sim��״̬
    // Thread.sleep(100);
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
    // }
    // }

    /**
     * 判断sim状况
     *
     * @return
     */
    private boolean isSimCardAccess() {

        boolean flag = false;
        switch (telManager.getSimState()) {
            case TelephonyManager.SIM_STATE_READY:
                flag = true;
                break;
            case TelephonyManager.SIM_STATE_ABSENT:// 无SIM卡
                flag = false;
                break;
            default://SIM卡被锁定或未知状态
                flag = false;
                break;
        }
        return flag;
    }

}
