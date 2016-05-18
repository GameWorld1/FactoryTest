package com.zzx.factorytest;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zzx.factorytest.view.JudgeView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class LightSensorActivity extends TestItemBaseActivity implements SensorEventListener {
    public static final String TAG = "LightSensorActivity.cla";
    private SensorManager mManager;
    private Sensor mLight;
    private ProgressBar mLightProgressBar;
    private TextView mLightTextView;
    private Sensor mProximity;
    private ProgressBar mProximityProgressBar;
    private TextView mProximityTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.light_sensor_activity);
        mManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLight = mManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mProximity = mManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        initView();
    }

    private void initView() {

        mLightProgressBar = (ProgressBar) findViewById(R.id.light_number_PB);
        mLightTextView = (TextView) findViewById(R.id.light_number_TV);

        mProximityProgressBar = (ProgressBar) findViewById(R.id.proximity_PB);
        mProximityTextView = (TextView) findViewById(R.id.proximity_TV);

        mLightProgressBar.setMax((int) SensorManager.LIGHT_SHADE);
        mProximityProgressBar.setMax(1);

        ((JudgeView) findViewById(R.id.judgeview)).setOnResultSelectedListener(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (null != mLight) {
            mManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_FASTEST);
            mManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_FASTEST);
        }
        Log.d(TAG, "注册监听");
        /*
        File in = new File("data/data/com.zzx.factorytest/shared_prefs/factoryTest.xml");
        File out = new File("D:/Log.txt");
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(in)));


            String line;
            while (null != (line = bufferedReader.readLine())) {
                Log.d(TAG, "line : " + line);
            }

            bufferedReader.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }

        Log.d(TAG, "in : " + in);
        Log.d(TAG, "out : " + out);
        */
    }

    @Override
    protected void onPause() {
        super.onPause();

        mManager.unregisterListener(this);
        Log.d(TAG, "注销监听");
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] values = event.values;

        switch (event.sensor.getType()) {
            case Sensor.TYPE_LIGHT:
                mLightProgressBar.setProgress(Math.round(values[0]));
                mLightTextView.setText(getResources().getString(R.string.light_number) + values[0]);
                Log.d(TAG, "光线 values[] : " + Arrays.toString(values));
                break;
            case Sensor.TYPE_PROXIMITY:
                mProximityProgressBar.setProgress(values[0] == 1.0f ? 0 : 1);
                mProximityTextView.setText(getResources().getString(R.string.proximity_number) + values[0]);
                Log.d(TAG, "距离 values[] : " + Arrays.toString(values));
                break;
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}

