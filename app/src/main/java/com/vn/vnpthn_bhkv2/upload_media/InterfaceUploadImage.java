package com.vn.vnpthn_bhkv2.upload_media;

import com.vn.vnpthn_bhkv2.models.ErrorApi;

public interface InterfaceUploadImage {
    interface Presenter {
        void api_upload_image(String part, String name);

    }

    interface View {
        void show_error_api_uploadimage();

        void show_upload_image(ErrorApi objError);
    }
}
