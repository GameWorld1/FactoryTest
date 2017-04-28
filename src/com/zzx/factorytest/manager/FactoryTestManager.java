package com.zzx.factorytest.manager;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.zzx.factorytest.AS60xFingerprintActivity;
import com.zzx.factorytest.BluetoochTestActivity;
import com.zzx.factorytest.CameraActivity;
import com.zzx.factorytest.FMTestActivity;
import com.zzx.factorytest.GPSTestActivity;
import com.zzx.factorytest.GravityActivity;
import com.zzx.factorytest.GyroscopeActivity;
import com.zzx.factorytest.HeadPhoneActivity;
import com.zzx.factorytest.IdentityCardActivity;
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
import com.zzx.factorytest.bean.PlatformBean;
import com.zzx.factorytest.bean.TestItem;
import com.zzx.factorytest.help.PlatformHelp;

import java.util.ArrayList;
import java.util.List;

public class FactoryTestManager {

    private Context mContext;
    private SharedPreferences mSharedPreferences;

    //    public static final String CONFIG_FILE_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + "factory.res";// 配置文件路径
    public static final String CONFIG_PRE_STRING = "zzx.factory.";// 配置文件前缀

    public static final String ITEM_MAGNET = "MagneticField";// 磁场
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
    private static final String ITEM_AS60X_IDENTITYCARD = "IdentityCard";
    public static final String ITEM_NFC = "NFC";// NFC测试
    public static final String ITEM_LIGHT_SENSOR = "Light_Distance_Sensor";//光线感应
    public static final String TIEM_GYROSCOPE_SENSOR = "Gyroscope_Sensor"; //陀螺仪


    public static final String ITEM_EMMCID = "EMMC_ID";// emmcid
    public static final String IMEI_serial = "IMEI_serial";
    public static final String MAC = "MAC";
    public static final String MODEL = "BUILD_ID";

    public static int currentTestMode;


    public interface TestMode {
        int MODE_AUTO_TEST = 1;// 自动测试
        int MODE_SINGAL_TEST = 2;// 单项测试
        int MODE_RESULT_TEST = 3;// 查看测试报表
    }

    public static List<TestItem> testList = new ArrayList<TestItem>();

    //    public static HashMap<String, String> mTestResultMap = new HashMap<String, String>();
    static FactoryTestManager instance;

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
        mContext = context;
        mSharedPreferences = context.getSharedPreferences("factoryTest", Context.MODE_PRIVATE);
        initTestList();
//        initTestResultMap();
    }

