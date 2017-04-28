package com.zzx.factorytest;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.hisign.AS60xSDK.AS60xIO;
import com.hisign.AS60xSDK.SDKUtilty;
import com.zzx.factorytest.manager.OTGManager;
import com.zzx.factorytest.view.JudgeView;

import java.io.DataOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AS60xFingerprintActivity extends TestItemBaseActivity implements View.OnClickListener {
    public static final String TAG = "AS60xFingerprintActivity";
    private Button mOpenDevice;
    private Button mCapFingers;


    /*指纹核验时附加参考信息*/
    private int[] fpFlags = new int[2];
    /*证件采集是否完毕*/
    private boolean isCardComplated = false;
    /*设备初始化是否成功*/
    private boolean mSensorInited = false;
    /*打开设备传入PID、VID*/
    private UsbDevice mUsbDevice = null;
    private int VendorId = 0x2109;//（0x261A:0x000D）
    private int ProductId = 0x7638;
    private UsbManager mUsbManager;
    /*获取USB权限*/
    private PendingIntent mPermissionIntent;
    private static final String ACTION_USB_PERMISSION = "Request_USB_PERMISSION";

    public final File execpath = new File("/data/data/com.zzx.factorytest.as60x/lib");

    /*音频资源*/
    private SoundPool pool;
    private Map<String, Integer> poolMap;
    private ImageView mFingerImg;
    private PowerManager mPowerManager;
    private byte[] mData;
    private int[] mLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.as60x_fingerprint_activity);

        initView();

        addListener();

        OTGManager.getInstance(this).startOTG();

        /*注册监听USB*/
        registerUSBpermisson(this.getApplicationContext());

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (pool != null) {
            pool.release();
            pool = null;
        }

        if (null != mUsbDevice) {
            AS60xIO.FCV_CloseDevice(mUsbDevice);// 关闭设备，释放资源
        }


        OTGManager.getInstance(this).stopOTG();


    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }


    private void initView() {

        mOpenDevice = (Button) findViewById(R.id.Opendevice);

        mCapFingers = (Button) findViewById(R.id.CapFingers);

        mFingerImg = (ImageView) findViewById(R.id.fingerImg);


        ((JudgeView) findViewById(R.id.JudgeView)).setOnResultSelectedListener(this);

        mPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);

        poolMap = new HashMap<String, Integer>();
        pool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        poolMap.put("didi", pool.load(this, R.raw.didi, 1));

        mData = new byte[92160];
        mLength = new int[1];


    }

    private void addListener() {

        mOpenDevice.setOnClickListener(this);
        mCapFingers.setOnClickListener(this);
    }

    private Bitmap getBitmap(String filePath) {

        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, newOpts);

        int w = newOpts.outWidth;
        int h = newOpts.outHeight;

        float hh = 300f;
        float ww = 200f;

        int be = 1;
        if (w > h && w > ww) {
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;

        newOpts.inJustDecodeBounds = false;


        Bitmap bitmap = BitmapFactory.decodeFile(filePath, newOpts);

        return bitmap;

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.Opendevice:
                mSensorInited = InitUsbDevice(VendorId, ProductId);
                if (!mSensorInited) {
                    SDKUtilty.showToast(AS60xFingerprintActivity.this, "设备打开失败!", Toast.LENGTH_SHORT);
                }
                break;

            case R.id.CapFingers:
                int nRet = -1;
                long startTime, endTime;
                if (mSensorInited) {
                    startTime = System.currentTimeMillis();
                    while (true) {


                        nRet = AS60xIO.HS_GetImage(mUsbDevice, 0);

                        if (0 == nRet) {
                            if (pool != null) {
                                pool.play(poolMap.get("didi"), 1.0f, 1.0f, 0, 0, 1.0f);
                            }
                            if (AS60xIO.HS_UpImage(mUsbDevice, 0, mData, mLength) == 0) {
                                SDKUtilty.showToast(AS60xFingerprintActivity.this, "采集成功！", Toast.LENGTH_SHORT);
                                File file = new File(getExternalFilesDir("fingerprint"), "fingerprint.bmp");
                                SDKUtilty.SaveRawToBmp(mData, file.getAbsolutePath(), false);

                                Bitmap bitmap = getBitmap(file.getAbsolutePath());
                                if (bitmap != null)
                                    mFingerImg.setImageBitmap(bitmap);
                            }
                            break;
                        }
                        endTime = System.currentTimeMillis();
                        if (1500 <= (endTime - startTime)) {
                            SDKUtilty.showToast(AS60xFingerprintActivity.this, "采集失败！", Toast.LENGTH_SHORT);
                            break;
                        }
                    }
                } else {
                    SDKUtilty.showToast(AS60xFingerprintActivity.this, "设备未打开，请打开设备!", Toast.LENGTH_SHORT);
                }
                break;
        }

    }

    public void registerUSBpermisson(Context context) {
        mUsbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);//拔出USB
        context.registerReceiver(mUsbReceiverPermission, filter);
    }

    /*设备初始化*/
    private boolean InitUsbDevice(int vid, int pid) {

		/*1、原先的初始化方式，不传递输入、输出节点，SDK内部写死,仍然可用*/
        /*{
            String usbRoot = "chmod -R 777 /dev/bus/usb";
			boolean isRoot = RootCommand(usbRoot);//此命令若想执行成功，需要机器Root
			mUsbDevice = AS60xIO.FCV_OpenDevice(this, vid, pid);
		}*/

        //2、以下指定初始化方式及端点等设置
        int initType = 0;
        boolean isSucceed = false;
        if (0 == initType)//Java
        {
            mUsbDevice = AS60xIO.FCV_OpenDeviceEx(this, vid, pid, -1, -1);
            mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);

            if (mUsbDevice != null && !mUsbManager.hasPermission(mUsbDevice))//Java方式：若无权限则，主动申请权限，等待用户回馈
            {
                mUsbManager.requestPermission(mUsbDevice, mPermissionIntent);
            }

        } else {//Jni方式
            String usbRoot = "chmod -R 777 /dev/bus/usb";
            boolean isRoot = RootCommand(usbRoot);//此命令若想执行成功，需要机器Root
            if (isRoot) {
                mUsbDevice = AS60xIO.FCV_OpenDeviceEx(this, vid, pid, 0x02, 0x81);//as606:1 as602:2
            } else {
                //Android设备无Root权限，无法初始化
                Toast.makeText(AS60xFingerprintActivity.this, "设备未Root权限获取失败！", Toast.LENGTH_SHORT).show();
            }
        }
        /*验证是否初始化成功*/
        int nRet = AS60xIO.HS_Verfiy(mUsbDevice);
        if (0 == nRet) {
            isSucceed = true;
            Toast.makeText(AS60xFingerprintActivity.this, "设备初始化完毕！", Toast.LENGTH_SHORT).show();
            if (pool != null) {
                pool.play(poolMap.get("didi"), 1.0f, 1.0f, 0, 0, 1.0f);
            }
        }
        return isSucceed;
    }

    public boolean RootCommand(String command) {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");//执行这一句，superuser.apk就会弹出授权对话框
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("cd " + execpath + "\n");
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            Log.d("*** DEBUG ***", "ROOT REE" + e.getMessage());
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
        Log.d("*** DEBUG ***", "Root SUC ");
        return true;
    }

    private final BroadcastReceiver mUsbReceiverPermission = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                SDKUtilty.showToast(AS60xFingerprintActivity.this, "申请USB通信权限！", Toast.LENGTH_SHORT);
                synchronized (this) {
                    UsbDevice usbDevice = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    //用户允许权限申请true
                    if (usbDevice != null && intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        //用户同意 ;
                        mSensorInited = true;
                    } else//用户拒绝
                    {
                        mSensorInited = false;
                        Log.e(TAG, "permission denied for device！！！");
                    }
                }
            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                mSensorInited = false;
                AS60xIO.FCV_CloseDevice(mUsbDevice);//关闭设备，释放资源
                SDKUtilty.showToast(AS60xFingerprintActivity.this, "注意：USB采集设备已拔出！", Toast.LENGTH_SHORT);
            }
        }
    };


}
