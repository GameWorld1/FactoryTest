package com.zzx.factorytest;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class KeysActivity extends TestItemBaseActivity {

    private TextView txtVolumnAdd;
    private TextView txtVolumnDesc;
    private TextView txtOnestone;
    private TextView txtHome;
    private TextView txtNavi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.keys_layout);
        super.onCreate(savedInstanceState);

        txtVolumnAdd = (TextView) findViewById(R.id.txt_volumn_add);
        txtVolumnDesc = (TextView) findViewById(R.id.txt_volumn_des);
//        txtOnestone = (TextView) findViewById(R.id.txt_onestone);
//        txtHome = (TextView) findViewById(R.id.txt_home);
//        txtNavi = (TextView) findViewById(R.id.txt_navi);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_A || keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            txtVolumnAdd.setText("正确");
        } else if (keyCode == KeyEvent.KEYCODE_B || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            txtVolumnDesc.setText("正确");
        }
//        else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP) {
//            txtOnestone.setText("正确ȷ");
//        } else if (keyCode == KeyEvent.KEYCODE_SOFT_RIGHT) {
//            txtHome.setText("正确");
//        } else if (keyCode == KeyEvent.KEYCODE_SEARCH) {
//            txtNavi.setText("正确");
//        }
        return super.onKeyDown(keyCode, event);
    }


}
