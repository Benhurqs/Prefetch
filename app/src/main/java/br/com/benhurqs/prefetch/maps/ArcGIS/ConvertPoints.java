package br.com.benhurqs.prefetch.maps.ArcGIS;

import com.esri.core.geometry.Point;


public final class ConvertPoints 
{
    private final int TILE_SIZE = 256;
    private Point _pixelOrigin;
    private double _pixelsPerLonDegree;
    private double _pixelsPerLonRadian;

    public ConvertPoints()
    {
        this._pixelOrigin = new Point(TILE_SIZE / 2.0,TILE_SIZE / 2.0);
        this._pixelsPerLonDegree = TILE_SIZE / 360.0;
        this._pixelsPerLonRadian = TILE_SIZE / (2 * Math.PI);
    }

    double bound(double val, double valMin, double valMax)
    {
        double res;
        res = Math.max(val, valMin);
        res = Math.min(val, valMax);
        return res;
    }

    double degreesToRadians(double deg) 
    {
        return deg * (Math.PI / 180);
    }

    double radiansToDegrees(double rad) 
    {
        return rad / (Math.PI / 180);
    }

//    PointF fromLatLngToPoint(double lat, double lng, int zoom)
//    {
//        PointF point = new PointF(0, 0);
//
//        point.x = _pixelOrigin.x + lng * _pixelsPerLonDegree;       
//
//        // Truncating to 0.9999 effectively limits latitude to 89.189. This is
//        // about a third of a tile past the edge of the world tile.
//        double siny = bound(Math.sin(degreesToRadians(lat)), -0.9999,0.9999);
//        point.y = _pixelOrigin.y + 0.5 * Math.log((1 + siny) / (1 - siny)) *- _pixelsPerLonRadian;
//
//        int numTiles = 1 << zoom;
//        point.x = point.x * numTiles;
//        point.y = point.y * numTiles;
//        return point;
//     }

    public Point fromPointToLatLng(Point point, int zoom)
    {
        int numTiles = 1 << zoom;
        point.setX( point.getX() / numTiles);
        point.setY(point.getY() / numTiles);       

        double lng = (point.getX() - _pixelOrigin.getX()) / _pixelsPerLonDegree;
        double latRadians = (point.getY() - _pixelOrigin.getY()) / - _pixelsPerLonRadian;
        double lat = radiansToDegrees(2 * Math.atan(Math.exp(latRadians)) - Math.PI / 2);
        return new Point(lat, lng);
    }

//    public static void main(String []args) 
//    {
//    	ConvertPoints gmap2 = new ConvertPoints();
//
//        PointF point1 = gmap2.fromLatLngToPoint(41.850033, -87.6500523, 15);
//        System.out.println(point1.x+"   "+point1.y);
//        PointF point2 = gmap2.fromPointToLatLng(point1,15);
//        System.out.println(point2.x+"   "+point2.y);
//    }
    
//    public final class PointF 
//    {
//        public double x;
//        public double y;
//
//        public PointF(double x, double y)
//        {
//            this.x = x;
//            this.y = y;
//        }
//    }
}



