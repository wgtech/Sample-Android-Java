package project.wgtech.sampleapp.tools.auth.kakao;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.kakao.auth.ISessionCallback;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;

import project.wgtech.sampleapp.model.ServiceType;
import project.wgtech.sampleapp.view.AuthActivity;
import project.wgtech.sampleapp.view.MainActivity;

public class KakaoSessionCallback implements ISessionCallback {
    private final static String TAG = KakaoSessionCallback.class.getSimpleName();

    @Override
    public void onSessionOpened() {

        UserManagement.getInstance().me(new MeV2ResponseCallback() {
            @Override
            public void onSuccess(MeV2Response result) {
                Context context = KakaoSDKApplication.getGlobalApplicationContext();
                Intent i = new Intent(context, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("serviceType", ServiceType.KAKAO.getValue());
                i.putExtra("id", String.valueOf(result.getId()));
                i.putExtra("email", result.getKakaoAccount().getEmail());
                i.putExtra("profileURL", result.getProfileImagePath());
                i.putExtra("serviceTypeIconPath", ServiceType.KAKAO.getIconPath());
                context.startActivity(i);
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {

            }

        });

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


