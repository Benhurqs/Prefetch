package br.com.benhurqs.prefetch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import br.com.benhurqs.prefetch.R;

/**
 * Created by benhur on 19/02/15.
 */
public class MapsAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> gridValues;
    private HashMap<Integer, Boolean> itensSelected = new HashMap<Integer, Boolean>();

    public MapsAdapter(Context context, ArrayList<String> itens){
        this.context = context;
        this.gridValues = itens;

    }

    @Override
    public int getCount() {
        return gridValues.size();
    }

    @Override
    public Object getItem(int position) {

        return null;
    }

    @Override
    public long getItemId(int position) {

        return 0;
    }

    public void setItemSelected(int position, boolean value){
        itensSelected.put(position, value);
        notifyDataSetChanged();
    }

    public void clearSelected(){
        itensSelected.clear();
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View nameView;
        nameView = new View(context);
        nameView = inflater.inflate(R.layout.maps_name_item, null);

        LinearLayout layoutSelected = (LinearLayout) nameView.findViewById(R.id.lay_bg);
        if(itensSelected.get(position) != null && itensSelected.get(position)){
            layoutSelected.setVisibility(View.VISIBLE);
        }else{
            layoutSelected.setVisibility(View.INVISIBLE);
        }




        return nameView;
    }
}