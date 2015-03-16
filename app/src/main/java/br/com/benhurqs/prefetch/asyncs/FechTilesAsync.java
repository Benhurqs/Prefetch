package br.com.benhurqs.prefetch.asyncs;

import android.content.Context;
import android.os.AsyncTask;

import java.io.File;
import java.util.ArrayList;

import br.com.benhurqs.prefetch.directory.PathManager;
import br.com.benhurqs.prefetch.listeners.DownloadTileEvent;
import br.com.benhurqs.prefetch.model.TilePoint;
import br.com.benhurqs.prefetch.preferences.MapsPreferences;
import br.com.benhurqs.prefetch.tiles.TileFinder;
import br.com.benhurqs.prefetch.tiles.TilesUtils;
import br.com.benhurqs.prefetch.util.DownLoadImageTask;
import br.com.benhurqs.prefetch.util.NetworkUtil;

/**
 * Created by benhur on 16/03/15.
 */
public class FechTilesAsync extends AsyncTask<Integer, Integer, Integer> {

    private int start_zoom;
    private MapsPreferences pref;
    private DownloadTileEvent tileEvent;
    private Context ctx;
    private String path;
    private int currentZoom = 0;
    private int currentMaxProgress = 0;
    private int currentProgress = 0;

    private static final int PROGRESS_STATUS_SUCCESS = 0 ;
    private static final int PROGRESS_STATUS_NETWORK_FAILURE = 1 ;

    private double KEY_LATLNG_TOP_X;
    private double KEY_LATLNG_TOP_Y;
    private double KEY_LATLNG_BOTTOM_X;
    private double KEY_LATLNG_BOTTOM_Y;

    public FechTilesAsync(Context ctx, String path, double[] params){
        this.ctx = ctx;
        this.path = path;
        pref = new MapsPreferences(ctx);
        tileEvent = new DownloadTileEvent();

        KEY_LATLNG_TOP_X = params[0];
        KEY_LATLNG_TOP_Y = params[1];
        KEY_LATLNG_BOTTOM_X = params[2];
        KEY_LATLNG_BOTTOM_Y = params[3];
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Integer... params) {

        TilesUtils tilesUtils = new TilesUtils();
        TileFinder tileFinder = new TileFinder(tilesUtils);

        if(params!= null && params[0] != null){
            start_zoom = params[0];
        }

        for (int i = start_zoom; i <= pref.getMaxZoom(); i++){
            currentZoom = i;
            ArrayList<TilePoint> urls = tileFinder.getTileUrlsByLatLngExtent(
                    KEY_LATLNG_TOP_X, KEY_LATLNG_BOTTOM_Y,KEY_LATLNG_BOTTOM_X, KEY_LATLNG_TOP_Y, i);
            currentMaxProgress = urls.size();
            currentProgress = 0;
            for (TilePoint tilePoint : urls){
                if( NetworkUtil.isNetworkAvailable(ctx)){
                    if( !fileExist(i, tilePoint.getY(), tilePoint.getX())){
                        if( !DownLoadImageTask.fechTile(path, i,
                                tilePoint.getY(), tilePoint.getX(), ctx)){
                            return PROGRESS_STATUS_NETWORK_FAILURE;
                        }
                    }
                    currentProgress++;
                    tileEvent.notifyTileEvent(DownloadTileEvent.DownloadTileType.UPDATE,
                            currentZoom,currentMaxProgress, currentProgress );
                }else{
                    return PROGRESS_STATUS_NETWORK_FAILURE;
                }
            }
        }

        return PROGRESS_STATUS_SUCCESS;
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);

        switch (result){
            case PROGRESS_STATUS_NETWORK_FAILURE:
                tileEvent.notifyTileEvent(DownloadTileEvent.DownloadTileType.FAILURE);
                break;
            case PROGRESS_STATUS_SUCCESS:
                tileEvent.notifyTileEvent(DownloadTileEvent.DownloadTileType.FINISH);
                break;
            default:
                break;
        }

    }

    private boolean fileExist(int zoom, long y, long x ){
        String tileUrl = PathManager.getFile(path) + "/" + zoom + "/" + y + "/" + x +".jpg";
        File tile = new File(tileUrl);
        if(tile.exists()) {
            return true;
        }else{
            return false;
        }

    }
}
