package com.zzx.factorytest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;




public class TpCalActivity extends TestItemBaseActivity {

	private String device_node="/sys/module/pixcir168";
	private int length;
	private String TAG="TpCalActivity";
	private byte[] buffer;
	private byte[] buffer_cal_init = {0x00,0x00,0x00};
	private byte[] buffer_cal_over = {0x00,0x07,(byte) 0xC0};
	private byte[] buffer_cal_ing = {0x00,0x00,(byte) 0x80};
	
	private TextView tv_cal;
	private Button btn_cal;
	private int calstat = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams. FLAG_FULLSCREEN);
		setContentView(R.layout.tpcal_layout);
		
		tv_cal = (TextView)findViewById(R.id.txt_tpstate);
		btn_cal = (Button)findViewById(R.id.btn_cal);
		btn_cal.setOnClickListener(new ButtonListener());
		
		tp_check();
		super.onCreate(savedInstanceState);
	}
	
    Handler updateHandler = new Handler()
    {

		@Override
		public void handleMessage(Message msg) {
			tp_check();
			super.handleMessage(msg);
		}
    	
    	
    };
	void tp_check()
	{
		
		File file=new File(device_node);	
		try {
			FileInputStream fi = new FileInputStream(file);
			buffer = new byte[(int) 3]; 
			try {
				fi.read(buffer, 0, 3);
			
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}  

			if (buffer[0]==buffer_cal_init[0]&&buffer[1]==buffer_cal_init[1]&&buffer[2]==buffer_cal_init[2]){	
				calstat = 0;
				tv_cal.setText("");
				Log.d(TAG,"");
			}else if (buffer[0]==buffer_cal_over[0]&&buffer[1]==buffer_cal_over[1]&&buffer[2]==buffer_cal_over[2]){
				calstat = 1;
				tv_cal.setText("");
			}else if (buffer[0]==buffer_cal_ing[0]&&buffer[1]==buffer_cal_ing[1]&&buffer[2]==buffer_cal_ing[2]){
				calstat = 2;
				tv_cal.setText("");
				
				Message msg = updateHandler.obtainMessage();
				updateHandler.sendMessageDelayed(msg, 1000);
			}
	/*	
		if(buffer.equals(buffer_cal_init)){
			tv_cal.setText("û�г�ʼ��");
			
		}else if (buffer.equals(buffer_cal_over)){
			tv_cal.setText("У׼���");
			
		}
		*/		
	}
	void tp_cal() 
	{
		File file=new File(device_node);
		
		try {
			FileOutputStream fo = new FileOutputStream(file);
			try {
				fo.write(0x00);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	class ButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			
			switch (v.getId()) {
			case R.id.btn_cal:
				if(calstat!=2){
				tp_cal();
				Message msg = updateHandler.obtainMessage();
				updateHandler.sendMessageDelayed(msg, 1000);
				}
				break;	
			default:
				break;
			}
			
		}	
	}  
	
}
