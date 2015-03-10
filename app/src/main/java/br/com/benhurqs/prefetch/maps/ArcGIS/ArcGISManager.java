package br.com.benhurqs.prefetch.maps.ArcGIS;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.LocationService;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.GeometryException;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.TextSymbol;
import com.esri.core.tasks.geocode.LocatorGeocodeResult;

import java.util.List;

import br.com.benhurqs.prefetch.R;
import br.com.benhurqs.prefetch.activity.SettingsActivity;
import br.com.benhurqs.prefetch.dialog.AlertUtil;
import br.com.benhurqs.prefetch.dialog.AlertUtil.AlertListener;
import br.com.benhurqs.prefetch.helpers.SuperUI;
import br.com.benhurqs.prefetch.json.obj.MapsDataObj;
import br.com.benhurqs.prefetch.maps.ArcGIS.progress.ArcgisPrefechProgress;
import br.com.benhurqs.prefetch.maps.ArcGIS.search.ExecuteLocationTask;
import br.com.benhurqs.prefetch.maps.ArcGIS.search.LocatorListener;
import br.com.benhurqs.prefetch.preferences.MapsPreferences;
import br.com.benhurqs.prefetch.preferences.PointsPreferences;
import br.com.benhurqs.prefetch.util.Dimensions;
import br.com.benhurqs.prefetch.util.MapTypeUtil;

@SuppressWarnings("deprecation")
public class ArcGISManager extends SuperUI implements LocatorListener {

    public static final String KEY_LATLNG_TOP_X = "KEY_LATLNG_TOP_X";
    public static final String KEY_LATLNG_TOP_Y = "KEY_LATLNG_TOP_Y";
    public static final String KEY_LATLNG_BOTTOM_X = "KEY_LATLNG_BOTTOM_X";
    public static final String KEY_LATLNG_BOTTOM_Y = "KEY_LATLNG_BOTTOM_Y";

    protected MapView mMapView = null;
    protected RelativeLayout area;
    protected Button btnStart;
    protected RelativeLayout layLoading;
    protected GraphicsLayer mLocationLayer;
    protected Dimensions dimensions;

