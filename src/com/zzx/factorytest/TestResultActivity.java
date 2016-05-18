package com.zzx.factorytest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;

import com.zzx.factorytest.manager.FactoryTestManager;
import com.zzx.factorytest.view.TestItemGridViewAdapter;

public class TestResultActivity extends Activity implements OnClickListener {

	private ListView listView;
	private TestItemGridViewAdapter adapter;
	private FactoryTestManager factoryTestManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams. FLAG_FULLSCREEN);
		setContentView(R.layout.test_result_list);

		listView = (ListView) findViewById(R.id.lv_testResult);
		adapter=new TestItemGridViewAdapter(this);
		factoryTestManager=FactoryTestManager.getInstance(this);
		
		Button button=new Button(this);
		button.setText("");
		button.setOnClickListener(this);
		listView.addFooterView(button);
		
		listView.setAdapter(adapter);
		super.onCreate(savedInstanceState);
	}
	@Override
	public void onClick(View v) {
		startActivity(new Intent(this,factoryTestManager.getNextActivityClass(null)));
	}
}
