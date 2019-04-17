package project.wgtech.sampleapp.view;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
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
    private RecyclerView rv;

    private ArrayList<String> dates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: MainActivity");
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setActivity(this);

        Intent i = getIntent();
        Log.d(TAG, "onCreate: " + i.getStringExtra("serviceType"));
        Log.d(TAG, "onCreate: " + i.getStringExtra("id"));
        Log.d(TAG, "onCreate: " + i.getStringExtra("email"));

        getDrawerLayout();
        getMainLayout();
        getRawData();
        setDatasIntoRecyclerView();
    }

    ///////////////////////////////////////

    private void getDrawerLayout() {
        binding.dlMain.closeDrawer(GravityCompat.START);
        toggle = new ActionBarDrawerToggle(this, binding.dlMain, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setToolbarNavigationClickListener(v -> {
            switch (v.getId()) {
                default:
                    Toast.makeText(this, "아무거나 눌렀네", Toast.LENGTH_SHORT).show();
                    break;
            }
        });
        toggle.syncState();
        binding.nvMain.setVerticalFadingEdgeEnabled(false);
    }

    private void getMainLayout() {
        setSupportActionBar(binding.viewIncludeMain.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_menu_black_36);
        getSupportActionBar().setTitle("");

        rv = findViewById(R.id.rv_main);

        binding.viewIncludeMain.fab.setOnClickListener(view -> {
            Toast.makeText(MainActivity.this, "Click! Click!", Toast.LENGTH_SHORT).show();
        });

        binding.viewIncludeMain.tvToolbarTitle.setOnClickListener(view -> {
            Toast.makeText(MainActivity.this, "제목 클릭", Toast.LENGTH_SHORT).show();
        });

    }

    private void getRawData() {
        // 추가
        dates = new ArrayList<>(1);
        dates.add("2019-03-27");
        dates.add("2019-03-28");
        dates.add("2019-03-29");
        dates.add("2019-03-30");
        dates.add("2019-03-31");
        dates.add("2019-04-01");
        dates.add("2019-04-02");
        dates.add("2019-04-03");
        dates.add("2019-04-04");
    }

    private void setDatasIntoRecyclerView() {

        ViewModelProvider.AndroidViewModelFactory f = new ViewModelProvider.AndroidViewModelFactory(getApplication());
        NASACardViewModel model = f.create(NASACardViewModel.class);
        model.getImages(dates).observe(this, nasaImageRepos -> {
            rv.setHasFixedSize(true);
            CardViewAdapter adapter = new CardViewAdapter(MainActivity.this, nasaImageRepos);
            rv.setAdapter(adapter);
            rv.setLayoutManager(new LinearLayoutManager(
                    MainActivity.this, RecyclerView.VERTICAL, false));
        });

        if (dates == null || dates.size() == 0) {
            LottieViewAdapter adapter = new LottieViewAdapter();
            rv.setAdapter(adapter);
            rv.setLayoutManager(new GridLayoutManager(
                    MainActivity.this, 1));
        }
    }

    ///////////////////////////////////////

    public void clickFab(View view) {
        Toast.makeText(MainActivity.this, "Click! Click!", Toast.LENGTH_SHORT).show();
    }

    public void clickTitle(View view) {
        // Title 복귀 또는 about
        Toast.makeText(MainActivity.this, "Main 터치", Toast.LENGTH_SHORT).show();
    }

    ///////////////////////////////////////

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
                Snackbar.make(findViewById(R.id.dl_main), "계정 확인바랍니다.", Snackbar.LENGTH_LONG).show();
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

