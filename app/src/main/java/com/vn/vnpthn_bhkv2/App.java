package com.vn.vnpthn_bhkv2;

import android.app.Application;

import com.google.gson.Gson;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;
import com.vn.vnpthn_bhkv2.config.Config;
import com.vn.vnpthn_bhkv2.config.Constants;
import com.vn.vnpthn_bhkv2.models.City;
import com.vn.vnpthn_bhkv2.models.Districts;
import com.vn.vnpthn_bhkv2.models.Gender;
import com.vn.vnpthn_bhkv2.models.ObjBankName;
import com.vn.vnpthn_bhkv2.models.ObjCTV;
import com.vn.vnpthn_bhkv2.models.ObjShopInfo;
import com.vn.vnpthn_bhkv2.models.Products;
import com.vn.vnpthn_bhkv2.models.ReportProduct;
import com.vn.vnpthn_bhkv2.models.respon_api.ResponGetReportProduct;
import com.vn.vnpthn_bhkv2.untils.SharedPrefs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by: Neo Company.
 * Developer: HuyNQ2
 * Date: 02-May-2019
 * Time: 11:28
 * Version: 1.0
 */
public class App extends Application {
    public static Products mProduct;
    public static ObjCTV mObjCTV;
    private static App sInstance;
    private Gson mGSon;
    public static List<City> mLisCity;
    public static List<Products> mLisProductCart;
    public static List<ReportProduct> mLisReportProduct;
    public static ResponGetReportProduct mReportProduct;
    public static City mCity;
    public static ObjBankName mBankName;
    public static Districts mDistrict;
    public static Gender mGender;
    public static Boolean isLoginHome;
    public static int mWidth=0;
    public static int mHeight=0;
    public static App self() {
        return sInstance;
    }

    public static Boolean isLoadOrder;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        mGSon = new Gson();
       /* ObjShopInfo objShop = SharedPrefs.getInstance().get(Constants.KEY_SAVE_OBJ_SHOP, ObjShopInfo.class);
        if (objShop != null) {
            Config.ID_SHOP = objShop.getUSER_CODE();
        }*/
        mLisCity = new ArrayList<>();
        mLisReportProduct = new ArrayList<>();
        mLisProductCart = new ArrayList<>();
        isLoginHome = false;
        isLoadOrder = false;
        FileDownloader.setupOnApplicationOnCreate(this)
                .connectionCreator(new FileDownloadUrlConnection
                        .Creator(new FileDownloadUrlConnection.Configuration()
                        .connectTimeout(15_000) // set connection timeout.
                        .readTimeout(15_000) // set read timeout.
                ))
                .commit();
    }
    public Gson getGSon() {
        return mGSon;
    }
}
