package project.wgtech.sampleapp.view;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import project.wgtech.sampleapp.R;
import project.wgtech.sampleapp.model.ServiceType;
import project.wgtech.sampleapp.tools.auth.kakao.KakaoSDKApplication;
import project.wgtech.sampleapp.tools.auth.naver.NaverSDKApplication;

public class OnNavigationHeaderItemClickListener implements View.OnClickListener, DialogInterface.OnClickListener {

    private static String TAG = OnNavigationHeaderItemClickListener.class.getSimpleName();

    private Context context;

    public OnNavigationHeaderItemClickListener(Context context) {
        this.context = context;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_drawer_logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogStyleLight);
                builder.setTitle(R.string.dialog_question_logout)
                        .setPositiveButton(R.string.yes, this)
                        .setNegativeButton(R.string.no, this).show();
                break;

            default:
                Toast.makeText(context, "띠용", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

        if (i == DialogInterface.BUTTON_POSITIVE) {
            try {
                if (context instanceof AppCompatActivity) {
                    AppCompatActivity activity = (AppCompatActivity) context;
                    String serviceType = activity.getIntent().getStringExtra("serviceType");

                    if (serviceType.equals(ServiceType.KAKAO.getValue())) {
                        KakaoSDKApplication.getInstance().loseKakaoAuth();
                    } else if (serviceType.equals(ServiceType.NAVER.getValue())) {
                        NaverSDKApplication.getInstance().logoutAndDeleteToken(activity);
                    }

                    activity.finish();
                } else {
                    throw new IllegalAccessException(context.getString(R.string.exception_not_found_activity_context));
                }
            } catch (IllegalAccessException e) {
                Log.e(TAG, "IllegalAccessException: " + context.getString(R.string.exception_not_found_activity_context));
                e.printStackTrace();
            }
        }

        if (i == DialogInterface.BUTTON_NEGATIVE) {
            dialogInterface.cancel();
        }



    }
}
