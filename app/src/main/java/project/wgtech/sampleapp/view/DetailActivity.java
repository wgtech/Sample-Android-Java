package project.wgtech.sampleapp.view;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.Explode;
import android.util.Log;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.palette.graphics.Palette;
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
        getWindow().setAllowEnterTransitionOverlap(true);
        getWindow().setAllowReturnTransitionOverlap(true);
        getWindow().setSharedElementEnterTransition(new AutoTransition());
        getWindow().setSharedElementReturnTransition(new AutoTransition());
        getWindow().setEnterTransition(new Explode());
        getWindow().setExitTransition(new Explode());

        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        binding.setActivity(this);

        Intent i = getIntent();
        Log.d(TAG, "onCreate: " + i.getIntExtra("position", 99999) + ", " + i.getStringExtra("url"));
        String transitionTag = i.getStringExtra("transition");

        Glide.with(getBaseContext())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .load(i.getStringExtra("url"))
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        if (resource != null){
                            Palette.from(resource).generate(palette -> {
                                if (palette == null) return;

                                Palette.Swatch vibrantSwatch = palette.getDarkVibrantSwatch();

                                if (vibrantSwatch != null) {
                                    int backgroundColor = vibrantSwatch.getRgb();
                                    int titleTextColor = vibrantSwatch.getTitleTextColor();
                                    int bodyTextColor = vibrantSwatch.getBodyTextColor();

                                    binding.clDetailButtons.setBackgroundColor(backgroundColor);
                                    binding.btnDetailCloud.setBackgroundTintList(ColorStateList.valueOf(titleTextColor));
                                    binding.btnDetailShare.setBackgroundTintList(ColorStateList.valueOf(titleTextColor));
                                    binding.btnDetailSound.setBackgroundTintList(ColorStateList.valueOf(titleTextColor));
                                    binding.clDetailAreaBottom.setBackgroundColor(backgroundColor);
                                }
                            });
                        }
                        return false;
                    }
                })
                .into(binding.ivDetail);

        binding.ivDetail.setTransitionName(transitionTag);
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: 눌렀다.");
        super.onBackPressed();
        finishAfterTransition();
    }
}

