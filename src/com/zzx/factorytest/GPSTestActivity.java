package com.zzx.factorytest;

import android.graphics.Color;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.zzx.factorytest.manager.GPSManager;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class GPSTestActivity extends TestItemBaseActivity implements
        OnClickListener, YAxisValueFormatter, OnChartValueSelectedListener, GpsStatus.Listener, LocationListener, Handler.Callback, GpsStatus.NmeaListener, ValueFormatter {
    public static final int CRITICALITY = 1 << 13;
    public static final String TAG = "safsaf";
    public static final int DEFAULT_INIT = 12;
    private BarChart mBarChart;

    private final int AUTO_TEST_TIMEOUT = 15;// 锟皆讹拷锟斤拷锟皆筹拷时时锟斤拷
    private final int AUTO_TEST_MINI_SHOW_TIME = 5;// 锟皆讹拷锟斤拷锟皆筹拷时时锟斤拷
    private GPSManager gpsManager;
    private Button mSwitchBtn;
    private TextView mLogcat;
    private long mLogLeng;

    private Handler mHandler = new Handler(this);
    private ArrayList<String> xVals;
    private ArrayList<BarEntry> yVals1;
    private ScrollView mScrollView;


    private boolean isInit = false;
    private View mSwitchLayout;
    private TextView mFix;
    private HashSet<Integer> mFixSet;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.gps_test_layout);
        super.onCreate(savedInstanceState);


        initView();
        addListener();

