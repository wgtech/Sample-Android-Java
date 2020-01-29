package project.wgtech.undeadserviceapp.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Calendar;

public class UndeadService extends Service {
    private static final String TAG = UndeadService.class.getSimpleName();
    public static Intent serviceIntent;

    private Context mContext;

    public UndeadService() {
        mContext = UndeadService.this;
    }

    public UndeadService(Context context) {
        mContext = context;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new Binder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "UndeadService: Grrr.... from onStartCommand");
        return START_STICKY;
    }

    private void reviveService() {
        Log.d(TAG, "UndeadService: Now reviving service... from reviveService");
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.MILLISECOND, 1);

        Intent alarmIntent = new Intent(mContext, ReviveAlarmReceiver.class);
        PendingIntent restartIntent = PendingIntent.getBroadcast(mContext, 0, alarmIntent, 0);

        AlarmManager mRunnerAlarm = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        mRunnerAlarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), restartIntent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        reviveService();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "UndeadService: Is this service alive again...? from onDestroy");
        super.onDestroy();
        serviceIntent = null;
        reviveService();
    }
}
