package com.zzx.factorytest;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Window;
import android.view.WindowManager;

public class VibrateTestActivity extends TestItemBaseActivity {

    private boolean isVibrating = false;
    private Vibrator mVibrator; //

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.vibrate_test_layout);
        super.onCreate(savedInstanceState);
        mVibrator = (Vibrator) getApplication().getSystemService(
                Context.VIBRATOR_SERVICE);
        if (mVibrator == null) {
            mVibrator = (Vibrator) getApplication().getSystemService(
                    Context.VIBRATOR_SERVICE);
        }
        mVibrator.vibrate(new long[]{100, 1000, 1000, 1000}, 1);
        isVibrating = true;


    }

    @Override
    protected void onDestroy() {
        if (isVibrating) {
            mVibrator.cancel();
            isVibrating = false;
        }
        super.onDestroy();
    }

}
