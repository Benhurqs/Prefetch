package br.com.benhurqs.prefetch.adapter;

import android.content.Context;
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

        return nameView;
    }
}