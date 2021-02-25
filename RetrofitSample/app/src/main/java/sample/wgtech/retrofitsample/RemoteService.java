package sample.wgtech.retrofitsample;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RemoteService {

    @GET
    Call<ResponseBody> downloadFileWithDynamicUrl(@Url String url);
}
