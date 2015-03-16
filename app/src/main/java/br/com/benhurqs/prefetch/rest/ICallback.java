package br.com.benhurqs.prefetch.rest;

import android.graphics.Bitmap;

/**
 * Created by benhur on 16/03/15.
 */
public interface ICallback {
    void onSuccess(Bitmap image);
    void onFailure(String errorString);
}