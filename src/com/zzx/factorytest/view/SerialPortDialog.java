package com.zzx.factorytest.view;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.zzx.factorytest.R;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;


public class SerialPortDialog extends Dialog implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private CheckBox mUHFCheckBox, mHFCheckBox, mLFCheckBox;
    private EditText mUHFEditText, mHFEditText, mLFEditText;
    private EditText mUHFUartEditText, mHFUartEditText, mLFUartEditText;
    private Button mSubmit, mCancel;
    private SharedPreferences mSP;
    private int[] mBaudrates;
    private Spinner mUHFSpinner, mHFSpinner, mLFSpinner;
    private String[] mSerialPortPath;

    public SerialPortDialog(Context context) {
        this(context, android.R.style.Theme_Holo_Dialog);
    }

    public SerialPortDialog(Context context, int theme) {
        super(context, theme);
        init(context);
    }


    private void init(Context context) {
        mBaudrates = context.getResources().getIntArray(R.array.serialport);
        mSerialPortPath = context.getResources().getStringArray(R.array.serialport_path);

        setTitle(context.getResources().getString(R.string.configuration_serialport_dialog_title));

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.configuration_serialport_dalog_view, null);
        findView(view);
        setContentView(view);

//        Window window = getWindow();
//        WindowManager windowManager = getWindow().getWindowManager();
//        Display display = windowManager.getDefaultDisplay();
//        WindowManager.LayoutParams attributes = window.getAttributes();
//        attributes.width = (int) (display.getWidth() * 0.8);
//        attributes.height = (int) (display.getHeight() * 0.8);
//        window.setAttributes(attributes);
//        window.getDecorView().setPadding(0, 0, 0, 0);

    }

    private void findView(View root) {
        mUHFCheckBox = (CheckBox) root.findViewById(R.id.uhf_CheckBox);
        mUHFEditText = (EditText) root.findViewById(R.id.uhf_EditText);
        mUHFUartEditText = (EditText) root.findViewById(R.id.uhf_uart_EditText);
        mUHFSpinner = (Spinner) root.findViewById(R.id.uhf_Spinner);


        mHFCheckBox = (CheckBox) root.findViewById(R.id.hf_CheckBox);
        mHFEditText = (EditText) root.findViewById(R.id.hf_EditText);
        mHFUartEditText = (EditText) root.findViewById(R.id.hf_uart_EditText);
        mHFSpinner = (Spinner) root.findViewById(R.id.hf_Spinner);

        mLFCheckBox = (CheckBox) root.findViewById(R.id.lf_CheckBox);
        mLFEditText = (EditText) root.findViewById(R.id.lf_EditText);
        mLFUartEditText = (EditText) root.findViewById(R.id.lf_uart_EditText);
        mLFSpinner = (Spinner) root.findViewById(R.id.lf_Spinner);

        mSubmit = (Button) root.findViewById(R.id.submit);
        mCancel = (Button) root.findViewById(R.id.cancel);

        mSubmit.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        mUHFCheckBox.setOnCheckedChangeListener(this);
        mHFCheckBox.setOnCheckedChangeListener(this);
        mLFCheckBox.setOnCheckedChangeListener(this);


        mSP = getContext().getSharedPreferences("serialport", Context.MODE_PRIVATE);

        //波特率
        mUHFEditText.setText(String.valueOf(mSP.getInt("uhf_baudrate", 115200)));
        mHFEditText.setText(String.valueOf(mSP.getInt("hf_baudrate", 115200)));
        mLFEditText.setText(String.valueOf(mSP.getInt("lf_baudrate", 115200)));

        //uart
        mLFUartEditText.setText(String.valueOf(mSP.getInt("lf_uart", 0)));
        mHFUartEditText.setText(String.valueOf(mSP.getInt("hf_uart", 0)));
        mUHFUartEditText.setText(String.valueOf(mSP.getInt("uhf_uart", 0)));

        //文件路径
        mUHFSpinner.setSelection(Arrays.binarySearch(mSerialPortPath, mSP.getString("uhf_path", "/dev/ttyMT0")));
        mHFSpinner.setSelection(Arrays.binarySearch(mSerialPortPath, mSP.getString("hf_path", "/dev/ttyMT0")));
        mLFSpinner.setSelection(Arrays.binarySearch(mSerialPortPath, mSP.getString("lf_path", "/dev/ttyMT0")));


        mUHFCheckBox.setChecked(mSP.getBoolean("uhf", false));
        mHFCheckBox.setChecked(mSP.getBoolean("hf", false));
        mLFCheckBox.setChecked(mSP.getBoolean("lf", false));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                SharedPreferences.Editor editor = mSP.edit();
                if (mUHFCheckBox.isChecked()) {

                    int baudrate = formatBaudrate(mUHFEditText.getText().toString());
                    if (baudrate < 0) {
                        Toast.makeText(getContext(), "波特率不正确", Toast.LENGTH_SHORT).show();
                        mUHFEditText.requestFocus();
                        return;
                    }
                    int uart = Integer.valueOf(mUHFUartEditText.getText().toString());
                    if (uart < 0 || uart > 8) {
                        Toast.makeText(getContext(), "uart不正确", Toast.LENGTH_SHORT).show();
                        mUHFUartEditText.requestFocus();
                        return;
                    }
                    editor.putInt("uhf_uart", uart);
                    editor.putInt("uhf_baudrate", baudrate);


                    int itemPosition = mUHFSpinner.getSelectedItemPosition();
                    String path = (String) mUHFSpinner.getItemAtPosition(itemPosition);
                    editor.putString("uhf_path", path);

                } else {
                    editor.remove("uhf_baudrate");
                    editor.remove("uhf_path");
                }

                if (mHFCheckBox.isChecked()) {

                    int baudrate = formatBaudrate(mHFEditText.getText().toString());
                    if (baudrate < 0) {
                        Toast.makeText(getContext(), "波特率不正确", Toast.LENGTH_SHORT).show();
                        mHFEditText.requestFocus();
                        return;
                    }
                    int uart = Integer.valueOf(mHFUartEditText.getText().toString());
                    if (uart < 0 || uart > 8) {
                        Toast.makeText(getContext(), "uart不正确", Toast.LENGTH_SHORT).show();
                        mHFUartEditText.requestFocus();
                        return;
                    }
                    editor.putInt("hf_uart", uart);
                    editor.putInt("hf_baudrate", baudrate);

                    int itemPosition = mHFSpinner.getSelectedItemPosition();

                    String path = (String) mHFSpinner.getItemAtPosition(itemPosition);

                    editor.putString("hf_path", path);
                } else {
                    editor.remove("hf_baudrate");
                    editor.remove("hf_path");
                }

                if (mLFCheckBox.isChecked()) {

                    int baudrate = formatBaudrate(mLFEditText.getText().toString());
                    if (baudrate < 0) {
                        Toast.makeText(getContext(), "波特率不正确", Toast.LENGTH_SHORT).show();
                        mLFEditText.requestFocus();
                        return;
                    }
                    int uart = Integer.valueOf(mLFUartEditText.getText().toString());
                    if (uart < 0 || uart > 8) {
                        Toast.makeText(getContext(), "uart不正确", Toast.LENGTH_SHORT).show();
                        mLFUartEditText.requestFocus();
                        return;
                    }
                    editor.putInt("lf_uart", uart);
                    editor.putInt("lf_baudrate", baudrate);

                    int itemPosition = mLFSpinner.getSelectedItemPosition();

                    String path = (String) mLFSpinner.getItemAtPosition(itemPosition);

                    editor.putString("lf_path", path);
                } else {
                    editor.remove("lf_baudrate");
                    editor.remove("lf_path");
                }
                editor.putBoolean("uhf", mUHFCheckBox.isChecked());
                editor.putBoolean("hf", mHFCheckBox.isChecked());
                editor.putBoolean("lf", mLFCheckBox.isChecked());
                editor.apply();

                createXML();

                break;
            case R.id.cancel:
                dismiss();
                break;
            default:
        }

        dismiss();
    }

    private void createXML() {


        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("resources");
        if (mUHFCheckBox.isChecked()) {

            String baudrate = mUHFEditText.getText().toString();
            String uart = mUHFUartEditText.getText().toString();
            Element uhf = root.addElement("uhf");
            uhf.addAttribute("baudrate", baudrate);
            uhf.addAttribute("uart", uart);
            uhf.setText((String) mUHFSpinner.getItemAtPosition(mUHFSpinner.getSelectedItemPosition()));
        }
        if (mHFCheckBox.isChecked()) {

            String baudrate = mHFEditText.getText().toString();
            String uart = mHFUartEditText.getText().toString();
            Element hf = root.addElement("hf");
            hf.addAttribute("baudrate", baudrate);
            hf.addAttribute("uart", uart);
            hf.setText((String) mHFSpinner.getItemAtPosition(mHFSpinner.getSelectedItemPosition()));
        }
        if (mLFCheckBox.isChecked()) {

            String baudrate = mLFEditText.getText().toString();
            String uart = mLFUartEditText.getText().toString();
            Element lf = root.addElement("lf");
            lf.addAttribute("baudrate", baudrate);
            lf.addAttribute("uart", uart);
            lf.setText((String) mLFSpinner.getItemAtPosition(mLFSpinner.getSelectedItemPosition()));
        }


        OutputFormat format = OutputFormat.createPrettyPrint();

        format.setEncoding("UTF-8");
        XMLWriter writer = null;
        try {
            File sotenFile = new File(Environment.getDataDirectory(), "soten");
            if (!sotenFile.exists()) {
                sotenFile.mkdir();
                sotenFile.setReadable(true, false);
                sotenFile.setExecutable(true, false);

            }
            File etc = new File(sotenFile, "etc");
            if (!etc.exists()) {
                etc.mkdir();
                etc.setReadable(true, false);
                etc.setExecutable(true, false);
            }
            File configFile = new File(etc, "soten-config.xml");
            writer = new XMLWriter(new FileOutputStream(configFile), format);
            writer.setEscapeText(false);
            writer.write(document);
            writer.flush();
            configFile.setReadable(true, false);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != writer)
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

    }

    private int formatBaudrate(String value) {
        if (null == value || "".equals(value)) {
            return -1;
        }
        value = value.trim();
        try {
            Integer baudrate = Integer.valueOf(value);

            return checkBaudrate(baudrate) ? baudrate : -1;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private boolean checkBaudrate(int baudrate) {
        return Arrays.binarySearch(mBaudrates, baudrate) >= 0;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.uhf_CheckBox:
                mUHFEditText.setEnabled(isChecked);
                mUHFSpinner.setEnabled(isChecked);
                mUHFUartEditText.setEnabled(isChecked);
                break;
            case R.id.hf_CheckBox:
                mHFEditText.setEnabled(isChecked);
                mHFSpinner.setEnabled(isChecked);
                mHFUartEditText.setEnabled(isChecked);
                break;
            case R.id.lf_CheckBox:
                mLFEditText.setEnabled(isChecked);
                mLFSpinner.setEnabled(isChecked);
                mLFUartEditText.setEnabled(isChecked);
                break;
        }
    }
}
