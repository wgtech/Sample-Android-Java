package project.wgtech.sampleapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import project.wgtech.sampleapp.R;
import project.wgtech.sampleapp.viewmodel.NASACardViewModel;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();

    private boolean isCardViewOn;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tv_toolbar_title) AppCompatTextView tv_toolbar_title;
    @BindView(R.id.fab) FloatingActionButton fab;
    //@BindView(R.id.lottie_main) LottieAnimationView lottie;
    @BindView(R.id.rv_main) RecyclerView rv;

    @OnClick(R.id.fab) void clickFab(){
        Toast.makeText(MainActivity.this, "Click! Click!", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.tv_toolbar_title) void clickTitle() {
        // Title 복귀 또는 about
        Toast.makeText(MainActivity.this, "Main 터치", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: MainActivity");
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_menu_black_36);
        getSupportActionBar().setTitle("");

        ViewModelProvider.AndroidViewModelFactory f = new ViewModelProvider.AndroidViewModelFactory(getApplication());
        NASACardViewModel model = f.create(NASACardViewModel.class);

        // 추가
        ArrayList<String> dates = new ArrayList<>(1);
        dates.add("2019-03-27");
        dates.add("2019-03-28");
        dates.add("2019-03-29");
        dates.add("2019-03-30");
        dates.add("2019-03-31");
        dates.add("2019-04-01");
        dates.add("2019-04-02");
        dates.add("2019-04-03");
        dates.add("2019-04-04");

        model.getImages(dates).observe(this, nasaImageRepos -> {
            CardViewAdapter adapter = new CardViewAdapter(getBaseContext(), nasaImageRepos);
            rv.setAdapter(adapter);
            rv.setHasFixedSize(true);
            rv.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false));
        });

        //startLottieAnimation();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) { // Account Icon
            case R.id.account_item:
                Snackbar.make(findViewById(R.id.main_layout), "계정 확인바랍니다.", Snackbar.LENGTH_LONG).show();
                break;

            default: // Navigation Icon
                Toast.makeText(this, "Navigation Button", Toast.LENGTH_SHORT).show();
                break;
        }

        return true;
    }

    private void switchLottieAnimation() {
        if (isCardViewOn == false) { // CardView off -> on
            isCardViewOn = true;
            stopLottieAnimation();
            Toast.makeText(this, "CardView On!", Toast.LENGTH_SHORT).show();
        } else { // CardView on -> off
            isCardViewOn = false;
            startLottieAnimation();
        }
    }

    private void startLottieAnimation() {
//        lottie.setMinProgress(0.0f);
//        lottie.setSpeed(0.5f);
//        lottie.setRepeatCount(0);
//        lottie.playAnimation();
    }

    private void stopLottieAnimation() {
//        lottie.cancelAnimation();
    }
}

