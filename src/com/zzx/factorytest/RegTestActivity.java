package com.zzx.factorytest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Method;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.zzx.factorytest.manager.FactoryTestManager;

import dalvik.system.DexFile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View.MeasureSpec;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RegTestActivity extends TestItemBaseActivity {
    private static String IMEI_serial = "";
    private static String EMMC_ID = "";
    private static String MAC = "";
    private static String MODEL = "";
    private static String device_node = "/sys/block/mmcblk0/device/cid";
    private FactoryTestManager factoryTestManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.reg_layout);

        super.onCreate(savedInstanceState);

        new Handler().postDelayed(mRunnable, 1000);
        factoryTestManager = FactoryTestManager.getInstance(this);

    }

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {

            WindowManager wm = (WindowManager) RegTestActivity.this
                    .getSystemService(Context.WINDOW_SERVICE);

            int height = wm.getDefaultDisplay().getHeight();

            IMEI_serial = ((TelephonyManager) RegTestActivity.this
                    .getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();

            try {
                if (null != IMEI_serial) {
                    if (!IMEI_serial.isEmpty()) {

                        Bitmap barcodeBitmap = creatBarcode(
                                RegTestActivity.this, IMEI_serial, height, 200);
                        Bitmap codeBitmap = creatCodeBitmap(IMEI_serial, height + 40,
                                50, RegTestActivity.this);
                        Bitmap resultBitmap = mixtureBitmap(barcodeBitmap,
                                codeBitmap);
                        Bitmap bmOk = adjustPhotoRotation(resultBitmap, 270);

                        if (null != bmOk) {
                            ((ImageView) RegTestActivity.this
                                    .findViewById(R.id.ivImei2))
                                    .setImageBitmap(bmOk);
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                BufferedReader reader = new BufferedReader(new FileReader(
                        device_node));
                EMMC_ID = reader.readLine();
                MmcCid mmccid = new MmcCid();
                mmccid.parse(EMMC_ID);
                if (null != EMMC_ID) {
                    if (!EMMC_ID.isEmpty()) {
                        Bitmap barcodeBitmap = creatBarcode(
                                RegTestActivity.this, EMMC_ID, height, 200);
                        Bitmap codeBitmap = creatCodeBitmap(EMMC_ID,
                                height + 40, 50, RegTestActivity.this);
                        Bitmap resultBitmap = mixtureBitmap(barcodeBitmap,
                                codeBitmap);
                        Bitmap bmOk = adjustPhotoRotation(resultBitmap, 270);
                        if (null != bmOk) {
                            ((ImageView) findViewById(R.id.ivEmmc2))
                                    .setImageBitmap(bmOk);
                        }
                    }
                }
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            WifiManager wifi = (WifiManager) RegTestActivity.this
                    .getSystemService(Context.WIFI_SERVICE);

            WifiInfo info = wifi.getConnectionInfo();
            MAC = info.getMacAddress();

            try {
                if (null != MAC) {
                    if (!MAC.isEmpty()) {

                        Bitmap barcodeBitmap = creatBarcode(
                                RegTestActivity.this, MAC, height, 200);
                        Bitmap codeBitmap = creatCodeBitmap(MAC,
                                height + 40, 50, RegTestActivity.this);
                        Bitmap resultBitmap = mixtureBitmap(barcodeBitmap,
                                codeBitmap);
                        Bitmap bmOk = adjustPhotoRotation(resultBitmap, 270);

                        if (null != bmOk) {
                            ((ImageView) RegTestActivity.this
                                    .findViewById(R.id.ivMac2))
                                    .setImageBitmap(bmOk);
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            MODEL = SystemPropertiesProxy.get(RegTestActivity.this,
                    "ro.build.display.id");



            try {
                if (null != MODEL) {
                    if (false == MODEL.isEmpty()) {

                        Bitmap barcodeBitmap = creatBarcode(
                                RegTestActivity.this, MODEL, height, 200);
                        Bitmap codeBitmap = creatCodeBitmap(MODEL, height + 40,
                                50, RegTestActivity.this);
                        Bitmap resultBitmap = mixtureBitmap(barcodeBitmap,
                                codeBitmap);
                        Bitmap bmOk = adjustPhotoRotation(resultBitmap, 270);

                        if (null != bmOk) {
                            ((ImageView) RegTestActivity.this
                                    .findViewById(R.id.ivModel2))
                                    .setImageBitmap(bmOk);
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    static Bitmap adjustPhotoRotation(Bitmap bm, int orientationDegree) {
        Matrix m = new Matrix();
        m.setRotate(orientationDegree, (float) bm.getWidth() / 2,
                (float) bm.getHeight() / 2);

        try {

            Bitmap bm1 = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                    bm.getHeight(), m, true);

            return bm1;
        } catch (OutOfMemoryError ex) {
        }

        return null;

    }

    public Bitmap creatBarcode(Context context, String contents,
                               int desiredWidth, int desiredHeight) {
        Bitmap ruseltBitmap = null;

        BarcodeFormat barcodeFormat = BarcodeFormat.CODE_128;

        ruseltBitmap = encodeAsBitmap(contents, barcodeFormat, desiredWidth,
                desiredHeight);

        return ruseltBitmap;
    }

    protected Bitmap encodeAsBitmap(String contents, BarcodeFormat format,
                                    int desiredWidth, int desiredHeight) {
        final int WHITE = 0xFFFFFFFF;
        final int BLACK = 0xFF000000;

        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result = null;
        try {
            result = writer.encode(contents, format, desiredWidth,
                    desiredHeight, null);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];

        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    protected Bitmap creatCodeBitmap(String contents, int width, int height,
                                     Context context) {
        TextView tv = new TextView(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(layoutParams);
        tv.setText(contents);
        tv.setHeight(height);
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        tv.setWidth(width);
        tv.setDrawingCacheEnabled(true);
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(16);
        tv.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        tv.layout(0, 0, tv.getMeasuredWidth(), tv.getMeasuredHeight());

        tv.buildDrawingCache();
        Bitmap bitmapCode = tv.getDrawingCache();
        return bitmapCode;
    }

    protected static Bitmap mixtureBitmap(Bitmap first, Bitmap second) {
        if (first == null || second == null) {
            return null;
        }

        Bitmap newBitmap = Bitmap.createBitmap(first.getWidth(),
                first.getHeight() + second.getHeight() + 10, Config.ARGB_8888);
        Canvas cv = new Canvas(newBitmap);
        cv.drawBitmap(first, 0, 0, null);
        cv.drawBitmap(second, (first.getWidth() - second.getWidth()) / 2,
                first.getHeight() + 10, null);
        cv.save(Canvas.ALL_SAVE_FLAG);
        cv.restore();

        return newBitmap;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        factoryTestManager.putResult(FactoryTestManager.ITEM_EMMCID, EMMC_ID == null ? "" : EMMC_ID);
        factoryTestManager.putResult(FactoryTestManager.MAC, MAC == null ? "" : MAC);
        factoryTestManager.putResult(FactoryTestManager.IMEI_serial, IMEI_serial == null ? "" : IMEI_serial);
        factoryTestManager.putResult(FactoryTestManager.MODEL, MODEL == null ? "" : MODEL);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public static class SystemPropertiesProxy {

        /**
         * This class cannot be instantiated
         */
        private SystemPropertiesProxy() {

        }

        /**
         * Get the value for the given key.
         *
         * @return an empty string if the key isn't found
         * @throws IllegalArgumentException if the key exceeds 32 characters
         */
        public static String get(Context context, String key)
                throws IllegalArgumentException {

            String ret = "";
            try {

                ClassLoader cl = context.getClassLoader();
                @SuppressWarnings("rawtypes")
                Class SystemProperties = cl
                        .loadClass("android.os.SystemProperties");

                // Parameters Types
                @SuppressWarnings("rawtypes")
                Class[] paramTypes = new Class[1];
                paramTypes[0] = String.class;

                Method get = SystemProperties.getMethod("get", paramTypes);

                // Parameters
                Object[] params = new Object[1];
                params[0] = new String(key);

                ret = (String) get.invoke(SystemProperties, params);

            } catch (IllegalArgumentException iAE) {
                throw iAE;
            } catch (Exception e) {
                ret = "";
            }

            return ret;

        }

        /**
         * Get the value for the given key.
         *
         * @return if the key isn't found, return def if it isn't null, or an
         * empty string otherwise
         * @throws IllegalArgumentException if the key exceeds 32 characters
         */
        public static String get(Context context, String key, String def)
                throws IllegalArgumentException {

            String ret = def;

            try {

                ClassLoader cl = context.getClassLoader();
                @SuppressWarnings("rawtypes")
                Class SystemProperties = cl
                        .loadClass("android.os.SystemProperties");

                // Parameters Types
                @SuppressWarnings("rawtypes")
                Class[] paramTypes = new Class[2];
                paramTypes[0] = String.class;
                paramTypes[1] = String.class;

                Method get = SystemProperties.getMethod("get", paramTypes);

                // Parameters
                Object[] params = new Object[2];
                params[0] = new String(key);
                params[1] = new String(def);

                ret = (String) get.invoke(SystemProperties, params);

            } catch (IllegalArgumentException iAE) {
                throw iAE;
            } catch (Exception e) {
                ret = def;

            }

            return ret;

        }

        /**
         * Get the value for the given key, and return as an integer.
         *
         * @param key the key to lookup
         * @param def a default value to return
         * @return the key parsed as an integer, or def if the key isn't found
         * or cannot be parsed
         * @throws IllegalArgumentException if the key exceeds 32 characters
         */
        public static Integer getInt(Context context, String key, int def)
                throws IllegalArgumentException {

            Integer ret = def;

            try {

                ClassLoader cl = context.getClassLoader();
                @SuppressWarnings("rawtypes")
                Class SystemProperties = cl
                        .loadClass("android.os.SystemProperties");

                // Parameters Types
                @SuppressWarnings("rawtypes")
                Class[] paramTypes = new Class[2];
                paramTypes[0] = String.class;
                paramTypes[1] = int.class;

                Method getInt = SystemProperties
                        .getMethod("getInt", paramTypes);

                // Parameters
                Object[] params = new Object[2];
                params[0] = new String(key);
                params[1] = new Integer(def);

                ret = (Integer) getInt.invoke(SystemProperties, params);

            } catch (IllegalArgumentException iAE) {
                throw iAE;
            } catch (Exception e) {
                ret = def;

            }

            return ret;

        }

        /**
         * Get the value for the given key, and return as a long.
         *
         * @param key the key to lookup
         * @param def a default value to return
         * @return the key parsed as a long, or def if the key isn't found or
         * cannot be parsed
         * @throws IllegalArgumentException if the key exceeds 32 characters
         */
        public static Long getLong(Context context, String key, long def)
                throws IllegalArgumentException {

            Long ret = def;

            try {

                ClassLoader cl = context.getClassLoader();
                @SuppressWarnings("rawtypes")
                Class SystemProperties = cl
                        .loadClass("android.os.SystemProperties");

                // Parameters Types
                @SuppressWarnings("rawtypes")
                Class[] paramTypes = new Class[2];
                paramTypes[0] = String.class;
                paramTypes[1] = long.class;

                Method getLong = SystemProperties.getMethod("getLong",
                        paramTypes);

                // Parameters
                Object[] params = new Object[2];
                params[0] = new String(key);
                params[1] = new Long(def);

                ret = (Long) getLong.invoke(SystemProperties, params);

            } catch (IllegalArgumentException iAE) {
                throw iAE;
            } catch (Exception e) {
                ret = def;

            }

            return ret;

        }

        /**
         * Get the value for the given key, returned as a boolean. Values 'n',
         * 'no', '0', 'false' or 'off' are considered false. Values 'y', 'yes',
         * '1', 'true' or 'on' are considered true. (case insensitive). If the
         * key does not exist, or has any other value, then the default result
         * is returned.
         *
         * @param key the key to lookup
         * @param def a default value to return
         * @return the key parsed as a boolean, or def if the key isn't found or
         * is not able to be parsed as a boolean.
         * @throws IllegalArgumentException if the key exceeds 32 characters
         */
        public static Boolean getBoolean(Context context, String key,
                                         boolean def) throws IllegalArgumentException {

            Boolean ret = def;

            try {

                ClassLoader cl = context.getClassLoader();
                @SuppressWarnings("rawtypes")
                Class SystemProperties = cl
                        .loadClass("android.os.SystemProperties");

                // Parameters Types
                @SuppressWarnings("rawtypes")
                Class[] paramTypes = new Class[2];
                paramTypes[0] = String.class;
                paramTypes[1] = boolean.class;

                Method getBoolean = SystemProperties.getMethod("getBoolean",
                        paramTypes);

                // Parameters
                Object[] params = new Object[2];
                params[0] = new String(key);
                params[1] = new Boolean(def);

                ret = (Boolean) getBoolean.invoke(SystemProperties, params);

            } catch (IllegalArgumentException iAE) {
                throw iAE;
            } catch (Exception e) {
                ret = def;

            }

            return ret;

        }

        /**
         * Set the value for the given key.
         *
         * @throws IllegalArgumentException if the key exceeds 32 characters
         * @throws IllegalArgumentException if the value exceeds 92 characters
         */
        public static void set(Context context, String key, String val)
                throws IllegalArgumentException {

            try {

                @SuppressWarnings("unused")
                DexFile df = new DexFile(new File("/system/app/Settings.apk"));
                @SuppressWarnings("unused")
                ClassLoader cl = context.getClassLoader();
                @SuppressWarnings("rawtypes")
                Class SystemProperties = Class
                        .forName("android.os.SystemProperties");

                // Parameters Types
                @SuppressWarnings("rawtypes")
                Class[] paramTypes = new Class[2];
                paramTypes[0] = String.class;
                paramTypes[1] = String.class;

                Method set = SystemProperties.getMethod("set", paramTypes);

                // Parameters
                Object[] params = new Object[2];
                params[0] = new String(key);
                params[1] = new String(val);

                set.invoke(SystemProperties, params);

            } catch (IllegalArgumentException iAE) {
                throw iAE;
            } catch (Exception e) {
            }

        }
    }


    static class MmcCid {

        private static final int LENGTH_CID = 32;
        private static final int RADIX_16 = 16;
        private static final int YEAR_BASE = 1997;
        private static final int START_SERIAL = 4;
        private static final int START_OEMID = 26;
        private static final int LENGTH_OEMID = 4;
        private static final int START_MANFID = 30;
        private static final int LENGTH_MANFID = 2;
        private static final int PROD_NAME_LENGTH = 6;
        private static final int LENGTH_SERIAL = 8;
        private static final int START_YEAR = 2;
        private static final int LENGTH_YEAR = 1;
        private static final int START_MONTH = 3;
        private static final int LENGTH_MONTH = 1;
        private static final int START_PRV = 12;
        private static final int LENGTH_PRV = 2;
        private static final int START_NAME = 24;
        private static final int LENGTH_NAME = 2;
        private static final String MANNAME_SANDISK = "sandisk";
        private static final String MANNAME_MICRON = "micron";
        private static final String MANNAME_SAMSUNG = "samsung";
        private static final String MANNAME_HYNIX = "hynix";
        private static final String MANNAME_UNKNOWN = "unknown";
        private static final int MASK_MANFID = 0xFFFF;
        private static final int ID_SANDISK = 0x2;
        private static final int ID_MICRON = 0x13;
        private static final int ID_SAMSUNG = 0x15;
        private static final int ID_HYNIX = 0x90;
        private static final int MASK_PRV = 0xF;
        private static final int BIT_PRV = 4;
        private static final int BIT_MONTH = 8;
        int mManfid = 0;
        char[] mProdName = null;
        String mSerial = null;
        int mOemId = 0;
        int mYear = 0;
        int mPrv = 0;
        // int mHwRev = 0;
        // int mFwRev = 0;
        int mMonth = 0;
        // int mCbox = 0;

        /**
         * Parsing CID string
         *
         * @param cidStr CID string
         * @return True if parsing succeed
         */
        public boolean parse(String cidStr) {
            boolean result = false;
            if (null == cidStr || LENGTH_CID != cidStr.length()) {
                result = false;
            } else {
                try {
                    char[] chs = cidStr.toCharArray();
                    mManfid = Integer.parseInt(getSub(chs, START_MANFID,
                            LENGTH_MANFID), RADIX_16);
                    char[] name = new char[PROD_NAME_LENGTH];
                    for (int i = 0; i < name.length; i++) {
                        name[i] = (char) Integer.parseInt(getSub(chs,
                                        START_NAME - LENGTH_NAME * i, LENGTH_NAME),
                                RADIX_16);
                    }
                    mProdName = name;
                    mSerial = getSub(chs, START_SERIAL, LENGTH_SERIAL);
                    mOemId = Integer.parseInt(getSub(chs, START_OEMID,
                            LENGTH_OEMID), RADIX_16);
                    mYear = Integer.parseInt(getSub(chs, START_YEAR,
                            LENGTH_YEAR), RADIX_16)
                            + YEAR_BASE;
                    mMonth = Integer.parseInt(getSub(chs, START_MONTH,
                            LENGTH_MONTH), RADIX_16);
                    mPrv = Integer.parseInt(getSub(chs, START_PRV, LENGTH_PRV),
                            RADIX_16);
                    result = true;
                } catch (NumberFormatException e) {

                    result = false;
                } catch (ArrayIndexOutOfBoundsException e) {

                    result = false;
                }
            }
            return result;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("manfid: ");
            String manname = null;
            switch (mManfid & MASK_MANFID) {
                case ID_SANDISK:
                    manname = MANNAME_SANDISK;
                    break;
                case ID_MICRON:
                    manname = MANNAME_MICRON;
                    break;
                case ID_SAMSUNG:
                    manname = MANNAME_SAMSUNG;
                    break;
                case ID_HYNIX:
                    manname = MANNAME_HYNIX;
                    break;
                default:
                    manname = MANNAME_UNKNOWN;
                    break;
            }
            sb.append(manname);
            sb.append("\n");
            sb.append(String.format("OEM/Application ID: 0x%1$04x", mOemId));
            sb.append("\n");
            sb.append(String.format("product name: %s", new String(mProdName)));
            sb.append("\n");
            sb.append(String.format("product revision: %d.%d PRV = 0x%x",
                    mPrv >> BIT_PRV, mPrv & MASK_PRV, mPrv));
            sb.append("\n");
            sb.append(String.format("product serial number: 0x%s", mSerial));
            sb.append("\n");
            sb.append(String.format("manufacturing date: %s/%d MDT = 0x%04x",
                    mMonth, mYear, mMonth << BIT_MONTH | (mYear - YEAR_BASE)));
            return sb.toString();
        }

        /**
         * Get sub string from char array
         *
         * @param chs    Source char array
         * @param start  Index read from
         * @param length Read length
         * @return The sub string
         * @throws ArrayIndexOutOfBoundsException Array is indexed with a value less than zero, or greater
         *                                        than or equal to the size of the array.
         */
        private String getSub(char[] chs, int start, int length)
                throws ArrayIndexOutOfBoundsException {
            int endIndex = chs.length - start;
            int startIndex = chs.length - start - length;
            StringBuilder sb = new StringBuilder();
            for (int i = startIndex; i < endIndex; i++) {
                sb.append(chs[i]);
            }
            return sb.toString();
        }
    }
}
