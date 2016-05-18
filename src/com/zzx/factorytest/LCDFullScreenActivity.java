package com.zzx.factorytest;

import com.zzx.factorytest.view.ScreenCanvasView;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.WindowManager;

public class LCDFullScreenActivity extends Activity {
	private ScreenCanvasView canvas;
	private int currentIndex = 0;
	private int test_mode = 0;

	private int[] color_test = new int[] { ScreenCanvasView.MODE_COLOR_BLUE,
			ScreenCanvasView.MODE_COLOR_BLACK,
			ScreenCanvasView.MODE_COLOR_GREEN,
			ScreenCanvasView.MODE_COLOR_PURPLE,
			ScreenCanvasView.MODE_COLOR_RED, ScreenCanvasView.MODE_COLOR_WHITE,
			ScreenCanvasView.MODE_COLOR_YELLOW };

	private int[] geometry_test = new int[] {
			ScreenCanvasView.MODE_GEOMETRY_WHITE_BLACK,
			ScreenCanvasView.MODE_GEOMETRY_BLUE_BLACK,
			ScreenCanvasView.MODE_GEOMETRY_GREEN_BLACK,
			ScreenCanvasView.MODE_GEOMETRY_RED_BLACK, };

	private int[] mix_test = new int[] { ScreenCanvasView.MODE_MIX_1DOT,
			ScreenCanvasView.MODE_MIX_2DOT, ScreenCanvasView.MODE_MIX_3DOT,
			ScreenCanvasView.MODE_MIX_1DOT_1BLACK,
			ScreenCanvasView.MODE_MIX_2DOT_1BLACK,
			ScreenCanvasView.MODE_MIX_3DOT_1BLACK,
			ScreenCanvasView.MODE_MIX_1DOT_1WHITE,
			ScreenCanvasView.MODE_MIX_2DOT_1WHITE,
			ScreenCanvasView.MODE_MIX_3DOT_1WHITE,
			ScreenCanvasView.MODE_MIX_1H, ScreenCanvasView.MODE_MIX_2H,
			ScreenCanvasView.MODE_MIX_3H, ScreenCanvasView.MODE_MIX_1V,
			ScreenCanvasView.MODE_MIX_2V, ScreenCanvasView.MODE_MIX_3V };

	private int[] step_test = new int[] { ScreenCanvasView.MODE_STEP_H_8,
			ScreenCanvasView.MODE_STEP_H_16, ScreenCanvasView.MODE_STEP_H_32,
			ScreenCanvasView.MODE_STEP_H_64, ScreenCanvasView.MODE_STEP_V_8,
			ScreenCanvasView.MODE_STEP_V_16, ScreenCanvasView.MODE_STEP_V_32,
			ScreenCanvasView.MODE_STEP_V_64, };

	private int[] current_test_res;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.lcd_fullscreen_layout);
		canvas = (ScreenCanvasView) findViewById(R.id.lcd_canvas);

		test_mode = getIntent().getIntExtra("mode", 0);

		switch (test_mode) {
		case LCDActivity.LCD_TEST_MODE_COLOR:
			current_test_res = color_test;
			break;
		case LCDActivity.LCD_TEST_MODE_GEOMETRY:
			current_test_res = geometry_test;
			break;
		case LCDActivity.LCD_TEST_MODE_MIX:
			current_test_res = mix_test;
			break;
		case LCDActivity.LCD_TEST_MODE_STEP:
			current_test_res = step_test;
			break;
		default:
			current_test_res = color_test;
			break;
		}
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		changeImage();
		super.onResume();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			changeImage();
		}
		return super.onTouchEvent(event);
	}

	private void changeImage() {
		if (currentIndex >= current_test_res.length) {
			finish();
			// currentIndex = 0;
			return;

		}
		canvas.setCurrentMode(current_test_res[currentIndex], currentIndex);
		currentIndex++;

	}

}
