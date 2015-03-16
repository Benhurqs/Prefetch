package br.com.benhurqs.prefetch.asyncs;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.File;
import java.util.ArrayList;

import br.com.benhurqs.prefetch.directory.PathManager;
import br.com.benhurqs.prefetch.util.files.FileManager;

/**
 * Created by benhur on 13/03/15.
 */
public class MyMapsAsyncs extends Activity {

    protected ProgressBar progress;
    protected ListView myMapsList;
    protected ArrayList<String[]> mapsListName;


    public class GetFolders extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
            myMapsList.setClickable(false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            for(File mapFile : PathManager.getPrefetchFile().listFiles()){
                String[] mapName = new String[]{mapFile.getName(), FileManager.getSize(mapFile)};
                mapsListName.add(mapName);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progress.setVisibility(View.GONE);
            myMapsList.setClickable(true);
        }
    }

    public class DeleteFolders extends AsyncTask<Integer,Void,Void>{

        @Override
        protected Void doInBackground(Integer... params) {
            PathManager.deleteFolder(PathManager.getMapName(params[0]));
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
            myMapsList.setClickable(false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progress.setVisibility(View.GONE);
            myMapsList.setClickable(true);
        }
    }

    public void init(){};
}
