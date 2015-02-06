package br.com.benhurqs.prefetch.preferences;

import com.esri.android.map.MapOptions;

import br.com.benhurqs.prefetch.R;
import br.com.benhurqs.prefetch.util.MapTypeUtil;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MapsPreferences {
	
	private Context context;
	private SharedPreferences preferences;
	
	public MapsPreferences(Context ctx){
		this.context = ctx;
		preferences = PreferenceManager
			    .getDefaultSharedPreferences(ctx);
		
	}
	
	public void saveMapType(int mapType){
		SharedPreferences.Editor editortop = preferences.edit();
		editortop.putString(context.getString(R.string.map_pref), String.valueOf(mapType));
		editortop.commit();
	}
	
	public MapOptions getMapType(){
		String mapType = preferences.getString(context.getString(R.string.map_pref), String.valueOf(MapTypeUtil.SATELLITE));
		return MapTypeUtil.getMapType(Integer.valueOf(mapType));
	}

}
