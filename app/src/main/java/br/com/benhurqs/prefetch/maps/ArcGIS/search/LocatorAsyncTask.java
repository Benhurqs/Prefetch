package br.com.benhurqs.prefetch.maps.ArcGIS.search;

import java.util.List;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.tasks.geocode.Locator;
import com.esri.core.tasks.geocode.LocatorFindParameters;
import com.esri.core.tasks.geocode.LocatorGeocodeResult;

import android.os.AsyncTask;
import android.util.Log;

public class LocatorAsyncTask extends AsyncTask<LocatorFindParameters, Void, List<LocatorGeocodeResult>> {


	protected GraphicsLayer layer;
	protected LocatorListener listener;
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		listener.onLocatorStart();
	}

	@Override
	protected List<LocatorGeocodeResult> doInBackground(
			LocatorFindParameters... params) {
        List<LocatorGeocodeResult> results = null;

        // Create locator using default online geocoding service and tell it
        // to find the given address
        Locator locator = Locator.createOnlineLocator();
        try {
            results = locator.find(params[0]);
        } catch (Exception e) {
//            mException = e;
        	Log.e("Exception", e.getMessage());
        }
        return results;
	}
	
	@Override
	protected void onPostExecute(List<LocatorGeocodeResult> result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		listener.onLocatorFinish(result);
		// Display results on UI thread
//        mProgressDialog.dismiss();
//        if (mException != null) 
//            Log.w(TAG, "LocatorSyncTask failed with:");
//            mException.printStackTrace();
//            Toast.makeText(MainActivity.this,
//                    getString(R.string.addressSearchFailed),
//                    Toast.LENGTH_LONG).show();
//            return;
//        }

        if (result.size() == 0) {
//            Toast.makeText(MainActivity.this,
//                    getString(R.string.noResultsFound), Toast.LENGTH_LONG)
//                    .show();
        } else {
            // Use first result in the list
//            LocatorGeocodeResult geocodeResult = result.get(0);
//
//            // get return geometry from geocode result
//            Point resultPoint = geocodeResult.getLocation();
//            // create marker symbol to represent location
//            SimpleMarkerSymbol resultSymbol = new SimpleMarkerSymbol(
//                    Color.RED, 16, SimpleMarkerSymbol.STYLE.CROSS);
//            // create graphic object for resulting location
//            Graphic resultLocGraphic = new Graphic(resultPoint,
//                    resultSymbol);
//            // add graphic to location layer
//            layer.addGraphic(resultLocGraphic);
//
//            // create text symbol for return address
//            String address = geocodeResult.getAddress();
//            TextSymbol resultAddress = new TextSymbol(20, address,
//                    Color.BLACK);
//            // create offset for text
//            resultAddress.setOffsetX(-4 * address.length());
//            resultAddress.setOffsetY(10);
//            // create a graphic object for address text
//            Graphic resultText = new Graphic(resultPoint, resultAddress);
//            // add address text graphic to location graphics layer
//            layer.addGraphic(resultText);
//
//            // Zoom map to geocode result location
//            mMapView.zoomToResolution(geocodeResult.getLocation(), 2);
        }
	}
	
	public void setListener(LocatorListener locatorListener){
		this.listener = locatorListener;
	}

}