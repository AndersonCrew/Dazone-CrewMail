package com.dazone.crewemail.gcm;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Explicitly specify that GcmIntentService will handle the intent.
        ComponentName comp = new ComponentName(context.getPackageName(),
                GcmIntentService.class.getName());
        // Start the service, keeping the device awake while it is launching.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d("sssDebug", "NotisInBackground" );
            ContextCompat.startForegroundService(context, (intent.setComponent(comp)));
        } else {
            Log.d("sssDebug", "isInBackground" );
            startWakefulService(context, (intent.setComponent(comp)));
        }
        setResultCode(Activity.RESULT_OK);
    }
}