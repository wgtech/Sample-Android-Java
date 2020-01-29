package project.wgtech.undeadserviceapp.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class BootCompleteReceiver extends BroadcastReceiver {
    private static final String TAG = BootCompleteReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) { // intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)
            Log.d(TAG, "BootCompleteReceiver: Boot Completed! from onReceive");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Log.d(TAG, "BootCompleteReceiver: Your system is above the Oreo version... from onReceive");
                Intent mIntent = new Intent(context, UndeadServiceO.class);
                context.startForegroundService(mIntent);
            } else {
                Log.d(TAG, "BootCompleteReceiver: Your system is under the Oreo version... from onReceive");
                Intent mIntent = new Intent(context, UndeadService.class);
                context.startService(mIntent);
            }
        }

    }
}
