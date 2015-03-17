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
import br.com.benhurqs.prefetch.asyncs.FechTilesAsync;
import br.com.benhurqs.prefetch.json.obj.MapsDataObj;
import br.com.benhurqs.prefetch.listeners.DownloadTileEvent;

/**
 * Created by bernardo on 16/03/15.
 */
public class ProgressDialog implements View.OnClickListener, DownloadTileEvent.OnTilesListener{

    private AlertDialog.Builder dialog;
    private AlertDialog optionDialog;
    private TextView errorMsg, titlePrefechZoom, prefechProgressTxt;
    private Button btnContiue, btnClose, btnCancel;
    private ProgressBar prefechProgress;
    private View prefechSuccessView;
    private RelativeLayout prefechLayout;
    private DownloadTileEvent tileEvent;
    private Activity activity;
    private FechTilesAsync tilesAsync;
    private int currentZoom = 0;
    private String path;
    private MapsDataObj params;

    public ProgressDialog(Activity activity, String path, MapsDataObj params) {
        this.activity = activity;
        this.path = path;
        this.params = params;
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.prefech_progress, null);
        dialog = new AlertDialog.Builder(activity);
        dialog.setView(dialoglayout);
        dialog.setCancelable(true);

        optionDialog = dialog.create();

        errorMsg = (TextView) dialoglayout.findViewById(R.id.txt_error);

        titlePrefechZoom = (TextView) dialoglayout.findViewById(R.id.txt_dialog_title);
        prefechProgress = (ProgressBar) dialoglayout.findViewById(R.id.prefechProgress);
        prefechProgressTxt = (TextView) dialoglayout.findViewById(R.id.prefechProgressTxt);
        prefechSuccessView =  dialoglayout.findViewById(R.id.prefechSuccess);
        prefechLayout = (RelativeLayout) dialoglayout.findViewById(R.id.actionBtnLayout);

        btnContiue = (Button) dialoglayout.findViewById(R.id.btnContinue);
        btnCancel = (Button) dialoglayout.findViewById(R.id.btnCancel);
        btnClose = (Button) dialoglayout.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(this);
        btnContiue.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        tileEvent = new DownloadTileEvent();
        tileEvent.addTileListener(this);
    }

    public void startDownload(){
        if(!optionDialog.isShowing()) {
            optionDialog.show();
        }
        startPrefechTask();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCancel:{
                stopPrefechTask();
                optionDialog.dismiss();
                tileEvent.notifyTileEvent(DownloadTileEvent.DownloadTileType.CANCEL);
                break;
            }case R.id.btnContinue:{
                errorMsg.setVisibility(View.GONE);
                btnContiue.setVisibility(View.GONE);
                tileEvent.notifyTileEvent(DownloadTileEvent.DownloadTileType.CONTINUE);
                startPrefechTask();
                break;
            }case R.id.btnClose:{
                tileEvent.notifyTileEvent(DownloadTileEvent.DownloadTileType.CANCEL);
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
        this.currentZoom = currentZoom;
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
    public void onTileEvent(DownloadTileEvent.DownloadTileType event, int currentZoom, int currentMaxProgress, int currentProgress) {
        switch (event){
            case UPDATE:
                updatePrefechProgressTitle(currentZoom,currentMaxProgress,currentProgress);
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

    private  void startPrefechTask(){
        tilesAsync = new FechTilesAsync(activity.getApplicationContext(), path, params);
        tilesAsync.execute(currentZoom);
    }

    private void stopPrefechTask(){
        currentZoom = 0;
        tilesAsync.cancel(true);
    }
}
