package project.wgtech.sampleapp.tools.auth.naver;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;

public class NaverSDK {
    private final static String TAG = NaverSDK.class.getSimpleName();

    private OAuthLogin session;

    private AppCompatActivity activity;
    private Context context;
    private String clientId;
    private String clientSecret;
    private String clientName;

    public NaverSDK(AppCompatActivity activity, String clientId, String clientSecret, String clientName) {
        this.activity = activity;
        this.context = activity.getBaseContext();
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.clientName = clientName;
    }

    public OAuthLogin getSession() {
        return session;
    }

    public void setSession() {
        this.session = OAuthLogin.getInstance();
    }

    public void initLogin() {
        getSession().init(context, clientId, clientSecret, clientName);
        getSession().startOauthLoginActivity(activity, new NaverSDKHandler());
    }

    public void logout() {
        new Runnable() {
            @Override
            public void run() {
                getSession().logoutAndDeleteToken(context);
            }
        };
    }

    class NaverSDKHandler extends OAuthLoginHandler {
        private final String TAG = NaverSDKHandler.class.getSimpleName();

        @Override
        public void run(boolean success) {
            if (success) {
                Log.d(TAG, "run: accessToken = " + session.getAccessToken(context));
                Log.d(TAG, "run: getState = " + session.getState(context).toString());
                new NaverSDKUserInfo(session).getUserInfo(session.getAccessToken(context));
            } else {
                Log.d(TAG, "run: " + session.getLastErrorCode(context).getCode());
                Log.d(TAG, "run: " + session.getLastErrorDesc(context));
            }

        }
    }
}
