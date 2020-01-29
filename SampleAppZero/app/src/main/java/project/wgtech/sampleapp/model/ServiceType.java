package project.wgtech.sampleapp.model;

import android.net.Uri;

import java.io.File;

import project.wgtech.sampleapp.R;

public enum ServiceType {

    KAKAO("KAKAO"),
    NAVER("NAVER"),
    GOOGLE("GOOGLE");

    private String value;

    ServiceType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getIconPath() {
        if (value.equals(KAKAO.getValue())) {
            return Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + R.drawable.kakaoaccount_icon).toString();
        }

        if (value.equals(NAVER.getValue())) {
            return Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + R.drawable.naver_icon).toString();
        }

        return null;
    }

    @Override
    public String toString() {
        return value;
    }

}
