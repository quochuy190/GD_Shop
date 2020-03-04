package com.vn.vnpthn_bhkv2.activity.login;

import com.vn.vnpthn_bhkv2.models.ErrorApi;
import com.vn.vnpthn_bhkv2.models.ObjLogin;
import com.vn.vnpthn_bhkv2.models.ObjShopInfo;

/**
 * Created by: Neo Company.
 * Developer: HuyNQ2
 * Date: 02-May-2019
 * Time: 10:37
 * Version: 1.0
 */
public interface InterfaceOTP {
    interface Presenter {
        void api_get_code(String MSISDN);
        void api_active(String ICODE, String MSISDN);

    }

    interface View {
        void show_error_api();

        void show_get_code(ErrorApi obj);

        void show_active_otp(ErrorApi obj);


    }
}
