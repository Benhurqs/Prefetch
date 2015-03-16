package br.com.benhurqs.prefetch.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by bernardo on 16/03/15.
 */
public class Utils {

    private static String TAG = "Prefetch";
    private static Boolean debug = true;

    public static void dLog(String msg){
        if(debug){
            Log.e(TAG, msg);
        }
    }


    public static boolean ehVazio(String string) {
        return (string==null || "".equals(string.trim()) || "null".equals(string.toLowerCase()));
    }

//    public static String getURL(Context ctx) {
//        return ctx.getResources().getString(R.string.url);
//    }

    private static ConnectivityManager getConnectivityManager(Context ctx) {
        return (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
    }


    public static boolean isDeviceOnline(Context ctx) {
        ConnectivityManager cm = getConnectivityManager(ctx);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        }
        return ni.isConnected();
    }


}


