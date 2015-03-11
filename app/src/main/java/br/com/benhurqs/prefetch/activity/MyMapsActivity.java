package br.com.benhurqs.prefetch.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Path;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

import br.com.benhurqs.prefetch.R;
import br.com.benhurqs.prefetch.adapter.MapsAdapter;
import br.com.benhurqs.prefetch.dialog.AlertUtil;
import br.com.benhurqs.prefetch.directory.PathManager;
import br.com.benhurqs.prefetch.preferences.MapsPreferences;
import br.com.benhurqs.prefetch.util.files.FileManager;

public class MyMapsActivity extends Activity{

    private ListView myMapsList;
    private ArrayList<String[]> mapsListName;
    private MapsAdapter myMapsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_maps);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        init();
    }

    public void init(){
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
        for(File mapFile : PathManager.getPrefetchFile().listFiles()){
            String[] mapName = new String[]{mapFile.getName(), FileManager.getSize(mapFile)};
            mapsListName.add(mapName);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_my_maps, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

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
        PathManager.deleteFolder(PathManager.getMapName(position));
        init();
    }

}
