package com.zzx.factorytest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LCDActivity extends TestItemBaseActivity implements
		OnClickListener {

	public final static int LCD_TEST_MODE_COLOR = 1;
	public final static int LCD_TEST_MODE_GEOMETRY = 2;
	public final static int LCD_TEST_MODE_MIX = 3;//
	public final static int LCD_TEST_MODE_STEP = 4;//

	private Button btn_color;
	private Button btn_geometry;
	private Button btn_mix;
	private Button btn_step;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams. FLAG_FULLSCREEN);
		// startActivity(new Intent(this,LCDFullScreenActivity.class));
		setContentView(R.layout.lcd_layout);
		super.onCreate(savedInstanceState);

		btn_color = (Button) findViewById(R.id.btn_color);
		btn_geometry = (Button) findViewById(R.id.btn_geometry);
		btn_mix = (Button) findViewById(R.id.btn_mix);
		btn_step = (Button) findViewById(R.id.btn_step);

		btn_color.setOnClickListener(this);
		btn_geometry.setOnClickListener(this);
		btn_mix.setOnClickListener(this);
		btn_step.setOnClickListener(this);
		// changeImage();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == btn_color.getId()) {
			Intent intent = new Intent(this, LCDFullScreenActivity.class);
			intent.putExtra("mode", LCD_TEST_MODE_COLOR);
			startActivity(intent);
		} else if (v.getId() == btn_geometry.getId()) {
			Intent intent = new Intent(this, LCDFullScreenActivity.class);
			intent.putExtra("mode", LCD_TEST_MODE_GEOMETRY);
			startActivity(intent);
		} else if (v.getId() == btn_mix.getId()) {
			Intent intent = new Intent(this, LCDFullScreenActivity.class);
			intent.putExtra("mode", LCD_TEST_MODE_MIX);
			startActivity(intent);
		} else if (v.getId() == btn_step.getId()) {
			Intent intent = new Intent(this, LCDFullScreenActivity.class);
			intent.putExtra("mode", LCD_TEST_MODE_STEP);
			startActivity(intent);
		}

	}

}
