package br.com.benhurqs.prefetch.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import br.com.benhurqs.prefetch.R;

/**
 * Created by benhur on 19/02/15.
 */
public class MapsAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String[]> gridValues;
    private HashMap<Integer, Boolean> itensSelected = new HashMap<Integer, Boolean>();

    public MapsAdapter(Context context, ArrayList<String[]> itens){
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
        nameView = inflater.inflate(R.layout.maps_name_item, null);

        TextView txtName = (TextView) nameView.findViewById(R.id.txt_map_name);
        txtName.setText(gridValues.get(position)[0]);

        TextView txtSize = (TextView) nameView.findViewById(R.id.txt_map_size);
        txtSize.setText(gridValues.get(position)[1]);

        RelativeLayout layoutSelected = (RelativeLayout) nameView.findViewById(R.id.lay_bg);
        if(itensSelected.get(position) != null && itensSelected.get(position)){
            layoutSelected.setVisibility(View.VISIBLE);
        }else{
            layoutSelected.setVisibility(View.INVISIBLE);
        }

        return nameView;
    }
}