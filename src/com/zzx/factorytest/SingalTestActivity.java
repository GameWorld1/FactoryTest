package com.zzx.factorytest;

import com.zzx.factorytest.view.ImageTextButton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;

public class SingalTestActivity extends Activity implements OnClickListener {

    private ImageTextButton itb_magnetic;//
    private ImageTextButton itb_gravity;//
    private ImageTextButton itb_keys;//
    private ImageTextButton itb_headphone;//
    private ImageTextButton itb_microphone;//
    private ImageTextButton itb_multi_touch;//
    private ImageTextButton itb_vibrate;//
    private ImageTextButton itb_radio;//
    private ImageTextButton itb_light;//
    private ImageTextButton itb_camera;
    private ImageTextButton itb_sim;//
    private ImageTextButton itb_sdcard;//
    private ImageTextButton itb_bluetooch;//
    private ImageTextButton itb_GPS;//
    private ImageTextButton itb_wifi;//


    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singal_test_layout);

        itb_magnetic = (ImageTextButton) findViewById(R.id.itb_magnetic);
        itb_magnetic.setOnClickListener(this);
        //itb_magnetic.setResult(false);
        itb_gravity = (ImageTextButton) findViewById(R.id.itb_gravity);
        itb_gravity.setOnClickListener(this);
        //itb_gravity.setResult(true);
        itb_keys = (ImageTextButton) findViewById(R.id.itb_keys);
        itb_keys.setOnClickListener(this);

        itb_headphone = (ImageTextButton) findViewById(R.id.itb_headphone);
        itb_headphone.setOnClickListener(this);

        itb_microphone = (ImageTextButton) findViewById(R.id.itb_microphone);
        itb_microphone.setOnClickListener(this);

        itb_multi_touch = (ImageTextButton) findViewById(R.id.itb_multi_touch);
        itb_multi_touch.setOnClickListener(this);

        itb_vibrate = (ImageTextButton) findViewById(R.id.itb_vibrate);
        itb_vibrate.setOnClickListener(this);

        itb_radio = (ImageTextButton) findViewById(R.id.itb_radio);
        itb_radio.setOnClickListener(this);

        itb_light = (ImageTextButton) findViewById(R.id.itb_light);
        itb_light.setOnClickListener(this);

        itb_camera = (ImageTextButton) findViewById(R.id.itb_camera);
        itb_camera.setOnClickListener(this);

        itb_sim = (ImageTextButton) findViewById(R.id.itb_sim);
        itb_sim.setOnClickListener(this);

        itb_sdcard = (ImageTextButton) findViewById(R.id.itb_sdcard);
        itb_sdcard.setOnClickListener(this);

        itb_bluetooch = (ImageTextButton) findViewById(R.id.itb_bluetooch);
        itb_bluetooch.setOnClickListener(this);

        itb_GPS = (ImageTextButton) findViewById(R.id.itb_GPS);
        itb_GPS.setOnClickListener(this);

        itb_wifi = (ImageTextButton) findViewById(R.id.itb_wifi);
        itb_wifi.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == itb_magnetic.getId()) {
            startActivity(new Intent(this, MagneticFieldActivity.class));
        } else if (id == itb_gravity.getId()) {
            startActivity(new Intent(this, GravityActivity.class));
        } else if (id == itb_keys.getId()) {
            startActivity(new Intent(this, KeysActivity.class));
        } else if (id == itb_headphone.getId()) {
            startActivity(new Intent(this, HeadPhoneActivity.class));
        } else if (id == itb_microphone.getId()) {
            startActivity(new Intent(this, MICPhoneTestActivity.class));
        } else if (id == itb_multi_touch.getId()) {
            startActivity(new Intent(this, MultiTouchActivity.class));
        } else if (id == itb_vibrate.getId()) {
            startActivity(new Intent(this, VibrateTestActivity.class));
        } else if (id == itb_radio.getId()) {
            startActivity(new Intent(this, FMTestActivity.class));
        } else if (id == itb_light.getId()) {
            startActivity(new Intent(this, LightActivity.class));
        } else if (id == itb_camera.getId()) {
            startActivity(new Intent(this, CameraActivity.class));
        } else if (id == itb_sim.getId()) {
            startActivity(new Intent(this, SIMActivity.class));
        } else if (id == itb_sdcard.getId()) {
            startActivity(new Intent(this, SdcardActivity.class));
        } else if (id == itb_bluetooch.getId()) {
            startActivity(new Intent(this, BluetoochTestActivity.class));
        } else if (id == itb_GPS.getId()) {
            startActivity(new Intent(this, GPSTestActivity.class));
        } else if (id == itb_wifi.getId()) {
            startActivity(new Intent(this, WifiTestActivity.class));
        }
    }

}
