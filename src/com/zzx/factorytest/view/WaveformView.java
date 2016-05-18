package com.zzx.factorytest.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

import com.zzx.factorytest.MICPhoneTestActivity;

public class WaveformView extends View {
	private Bitmap.Config config = null;
	private Canvas canvas = null;
	private Bitmap bitmap = null;
	private Paint paint = null;
	private short[] audioBuffer = null;
	private float scale = 0;
	private int mWidth = 0;
	private int mHeight = 0;
	private int marginPix = 2;
	private int dispWidth = 0;
	private float space = 0;
	private int mBaseX = 0;
	private int mBaseY = 0;
	private float bias = 0;

	public WaveformView(Context context, int _width, int _height) {
		super(context);
		mHeight = _height;
		mWidth = _width;
		mBaseX = marginPix;
		mBaseY = mHeight / 2;
		dispWidth = mWidth - marginPix * 2;
		Log.d(MICPhoneTestActivity.TAG_D, "mWidth = " + mWidth
				+ " ; dispWidth = " + dispWidth);
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);
	}

	public void updateView(short[] buffer, float bias, float range) {
		audioBuffer = buffer;
		scale = (float) Math.pow(1f / (range / 6500f), 0.7) / 16384
				* (mHeight - marginPix);
		if (scale < 0.001f || Float.isInfinite(scale))
			scale = 0.001f;
		else if (scale > 1000f)
			scale = 1000f;
		space = dispWidth / 256.0f;
		// Log.d(MICPhoneTestActivity.TAG_D, "space -------> " + space);
		this.bias = bias;
		this.invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		drawAxis(canvas);
		if (audioBuffer != null) {
			drawAudioBuffer(canvas);
		}

		super.onDraw(canvas);
	}

	private void drawAudioBuffer(Canvas canvas) {
		for (int i = 0; i < audioBuffer.length; i++) {
			// canvas.drawLine(i, mBaseY, i, mBaseY - audioBuffer[i], paint);
			float startX = mBaseX + i * space + 1;
			int flag = Math.abs(audioBuffer[i]) <= 130 ? 0 : audioBuffer[i];
			float y = mBaseY - (flag - bias) * scale;
			canvas.drawLine(startX, mBaseY, startX, y, paint);
		}
	}

	private void drawAxis(Canvas canvas) {
		paint.setColor(0xffffff00);
		canvas.drawLine(marginPix, marginPix, marginPix, mHeight - marginPix
				* 2, paint);
		// canvas.drawLine(marginPix, mHeight / 2, mWidth - marginPix * 2,
		// mHeight / 2, paint);
	}

}
