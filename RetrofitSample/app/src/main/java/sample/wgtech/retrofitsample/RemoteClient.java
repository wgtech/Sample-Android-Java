package sample.wgtech.retrofitsample;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

public class RemoteClient {
    private Retrofit module;
    private RemoteService service;

    private static class Holder {
        private static final RemoteClient instance = new RemoteClient();
    }

    public static RemoteClient getInstance() {
        return Holder.instance;
    }

    private RemoteClient() {
        if (module == null) {
            module = new Retrofit.Builder()
                    .baseUrl("https://www.dropbox.com")
                    .client(new OkHttpClient.Builder()
                            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                            .callTimeout(2, TimeUnit.MINUTES)
                            .writeTimeout(2, TimeUnit.MINUTES)
                            .readTimeout(2, TimeUnit.MINUTES)
                            .callTimeout(2, TimeUnit.MINUTES)
                            .build())
                    .build();
        }
    }

    public RemoteService getService() {
        if (service == null) {
            service = module.create(RemoteService.class);
        }
        return service;
    }
}
