package project.wgtech.sampleapp.tools.auth.kakao;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.KakaoSDK;
import com.kakao.auth.Session;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import androidx.appcompat.app.AppCompatActivity;
import project.wgtech.sampleapp.view.MainActivity;

public class KakaoSDKApplication extends Application {
    private final static String TAG = "KakaoSDKApplication";

    private static volatile KakaoSDKApplication instance;
    private static volatile AppCompatActivity activity;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static KakaoSDKApplication getInstance() {
        return instance;
    }

    public static AppCompatActivity getActivity() {
        return activity;
    }

    public static void setActivity(AppCompatActivity activity) {
        KakaoSDKApplication.activity = activity;
    }

    // 싱글턴 객체
    public static Context getGlobalApplicationContext() {
        if (instance == null) {
            throw new IllegalStateException("com.kakao.KakaoSDKApplication 상속되지 않음.");
        }
        return instance;
    }


    public void getKakaoAuth(KakaoAdapter adapter) {
        Session s = Session.getCurrentSession();
        s.addCallback(new KakaoSessionCallback());

        if (s.isClosed()) {
            Log.d(TAG, "getKakaoAuth: 세션 닫혀있음.");
            s.open(adapter.getSessionConfig().getAuthTypes()[0], getActivity());
            if (s.isOpened()) {
                Log.d(TAG, "getKakaoAuth: 세션 확인");
                getActivity().finish();
            }
        }

        if (s.isOpened()) {
            Log.d(TAG, "getKakaoAuth: 세션 이미 살아있음.");

            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().startActivity(intent);
        }


    }

    public void loseKakaoAuth() {
        UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                Log.d(TAG, "onCompleteLogout: 로그아웃 성공");
            }
        });

        Session s = Session.getCurrentSession();
        if (s.isOpened()) {
            s.clearCallbacks();
            s.close();
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        loseKakaoAuth();
        instance = null;
    }

}
