package com.zzx.factorytest;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

public class GyroscopeActivity extends TestItemBaseActivity implements SensorEventListener {
    private static String TAG = "GyroscopeActivity";
    private SensorManager mSensorManager;
    private Sensor gyroscope;
    private ProgressBar mX, mY, mZ;
    private TextView mXTextView, mYTextView, mZTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_gyroscope);
        super.onCreate(savedInstanceState);


        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        initView();
    }

    private void initView() {
        mX = (ProgressBar) findViewById(R.id.x);
        mY = (ProgressBar) findViewById(R.id.y);
        mZ = (ProgressBar) findViewById(R.id.z);

        mXTextView = (TextView) findViewById(R.id.x_textview);
        mYTextView = (TextView) findViewById(R.id.y_textview);
        mZTextView = (TextView) findViewById(R.id.z_textview);

        mX.setMax(10);
        mY.setMax(10);
        mZ.setMax(10);

        mXTextView.setText(getResources().getString(R.string.gyroscope_x, 0.0));
        mYTextView.setText(getResources().getString(R.string.gyroscope_y, 0.0));
        mZTextView.setText(getResources().getString(R.string.gyroscope_z, 0.0));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null == gyroscope) {
            gyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        }
        mSensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);

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
    protected void onPause() {
        super.onPause();
        if (null != gyroscope) {
            mSensorManager.unregisterListener(this, gyroscope);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
//            Log.d(TAG, "x:" + event.values[0] + ",y:" + event.values[1] + ",z:" + event.values[2]);
            int roundX = Math.round(Math.abs(event.values[0]) + 0.5f);
            int roundY = Math.round(Math.abs(event.values[1]) + 0.5f);
            int roundZ = Math.round(Math.abs(event.values[2]) + 0.5f);

            mX.setProgress(roundX);
            mY.setProgress(roundY);
            mZ.setProgress(roundZ);

            mXTextView.setText(getResources().getString(R.string.gyroscope_x, event.values[0]));
            mYTextView.setText(getResources().getString(R.string.gyroscope_y, event.values[1]));
            mZTextView.setText(getResources().getString(R.string.gyroscope_z, event.values[2]));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d(TAG, sensor.getName() + "," + accuracy);
    }
}

