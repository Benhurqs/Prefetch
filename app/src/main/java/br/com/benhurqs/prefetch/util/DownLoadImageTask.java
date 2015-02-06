package br.com.benhurqs.prefetch.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import br.com.benhurqs.prefetch.R;

public class DownLoadImageTask  {



	public static class DownLoadImage extends AsyncTask<String, Bitmap, Bitmap> {

		private static final String TAG = "JSONTask";
		private DownloadImageTaskHandler taskHandler;

		@Override
		protected Bitmap doInBackground(String... params) {
			return getBitmapFromURL(params[0]);
		}



		protected void onPostExecute(Bitmap result) {
			if (result != null) {
				try {
					taskHandler.taskSuccessful(result);
				} catch (Exception e) {
					Log.e(TAG, "Erro ao fazer download.");
					e.printStackTrace();
				}
			} else {
				taskHandler.taskFailed( );
			}
		}


		public void setTaskHandler(DownloadImageTaskHandler taskHandler) {
			this.taskHandler = taskHandler;
		}


	}


	public static Bitmap getBitmapFromURL(String src) {
		try 
		{
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);

			return myBitmap;

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}


	public static interface DownloadImageTaskHandler {
		void taskSuccessful( Bitmap result ) ;

		void taskFailed( );
	}




	public static boolean fechTile(  final int zoom , final long y , final long x , final Context ctx ) {
		String tileUrl = ctx.getString( R.string.tile_url,  ctx.getString(R.string.host_name),zoom,y,x ) ;

		try {

			Bitmap tileBmp = DownLoadImageTask.getBitmapFromURL(tileUrl);
			if( tileBmp != null ){
				return  TileFileHandler.storeTile( tileBmp , String.valueOf(zoom) , String.valueOf(y) , String.valueOf(x) , ctx );
			}else{
				return false;
			}

		} catch (Exception e) {
			return false;
		}




	}





}
