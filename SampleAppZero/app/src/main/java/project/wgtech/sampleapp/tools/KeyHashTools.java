package project.wgtech.sampleapp.tools;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.kakao.util.helper.Utility.getPackageInfo;

public class KeyHashTools {
    private static final String TAG = KeyHashTools.class.getSimpleName();

    /**
     * 디버그 앱 키 해쉬
     * @param context
     * @return
     */
    public static String getKeyHashForKakao(final Context context) {
        PackageInfo packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES);
        if (packageInfo == null)
            return null;

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
            } catch (NoSuchAlgorithmException e) {
                Log.w(TAG, "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
        return null;
    }

    /**
     * 릴리즈 앱 키 해쉬
     * keytool -exportcert -alias <release_key_alias> -keystore <release_keystore_path> | openssl sha1 -binary | openssl base64
     */


}
