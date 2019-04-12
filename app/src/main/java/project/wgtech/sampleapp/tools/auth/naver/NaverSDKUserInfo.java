package project.wgtech.sampleapp.tools.auth.naver;

import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NaverSDKUserInfo {

    private final static String TAG = NaverSDKUserInfo.class.getSimpleName();

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

        Call<ResponseBody> call = service.getUserInfo(token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        Log.d(TAG, "onResponse: " + response.code() + ", " + response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getStackTrace());
            }
        });
        Log.d(TAG, "getUserInfo: 끝");
    }
}
