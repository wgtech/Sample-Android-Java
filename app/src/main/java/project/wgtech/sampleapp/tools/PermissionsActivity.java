package project.wgtech.sampleapp.tools;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import project.wgtech.sampleapp.R;

public class PermissionsActivity extends AppCompatActivity {
    private final static String TAG = PermissionsActivity.class.getSimpleName();

    private String[] permissions;
    private String currentCheckingPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissions = getIntent().getStringArrayExtra("permissions");

        int grantCount = 0;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (int i = 0; i < permissions.length; i++) {
                if (ContextCompat.checkSelfPermission(this, permissions[i]) == PackageManager.PERMISSION_DENIED) {
                    currentCheckingPermission = permissions[i];
                    ActivityCompat.requestPermissions(this, new String[]{currentCheckingPermission}, Constants.PERMISSIONS_REQUEST);
                } else {
                    grantCount += 1;
                }
            }

            if (grantCount == permissions.length) {
                finishBySucceed();
            } else {
                finishByFailed();
            }

        }

        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        //
        //}
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Constants.PERMISSIONS_REQUEST) {
            String requestMessage = "";

            switch (currentCheckingPermission) {
                case Manifest.permission.CAMERA:
                    requestMessage = getString(R.string.permission_request_camera);
                    break;

                case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                    requestMessage = getString(R.string.permission_request_write_external_storage);
                    break;

                case Manifest.permission.READ_EXTERNAL_STORAGE:
                    requestMessage = getString(R.string.permission_request_read_external_storage);
                    break;

                default:
                    break;
            }

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, currentCheckingPermission)) {
                finishByFailed();
            } else {
                finishBySucceed();
            }


        }
    }

    private void finishByFailed() {
        // 권한 실패
        setResult(Constants.PERMISSIONS_RESPONSE_FAIL);
        finish();
    }

    private void finishBySucceed() {
        setResult(Constants.PERMISSIONS_RESPONSE_OK);
        finish();
    }
}
