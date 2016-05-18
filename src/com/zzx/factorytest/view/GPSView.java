package com.zzx.factorytest.view;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.RectF;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.GpsStatus.Listener;
import android.location.GpsStatus.NmeaListener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.zzx.factorytest.R;
import com.zzx.factorytest.manager.GPSManager;
import com.zzx.factorytest.utils.CommonUtils;

/**
 * @author 闄堜紵鏂�
 */
public class GPSView extends View implements Listener, NmeaListener,
		LocationListener {

	private static final String NEMADATA_FILE = "/data/gps/log";
	private float graphWidth;
	private float graphHeigth;
	private float padingLeft = 0;
	private float padingRight = 0;
	private float padingTop = 100;
	private float padingBottom = 80;
	private int yLineNumber = 3;// y杞村埢搴︽暟
	private int maxStarNumber = 13;// 鏈�澶ф槦绮掓暩
	private float singalBarWidth = 0;
	private float singalGap = 0;// 鐩搁偦鏌辩姸鍥剧殑闂磋窛
	private float singalViewScale = 0.6f;
	float colorBarPadingTop = 0;
	float colorBarpadingLeft = 0;
	private float colorBarWidth = 0;// 鑹插付瀵害
	private boolean isButtonPress = false;
	private RectF buttonRect;
	private RectF mainRect;
	private RectF dataRect;
	private Rect nemaRect;
	private int[] gps_snr = { 0, 10, 20, 30, 40, 99 };
	private int[] colorList = { mx_color_red, mx_color_org, mx_color_yellow,
			mx_color_green_light, mx_color_green_dark };
	static int mx_color_grap;
	static int mx_color_red;
	static int mx_color_org;
	static int mx_color_yellow;
	static int mx_color_green_light;
	static int mx_color_green_dark;
	private GPSManager gpsManager;
	private LocationManager locationManager;
	private String nemaData = "";
	private double longitude;
	private double latitude;
	private GpsStatus gpsStatus = null;
	private int satelliteInFixCount = 0;// 瀹氬埌浣嶇殑鍗槦鏁�
	private int satelliteCount = 0;// 鎼滅储鍒扮殑鍗槦鏁�
	private TimeThread timeThread = null;
	private Handler mHandler = null;
	private int timeFix = 0;
	private boolean hasFix = false;
	private String prenema;
	private boolean isNemaChange;
	private AutoTestCallBack autoTestCallBack;
	private Resources mRes = null;

	static {
		mx_color_grap = Color.rgb(192, 192, 192);
		mx_color_red = Color.rgb(255, 0, 0);
		mx_color_org = Color.rgb(255, 128, 0);
		mx_color_yellow = Color.rgb(255, 255, 0);
		mx_color_green_light = Color.rgb(217, 255, 0);
		mx_color_green_dark = Color.rgb(0, 255, 0);
	}

	public GPSView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mRes = context.getResources();
		colorBarWidth = mRes.getDimension(R.dimen.GPSColorBarWidth);
		singalGap = mRes.getDimension(R.dimen.GPSSingalGap);
		padingLeft = mRes.getDimension(R.dimen.GPSPadingLeft);
		padingRight = mRes.getDimension(R.dimen.GPSPadingRinght);
		colorBarpadingLeft = mRes.getDimension(R.dimen.GPSColorBarpadingLeft);
		Log.d("tomy", "colorBarpadingLeft = " + colorBarpadingLeft);
		colorBarPadingTop = mRes.getDimension(R.dimen.GPSColorBarpadingTop);
		gpsManager = new GPSManager(context);
		locationManager = ((LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE));
		locationManager.addGpsStatusListener(this);
		locationManager.addNmeaListener(this);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				1000, 0.0f, this);
		gpsStatus = locationManager.getGpsStatus(null);
		this.setBackgroundColor(Color.BLACK);

		mHandler = new HandlerTime();
		timeThread = new TimeThread();
		timeThread.start();
	}

	class HandlerTime extends Handler {
		@Override
		public void handleMessage(Message msg) {
			timeFix++;
			if (hasFix) {
				timeThread.stopThread();
			}
			invalidate();
		}
	}

	;

	@Override
	protected void onDraw(Canvas canvas) {
		drawGridBg(canvas);
		drawColorBar(canvas);
		drawStarSingal(canvas);
		drawData(canvas);

		super.onDraw(canvas);
	}

	/**
	 * 缁樺埗鍗槦鏁版嵁
	 * 
	 * @param canvas
	 */
	private void drawData(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setTextSize(this.getResources()
				.getDimension(R.dimen.GPSTextSizes));

		float textHeight = paint.measureText("姝�");
		// 鍗槦鏁扮洰
		canvas.drawText("Count:" + satelliteCount, mainRect.left + 5,
				mainRect.top + textHeight, paint);

		// 瀹氫綅鍗槦鏁�
		String text = "Fix:" + satelliteInFixCount;
		float textWidth = paint.measureText(text);
		canvas.drawText(text, mainRect.right - textWidth - 5, mainRect.top
				+ textHeight, paint);

		dataRect = new RectF(mainRect.right + colorBarpadingLeft * 8,
				mainRect.top, mainRect.left, mainRect.bottom);// 棰戠箒鍒锋柊鍖哄煙
		// Log.d("tomy", "dataRect.left = " + dataRect.left +
		// "; colorBarpadingLeft = " + colorBarpadingLeft);
		// 缁忕含搴�

		String lon = "0.0";
		String lat = "0.0";
		if (longitude != 0) {
			lon = CommonUtils.keep2Point(longitude)
					+ (longitude > 0 ? "E" : "W");
		}
		if (latitude != 0) {
			lat = CommonUtils.keep2Point(latitude) + (latitude > 0 ? "N" : "S");
		}
		canvas.drawText("缁忓害:" + lon, dataRect.left
				- this.getResources().getDimension(R.dimen.GPSdataRectLeft),
				(float) (mainRect.top + mainRect.height() * 0.3), paint);
		canvas.drawText(
				"绾害:" + lat,
				dataRect.left
						- this.getResources().getDimension(
								R.dimen.GPSdataRectLeft),
				(float) (mainRect.top + mainRect.height() * 0.3 + textHeight + 3),
				paint);

		// 瀹氫綅鏃堕棿
		String str = "瀹氫綅鏃堕棿(s)";// String.format("%02d", snr);
		textWidth = paint.measureText(str);
		float x = dataRect.left
				- this.getResources().getDimension(R.dimen.GPSdataRectLeft);
		float y = (float) (mainRect.top + mainRect.height() * 0.7);
		drawText(canvas, str, x, y, paint, 0);
		paint.setTextSize(this.getResources()
				.getDimension(R.dimen.GPSTextSizem));
		String str_time = timeFix + "";
		float str_time_width = paint.measureText(str_time);
		float height = mRes.getDimension(R.dimen.GPSTimeHeight);
		canvas.drawText(str_time, x + textWidth / 2 - str_time_width / 2, y
				+ height, paint);

		// nema鏁版嵁
		paint.setTextSize(this.getResources()
				.getDimension(R.dimen.GPSTextSizes));
		if (nemaRect == null) {
			nemaRect = new Rect((int) mainRect.left, (int) this.getResources()
					.getDimension(R.dimen.GPSnemaRect), (int) mainRect.right,
					(int) ((int) this.getResources().getDimension(
							R.dimen.GPSnemaRect) + textHeight));
		}
		canvas.drawText(nemaData.toCharArray(), 0, nemaData.length() > 40 ? 40
				: nemaData.length(), nemaRect.left, nemaRect.top, paint);

		// gps寮�鍏�
		int buttonHeight = (int) this.getResources().getDimension(
				R.dimen.GPSbuttonHeight);
		int buttonWidth = (int) this.getResources().getDimension(
				R.dimen.GPSbuttonWidth);
		float buttonPadingTop = 20;
		if (!isButtonPress) {
			paint.setColor(Color.rgb(136, 136, 136));
		} else {
			paint.setColor(Color.rgb(80, 80, 80));
		}
		buttonRect = new RectF(dataRect.left
				- (int) this.getResources().getDimension(
						R.dimen.GPSdataRectLeft), buttonPadingTop,
				dataRect.left + buttonWidth, buttonPadingTop + buttonHeight);
		canvas.drawRoundRect(buttonRect, 10, 10, paint);

		paint.setColor(Color.WHITE);
		paint.setTextSize(this.getResources()
				.getDimension(R.dimen.GPSTextSizes));
		canvas.drawText("GPS鐘舵��", buttonRect.left + 3, buttonRect.top
				+ textHeight, paint);

		int circleRadio = (int) this.getResources().getDimension(
				R.dimen.GPScircleRadio);
		paint.setTextSize(this.getResources()
				.getDimension(R.dimen.GPSTextSizem));
		textHeight = paint.measureText("姝�");
		boolean gpsEnable = gpsManager.isGPSEnable();
		String label = gpsEnable ? "寮�" : "鍏�";
		int color = gpsEnable ? Color.GREEN : Color.RED;
		paint.setColor(color);
		canvas.drawCircle((float) (buttonRect.left + buttonRect.width() * 0.2),
				buttonRect.top + buttonRect.height() / 2 + circleRadio,
				circleRadio, paint);
		canvas.drawText(label,
				(float) (buttonRect.left + buttonRect.width() * 0.5),
				buttonRect.top + buttonRect.height() / 2 + circleRadio / 2
						+ textHeight / 2, paint);
	}

	@Override
	protected void finalize() throws Throwable {
		locationManager.removeGpsStatusListener(this);
		locationManager.removeNmeaListener(this);
		locationManager.removeUpdates(this);
		super.finalize();
	}

	public void stopGPS() {
		locationManager.removeGpsStatusListener(this);
		locationManager.removeNmeaListener(this);
		locationManager.removeUpdates(this);
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			gpsManager.stopGPS();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.i("test", event.getAction() + "--------------------");
		switch (event.getAction()) {

		case MotionEvent.ACTION_DOWN:
			if (buttonRect != null) {
				if (buttonRect.contains(event.getX(), event.getY())) {
					isButtonPress = true;
					// gpsManager.toggleGPS();
					Rect rect = new Rect();
					buttonRect.round(rect);
					this.invalidate(rect);
				}

			}
			break;
		case MotionEvent.ACTION_CANCEL:

		case MotionEvent.ACTION_UP:
			if (isButtonPress) {
				isButtonPress = false;
				Rect rect = new Rect();
				buttonRect.round(rect);
				Intent intent = new Intent();
				intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				getContext().startActivity(intent);
				this.invalidate(rect);
			}
			break;
		default:
			break;
		}

		return true;
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {

		graphWidth = this.getWidth();
		graphHeigth = this.getHeight();
		super.onLayout(changed, left, top, right, bottom);
	}

	void drawText(Canvas canvas, String text, float x, float y, Paint paint,
			float angle) {
		if (angle != 0) {
			canvas.rotate(angle, x, y);
		}
		canvas.drawText(text, x, y, paint);
		if (angle != 0) {
			canvas.rotate(-angle, x, y);
		}
	}

	/**
	 * 鐢诲崼鏄熶俊鍙�
	 * 
	 * @param canvas
	 */
	private void drawStarSingal(Canvas canvas) {

		// 鏌辩姸鍥惧搴�
		singalBarWidth = (mainRect.right - mainRect.left) / maxStarNumber
				- singalGap;

		Iterator<GpsSatellite> iterator = this.gpsStatus.getSatellites()
				.iterator();
		float offset = singalGap;
		this.satelliteInFixCount = 0;
		this.satelliteCount = 0;
		if (gpsStatus == null) {
			return;
		}
		while (iterator.hasNext()) {
			GpsSatellite satellite = iterator.next();
			boolean isFix = satellite.usedInFix();// 鏄惁瀹氬埌浣�
			satelliteCount++;
			if (isFix) {
				this.satelliteInFixCount++;
			}
			if (satelliteCount <= maxStarNumber) {
				drawSingleStarInfo(offset, satellite.getSnr(),
						satellite.getPrn(), isFix, canvas);
				offset += singalBarWidth + singalGap;
			}
		}
		// drawSingleStarInfo(10, 30, 1, false, canvas);
		// drawSingleStarInfo(10 + singalBarWidth + singalGap, 80, 2, true,
		// canvas);
		// drawSingleStarInfo(10 + singalBarWidth + singalGap + singalBarWidth
		// + singalGap, 0, 10, false, canvas);
	}

	/**
	 * 鐢诲崟涓崼鏄熶俊鍙�
	 */
	private void drawSingleStarInfo(float xOffset, float snr, int pnr,
			boolean isInFix, Canvas canvas) {
		Paint paint = new Paint();
		paint.setStyle(Style.FILL_AND_STROKE);

		// 鐢绘煴鐘跺浘
		if (!isInFix) {
			paint.setColor(mx_color_grap);
		} else {
			if (snr <= gps_snr[1]) {
				paint.setColor(mx_color_red);
			} else if (snr <= gps_snr[2]) {
				paint.setColor(mx_color_org);
			} else if (snr <= gps_snr[3]) {
				paint.setColor(mx_color_yellow);
			} else if (snr <= gps_snr[4]) {
				paint.setColor(mx_color_green_light);
			} else if (snr <= gps_snr[5]) {
				paint.setColor(mx_color_green_dark);
			} else {
				paint.setColor(mx_color_grap);
			}
		}

		float barHeight = (float) ((mainRect.height() * 0.9 / (gps_snr[gps_snr.length - 1])) * snr);
		int minBarHeight = 4;
		if (barHeight < minBarHeight) {
			barHeight = minBarHeight;
		}
		RectF startBarRect = new RectF(mainRect.left + xOffset, mainRect.bottom
				- barHeight, mainRect.left + xOffset + singalBarWidth,
				mainRect.bottom);
		canvas.drawRect(startBarRect, paint);

		// 鐢籶nr鍊�
		paint.setColor(Color.WHITE);
		paint.setTextSize(this.getResources()
				.getDimension(R.dimen.GPSTextSizes));
		String pnrText = String.format("%02d", pnr);
		float textWidth = paint.measureText(pnrText);
		float textX = (startBarRect.right + startBarRect.left) / 2 - textWidth
				/ 2;
		canvas.drawText(pnrText, textX, startBarRect.bottom + 30, paint);

		// 鐢籹nr鍊�
		String snrText = (int) snr + "";// String.format("%02d", snr);
		textWidth = paint.measureText(snrText);
		textX = (startBarRect.right + startBarRect.left) / 2 - textWidth / 2;
		canvas.drawText(snrText, textX, startBarRect.top - 10, paint);
	}

	private void drawColorBar(Canvas canvas) {

		// 鐢昏壊甯�
		float colorBarHeight = (float) ((mainRect.height()) * 0.85);
		float unit = colorBarHeight
				/ (gps_snr[gps_snr.length - 1] - gps_snr[0]);

		Paint p = new Paint();
		p.setStyle(Style.FILL);
		p.setTextSize(this.getResources().getDimension(R.dimen.GPSTextSizes));

		float beginY = 0;
		float currentColorBarHeight = 0;

		for (int i = gps_snr.length - 1; i > 0; i--) {

			if (i > 0) {
				currentColorBarHeight = (gps_snr[i] - gps_snr[i - 1]) * unit;
				p.setColor(colorList[i - 1]);
				RectF colorRect = new RectF(
						mainRect.right + colorBarpadingLeft, mainRect.top
								+ colorBarPadingTop + beginY, mainRect.right
								+ colorBarpadingLeft + colorBarWidth,
						mainRect.top + colorBarPadingTop + beginY
								+ currentColorBarHeight);
				canvas.drawRect(colorRect, p);

				// 鍐欏埢搴�
				String text = gps_snr[i] + "";
				float textHeight = p.measureText(text);
				canvas.drawText(text, colorRect.right + 5, colorRect.top
						+ textHeight, p);
				beginY += currentColorBarHeight;
			}

		}
		// 鐢绘枃瀛楄鏄�
		p.setColor(Color.WHITE);
		String text = "SNR";
		float textHeight = p.measureText(text);
		canvas.drawText("SNR", mainRect.right + colorBarpadingLeft,
				mainRect.top + textHeight / 2, p);// SNR

	}

	private void drawGridBg(Canvas canvas) {
		padingTop = this.getResources().getDimension(R.dimen.GPSpadingTop);
		padingBottom = this.getResources()
				.getDimension(R.dimen.GPSpadingBottom);
		// 鐢诲妗�
		mainRect = new RectF(padingLeft, padingTop, graphWidth
				* singalViewScale - padingRight, graphHeigth - padingBottom);
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setStyle(Style.STROKE);
		canvas.drawRect(mainRect, paint);

		// 鐢诲唴绾�
		float gap = mainRect.height() / (yLineNumber + 1);
		PathEffect effects = new DashPathEffect(new float[] { 5, 5, 5, 5 }, 1);
		paint.setPathEffect(effects);
		for (int i = 0; i < yLineNumber; i++) {
			Path path = new Path();
			path.moveTo(mainRect.left, mainRect.top + (i + 1) * gap);
			path.lineTo(mainRect.right, mainRect.top + (i + 1) * gap);
			canvas.drawPath(path, paint);
		}

		//
		// // 鐢绘枃瀛楄鏄�
		// paint.setTextSize(25);
		// canvas.drawText("WIFI棰戦亾", (rect.right - rect.left) / 2,
		// this.getBottom() - 10, paint);

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}

	@Override
	public void onGpsStatusChanged(int event) {
		switch (event) {
		case 4:
			gpsStatus = locationManager.getGpsStatus(null);
			// 鐢ㄤ簬鍥炶皟鑷姩娴嬭瘯缁撴灉
			if (autoTestCallBack != null) {
				autoTestCallBack.autoTestResult(isNemaChange
						&& ((satelliteInFixCount > 0) || satelliteCount >= 3));
			}
			invalidate();

			break;
		case 3:
			hasFix = true;
			break;
		case 1:
			hasFix = false;
			break;
		case 2:
			hasFix = false;
		}

	}

	@Override
	public void onNmeaReceived(long timestamp, String nmea) {
		this.nemaData = nmea;

		File file = new File(NEMADATA_FILE);
		FileWriter fwriter;
		try {
			fwriter = new FileWriter(file);
			BufferedWriter writer = new BufferedWriter(fwriter);
			writer.write(nmea);
			writer.newLine();
			fwriter.close();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!nmea.equals(prenema)) {
			isNemaChange = true;
		}
		prenema = nmea;

		this.invalidate();

	}

	@Override
	public void onLocationChanged(Location location) {
		longitude = location.getLongitude();
		latitude = location.getLatitude();
		// Rect rect = new Rect();
		// dataRect.round(rect);
		// invalidate(rect);
		invalidate();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

		gpsStatus = locationManager.getGpsStatus(null);
		this.invalidate();
	}

	@Override
	public void onProviderEnabled(String provider) {
		this.invalidate();

	}

	@Override
	public void onProviderDisabled(String provider) {
		this.invalidate();

	}

	public class TimeThread extends Thread {
		private int iCount = 0;
		private boolean bStart = false;

		@Override
		public synchronized void start() {
			bStart = true;
			super.start();
		}

		public void stopThread() {
			bStart = false;
		}

		@Override
		public void run() {
			while (bStart) {
				try {
					sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (iCount++ >= 10) {
					iCount = 0;
					mHandler.sendEmptyMessage(0);
				}
			}
		}
	}

	public void setAutoTestCallBack(AutoTestCallBack callBack) {
		this.autoTestCallBack = callBack;
	}

	public interface AutoTestCallBack {
		public void autoTestResult(boolean result);
	}
}
