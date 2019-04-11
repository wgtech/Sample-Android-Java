package project.wgtech.sampleapp.viewmodel;

import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import okhttp3.OkHttpClient;
import project.wgtech.sampleapp.model.NASAImageRepo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NASACardViewModel extends ViewModel {

    private final static String TAG = NASACardViewModel.class.getSimpleName();

    private MutableLiveData<NASAImageRepo> image;
    private ArrayList<NASAImageRepo> repos;
    private MutableLiveData<ArrayList<NASAImageRepo>> images;

    public MutableLiveData<NASAImageRepo> getImage() {
        if (image == null) {
            image = new MutableLiveData<>();
            
            loadImage();
        }
        return image;
    }

    public MutableLiveData<ArrayList<NASAImageRepo>> getImages(ArrayList<String> dates) {
        repos = new ArrayList<>(1);

        if (images == null) {
            images = new MutableLiveData<>();

            loadImages(dates);
        }
        return images;
    }


    /**
     * 자료 하나만 처리하거나, 받은 json 내용이 array 로 받게 될 경우 사용.
     */
    private void loadImage() {

        Retrofit client = new Retrofit.Builder()
                .baseUrl("https://api.nasa.gov")
                .client(new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NASAImageRepo.NASAApiInterface service = client.create(NASAImageRepo.NASAApiInterface.class);
        Call<NASAImageRepo> call = service.get_NASA_images("2019-04-04", "LpKgRB6SznKz4CN54wSxXfvlvS1cd0sgUzAxSgH9");
        call.enqueue(new Callback<NASAImageRepo>() {
            @Override
            public void onResponse(Call<NASAImageRepo> call, Response<NASAImageRepo> response) {
                if (response.isSuccessful()) {

                    if (response.code() == 200) {
                        Log.d(TAG, "onResponse: 완료");
                        image.setValue(response.body());
                        Log.d(TAG, "onResponse: " + image.getValue().hdurl);
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


    /**
     * 하나씩, 단일 객체들로 나오는 결과물들을 모아 ArrayList 로 삽입 후, 처리
     * @param dates
     */
    private void loadImages(ArrayList<String> dates) {

        Retrofit client = new Retrofit.Builder()
                .baseUrl("https://api.nasa.gov")
                .client(new OkHttpClient.Builder()
                        .connectTimeout(1, TimeUnit.MINUTES)
                        .readTimeout(1, TimeUnit.MINUTES)
                        .build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NASAImageRepo.NASAApiInterface service = client.create(NASAImageRepo.NASAApiInterface.class);

        for (String date: dates) {

            Call<NASAImageRepo> call = service.get_NASA_images(date, "LpKgRB6SznKz4CN54wSxXfvlvS1cd0sgUzAxSgH9");
            call.enqueue(new Callback<NASAImageRepo>() {
                @Override
                public void onResponse(Call<NASAImageRepo> call, Response<NASAImageRepo> response) {
                    if (response.isSuccessful()) {

                        if (response.code() == 200) {
                            NASAImageRepo repo = response.body();
                            repos.add(repo);
                            images.setValue(repos);
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