//        gpsManager.stopGPS();
//        gpsManager.startBeiDou();

        gpsManager.startGPS();

        setBtnContent(false);
    }


    private void initView() {
        mBarChart = (BarChart) findViewById(R.id.chart);
        mSwitchBtn = (Button) findViewById(R.id.Switch_Btn);
        mSwitchLayout = findViewById(R.id.Switch_layout);
        mFix = (TextView) findViewById(R.id.fix);

        gpsManager = new GPSManager(this);
        mLogcat = (TextView) findViewById(R.id.logcat);
        mScrollView = (ScrollView) findViewById(R.id.ScrollView);

        initBarChart();

    }

    private void initBarChart() {
        mBarChart.setOnChartValueSelectedListener(this);

        mBarChart.setDrawBarShadow(false);
        mBarChart.setDrawValueAboveBar(true);
        mBarChart.setDrawGridBackground(false);

        mBarChart.setDescription("");


        mBarChart.setPinchZoom(false);
        mBarChart.animateY(2500);
//        BarLineChartTouchListener

        mBarChart.setScaleYEnabled(false);
        mBarChart.setScaleXEnabled(true);
        mBarChart.getLegend().setTextColor(Color.WHITE);

        mBarChart.getLegend().setForm(Legend.LegendForm.CIRCLE);

        XAxis xAxis = mBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setSpaceBetweenLabels(2);

        xAxis.setTextColor(Color.WHITE);


        YAxis leftAxis = mBarChart.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(this);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinValue(0.0f);
        leftAxis.setAxisMaxValue(60.0f);
        leftAxis.setTextColor(Color.WHITE);

        mBarChart.getAxisRight().setEnabled(false);

        Legend l = mBarChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);


        setData();

    }

    private void addListener() {
        mSwitchLayout.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "开始监听");

        if (!isInit) {
            isInit = true;
            gpsManager.getLocationManager().addGpsStatusListener(this);
            gpsManager.getLocationManager().addNmeaListener(this);

        }
        gpsManager.getLocationManager().requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0.0f, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gpsManager.getLocationManager().removeUpdates(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gpsManager.stopGPS();
        gpsManager.stopBeiDou();
        gpsManager.getLocationManager().removeGpsStatusListener(this);
        gpsManager.getLocationManager().removeUpdates(this);
        gpsManager.getLocationManager().removeNmeaListener(this);
        mBarChart.setFixSet(null);
        mFixSet.clear();
        mFixSet = null;
        Log.d(TAG, "取消监听");
    }

    private void setBtnContent(boolean isGPS) {
        if (isGPS) {
            mSwitchBtn.setText(getResources().getString(R.string.GPS_btn));
            mSwitchLayout.setTag(GPSManager.GPS);
        } else {
            mSwitchBtn.setText(getResources().getString(R.string.beidou_btn));
            mSwitchLayout.setTag(GPSManager.BEIDOU);
        }


    }


    @Override
    public void executeAutoTest() {
        super.startAutoTest(AUTO_TEST_TIMEOUT, AUTO_TEST_MINI_SHOW_TIME);

    }

    @Override
    public void onClick(View v) {


//        xVals.clear();
//        yVals1.clear();
//        for (int i = 0; i < DEFAULT_INIT; i++) {
//            xVals.add("");
//        }
//
//        for (int i = 0; i < DEFAULT_INIT; i++) {
//            yVals1.add(new BarEntry(0, i));
//        }
//        mBarChart.notifyDataSetChanged();
//        mBarChart.invalidate();
        mFixSet.clear();
        mHandler.sendEmptyMessage(1);
        String tag = (String) v.getTag();
        if (GPSManager.GPS.equals(tag)) {
            gpsManager.stopGPS();
            gpsManager.startBeiDou();
            gpsManager.startGPS();
            setBtnContent(false);
        } else if (GPSManager.BEIDOU.equals(tag)) {
            gpsManager.stopGPS();
            gpsManager.stopBeiDou();
            gpsManager.startGPS();
            setBtnContent(true);
        }


//        xVals.clear();
//        yVals1.clear();
//        Random randrom = new Random();
//
//        for (int i = 0; i < 20; i++) {
//
//            yVals1.add(new BarEntry(randrom.nextInt(60), i));
//            xVals.add(randrom.nextInt(300) + "");
//        }
//
//        mBarChart.notifyDataSetChanged();
//        mBarChart.invalidate();


//        mHandler.sendMessage(mHandler.obtainMessage(1, 0, 0));
//
//        int offset = mLogcat.getMeasuredHeight() - mScrollView.getMeasuredHeight();
//        if (offset > 0) {
//            mScrollView.scrollTo(0, offset);
//        }

    }

    private void setData() {
        xVals = new ArrayList<String>();
        yVals1 = new ArrayList<BarEntry>();
        mFixSet = new HashSet<Integer>();

        mBarChart.setFixSet(mFixSet);
        for (int i = 0; i < DEFAULT_INIT; i++) {
            xVals.add("");
        }

        for (int i = 0; i < DEFAULT_INIT; i++) {
            yVals1.add(new BarEntry(0, i));
        }

        BarDataSet set1 = new BarDataSet(yVals1, "SNR");

        set1.setValueTextColor(Color.WHITE);
        set1.setValueFormatter(this);

        set1.setColors(new int[]{
                Color.rgb(254, 0, 0),
                Color.rgb(255, 128, 0),
                Color.rgb(255, 255, 0),
                Color.rgb(217, 255, 0),
                Color.rgb(0, 255, 0)
        });

        set1.setBarSpacePercent(35f);

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);
        data.setValueTextSize(10f);

        mBarChart.setData(data);

    }

    @Override
    public String getFormattedValue(float value, YAxis yAxis) {
        return value + "";
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void onGpsStatusChanged(int event) {
        String message = "";
        switch (event) {
            case GpsStatus.GPS_EVENT_STARTED:
                message = "定位开始\n";
                break;
            case GpsStatus.GPS_EVENT_STOPPED:
                message = "定位结束\n";
                mFixSet.clear();
                break;
            case GpsStatus.GPS_EVENT_FIRST_FIX:
                message = "第一次定位\n";
                break;
            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                message = "\n";

                GpsStatus gpsStatus = gpsManager.getLocationManager().getGpsStatus(null);

                int maxSatellites = gpsStatus.getMaxSatellites();
                Iterator<GpsSatellite> iters = gpsStatus.getSatellites().iterator();
                int count = 0;
                xVals.clear();
                yVals1.clear();
                while (iters.hasNext() && count <= maxSatellites) {

                    GpsSatellite s = iters.next();
                    xVals.add(s.getPrn() + "");
                    yVals1.add(new BarEntry(s.getSnr(), count));
                    count++;
                }
                mFix.setText("Fix:" + count);
                mBarChart.notifyDataSetChanged();
                mBarChart.invalidate();
                break;
        }

        mHandler.sendMessage(mHandler.obtainMessage(0, message));
        Log.d(TAG, "message:" + message);
    }

    @Override
    public void onLocationChanged(Location location) {


        String message = "精度:" + location.getAccuracy() + ",经度:" + location.getLongitude() + ",纬度:" + location.getLatitude() + ",高度:" + location.getAltitude() + ",速度:" + location.getSpeed();
        message += "\n";
        mHandler.sendMessage(mHandler.obtainMessage(0, message));


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        String message = "状态改变\n";
        mHandler.sendMessage(mHandler.obtainMessage(0, message));

    }

    @Override
    public void onProviderEnabled(String provider) {
        String message = "启动定位\n";
        mHandler.sendMessage(mHandler.obtainMessage(0, message));
    }

    @Override
    public void onProviderDisabled(String provider) {
        String message = "取消定位\n";
        mHandler.sendMessage(mHandler.obtainMessage(1, message));
    }

    @Override
    public void onNmeaReceived(long timestamp, String nmea) {


        String message = "NMEA:" + nmea;
        getFixNumber(nmea, "$GPGSA");
        getFixNumber(nmea, "$BDGSA");
        mHandler.sendMessage(mHandler.obtainMessage(0, message));
//        Log.d(TAG, "NMEA:" + message);
    }

    private int[] getFixNumber(String message, String lab) {
        if (message.contains(lab)) {

            String substring1 = message.substring(11, message.lastIndexOf(","));
            String substring2 = substring1.substring(0, substring1.lastIndexOf(","));
            String substring = substring2.substring(0, substring2.lastIndexOf(","));
            String[] split = substring.split(",");
            for (int i = 0; i < split.length; i++) {
                String s = split[i];
                if (null != s && !"".equals(s)) {
                    mFixSet.add(Integer.valueOf(s));
                }
            }
        }
        return null;
    }

    @Override
    public boolean handleMessage(Message msg) {

        switch (msg.what) {
            case 0:


                boolean isClean = false;
                if (mLogLeng >= CRITICALITY) {
                    new MyAsyncTask().execute(mLogcat.getText().toString());
                    mLogLeng = 0;
                    isClean = true;
                }

                String log = (String) msg.obj;
                mLogLeng += log.length();
                if (isClean) {
                    mLogcat.setText(log);
                } else {
                    mLogcat.append(log);
                }
                mHandler.sendMessage(mHandler.obtainMessage(2));

//                Log.d(TAG, "mLogLeng:" + mLogLeng + ",CRITICALITY:" + CRITICALITY);
                break;
            case 1:

                mFix.setText("Fix:0");
                xVals.clear();
                yVals1.clear();

                for (int i = 0; i < DEFAULT_INIT; i++) {
                    xVals.add("");
                }

                for (int i = 0; i < DEFAULT_INIT; i++) {
                    yVals1.add(new BarEntry(0, i));
                }
                mBarChart.notifyDataSetChanged();
                mBarChart.invalidate();
                break;
            case 2:


                int offset = mLogcat.getMeasuredHeight() - mScrollView.getMeasuredHeight();
                if (offset > 0) {
                    mScrollView.scrollTo(0, offset);
                }


                break;
        }
        return true;
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return value == 0 ? String.valueOf(0) : String.valueOf(value);
    }

    private class MyAsyncTask extends AsyncTask<String, Void, Void> {


        @Override
        protected Void doInBackground(String... params) {
            String log = params[0];
            File logFile;
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                logFile = new File(getExternalFilesDir("log"), "log.txt");

            } else {
                logFile = new File(getFilesDir(), "log" + File.separator + "log.txt");

            }

            try {

                RandomAccessFile accessFile = new RandomAccessFile(logFile, "rw");

                accessFile.seek(accessFile.length());
                accessFile.write(log.getBytes("UTF-8"));


            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }


}
