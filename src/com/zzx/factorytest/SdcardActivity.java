package com.zzx.factorytest;

import java.io.File;

import android.os.Bundle;
import android.os.StatFs;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class SdcardActivity extends TestItemBaseActivity {

    private TextView txtSdAviable;
    private TextView txtSdSize;
    private TextView txtSd2Size;

    private final int AUTO_TEST_TIMEOUT = 3;//
    private final float AUTO_TEST_RANGE = 0f;//
    private final int AUTO_TEST_MINI_SHOW_TIME = 2;//

    // private TextView txtCheckSum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.sdcard_layout);
        super.onCreate(savedInstanceState);

        txtSdAviable = (TextView) findViewById(R.id.txt_sd_available);
        txtSdSize = (TextView) findViewById(R.id.txt_sd_capacity);
        txtSd2Size = (TextView) findViewById(R.id.txt_sd2_capacity);
        // txtCheckSum = (TextView) findViewById(R.id.txt_checkSum);

    }

    @Override
    void executeAutoTest() {
        super.startAutoTest(AUTO_TEST_TIMEOUT, AUTO_TEST_MINI_SHOW_TIME);

    }

    @Override
    protected void onResume() {
        super.onResume();

        checkSDState();
    }

    private void checkSDState() {

        String sd = "/mnt/sdcard/";
        String sd2 = "/mnt/sdcard2/";

        long momorySDCard1Size = 0;
        long momorySDCard2Size = 0;

        File sdfile = new File(sd);
        if (sdfile.exists()) {
            momorySDCard1Size = getMemorySize(sd);
            txtSdSize.setText((momorySDCard1Size) / (1024 * 1024) + " MB");
        } else {
            txtSdSize.setText("δ֪");
        }
        File sd2file = new File(sd2);
        if (sd2file.exists()) {
            momorySDCard2Size = getMemorySize(sd2);
            txtSd2Size.setText((momorySDCard2Size) / (1024 * 1024) + " MB");

            txtSdAviable
                    .setText((getFreeMemorySize(sd2) + getFreeMemorySize(sd))
                            / (1024 * 1024) + " MB");
        } else {

            txtSd2Size.setText("δ֪");

            txtSdAviable.setText((momorySDCard1Size) / (1024 * 1024) + " MB");
        }

        if ((momorySDCard1Size > AUTO_TEST_RANGE)
                && (momorySDCard2Size > AUTO_TEST_RANGE)) {
            synchronized (this) {

                stopAutoTest(true);
            }
        } else {
            synchronized (this) {

                stopAutoTest(false);
            }
        }
    }

    public static long getMemorySize(String sdcardPath) {

        StatFs stat = new StatFs(sdcardPath);
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getBlockCount();
        return availableBlocks * blockSize;

    }

    public static long getFreeMemorySize(String sdcardPath) {
        StatFs stat = new StatFs(sdcardPath);
        long blockSize = stat.getBlockSize();
        long freeblocks = stat.getFreeBlocks();
        return freeblocks * blockSize;
    }

}
