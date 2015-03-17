package br.com.benhurqs.prefetch.listeners;

import java.util.ArrayList;

/**
 * Created by bernardo on 16/03/15.
 */
public class DownloadTileEvent {

    public enum DownloadTileType{
        START, UPDATE, FAILURE, CANCEL, ERROR, FINISH, CONTINUE;
    }

    public interface OnTilesListener{
        public void onTileEvent(DownloadTileType event, int currentZoom, int currentMaxProgress, int currentProgress);
    }

    private static ArrayList<OnTilesListener> tilesListeners = new ArrayList<OnTilesListener>();

    public void addTileListener(OnTilesListener listener){
        if (listener != null & !tilesListeners.contains(listener)){
            tilesListeners.add(listener);
        }
    }

    public void removeMagListener(OnTilesListener listener) {
        if (listener != null && tilesListeners.contains(listener)){
            tilesListeners.remove(listener);
        }
    }

    public void removeAllListener(){
        tilesListeners.clear();
    }

    public void notifyTileEvent(DownloadTileType event) {
       this.notifyTileEvent(event, -1, -1, -1);
    }

    public void notifyTileEvent(DownloadTileType event, int currentZoom, int currentMaxProgress, int currentProgress ) {
        if (tilesListeners.size() > 0) {
            for (OnTilesListener listener : tilesListeners) {
                listener.onTileEvent(event, currentZoom, currentMaxProgress, currentProgress);
            }

        }
    }

}
