package br.com.benhurqs.prefetch.helpers;

import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.esri.android.map.MapView;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.GeometryException;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;

import br.com.benhurqs.prefetch.R;
import br.com.benhurqs.prefetch.dialog.AlertUtil;
import br.com.benhurqs.prefetch.dialog.ProgressDialog;
import br.com.benhurqs.prefetch.json.obj.MapsDataObj;
import br.com.benhurqs.prefetch.listeners.DownloadTileEvent;
import br.com.benhurqs.prefetch.maps.ArcGIS.progress.ArcgisPrefechProgress;
import br.com.benhurqs.prefetch.preferences.PointsPreferences;
import br.com.benhurqs.prefetch.util.Constants;
import br.com.benhurqs.prefetch.util.Dimensions;

/**
 * Created by benhur on 16/03/15.
 */
public class DownloadManager extends  SuperUI {

    protected MapView mMapView = null;
    protected RelativeLayout area;
    protected Dimensions dimensions;
    private PointsPreferences pointsPreferences;

    @Override
    protected void onResume() {
        super.onResume();

        pointsPreferences = new PointsPreferences(this, getResources().getConfiguration().orientation);
        if (pointsPreferences.isEmpty()) {
            dimensions = new Dimensions(mMapView, area, pointsPreferences);
        }
    }

    public void startPreftch(String path) {
        Point topLeft = mMapView.toMapPoint(pointsPreferences.getTopLeftX(), pointsPreferences.getTopLeftY());
        Point bottomRight = mMapView.toMapPoint(pointsPreferences.getBottomRightX(), pointsPreferences.getBottomRightY());
        Point center =  mMapView.toMapPoint(
                (pointsPreferences.getTopLeftX() + pointsPreferences.getBottomRightX())/2,
                (pointsPreferences.getTopLeftY() + pointsPreferences.getBottomRightY())/2);

        SpatialReference sp = SpatialReference.create(SpatialReference.WKID_WGS84);
        Point latlngTop = (Point) GeometryEngine.project(topLeft, mMapView.getSpatialReference(), sp);
        Point latlngBottom = (Point) GeometryEngine.project(bottomRight, mMapView.getSpatialReference(), sp);
        Point latlngCenter = (Point) GeometryEngine.project(center, mMapView.getSpatialReference(), sp);


        try {
            MapsDataObj obj = new MapsDataObj();
            obj.setName(path.toString().trim());
            obj.setLat(latlngCenter.getX());
            obj.setLng(latlngCenter.getY());
            obj.setLatlngTop(latlngTop);
            obj.setLatlngBottom(latlngBottom);
            obj.setMaxZoom(5);

            ProgressDialog progressTile = new ProgressDialog(DownloadManager.this, path, obj);
            progressTile.startDownload();
        }catch (GeometryException ex){
            Toast.makeText(getApplicationContext(), "Error ao pegar a localizacao", Toast.LENGTH_SHORT).show();
        }


    }

    public void showYesNoDialog() {
        AlertUtil.showDialogFindAlert(this,
                getResources().getString(R.string.app_name), getResources().getString(R.string.name_prefetch),
                new AlertUtil.AlertListener() {

                    @Override
                    public void onClickOk(String name) {
                        if (name == null || name.equalsIgnoreCase("")) {
                            return;
                        }
                        startPreftch(name);
                    }
                });
    }


    @Override
    public void onTileEvent(DownloadTileEvent.DownloadTileType event, int currentZoom, int currentMaxProgress, int currentProgress) {
        super.onTileEvent(event,currentZoom,currentMaxProgress,currentProgress);
        switch (event) {
            case START:
                break;
            case FINISH:
                break;
            case CANCEL:
                break;
            case FAILURE:
                break;
            case UPDATE:
                break;
            default:
                break;
        }
    }


}
