package project.wgtech.sampleapp.tools;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class UriFilePathConverter {

    private final static String TAG = UriFilePathConverter.class.getSimpleName();

    private Context context;

    public UriFilePathConverter(Context context) {
        this.context = context;
    }

    public String getPathFromUri(Uri uri){
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null );
        cursor.moveToNext();
        String path = cursor.getString( cursor.getColumnIndex( "_data" ) );
        cursor.close();

        return path;
    }

    /**
     * File 경로 -> Uri <br>
     * 테스트 필요
     * @param filePath
     * @return
     */
    public Uri getUriFromPath(String filePath) {
        Cursor cursor = context.getContentResolver().query( MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, "_data = '" + filePath + "'", null, null );
        cursor.moveToNext();
        int id = cursor.getInt( cursor.getColumnIndex( "_id" ) );
        Uri uri = ContentUris.withAppendedId( MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id );

        return uri;
    }


}
