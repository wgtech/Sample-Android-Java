package project.wgtech.sampleapp.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import project.wgtech.sampleapp.R;
import project.wgtech.sampleapp.databinding.ActivityMainBinding;
import project.wgtech.sampleapp.model.UserInfo;
import project.wgtech.sampleapp.tools.Constants;
import project.wgtech.sampleapp.viewmodel.NASACardViewModel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private final static String TAG = MainActivity.class.getSimpleName();

    private ActivityMainBinding binding;

    private ActionBarDrawerToggle toggle;
    private RecyclerView rv;

    private UserInfo info;


    private ArrayList<String> dates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: MainActivity");
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setActivity(this);

        getDrawerLayout();
        getRawData();
        getMainLayout();
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


        View nav = binding.nvMain.getHeaderView(0);

        AppCompatButton btn = nav.findViewById(R.id.btn_drawer_logout);
        btn.setOnClickListener(new OnNavigationHeaderItemClickListener(MainActivity.this));

        AppCompatTextView tv = nav.findViewById(R.id.tv_drawer_name);
        if (info.getEmail() == null || info.getEmail().equals("")) {
            tv.setText(info.getId());
        } else {
            tv.setText(info.getEmail());
        }

        AppCompatImageView profile = nav.findViewById(R.id.iv_drawer_head);
        Glide.with(MainActivity.this)
                .asBitmap()
                .load(info.getProfileURL())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(1.0f)
                .error(R.drawable.baseline_account_circle_black_48)
                .into(profile);

        AppCompatImageView icon = nav.findViewById(R.id.iv_drawer_service_type);
        Glide.with(MainActivity.this)
                .asBitmap()
                .load(info.getServiceTypeIcon())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.baseline_account_circle_black_48)
                .into(icon);

        binding.nvMain.setNavigationItemSelectedListener(this);
    }

    private void getRawData() {
        Intent i = getIntent();
        Log.d(TAG, "onCreate: " + i.getStringExtra("serviceType"));
        Log.d(TAG, "onCreate: " + i.getStringExtra("id"));
        Log.d(TAG, "onCreate: " + i.getStringExtra("email"));
        Log.d(TAG, "onCreate: " + i.getStringExtra("profileURL"));
        Log.d(TAG, "onCreate: " + i.getStringExtra("serviceTypeIconPath"));
        info = new UserInfo.Builder()
                .serviceType(i.getStringExtra("serviceType"))
                .id(i.getStringExtra("id"))
                .email(i.getStringExtra("email"))
                .profileURL(i.getStringExtra("profileURL"))
                .serviceTypeIcon(i.getStringExtra("serviceTypeIconPath"))
                .build();

        // 추가
        dates = new ArrayList<>(1);
//        dates.add("2019-04-01");
//        dates.add("2019-04-02");
//        dates.add("2019-04-03");
//        dates.add("2019-04-04");
//        dates.add("2019-04-05");
//        dates.add("2019-04-06");
//        dates.add("2019-04-07");
//        dates.add("2019-04-08");
//        dates.add("2019-04-09");
//        dates.add("2019-04-10");
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
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.item_nav_camera:
                Toast.makeText(MainActivity.this, "카메라 카메라", Toast.LENGTH_SHORT).show();
                Intent camera = new Intent(MainActivity.this, Camera2Activity.class);
                startActivityForResult(camera, Constants.CAMERA_REQUEST);
                break;

            case R.id.item_nav_gallery:
                Toast.makeText(MainActivity.this, "갤러리 갤러리", Toast.LENGTH_SHORT).show();
                Intent gallery = new Intent(MainActivity.this, GalleryActivity.class);
                startActivityForResult(gallery, Constants.GALLERY_REQUEST);
                break;

            case R.id.nav_share:
                Toast.makeText(MainActivity.this, "공유 공유", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
        binding.dlMain.closeDrawers();

        return false;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constants.PERMISSIONS_RESPONSE_FAIL) {
            Snackbar.make(binding.dlMain, "권한 확인바랍니다.", Snackbar.LENGTH_LONG).show();
        }

        if (resultCode == Constants.CAMERA_PIC_OK) {
            // 데이터 새로고침
            Log.d(TAG, "onActivityResult: 카메라 저장 성공");
        }

        if (resultCode == Constants.GALLERY_RESPONSE_OK) {
            // 호출
            Log.d(TAG, "onActivityResult: 사진 불러오기 성공 " + data.getData().getPath());
        }

        if (resultCode == Constants.GALLERY_RESPONSE_FAIL) {
            Log.d(TAG, "onActivityResult: 사진 불러오기 실패/취소");
        }
    }
}

