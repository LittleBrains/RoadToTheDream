package ru.littlebrains.roadtothedream.core;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.inputmethod.InputMethodManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.util.concurrent.atomic.AtomicInteger;

import trikita.log.Log;

public class Utils {
    private static String STORAGE_NAME = "RD";


    public static SharedPreferences.Editor getSharedPreferencesEditor(
            Context mContext) {
        return mContext
                .getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE)
                .edit();
    }

    public static SharedPreferences getSharedPreferences(Context mContext) {
        return mContext
                .getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE);
    }

    public static boolean isInternetOn(Context mContext) {
        try {
            ConnectivityManager cm =
                    (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                Log.d("", "Internet enable");
                return true;
            }
            Log.d("", "Internet desable");
            return false;
        }catch (Exception e){}
        return true;
    }

    public static void hideKeyboard(Activity activity) {
        Log.d("hideKeyboard");
        try {
            InputMethodManager imm = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);

            imm.hideSoftInputFromWindow(activity.getWindow().getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
        }
    }

    public static void showKeyboard(Context mContext) {
        ((InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE))
                .toggleSoftInput(InputMethodManager.SHOW_FORCED,
                        InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public static String getVersionApp(Activity context){
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
        return pInfo.versionName;
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public static int dptopx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public static int pxtodp(Context context, int px){
        Resources r = context.getResources();
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, r.getDisplayMetrics());
    }

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
    public static int generateViewId() {
        for (;;) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }


    public static String md5(String toEncrypt) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("md5");
            digest.update(toEncrypt.getBytes());
            final byte[] bytes = digest.digest();
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(String.format("%02X", bytes[i]));
            }
            return sb.toString().toLowerCase();
        } catch (Exception exc) {
            return ""; // Impossibru!
        }
    }

    public static Object fromString( String s ){
        Object o = null;
        try {
            byte [] data = Base64.decode(s, Base64.DEFAULT);
            ObjectInputStream ois = new ObjectInputStream(
                    new ByteArrayInputStream(  data ) );

            o  = ois.readObject();
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return o;
    }

    /** Write the object to a Base64 string. */
    public static String toString(Serializable o ){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream( baos );
            oos.writeObject( o );
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
    }
}
