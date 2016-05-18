package com.zzx.factorytest;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.zzx.factorytest.utils.CommonUtils;

public class FMTestActivity extends TestItemBaseActivity {

	private TextView txtFmResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams. FLAG_FULLSCREEN);
		setContentView(R.layout.fm_layout);
		txtFmResult = (TextView) findViewById(R.id.txtFmResult);
		super.onCreate(savedInstanceState);
		openFM();

	}

	public void btnFMTest(View view) {
		openFM();
	}

	private void openFM() {
		Intent fmIntent = new Intent();
		fmIntent.putExtra("freq", "97.1");
		fmIntent.setComponent(new ComponentName("com.mediatek.FMRadio",
				"com.mediatek.FMRadio.FMRadioActivity"));
		if (CommonUtils.checkAppInstalled(fmIntent, this)) {
			startActivityForResult(fmIntent, 0);
		}else {
			Toast.makeText(this, "FMӦ��δ��װ", 0).show();
			onSelectResult(false);
			finish();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		txtFmResult.setVisibility(View.VISIBLE);
		super.onActivityResult(requestCode, resultCode, data);
	}

}
