package sample.wgtech.retrofitsample;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        RemoteClient client = RemoteClient.getInstance();
        client.getService()
                .downloadFileWithDynamicUrl("https://www.dropbox.com/s/18wz9lsknuca5h0/%ED%8E%B8%EC%A7%91.mp3?dl=1")
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Log.d("wgtech.dev", "response is success: " + response.code());
                            writeResponseBodyToDisk("test.mp3", response.body());

                        } else {
                            Log.d("wgtech.dev", "response is failure: " + response.code());

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d("wgtech.dev", "response is epic failure: " + t.getMessage());
                        t.printStackTrace();
                    }
                });
    }

    ///////////////

    private boolean writeResponseBodyToDisk(String fileName, ResponseBody body) {
        Log.d("wgtech.dev", "writeResponseBodyToDisk");

        try {
            File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + File.separator + fileName);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                file = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + File.separator + fileName);
            }

            String savedFilePath = file.getAbsolutePath();
            Log.d("wgtech.dev", "writeResponseBodyToDisk : " + savedFilePath);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(file);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;
                    String progressText = ((int)(fileSizeDownloaded*100/fileSize)) + "% (" + getNotationCapacity(fileSizeDownloaded) + " / " + getNotationCapacity(fileSize) + ")";
                    Log.d("wgtech.dev", progressText);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    ///////////////

    private String getNotationCapacity(float size) {

        int count = 0;
        while (size / 1000 >= 1) {
            count ++;
            size /= 1000;
        }

        String unit = "";
        switch (count) {
            case 0: default: unit = " Bytes";
                break;
            case 1: unit = " KB";
                break;
            case 2: unit = " MB";
                break;
            case 3: unit = " GB";
                break;
        }
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(size) + unit;
    }

    ///////////////


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}