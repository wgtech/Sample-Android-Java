package project.wgtech.sampleapp.view;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import project.wgtech.sampleapp.tools.Constants;
import project.wgtech.sampleapp.tools.PermissionsActivity;

public class GalleryActivity extends AppCompatActivity {
    private final static String TAG = GalleryActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissions = new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            };

            Intent perm = new Intent(this, PermissionsActivity.class);
            perm.putExtra("permissions", permissions);

            startActivityForResult(perm, Constants.PERMISSIONS_REQUEST);
        } else {
            initGallery();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 권한
        if (resultCode == Constants.PERMISSIONS_RESPONSE_OK) {
            initGallery();
        } else {
            // 권한 없음
            finish();
        }

        // 카메라
        if (resultCode == Constants.GALLERY_RESPONSE_OK) {
            // 저장
            setResult(resultCode);
            sendBroadcast(data);
        }
    }

    private void initGallery() {
    }
}
