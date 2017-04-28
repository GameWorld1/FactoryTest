package com.zzx.factorytest.help;


import android.content.Context;
import android.os.Environment;
import android.util.Xml;

import com.zzx.factorytest.bean.PlatformBean;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PlatformHelp {
    private static final File PATH = new File(Environment.getRootDirectory(), "etc" + File.separator + "FactoryTestConfig.xml");

    private static final String PLATFORM_TAG = "Platform";
    private static PlatformBean mPlatform;

    public static PlatformBean getPlatform(Context context) {

        if (null == mPlatform) {
            mPlatform = readPlatform(PATH);
        }
        return mPlatform;
    }

    private static PlatformBean readPlatform(File configFile) {
        PlatformBean bean = new PlatformBean();
        XmlPullParser parser = Xml.newPullParser();
        FileInputStream input;
        try {
            input = new FileInputStream(configFile);
            parser.setInput(input, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (PLATFORM_TAG.equals(name)) {
                            bean.Name = parser.getAttributeValue(null, PlatformBean.NAME);
                        } else if (PlatformBean.MAGNETICFIELD.equals(name)) {
                            parser.next();
                            int value = Integer.valueOf(parser.getText().trim());
                            bean.MagneticField = value != 0;

                        } else if (PlatformBean.BLUETOOTH.equals(name)) {
                            parser.next();
                            int value = Integer.valueOf(parser.getText().trim());
                            bean.Bluetooth = value != 0;

                        } else if (PlatformBean.BATTERY.equals(name)) {
                            parser.next();
                            int value = Integer.valueOf(parser.getText().trim());
                            bean.Battery = value != 0;

                        } else if (PlatformBean.CAMERA.equals(name)) {
                            parser.next();
                            int value = Integer.valueOf(parser.getText().trim());
                            bean.Camera = value != 0;

                        } else if (PlatformBean.DEVICES_REGISTER.equals(name)) {
                            parser.next();
                            int value = Integer.valueOf(parser.getText().trim());
                            bean.DeviceRegister = value != 0;

                        } else if (PlatformBean.FINGERPRINT.equals(name)) {
                            bean.FingerprintPath = parser.getAttributeValue(null, "path");
                            bean.FingerprintBaudrate = Integer.valueOf(parser.getAttributeValue(null, "baudrate"));
                            parser.next();
                            int value = Integer.valueOf(parser.getText().trim());
                            bean.Fingerprint = value != 0;

                        } else if (PlatformBean.GPS.equals(name)) {
                            parser.next();
                            int value = Integer.valueOf(parser.getText().trim());
                            bean.Gps = value != 0;

                        } else if (PlatformBean.GRAVITY_INDUCTION.equals(name)) {
                            parser.next();
                            int value = Integer.valueOf(parser.getText().trim());
                            bean.GravityInduction = value != 0;

                        } else if (PlatformBean.GYROSCOPE_SENSOR.equals(name)) {
                            parser.next();
                            int value = Integer.valueOf(parser.getText().trim());
                            bean.GyroscopeSensor = value != 0;

                        } else if (PlatformBean.HORN.equals(name)) {
                            parser.next();
                            int value = Integer.valueOf(parser.getText().trim());
                            bean.Horn = value != 0;

                        } else if (PlatformBean.IMEI3G.equals(name)) {
                            parser.next();
                            int value = Integer.valueOf(parser.getText().trim());
                            bean.Imei3G = value != 0;

                        } else if (PlatformBean.KEYPRESS.equals(name)) {
                            parser.next();
                            int value = Integer.valueOf(parser.getText().trim());
                            bean.KeyPress = value != 0;

                        } else if (PlatformBean.LCD.equals(name)) {
                            parser.next();
                            int value = Integer.valueOf(parser.getText().trim());
                            bean.Lcd = value != 0;

                        } else if (PlatformBean.LIGHT_DISTANCE_SENSOR.equals(name)) {
                            parser.next();
                            int value = Integer.valueOf(parser.getText().trim());
                            bean.LightDistanceSensor = value != 0;

                        } else if (PlatformBean.MIC.equals(name)) {
                            parser.next();
                            int value = Integer.valueOf(parser.getText().trim());
                            bean.Mic = value != 0;
                        } else if (PlatformBean.MAGNETICFIELD.equals(name)) {
                            parser.next();
                            int value = Integer.valueOf(parser.getText().trim());
                            bean.Mic = value != 0;

                        } else if (PlatformBean.NFC.equals(name)) {
                            parser.next();
                            int value = Integer.valueOf(parser.getText().trim());
                            bean.Nfc = value != 0;

                        } else if (PlatformBean.RADIO.equals(name)) {
                            parser.next();
                            int value = Integer.valueOf(parser.getText().trim());
                            bean.Radio = value != 0;

                        } else if (PlatformBean.SDCARD.equals(name)) {
                            parser.next();
                            int value = Integer.valueOf(parser.getText().trim());
                            bean.Sdcard = value != 0;
                        } else if (PlatformBean.SHOCK.equals(name)) {
                            parser.next();
                            int value = Integer.valueOf(parser.getText().trim());
                            bean.Shock = value != 0;

                        } else if (PlatformBean.WIFI.equals(name)) {
                            parser.next();
                            int value = Integer.valueOf(parser.getText().trim());
                            bean.Wifi = value != 0;

                        } else if (PlatformBean.VOICE_TUBE.equals(name)) {
                            parser.next();
                            int value = Integer.valueOf(parser.getText().trim());
                            bean.VoiceTube = value != 0;

                        } else if (PlatformBean.TOUCH_SCREEN_CALIBRATION.equals(name)) {
                            parser.next();
                            int value = Integer.valueOf(parser.getText().trim());
                            bean.TouchScreenCalibration = value != 0;

                        } else if (PlatformBean.TOUCH_SCREEN.equals(name)) {
                            parser.next();
                            int value = Integer.valueOf(parser.getText().trim());
                            bean.TouchScreen = value != 0;
                        } else if (PlatformBean.SIM.equals(name)) {
                            parser.next();
                            int value = Integer.valueOf(parser.getText().trim());
                            bean.Sim = value != 0;
                        } else if (PlatformBean.IDENTITYCARD.equals(name)) {
                            bean.IdentityCardPath = parser.getAttributeValue(null, "path");
                            bean.IdentityCardBaudrate = Integer.valueOf(parser.getAttributeValue(null, "baudrate"));
                            parser.next();
                            int value = Integer.valueOf(parser.getText().trim());
                            bean.IdentityCard = value != 0;
                        }


                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException e1) {
            e1.printStackTrace();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return bean;
    }


}

