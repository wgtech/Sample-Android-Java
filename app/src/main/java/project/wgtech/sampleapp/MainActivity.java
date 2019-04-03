package project.wgtech.sampleapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.tv_main) TextView tv_main;
    @BindView(R.id.b_main) AppCompatButton b_main;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tv_toolbar_title) AppCompatTextView tv_toolbar_title;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.lottie_main) LottieAnimationView lottie;

    @OnClick(R.id.fab) void clickFab(){
        Toast.makeText(MainActivity.this, "Click! Click!", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.b_main) void showLayout() {
        Intent intent = new Intent(this, LayoutActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_toolbar_title) void clickTitle() {
        Toast.makeText(MainActivity.this, "Main 터치", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: MainActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_menu_black_36);
        getSupportActionBar().setTitle("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        lottie.setSpeed(0.5f);
        lottie.setRepeatCount(0);
        lottie.playAnimation();
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
}
