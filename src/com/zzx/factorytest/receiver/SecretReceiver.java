package com.zzx.factorytest.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zzx.factorytest.MainActivity;

public class SecretReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent arg) {

        if (arg.getAction().equals("android.provider.Telephony.SECRET_CODE")) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setClass(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            arg.getBooleanExtra("", true);
            context.startActivity(intent);
        }

    }

}
