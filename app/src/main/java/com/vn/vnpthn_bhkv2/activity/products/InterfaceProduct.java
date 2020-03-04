package com.vn.vnpthn_bhkv2.activity.products;


import com.vn.vnpthn_bhkv2.models.CategoryProductHome;
import com.vn.vnpthn_bhkv2.models.respon_api.ResponGetCat;
import com.vn.vnpthn_bhkv2.models.respon_api.ResponGetProduct;
import com.vn.vnpthn_bhkv2.models.respon_api.ResponSubProduct;

/**
 * Created by: Neo Company.
 * Developer: HuyNQ2
 * Date: 21-May-2019
 * Time: 10:09
 * Version: 1.0
 */
public interface InterfaceProduct {
    interface Presenter {
        void api_get_product_cat(String USERNAME, String ID_PARENT);

        void api_get_get_sub_product(String USERNAME, String ID_PARENT);

        void api_get_get_sub_product_child(String USERNAME, String SUB_ID);

        void api_get_product_cat_detail(String USERNAME, String SUB_ID_PARENT, String SUB_ID,
                                        String PAGE, String NUMOFPAGE);

        void api_get_get_product_trend(String USERNAME);

        void api_get_product_by_listid(String USERNAME, String LISTID);
    }

    interface View {
        void show_error_api();

        void show_product_cat(ResponGetCat obj);

        void show_product_sub_product(ResponSubProduct obj);

        void show_product_sub_product_child(ResponSubProduct obj);

        void show_product_cat_detail(ResponGetProduct obj);

        void show_product_trend(CategoryProductHome obj);

        void show_product_by_listid(ResponGetProduct obj);
    }
}