package com.example.gonzalo.schoolapp.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.gonzalo.schoolapp.R;

/**
 * Created by Gonzalo on 21/04/2015.
 */
public class Utilities {

    public static final String TAG = "Utilities";
    private static boolean wifiConnected = false;
    private static boolean mobileConnected = false;
    private static Context myContext;

    //En la actividad que se llama a esta función usarla con this
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

}//class
