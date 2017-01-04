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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zzx.factorytest.manager.BluetoothManager;
import com.zzx.factorytest.manager.FactoryTestManager;
import com.zzx.factorytest.view.SerialPortDialog;

import java.util.HashMap;

public class MainActivity extends Activity implements OnClickListener {
    private static final String BUILD_PLATFORM_NAME = "ro.product.board";
    private static final String BUILD_DATA = "ro.build.date";
    public static String CURRENT_PLATFORM;
    private Button btn_singalTest;
    private Button btn_auto_Test;
    private Button btn_result_Test;
    private Button btn_exit;
    private Button btn_recover;
    private Button btn_ageing_test;
    private LocationManager mLocationManager;
    private BluetoothManager bluetoothManager;
    public static long tTFFStart = SystemClock.elapsedRealtime();
    public static HashMap<String, String> PLATFORM_ID = new HashMap<String, String>();

    static {
        PLATFORM_ID.put("X3089", "M3089");
        PLATFORM_ID.put("T71L", "M4082");
        PLATFORM_ID.put("T800A", "T80");
        PLATFORM_ID.put("SOTEN_XL01A", "S50");
        PLATFORM_ID.put("L501C", "T71V3");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        String data = RegTestActivity.SystemPropertiesProxy.get(this, BUILD_DATA);
        String platform = RegTestActivity.SystemPropertiesProxy.get(this, BUILD_PLATFORM_NAME);
        if (PLATFORM_ID.containsKey(platform)) {
            platform = PLATFORM_ID.get(platform);
        }
        CURRENT_PLATFORM = platform;
        setContentView(R.layout.main_layout);
        ((TextView) findViewById(R.id.version)).setText(getResources().getString(R.string.platform_name, platform, getVersion(), data));

        btn_singalTest = (Button) findViewById(R.id.btn_singalTest);
        btn_result_Test = (Button) findViewById(R.id.btn_result_Test);
        btn_exit = (Button) findViewById(R.id.btn_exit);
        btn_auto_Test = (Button) findViewById(R.id.btn_auto_Test);
        btn_ageing_test = (Button) findViewById(R.id.btn_ageing_test);
        btn_recover = (Button) findViewById(R.id.btn_recover);
        btn_singalTest.setOnClickListener(this);
        btn_result_Test.setOnClickListener(this);
        btn_auto_Test.setOnClickListener(this);
        btn_ageing_test.setOnClickListener(this);
        btn_exit.setOnClickListener(this);
        btn_recover.setOnClickListener(this);

        initDeviceState();

        super.onCreate(savedInstanceState);

    }

    private void initDeviceState() {
        // ��ʹ��
        // RootContext.getInstance(this).runCommand("chmod 777 /data");
        // RootContext.getInstance(this).runCommand("chmod 777 /data/nvram");
        // RootContext.getInstance(this).runCommand("chmod 777 /data/nvram/RestoreFlag");

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        bluetoothManager = new BluetoothManager(this);
        bluetoothManager.openBluetooth();

    }

    @Override
    protected void onDestroy() {
        bluetoothManager.unregisterBluethoothReceiver();
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.restartPackage(this.getApplication().getPackageName());
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(this, TestGridActivity.class);
        if (v.getId() == btn_singalTest.getId()) {
            FactoryTestManager.currentTestMode = FactoryTestManager.TestMode.MODE_SINGAL_TEST;

            startActivity(new Intent(this, TestGridActivity.class));
        } else if (v.getId() == btn_auto_Test.getId()) {
            FactoryTestManager.currentTestMode = FactoryTestManager.TestMode.MODE_AUTO_TEST;

            startActivity(new Intent(this, TestGridActivity.class));
        } else if (v.getId() == btn_result_Test.getId()) {
            FactoryTestManager.currentTestMode = FactoryTestManager.TestMode.MODE_RESULT_TEST;

            startActivity(new Intent(this, TestGridActivity.class));
        } else if (v.getId() == btn_exit.getId()) {// 退出
            this.finish();
        } else if (v.getId() == btn_recover.getId()) {// 恢复出厂测试
            sendBroadcast(new Intent("android.intent.action.MASTER_CLEAR"));
        } else if (v.getId() == btn_ageing_test.getId()) {// 老化测试
            startActivity(new Intent(this, AgeingActivity.class));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_gyroscope, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_gernal_configfile) {
            FactoryTestManager.getInstance(this).gernalConfigFile();
            Toast.makeText(this, "生成成功!", Toast.LENGTH_SHORT).show();
        }
        if (item.getItemId() == R.id.menu_configuration_serialport) {
            createDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    public void serialport(View view) {
        createDialog();
    }

    private void createDialog() {
        new SerialPortDialog(this).show();
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
