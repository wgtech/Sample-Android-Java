package project.wgtech.sampleapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NaverSDKAuthModel {

    @SerializedName("resultcode")
    public String resultCode;

    @SerializedName("message")
    public String message;

    @SerializedName("response")
    public Data data;

    public class Data {
        @SerializedName("email")
        public String email;

        @SerializedName("nickname")
        public String nickname;

        @SerializedName("profile")
        public String profile;

        @SerializedName("age")
        public String age;

        @SerializedName("id")
        public String id;

        @SerializedName("name")
        public String name;

        @SerializedName("birthday")
        public String birthday;

    }
}
