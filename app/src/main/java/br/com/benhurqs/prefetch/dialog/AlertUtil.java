package br.com.benhurqs.prefetch.dialog;

import br.com.benhurqs.prefetch.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AlertUtil {
	
	public interface AlertListener{
		void onClickOk(String value);
//		void onClickCancel();
	}
	
	@SuppressLint("InflateParams")
	public static void showDialogFindAlert(Activity activity, String title, String msg, final AlertListener listener) {
		LayoutInflater inflater = activity.getLayoutInflater();
		View dialoglayout = inflater.inflate(R.layout.dialog_layout, null);
		AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
		dialog.setView(dialoglayout);
		dialog.setCancelable(true);

		final AlertDialog optionDialog = dialog.create();

		TextView txtAlert = (TextView) dialoglayout.findViewById(R.id.txt_dialog_msg);
		txtAlert.setText(msg.toString().trim());

		TextView txtTitle = (TextView) dialoglayout.findViewById(R.id.txt_dialog_title);
		txtTitle.setText(title.toString().trim());
		
		final EditText edtText = (EditText) dialoglayout.findViewById(R.id.edt_value);
		if(txtTitle.getText().toString().equals(activity.getResources().getString(R.string.find))){
			edtText.setHint("99.9999,99.9999 ou Endereço");
		}
		Button btnOk = (Button) dialoglayout.findViewById(R.id.btnOk);
		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				optionDialog.dismiss();
				listener.onClickOk(edtText.getText().toString().trim());
			}
		});
		
		Button btnCancel = (Button) dialoglayout.findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				optionDialog.dismiss();
			}
		});

		optionDialog.show();
	}

    @SuppressLint("InflateParams")
    public static void showDialogAlert(Activity activity, String title, String msg, final AlertListener listener) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.dialog_layout, null);
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setView(dialoglayout);
        dialog.setCancelable(true);

        final AlertDialog optionDialog = dialog.create();

        TextView txtAlert = (TextView) dialoglayout.findViewById(R.id.txt_dialog_msg);
        txtAlert.setText(msg.toString().trim());

        TextView txtTitle = (TextView) dialoglayout.findViewById(R.id.txt_dialog_title);
        txtTitle.setText(title.toString().trim());

        final EditText edtText = (EditText) dialoglayout.findViewById(R.id.edt_value);

        Button btnOk = (Button) dialoglayout.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                optionDialog.dismiss();
                listener.onClickOk(edtText.getText().toString().trim());
            }
        });

        Button btnCancel = (Button) dialoglayout.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                optionDialog.dismiss();
            }
        });

        optionDialog.show();
    }

    @SuppressLint("InflateParams")
    public static void showDialogDeleteMap(Activity activity, String map_name, final AlertListener listener) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.dialog_delete_map, null);
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setView(dialoglayout);
        dialog.setCancelable(true);

        final AlertDialog optionDialog = dialog.create();

        TextView txtAlert = (TextView) dialoglayout.findViewById(R.id.txt_dialog_msg);
        txtAlert.setText(Html.fromHtml(activity.getString(R.string.deleted_map_name, map_name)));

        Button btnOk = (Button) dialoglayout.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                optionDialog.dismiss();
                listener.onClickOk(null);
            }
        });

        Button btnCancel = (Button) dialoglayout.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                optionDialog.dismiss();
            }
        });

        optionDialog.show();
    }


}
