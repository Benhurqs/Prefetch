package br.com.benhurqs.prefetch.rest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Looper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.apache.http.Header;

import br.com.benhurqs.prefetch.R;
import br.com.benhurqs.prefetch.listeners.DownloadTileEvent;
import br.com.benhurqs.prefetch.preferences.MapsPreferences;
import br.com.benhurqs.prefetch.util.TileFileHandler;
import br.com.benhurqs.prefetch.util.Utils;

/**
 * Created by benhur on 16/03/15.
 */
public class Core {

    private AsyncHttpClient client;
    private String url;
    private Context ctx;
    private RequestType type;
    private String pathName;
    private DownloadTileEvent tilesEvent;
    private int zoom;
    private String x, y;

    protected RequestParams params;
    protected AsyncHttpResponseHandler responseHandler = null;

    public static AsyncHttpClient syncHttpClient= new SyncHttpClient();
    public static AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

    public enum  RequestType{
        GET, POST;
    }

    public Core(Context ctx, RequestType type, String pathName, int zoom , long y , long x){
        this.ctx = ctx;
        this.type = type;
        this.pathName = pathName;
        this.zoom = zoom;
        this.x = String.valueOf(x);
        this.y = String.valueOf(y);

        MapsPreferences pref = new MapsPreferences(ctx);
        this.url = ctx.getString( pref.getMapUrl(),  ctx.getString(R.string.host_name),zoom,y,x );
        tilesEvent = new DownloadTileEvent();
    }


    public void get() {
        getClient().get(url, params, responseHandler);
    }

    public void post( ) {
        getClient().post(url, params, responseHandler);
    }

    public boolean enviar(){
        if(Utils.isDeviceOnline(ctx)){
            startComm();
            return true;
        }else{
            tilesEvent.notifyTileEvent(DownloadTileEvent.DownloadTileType.ERROR);
        }

        return false;
    }


    public void startComm(){
        responseHandler = new AsyncHttpResponseHandler(){
            @Override
            public void onStart() {
                super.onStart();
//                tilesEvent.notifyTileEvent(DownloadTileEvent.DownloadTileType.START);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Bitmap tileBmp = BitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);

                if( tileBmp != null ) {
                    if (TileFileHandler.storeTile(pathName, tileBmp,
                            String.valueOf(zoom), String.valueOf(y), String.valueOf(x), ctx)) {
                        return;
                    }
                }
                tilesEvent.notifyTileEvent(DownloadTileEvent.DownloadTileType.ERROR);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                tilesEvent.notifyTileEvent(DownloadTileEvent.DownloadTileType.ERROR);
            }

            @Override
            public void onFinish() {
                super.onFinish();
//                tilesEvent.notifyTileEvent(DownloadTileEvent.DownloadTileType.FINISH);
            }
        };

        switch (type){
            case GET:
                get();
                break;
            case POST:
                post();
                break;
            default:
                break;
        }
    }


    private static AsyncHttpClient getClient()
    {
        if (Looper.myLooper() == null)
            return syncHttpClient;
        return asyncHttpClient;
    }


}
