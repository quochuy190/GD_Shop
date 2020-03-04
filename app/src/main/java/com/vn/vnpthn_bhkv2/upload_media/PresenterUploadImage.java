package com.vn.vnpthn_bhkv2.upload_media;

import android.util.Log;

import com.google.gson.Gson;
import com.vn.vnpthn_bhkv2.apiservice_base.ApiServicePostImage;
import com.vn.vnpthn_bhkv2.callback.CallbackData;
import com.vn.vnpthn_bhkv2.models.ErrorApi;
import com.vn.vnpthn_bhkv2.models.respon_api.ResponGetReportDefault;

public class PresenterUploadImage implements InterfaceUploadImage.Presenter {
    private static final String TAG = "PresenterUploadImage";
    ApiServicePostImage mApiService;
    InterfaceUploadImage.View mView;

    public PresenterUploadImage(InterfaceUploadImage.View mView) {
        mApiService = new ApiServicePostImage();
        this.mView = mView;
    }

    @Override
    public void api_upload_image(String part, final String name) {
        mApiService.apiUploadImage(new CallbackData<String>() {
            @Override
            public void onGetDataErrorFault(Exception e) {
                mView.show_error_api_uploadimage();
            }

            @Override
            public void onGetDataSuccess(String objT) {
                Log.i(TAG, "onGetDataSuccess: " + objT);
                try {
                    ErrorApi objEror = new Gson().fromJson(objT, ErrorApi.class);
                    objEror.setsNameImage(name);
                    mView.show_upload_image(objEror);
                } catch (Exception e) {
                    e.printStackTrace();
                    mView.show_error_api_uploadimage();
                    Log.i(TAG, "Log_error_api_filght: " + e);
                }
            }
        }, part);
    }
}
