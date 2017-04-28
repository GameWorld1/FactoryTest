package com.zzx.factorytest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.zzx.factorytest.view.Preview;

import java.util.List;

public class CameraActivity extends TestItemBaseActivity implements
        OnCheckedChangeListener {

    // private TextView txtCameraResult;
    private Preview mPreview;
    Camera mCamera;
    int mNumberOfCameras;
    int mCurrentCamera; // Camera ID currently chosen
    int mCameraCurrentlyLocked; // Camera ID that's actually acquired
    private CheckBox cb_openFlash;
    boolean isFlashOpen = false;
    ;

    // The first rear facing camera
    int mDefaultCameraId;
    private Button btnCameraSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.camera_layout);
        // txtCameraResult = (TextView) findViewById(R.id.txtCameraResult);
        super.onCreate(savedInstanceState);
        // openCamera();

        // Create a container that will hold a SurfaceView for camera previews
        mPreview = (Preview) findViewById(R.id.camera_preview);
        cb_openFlash = (CheckBox) findViewById(R.id.cb_openFlash);
        btnCameraSwitch = (Button) findViewById(R.id.btn_switch);

        View view = findViewById(R.id.btnAF);
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (null != mCamera) {
                    Camera.Parameters parameter = mCamera.getParameters();
                    // parameter.getSupportedPreviewSizes();
                    parameter.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                    parameter.set("focus-meter", "spot");
                    mCamera.setParameters(parameter);
                    mCamera.autoFocus(mAutoFocCalback);
                }

            }
        });

        cb_openFlash.setChecked(isFlashOpen);
        cb_openFlash.setOnCheckedChangeListener(this);
        // Find the total number of cameras available
        mNumberOfCameras = Camera.getNumberOfCameras();

        // Find the ID of the rear-facing ("default") camera
        CameraInfo cameraInfo = new CameraInfo();
        for (int i = 0; i < mNumberOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
                mCurrentCamera = mDefaultCameraId = i;
            }
        }
        if (mNumberOfCameras < 2) {
            btnCameraSwitch.setEnabled(false);
        }

    }

    // public void btn_flash_light(View view) {
    // // ��
    // Parameters parameter = mCamera.getParameters();
    // if (parameter.getFlashMode() == Parameters.FLASH_MODE_TORCH) {
    //
    // parameter.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
    //
    // ((Button) view).setText("�������");
    // //mCamera.stopPreview();
    // } else {
    // // parameter.setFlashMode(Parameters.FLASH_MODE_OFF);
    // parameter.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
    // ((Button) view).setText("�ر������");
    //
    // }
    // mCamera.setParameters(parameter);
    // mCamera.startPreview();
    //
    // }

    private final AutoFocusCallback mAutoFocCalback = new AutoFocusCallback();

    private final class AutoFocusCallback implements
            android.hardware.Camera.AutoFocusCallback {
        public void onAutoFocus(boolean focused, android.hardware.Camera camera) {

        }
    }

    public void btn_switch(View view) {
        if (mCamera != null) {
            mCamera.stopPreview();
            mPreview.setCamera(null);
            mCamera.release();
            mCamera = null;
        }

        // Acquire the next camera and request Preview to reconfigure
        // parameters.
        mCurrentCamera = (mCameraCurrentlyLocked + 1) % mNumberOfCameras;
        CameraInfo cameraInfo = new CameraInfo();
        Camera.getCameraInfo(mCurrentCamera, cameraInfo);


        mCamera = Camera.open(mCurrentCamera);
//        if ("T80".equals(MainActivity.PLATFORM)) {
//
//            if (cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT) {
//                mCamera.setDisplayOrientation(180);
//            }
//
//        }
        Parameters parameter = mCamera.getParameters();
        if (isFlashOpen) {
            parameter.setFlashMode(Parameters.FLASH_MODE_TORCH);
        } else {
            parameter.setFlashMode(Parameters.FLASH_MODE_OFF);
        }
        //
        mCamera.setParameters(parameter);
        mCameraCurrentlyLocked = mCurrentCamera;
        mPreview.switchCamera(mCamera);
        mCamera.startPreview();
    }

    @Override
    protected void onResume() {
        try {

            mCamera = Camera.open(mCurrentCamera);
            Parameters parameter = mCamera.getParameters();
            if (isFlashOpen) {
                parameter.setFlashMode(Parameters.FLASH_MODE_TORCH);
            } else {
                parameter.setFlashMode(Parameters.FLASH_MODE_OFF);
            }
            //
            mCamera.setParameters(parameter);
            mCameraCurrentlyLocked = mCurrentCamera;
            mPreview.setCamera(mCamera);
        } catch (Exception e) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("打开摄像头出错");
            dialog.show();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (mCamera != null) {
            mPreview.setCamera(null);
            mCamera.release();
            mCamera = null;
        }
        super.onPause();
    }

    public static boolean checkAppInstalled(Intent intent, Context context) {
        PackageManager manager = context.getPackageManager();

        List<ResolveInfo> list = manager.queryIntentActivities(intent, 0);
        if (list == null || list.size() < 1)
            return false;
        return true;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.isPressed()) {
            isFlashOpen = isChecked;
            Parameters parameters = mCamera.getParameters();
            String mode = "";
            if (isFlashOpen) {
                mode = Parameters.FLASH_MODE_TORCH;

            } else {
                mode = Parameters.FLASH_MODE_OFF;
            }
            parameters.setFlashMode(mode);
            mCamera.setParameters(parameters);
        }

    }

}
