package br.com.benhurqs.prefetch.directory;

import android.os.Environment;

import java.io.File;

/**
 * Created by benhur on 18/02/15.
 */
public class PathManager {

    public static File directory;
    public static String PATH = "/Prefetch";

    public static  File getPrefetchFile(){
        init();
        return directory;
    }

    public static String getPrefetchPath(){
        if(Environment.getExternalStorageState() == null){
            return Environment.getDataDirectory()
                    + PATH;
        }
        return  Environment.getExternalStorageDirectory()
                + PATH;
    }


    public static void init(){
        //if there is no SD card, create new directory objects to make directory on device
        if (Environment.getExternalStorageState() == null) {
            directory = new File(Environment.getDataDirectory()
                    + PATH);

            // if no directory exists, create new directory
            if (!directory.exists()) {
                directory.mkdir();
            }


            // if phone DOES have sd card
        } else if (Environment.getExternalStorageState() != null) {
            // search for directory on SD card
            directory = new File(Environment.getExternalStorageDirectory()
                    + PATH);

            // if no directory exists, create new directory to store test
            // results
            if (!directory.exists()) {
                directory.mkdir();
            }
        }// end of SD card checking
    }

    public static boolean deleteFolder(String folder){
        init();

        File deletDirectory = new File(getPrefetchPath()
                + File.pathSeparator + folder);

        if (deletDirectory.exists()) {
            File[] dirFiles = deletDirectory.listFiles();
            if (dirFiles.length != 0) {
                for (int ii = 0; ii <= dirFiles.length; ii++) {
                    if(!dirFiles[ii].delete()){
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public static File getFile(String folder){
        init();
        File file = new File(getPrefetchPath()
                + "/" + folder);

        // if no directory exists, create new directory to store test
        // results
        if (!file.exists()) {
            file.mkdir();
        }

        return file;

    }

    public static String[] getMaps(){
        init();
        return getPrefetchFile().list();
    }

    public static String getMapName(int position){
        init();
        if(getPrefetchFile().list().length > position){
            return getPrefetchFile().list()[position];
        }

        return null;
    }

}
