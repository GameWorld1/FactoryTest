package com.zzx.factorytest.bean;

public class PlatformBean {
    public static final String NAME = "name";
    public static final String MAGNETICFIELD = "Magneticfield";
    public static final String CAMERA = "Camera";
    public static final String GPS = "GPS";
    public static final String VOICE_TUBE = "VoiceTube";
    public static final String MIC = "MIC";
    public static final String SDCARD = "Sdcard";
    public static final String SIM = "SIM";
    public static final String TOUCH_SCREEN = "TouchScreen";
    public static final String SHOCK = "Shock";
    public static final String BLUETOOTH = "Bluetooth";
    public static final String WIFI = "Wifi";
    public static final String RADIO = "Radio";
    public static final String KEYPRESS = "KeyPress";
    public static final String GRAVITY_INDUCTION = "GravityInduction";
    public static final String IMEI3G = "IMEI3G";
    public static final String LCD = "LCD";
    public static final String BATTERY = "Battery";
    public static final String HORN = "Horn";
    public static final String TOUCH_SCREEN_CALIBRATION = "TouchScreenCalibration";
    public static final String FINGERPRINT = "Fingerprint";
    public static final String NFC = "NFC";
    public static final String LIGHT_DISTANCE_SENSOR = "LightDistanceSensor";
    public static final String GYROSCOPE_SENSOR = "GyroscopeSensor";
    public static final String DEVICES_REGISTER = "DeviceRegister";
    public static final String IDENTITYCARD = "IdentityCard";

    public String Name;
    public boolean MagneticField;//磁场
    public boolean Camera;
    public boolean Gps;
    public boolean VoiceTube;
    public boolean Mic;
    public boolean Sdcard;
    public boolean Sim;
    public boolean TouchScreen;
    public boolean Shock;
    public boolean Bluetooth;
    public boolean Wifi;
    public boolean Radio;
    public boolean KeyPress;
    public boolean GravityInduction;
    public boolean Imei3G;
    public boolean Lcd;
    public boolean Battery;
    public boolean Horn;
    public boolean TouchScreenCalibration;
    public boolean Fingerprint;
    public boolean Nfc;
    public boolean LightDistanceSensor;
    public boolean GyroscopeSensor;
    public boolean DeviceRegister;
    public boolean IdentityCard;
    public String FingerprintPath;
    public int FingerprintBaudrate;
    public String IdentityCardPath;
    public int IdentityCardBaudrate;

    @Override
    public String toString() {
        return "PlatformBean{" +
                "Name='" + Name + '\'' +
                ", MagneticField=" + MagneticField +
                ", Camera=" + Camera +
                ", Gps=" + Gps +
                ", VoiceTube=" + VoiceTube +
                ", Mic=" + Mic +
                ", Sdcard=" + Sdcard +
                ", Sim=" + Sim +
                ", TouchScreen=" + TouchScreen +
                ", Shock=" + Shock +
                ", Bluetooth=" + Bluetooth +
                ", Wifi=" + Wifi +
                ", Radio=" + Radio +
                ", KeyPress=" + KeyPress +
                ", GravityInduction=" + GravityInduction +
                ", Imei3G=" + Imei3G +
                ", Lcd=" + Lcd +
                ", Battery=" + Battery +
                ", Horn=" + Horn +
                ", TouchScreenCalibration=" + TouchScreenCalibration +
                ", Fingerprint=" + Fingerprint +
                ", Nfc=" + Nfc +
                ", LightDistanceSensor=" + LightDistanceSensor +
                ", GyroscopeSensor=" + GyroscopeSensor +
                ", DeviceRegister=" + DeviceRegister +
                ", IdentityCard=" + IdentityCard +
                ", FingerprintPath='" + FingerprintPath + '\'' +
                ", FingerprintBaudrate=" + FingerprintBaudrate +
                ", IdentityCardPath='" + IdentityCardPath + '\'' +
                ", IdentityCardBaudrate=" + IdentityCardBaudrate +
                '}';
    }
}
