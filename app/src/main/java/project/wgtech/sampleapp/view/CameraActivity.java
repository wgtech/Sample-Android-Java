package project.wgtech.sampleapp.view;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import project.wgtech.sampleapp.tools.Constants;
import project.wgtech.sampleapp.tools.PermissionsActivity;

public class CameraActivity extends AppCompatActivity {

    private final static String TAG = CameraActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissions = new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };

            Intent perm = new Intent(this, PermissionsActivity.class);
            perm.putExtra("permissions", permissions);

            startActivityForResult(perm, Constants.PERMISSIONS_REQUEST);
        } else {
            initCamera();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 권한
        if (resultCode == Constants.PERMISSIONS_RESPONSE_OK) {
            Log.d(TAG, "onActivityResult: 권한 획득 성공 후 카메라 실행");
            initCamera();
        } else {
            // 권한 없음
            finish();
        }

        // 카메라
        if (resultCode == Constants.CAMERA_PIC_OK) {
            // 저장
            Log.d(TAG, "onActivityResult: " + data.getData().getPath());
            setResult(resultCode);
            sendBroadcast(data);
        }
    }

    private void initCamera() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, Constants.CAMERA_REQUEST);
    }
}