    private PointsPreferences pointsPreferences;
    private MapsPreferences mapsPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void initMap() {
        mapsPreferences = new MapsPreferences(this);
        area = (RelativeLayout) findViewById(R.id.rel_area);

        mLocationLayer = new GraphicsLayer();
        mMapView.addLayer(mLocationLayer);
//        mMapView.enableWrapAround(true);

        mMapView.setOnStatusChangedListener(new OnStatusChangedListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void onStatusChanged(Object source, STATUS status) {
                if (source == mMapView && status == STATUS.INITIALIZED) {

                    LocationService ls = mMapView.getLocationService();
                    ls.setAutoPan(true);
                    ls.start();
                    ls.setAutoPan(false);
                    mMapView.setMapOptions(mapsPreferences.getMapType());
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//		case R.id.arcgis_key:
//			showKeyDialog();
//			return true;
            case R.id.find:
                showFindDialog();
                return true;
            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.World_Street_Map:
                updateMapType(MapTypeUtil.STREETS);
                return true;
            case R.id.World_Topo:
                updateMapType(MapTypeUtil.TOPO);
                return true;
//            case R.id.Gray:
//                updateMapType(MapTypeUtil.GRAY);
//                return true;
            case R.id.Ocean_Basemap:
                updateMapType(MapTypeUtil.OCEANS);
                return true;
            case R.id.Satellite_Map:
                updateMapType(MapTypeUtil.SATELLITE);
                return true;
//            case R.id.Hybrid_map:
//                updateMapType(MapTypeUtil.HYBRID);
//                return true;
            case R.id.NatGeo_map:
                updateMapType(MapTypeUtil.NATIONAL_GEOGRAPHIC);
                return true;
//            case R.id.OSM_Map:
//                updateMapType(MapTypeUtil.OSM);
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateMapType(int mapType) {
        mMapView.setMapOptions(MapTypeUtil.getMapType(mapType));
        mapsPreferences.saveMapType(mapType);
    }

//	public void showKeyDialog(){
//		AlertUtil.showdialogAlert(this,
//				getResources().getString(R.string.key),  getResources().getString(R.string.arcgis_key),
//				new AlertListener() {
//
//			@Override
//			public void onClickOk(String text) {
//				ArcGISLicense.checkLicense(ArcGISManager.this, text);
//
//			}
//		});
//	}

    public void showFindDialog() {
        AlertUtil.showDialogFindAlert(this,
                getResources().getString(R.string.find), getResources().getString(R.string.find_address),
                new AlertListener() {

                    @Override
                    public void onClickOk(String text) {
                        if (text == null || text.equalsIgnoreCase("")) {
                            return;
                        }

                        String[] latlng = text.split(",");
                        if (latlng.length == 2) {
                            try {
                                double lat = Double.parseDouble(latlng[0]);
                                double lng = Double.parseDouble(latlng[1]);
                                mMapView.centerAt(lat, lng, true);
                                return;
                            } catch (NumberFormatException e) {
                            }
                        }

                        ExecuteLocationTask task = new ExecuteLocationTask(mMapView, mLocationLayer, ArcGISManager.this);
                        task.findAddrress(text);

                    }
                });
    }


    @Override
    protected void onPause() {
        super.onPause();
        mMapView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.unpause();

        pointsPreferences = new PointsPreferences(ArcGISManager.this, getResources().getConfiguration().orientation);
        if (pointsPreferences.isEmpty()) {
            dimensions = new Dimensions(mMapView, area, pointsPreferences);
        }

        mMapView.setMapOptions(mapsPreferences.getMapType());
    }

    public void startPreftch(String name) {
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
            obj.setName(name.toString().trim());
            obj.setLat(latlngCenter.getX());
            obj.setLng(latlngCenter.getY());
            obj.setMaxZoom(5);

            ArcgisPrefechProgress prefechProgress = new ArcgisPrefechProgress(name);

            Bundle prefechArguments = new Bundle();
            if (latlngTop != null && latlngBottom != null) {
                prefechArguments.putDouble(KEY_LATLNG_TOP_X, latlngTop.getX());
                prefechArguments.putDouble(KEY_LATLNG_TOP_Y, latlngTop.getY());
                prefechArguments.putDouble(KEY_LATLNG_BOTTOM_X, latlngBottom.getX());
                prefechArguments.putDouble(KEY_LATLNG_BOTTOM_Y, latlngBottom.getY());
                prefechProgress.setArguments(prefechArguments);
                prefechProgress.show(this.getFragmentManager(), "");
            }
        }catch (GeometryException ex){
            Toast.makeText(getApplicationContext(), "Error ao pegar a localizacao", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onLocatorStart() {
        layLoading.setVisibility(View.VISIBLE);
        btnStart.setClickable(false);

    }

    @Override
    public void onLocatorFinish(List<LocatorGeocodeResult> results) {
        layLoading.setVisibility(View.GONE);
        btnStart.setClickable(true);

        if (results != null && results.size() == 0) {
            Toast.makeText(this, getResources().getString(R.string.find_error), Toast.LENGTH_LONG).show();
            return;
        }

        LocatorGeocodeResult geocodeResult = results.get(0);

        // get return geometry from geocode result
        Point resultPoint = geocodeResult.getLocation();
        // create marker symbol to represent location
        SimpleMarkerSymbol resultSymbol = new SimpleMarkerSymbol(
                Color.RED, 16, SimpleMarkerSymbol.STYLE.CROSS);
        // create graphic object for resulting location
        Graphic resultLocGraphic = new Graphic(resultPoint,
                resultSymbol);
        // add graphic to location layer
        mLocationLayer.addGraphic(resultLocGraphic);

        // create text symbol for return address
        String address = geocodeResult.getAddress();
        TextSymbol resultAddress = new TextSymbol(20, address,
                Color.WHITE);
        // create offset for text
        resultAddress.setOffsetX(-4 * address.length());
        resultAddress.setOffsetY(10);
        // create a graphic object for address text
        Graphic resultText = new Graphic(resultPoint, resultAddress);
        // add address text graphic to location graphics layer
        mLocationLayer.addGraphic(resultText);

        // Zoom map to geocode result location
        mMapView.zoomToResolution(geocodeResult.getLocation(), 2);


    }

    public void addMarker(Point mapPoint) {
        PictureMarkerSymbol symbol = new PictureMarkerSymbol(getResources().getDrawable(R.drawable.ic_wp_map));
        Graphic resultLocGraphic = new Graphic(mapPoint,
                symbol);

        // add graphic to location layer
        mLocationLayer.addGraphic(resultLocGraphic);
    }

    public void showYesNoDialog() {
        //		if( SdHealthCheck.getFreeSpace(this) <= SdHealthCheck.ALARM_LOW_FREE_MEMO ){
        //			final Activity activity = this;
        //			YesNoDialog ynd = YesNoDialog.newInstance(getString(R.string.sd_warning),
        //					getString(R.string.sd_full_txt), new YesNoDialog.Listener() {
        //				@Override
        //				public void onYes() {
        //					ArcgisDeleteFiles deleteFiles = new ArcgisDeleteFiles();
        //					deleteFiles.show(activity.getFragmentManager(), "");
        //
        //				}
        //
        //				@Override
        //				public void onNo() {
        //				}
        //			});
        //			ynd.show( activity.getFragmentManager(), "clearMission");
        //		}
        //		else{

        //		}

        AlertUtil.showDialogFindAlert(this,
                getResources().getString(R.string.app_name), getResources().getString(R.string.name_prefetch),
                new AlertListener() {

                    @Override
                    public void onClickOk(String name) {
                        if (name == null || name.equalsIgnoreCase("")) {
                            return;
                        }
                        startPreftch(name);
                    }
                });
    }


}
