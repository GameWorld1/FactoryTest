package com.zzx.factorytest;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.theme.finger.print.Device;
import com.theme.finger.print.Device.OnConnectionListener;
import com.theme.finger.print.Device.OnRecvBitmapListener;
import com.theme.finger.print.ErrorCode;
import com.zzx.factorytest.view.JudgeView;

public class FingerprintActivity extends TestItemBaseActivity implements
        OnConnectionListener, OnRecvBitmapListener, OnCheckedChangeListener {
    public static final String TAG = "FingerprintActivity";
    private ImageView mImageView;
    private final String DEFAULT_SERIALPORT_NAME = "/dev/ttyMT0";
    public static final int DEFAULT_SERIALPORT_SPEED = 921600;
    private Device mDevice;
    private TextView mFingerprintSize, mCurrentIds, mTxt;
    private CheckBox mSwitch;
    private JudgeView mJudgeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fingerprint_activity);
        initView();
        addListener();

    }

    private void initView() {
        mImageView = (ImageView) findViewById(R.id.image);
        mFingerprintSize = (TextView) findViewById(R.id.fingerprint_size);
        mSwitch = (CheckBox) findViewById(R.id.id_chk_offs);
        mTxt = (TextView) findViewById(R.id.txt);
        mJudgeView = (JudgeView) findViewById(R.id.judgeview);
        mDevice = createDevice();
    }

    private void addListener() {
        mSwitch.setOnCheckedChangeListener(this);

        mJudgeView.setOnResultSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        openDevice();
    }

    private Device createDevice() {

        return new Device(this, DEFAULT_SERIALPORT_NAME,
                DEFAULT_SERIALPORT_SPEED);
    }

    private void openDevice() {
        if (null == mDevice) {
            mDevice = createDevice();
        }
        if (!mDevice.isOpend()) {
            mDevice.open(this);
        }
    }

    private void destoryDevice() {
        if (mDevice != null) {
            mDevice.destory();
            mDevice = null;
        }
    }

    private void setTextView() {
        if (null == mDevice || !mDevice.isOpend()) {
            openDevice();
        }
        if (null != mDevice) {
            int maxCount = mDevice.getMaxCount();
            int count = mDevice.getCount();
            mFingerprintSize.setText(count + "/" + maxCount);
        }
    }

    @Override
    public void onConnected() {
        Log.d(TAG, "onConnected");
        Toast.makeText(this, "�豸���ӳɹ�,", Toast.LENGTH_SHORT).show();
        mDevice.recvBitmap(this);
    }

    @Override
    public void onDisConnected() {
        Log.d(TAG, "onDisConnected");
        Toast.makeText(this, "�豸�ѶϿ�", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNotFound() {
        Log.d(TAG, "onNotFound");
        Toast.makeText(this, "û���ҵ���ǰ�豸", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionDenied() {
        Log.d(TAG, "onPermissionDenied");
        Toast.makeText(this, "��ǰ�豸û�в���Ȩ��", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBitmap(Bitmap bitmap) {
        mImageView.setImageBitmap(bitmap);
        mTxt.setText("�ɼ��ɹ�");
    }

    @Override
    public void onBitmapProgress(int percent, int current, int count) {
        mTxt.setText(getString(R.string.string_recv_bimap_percent, percent));
    }

    @Override
    public void onFailed(int error) {
        mTxt.setText(getErrMessage(error));
    }

    @Override
    public void onEndRecv() {

    }

    @Override
    public void onStartRecv() {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.isPressed()) {
            if (isChecked) {
                openDevice();
            } else {
                destoryDevice();
            }
        }
    }

    private String getErrMessage(final int errorCode) {
        String result = "";
        switch (errorCode) {
            case ErrorCode.ERR_FAIL:
                result = getString(R.string.string_err_fail);
                break;
            case ErrorCode.ERR_CONNECTION:
                result = getString(R.string.string_err_connection);
                break;
            case ErrorCode.ERR_VERIFY:
                result = getString(R.string.string_err_verify);
                break;
            case ErrorCode.ERR_IDENTIFY:
                result = getString(R.string.string_err_identify);
                break;
            case ErrorCode.ERR_TMPL_EMPTY:
                result = getString(R.string.string_err_tmpl_empty);
                break;
            case ErrorCode.ERR_TMPL_NOT_EMPTY:
                result = getString(R.string.string_err_tmpl_not_empty);
                break;
            case ErrorCode.ERR_ALL_TMPL_EMPTY:
                result = getString(R.string.string_err_all_tmpl_empty);
                break;
            case ErrorCode.ERR_EMPTY_ID_NOEXIST:
                result = getString(R.string.string_err_tmpl_empty_no_exist);
                break;
            case ErrorCode.ERR_BROKEN_ID_NOEXIST:
                result = getString(R.string.string_err_tmpl_broken_no_exist);
                break;
            case ErrorCode.ERR_INVALID_TMPL_DATA:
                result = getString(R.string.string_err_tmp_invalid);
                break;
            case ErrorCode.ERR_DUPLICATION_ID:
                result = getString(R.string.string_err_tmp_duplication);
                break;
            case ErrorCode.ERR_BAD_QUALITY:
                result = getString(R.string.string_err_bad_qualy);
                break;
            case ErrorCode.ERR_MERGE_FAIL:
                result = getString(R.string.string_err_merge_failed);
                break;
            case ErrorCode.ERR_NOT_AUTHORIZED:
                result = getString(R.string.string_err_auth);
                break;
            case ErrorCode.ERR_MEMORY:
                result = getString(R.string.string_err_memory);
                break;
            case ErrorCode.ERR_INVALID_TMPL_NO:
                result = getString(R.string.string_err_invalid_tmpl_id);
                break;
            case ErrorCode.ERR_INVALID_PARAM:
                result = getString(R.string.string_err_invalid_param);
                break;
            case ErrorCode.ERR_TIME_OUT:
                result = getString(R.string.string_err_time_out);
                break;
            case ErrorCode.ERR_GEN_COUNT:
                result = getString(R.string.string_err_gen_count);
                break;
            case ErrorCode.ERR_INVALID_BUFFER_ID:
                result = getString(R.string.string_err_invalid_buffer_id);
                break;
            case ErrorCode.ERR_INVALID_OPERATION_MODE:
                result = getString(R.string.string_err_invalid_operation_mode);
                break;
            case ErrorCode.ERR_FP_NOT_DETECTED:
                result = getString(R.string.string_err_not_detectd);
                break;
            case ErrorCode.ERR_FP_CANCEL:
                result = getString(R.string.string_err_cancel);
                break;
            default:
                result = getString(R.string.string_err_unknow);
        }
        return result
                + " "
                + getString(R.string.string_err_code_format,
                String.format("0x%02x", errorCode));
    }
}
