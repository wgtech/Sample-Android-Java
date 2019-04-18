package project.wgtech.sampleapp.view;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback2 {
    private final static String TAG = CameraSurfaceView.class.getSimpleName();

    private CameraManager manager;

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void surfaceRedrawNeeded(SurfaceHolder holder) {

    }

    @Override
    public void surfaceRedrawNeededAsync(SurfaceHolder holder, Runnable drawingFinished) {

    }
}