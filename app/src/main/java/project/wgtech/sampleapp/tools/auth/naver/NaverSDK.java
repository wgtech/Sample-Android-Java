package project.wgtech.sampleapp.tools.auth.naver;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.nhn.android.naverlogin.OAuthLogin;

public class NaverSDK {
    private final static String TAG = NaverSDK.class.getSimpleName();

    private OAuthLogin session;

    private AppCompatActivity activity;
    private Context context;
    private String clientId;
    private String clientSecret;
    private String clientName;

    private String accessToken;

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

    public String getAccessToken() {
        return accessToken;
    }

    public void initLogin() {
        getSession().init(context, clientId, clientSecret, clientName);
        getSession().startOauthLoginActivity(activity, new NaverSDKAuthHandler());
        accessToken = getSession().getAccessToken(context);
    }

    public String getLastErrorDesc() {
        return getSession().getLastErrorDesc(context);
    }

    public String getLastErrorCode() {
        return getSession().getLastErrorCode(context).getCode();
    }

    public void logout() {
        new Runnable() {
            @Override
            public void run() {
                getSession().logoutAndDeleteToken(context);
            }
        };
    }
}
