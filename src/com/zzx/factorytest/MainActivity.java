package com.zzx.factorytest;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.zzx.factorytest.bean.PlatformBean;
import com.zzx.factorytest.help.PlatformHelp;
import com.zzx.factorytest.manager.BluetoothManager;
import com.zzx.factorytest.manager.FactoryTestManager;
import com.zzx.factorytest.view.SerialPortDialog;

import static com.zzx.factorytest.R.id.btn_singalTest;

public class MainActivity extends Activity implements OnClickListener {
    private static final String BUILD_DATA = "ro.build.date";
    private PlatformBean mPlatformBean;
    private Button mSingalTestBtn, mAutoTestBtn, mResultTestBtn,
            mExitBtn, mRecoverBtn, mAgeingTestBtn, mSerialPortConfigBtn;
    private TextView mVersionTv;
    private LocationManager mLocationManager;
    private BluetoothManager bluetoothManager;
    public static long tTFFStart = SystemClock.elapsedRealtime();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        findView();
        initializeView();
        setListener();
        initDeviceState();


    }

    @Override
    protected void onDestroy() {
        bluetoothManager.unregisterBluethoothReceiver();
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.restartPackage(this.getApplication().getPackageName());
        super.onDestroy();
    }

    private void findView() {
        mVersionTv = ((TextView) findViewById(R.id.version));
        mSingalTestBtn = (Button) findViewById(btn_singalTest);
        mResultTestBtn = (Button) findViewById(R.id.btn_result_Test);
        mExitBtn = (Button) findViewById(R.id.btn_exit);
        mAutoTestBtn = (Button) findViewById(R.id.btn_auto_Test);
        mAgeingTestBtn = (Button) findViewById(R.id.btn_ageing_test);
        mRecoverBtn = (Button) findViewById(R.id.btn_recover);
        mSerialPortConfigBtn = (Button) findViewById(R.id.serial_port);
        mSerialPortConfigBtn.setVisibility(View.GONE);
    }

    private void initializeView() {
        String data = RegTestActivity.SystemPropertiesProxy.get(this, BUILD_DATA);
        mPlatformBean = PlatformHelp.getPlatform(this);
        System.out.println(mPlatformBean.toString());
        if (null == mPlatformBean.Name) {
            finish();
        }
        mVersionTv.setText(getResources().getString(R.string.platform_name,
                mPlatformBean.Name, getVersion(), data));

    }

    private void setListener() {

        mSingalTestBtn.setOnClickListener(this);
        mResultTestBtn.setOnClickListener(this);
        mAutoTestBtn.setOnClickListener(this);
        mAgeingTestBtn.setOnClickListener(this);
        mExitBtn.setOnClickListener(this);
        mRecoverBtn.setOnClickListener(this);
        mSerialPortConfigBtn.setOnClickListener(this);
    }

    private void initDeviceState() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        bluetoothManager = new BluetoothManager(this);
        bluetoothManager.openBluetooth();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_singalTest:
                FactoryTestManager.currentTestMode = FactoryTestManager.TestMode.MODE_SINGAL_TEST;
                startActivity(new Intent(this, TestGridActivity.class));

                break;
            case R.id.btn_auto_Test:
                FactoryTestManager.currentTestMode = FactoryTestManager.TestMode.MODE_AUTO_TEST;
                startActivity(new Intent(this, TestGridActivity.class));

                break;
            case R.id.btn_result_Test:
                FactoryTestManager.currentTestMode = FactoryTestManager.TestMode.MODE_RESULT_TEST;
                startActivity(new Intent(this, TestGridActivity.class));

                break;
            case R.id.btn_exit:
                finish();

                break;
            case R.id.btn_recover:
                sendBroadcast(new Intent("android.intent.action.MASTER_CLEAR"));

                break;
            case R.id.btn_ageing_test:
                startActivity(new Intent(this, AgeingActivity.class));

                break;
            case R.id.serial_port:
                new SerialPortDialog(this).show();
                break;
        }
    }


    public String getVersion() {
        String version = "0.0.0";

        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            version = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return version;
    }
}
