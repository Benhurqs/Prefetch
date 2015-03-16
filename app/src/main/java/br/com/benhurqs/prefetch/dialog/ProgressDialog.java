package br.com.benhurqs.prefetch.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import br.com.benhurqs.prefetch.R;
import br.com.benhurqs.prefetch.listeners.DownloadTileEvent;

/**
 * Created by bernardo on 16/03/15.
 */
public class ProgressDialog implements View.OnClickListener, DownloadTileEvent.OnTilesListener{

    private AlertDialog.Builder dialog;
    private AlertDialog optionDialog;
    private TextView errorMsg, titlePrefechZoom, prefechProgressTxt;
    private Button btnContiue, btnClose;
    private ProgressBar prefechProgress;
    private View prefechSuccessView;
    private RelativeLayout prefechLayout;
    private DownloadTileEvent tileEvent;
    private Activity activity;

    public ProgressDialog(Activity activity) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.prefech_progress, null);
        dialog = new AlertDialog.Builder(activity);
        dialog.setView(dialoglayout);
        dialog.setCancelable(true);

        optionDialog = dialog.create();

        errorMsg = (TextView) dialoglayout.findViewById(R.id.txt_error);
        btnContiue = (Button) dialoglayout.findViewById(R.id.btnContinue);
        btnContiue.setOnClickListener(this);
        titlePrefechZoom = (TextView) dialoglayout.findViewById(R.id.txt_dialog_title);
        prefechProgress = (ProgressBar) dialoglayout.findViewById(R.id.prefechProgress);
        prefechProgressTxt = (TextView) dialoglayout.findViewById(R.id.prefechProgressTxt);
        prefechSuccessView =  dialoglayout.findViewById(R.id.prefechSuccess);
        prefechLayout = (RelativeLayout) dialoglayout.findViewById(R.id.actionBtnLayout);
        btnClose = (Button) dialoglayout.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(this);

        tileEvent = new DownloadTileEvent();
    }

    public void show(){
        optionDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCancel:{
//                stopPrefechTask( );
                optionDialog.dismiss();
                tileEvent.notifyTileEvent(DownloadTileEvent.DownloadTileType.CANCEL);
                break;
            }case R.id.btnContinue:{
                errorMsg.setVisibility(View.GONE);
                btnContiue.setVisibility(View.GONE);
                tileEvent.notifyTileEvent(DownloadTileEvent.DownloadTileType.CONTINUE);

//                startPrefechTask( currentZoom );

                break;
            }case R.id.btnClose:{
                tileEvent.notifyTileEvent(DownloadTileEvent.DownloadTileType.STOP);
                optionDialog.dismiss();
                break;
            }

        }

    }


    private void updatePrefechProgressTitle(int currentZoom, int currentMaxProgress, int currentProgress){
        titlePrefechZoom.setText(activity.getString(R.string.prefech_progress_title, currentZoom));
        prefechProgress.setMax(currentMaxProgress);
        prefechProgress.setProgress(currentProgress);
        prefechProgressTxt.setText(activity.getString(R.string.prefech_progress_txt_bar, currentProgress,currentMaxProgress));

    }

    private void updatePrefechNetworkFailure(){
        errorMsg.setVisibility(View.VISIBLE);
        errorMsg.setText(R.string.prefech_progress_network_error);
        btnContiue.setVisibility(View.VISIBLE);
    }


    private void updatePrefechSuccess(){
        prefechSuccessView.setVisibility(View.VISIBLE);
        prefechLayout.setVisibility(View.GONE);
    }

    @Override
    public void onTileEvent(DownloadTileEvent.DownloadTileType event) {
        switch (event){
            case UPDATE:
                updatePrefechProgressTitle();
                break;
            case FAILURE:
                updatePrefechNetworkFailure();
                break;
            case FINISH:
                updatePrefechSuccess();
                break;
            default:
                break;
        }
    }
}
