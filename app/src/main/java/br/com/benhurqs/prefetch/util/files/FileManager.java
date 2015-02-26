package br.com.benhurqs.prefetch.util.files;

/**
 * Created by Benhur on 19/02/15.
 */

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.os.Environment;

public class FileManager {

    /**
     * Timestamp for logs in the Mission Planner Format
     */
    static public String getTimeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss",
                Locale.US);
        String timeStamp = sdf.format(new Date());
        return timeStamp;
    }

    public static boolean isExternalStorageAvaliable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    private static long getFolderSize(File dir) {
        long size = 0;
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                size += file.length();
            } else {
                size += getFolderSize(file);
            }
        }
        return size;
    }

    public static String getSize(File dir){
        long size = getFolderSize(dir);
        String hrSize = "";
        double m = size/1024.0;
        DecimalFormat dec = new DecimalFormat("0.00");

        if (m > 1) {
            hrSize = dec.format(m).concat(" MB");
        } else {
            hrSize = dec.format(size).concat(" KB");
        }
        return "(" + hrSize + ")";
    }

}
