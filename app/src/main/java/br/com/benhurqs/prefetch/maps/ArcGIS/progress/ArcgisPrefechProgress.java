package br.com.benhurqs.prefetch.maps.ArcGIS.progress;

import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import br.com.benhurqs.prefetch.R;
import br.com.benhurqs.prefetch.directory.PathManager;
import br.com.benhurqs.prefetch.maps.ArcGIS.ArcGISManager;
import br.com.benhurqs.prefetch.model.TilePoint;
import br.com.benhurqs.prefetch.tiles.TileFinder;
import br.com.benhurqs.prefetch.tiles.TilesUtils;
import br.com.benhurqs.prefetch.util.DownLoadImageTask;
import br.com.benhurqs.prefetch.util.NetworkUtil;

public class ArcgisPrefechProgress extends DialogFragment implements OnClickListener {
	
	
	private View rootView;
	private static double KEY_LATLNG_TOP_X;
	private static double KEY_LATLNG_TOP_Y;
	private static double KEY_LATLNG_BOTTOM_X;
	private static double KEY_LATLNG_BOTTOM_Y;
	private static final int MAX_ZOOM = 5;
	private static final int MIN_ZOOM = 0;
	private static int START_ZOOM = 0;
	
	private static final int PROGRESS_STATUS_SUCCESS = 0 ;
	private static final int PROGRESS_STATUS_NETWORK_FAILURE = 1 ;

	
	private static final String UPDATE_PROGRESS_DIALOG = "br.com.stormsec.raven.prefech.maps.Arcgis.progress.update";
	
	private FechTiles getTiles;
	
