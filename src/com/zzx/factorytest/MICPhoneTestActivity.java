package com.zzx.factorytest;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zzx.factorytest.manager.FactoryTestManager;
import com.zzx.factorytest.utils.SinglePower;
import com.zzx.factorytest.view.PowerView;
import com.zzx.factorytest.view.WaveformView;

public class MICPhoneTestActivity extends TestItemBaseActivity {
	TextView tv = null;
	private WaveformView waveView = null;
	private PowerView powerView = null;
	// private ImageButton successBtn = null;
	// private ImageButton failedBtn = null;
	public static final String TAG_E = "micdebug";
	public static final String TAG_D = "micdebug";
	public static final int MSG_WHAT = 110;
	public static final boolean MICTEST_SUCCESSED = true;
	public static final boolean MICTEST_FAILED = false;
	public final static String MICTEST_SHARE_P = "mic_sharepreference";
	public final static String MICTEST_ISSUCCESSED = "mic_test_issuccess";
	private final static String AUDIO_DATA_MSG = "AudioRecordData";
	private final int AUTO_TEST_TIMEOUT = 10;//
	private final int AUTO_TEST_MINI_SHOW_TIME = 5;//
	private int screenHeight = 0;
	private int screenwidth = 0;
	private int viewHeight = 0;
	private int viewWidth = 0;
	private int viewMargin = 0;
	private int sampleRate = 44100;
	private int readSize = 256;
	private short[][] bufferTmp = null;
	private float[] biasRange = new float[2];
	private LinearLayout gugaLayout = null;
	private AudioRecord recorder = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.microphone_layout);
		super.onCreate(savedInstanceState);
		gugaLayout = (LinearLayout) findViewById(R.id.guga_layout);
		bufferTmp = new short[2][readSize];
		calViewRect();
		initRecorder();

		Log.d(TAG_D, "onCreate()");
		if (gugaLayout == null) {
			Log.d(TAG_D, "gugaLayout == null");
		} else {
			Log.d(TAG_D, "gugaLayout != null");
		}
		waveView = new WaveformView(this, viewWidth, viewHeight);
		powerView = new PowerView(this, viewHeight - viewMargin * 2, viewWidth
				- viewMargin * 2);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.bottomMargin = viewMargin;
		params.leftMargin = viewMargin;
		params.rightMargin = viewMargin;
		params.topMargin = viewMargin;
		params.weight = 1;
		gugaLayout.addView(waveView, params);
		// params.gravity = Gravity.BOTTOM;
		gugaLayout.addView(powerView, params);
		Thread audioReaderService = new AudioReaderService();
		audioReaderService.start();
	}

	@Override
	void executeAutoTest() {
		super.startAutoTest(AUTO_TEST_TIMEOUT, AUTO_TEST_MINI_SHOW_TIME);
		super.executeAutoTest();
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			short[] buffer = msg.getData().getShortArray(AUDIO_DATA_MSG);
			// Log.d(TAG_D, String.valueOf(buffer[i]));
			final int len = buffer.length;
			SinglePower.biasAndRange(buffer, len - readSize, readSize,
					biasRange);
			double currentPower = SinglePower.calculatePowerDb(buffer, len
					- readSize, readSize);
			final float bias = biasRange[0];
			float range = biasRange[1];
			if (range < 1f)
				range = 1f;
			// Log.d(TAG_D, "bias === " + biasRange[0] + " Rang === " +
			// biasRange[1]);
			powerView.update(currentPower);
			// Log.d(TAG_D, "currentPower ---------> " + currentPower);
			waveView.updateView(buffer, bias, range);
			onDataChange(currentPower);
			super.handleMessage(msg);
		}
	};

	private void onDataChange(double currentPower) {
		Log.i("test", "----------------" + currentPower);
		// �Զ�������
		if (FactoryTestManager.currentTestMode == FactoryTestManager.TestMode.MODE_AUTO_TEST) {
			if (currentPower > -50) {
				synchronized (this) {
					stopAutoTest(true);
				}
			}
		}
	}

	private void calViewRect() {
		WindowManager wm = getWindowManager();
		Display dis = wm.getDefaultDisplay();
		screenHeight = dis.getHeight();
		screenwidth = dis.getWidth();
		Log.d(TAG_D, "screenHeight ==== " + screenHeight);
		Log.d(TAG_D, "screenWidth ==== " + screenwidth);
		viewHeight = screenHeight / 3;
		viewWidth = screenwidth / 2;
		viewMargin = viewWidth / 20;
		Log.d(TAG_D, "viewMargin ====" + viewMargin);
		Log.d(TAG_D, "ViewHeight ==== " + viewHeight);
		Log.d(TAG_D, "ViewWidth  ==== " + viewWidth);
	}

	class AudioReaderService extends Thread {

		@Override
		public void run() {
			boolean done = false;
			short[] buffer = new short[readSize];
			if (recorder.getState() != AudioRecord.STATE_INITIALIZED) {
				Log.e(TAG_E, "AudioRecord init failed!");
				return;
			} else {
				Log.d(TAG_D, "AudioRecord init Success");
			}

			int bufferWhich = 0;
			int index = 0;
			recorder.startRecording();
			while (true) {
				buffer = bufferTmp[bufferWhich];
				int count = recorder.read(buffer, index, readSize);
				// Log.d(TAG_D, buffer.length + " " + index + " " + readSize);
				if (count < 0) {
					Log.e(TAG_E, "" + count);
					return;
				}
				int end = count + index;
				if (end >= readSize) {
					index = 0;
					bufferWhich = (bufferWhich + 1) % 2;
					done = true;
				} else {
					index = end;
				}

				if (done) {
					// Log.d(TAG_D, "done!!");
					Message msg = new Message();
					Bundle data = new Bundle();
					data.putShortArray(AUDIO_DATA_MSG, buffer);
					msg.setData(data);
					msg.what = MSG_WHAT;
					handler.sendMessage(msg);
				}
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		removeAll();
	}

	private void removeAll() {
		handler.removeMessages(MSG_WHAT);
		recorder.stop();
		recorder.release();
		Log.d(TAG_D, "removeAll");
	}

	private void initRecorder() {
		// TODO Auto-generated method stub
		int channleConfigMono = AudioFormat.CHANNEL_IN_MONO;
		int audioFormtPCM16 = AudioFormat.ENCODING_PCM_16BIT;
		int audioTotalSize = AudioRecord.getMinBufferSize(sampleRate,
				channleConfigMono, audioFormtPCM16) * 2;
		recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate,
				channleConfigMono, audioFormtPCM16, audioTotalSize / 1);
		int timeout = 200;
		try {
			while (timeout > 0
					&& recorder.getState() != AudioRecord.STATE_INITIALIZED) {
				Thread.sleep(50);
				timeout -= 50;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
