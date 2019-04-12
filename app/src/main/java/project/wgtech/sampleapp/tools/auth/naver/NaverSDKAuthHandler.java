package project.wgtech.sampleapp.tools.auth.naver;

import android.util.Log;

import com.nhn.android.naverlogin.OAuthLoginHandler;

public class NaverSDKAuthHandler extends OAuthLoginHandler {
    private static final String TAG = NaverSDKAuthHandler.class.getSimpleName();

    @Override
    public void run(boolean success) {
        if (success) {
            Log.d(TAG, "run: 인증 성공");
        } else {
            Log.d(TAG, "run: 인증 실패");
        }
    }
}
