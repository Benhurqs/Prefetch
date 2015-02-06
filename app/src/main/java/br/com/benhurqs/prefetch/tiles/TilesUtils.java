package br.com.benhurqs.prefetch.tiles;



import com.esri.core.geometry.Point;


/*
 * 
 * Autor: b.quintino
 * 
 * */

public class TilesUtils {

	private static double earth_radius = 6378137;
	private static double min_lat = -85.05112878;
	private static double max_lat = 85.05112878;
	private static double min_lng = -180;
	private static double max_lng = 180;

	public double clipValue(double value, double minValue, double maxValue){
		/*
    Makes sure that value is within a specific range.
    If not, then the lower or upper bounds is returned
		 */
		return Math.min(Math.max(value, minValue), maxValue);
	}

	public double getMapDimensionsByZoomLevel(long zoomLevel){
		/*
    	 Returns the width/height in pixels of the entire map
        based on the zoom level.
		 */
		return 256 << zoomLevel;
	}

	public double getGroundResolution(double latitude, long level){
		/*
        returns the ground resolution for based on latitude and zoom level.
		 */
		latitude = this.clipValue(latitude, min_lat, max_lat);
		double mapSize = this.getMapDimensionsByZoomLevel(level);
		return Math.cos(latitude * Math.PI / 180) * 2 * Math.PI * earth_radius / mapSize;
	}

	public double getMapScale(double latitude, long level, double dpi){
		/*
		 returns the map scale on the dpi of the screen
		 */
		double dpm = dpi / 0.0254; //convert to dots per meter
		return this.getGroundResolution(latitude, level) * dpm;
	}


	public Point convertLatLngToPixelXY(double lat, double lng, long level){
		/*
        returns the x and y values of the pixel corresponding to a latitude and longitude.
        */
		
        double mapSize = this.getMapDimensionsByZoomLevel(level);
       
        lat = this.clipValue(lat, min_lat, max_lat);
        lng = this.clipValue(lng, min_lng, max_lng);

        double x = (lng + 180) / 360;
        double sinlat = Math.sin(lat * Math.PI / 180);
        double y = 0.5 - Math.log((1 + sinlat) / (1 - sinlat)) / (4 * Math.PI);

        int pixelX = (int)(this.clipValue(x * mapSize + 0.5, 0, mapSize - 1));
        int pixelY = (int)(this.clipValue(y * mapSize + 0.5, 0, mapSize - 1));
        return new Point(pixelX, pixelY);
	}
	
	public Point convertPixelXYToTileXY(double pixelX, double pixelY){
        /*
        Converts pixel XY coordinates into tile XY coordinates of the tile containing
        */
        return new Point((int)(pixelX / 256), (int)(pixelY / 256));
	}
	
	public Point convertLngLatToTileXY(double lng, double lat, long level){
        Point pixelXY = this.convertLatLngToPixelXY(lat, lng, level);
        return this.convertPixelXYToTileXY(pixelXY.getX(), pixelXY.getY());
	}
	
	public Point convertPixelXYToLngLat(double pixelX, double pixelY, long level){
        /*
        converts a pixel x, y to a latitude and longitude.
        */
		
        double mapSize = this.getMapDimensionsByZoomLevel(level);
        double x = (this.clipValue(pixelX, 0, mapSize - 1) / mapSize) - 0.5;
        double y = 0.5 - (this.clipValue(pixelY, 0, mapSize - 1) / mapSize);

        double lat = 90 - 360 * Math.atan(Math.exp(-y * 2 * Math.PI)) / Math.PI;
        double lng = 360 * x;

        return new Point(lng, lat);
	}



}
