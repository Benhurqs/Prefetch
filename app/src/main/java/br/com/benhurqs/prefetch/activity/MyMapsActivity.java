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

import br.com.benhurqs.prefetch.R;
import br.com.benhurqs.prefetch.directory.PathManager;
import br.com.benhurqs.prefetch.preferences.MapsPreferences;

public class MyMapsActivity extends Activity implements ListView.MultiChoiceModeListener{

    private ListView myMapsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_maps);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        init();
    }

    public void init(){
        myMapsList = (ListView)this.findViewById(R.id.lv_my_maps);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, PathManager.getMaps());
        myMapsList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        myMapsList.setMultiChoiceModeListener(this);
        myMapsList.setAdapter(adapter);
        myMapsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MapsPreferences pref = new MapsPreferences(MyMapsActivity.this);
                pref.saveMapName(PathManager.getMapName(position));
                finish();
            }
        });
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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
//        myMapsList.setItemSelected(position, checked);

        int selectCount = myMapsList.getCheckedItemCount();

        switch (selectCount) {
            case 1:
                mode.setSubtitle("Um item selecionado");
                break;
            default:
                mode.setSubtitle("" + selectCount + " itens selecionados");
                break;
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_select_menu, menu);
        mode.setTitle("Selecione os itens que deseja deletar");
        mode.setSubtitle("Um item selecionado");
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                deleteFolder();
                mode.finish();
                break;
            default:
                break;
        }

        return true;
    }

    public void deleteFolder() {
        SparseBooleanArray checked = myMapsList.getCheckedItemPositions();
        int size = checked.size();
        for (int i = size-1; i >= 0; i--) {
            Log.e("tamanho", i + "");
            int key = checked.keyAt(i);
            boolean value = checked.get(key);
            if (value){
                PathManager.deleteFolder(PathManager.getMapName(key));
                Log.e("tamanho",PathManager.getMapName(key));
            }
        }

        init();
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }
}