	private static int currentZoom = 0;
	private static int currentMaxProgress = 0;
	private static int currentProgress = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_TITLE, STYLE_NORMAL);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.prefech_progress, container,false);
		
		getDialog().setCanceledOnTouchOutside(false);
		setCancelable(false);
		
		KEY_LATLNG_TOP_X = getArguments().getDouble(ArcGISManager.KEY_LATLNG_TOP_X);
		KEY_LATLNG_TOP_Y = getArguments().getDouble(ArcGISManager.KEY_LATLNG_TOP_Y);
		KEY_LATLNG_BOTTOM_X = getArguments().getDouble(ArcGISManager.KEY_LATLNG_BOTTOM_X);
		KEY_LATLNG_BOTTOM_Y = getArguments().getDouble(ArcGISManager.KEY_LATLNG_BOTTOM_Y);
		
		rootView.getContext().registerReceiver( updateDialogStatus, new IntentFilter(UPDATE_PROGRESS_DIALOG));
		
		onViewCreated();
		return rootView;
	}
	
	
	private void onViewCreated(){
		
		Button btnCancel = (Button) rootView.findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(this);
		
		startPrefechTask(MIN_ZOOM);
			
	}

	@Override
	public void onClick(View v) {
		switch (v.getId())
		{
			case R.id.btnCancel:
			{
				stopPrefechTask( );
				this.dismiss();
				break;
			}
			case R.id.btnContinue:
			{
				TextView errorMsg = (TextView) rootView.findViewById(R.id.txt_error);
				errorMsg.setVisibility(View.GONE);
				
				Button btnContiue = (Button) rootView.findViewById(R.id.btnContinue);
				btnContiue.setVisibility(View.GONE);
				
				startPrefechTask( currentZoom );
				
				break;
			}
			case R.id.btnClose:
			{
				dismiss();
				break;
			}

		}

	}
	
	@Override
	public void onPause() 
	{
		super.onPause();
	}
	
	@Override
	public void onDestroy() {
		rootView.getContext().unregisterReceiver(updateDialogStatus);
		super.onDestroy();
	}
	
	
	
	private void updatePrefechProgressTitle(Context ctx)
	{
		TextView titlePrefechZoom = (TextView) rootView.findViewById(R.id.txt_dialog_title);
		titlePrefechZoom.setText(ctx.getString(R.string.prefech_progress_title, currentZoom));
		
		ProgressBar prefechProgress = (ProgressBar) rootView.findViewById(R.id.prefechProgress);
		prefechProgress.setMax(currentMaxProgress);
		prefechProgress.setProgress(currentProgress);		
		
		TextView prefechProgressTxt = (TextView) rootView.findViewById(R.id.prefechProgressTxt);
		prefechProgressTxt.setText(ctx.getString(R.string.prefech_progress_txt_bar, currentProgress,currentMaxProgress));
		
	}
	
	private void updatePrefechNetworkFailure( )
	{
		TextView errorMsg = (TextView) rootView.findViewById(R.id.txt_error);
		errorMsg.setVisibility(View.VISIBLE);
		errorMsg.setText(R.string.prefech_progress_network_error);
		
		Button btnContiue = (Button) rootView.findViewById(R.id.btnContinue);
		btnContiue.setVisibility(View.VISIBLE);
		btnContiue.setOnClickListener(this);
		
	}
	
	
	private void updatePrefechSuccess( )
	{
		
		View prefechSuccessView =  rootView.findViewById(R.id.prefechSuccess);
		prefechSuccessView.setVisibility(View.VISIBLE);
		
		RelativeLayout prefechProgress = (RelativeLayout) rootView.findViewById(R.id.actionBtnLayout);
		prefechProgress.setVisibility(View.GONE);
		
		Button btnClose = (Button) rootView.findViewById(R.id.btnClose);	
		btnClose.setOnClickListener(this);
		
	}
	
	private class FechTiles extends AsyncTask<Integer, Integer, Integer>
	{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground(Integer... params) {
			
			TilesUtils tilesUtils = new TilesUtils();
			TileFinder tileFinder = new TileFinder(tilesUtils);
			
			if( params!= null && params[0] != null  )
			{
				START_ZOOM = params[0];
				
			}
			
			for (int i = START_ZOOM; i <= MAX_ZOOM; i++) 
			{
				currentZoom = i;
				
				Intent intent = new Intent();
				intent.setAction( UPDATE_PROGRESS_DIALOG );
				rootView.getContext().sendBroadcast(intent);
			    		    
				ArrayList<TilePoint> urls = tileFinder.getTileUrlsByLatLngExtent(KEY_LATLNG_TOP_X, KEY_LATLNG_BOTTOM_Y,KEY_LATLNG_BOTTOM_X, KEY_LATLNG_TOP_Y, i);
				currentMaxProgress = urls.size();
				currentProgress = 0;
				for (TilePoint tilePoint : urls) 
				{	
					if( NetworkUtil.isNetworkAvailable(rootView.getContext()) )
					{
						
						if( !fileExist(i, tilePoint.getY(), tilePoint.getX())  ) 
						{
							if( !DownLoadImageTask.fechTile(i, tilePoint.getY(), tilePoint.getX(), rootView.getContext()) )
							{
								return PROGRESS_STATUS_NETWORK_FAILURE;
							}
							
						
						}
						
						currentProgress++;
						rootView.getContext().sendBroadcast(intent);
					

					}
					else
					{	
						return PROGRESS_STATUS_NETWORK_FAILURE;
					}
				}
			}
			
			return PROGRESS_STATUS_SUCCESS;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			
			switch ( result ) 
			{
				case PROGRESS_STATUS_NETWORK_FAILURE:
					updatePrefechNetworkFailure();
					break;
				case PROGRESS_STATUS_SUCCESS:

					updatePrefechSuccess( );
					
					break;
				default:
					break;
			}
			
		}
		
	}
	
	
	 private BroadcastReceiver updateDialogStatus = new BroadcastReceiver() {

	        @Override
	        public void onReceive(Context context, Intent intent) {

	        	updatePrefechProgressTitle(context);

	        }

	    };
	    
	    
	 private boolean fileExist(  int zoom ,  long y ,  long x )
	 {
		 
//		 String tileUrl = getString(R.string.tile_path,zoom,y) +"/"+ x +".jpg";
         String tileUrl = PathManager.getFile("Maps") + "/" + zoom + "/" + y + "/" + x +".jpg";
         File tile = new File(tileUrl);
		 if( tile.exists() )
			 return true;
		 else
			 return false;
		 
	 }
	 
	 private  void startPrefechTask( Integer startZoom )
	 {
			getTiles = new FechTiles();
			getTiles.execute(startZoom);
	 }
	 
	 private void stopPrefechTask()
	 {
		 getTiles.cancel(true);
	 }

	
	

}
