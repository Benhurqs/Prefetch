package br.com.benhurqs.prefetch.util;

import android.content.Context;
import android.os.StatFs;
import br.com.benhurqs.prefetch.R;

public class SdHealthCheck 
{
	public static final long ALARM_LOW_FREE_MEMO = 3000 ;
	
	public static long getFreeSpace( Context ctx )
	{
		StatFs stat = new StatFs( ctx.getString(R.string.sd_path) );
		@SuppressWarnings("deprecation")
		long bytesAvailable = (long)stat.getBlockSize() *(long)stat.getBlockCount();
		long megAvailable = bytesAvailable / 1048576;
		//Log.e("SD SPACE", ""+ megAvailable + "==" +Environment.getExternalStorageDirectory().getUsableSpace() );
		
		return megAvailable;
	}

	

}
