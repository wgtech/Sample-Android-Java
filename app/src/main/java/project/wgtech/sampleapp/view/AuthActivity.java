package project.wgtech.sampleapp.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.kakao.auth.KakaoSDK;
import com.kakao.auth.Session;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import project.wgtech.sampleapp.R;
import project.wgtech.sampleapp.databinding.ActivityAuthBinding;
import project.wgtech.sampleapp.tools.KeyHashTools;
import project.wgtech.sampleapp.tools.auth.kakao.GlobalApplication;
import project.wgtech.sampleapp.tools.auth.kakao.KakaoSDKAdapter;

public class AuthActivity extends AppCompatActivity {
    private final static String TAG = AuthActivity.class.getSimpleName();

    private ActivityAuthBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth);
        binding.setActivity(this);

        // 디버그 앱 키 해쉬
        Log.d(TAG, "onCreate: " + KeyHashTools.getKeyHashForKakao(getBaseContext()));
    }

    public void naverAuthClick(View view) {
        Toast.makeText(this, "네이버 인증", Toast.LENGTH_SHORT).show();
    }

    public void kakaoAuthClick(View view) {
        KakaoSDK.init(new KakaoSDKAdapter());
    }

    public void googleAuthClick(View view) {
        Toast.makeText(this, "구글 인증", Toast.LENGTH_SHORT).show();
    }
}
