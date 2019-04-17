package project.wgtech.sampleapp.tools.auth.naver;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ObservableMap;
import project.wgtech.sampleapp.model.NaverSDKAuthModel;
import project.wgtech.sampleapp.tools.RetrofitBuilder;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;

public class NaverSDKApplication {
    private final static String TAG = NaverSDKApplication.class.getSimpleName();

    private static volatile OAuthLogin instance;

    private AppCompatActivity activity;
    private Context context;
    private String clientId;
    private String clientSecret;
    private String clientName;

    public NaverSDKApplication(AppCompatActivity activity, String clientId, String clientSecret, String clientName) {
        this.activity = activity;
        this.context = activity.getBaseContext();
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.clientName = clientName;
        this.instance = OAuthLogin.getInstance();
    }

    public static OAuthLogin getInstance() {
        return instance;
    }

    public void login(ObservableMap map) {
        getInstance().init(context, clientId, clientSecret, clientName);
        getInstance().startOauthLoginActivity(activity, new OAuthLoginHandler() {
            @Override
            public void run(boolean success) {
                if (success) {
                    Log.d(TAG, "run: accessToken = " + getInstance().getAccessToken(context));
                    Log.d(TAG, "run: getState = " + getInstance().getState(context).toString());

                    Retrofit client = new RetrofitBuilder().build("https://openapi.naver.com/");

                    NaverSDKApiInterface service = client.create(NaverSDKApiInterface.class);

                    Call<NaverSDKAuthModel> call = service.getUserInfo("Bearer " + getInstance().getAccessToken(context));
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
                    Log.d(TAG, "run: " + getInstance().getLastErrorCode(context).getCode());
                    Log.d(TAG, "run: " + getInstance().getLastErrorDesc(context));
                }
            }
        });
    }

    public void logout() {
        new Runnable() {
            @Override
            public void run() {
                getInstance().logoutAndDeleteToken(context);
            }
        };
    }

}
