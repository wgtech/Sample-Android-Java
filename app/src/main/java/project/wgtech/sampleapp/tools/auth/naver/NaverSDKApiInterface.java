package project.wgtech.sampleapp.tools.auth.naver;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface NaverSDKApiInterface {

    @GET("v1/nid/me/")
    Call<ResponseBody> getUserInfo(@Header("Authorization") String token);

}
