package project.wgtech.sampleapp.view;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.bumptech.glide.Glide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import project.wgtech.sampleapp.R;
import project.wgtech.sampleapp.databinding.ActivityDetailBinding;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = DetailActivity.class.getSimpleName();

    private ActivityDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        binding.setActivity(this);

        Intent i = getIntent();
        String url = i.getStringExtra("hdurl");
        int backgroundColor = i.getIntExtra("backgroundColor", R.color.gray_transparent30);
        int titleTextColor = i.getIntExtra("titleTextColor", R.color.gray_transparent30);
        int bodyTextColor = i.getIntExtra("bodyTextColor", R.color.gray_transparent30);


        Log.d(TAG, "onCreate: " + url);
        Log.d(TAG, "onCreate: " + backgroundColor);
        Log.d(TAG, "onCreate: " + titleTextColor);
        Log.d(TAG, "onCreate: " + bodyTextColor);

        Glide.with(getBaseContext())
                .asBitmap()
                .load(url)
                .into(binding.ivDetail);
        binding.clDetailButtons.setBackgroundColor(backgroundColor);
        binding.btnDetailCloud.setBackgroundTintList(ColorStateList.valueOf(titleTextColor));
        binding.btnDetailShare.setBackgroundTintList(ColorStateList.valueOf(titleTextColor));
        binding.btnDetailSound.setBackgroundTintList(ColorStateList.valueOf(titleTextColor));
        binding.clDetailAreaBottom.setBackgroundColor(titleTextColor);
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: 눌렀다.");
        super.onBackPressed();
        finish();
        //finishAfterTransition();
    }
}

