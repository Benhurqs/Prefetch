package br.com.benhurqs.prefetch.util;

import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.RelativeLayout;
import br.com.benhurqs.prefetch.preferences.PointsPreferences;

import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;

public class Dimensions {
	
	private float mapWidth;
	private float mapHeight;
	private float rectWidth;
	private float rectHeight;
	
	private float topLeftX, topLeftY, bottomRightX, bottomRightY;
	
	private MapView mMap = null;
	private RelativeLayout mArea =null;
	private PointsPreferences pref;
	
	public Dimensions(MapView map, RelativeLayout area, PointsPreferences pref){
		this.mMap = map;
		this.mArea = area;
		this.pref = pref;
		
		setValues();
	}
	
	private void setValues(){
		ViewTreeObserver mapObserver = mMap.getViewTreeObserver();
		mapObserver.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				
				boolean isEmpty = getHeight() <= 0 || getWidth() <= 0;
				boolean isDifferent = mMap.getHeight() != getHeight() || mMap.getWidth() != getWidth(); 
				
				if( isEmpty || isDifferent ){
					setHeight(mMap.getHeight());
					setWidth(mMap.getWidth());
					setTopLeft();
				}
			}
		});
		
		
		ViewTreeObserver areaObserver = mArea.getViewTreeObserver();
		areaObserver.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				
				boolean isEmpty = getRectHeight() <= 0 || getRectWidth() <= 0;
				boolean isDifferent = mArea.getHeight() != getRectHeight() || mArea.getWidth() != getRectWidth(); 
				
				if( isEmpty || isDifferent ){
					setRectHeight(mArea.getHeight());
					setRectWidth(mArea.getWidth());
					setTopLeft();
				}
			}
		});
	}
	
	public void setTopLeft(){
		setTopLeftX(mArea.getX() - mMap.getX());
		setTopLeftY(mArea.getY() - mMap.getY());
		
		pref.saveTopLeft(getTopLeftX(), getTopLeftY());
		
		setBottomRight();
	}
	
	public void setBottomRight(){
		setBottomRightY(mArea.getHeight() + getTopLeftX());
		setBottomRightX(mArea.getWidth() + getTopLeftY());
		
		pref.savebottomRight(getBottomRightX(), getBottomRightY());
	}
	
	public Point getTopLeft(){
		if(getTopLeftX() > 0 && getTopLeftY() > 0){
			return mMap.toMapPoint(getTopLeftX(), getTopLeftY());
		}
		return null;
	}
	
	public Point getBottomRight(){
		if(getBottomRightX() > 0 && getBottomRightY() > 0){
			return mMap.toMapPoint(getBottomRightX(), getBottomRightY());
		}
		return null;
	}
	
	public void setWidth(float width){
		this.mapWidth = width;
	}
	
	public void setHeight(float height) {
		this.mapHeight = height;
	}

	public float getWidth() {
		return mapWidth;
	}

	public float getHeight() {
		return mapHeight;
	}

	public float getTopLeftX() {
		return topLeftX;
	}

	public void setTopLeftX(float topLeftX) {
		this.topLeftX = topLeftX;
	}

	public float getTopLeftY() {
		return topLeftY;
	}

	public void setTopLeftY(float topLeftY) {
		this.topLeftY = topLeftY;
	}

	public float getBottomRightX() {
		return bottomRightX;
	}

	public void setBottomRightX(float bottomRightX) {
		this.bottomRightX = bottomRightX;
	}

	public float getBottomRightY() {
		return bottomRightY;
	}

	public void setBottomRightY(float bottomRightY) {
		this.bottomRightY = bottomRightY;
	}

	public float getRectWidth() {
		return rectWidth;
	}

	public void setRectWidth(float rectWidth) {
		this.rectWidth = rectWidth;
	}

	public float getRectHeight() {
		return rectHeight;
	}

	public void setRectHeight(float rectHeight) {
		this.rectHeight = rectHeight;
	}
	
	

}
