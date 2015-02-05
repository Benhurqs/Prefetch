package br.com.benhurqs.prefetch.maps.ArcGIS.progress;

import java.io.File;
import java.io.IOException;

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
import android.widget.TextView;
import br.com.benhurqs.prefetch.R;

public class ArcgisDeleteFiles extends DialogFragment implements OnClickListener {
	
	
	private View rootView;
	private static final int MIN_ZOOM = 0;

	
	private static final int PROGRESS_STATUS_SUCCESS = 0 ;


	
	private static final String UPDATE_PROGRESS_DIALOG = "br.com.stormsec.raven.prefech.maps.Arcgis.delete.progress.update";
	
	private DeleteTiles getTiles;
	
	private static int currentMaxProgress = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_TITLE, STYLE_NORMAL);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.prefech_delete_maps, container,false);
		
		getDialog().setCanceledOnTouchOutside(false);
		setCancelable(false);
		
		
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
		titlePrefechZoom.setText(ctx.getString(R.string.deleting_tiles));
			
		
		TextView deleteTxt = (TextView) rootView.findViewById(R.id.deleteTxt);
		deleteTxt.setText(ctx.getString(R.string.deleted_files_progress, currentMaxProgress) );
		
	}
	

	
	
	
	private class DeleteTiles extends AsyncTask<Integer, Integer, Integer>
	{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground(Integer... params) {
		
			
			
			
			try {
				
				 delete( new File(getString(R.string.maps_path))); 
					
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			
			
			return PROGRESS_STATUS_SUCCESS;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			
			switch ( result ) 
			{

				case PROGRESS_STATUS_SUCCESS:

					dismiss();
					
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
	    
	    

	 
	 private  void startPrefechTask( Integer startZoom )
	 {
			getTiles = new DeleteTiles();
			getTiles.execute(startZoom);
	 }
	 
	 private void stopPrefechTask()
	 {
		 getTiles.cancel(true);
	 }
	 
	 
	  public  void delete(File file)
		    	throws IOException{
		 
		    Intent intent = new Intent();
			intent.setAction( UPDATE_PROGRESS_DIALOG );
		  			
		    	if(file.isDirectory()){
		 
		    		//directory is empty, then delete it
		    		if(file.list().length==0){
		 
		    		   file.delete();
		    		   currentMaxProgress++;
		    		   rootView.getContext().sendBroadcast(intent);
		    		   System.out.println("Directory is deleted : " 
		                                                 + file.getAbsolutePath());
		 
		    		}else{
		 
		    		   //list all the directory contents
		        	   String files[] = file.list();
		 
		        	   for (String temp : files) {
		        	      //construct the file structure
		        	      File fileDelete = new File(file, temp);
		 
		        	      //recursive delete
		        	     delete(fileDelete);
		        	     currentMaxProgress++;
		        	     rootView.getContext().sendBroadcast(intent);
		        	   }
		 
		        	   //check the directory again, if empty then delete it
		        	   if(file.list().length==0){
		           	     file.delete();
		        	     System.out.println("Directory is deleted : " 
		                                                  + file.getAbsolutePath());
		        	     currentMaxProgress++;
		        	     rootView.getContext().sendBroadcast(intent);
		        	   }
		    		}
		 
		    	}else{
		    		//if file, then delete it
		    		file.delete();
		    		System.out.println("File is deleted : " + file.getAbsolutePath());
		    		currentMaxProgress++;
		    		rootView.getContext().sendBroadcast(intent);
		    	}
		    }


	
	

}
