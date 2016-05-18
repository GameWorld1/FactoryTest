package com.zzx.factorytest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class GPIOActivity extends TestItemBaseActivity {

	private TextView tv_message;
	private final int AUTO_TEST_TIMEOUT = 3;
	private final int AUTO_TEST_MINI_SHOW_TIME = 2;

	private String device_node="/sys/devices/platform/gpio_test/gpio_test";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams. FLAG_FULLSCREEN);
		setContentView(R.layout.gpio_layout);
		tv_message = (TextView) findViewById(R.id.tv_message);
		
		
		File node=new File(device_node);
		try {
			
			BufferedReader reader=new BufferedReader(new FileReader(node));
			String result=reader.readLine();
			if(result==null){
				tv_message.setTextSize(20);
				tv_message.setTextColor(Color.RED);
				tv_message.setText("δ���ҵ�gpio_test���");
				stopAutoTest(false);
			}else{
				int data=result==null?-1:Integer.parseInt(result);
				if (data == -1) {
					tv_message.setTextColor(Color.RED);
					tv_message.setText("GPIO�쳣");
					stopAutoTest(false);
				} else {
					tv_message.setTextColor(Color.GREEN);
					tv_message.setText("GPIO����");
					stopAutoTest(true);
				}
				
			}
			
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onCreate(savedInstanceState);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}



	@Override
	void executeAutoTest() {
		super.startAutoTest(AUTO_TEST_TIMEOUT, AUTO_TEST_MINI_SHOW_TIME);

	}

}
