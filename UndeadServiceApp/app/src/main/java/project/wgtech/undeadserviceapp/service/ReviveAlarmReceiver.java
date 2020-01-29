package project.wgtech.undeadserviceapp.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class ReviveAlarmReceiver extends BroadcastReceiver {
    private static final String TAG = ReviveAlarmReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d(TAG, "ReviveAlarmReceiver: Your system is above the Oreo version... from onReceive");
            Intent mIntent = new Intent(context, UndeadServiceO.class);
            context.startForegroundService(mIntent);
        } else {
            Log.d(TAG, "ReviveAlarmReceiver: Your system is under the Oreo version... from onReceive");
            Intent mIntent = new Intent(context, UndeadService.class);
            context.startService(mIntent);
        }
    }
}
