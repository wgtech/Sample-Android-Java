package project.wgtech.sampleapp.tools.auth.kakao;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.kakao.auth.ISessionCallback;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.util.exception.KakaoException;

import project.wgtech.sampleapp.view.MainActivity;

public class KakaoSessionCallback implements ISessionCallback {
    private final static String TAG = KakaoSessionCallback.class.getSimpleName();

    @Override
    public void onSessionOpened() {
        Context context = KakaoSDKApplication.getGlobalApplicationContext();
        Intent i = new Intent(context, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    @Override
    public void onSessionOpenFailed(KakaoException exception) {
        Log.d(TAG, "onSessionOpenFailed: " + exception.getErrorType() + ", " + exception.getMessage());
    }

    public void doSessionClose() {
        UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                Log.d(TAG, "onCompleteLogout: 로그아웃 성공");
            }
        });
    }
}


