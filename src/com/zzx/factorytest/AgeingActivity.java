package com.zzx.factorytest;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;

/**
 * -----------�ϻ�����
 * 
 * @author bin
 * 
 */
public class AgeingActivity extends Activity {

	private VideoView videoView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams. FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		setContentView(R.layout.ageing_layout);
		super.onCreate(savedInstanceState);

		videoView = (VideoView) this.findViewById(R.id.videoView);
//		MediaController mc = new MediaController(this);
//		videoView.setMediaController(mc);
		//Uri uri = Uri.parse("/mnt/sdcard2/video.mp4");
		//Uri uri = Uri.parse("/mnt/sdcard2/video.mp4");
		 //videoView.setVideoURI(uri);
		 videoView.setVideoPath("android.resource://" + getPackageName() +"/"+R.raw.video);
		
		 videoView.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				
				mp.start();
				
			}
			
		});
//		 videoView.setOnErrorListener(new OnErrorListener() {
//			
//			@Override
//			public boolean onError(MediaPlayer mp, int what, int extra) {
//				// TODO Auto-generated method stub
//				return true;
//			}
//		});
		videoView.requestFocus();
		videoView.start();
		// changeImage();
		this.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.water_logo));
	}

	public void btnReturn(View view){
		this.finish();
		
	}

}
