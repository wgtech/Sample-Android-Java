package project.wgtech.sampleapp.tools;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ImageSenderInterface {

    @Multipart
    @POST("upload/")
    Call<MultipartBody.Part> postImage(@Part MultipartBody.Part body);

}
