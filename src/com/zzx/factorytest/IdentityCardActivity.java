package com.zzx.factorytest;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hisign.AS60xSDK.IDCardDecodeAPI;
import com.ivsign.android.IDCReader.IDCReaderSDK;
import com.zzx.factorytest.bean.PlatformBean;
import com.zzx.factorytest.help.PlatformHelp;
import com.zzx.factorytest.manager.OTGManager;
import com.zzx.factorytest.view.JudgeView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class IdentityCardActivity extends TestItemBaseActivity implements View.OnClickListener {


    public static final String TAG = "IdentityCardActivity.cl";
    private Button mRead;
    private TextView mTxt;
    private TextView mSAM;
    private ImageView mImg;
    private IDCReaderSDK mChatService;

    private String mConnectedDeviceName = "/dev/ttyMT";
    private PlatformBean mPlatform;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.identity_card_activity);
        initView();
        addListener();
        init();
        OTGManager.getInstance(this).startUART1();
    }

    private void init() {
        mPlatform = PlatformHelp.getPlatform(this);
        mConnectedDeviceName += mPlatform.IdentityCardPath;
        Log.d(TAG, Environment.getExternalStorageDirectory().getPath() + File.separator + "wltlib");
        if (mChatService == null) {
            mChatService = new IDCReaderSDK(this, "/sdcard/wltlib");
        }
    }

    private void addListener() {
        mRead.setOnClickListener(this);
    }

    private void initView() {
        ((JudgeView) findViewById(R.id.judgeview)).setOnResultSelectedListener(this);
        mRead = (Button) findViewById(R.id.read);
        mTxt = (TextView) findViewById(R.id.information);
        mSAM = (TextView) findViewById(R.id.sam);
        mImg = (ImageView) findViewById(R.id.img);
    }

    @Override
    protected void onResume() {
        super.onResume();

        switch (mChatService.getState()) {
            case IDCReaderSDK.STATE_CONNECTED:
                Toast.makeText(getApplicationContext(), "已连接", Toast.LENGTH_SHORT).show();
                break;

            case IDCReaderSDK.STATE_CONNECTING:
                Toast.makeText(getApplicationContext(), "连接中", Toast.LENGTH_SHORT).show();
                break;

            case IDCReaderSDK.STATE_LISTEN:
                Toast.makeText(getApplicationContext(), "STATE_LISTEN", Toast.LENGTH_SHORT).show();
                break;

            case IDCReaderSDK.STATE_NONE:
                mTxt.setText("准备连接");
                connect();
                break;

        }


    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mChatService != null) {
            mChatService.CVR_CloseComm();
            mChatService = null;
        }

        OTGManager.getInstance(this).stopUART1();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.read:
                certification();
                break;
        }
    }

    private void certification() {
        if (mChatService.getState() == IDCReaderSDK.STATE_CONNECTED) {
            int ret = mChatService.CVR_Authenticate(3000);
            switch (ret) {
                case IDCReaderSDK.CVR_RETCODE_SUCCESS:
                    mTxt.setText("认证成功,正在读取信息");
                    Read_1();
                    Read_2();
                    break;
                case IDCReaderSDK.CVR_RETCODE_TIMEOUT:

                case IDCReaderSDK.CVR_RETCODE_LICERROR:

                case IDCReaderSDK.CVR_RETCODE_ERROR:
                    mTxt.setText("认证失败，正在重新认证");
                    ret = mChatService.CVR_Authenticate(3000);
                    if (IDCReaderSDK.CVR_RETCODE_SUCCESS == ret) {
                        mTxt.setText("认证成功,正在读取信息");
                        Read_1();
                        Read_2();
                    } else {
                        mTxt.setText("无法连接身份证模块，请检查硬件连接.");
                    }

                    break;
            }


//            if (IDCReaderSDK.CVR_RETCODE_SUCCESS == ret) {
//                byte[] dataBuf = mChatService.CVR_GetSAMID();
//                readMessage = "SAM信息: "
//                        + printSAMString(dataBuf, dataBuf.length);
//                mTxt.setText(readMessage);
//
//            } else {
//
//            }
        }
    }

    private static String printSAMString(byte[] b, int len) {
        int[] data = new int[4];

        data[0] = b[0];
        data[1] = b[2];
        data[2] = (b[4] & 0xFF) + (b[5] & 0xFF) * 256 + (b[6] & 0xFF) * 256 * 256 + (b[7] & 0xFF) * 256 * 256 * 256;
        data[3] = (b[8] & 0xFF) + (b[9] & 0xFF) * 256 + (b[10] & 0xFF) * 256 * 256 + (b[11] & 0xFF) * 256 * 256 * 256;

        String result = String.format("%02d%02d-%08d-%010d", data[0], data[1], data[2], data[3]);

        return result;
    }

    private void Read_1() {
        byte[] dataBuf = mChatService.CVR_GetSAMID();
        String readMessage = "SAM信息: " + printSAMString(dataBuf, dataBuf.length);
        mSAM.setText(readMessage);
    }

    private void Read_2() {
        String readMessage = "";
        int ret = 0;
        boolean bRet = false;
        if (mChatService.getState() == IDCReaderSDK.STATE_CONNECTED) {
            mTxt.setText("开始读取信息:");
            String FpInfo = "证件内指纹区域未注册！";
            // 读取全部信息，包括指纹信息
            ret = mChatService.CVR_Read_FPContent(1, 5000);
            Log.i(TAG, "CVR_Read_Content ret = " + ret);

            if (IDCReaderSDK.CVR_RETCODE_SUCCESS == ret) {
//                byte[] dataBuf = mChatService.CVR_GetInfo();
//                try {
//                    String TestStr = new String(dataBuf, "UTF16-LE");
//                    readMessage = new String(TestStr.getBytes("UTF-8"));
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }

                byte[] fp_data = mChatService.fingerInfo;

                if (fp_data[0] == 0x0 && fp_data[512] == 0x0) {
                    FpInfo = "证件内指纹区域未注册！";
                } else {
                    WriteBufferToFile("/sdcard/fpinfo.dat", fp_data);
                    FpInfo = "证件内指纹区域已经注册！";
                }
            } else if (IDCReaderSDK.CVR_RETCODE_TIMEOUT == ret) {
                mTxt.setText(mConnectedDeviceName + ":  " + "操作超时 ");
                return;
            } else {
                mTxt.setText("没有读到人员信息！ ");
                //button_clean_click();
                return;
            }

            // 2015-10-27:
            // delete original bmp file if exists.
            File bmpFile = new File("/sdcard/zp.bmp");
            if (bmpFile.exists()) {
                bRet = bmpFile.delete();
                if (bRet == false) {
                    mTxt.setText(" 照片删除失败!");
                    return;
                }
            }

            byte[] wlt = getBytesFromSdcard("/sdcard/wltlib/zp.wlt");

            bRet = IDCardDecodeAPI.FCV_SaveCardPictoBmp(wlt, Environment.getExternalStorageDirectory().getPath());

            if (bRet)
                mTxt.setText(" 照片解码成功!");
            else
                mTxt.setText(" 照片解码失败!");

            // TODO: need debug here, get image from mIDCReader.
            mImg.setImageBitmap(BitmapFactory.decodeFile("/sdcard/zp.bmp"));

            mTxt.setText("信息解码：\n"
                    + "姓名：" + mChatService.GetPeopleName() + "\n"
                    + "性别：" + mChatService.GetPeopleSex() + "\n"
                    + "民族：" + mChatService.GetPeopleNation() + "\n"
                    + "出生：" + mChatService.GetPeopleBirthday() + "\n"
                    + "住址：" + mChatService.GetPeopleAddress() + "\n"
                    + "卡号：" + mChatService.GetPeopleIDCode() + "\n"
                    + "发证机关：" + mChatService.GetDepartment() + "\n"
                    + "有效开始日期：" + mChatService.GetStartDate() + "\n"
                    + "有效截止日期：" + mChatService.GetEndDate() + "\n"
                    + "指纹信息:" + FpInfo + "\n");
        } else {
            mTxt.setText(" 设备未连接 " + readMessage);
        }
    }


    private void connect() {

        File file = new File(mConnectedDeviceName);
        System.out.println(file.getAbsolutePath() + " canRead = " + file.canRead() + ",canWrite = " + file.canWrite() + ",canExecute = " + file.canExecute());
        file = new File("/dev/ttyMT2");
        System.out.println(file.getAbsolutePath() + " canRead = " + file.canRead() + ",canWrite = " + file.canWrite() + ",canExecute = " + file.canExecute());

        int ret = mChatService.CVR_InitComm(mConnectedDeviceName, 1000, IDCReaderSDK.DEVICE_SERIALPORT);

        switch (ret) {
            case IDCReaderSDK.CVR_RETCODE_SUCCESS:
                mTxt.setText("设备" + mConnectedDeviceName + "连接成功,请放卡操作");
                break;
            case IDCReaderSDK.CVR_RETCODE_TIMEOUT:
                mTxt.setText("设备连接超时");
                break;
            default:
                mTxt.setText("设备连接失败");
                break;
        }
    }

    public static byte[] getBytesFromSdcard(String filePath) {

        byte[] imgData = null;
        FileInputStream fis = null;
        try {
            File f = new File(filePath);
            fis = new FileInputStream(f);
            imgData = new byte[fis.available()];
            fis.read(imgData);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imgData;
    }

    public void WriteBufferToFile(String filename, byte[] buf) {
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            fos.write(buf);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
