package br.com.benhurqs.prefetch.json.obj;

import com.esri.core.geometry.Point;

/**
 * Created by bernardo on 19/02/15.
 */
public class MapsDataObj {

    private String name;
    private double lat;
    private double lng;
    private Point latlngTop;
    private Point latlngBottom;
    private int maxZoom;
    private int minZoom;

    public Point getLatlngTop() {
        return latlngTop;
    }

    public void setLatlngTop(Point latlngTop) {
        this.latlngTop = latlngTop;
    }

    public Point getLatlngBottom() {

        return latlngBottom;
    }

    public void setLatlngBottom(Point latlngBottom) {
        this.latlngBottom = latlngBottom;
    }

    public int getMinZoom() {
        return minZoom;
    }

    public void setMinZoom(int minZoom) {
        this.minZoom = minZoom;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int getMaxZoom() {
        return maxZoom;
    }

    public void setMaxZoom(int maxZoom) {
        this.maxZoom = maxZoom;
    }


}
