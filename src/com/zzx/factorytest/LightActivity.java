package com.zzx.factorytest;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class LightActivity extends TestItemBaseActivity implements
		SensorEventListener {

	private Sensor gravitySensor;

	private SensorManager mSensorManager;

	private TextView result;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams. FLAG_FULLSCREEN);
		setContentView(R.layout.light_layout);
		super.onCreate(savedInstanceState);

		result = (TextView) findViewById(R.id.result);
		

		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		gravitySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
		mSensorManager.registerListener(this, gravitySensor, 0);
	}

	@Override
	protected void onDestroy() {
		mSensorManager.unregisterListener(this, gravitySensor);
		super.onDestroy();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		
		result.setText(String.format("%.02f",  event.values[0]));

	}


}
