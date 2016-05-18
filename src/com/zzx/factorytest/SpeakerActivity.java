package com.zzx.factorytest;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class SpeakerActivity extends TestItemBaseActivity {

	private MediaPlayer mMediaPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams. FLAG_FULLSCREEN);
		setContentView(R.layout.speaker_layout);

		try {
			mMediaPlayer = MediaPlayer.create(this, R.raw.walle);
			mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mMediaPlayer.setLooping(true);
			mMediaPlayer.stop();
			mMediaPlayer.prepare();
			mMediaPlayer.start();
		} catch (Exception e) {

			e.printStackTrace();  
		}
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void onStop() {

		mMediaPlayer.stop();
		super.onStop();
	}

	public void volumn2Max(View view) {
		AudioManager audioManager=(AudioManager) getSystemService(Context.AUDIO_SERVICE);
		int max=audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, max, 0);
		Toast.makeText(this, "�ɹ�", 0).show();
	}


}
