package br.com.benhurqs.prefetch.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;

import br.com.benhurqs.prefetch.R;
import br.com.benhurqs.prefetch.adapter.MapsAdapter;
import br.com.benhurqs.prefetch.dialog.AlertUtil;
import br.com.benhurqs.prefetch.directory.PathManager;
import br.com.benhurqs.prefetch.helpers.MyMapsAsyncs;
import br.com.benhurqs.prefetch.preferences.MapsPreferences;

public class MyMapsActivity extends MyMapsAsyncs {

    private MapsAdapter myMapsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_maps);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        init();
    }

    @Override
    public void init(){
        progress = (ProgressBar)this.findViewById(R.id.progress);
        mapsListName = new ArrayList<String[]>();

        myMapsList = (ListView)this.findViewById(R.id.lv_my_maps);

        populateList();

        myMapsAdapter = new MapsAdapter(this, mapsListName);
        myMapsList.setAdapter(myMapsAdapter);
        myMapsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MapsPreferences pref = new MapsPreferences(MyMapsActivity.this);
                pref.saveMapName(PathManager.getMapName(position));
                finish();
                overridePendingTransition(R.anim.in_from_right, R.anim.out_from_left);
            }
        });

        myMapsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertUtil.showDialogDeleteMap(MyMapsActivity.this, PathManager.getMapName(position), new AlertUtil.AlertListener() {
                    @Override
                    public void onClickOk(String value) {
                        deleteFolder(position);
                    }
                });
                return true;
            }
        });
    }

    public void populateList(){
//        for(File mapFile : PathManager.getPrefetchFile().listFiles()){
//            String[] mapName = new String[]{mapFile.getName(), FileManager.getSize(mapFile)};
//            mapsListName.add(mapName);
//        }

        new GetFolders().execute();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.in_from_right, R.anim.out_from_left);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    public void deleteFolder(int position) {
//        PathManager.deleteFolder(PathManager.getMapName(position));
//        init();
        new DeleteFolders().execute(position);
    }

}
