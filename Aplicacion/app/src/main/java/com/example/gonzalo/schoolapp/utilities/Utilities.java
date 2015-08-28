package com.example.gonzalo.schoolapp.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.gonzalo.schoolapp.R;
import com.example.gonzalo.schoolapp.clases.Date;
import com.example.gonzalo.schoolapp.clases.Message;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Gonzalo on 21/04/2015.
 */
public class Utilities {

    public static final String TAG = "Utilities";
    private static boolean wifiConnected = false;
    private static boolean mobileConnected = false;
    private static Context myContext;

    //En la actividad que se llama a esta funci&oacute;n usarla con this
    public static boolean haveInternet (Context context) {
        myContext = context;
        ConnectivityManager connMgr =(ConnectivityManager) myContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()) { //Have Internet
            wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            if ((wifiConnected) || (mobileConnected)) {
                Log.i(TAG, "Tiene Internet");
                return true;
            }//if Have Internet
            else {//Don't have Internet
                Log.i(TAG, "No Tiene Internet");
                return false;
            }//else Don't have Internet
        }//if
        return false;
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static String getID(Message message) {
        Date date = message.getDate();
        String id = "" + date.getDay() + date.getMonth() + date.getYear() + date.getHour() +
                date.getMinutes();
        Calendar calendar = Calendar.getInstance();
        id += calendar.SECOND + calendar.MILLISECOND;
        return id;
    }

    //Funciona Bien
    public static boolean isTelephone(String telephone) {
        String expReg = "^([+]\\d{2})?(6|7)\\d{8}$";
        return telephone.matches(expReg);
    }

    //Funciona Bien
    public static boolean isMail(String mail) {
        String expReg = "^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,3})$";
        return mail.matches(expReg);
    }

    //Funciona Bien
    public static boolean areManyClasses(String classroom) {
        String expReg = "^(\\d{1}[A-Z])(,(\\d{1}[A-Z]))*$";
        return classroom.matches(expReg);
    }

    //Funciona Bien
    public static boolean isOneClass(String classroom) {
        String expReg = "^\\d{1}[A-Z]{1}$";
        return classroom.matches(expReg);
    }

    public static String deleteManySpaces (String chain) {
        String tmp = chain.trim();
        return tmp;
    }

    public static void loadLanguage (Activity activity) {
        Context context = activity.getApplicationContext();
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.app_name), context.MODE_PRIVATE);
        String language = preferences.getString(context.getString(R.string.language), Locale.getDefault().toString());

        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;
        activity.getBaseContext().getResources().updateConfiguration(config, activity.getBaseContext().
                getResources().getDisplayMetrics());
        Log.i("Utilities", "Load Language: " + language);
    }

    public static boolean isDNI (String dni) {
        String expReg = "^\\d{8}[A-Z]{1}$";
        return dni.matches(expReg);
    }

    public static boolean isNIE (String dni) {
        String expReg = "^[XYZ]{1}\\d{7}[A-Z]{1}$";
        return dni.matches(expReg);
    }

}//class
