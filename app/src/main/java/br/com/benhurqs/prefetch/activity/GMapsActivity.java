package br.com.benhurqs.prefetch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;

import br.com.benhurqs.prefetch.R;
import br.com.benhurqs.prefetch.googleMaps.provider.LocalMapTileProvider;
import br.com.benhurqs.prefetch.preferences.MapsPreferences;

public class GMapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private MapsPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gmaps);

        pref = new MapsPreferences(this);
        setUpMapIfNeeded();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }else{
            setUpMap();
        }
    }

    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
        if(pref.getMyMapName() != null) {
            setupOfflineMapOverlay();
        }

//        CameraUpdate upd = CameraUpdateFactory.newLatLngZoom(new LatLng(LAT, LON), ZOOM);
//        mMap.moveCamera(upd);


    }

    private void setupOfflineMapOverlay() {
        mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
        TileOverlay tileOverlay = mMap
                .addTileOverlay(new TileOverlayOptions().tileProvider(new LocalMapTileProvider(pref.getMyMapName())));
        tileOverlay.setZIndex(-1);
        tileOverlay.clearTileCache();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_offline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(this,SettingsActivity.class);
                startActivity(intent);
//                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;
            case R.id.downlaod:
                Intent arcgis = new Intent(this,ArcGISActivity.class);
                arcgis.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(arcgis);
                return true;
            case R.id.my_maps:
                Intent myMaps = new Intent(this,MyMapsActivity.class);
                startActivity(myMaps);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
