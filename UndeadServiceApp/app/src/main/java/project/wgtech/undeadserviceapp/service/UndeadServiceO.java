package project.wgtech.undeadserviceapp.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import project.wgtech.undeadserviceapp.R;

public class UndeadServiceO extends Service {
    private static final String TAG = UndeadServiceO.class.getSimpleName();

    private static final String CHANNEL_ID = "undead_channel";
    private static final String CHANNEL_NAME = "undead_channel";

    private Context mContext;

    public UndeadServiceO() {
        mContext = UndeadServiceO.this;
    }

    public UndeadServiceO(Context context) {
        mContext = context;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new Binder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "UndeadServiceO: Grrr.... from onStartCommand");

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = manager.getNotificationChannel(CHANNEL_ID);
            if (mChannel == null) {
                mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_NONE);
                manager.createNotificationChannel(mChannel);
            }
        }

        Notification fakeNotification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(null)
                .setContentText(null)
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
        startForeground(1111, fakeNotification);

        Intent i = new Intent(mContext, UndeadService.class);
        startService(i);

        stopForeground(true);
        stopSelf();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
