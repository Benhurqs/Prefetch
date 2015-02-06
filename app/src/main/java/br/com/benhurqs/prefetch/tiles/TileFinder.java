package br.com.benhurqs.prefetch.tiles;

import java.util.ArrayList;

import br.com.benhurqs.prefetch.model.TilePoint;

import com.esri.core.geometry.Point;

/*
 * 
 * Autor: b.quintino
 * 
 * */
public class TileFinder {
	
	private TilesUtils tilesUtils;
	private String url;
	
	public TileFinder(TilesUtils tilesUtils){
		this.tilesUtils = tilesUtils;
		
	}
	
	public TileFinder(TilesUtils tilesUtils, String url){
		this.tilesUtils = tilesUtils;
		this.url = url;
	}
	
	public ArrayList<TilePoint> getTileUrlsByLatLngExtent(double xmin, double ymin, double xmax, double ymax, long zoom){
		/*
		 Returns a list of tile urls by extent
		 */
		
		//Upper-Left Tile
        Point tileXYMin = this.tilesUtils.convertLngLatToTileXY(xmin, ymax, zoom);

        //Lower-Right Tile
        Point tileXYMax = this.tilesUtils.convertLngLatToTileXY(xmax, ymin, zoom);
        
        ArrayList<TilePoint> urlList = new ArrayList<TilePoint>();
        for(int y = (int)tileXYMin.getY(); y < tileXYMax.getY()+1; y++ ){
        	for(int x = (int)tileXYMin.getX() ; x < tileXYMax.getX()+1; x++){
        		//urlList.add(this.createTileUrl(x, y, zoom));
        		urlList.add(new TilePoint(x, y));
        	}
        }
        
        return urlList;
	}
	
	public String createTileUrl(double x, double y, long zoom){
		/*
		 returns new tile url based on template
		 */
		return url + "/" + zoom + "/" + y + "/" + x + ".png"; 
	}
        

}
