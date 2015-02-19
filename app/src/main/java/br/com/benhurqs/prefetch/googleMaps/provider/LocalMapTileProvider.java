package br.com.benhurqs.prefetch.googleMaps.provider;

import android.graphics.Path;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;


import com.google.android.gms.internal.pa;
import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileProvider;

import br.com.benhurqs.prefetch.directory.PathManager;
import br.com.benhurqs.prefetch.util.files.FileStream;

/**
 * Title provider for a MapView from the local storage. Based on:
 * http://stackoverflow.com/questions/14784841/tileprovider-using-local-tiles
 *
 */
public class LocalMapTileProvider implements TileProvider {
    private static final int TILE_WIDTH = 256;
    private static final int TILE_HEIGHT = 256;
    private static final int BUFFER_SIZE = 16 * 1024;
    private String path = null;

    public LocalMapTileProvider(String path) {
        this.path = path;
        tryToAddANoMediaFile();
    }

    private void tryToAddANoMediaFile() {
        try {
            FileStream.createNoMediaFile(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Tile getTile(int x, int y, int zoom) {
        byte[] image = readTileImage(x, y, zoom);
        if (image == null) {
            return NO_TILE;
        } else {
            return new Tile(TILE_WIDTH, TILE_HEIGHT, image);
        }

    }

    private byte[] readTileImage(int x, int y, int zoom) {
        FileInputStream in = null;
        ByteArrayOutputStream buffer = null;

        try {
//            String patch = DirectoryPath.getMapsPath() + getTileFilename(x, y, zoom);
            String patch = PathManager.getPrefetchPath() + File.separator + path +  File.separator + getTileFilename(x, y, zoom);
            System.out.println( patch );
            in = new FileInputStream(patch);
            buffer = new ByteArrayOutputStream();

            int nRead;
            byte[] data = new byte[BUFFER_SIZE];

            while ((nRead = in.read(data, 0, BUFFER_SIZE)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();

            return buffer.toByteArray();
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (Exception ignored) {
                }
            if (buffer != null)
                try {
                    buffer.close();
                } catch (Exception ignored) {
                }
        }
    }

    private String getTileFilename(int x, int y, int zoom) {

        return String.format(Locale.US, "%d/%d/%d.jpg", zoom, y, x);
    }

}