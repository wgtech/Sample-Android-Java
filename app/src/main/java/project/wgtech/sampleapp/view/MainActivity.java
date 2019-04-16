package project.wgtech.sampleapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import project.wgtech.sampleapp.R;
import project.wgtech.sampleapp.databinding.ActivityMainBinding;
import project.wgtech.sampleapp.viewmodel.NASACardViewModel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();

    private ActivityMainBinding binding;

    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private RecyclerView rv;


    private boolean isCardViewOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: MainActivity");
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        Log.d(TAG, "onCreate: " + i.getStringExtra("serviceType"));
        Log.d(TAG, "onCreate: " + i.getStringExtra("id"));
        Log.d(TAG, "onCreate: " + i.getStringExtra("email"));

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setActivity(this);
        binding.dlMain.closeDrawer(GravityCompat.START);
        toggle = new ActionBarDrawerToggle(this, binding.dlMain, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setToolbarNavigationClickListener(view -> {
            Toast.makeText(this, "누름! 누름!", Toast.LENGTH_SHORT).show();
        });
        toggle.syncState();
        toolbar = findViewById(R.id.toolbar);
        binding.nvMain.setVerticalFadingEdgeEnabled(false);

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

        rv = findViewById(R.id.rv_main);
        model.getImages(dates).observe(this, nasaImageRepos -> {
            CardViewAdapter adapter = new CardViewAdapter(MainActivity.this, nasaImageRepos);
            rv.setAdapter(adapter);
            rv.setHasFixedSize(true);
            rv.setLayoutManager(new LinearLayoutManager(
                    MainActivity.this, RecyclerView.VERTICAL, false));
        });

        //startLottieAnimation();

    }

    public void clickFab(View view){
        Toast.makeText(MainActivity.this, "Click! Click!", Toast.LENGTH_SHORT).show();
    }

    public void clickTitle(View view) {
        // Title 복귀 또는 about
        Toast.makeText(MainActivity.this, "Main 터치", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
                if (binding.dlMain.isDrawerOpen(GravityCompat.START)) {
                    binding.dlMain.closeDrawer(GravityCompat.START);
                } else {
                    binding.dlMain.openDrawer(GravityCompat.START);
                }
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

    @Override
    public void onBackPressed() {
        if (binding.dlMain.isDrawerOpen(GravityCompat.START)) {
            binding.dlMain.closeDrawers();
        } else {
            super.onBackPressed();
            finish();
        }
    }
}

