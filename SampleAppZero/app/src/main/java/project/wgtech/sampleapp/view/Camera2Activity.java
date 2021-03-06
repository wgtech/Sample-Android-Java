package project.wgtech.sampleapp.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;
import android.util.SparseArray;
import android.view.Display;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import project.wgtech.sampleapp.R;
import project.wgtech.sampleapp.databinding.ActivityCamera2Binding;
import project.wgtech.sampleapp.tools.Constants;
import project.wgtech.sampleapp.tools.ImageSenderInterface;
import project.wgtech.sampleapp.tools.PermissionsActivity;
import project.wgtech.sampleapp.tools.RetrofitBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Camera2Activity extends AppCompatActivity implements TextureView.SurfaceTextureListener, ImageReader.OnImageAvailableListener {

    private final static String TAG = Camera2Activity.class.getSimpleName();

    private ActivityCamera2Binding binding;

    private Context context;

    private CameraManager manager;
    private CameraDevice camera;
    private CameraCharacteristics cc;
    private CameraCaptureSession session;
    private CaptureRequest.Builder reqBuilder;
    private ImageReader reader;

    private Surface surface;
    private ArrayList<Surface> surfaces;

    private boolean isPreview;

    // Orientation
    private static final SparseArray ORIENTATIONS = new SparseArray(4);
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = Camera2Activity.this;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d(TAG, "onCreate: Above M Version");
            String[] permissions = new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };

            Intent perm = new Intent(this, PermissionsActivity.class);
            perm.putExtra("permissions", permissions);

            startActivityForResult(perm, Constants.PERMISSIONS_REQUEST);
        } else {
            Log.d(TAG, "onCreate: Under M Version");
            initActivity();
        }
    }

    private void initActivity() {
        Log.d(TAG, "initActivity: ");
        manager = (CameraManager) context.getSystemService(CAMERA_SERVICE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_camera2);
        binding.setActivity(this);
        binding.tevCameraPreview.setSurfaceTextureListener(this);

        // Camera Rotation
        //binding.tevCameraPreview.setTransform(getCameraMatrix());
    }

    private void configureTransform(int viewWidth, int viewHeight) {

        int rotation = getWindowManager().getDefaultDisplay().getRotation();

        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, viewHeight, viewWidth);

        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();

        if (rotation == Surface.ROTATION_90
                || rotation == Surface.ROTATION_270) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);

            float scale = Math.max(
                (float) viewHeight / binding.tevCameraPreview.getHeight(),   /* mPreviewSize.getHeight() */
                (float) viewWidth / binding.tevCameraPreview.getWidth()    /* mPreviewSize.getWidth() */
            );

            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        } else if (rotation == Surface.ROTATION_180) {
            matrix.postRotate(180, centerX, centerY);
        }
        binding.tevCameraPreview.setTransform(matrix);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 권한
        if (resultCode == Constants.PERMISSIONS_RESPONSE_OK) {
            Log.d(TAG, "onActivityResult: 권한 획득 성공 후 카메라 실행");
            initActivity();
        }

        else if (resultCode == Constants.PERMISSIONS_RESPONSE_FAIL) {
            // 권한 없음
            setResult(Constants.PERMISSIONS_RESPONSE_FAIL);
            finish();
        }

        // 카메라
        if (resultCode == Constants.CAMERA_PIC_OK) {
            // 저장
            Log.d(TAG, "onActivityResult: " + data.getData().getPath());
            setResult(resultCode);
            sendBroadcast(data);
        }
    }

    ////////////////////////////////////////////////////////////////////////

    @SuppressLint("MissingPermission")
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        Log.d(TAG, "onSurfaceTextureAvailable: ");

        try {
            for (String camId: manager.getCameraIdList()) {
                cc = manager.getCameraCharacteristics(camId);
                StreamConfigurationMap map = cc.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

                Size size = map.getOutputSizes(SurfaceTexture.class)[0];
                Log.d(TAG, "onSurfaceTextureAvailable: " + size.getWidth() + ", " + size.getHeight());

                configureTransform(binding.tevCameraPreview.getWidth(), binding.tevCameraPreview.getHeight());

                // ImageReader 생성
                reader = ImageReader.newInstance(1920, 1080, ImageFormat.JPEG, 1); // maxImages =1 : ImageReader.acquireNextImage() 사용 권장
                reader.setOnImageAvailableListener(this, null);

                // surfaces 구현
                surfaceTexture.setDefaultBufferSize(1440, 1080);
                surface = new Surface(surfaceTexture);

                surfaces = new ArrayList<>();
                surfaces.add(reader.getSurface());
                surfaces.add(surface);

                manager.openCamera(camId, new CameraDevice.StateCallback() {
                    @Override
                    public void onOpened(@NonNull CameraDevice c) {
                        Log.d(TAG, "onOpened: ");
                        camera = c;

                        try {
                            reqBuilder = camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                            reqBuilder.addTarget(surface);
                            reqBuilder.set(CaptureRequest.JPEG_ORIENTATION,
                                    getJpegOrientation(cc, getWindowManager().getDefaultDisplay().getRotation())
                            );
                            reqBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_AF_MODE_AUTO);

                            camera.createCaptureSession(
                                    surfaces,
                                    new CameraCaptureSession.StateCallback() {
                                        @Override
                                        public void onConfigured(@NonNull CameraCaptureSession s) {
                                            Log.d(TAG, "onConfigured: ");
                                            session = s;
                                            startPreview();
                                        }

                                        @Override
                                        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                                            Log.d(TAG, "onConfigureFailed: ");
                                        }
                                    },
                                    null
                            ); // camera.createCaptureSession

                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onDisconnected(@NonNull CameraDevice c) {
                        Log.d(TAG, "onDisconnected: ");
                        camera.close();
                        camera = null;
                    }

                    @Override
                    public void onError(@NonNull CameraDevice camera, int error) {
                        Log.d(TAG, "onError: " + error);
                    }
                }, null);
            }

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private int getJpegOrientation(CameraCharacteristics c, int deviceOrientation) {
        // Sensor Orientation, Camera Module/Device Orientation

        if (deviceOrientation == android.view.OrientationEventListener.ORIENTATION_UNKNOWN) return 0;
        int sensorOrientation = c.get(CameraCharacteristics.SENSOR_ORIENTATION);
        Log.d(TAG, "getJpegOrientation: deviceOrientation " + deviceOrientation);
        Log.d(TAG, "getJpegOrientation: sensorOrientation " + sensorOrientation);

        // Round device orientation to a multiple of 90
        //deviceOrientation = (deviceOrientation + 45) / 90 * 90;
        deviceOrientation = ((int) ORIENTATIONS.get(deviceOrientation) + 45) / 90 * 90;

        // Reverse device orientation for front-facing cameras
        boolean facingFront = c.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT;
        if (facingFront) deviceOrientation = -deviceOrientation;

        // Calculate desired JPEG orientation relative to camera orientation to make
        // the image upright relative to the device orientation

        Log.d(TAG, "getJpegOrientation: deviceOrientation (2) " + deviceOrientation);
        Log.d(TAG, "getJpegOrientation: sensorOrientation (2) " + sensorOrientation);

        int jpegOrientation = (sensorOrientation + deviceOrientation + 360) % 360; // 우 버튼
        //int jpegOrientation = (sensorOrientation + deviceOrientation + 180) % 360; // 좌 버튼

        Log.d(TAG, "getJpegOrientation: jpegOrientation " + jpegOrientation);
        Log.d(TAG, "getJpegOrientation: ==================");


        return jpegOrientation;
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        Log.d(TAG, "onSurfaceTextureSizeChanged: ");
        //configureTransform(binding.tevCameraPreview.getWidth(), binding.tevCameraPreview.getHeight());
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        Log.d(TAG, "onSurfaceTextureDestroyed: ");
        if (reader != null) reader.close();
        if (session != null) session.close();
        if (camera != null) camera.close();

        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    ////////////////////////////////////////////////////////////////////////

    @Override
    public void onImageAvailable(ImageReader reader) {
        Image img = reader.acquireNextImage();

        if (img != null) {
            if (reader.getImageFormat() == ImageFormat.JPEG) {
                ByteBuffer buffer = img.getPlanes()[0].getBuffer();
                byte[] bytes = new byte[buffer.capacity()];
                buffer.get(bytes);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                Log.d(TAG, "onImageAvailable: 저장 준비 " + baos.toByteArray().length + " bytes.");

                // 저장
                save(baos.toByteArray(), System.currentTimeMillis());

                // 업로드
                //upload(System.currentTimeMillis());

                bitmap.recycle();
            }

            img.close();
        }
    }

    ////////////////////////////////////////////////////////////////////////

    private void startPreview() {
        isPreview = true;
        try {
            reqBuilder.removeTarget(reader.getSurface());
            reqBuilder.addTarget(surface);
            session.setRepeatingRequest(
                reqBuilder.build(),
                new CameraCaptureSession.CaptureCallback() {
                    @Override
                    public void onCaptureStarted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, long timestamp, long frameNumber) {
                        super.onCaptureStarted(session, request, timestamp, frameNumber);
                    }

                    @Override
                    public void onCaptureFailed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureFailure failure) {
                        super.onCaptureFailed(session, request, failure);
                    }
                }, null
            );
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void stopPreview() {
        isPreview = false;
        try {
            session.stopRepeating();
            reqBuilder.removeTarget(surface);
            reqBuilder.addTarget(reader.getSurface());
            session.capture(reqBuilder.build(), new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                    Log.d(TAG, "onCaptureCompleted: 캡쳐 완료");
                    super.onCaptureCompleted(session, request, result);
                }

                @Override
                public void onCaptureFailed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureFailure failure) {
                    super.onCaptureFailed(session, request, failure);
                }
            }, null);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void save(byte[] bytes, long timestamp) {
        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()
                + "/WGSampleApp/");

        try {
            if (!dir.exists()) {
                dir.mkdirs();
                Log.d(TAG, "save: Directory created");
            }

            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATE_TAKEN, timestamp);
            values.put(MediaStore.Images.Media.TITLE, timestamp);
            values.put(MediaStore.Images.Media.DISPLAY_NAME, timestamp +".jpg");
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.Images.Media.SIZE, bytes.length);
            values.put("_data", dir.getAbsolutePath() + "/" + timestamp + ".jpg");

            Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            OutputStream os = getContentResolver().openOutputStream(uri);
            os.write(bytes);
            os.flush();
            os.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void upload(long timestamp) {
        Retrofit imgSender = new RetrofitBuilder().build(getString(R.string.server_ipv4));
        ImageSenderInterface service = imgSender.create(ImageSenderInterface.class);

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()
                + "/WGSampleApp/" + "/" + timestamp + ".jpg"
        );
        Log.d(TAG, "clickOk: " + file.getAbsolutePath());

        RequestBody requestImg = RequestBody.create(MediaType.parse("Content-type: multipart/formed-data"), file);
        Call<MultipartBody.Part> call = service.postImage(
                MultipartBody.Part.createFormData("image", file.getName(), requestImg)
        );
        call.enqueue(new Callback<MultipartBody.Part>() {
            @Override
            public void onResponse(Call<MultipartBody.Part> call, Response<MultipartBody.Part> response) {
                Log.d(TAG, "onResponse: 성공");
            }

            @Override
            public void onFailure(Call<MultipartBody.Part> call, Throwable t) {
                Log.d(TAG, "onFailure: 실패 " + t.getMessage());
            }
        });
    }

    ////////////////////////////////////////////////////////////////////////

    public void clickLensChange(View view) {
        Toast.makeText(context, "전후 반전", Toast.LENGTH_SHORT).show();
    }

    public void clickCapture(View view) {
        if (isPreview) {
            Toast.makeText(context, "저장", Toast.LENGTH_SHORT).show();
            stopPreview();

        } else {
            Toast.makeText(context, "촬영 재개", Toast.LENGTH_SHORT).show();
            startPreview();
        }

    }

}