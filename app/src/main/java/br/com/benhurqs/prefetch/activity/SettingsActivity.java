package br.com.benhurqs.prefetch.activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.view.MenuItem;

import br.com.benhurqs.prefetch.R;

public class SettingsActivity extends PreferenceActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		ActionBar bar = getActionBar();
		bar.setDisplayHomeAsUpEnabled(true);
		
		getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
	    
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				SettingsActivity.this.finish();
                overridePendingTransition(R.anim.in_from_bottom, R.anim.out_from_top);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	public static class MyPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_pref);
        }
    }

}
