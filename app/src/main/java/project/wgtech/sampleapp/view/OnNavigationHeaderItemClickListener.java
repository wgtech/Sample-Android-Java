package project.wgtech.sampleapp.view;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import project.wgtech.sampleapp.R;
import project.wgtech.sampleapp.model.ServiceType;
import project.wgtech.sampleapp.tools.auth.kakao.KakaoSDKApplication;
import project.wgtech.sampleapp.tools.auth.naver.NaverSDKApplication;

public class OnNavigationHeaderItemClickListener implements View.OnClickListener {

    private static String TAG = OnNavigationHeaderItemClickListener.class.getSimpleName();

    private Context context;

    public OnNavigationHeaderItemClickListener(Context context) {
        this.context = context;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_drawer_logout:
                if (context instanceof AppCompatActivity) {
                    AppCompatActivity activity = (AppCompatActivity) context;
                    String serviceType = activity.getIntent().getStringExtra("serviceType");
                    if (serviceType.equals(ServiceType.KAKAO.getValue())) {
                        KakaoSDKApplication.getInstance().loseKakaoAuth();
                        activity.finish();
                    } else if (serviceType.equals(ServiceType.NAVER.getValue())) {
                        NaverSDKApplication.getInstance().logoutAndDeleteToken(activity);
                        activity.finish();
                    }
                } else {
                    Log.d(TAG, "onClick: 알 수 없는 오류 발생");
                }
                break;

            default:
                Toast.makeText(context, "띠용", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
