package br.com.benhurqs.prefetch.maps.ArcGIS.progress;

import br.com.benhurqs.prefetch.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class YesNoDialog extends DialogFragment {

	public interface Listener {
		void onYes();

		void onNo();
	}

	public static YesNoDialog newInstance(String title, String msg,
			Listener listener) {
		YesNoDialog f = new YesNoDialog();
		Bundle b = new Bundle();
		b.putString("title", title);
		b.putString("message", msg);
		f.setArguments(b);
		f.mListener = listener;
		return f;
	}

	private Listener mListener;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder b = new AlertDialog.Builder(getActivity())
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(getArguments().getString("title"))
				.setMessage(getArguments().getString("message"))
				.setPositiveButton(getString(R.string.txt_yes),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								mListener.onYes();
							}
						})
				.setNegativeButton(getString(R.string.txt_no),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								mListener.onNo();
							}
						});

		return b.create();
	}
}
