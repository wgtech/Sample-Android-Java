package project.wgtech.sampleapp.viewmodel;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;

import project.wgtech.sampleapp.model.NASAImageRepo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NASAThread extends Thread {

    private static String TAG = NASAThread.class.getSimpleName();

    private Context context;
    private Handler handler;
    private ArrayList<String> dates;
    private NASAImageRepo repo;

    public NASAThread(Context context, Handler handler, ArrayList<String> dates, NASAImageRepo repo) {
        this.context = context;
        this.handler = handler;
        this.dates = dates;
        this.repo = repo;
    }

    @Override
    public void run() {

        Retrofit client = new Retrofit.Builder()
                .baseUrl("https://api.nasa.gov")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NASAImageRepo.NASAApiInterface service = client.create(NASAImageRepo.NASAApiInterface.class);

        for (String date: dates) {
            //Call<NASAImageRepo> call = service.get_NASA_images(date, "LpKgRB6SznKz4CN54wSxXfvlvS1cd0sgUzAxSgH9");
            Call<NASAImageRepo> call = service.get_NASA_images("2019-04-01", "LpKgRB6SznKz4CN54wSxXfvlvS1cd0sgUzAxSgH9");
            call.enqueue(new Callback<NASAImageRepo>() {
                @Override
                public void onResponse(Call<NASAImageRepo> call, Response<NASAImageRepo> response) {
                    if (response.isSuccessful()) {
                        repo = response.body();
                        //Log.d(TAG, "onResponse: \n" + response.raw());

                        if (response.code() == 200) {
                            Log.d(TAG, "onResponse: " + repo.hdurl);
                            Log.d(TAG, "onResponse: " + repo.url);
                            Log.d(TAG, "onResponse: " + repo.title);
                            Log.d(TAG, "onResponse: " + repo.date);

                            Message message = Message.obtain();
                            Bundle bundle = new Bundle();
                            bundle.putString("hdurl", repo.hdurl);
                            bundle.putString("url", repo.url);
                            bundle.putString("title", repo.title);
                            bundle.putString("date", repo.date);
                            message.setData(bundle);
                            handler.sendMessage(message);
                        }
                    }
                }

                @Override
                public void onFailure(Call<NASAImageRepo> call, Throwable t) {
                    Log.d(TAG, "NASA 이미지 불러오기 실패: \n" + t.getMessage());
                    Log.d(TAG, "NASA의 요청 메시지: " + call.request());
                }
            });

        }

    }
}
