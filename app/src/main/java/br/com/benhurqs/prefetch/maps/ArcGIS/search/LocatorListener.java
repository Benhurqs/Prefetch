package br.com.benhurqs.prefetch.maps.ArcGIS.search;

import java.util.List;

import com.esri.core.tasks.geocode.LocatorGeocodeResult;

public interface LocatorListener {
	void onLocatorStart();
	void onLocatorFinish(List<LocatorGeocodeResult> results);
}
