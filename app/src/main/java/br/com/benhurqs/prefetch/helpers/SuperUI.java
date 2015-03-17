package br.com.benhurqs.prefetch.helpers;

import android.app.Activity;
import android.os.Bundle;

import br.com.benhurqs.prefetch.listeners.DownloadTileEvent;

/**
 * Created by benhur on 17/02/15.
 */
public class SuperUI extends Activity implements DownloadTileEvent.OnTilesListener {

    private DownloadTileEvent tileEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tileEvent = new DownloadTileEvent();

    }

    @Override
    protected void onResume() {
        super.onResume();
        tileEvent.addTileListener(this);
    }

    @Override
    public void onTileEvent(DownloadTileEvent.DownloadTileType event, int currentZoom, int currentMaxProgress, int currentProgress) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        tileEvent.removeMagListener(this);
    }
}
