package br.com.benhurqs.prefetch.maps.ArcGIS.search;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Envelope;
import com.esri.core.tasks.geocode.LocatorFindParameters;

public class ExecuteLocationTask extends LocatorAsyncTask {

	// Create Locator parameters from single line address string
	private LocatorFindParameters findParams;
	private Envelope mapExtent;
	private MapView mMapView;

	public ExecuteLocationTask(MapView mMapView, GraphicsLayer layer,
			LocatorListener listener) {
		this.mMapView = mMapView;
		this.layer = layer;
		setListener(listener);
	}

	public void findAddrress(String address) {

		try {

			if( mMapView != null  )
			{
				findParams = new LocatorFindParameters(address);
				findParams.setLocation(mMapView.getCenter(),
						mMapView.getSpatialReference());
	
				// Calculate distance for find operation
				mapExtent = new Envelope();
				mMapView.getExtent().queryEnvelope(mapExtent);
				// assume map is in meters, other units wont work, double current
				// envelope
				double distance = (mapExtent != null && mapExtent.getWidth() > 0) ? mapExtent
						.getWidth() * 2 : 10000;
				findParams.setDistance(distance);
				findParams.setMaxLocations(2);
	
				// Set address spatial reference to match map
				findParams.setOutSR(mMapView.getSpatialReference());
	
				// Execute async task to find the address
				// new LocatorAsyncTask().execute(findParams);
				this.execute(findParams);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
