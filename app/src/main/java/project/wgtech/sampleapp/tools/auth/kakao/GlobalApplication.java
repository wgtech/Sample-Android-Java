package project.wgtech.sampleapp.tools.auth.kakao;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;

public class GlobalApplication extends Application {
    private final static String TAG = "GlobalApplication";

    private static volatile GlobalApplication instance;
    private static volatile Activity activity;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static GlobalApplication getInstance() {
        return instance;
    }

    public static Activity getActivity() {
        return activity;
    }

    public static void setActivity(Activity activity) {
        GlobalApplication.activity = activity;
    }

    // 싱글턴 객체
    public static Context getGlobalApplicationContext() {
//        if (instance == null) {
//            throw new IllegalStateException("com.kakao.GlobalApplication 상속되지 않음.");
//        }
        return instance;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        instance = null;
    }

}
