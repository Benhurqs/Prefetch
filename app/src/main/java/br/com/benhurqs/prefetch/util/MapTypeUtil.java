package br.com.benhurqs.prefetch.util;

import java.util.ArrayList;
import java.util.HashMap;

import com.esri.android.map.MapOptions;
import com.esri.android.map.MapOptions.MapType;

import br.com.benhurqs.prefetch.R;

public class MapTypeUtil {

	public static int TOPO = 0;
	public static int STREETS = 1;
	public static int GRAY = 2;
	public static int OCEANS = 3;
	public static int SATELLITE = 4;
	public static int HYBRID = 5;
	public static int NATIONAL_GEOGRAPHIC = 6;
	public static int OSM = 7;
	
	
	public static MapOptions mTopoBasemap = new MapOptions(MapType.TOPO);
	public static MapOptions mStreetsBasemap = new MapOptions(MapType.STREETS);
	public static MapOptions mGrayBasemap = new MapOptions(MapType.GRAY);
	public static MapOptions mOceansBasemap = new MapOptions(MapType.OCEANS);
	public static MapOptions mSatteliteBaseMap = new MapOptions(MapType.SATELLITE);
	public static MapOptions mHybridBaseMap = new MapOptions(MapType.HYBRID);
	public static MapOptions mNationalGeographicBaseMap = new MapOptions(MapType.NATIONAL_GEOGRAPHIC);
	public static MapOptions mOSMBaseMap = new MapOptions(MapType.OSM);
	
	public static final ArrayList<MapOptions> mapType = new ArrayList<MapOptions>(){
		private static final long serialVersionUID = 1L;
		{
			add(mTopoBasemap);
			add(mStreetsBasemap);
			add(mGrayBasemap);
			add(mOceansBasemap);
			add(mSatteliteBaseMap);
			add(mHybridBaseMap);
			add(mNationalGeographicBaseMap);
			add(mOSMBaseMap);
		}
		
	};
	
	public static MapOptions getMapType(int position){
		return mapType.get(position);
	}
	
	public static ArrayList<String> mapName = new ArrayList<String>(){
		private static final long serialVersionUID = 1L;
		{
			add("World_Topo_Map");
			add("ESRI_StreetMap_World_2D");
			add("null");
			add("Ocean_Basemap");
			add("World_Imagery");
			add("null");
			add("NatGeo_World_Map");
			add("World_Street_Map");
		}
	};

    public static ArrayList<Integer> mapUrl = new ArrayList<Integer>(){
        private static final long serialVersionUID = 1L;
        {
            add(R.string.tile_topo_url);
            add( R.string.tile_street_url);
            add(null);
            add( R.string.tile_ocean_url);
            add( R.string.tile_sattelite_url);
            add(null);
            add( R.string.tile_natgeo_url);
            add(null);
        }
    };
	
	
	public static String getMapName(int position){
		return mapName.get(position);
	}

    public static Integer getMapUrl(int position){
        return mapUrl.get(position);
    }
	
	/*
	  "services": [
  {
   "name": "ESRI_Imagery_World_2D",
   "type": "MapServer"
  },
  {
   "name": "ESRI_StreetMap_World_2D",
   "type": "MapServer"
  },
  {
   "name": "I3_Imagery_Prime_World",
   "type": "GlobeServer"
  },
  {
   "name": "NASA_CloudCover_World",
   "type": "GlobeServer"
  },
  {
   "name": "NatGeo_World_Map",
   "type": "MapServer"
  },
  {
   "name": "NGS_Topo_US_2D",
   "type": "MapServer"
  },
  {
   "name": "Ocean_Basemap",
   "type": "MapServer"
  },
  {
   "name": "USA_Topo_Maps",
   "type": "MapServer"
  },
  {
   "name": "World_Imagery",
   "type": "MapServer"
  },
  {
   "name": "World_Physical_Map",
   "type": "MapServer"
  },
  {
   "name": "World_Shaded_Relief",
   "type": "MapServer"
  },
  {
   "name": "World_Street_Map",
   "type": "MapServer"
  },
  {
   "name": "World_Terrain_Base",
   "type": "MapServer"
  },
  {
   "name": "World_Topo_Map",
   "type": "MapServer"
  }
 ]
	 * */

}
