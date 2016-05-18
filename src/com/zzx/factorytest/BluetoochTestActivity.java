package com.zzx.factorytest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.zzx.factorytest.manager.BluetoothManager;
import com.zzx.factorytest.manager.BluetoothManager.BluetoochState;

public class BluetoochTestActivity extends TestItemBaseActivity implements
        Callback {

    private BluetoothManager bluetoothManager;
    private Handler mHandler;
    private TextView bt_address;
    private TextView bt_status;
    private final int AUTO_TEST_TIMEOUT = 3;//
    private final int AUTO_TEST_MINI_SHOW_TIME = 2;//

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.bluetooch_test_layout);
        super.onCreate(savedInstanceState);
        mHandler = new Handler(this);

        bluetoothManager = new BluetoothManager(this, mHandler);
        bluetoothManager.openBluetooth();

        bt_address = (TextView) findViewById(R.id.txt_bt_address);
        bt_status = (TextView) findViewById(R.id.txt_bt_status);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!bluetoothManager.isOpenBluetooch()) {
            bt_status.setText(R.string.closed);
            synchronized (this) {

                stopAutoTest(false);
            }
        } else {
            bt_status.setText(R.string.opened);
            bt_address.setText(bluetoothManager.getMacAdress());

            if (bluetoothManager.getMacAdress() != null) {
                synchronized (this) {

                    stopAutoTest(true);
                }
            }
        }

    }

    @Override
    void executeAutoTest() {
        super.startAutoTest(AUTO_TEST_TIMEOUT, AUTO_TEST_MINI_SHOW_TIME);

    }

    @Override
    protected void onDestroy() {

        bluetoothManager.unregisterBluethoothReceiver();
        super.onDestroy();
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case BluetoochState.BLUETOOCH_STATE_ON:
                bt_status.setText(R.string.opened);
                bt_address.setText(bluetoothManager.getMacAdress());
                break;
            case BluetoochState.BLUETOOCH_STATE_TURNING_ON:
            case BluetoochState.BLUETOOCH_STATE_OFF:
                bt_status.setText(R.string.closed);
                bt_address.setText("");
                break;
            case BluetoochState.BLUETOOCH_STATE_TURNING_OFF:
                break;
            case BluetoochState.BLUETOOCH_STATE_FAIL:
                bt_status.setText(R.string.fail);
                bt_address.setText("");
            default:
                break;
        }

        return false;
    }

}