//    private void initTestResultMap() {
//        mTestResultMap.put(ITEM_MAGNET, "");
//        mTestResultMap.put(ITEM_CAMERA, "");
//        mTestResultMap.put(ITEM_GPS, "");
//        mTestResultMap.put(ITEM_HEADPHONE, "");
//
//        mTestResultMap.put(ITEM_MICROPHONE, "");
//        mTestResultMap.put(ITEM_SDCARD, "");
//        mTestResultMap.put(ITEM_SIM, "");
//        mTestResultMap.put(ITEM_TOUCH_SCREEN, "");
//
//        mTestResultMap.put(ITEM_VIBRATE, "");
//        mTestResultMap.put(ITEM_BLUETOOTH, "");
//        mTestResultMap.put(ITEM_WIFI, "");
//        mTestResultMap.put(ITEM_RADIO, "");
//
//        mTestResultMap.put(ITEM_BUTTON, "");
//        mTestResultMap.put(ITEM_GRAVITY, "");
//        mTestResultMap.put(ITEM_3G, "");
//        mTestResultMap.put(ITEM_LCD, "");
//
//        mTestResultMap.put(ITEM_POWER, "");
//        mTestResultMap.put(ITEM_SPEAKER, "");
//        mTestResultMap.put(ITEM_TPCAL, "");
//        mTestResultMap.put(ITEM_FINGERPRINT, "");
//
//        mTestResultMap.put(ITEM_NFC, "");
//        mTestResultMap.put(ITEM_LIGHT_SENSOR, "");
//        mTestResultMap.put(IMEI_serial, "");
//        mTestResultMap.put(MAC, "");
//        mTestResultMap.put(TIEM_GYROSCOPE_SENSOR, "");
//
//        mTestResultMap.put(MODEL, "");
//        mTestResultMap.put(ITEM_EMMCID, "");
//
//
//    }

    public TestItem getActivityTestItem(Activity activity) {
        for (int i = 0; i < testList.size(); i++) {
            Class cls = testList.get(i).activityCls;
            if (cls == activity.getClass()) {
                return testList.get(i);
            }
        }
        return null;
    }

    private void initTestList() {

        PlatformBean bean = PlatformHelp.getPlatform(mContext);
        if (bean.MagneticField) {
            TestItem magnet = new TestItem(MagneticFieldActivity.class,
                    R.drawable.magnet, ITEM_MAGNET, "磁场测试", true);
            add2List(magnet);
        }
        if (bean.TouchScreen) {
            TestItem gravity = new TestItem(GravityActivity.class,
                    R.drawable.gravity, ITEM_GRAVITY, "重力感应", true);
            add2List(gravity);
        }
        if (bean.Battery) {
            TestItem power = new TestItem(PowerActivity.class, R.drawable.power,
                    ITEM_POWER, "电池测试", true);
            add2List(power);
        }
        if (bean.Sdcard) {
            TestItem sdcard = new TestItem(SdcardActivity.class,
                    R.drawable.sd_card, ITEM_SDCARD, "SD卡检测", true);
            add2List(sdcard);
        }
        if (bean.Sim) {
            TestItem sim = new TestItem(SIMActivity.class, R.drawable.sim_card,
                    ITEM_SIM, "SIM卡检测", true);
            add2List(sim);
        }
        if (bean.Bluetooth) {
            TestItem bluetooth = new TestItem(BluetoochTestActivity.class,
                    R.drawable.bluetooth, ITEM_BLUETOOTH, "蓝牙测试", true);
            add2List(bluetooth);
        }
        if (bean.Wifi) {
            TestItem wifi = new TestItem(WifiTestActivity.class, R.drawable.wifi,
                    ITEM_WIFI, "wifi测试", true);
            add2List(wifi);
        }
        if (bean.Gps) {
            TestItem gps = new TestItem(GPSTestActivity.class, R.drawable.gps,
                    ITEM_GPS, "GPS定位", true);
            add2List(gps);
        }
        if (bean.Imei3G) {
            TestItem _3g = new TestItem(_3GActivity.class, R.drawable._3g,
                    ITEM_3G, "3G/IMEI", true);
            add2List(_3g);
        }
        if (bean.Mic) {
            TestItem microphone = new TestItem(MICPhoneTestActivity.class,
                    R.drawable.microphone, ITEM_MICROPHONE, "MIC测试", true);
            add2List(microphone);
        }
        if (bean.Camera) {
            TestItem camera = new TestItem(CameraActivity.class, R.drawable.camera,
                    ITEM_CAMERA, "摄像头测试", false);
            add2List(camera);
        }
        if (bean.VoiceTube) {
            TestItem headphone = new TestItem(HeadPhoneActivity.class,
                    R.drawable.headphone, ITEM_HEADPHONE, "听筒测试", false);
            add2List(headphone);
        }
        if (bean.Horn) {
            TestItem speaker = new TestItem(SpeakerActivity.class,
                    R.drawable.music, ITEM_SPEAKER, "喇叭测试", false);
            add2List(speaker);
        }
        if (bean.TouchScreen) {
            TestItem touch_screen = new TestItem(MultiTouchActivity.class,
                    R.drawable.touch_screen, ITEM_TOUCH_SCREEN, "触摸屏", false);
            add2List(touch_screen);
        }
        if (bean.Shock) {
            TestItem vibrate = new TestItem(VibrateTestActivity.class,
                    R.drawable.vibrate, ITEM_VIBRATE, "振动测试", false);
            add2List(vibrate);
        }
        if (bean.Radio) {
            TestItem radio = new TestItem(FMTestActivity.class, R.drawable.radio,
                    ITEM_RADIO, "收音机测试", false);
            add2List(radio);
        }
        if (bean.KeyPress) {
            TestItem keys = new TestItem(KeysActivity.class, R.drawable.button,
                    ITEM_BUTTON, "按键测试", false);
            add2List(keys);
        }
        if (bean.TouchScreen) {
            TestItem lcd = new TestItem(LCDActivity.class, R.drawable.rgb,
                    ITEM_LCD, "LCD测试", false);
            add2List(lcd);
        }

        if (bean.LightDistanceSensor) {
            TestItem ls = new TestItem(LightSensorActivity.class, R.drawable.reg,
                    ITEM_LIGHT_SENSOR, "光线、距离传感器测试", false);
            add2List(ls);
        }
        if (bean.TouchScreenCalibration) {
            TestItem tpcal = new TestItem(TpCalActivity.class, R.drawable.reg,
                    ITEM_TPCAL, "触摸屏校准", false);
            tpcal.enable = false;
            add2List(tpcal);
        }
        if (bean.GyroscopeSensor) {
            TestItem ga = new TestItem(GyroscopeActivity.class, R.drawable.reg,
                    TIEM_GYROSCOPE_SENSOR, "陀螺仪传感器", false);
            ga.enable = false;
            add2List(ga);
        }
        if (bean.DeviceRegister) {
            TestItem regtest = new TestItem(RegTestActivity.class, R.drawable.reg,
                    ITEM_EMMCID, "设备注册", false);
            regtest.enable = false;
            add2List(regtest);
        }
        if (bean.Fingerprint) {
            TestItem fp = new TestItem(AS60xFingerprintActivity.class, R.drawable.reg,
                    ITEM_FINGERPRINT, "指纹识别", false);
            fp.enable = false;
            add2List(fp);
        }
        if (bean.Nfc) {
            TestItem nfc = new TestItem(NfcTestActivity.class, R.drawable.reg,
                    ITEM_NFC, "NFC测试", false);
            nfc.enable = false;
            add2List(nfc);
        }
        if (bean.IdentityCard) {
            TestItem ic = new TestItem(IdentityCardActivity.class, R.drawable.reg,
                    ITEM_AS60X_IDENTITYCARD, "身份证识别", false);
            ic.enable = false;
            add2List(ic);
        }

    }

    private void add2List(TestItem item) {
        testList.add(item);
    }

    public void setResult(String item, boolean result) {
        Editor edit = mSharedPreferences.edit();
        edit.putString(item, result ? "1" : "0");
        edit.apply();
    }

    public void putResult(String key, String value) {
        mSharedPreferences.edit().putString(key, value).apply();
    }

    public Boolean getResult(String item) {
        String result = mSharedPreferences.getString(item, null);
        if ("1".equals(result)) {
            return true;
        } else if ("0".equals(result)) {
            return false;
        }
        return null;
    }

    public void clearTestResult() {
        mSharedPreferences.edit().clear().apply();
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
                    TestItem testItem = testList.get(i + 1);
                    if (testItem.isEnable()) {
                        return testItem.activityCls;
                    } else {
                        currentCls = testItem.activityCls;
                    }
                }
            }
        }
        return null;

    }
}
