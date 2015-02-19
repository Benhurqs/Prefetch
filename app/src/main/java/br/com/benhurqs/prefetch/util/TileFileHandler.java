package br.com.benhurqs.prefetch.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import br.com.benhurqs.prefetch.directory.PathManager;

public class TileFileHandler {
	
	
	public static final String TILE_FILE_PATH = "";
	
	
	   public static boolean storeTile( Bitmap tile ,String zoom, String y , String x , Context ctx)
	    {

	        File pictureFile = getOutputMediaFile(zoom,y,x,ctx);
	        if (pictureFile == null) {
	            Log.d("Erro","Erro diretorio null");// e.getMessage());
	            return false;
	        }
	        try {
	            FileOutputStream fos = new FileOutputStream(pictureFile);
	            tile.compress(Bitmap.CompressFormat.JPEG, 90, fos);
	            fos.close();
	            return true;
	        } catch (FileNotFoundException e) {
	            Log.d("Erro", "File not found: " + e.getMessage());
	            return false;
	        } catch (IOException e) {
	            Log.d("Erro", "Error accessing file: " + e.getMessage());
	            return false;
	        }

	    }
	   
	   
	    public static  File getOutputMediaFile( String zoom ,String y, String x , Context ctx ){


//	        File mediaStorageDir = new File(ctx.getString(R.string.tile_path,zoom,y) );
	        //File mediaStorageDir = new File("/storage/sdcard1/Raven");
            File mediaStorageDir = new File(
                    PathManager.getFile("Maps").getPath() + File.separator + zoom + File.separator + y) ;

	        if (! mediaStorageDir.exists()){
	            if (! mediaStorageDir.mkdirs())
	            {	
	            	Log.e("Error Saving Tile", "Diretorio nao existe");
	                return null;
	            }
	        }

	        String mImageName = x +".jpg";

	        File mediaFile;
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
	        return mediaFile;
	    }

}
