package br.com.benhurqs.prefetch.preferences;

import br.com.benhurqs.prefetch.R;
import android.content.Context;
import android.content.SharedPreferences;

public class PointsPreferences {
	
	private static float DEFAULT = -1;
	private Context context;
	private SharedPreferences preferences;
	
	public PointsPreferences(Context ctx, int type){
		this.context = ctx;
		preferences = context.getSharedPreferences(
				context.getString(R.string.point_pref) + type, Context.MODE_PRIVATE);
		
	}
	
	public void saveTopLeft(float x , float y){
		SharedPreferences.Editor editortop = preferences.edit();
		editortop.putFloat(context.getString(R.string.top_left_x), x);
		editortop.putFloat(context.getString(R.string.top_left_y), y);
		editortop.commit();
	}
	
	public void savebottomRight(float x , float y){
		SharedPreferences.Editor editorBottom = preferences.edit();
		editorBottom.putFloat(context.getString(R.string.bottom_right_x), x);
		editorBottom.putFloat(context.getString(R.string.bottom_right_y), y);
		editorBottom.commit();
	}
	
	public boolean isEmpty(){
		return(getBottomRightX() == DEFAULT || getBottomRightY() == DEFAULT || 
				getTopLeftX() == DEFAULT || getTopLeftY() == DEFAULT);
	}
	
	public float getTopLeftX(){
		return preferences.getFloat(context.getString(R.string.top_left_x), DEFAULT);
	}
	
	public float getTopLeftY(){
		return preferences.getFloat(context.getString(R.string.top_left_y), DEFAULT);
	}
	
	public float getBottomRightX(){
		return preferences.getFloat(context.getString(R.string.bottom_right_x), DEFAULT);
	}
	
	public float getBottomRightY(){
		return preferences.getFloat(context.getString(R.string.bottom_right_y), DEFAULT);
	}

}
