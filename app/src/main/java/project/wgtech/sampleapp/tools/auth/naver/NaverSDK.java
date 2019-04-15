package project.wgtech.sampleapp.tools.auth.naver;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ObservableArrayMap;
import androidx.databinding.ObservableMap;
import project.wgtech.sampleapp.model.NaverSDKAuthModel;
import project.wgtech.sampleapp.tools.RetrofitBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;

import java.util.HashMap;
import java.util.Map;

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

    public void login(ObservableMap map) {
        getSession().init(context, clientId, clientSecret, clientName);
        getSession().startOauthLoginActivity(activity, new OAuthLoginHandler() {
            @Override
            public void run(boolean success) {
                if (success) {
                    Log.d(TAG, "run: accessToken = " + session.getAccessToken(context));
                    Log.d(TAG, "run: getState = " + session.getState(context).toString());

                    Retrofit client = new RetrofitBuilder().build("https://openapi.naver.com/");

                    NaverSDKApiInterface service = client.create(NaverSDKApiInterface.class);

                    Call<NaverSDKAuthModel> call = service.getUserInfo("Bearer " + session.getAccessToken(context));
                    call.enqueue(new retrofit2.Callback<NaverSDKAuthModel>() {
                        @Override
                        public void onResponse(Call<NaverSDKAuthModel> call, Response<NaverSDKAuthModel> response) {
                            Log.d(TAG, "onResponse: " + response.code());
                            if (response.isSuccessful()) {
                                NaverSDKAuthModel res = response.body();
                                Log.d(TAG, "onResponse: " + res.message + ", " + res.resultCode);
                                NaverSDKAuthModel.Data data = res.data;
                                Log.d(TAG, "onResponse: " + data.id + ", " + data.email);

                                map.put("id", data.id);
                                map.put("email", data.email);
                            }
                        }

                        @Override
                        public void onFailure(Call<NaverSDKAuthModel> call, Throwable t) {
                            Log.d(TAG, "onFailure: " + t.getStackTrace());
                        }
                    });

                } else {
                    Log.d(TAG, "run: " + session.getLastErrorCode(context).getCode());
                    Log.d(TAG, "run: " + session.getLastErrorDesc(context));
                }
            }
        });
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
