package project.wgtech.sampleapp.tools.auth.naver;

import android.util.Log;

import com.nhn.android.naverlogin.OAuthLogin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import project.wgtech.sampleapp.model.NaverSDKAuthModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NaverSDKUserInfo {

    private final static String TAG = NaverSDKUserInfo.class.getSimpleName();

    private OAuthLogin session;

    public NaverSDKUserInfo(OAuthLogin session) {
        this.session = session;
    }

    public void getUserInfo(String token) {
        Retrofit client = new Retrofit.Builder()
                .client(new OkHttpClient.Builder()
                        .connectTimeout(1, TimeUnit.MINUTES)
                        .callTimeout(1, TimeUnit.MINUTES)
                        .build())
                .baseUrl("https://openapi.naver.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NaverSDKApiInterface service = client.create(NaverSDKApiInterface.class);

        Call<NaverSDKAuthModel> call = service.getUserInfo("Bearer " + token);
        call.enqueue(new Callback<NaverSDKAuthModel>() {
            @Override
            public void onResponse(Call<NaverSDKAuthModel> call, Response<NaverSDKAuthModel> response) {
                Log.d(TAG, "onResponse: " + response.code());
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: " + response.body().message + ", " + response.body().resultCode);
                    NaverSDKAuthModel.Data data = response.body().data;
                    Log.d(TAG, "onResponse: " + data.id + ", " + data.email);
                }
            }

            @Override
            public void onFailure(Call<NaverSDKAuthModel> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getStackTrace());
            }
        });
        Log.d(TAG, "getUserInfo: 끝");
    }
}
