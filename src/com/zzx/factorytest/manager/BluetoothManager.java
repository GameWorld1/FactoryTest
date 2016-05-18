package com.zzx.factorytest.manager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @{# BluetoothManager.java Create on 2012-11-28 11:07:00
 * @Version 1.0
 * @Author
 */
public class BluetoothManager {
    private static final String TAG = "BluetoothManager";
    private Context mContext;
    private static BluetoothSocket btSocket;
    private static final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
    private ArrayAdapter<String> adtDevices;
    private BluetoothAdapter btAdapt;
    private Handler mHandler;

    public BluetoothManager(Context mContext) {
        this.mContext = mContext;
        init();
    }

    public BluetoothManager(Context mContext, Handler mHandler) {
        this.mContext = mContext;
        this.mHandler = mHandler;
        init();
    }

    private void init() {
        btAdapt = BluetoothAdapter.getDefaultAdapter();// ��ʼ��������������
        IntentFilter intent = new IntentFilter();
        //intent.addAction(BluetoothDevice.ACTION_FOUND);// ��BroadcastReceiver��ȡ���������
        intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        mContext.registerReceiver(searchDevices, intent);
    }


    /**
     * �����������������豸��
     *
     * @param name
     * @return
     */
    public boolean renameDevice(String name) {
        return btAdapt.setName(name);
    }

    public String getDeviceName() {
        return btAdapt.getName();
    }

    /**
     * ��ȡMAC��ַ
     *
     * @return
     */
    public String getMacAdress() {
        return btAdapt.getAddress();
    }

    /**
     * ע��㲥
     */
    private BroadcastReceiver searchDevices = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle b = intent.getExtras();
            Object[] lstName = b.keySet().toArray();

            // ��ʾ�����յ�����Ϣ����ϸ��
            for (int i = 0; i < lstName.length; i++) {
                String keyName = lstName[i].toString();
                Log.e(keyName, String.valueOf(b.get(keyName)));
            }
            BluetoothDevice device = null;
            // �����豸ʱ��ȡ���豸��MAC��ַ
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if (mHandler != null) {
                    mHandler.sendEmptyMessage(BluetoochState.BLUETOOCH_STATE_ACTION_FOUND);
                }
            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {

            } else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.STATE_OFF);
                if (mHandler != null) {

                    if (state == BluetoothAdapter.STATE_OFF) {
                        Log.i(TAG, "----------------BLUETOOCH_STATE_OFF--");
                        mHandler.sendEmptyMessage(BluetoochState.BLUETOOCH_STATE_OFF);
                    } else if (state == BluetoothAdapter.STATE_ON) {
                        Log.i(TAG, "----------------BLUETOOCH_STATE_ON--");
                        mHandler.sendEmptyMessage(BluetoochState.BLUETOOCH_STATE_ON);
                    } else if (state == BluetoothAdapter.STATE_TURNING_OFF) {
                        mHandler.sendEmptyMessage(BluetoochState.BLUETOOCH_STATE_TURNING_OFF);
                        Log.i(TAG, "----------------STATE_TURNING_OFF--");
                    } else if (state == BluetoothAdapter.STATE_TURNING_ON) {
                        mHandler.sendEmptyMessage(BluetoochState.BLUETOOCH_STATE_TURNING_ON);
                        Log.i(TAG, "----------------STATE_TURNING_ON--");
                    } else if (state == BluetoothAdapter.ERROR) {
                        mHandler.sendEmptyMessage(BluetoochState.BLUETOOCH_STATE_FAIL);
                        Log.i(TAG, "----------------ERROR--");

                    }
                }

            }
        }
    };

    public void connectRemote(String address) {
        if (btAdapt.isDiscovering())
            btAdapt.cancelDiscovery();
        /*
		 * String str = lstDevices.get(location); String[] values =
		 * str.split("\\|"); String address = values[2]; Log.e(TAG, "address = "
		 * + values[2]);
		 */
        BluetoothDevice btDev = btAdapt.getRemoteDevice(address);
        try {
            Boolean returnValue = false;
            if (btDev.getBondState() == BluetoothDevice.BOND_NONE) {
                // ���÷��䷽������BluetoothDevice.createBond(BluetoothDevice
                // remoteDevice);
                Method createBondMethod = BluetoothDevice.class
                        .getMethod("createBond");
                Log.d(TAG, "��ʼ���");
                returnValue = (Boolean) createBondMethod.invoke(btDev);

            } else if (btDev.getBondState() == BluetoothDevice.BOND_BONDED) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean isOpenBluetooch() {
        boolean flag = false;
        if (btAdapt != null) {
            int state = btAdapt.getState();
            if (state == BluetoothAdapter.STATE_OFF)
                flag = false;
            else if (state == BluetoothAdapter.STATE_ON)
                flag = true;
        }
        return flag;
    }

    /**
     * ����������������
     */
    public void openBluetooth() {
        if (btAdapt != null) {

            btAdapt.enable();
        }
    }

    /**
     * �����������ر�����
     */
    public void closeBluethooth() {
        if (btAdapt != null) {
            btAdapt.disable();
        }
    }

    /**
     * �������������������豸
     */
    public void findDevice() {
        if (btAdapt.getState() == BluetoothAdapter.STATE_OFF) {// ���������û����
            // HSJToast.makeText(BlueToothTestActivity.this, "���ȴ�����",
            // 1000).show();
            Log.i(TAG, "�ȴ�����");
            return;
        }
        if (btAdapt.isDiscovering())
            btAdapt.cancelDiscovery();
        // list.clear();
        Object[] lstDevice = btAdapt.getBondedDevices().toArray();
        for (int i = 0; i < lstDevice.length; i++) {
            BluetoothDevice device = (BluetoothDevice) lstDevice[i];
            String str = "�����|" + device.getName() + "|" + device.getAddress();
            // lstDevices.add(str); // ��ȡ�豸���ƺ�mac��ַ
            // adtDevices.notifyDataSetChanged();
            // Log.i(TAG, str);

        }
        Log.i(TAG, "����������ַ��" + btAdapt.getAddress());
        btAdapt.startDiscovery();
    }

    /**
     * ���������������ֻ������ԣ������豸һ�ɹ��˵�
     *
     * @return
     */
    private boolean isAccordDevicePair(BluetoothDevice device) {
        boolean flag = false;
        BluetoothClass btClass = device.getBluetoothClass();
        if (btClass != null) {
            int major = btClass.getMajorDeviceClass();
            if (major == BluetoothClass.Device.Major.COMPUTER
                    || major == BluetoothClass.Device.Major.PHONE) {
                flag = true;
            }
        }
        return flag;
    }

    public void unregisterBluethoothReceiver() {
        try {
            if (btSocket != null)
                btSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mContext.unregisterReceiver(searchDevices);

    }


    public static class BluetoochState {
        public static final int BLUETOOCH_STATE_ACTION_FOUND = 21;// ����Ѱ��

        public static final int BLUETOOCH_STATE_OFF = 22;// �ر�

        public static final int BLUETOOCH_STATE_ON = 23;// ����

        public static final int BLUETOOCH_STATE_TURNING_ON = 24;// ���ڿ���

        public static final int BLUETOOCH_STATE_TURNING_OFF = 25;// ���ڹر�

        public static final int BLUETOOCH_STATE_FAIL = 26;// ����ʧ��

        public static final int BLUETOOCH_BOND_STATE_CHANGED = 27;// ������״̬


    }

}
