/* Copyright 2013 ESRI
 *
 * All rights reserved under the copyright laws of the United States
 * and applicable international laws, treaties, and conventions.
 *
 * You may freely redistribute and use this sample code, with or
 * without modification, provided you include the original copyright
 * notice and use restrictions.
 *
 * See the Sample code usage restrictions document for further information.
 *
 */

package br.com.benhurqs.prefetch.activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import br.com.benhurqs.prefetch.maps.ArcGIS.ArcGISManager;
import br.com.benhurqs.prefetch.R;

import com.esri.android.map.MapView;

public class ArcGISActivity extends ArcGISManager implements OnClickListener {


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

        ActionBar bar = getActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

		init();

	}

	public void init(){
		btnStart = (Button)this.findViewById(R.id.btn_start);
		btnStart.setOnClickListener(this);

		layLoading = (RelativeLayout)this.findViewById(R.id.lay_loading);
		layLoading.setVisibility(View.GONE);

		mMapView = (MapView) findViewById(R.id.map);		

		initMap();

	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		switch (item.getItemId()) {
            case android.R.id.home:
                ArcGISActivity.this.finish();
                overridePendingTransition(R.anim.in_from_left, R.anim.out_from_right);
                return true;
		default:
			return true;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_start:
		{
			showYesNoDialog();
			break;
		}

		default:
			break;
		}

	}

}