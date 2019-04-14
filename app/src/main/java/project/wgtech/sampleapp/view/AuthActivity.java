package project.wgtech.sampleapp.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.kakao.auth.KakaoSDK;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import project.wgtech.sampleapp.R;
import project.wgtech.sampleapp.databinding.ActivityAuthBinding;
import project.wgtech.sampleapp.tools.KeyHashTools;
import project.wgtech.sampleapp.tools.auth.kakao.KakaoSDKApplication;
import project.wgtech.sampleapp.tools.auth.kakao.KakaoSDKAdapter;
import project.wgtech.sampleapp.tools.auth.naver.NaverSDK;
import project.wgtech.sampleapp.tools.auth.naver.NaverSDKUserInfo;

public class AuthActivity extends AppCompatActivity {
    private final static String TAG = AuthActivity.class.getSimpleName();

    private ActivityAuthBinding binding;
    private KakaoSDKAdapter kakaoAdapter;
    private KakaoSDKApplication kakaoApp;
    private NaverSDK naverApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth);
        binding.setActivity(this);

        // 디버그 앱 키 해쉬
        Log.d(TAG, "onCreate: " + KeyHashTools.getKeyHashForKakao(getBaseContext()));

    }

    @Override
    protected void onStart() {
        super.onStart();
        startSDK();
    }

    private void startSDK() {
        // 카카오
        if (KakaoSDK.getAdapter() == null) {
            Log.d(TAG, "startSDK: KakaoSDK 초기 실행");
            kakaoAdapter = new KakaoSDKAdapter();
            KakaoSDK.init(kakaoAdapter);
        } else {
            Log.d(TAG, "startSDK: KakaoSDK 실행중");
        }

        // 네이버
        if (naverApp == null) {
            naverApp = new NaverSDK(AuthActivity.this,
                getString(R.string.naver_client_id),
                getString(R.string.naver_client_secret),
                getString(R.string.naver_client_name)
            );
            naverApp.setSession();
        }

    }

    private void stopSDK() {
        // 카카오
        Log.d(TAG, "stopSDK: 종료");
        if (KakaoSDK.getAdapter() != null && kakaoApp != null) {
            kakaoApp.loseKakaoAuth(); // 로그아웃 테스트
        }

        // 네이버
        if (naverApp != null) {
            naverApp.logout();
            naverApp = null;
        }

    }

    public void naverAuthClick(View view) {
        Toast.makeText(this, "네이버 인증", Toast.LENGTH_SHORT).show();
        naverApp.initLogin();
    }

    public void kakaoAuthClick(View view) {
        Toast.makeText(this, "카카오 인증", Toast.LENGTH_SHORT).show();
        kakaoApp = KakaoSDKApplication.getInstance();
        kakaoApp.setActivity(this);
        kakaoApp.getKakaoAuth(KakaoSDK.getAdapter());
    }

    public void googleAuthClick(View view) {
        Toast.makeText(this, "구글 인증", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        stopSDK();
        super.onDestroy();
    }
}
