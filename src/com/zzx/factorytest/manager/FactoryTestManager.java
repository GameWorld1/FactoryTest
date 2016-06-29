package com.zzx.factorytest.manager;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;

import com.zzx.factorytest.BluetoochTestActivity;
import com.zzx.factorytest.CameraActivity;
import com.zzx.factorytest.FMTestActivity;
import com.zzx.factorytest.FingerprintActivity;
import com.zzx.factorytest.GPSTestActivity;
import com.zzx.factorytest.GravityActivity;
import com.zzx.factorytest.GyroscopeActivity;
import com.zzx.factorytest.HeadPhoneActivity;
import com.zzx.factorytest.KeysActivity;
import com.zzx.factorytest.LCDActivity;
import com.zzx.factorytest.LightSensorActivity;
import com.zzx.factorytest.MICPhoneTestActivity;
import com.zzx.factorytest.MagneticFieldActivity;
import com.zzx.factorytest.MultiTouchActivity;
import com.zzx.factorytest.NfcTestActivity;
import com.zzx.factorytest.PowerActivity;
import com.zzx.factorytest.R;
import com.zzx.factorytest.RegTestActivity;
import com.zzx.factorytest.SIMActivity;
import com.zzx.factorytest.SdcardActivity;
import com.zzx.factorytest.SpeakerActivity;
import com.zzx.factorytest.TpCalActivity;
import com.zzx.factorytest.VibrateTestActivity;
import com.zzx.factorytest.WifiTestActivity;
import com.zzx.factorytest._3GActivity;
import com.zzx.factorytest.bean.TestItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class FactoryTestManager {

    private Context context;
    private SharedPreferences sp;

    public static final String CONFIG_FILE_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + "factory.res";// 配置文件路径
    public static final String CONFIG_PRE_STRING = "zzx.factory.";// 配置文件前缀

    public static final String ITEM_MAGNET = "Magnetic_Field";// 磁场
    public static final String ITEM_CAMERA = "Camera";// 摄像机

    public static final String ITEM_GPS = "GPS";// gps
    public static final String ITEM_HEADPHONE = "Receiver";// 声筒
    public static final String ITEM_MICROPHONE = "MIC";// 话筒
    public static final String ITEM_SDCARD = "SD_Card";// sdcard

    public static final String ITEM_SIM = "sim";// sim
    public static final String ITEM_TOUCH_SCREEN = "Touch_Screen";// touch_screen
    public static final String ITEM_VIBRATE = "Shock";// 振动
    public static final String ITEM_BLUETOOTH = "Bluetooth";// 蓝牙

    public static final String ITEM_WIFI = "Wifi";// wifi
    public static final String ITEM_RADIO = "Radio";// radio
    public static final String ITEM_BUTTON = "Key_Press";// 按键
    public static final String ITEM_GRAVITY = "Gravity_Induction";// 重力感应

    public static final String ITEM_3G = "3G_IMEI";// 3g
    public static final String ITEM_LCD = "LCD";// lcd
    public static final String ITEM_POWER = "Battery";// 电压电流
    public static final String ITEM_SPEAKER = "Horn";// 喇叭电流

    public static final String ITEM_TPCAL = "Touch_Screen_Calibration";// tpcal
    public static final String ITEM_FINGERPRINT = "Fingerprint";// 指纹识别测试
    public static final String ITEM_NFC = "NFC";// NFC测试
    public static final String ITEM_LIGHT_SENSOR = "Light_Distance_Sensor";//光线感应
    public static final String TIEM_GYROSCOPE_SENSOR = "Gyroscope_Sensor"; //陀螺仪


    public static final String ITEM_EMMCID = "EMMC_ID";// emmcid
    public static final String IMEI_serial = "IMEI_serial";
    public static final String MAC = "MAC";
    public static final String MODEL = "BUILD_ID";

    public static int currentTestMode;


    public interface TestMode {
        public static final int MODE_AUTO_TEST = 1;// 自动测试
        public static final int MODE_SINGAL_TEST = 2;// 单项测试
        public static final int MODE_RESULT_TEST = 3;// 查看测试报表
    }

    // public static final String ITEM_SIGNAL = "Signal";// �ź�

    // private List<Class> activityList = new ArrayList<Class>();

    public static List<TestItem> testList = new ArrayList<TestItem>();

    public static HashMap<String, String> mTestResultMap = new HashMap<String, String>();
    static FactoryTestManager instance = null;

    public static FactoryTestManager getInstance(Context context) {
        if (instance == null) {
            instance = new FactoryTestManager(context);
        }
        return instance;
    }

    public List<TestItem> getTestList() {
        return testList;
    }

    private FactoryTestManager(Context context) {
        this.context = context;
        sp = context.getSharedPreferences("factoryTest", Context.MODE_PRIVATE);
        // readConfigFile();
        initTestList();
        initTestResultMap();
    }

    private void initTestResultMap() {
        mTestResultMap.put(ITEM_MAGNET, "");
        mTestResultMap.put(ITEM_CAMERA, "");
        mTestResultMap.put(ITEM_GPS, "");
        mTestResultMap.put(ITEM_HEADPHONE, "");

        mTestResultMap.put(ITEM_MICROPHONE, "");
        mTestResultMap.put(ITEM_SDCARD, "");
        mTestResultMap.put(ITEM_SIM, "");
        mTestResultMap.put(ITEM_TOUCH_SCREEN, "");

        mTestResultMap.put(ITEM_VIBRATE, "");
        mTestResultMap.put(ITEM_BLUETOOTH, "");
        mTestResultMap.put(ITEM_WIFI, "");
        mTestResultMap.put(ITEM_RADIO, "");

        mTestResultMap.put(ITEM_BUTTON, "");
        mTestResultMap.put(ITEM_GRAVITY, "");
        mTestResultMap.put(ITEM_3G, "");
        mTestResultMap.put(ITEM_LCD, "");

        mTestResultMap.put(ITEM_POWER, "");
        mTestResultMap.put(ITEM_SPEAKER, "");
        mTestResultMap.put(ITEM_TPCAL, "");
        mTestResultMap.put(ITEM_FINGERPRINT, "");

        mTestResultMap.put(ITEM_NFC, "");
        mTestResultMap.put(ITEM_LIGHT_SENSOR, "");
        mTestResultMap.put(IMEI_serial, "");
        mTestResultMap.put(MAC, "");
        mTestResultMap.put(TIEM_GYROSCOPE_SENSOR, "");

        mTestResultMap.put(MODEL, "");
        mTestResultMap.put(ITEM_EMMCID, "");


    }

    public TestItem getActivityTestItem(Activity activity) {
        for (int i = 0; i < testList.size(); i++) {
            Class cls = testList.get(i).activityCls;
            if (cls == activity.getClass()) {
                return testList.get(i);
            }
        }
        return null;
    }

    public void initTestList() {

        TestItem magnet = new TestItem(MagneticFieldActivity.class,
                R.drawable.magnet, ITEM_MAGNET, "磁场测试", true);
        add2List(magnet);

        TestItem gravity = new TestItem(GravityActivity.class,
                R.drawable.gravity, ITEM_GRAVITY, "重力感应", true);
        add2List(gravity);

        TestItem power = new TestItem(PowerActivity.class, R.drawable.power,
                ITEM_POWER, "电池测试", true);
        add2List(power);

        TestItem sdcard = new TestItem(SdcardActivity.class,
                R.drawable.sd_card, ITEM_SDCARD, "SD卡检测", true);
        add2List(sdcard);

        TestItem sim = new TestItem(SIMActivity.class, R.drawable.sim_card,
                ITEM_SIM, "SIM卡检测", true);
        add2List(sim);

        TestItem bluetooth = new TestItem(BluetoochTestActivity.class,
                R.drawable.bluetooth, ITEM_BLUETOOTH, "蓝牙测试", true);
        add2List(bluetooth);

        TestItem wifi = new TestItem(WifiTestActivity.class, R.drawable.wifi,
                ITEM_WIFI, "wifi测试", true);
        add2List(wifi);

        TestItem gps = new TestItem(GPSTestActivity.class, R.drawable.gps,
                ITEM_GPS, "GPS定位", true);
        add2List(gps);

        TestItem _3g = new TestItem(_3GActivity.class, R.drawable._3g,
                ITEM_3G, "3G/IMEI", true);
        add2List(_3g);

        TestItem microphone = new TestItem(MICPhoneTestActivity.class,
                R.drawable.microphone, ITEM_MICROPHONE, "MIC测试", true);
        add2List(microphone);

        TestItem camera = new TestItem(CameraActivity.class, R.drawable.camera,
                ITEM_CAMERA, "摄像头测试", false);
        add2List(camera);

        TestItem headphone = new TestItem(HeadPhoneActivity.class,
                R.drawable.headphone, ITEM_HEADPHONE, "听筒测试", false);
        add2List(headphone);

        TestItem speaker = new TestItem(SpeakerActivity.class,
                R.drawable.music, ITEM_SPEAKER, "喇叭测试", false);
        add2List(speaker);

        TestItem touch_screen = new TestItem(MultiTouchActivity.class,
                R.drawable.touch_screen, ITEM_TOUCH_SCREEN, "触摸屏", false);
        add2List(touch_screen);

        TestItem vibrate = new TestItem(VibrateTestActivity.class,
                R.drawable.vibrate, ITEM_VIBRATE, "振动测试", false);
        add2List(vibrate);

        TestItem radio = new TestItem(FMTestActivity.class, R.drawable.radio,
                ITEM_RADIO, "收音机测试", false);
        add2List(radio);

        TestItem keys = new TestItem(KeysActivity.class, R.drawable.button,
                ITEM_BUTTON, "按键测试", false);
        add2List(keys);

        TestItem lcd = new TestItem(LCDActivity.class, R.drawable.rgb,
                ITEM_LCD, "LCD测试", false);
        add2List(lcd);

        TestItem regtest = new TestItem(RegTestActivity.class, R.drawable.reg,
                ITEM_EMMCID, "设备注册", false);
        add2List(regtest);

        TestItem ls = new TestItem(LightSensorActivity.class, R.drawable.reg,
                ITEM_LIGHT_SENSOR, "光线、距离传感器测试", false);
        add2List(ls);
    /*
        TestItem tpcal = new TestItem(TpCalActivity.class, R.drawable.reg,
                ITEM_TPCAL, "触摸屏校准", false);
        add2List(tpcal);
        */
        TestItem ga = new TestItem(GyroscopeActivity.class, R.drawable.reg, TIEM_GYROSCOPE_SENSOR, "陀螺仪传感器", false);

        add2List(ga);

//        TestItem fp = new TestItem(FingerprintActivity.class, R.drawable.reg,
//                ITEM_FINGERPRINT, "指纹识别", false);
//        add2List(fp);
//
        TestItem nfc = new TestItem(NfcTestActivity.class, R.drawable.reg,
                ITEM_NFC, "NFC测试", false);
        add2List(nfc);
    }

    private void add2List(TestItem item) {
        if (item.isEnable()) {
            testList.add(item);
        }
    }

    /**
     * 生成配置文件
     * <p/>
     * 陈伟斌 2013-4-16
     */
    public void gernalConfigFile() {
        try {
            File outFile = new File(CONFIG_FILE_PATH);
            if (!outFile.exists()) {
                outFile.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(outFile);
            Properties properties = new Properties();
            for (Map.Entry<String, String> entry : mTestResultMap.entrySet()) {

                String key = entry.getKey();
                String result = pullResult(key);
                if (null != result) {
                    properties.setProperty(key, result);
                }
            }
            properties.store(fos, "");

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public void setResult(String item, boolean result) {
        Editor edit = sp.edit();
        edit.putString(item, result ? "1" : "0");
        edit.apply();
    }

    public void putResult(String key, String value) {
        sp.edit().putString(key, value).apply();
    }

    public String pullResult(String key) {
        return sp.getString(key, null);
    }

    public Boolean getResult(String item) {
        String result = sp.getString(item, null);
        if ("1".equals(result)) {
            return true;
        } else if ("0".equals(result)) {
            return false;
        }
        return null;
    }

    public void clearTestResult() {
        sp.edit().clear().commit();
    }

    public Class getNextActivityClass(Class currentCls) {

        if (currentCls == null && testList.size() > 0) {
            return testList.get(0).activityCls;
        }
        for (int i = 0; i < testList.size(); i++) {
            Class cls = testList.get(i).activityCls;
            if (cls == currentCls) {
                if (i == testList.size() - 1) {
                    return null;
                } else {
                    return testList.get(i + 1).activityCls;
                }
            }
        }
        return null;

    }
}
