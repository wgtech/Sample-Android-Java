package project.wgtech.sampleapp.view;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import project.wgtech.sampleapp.R;
import project.wgtech.sampleapp.databinding.ActivityGalleryBinding;
import project.wgtech.sampleapp.tools.Constants;
import project.wgtech.sampleapp.tools.PermissionsActivity;

public class GalleryActivity extends AppCompatActivity {
    private final static String TAG = GalleryActivity.class.getSimpleName();

    private ActivityGalleryBinding binding;
    private Intent preview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(GalleryActivity.this, R.layout.activity_gallery);
        binding.setActivity(this);

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (requestCode == Constants.PERMISSIONS_REQUEST) {
                if (resultCode == Constants.PERMISSIONS_RESPONSE_OK) {
                    Log.d(TAG, "onActivityResult: 권한 획득 성공");
                    initGallery();
                }

                else if (resultCode == Constants.GALLERY_RESPONSE_FAIL) {
                    Log.d(TAG, "onActivityResult: 권한 획득 실패");
                    finish();
                }
            }
        }

        // 카메라
        if (requestCode == Constants.GALLERY_REQUEST) {
            if (data != null) {
                preview = data;
                Log.d(TAG, "onActivityResult: 사진 선택 " + preview.getData().getPath());

                // 프리뷰
                Glide.with(GalleryActivity.this)
                        .asBitmap()
                        .addListener(new RequestListener<Bitmap>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                setResult(Constants.GALLERY_RESPONSE_FAIL);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        })
                        .load(preview.getData())
                        .into(binding.ivGalleryPreview);


            } else {
                Log.d(TAG, "onActivityResult: 사진 미선택");
                setResult(Constants.GALLERY_RESPONSE_FAIL);
                finish();
            }
        }
    }

    private void initGallery() {
        Intent gallery = new Intent();
        gallery.setAction(Intent.ACTION_PICK);
        gallery.setType(MediaStore.Images.Media.CONTENT_TYPE);
        gallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallery, Constants.GALLERY_REQUEST);
    }

    public void clickCancel(View view) {
        Toast.makeText(this, "취소했습니다.", Toast.LENGTH_SHORT).show();
        initGallery();
    }

    public void clickOk(View view) {
        Toast.makeText(this, "사진을 보냅니다.", Toast.LENGTH_SHORT).show();
        setResult(Constants.GALLERY_RESPONSE_OK, preview);
        finish();
    }


}
