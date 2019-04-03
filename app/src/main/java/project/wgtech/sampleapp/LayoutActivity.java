package project.wgtech.sampleapp;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.ButterKnife;
import project.wgtech.sampleapp.fragments.TestFragment;

public class LayoutActivity extends FragmentActivity {
    private static final String TAG = LayoutActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        TestFragment fragment = new TestFragment();
        ft.add(R.id.frag_container, fragment);
        ft.addToBackStack(null);
        ft.commit();

        ButterKnife.bind(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

