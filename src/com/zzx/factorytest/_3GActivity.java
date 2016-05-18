package com.zzx.factorytest;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zzx.factorytest.manager.FactoryTestManager;
import com.zzx.factorytest.view.SignalView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class _3GActivity extends TestItemBaseActivity implements Callback {

    private TextView txt_service;
    private TextView txt_location;
    private TextView txt_singal_type;
    private TextView txt_operator;
    private TextView txt_imei;
    private TextView txtStrength;
    private TextView txt3gTime;
    private TextView txt2gTime;
    private TextView txt3gSwitch;
    private TextView txt2gSwitch;
    private TextView txt_correct;
    private GsmCellLocation gsmCellLocation;
    private TelephonyManager telephonyManager;
    private ProgressBar singal_strength;
    private Handler mHandler;
    private SignalView signalView;
    private int strength = 0;
    private boolean hasGetSingal = false;//
    private int _2g_switch = 0;//
    private int _3g_switch = 0;//
    private int _2g_time = 0;//
    private int _3g_time = 0;//
    private int oldNetType = 0;
    private String imei;
    private boolean isCorrect = false;//
    public static final int NETWORK_CLASS_UNKNOWN = 0;
    public static final int NETWORK_CLASS_2_G = 1;
    public static final int NETWORK_CLASS_2_75_G = 4;
    public static final int NETWORK_CLASS_3_G = 2;
    public static final int NETWORK_CLASS_4_G = 3;

    public static final int NETWORK_TYPE_UNKNOWN = 0;
    public static final int NETWORK_TYPE_GPRS = 1;
    public static final int NETWORK_TYPE_EDGE = 2;
    public static final int NETWORK_TYPE_UMTS = 3;
    public static final int NETWORK_TYPE_CDMA = 4;
    public static final int NETWORK_TYPE_EVDO_0 = 5;
    public static final int NETWORK_TYPE_EVDO_A = 6;
    public static final int NETWORK_TYPE_1xRTT = 7;
    public static final int NETWORK_TYPE_HSDPA = 8;
    public static final int NETWORK_TYPE_HSUPA = 9;
    public static final int NETWORK_TYPE_HSPA = 10;
    public static final int NETWORK_TYPE_IDEN = 11;
    public static final int NETWORK_TYPE_EVDO_B = 12;
    public static final int NETWORK_TYPE_LTE = 13;
    public static final int NETWORK_TYPE_EHRPD = 14;
    public static final int NETWORK_TYPE_HSPAP = 15;

    private final int AUTO_TEST_TIMEOUT = 10;//

    private final int AUTO_TEST_MINI_SHOW_TIME = 5;//
    private String flagFile = "/data/nvram/RestoreFlag";//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout._3g_layout);
        super.onCreate(savedInstanceState);

        txt_service = (TextView) findViewById(R.id.txt_service);
        txt2gSwitch = (TextView) findViewById(R.id.txt_2g_switch);
        txt3gSwitch = (TextView) findViewById(R.id.txt_3g_switch);
        txt2gTime = (TextView) findViewById(R.id.txt_2g_time);
        txt3gTime = (TextView) findViewById(R.id.txt_3g_time);
        txt_location = (TextView) findViewById(R.id.txt_location);
        txt_imei = (TextView) findViewById(R.id.txt_imei);
        txt_correct = (TextView) findViewById(R.id.txt_correct);
        txtStrength = (TextView) findViewById(R.id.txtStrength);
        txt_singal_type = (TextView) findViewById(R.id.txt_singal_type);
        txt_operator = (TextView) findViewById(R.id.txt_operator);
        singal_strength = (ProgressBar) findViewById(R.id.singal_strength);
        signalView = (SignalView) findViewById(R.id.signalView);

        // signalView.getViewSize(360);
        // signalView.myGraphWidth = 400;
        init();
    }

    @Override
    void executeAutoTest() {
        super.startAutoTest(AUTO_TEST_TIMEOUT, AUTO_TEST_MINI_SHOW_TIME);

    }

    private void init() {


        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        gsmCellLocation = (GsmCellLocation) telephonyManager.getCellLocation();
        if (gsmCellLocation != null) {
            txt_service.setText(gsmCellLocation.getCid() + "");
            txt_location.setText(gsmCellLocation.getLac() + "");
        }
        imei = telephonyManager.getDeviceId();
        txt_imei.setText(imei);

        PhoneSignalStateListener phoneSignalStateListener = new PhoneSignalStateListener();

        telephonyManager.listen(phoneSignalStateListener,

                PhoneStateListener.LISTEN_SIGNAL_STRENGTHS
                        | PhoneStateListener.LISTEN_SERVICE_STATE);//

        mHandler = new Handler(this);
        new SignalMonitorThread().start();

    }

    @Override
    protected void onResume() {

        File file = new File(flagFile);
        boolean exist = file.exists();
        boolean isCorrect = false;
        if (exist) {
            try {

                BufferedReader reader = new BufferedReader(new FileReader(file));
                String content = reader.readLine();

                isCorrect = content != null && !"".equals(content);//
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (isCorrect) {
            txt_correct.setText(getResources().getString(R.string.calibration));
            txt_correct.setTextColor(Color.GREEN);
            this.isCorrect = true;
        } else {
            txt_correct.setText(getResources().getString(R.string.no_calibration));
            txt_correct.setTextColor(Color.RED);
        }
        super.onResume();
    }

    class SignalMonitorThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    mHandler.sendEmptyMessage(0);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    class PhoneSignalStateListener extends PhoneStateListener {

        ServiceState mServiceState;
        SignalStrength mSignalStrength;

        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {

            mSignalStrength = signalStrength;
            updateSignalStrength(mServiceState, mSignalStrength);//

            super.onSignalStrengthsChanged(signalStrength);
        }

        @Override
        public void onServiceStateChanged(ServiceState serviceState) {
            mServiceState = serviceState;
            updateSignalStrength(mServiceState, mSignalStrength);//

            super.onServiceStateChanged(serviceState);
        }
    }

    private void updateSignalStrength(ServiceState mServiceState,
                                      SignalStrength mSignalStrength) {

        String carrierName = this.getCarrierName(telephonyManager
                .getNetworkOperatorName());// 运营商
        int singalStrength = this.getSignalStrength(mServiceState,
                mSignalStrength);// 信号
        txt_operator.setText(carrierName);
        // 网络类型
        int netWorkClass = _3GActivity.getNetworkClass(telephonyManager
                .getNetworkType());

        if (netWorkClass == NETWORK_CLASS_2_G) {
            txt_singal_type.setText("2G");
            if (oldNetType != netWorkClass) {
                _2g_switch++;
            }
        } else if (netWorkClass == NETWORK_CLASS_2_75_G) {
            txt_singal_type.setText("2.75G");
            if (oldNetType != netWorkClass) {
                _2g_switch++;
            }
        } else if (netWorkClass == NETWORK_CLASS_3_G) {
            txt_singal_type.setText("3G");
            if (oldNetType != netWorkClass) {
                _3g_switch++;
            }
        } else if (netWorkClass == NETWORK_CLASS_4_G) {
            txt_singal_type.setText("4G");
        } else if (netWorkClass == NETWORK_CLASS_UNKNOWN) {
            txt_singal_type.setText("未知֪");
        }
        txt2gSwitch.setText(_2g_switch + "");
        txt3gSwitch.setText(_3g_switch + "");

        oldNetType = netWorkClass;
        int dbm = 0;
        if (mSignalStrength != null) {
            dbm = mSignalStrength.getEvdoDbm();
            dbm = mSignalStrength.getCdmaDbm();
            if (mSignalStrength.isGsm()) {
                dbm = -113 + 2 * mSignalStrength.getGsmSignalStrength();
            } else if (mSignalStrength.getEvdoDbm() != -1) {
                dbm = mSignalStrength.getEvdoDbm();
            } else {
                dbm = mSignalStrength.getCdmaDbm();
            }
            strength = dbm;
            hasGetSingal = true;
            singal_strength.setProgress((int) (Math.abs(dbm) * 0.6));
            txtStrength.setText(dbm + "dbm");
        }

    }

    /**
     * 说明:转换运营商 Author:陈伟斌 2012-11-28
     *
     * @param carrier
     */
    public String getCarrierName(String carrier) {

        if (carrier == null) {
            return null;
        }
        String carrier_new = null;
        if (carrier.contains("CHN-CUGSM") || carrier.contains("CHN-UNICOM")
                || carrier.contains("中国联通") || carrier.contains("China Unicom")) {
            // 联通
            carrier_new = this.getResources()
                    .getString(R.string.carrier_unicom);

        } else if (carrier.contains("CHINA MOBILE") || carrier.contains("中国移动")) {
            //移动
            carrier_new = this.getResources().getString(R.string.carrier_cmcc);
        } else if (carrier.contains("EVDO") || carrier.contains("中国电信")
                || carrier.contains("CDMA")
                || carrier.contains("CHINA TELECOM")) {
            // 电信
            carrier_new = this.getResources().getString(
                    R.string.carrier_telecom);
        }
        if (carrier_new == null || "".equals(carrier_new)) {
            carrier_new = carrier;
        }
        if (!isSimCardAccess()) {//
            // carrier_new = context.getResources().getString(
            // R.string.sim_no_exists);
            carrier_new = "";
        }
        return carrier_new;
    }

    /**
     * 说明:取得信号强度(返回0时表无服务) Author:陈伟斌 2012-11-28
     *
     * @param mServiceState
     * @param mSignalStrength
     * @return
     */
    public int getSignalStrength(ServiceState mServiceState,
                                 SignalStrength mSignalStrength) {

        int StrengthLevel = 0;
        if (mServiceState == null
                || mSignalStrength == null
                || (!hasService(mServiceState) && mServiceState.getState() != ServiceState.STATE_EMERGENCY_ONLY))
            return 0;// 无服务

        // boolean result=mSignalStrength.isGsm();
        if (mSignalStrength != null && mSignalStrength.isGsm()) {// is gsm
            int asu = mSignalStrength.getGsmSignalStrength();

            if (asu <= 2 || asu == 99)
                StrengthLevel = 0;
            else if (asu >= 12)
                StrengthLevel = 4;
            else if (asu >= 8)
                StrengthLevel = 3;
            else if (asu >= 5)
                StrengthLevel = 2;
            else
                StrengthLevel = 1;
        } else {

            StrengthLevel = getEvdoLevel(mSignalStrength);
            if (StrengthLevel == 0) {
                StrengthLevel = getCdmaLevel(mSignalStrength);
            } else {
                StrengthLevel = getCdmaLevel(mSignalStrength);
            }
        }

        return StrengthLevel;
    }

    private int getCdmaLevel(SignalStrength mSignalStrength) {
        final int cdmaDbm = mSignalStrength.getCdmaDbm();
        final int cdmaEcio = mSignalStrength.getCdmaEcio();
        int levelDbm = 0;
        int levelEcio = 0;

        if (cdmaDbm >= -75)
            levelDbm = 4;
        else if (cdmaDbm >= -85)
            levelDbm = 3;
        else if (cdmaDbm >= -95)
            levelDbm = 2;
        else if (cdmaDbm >= -100)
            levelDbm = 1;
        else
            levelDbm = 0;

        // Ec/Io are in dB*10
        if (cdmaEcio >= -90)
            levelEcio = 4;
        else if (cdmaEcio >= -110)
            levelEcio = 3;
        else if (cdmaEcio >= -130)
            levelEcio = 2;
        else if (cdmaEcio >= -150)
            levelEcio = 1;
        else
            levelEcio = 0;

        return (levelDbm < levelEcio) ? levelDbm : levelEcio;
    }

    private int getEvdoLevel(SignalStrength mSignalStrength) {
        int evdoDbm = mSignalStrength.getEvdoDbm();
        int evdoSnr = mSignalStrength.getEvdoSnr();
        int levelEvdoDbm = 0;
        int levelEvdoSnr = 0;

        if (evdoDbm >= -65)
            levelEvdoDbm = 4;
        else if (evdoDbm >= -75)
            levelEvdoDbm = 3;
        else if (evdoDbm >= -90)
            levelEvdoDbm = 2;
        else if (evdoDbm >= -105)
            levelEvdoDbm = 1;
        else
            levelEvdoDbm = 0;

        if (evdoSnr >= 7)
            levelEvdoSnr = 4;
        else if (evdoSnr >= 5)
            levelEvdoSnr = 3;
        else if (evdoSnr >= 3)
            levelEvdoSnr = 2;
        else if (evdoSnr >= 1)
            levelEvdoSnr = 1;
        else
            levelEvdoSnr = 0;

        return (levelEvdoDbm < levelEvdoSnr) ? levelEvdoDbm : levelEvdoSnr;
    }

    private boolean hasService(ServiceState mServiceState) {
        if (mServiceState != null) {
            switch (mServiceState.getState()) {
                case ServiceState.STATE_OUT_OF_SERVICE:
                case ServiceState.STATE_POWER_OFF:
                    return false;
                default:
                    return true;
            }
        } else {
            return false;
        }
    }

    /**
     * 判断sim状况
     *
     * @return
     */
    private boolean isSimCardAccess() {
        TelephonyManager telManager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);
        boolean flag = false;
        switch (telManager.getSimState()) {
            case TelephonyManager.SIM_STATE_READY:
                flag = true;
                break;
            case TelephonyManager.SIM_STATE_ABSENT:// ��SIM��
                flag = false;
                break;
            default:// SIM卡被锁定或未知状态
                flag = false;
                break;
        }
        return flag;
    }

    public static int getNetworkClass(int networkType) {
        switch (networkType) {
            case NETWORK_TYPE_GPRS:
            case NETWORK_TYPE_CDMA:
            case NETWORK_TYPE_1xRTT:
            case NETWORK_TYPE_IDEN:
                return NETWORK_CLASS_2_G;
            case NETWORK_TYPE_EDGE:
                return NETWORK_CLASS_2_75_G;
            case NETWORK_TYPE_UMTS:
            case NETWORK_TYPE_EVDO_0:
            case NETWORK_TYPE_EVDO_A:
            case NETWORK_TYPE_HSDPA:
            case NETWORK_TYPE_HSUPA:
            case NETWORK_TYPE_HSPA:
            case NETWORK_TYPE_EVDO_B:
            case NETWORK_TYPE_EHRPD:
            case NETWORK_TYPE_HSPAP:
                return NETWORK_CLASS_3_G;
            case NETWORK_TYPE_LTE:
                return NETWORK_CLASS_4_G;
            default:
                return NETWORK_CLASS_UNKNOWN;
        }
    }

    int preStrength = 0;

    @Override
    public boolean handleMessage(Message msg) {
        gsmCellLocation = (GsmCellLocation) telephonyManager.getCellLocation();
        if (hasGetSingal) {
            signalView.onSignalChanged(strength);
        }
        // 自动化测试
        if (FactoryTestManager.currentTestMode == FactoryTestManager.TestMode.MODE_AUTO_TEST) {

            if (imei != null && strength > -95 && isCorrect) {

                synchronized (this) {
                    stopAutoTest(true);
                }
            }
            if (preStrength != 0 && Math.abs(preStrength - strength) > 15) {
                changeResult(false);
            }
            preStrength = strength;
        }
        int netTypeClass = _3GActivity.getNetworkClass(telephonyManager
                .getNetworkType());
        if (netTypeClass == NETWORK_CLASS_3_G) {
            _3g_time++;
        } else if (netTypeClass == NETWORK_CLASS_2_G) {
            _2g_time++;
        }

        txt2gTime.setText(_2g_time + "s");
        txt3gTime.setText(_3g_time + "s");
        return false;
    }
}
