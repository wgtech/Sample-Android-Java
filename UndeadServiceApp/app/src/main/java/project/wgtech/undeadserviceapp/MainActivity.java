package project.wgtech.undeadserviceapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import project.wgtech.undeadserviceapp.service.UndeadService;
import project.wgtech.undeadserviceapp.service.UndeadServiceO;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private Context mContext;
    private Intent serviceIntent;

    private AppCompatButton btnService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = MainActivity.this;
        btnService = findViewById(R.id.btnService);

        // Add whitelist to ignore battery optimizations, like doze mode
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(POWER_SERVICE);
        boolean isWhiteListing = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isWhiteListing = pm.isIgnoringBatteryOptimizations(getApplicationContext().getPackageName());
        }
        if (!isWhiteListing) {
            Intent i = new Intent();
            i.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            i.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
            startActivity(i);
        }

        btnService.setEnabled(!isServiceRun());
        btnService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readyToService();
                btnService.setEnabled(false);
            }
        });

    }

    private boolean isServiceRun() {
        return UndeadService.serviceIntent != null;
    }

    private void readyToService() {
        if (!isServiceRun()) {
            serviceIntent = new Intent(mContext, UndeadService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Intent mServiceIntent = new Intent(mContext, UndeadServiceO.class);
                mContext.startForegroundService(mServiceIntent);
            } else {
                Intent mServiceIntent = new Intent(mContext, UndeadService.class);
                mContext.startService(mServiceIntent);
            }
            Toast.makeText(mContext, "Start undead service", Toast.LENGTH_SHORT).show();
        } else {
            serviceIntent = UndeadService.serviceIntent;
            Toast.makeText(mContext, "Already Running", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isServiceRun()) {
            stopService(serviceIntent);
            serviceIntent = null;
        }
    }
}
