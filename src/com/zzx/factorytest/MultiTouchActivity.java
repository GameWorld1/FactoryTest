package com.zzx.factorytest;

import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.zzx.factorytest.view.PointerLocationView;

public class MultiTouchActivity extends TestItemBaseActivity{

	private boolean isPointerLocation = true;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams. FLAG_FULLSCREEN);
        View locationView = new PointerLocationView(this);
    
        setContentView(R.layout.touch_test_layout);
        super.onCreate(savedInstanceState);
        Settings.System.putInt(getContentResolver(),
                "pointer_location", isPointerLocation ? 1 : 0);
     // Make the screen full bright for this activity.
      /*  WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness = 1.0f;
        getWindow().setAttributes(lp);*/
    }
    
    
    @Override
	protected void onPause() {
    	isPointerLocation = false;
   	 Settings.System.putInt(getContentResolver(),
                "pointer_location", isPointerLocation ? 1 : 0);
		super.onPause();
	}



	@Override
    protected void onDestroy() {
    	isPointerLocation = false;
    	 Settings.System.putInt(getContentResolver(),
                 "pointer_location", isPointerLocation ? 1 : 0);
    	super.onDestroy();
    }

	@Override
	public void onBackPressed() {
		isPointerLocation = false;
		 Settings.System.putInt(getContentResolver(),
                 "pointer_location", isPointerLocation ? 1 : 0);
		super.onBackPressed();
	}



    
    
}