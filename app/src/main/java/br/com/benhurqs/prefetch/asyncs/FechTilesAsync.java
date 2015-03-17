package br.com.benhurqs.prefetch.asyncs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;

import java.io.File;
import java.util.ArrayList;

import br.com.benhurqs.prefetch.directory.PathManager;
import br.com.benhurqs.prefetch.json.obj.MapsDataObj;
import br.com.benhurqs.prefetch.listeners.DownloadTileEvent;
import br.com.benhurqs.prefetch.model.TilePoint;
import br.com.benhurqs.prefetch.preferences.MapsPreferences;
import br.com.benhurqs.prefetch.rest.Core;
import br.com.benhurqs.prefetch.tiles.TileFinder;
import br.com.benhurqs.prefetch.tiles.TilesUtils;
import br.com.benhurqs.prefetch.util.DownLoadImageTask;
import br.com.benhurqs.prefetch.util.NetworkUtil;
import br.com.benhurqs.prefetch.util.Utils;

/**
 * Created by benhur on 16/03/15.
 */
public class FechTilesAsync extends AsyncTask<Integer, Integer, Integer> implements DownloadTileEvent.OnTilesListener {

    private int start_zoom = 0;
    private MapsPreferences pref;
    private DownloadTileEvent tileEvent;
    private Context ctx;
    private String path;
    private int currentZoom = 0;
    private int currentMaxProgress = 0;
    private int currentProgress = 0;
    private  Intent intent = new Intent();

    private double KEY_LATLNG_TOP_X;
    private double KEY_LATLNG_TOP_Y;
    private double KEY_LATLNG_BOTTOM_X;
    private double KEY_LATLNG_BOTTOM_Y;

    private int status;
    private static final int PROGRESS_STATUS_SUCCESS = 0 ;
    private static final int PROGRESS_STATUS_NETWORK_FAILURE = 1 ;
    private static final int PROGRESS_STATUS_NETWORK_CANCEL = 2 ;
    private static final String UPDATE_PROGRESS_DIALOG = "update_progress_tiles";

    private BroadcastReceiver updateDialogStatus = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            tileEvent.notifyTileEvent(DownloadTileEvent.DownloadTileType.UPDATE,
                    currentZoom, currentMaxProgress, currentProgress);
        }
    };


    public FechTilesAsync(Context ctx, String path, MapsDataObj params){
        this.ctx = ctx;
        this.path = path;
        pref = new MapsPreferences(ctx);
        tileEvent = new DownloadTileEvent();

        KEY_LATLNG_TOP_X = params.getLatlngTop().getX();
        KEY_LATLNG_TOP_Y = params.getLatlngTop().getY();
        KEY_LATLNG_BOTTOM_X = params.getLatlngBottom().getX();
        KEY_LATLNG_BOTTOM_Y = params.getLatlngBottom().getY();

        intent.setAction(UPDATE_PROGRESS_DIALOG);
        ctx.registerReceiver(updateDialogStatus, new IntentFilter(UPDATE_PROGRESS_DIALOG));

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Integer... params) {
        status = PROGRESS_STATUS_SUCCESS;
        TilesUtils tilesUtils = new TilesUtils();
        TileFinder tileFinder = new TileFinder(tilesUtils);

        if(params!= null && params[0] != null){
            start_zoom = params[0];
        }
        currentZoom = start_zoom;
        for (int i = start_zoom; i <= pref.getMaxZoom(); i++){
            ArrayList<TilePoint> urls = tileFinder.getTileUrlsByLatLngExtent(
                    KEY_LATLNG_TOP_X, KEY_LATLNG_BOTTOM_Y,KEY_LATLNG_BOTTOM_X, KEY_LATLNG_TOP_Y, i);
            currentMaxProgress = urls.size();
            currentProgress = 0;

            if(status != PROGRESS_STATUS_SUCCESS){
                break;
            }

            ctx.sendBroadcast(intent);

            for (TilePoint tilePoint : urls){
                if(isCancelled()) {
                    status = PROGRESS_STATUS_NETWORK_CANCEL;
                    break;
                }

                if( NetworkUtil.isNetworkAvailable(ctx)){
                    if( !fileExist(i, tilePoint.getY(), tilePoint.getX())){
                        Core rest = new Core(ctx, Core.RequestType.GET, path, i,
                                tilePoint.getY(), tilePoint.getX());
                        rest.enviar();
                    }
                    currentProgress++;
                    ctx.sendBroadcast(intent);
                }else{
                    status = PROGRESS_STATUS_NETWORK_FAILURE;
                    break;
                }
            }
            currentZoom = i;

        }


        return status;
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
            case PROGRESS_STATUS_NETWORK_CANCEL:
                tileEvent.notifyTileEvent(DownloadTileEvent.DownloadTileType.CANCEL);
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

    @Override
    protected void onCancelled(Integer integer) {
        super.onCancelled(integer);

        start_zoom = 0;
        currentZoom = 0;
        currentMaxProgress = 0;
        currentProgress = 0;
    }

    @Override
    public void onTileEvent(DownloadTileEvent.DownloadTileType event, int currentZoom, int currentMaxProgress, int currentProgress) {
        switch (event){
            case ERROR:
                status = PROGRESS_STATUS_NETWORK_FAILURE;
                break;
            default:
                break;
        }
    }
}
