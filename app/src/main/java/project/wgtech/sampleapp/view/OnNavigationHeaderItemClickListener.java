package project.wgtech.sampleapp.view;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import project.wgtech.sampleapp.R;

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
                Toast.makeText(context, "로그아웃", Toast.LENGTH_SHORT).show();
                break;

            default:
                Toast.makeText(context, "띠용", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
