package com.vn.vnpthn_bhkv2.activity.order;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.vn.gd_shop.R;
import com.vn.vnpthn_bhkv2.App;
import com.vn.vnpthn_bhkv2.activity.collaborators.ActivityCtvDetail;
import com.vn.vnpthn_bhkv2.activity.collaborators.InterfaceCollaborators;
import com.vn.vnpthn_bhkv2.activity.collaborators.PresenterCTV;
import com.vn.vnpthn_bhkv2.activity.login.Menu_Search.ActivityDistrict;
import com.vn.vnpthn_bhkv2.activity.login.Menu_Search.ActivityListCity;
import com.vn.vnpthn_bhkv2.activity.tintuc.ActivityDetailNews;
import com.vn.vnpthn_bhkv2.base.BaseActivity;
import com.vn.vnpthn_bhkv2.callback.ClickDialog;
import com.vn.vnpthn_bhkv2.config.Constants;
import com.vn.vnpthn_bhkv2.models.City;
import com.vn.vnpthn_bhkv2.models.CustomEvent;
import com.vn.vnpthn_bhkv2.models.Districts;
import com.vn.vnpthn_bhkv2.models.ErrorApi;
import com.vn.vnpthn_bhkv2.models.ObjCTV;
import com.vn.vnpthn_bhkv2.models.ObjOrder;
import com.vn.vnpthn_bhkv2.models.Products;
import com.vn.vnpthn_bhkv2.models.PropetiObj;
import com.vn.vnpthn_bhkv2.models.respon_api.ResponGetLisCTV;
import com.vn.vnpthn_bhkv2.models.respon_api.ResponGetProduct;
import com.vn.vnpthn_bhkv2.models.respon_api.ResponHistoryOrder;
import com.vn.vnpthn_bhkv2.untils.KeyboardUtil;
import com.vn.vnpthn_bhkv2.untils.PhoneNumberUntil;
import com.vn.vnpthn_bhkv2.untils.SharedPrefs;
import com.vn.vnpthn_bhkv2.untils.StringUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.vn.vnpthn_bhkv2.config.Config.mShipPolicy;


/**
 * Created by: Neo Company.
 * Developer: HuyNQ2
 * Date: 31-May-2019
 * Time: 08:58
 * Version: 1.0
 */
public class ActivityAddOrder extends BaseActivity
        implements InterfaceOrder.View, View.OnClickListener, InterfaceCollaborators.View {
    @BindView(R.id.btn_add_order)
    Button btn_add_order;
    @BindView(R.id.edt_fullname_customer)
    EditText edt_fullname_customer;
    @BindView(R.id.txt_district)
    TextView txt_district;
    @BindView(R.id.edt_phone_customer)
    EditText edt_phone_customer;
    @BindView(R.id.edt_address_customer)
    EditText edt_address_customer;
    @BindView(R.id.txt_city)
    TextView txt_city;
    @BindView(R.id.ll_city_spinner)
    ConstraintLayout ll_city_spinner;
    @BindView(R.id.ll_filter_district)
    ConstraintLayout ll_filter_district;
    @BindView(R.id.txt_commission)
    TextView txt_commission;
    @BindView(R.id.txt_price)
    TextView txt_price;
    @BindView(R.id.txt_transport)
    TextView txt_transport;
    @BindView(R.id.txt_tudathang)
    TextView txt_tudathang;
    @BindView(R.id.edt_price_distcount)
    TextView edt_price_distcount;
    @BindView(R.id.edt_note)
    EditText edt_note;
    private List<Products> mList;
    private PresenterOrder mPresenter;
    private PresenterCTV mPresenterCTV;
    ObjCTV objCTV;

    @Override
    public int setContentViewId() {
        return R.layout.activity_add_order;
    }

    private void initAppbar() {
        txt_tudathang.setVisibility(View.VISIBLE);
        ImageView img_back = findViewById(R.id.img_back);
        TextView txt_title = findViewById(R.id.txt_title);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txt_title.setText("Đặt hàng");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new PresenterOrder(this);
        //KeyboardUtil.hideSoftKeyboard(this);
        mPresenterCTV = new PresenterCTV(this);
        objCity = new City();
        objDistrict = new Districts();
        initAppbar();
        initData();
        initEvent();

    }

    boolean isDelay = false;

    private void initEvent() {
        txt_transport.setOnClickListener(this);
        ll_city_spinner.setOnClickListener(this);
        ll_filter_district.setOnClickListener(this);
        btn_add_order.setOnClickListener(this);
        txt_tudathang.setOnClickListener(this);
        edt_price_distcount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!isDelay) {
                    isDelay = true;
                    //txt_commission.setText(edt_price_distcount.getText().toString());
                    if (edt_price_distcount.length() > 0)
                        set_hh();
                    else
                        txt_commission.setText(hoahong);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isDelay = false;
                        }
                    }, 100);
                }

            }
        });
     /*   edt_price_distcount.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return false;
            }
        });*/
        /*edt_price_distcount.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    set_hh();
                    return true;
                }
                return false;
            }
        });*/
    }

    private void set_hh() {
        try {
            int total_hh = Integer.parseInt(hoahong.trim().replaceAll(",", "")
                    .replaceAll("đ", "").replaceAll("\\.", ""));
            int distcount = Integer.parseInt(edt_price_distcount.getText().toString().trim().replaceAll(",", "")
                    .replaceAll("đ", "").replaceAll("\\.", ""));
            if (distcount <= total_hh) {
                txt_commission.setText(StringUtil.conventMonney_Long("" + (total_hh - distcount)));
                txt_price.setText(StringUtil.conventMonney_Long("" + (lPrice - distcount)));
            } else {
                KeyboardUtil.dismissKeyboard(edt_price_distcount);
                txt_commission.setText("0đ");
                txt_price.setText(StringUtil.conventMonney_Long("" + (lPrice - total_hh)));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        edt_price_distcount.setText(StringUtil.formatNumber_new("" + total_hh));
                    }
                }, 150);
          /*  String sPriceCommission = edt_price_distcount.getText().toString();
            sPriceCommission = sPriceCommission.substring(0, sPriceCommission.length() -1);
            edt_price_distcount.setText(sPriceCommission);
            showDialogNotify("Thông báo", "Số tiền giảm giá phải nhỏ hơn tổng hoa hồng");*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    String hoahong;

    private void initData() {
        objCTV = SharedPrefs.getInstance().get(Constants.KEY_SAVE_OBJ_CTV_APPLICATION, ObjCTV.class);
        hoahong = getIntent().getStringExtra(Constants.KEY_SEND_TOTAL_HOAHONG);
        if (hoahong != null) {
            txt_commission.setText(hoahong);
        } else
            txt_commission.setText("0đ");
        mList = new ArrayList<>();
        if (App.mLisProductCart.size() > 0)
            mList.addAll(App.mLisProductCart);
        if (mList.size() > 0)
            set_price_total();
    }

    City objCity;
    Districts objDistrict;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_order:
                get_api_order();
                break;
            case R.id.txt_tudathang:
                objCTV = SharedPrefs.getInstance().get(Constants.KEY_SAVE_OBJ_CTV_APPLICATION, ObjCTV.class);
                if (objCTV != null) {
                    if (objCTV.getFULL_NAME() != null)
                        edt_fullname_customer.setText(objCTV.getFULL_NAME());
                    else {
                        show_update_account();
                        return;
                    }
                    if (objCTV.getUSERNAME() != null)
                        edt_phone_customer.setText(objCTV.getUSERNAME());
                    else {
                        show_update_account();
                        return;
                    }
                    if (objCTV.getCITY() != null) {
                        txt_city.setText(objCTV.getCITY());
                        objCity.setMATP(objCTV.getCITY_ID());
                        objCity.setNAME(objCTV.getCITY());
                    } else {
                        show_update_account();
                        return;
                    }

                    if (objCTV.getDISTRICT() != null) {
                        txt_district.setText(objCTV.getDISTRICT());
                        objDistrict.setMAQH(objCTV.getDISTRICT_ID());
                        objDistrict.setNAME(objCTV.getDISTRICT());
                        objDistrict.setMATP(objCTV.getCITY_ID());
                    } else {
                        show_update_account();
                        return;
                    }

                    if (objCTV.getADDRESS() != null)
                        edt_address_customer.setText(objCTV.getADDRESS());
                    else {
                        show_update_account();
                        return;
                    }
                }
                break;
            case R.id.txt_transport:
                if (mShipPolicy != null) {
                    Intent intent = new Intent(ActivityAddOrder.this, ActivityDetailNews.class);
                    intent.putExtra(Constants.KEY_SEND_NEWS_TITLE, "Chính sách vận chuyển");
                    intent.putExtra(Constants.KEY_SEND_NEWS_OBJ, mShipPolicy);
                    startActivity(intent);
                } else {
                    showAlertDialog("Thông báo", "Mời bạn liện hệ trực tiếp với shop để biết" +
                            " thêm về chính sách vận chuyển.");
                }
                break;
            case R.id.ll_city_spinner:
                App.mCity = null;
                App.mDistrict = null;
                objCity = null;
                Intent intent_city = new Intent(ActivityAddOrder.this, ActivityListCity.class);
                startActivityForResult(intent_city, Constants.RequestCode.GET_CITY);
                break;
            case R.id.ll_filter_district:
                if (objCity != null) {
                    App.mDistrict = null;
                    Intent intent_district = new Intent(ActivityAddOrder.this, ActivityDistrict.class);
                    intent_district.putExtra(Constants.KEY_SEND_ID_CITY, objCity.getMATP());
                    startActivityForResult(intent_district, Constants.RequestCode.GET_DISTRICT);
                } else
                    showDialogNotify("Thông báo", "Bạn chưa chọn tỉnh thành phố nào.");
                break;
        }
    }

    private void show_update_account() {
        showDialogComfirm("Thông báo",
                "Bạn cần cập nhật đầy đủ thông tin cá nhân để có thể tự đặt hàng.", true,
                new ClickDialog() {
                    @Override
                    public void onClickYesDialog() {
                        //  startActivity(new Intent(ActivityAddOrder.this, ActivityCtvDetail.class));
                        Intent intent = new Intent(ActivityAddOrder.this, ActivityCtvDetail.class);
                        intent.putExtra(Constants.KEY_SEND_UPDATE_USER, true);
                        startActivityForResult(intent, Constants.RequestCode.GET_UPDATE_CTV);
                    }

                    @Override
                    public void onClickNoDialog() {

                    }
                });
    }

    String sFullName = "", sPhone = "", sAddress = "", sCity = "", sDistrict = "", sUsername = "",
            sCODE_PRODUCT = "", sAMOUNT = "", sPRICE = "", sMONEY = "", sBONUS = "", sDistcount = "",
            sThuoctinh = "", sNote = "";

    private void get_api_order() {
        if (edt_fullname_customer.getText().toString().length() > 0) {
            sFullName = edt_fullname_customer.getText().toString();
        } else {
            showDialogNotify("Thông báo", "Mời nhập vào tên khách hàng");
            KeyboardUtil.requestKeyboard(edt_fullname_customer);
            return;
        }
        if (edt_phone_customer.getText().toString().length() > 0) {
            sPhone = edt_phone_customer.getText().toString();
        } else {
            showDialogNotify("Thông báo", "Mời nhập vào số điện thoại khách hàng");
            KeyboardUtil.requestKeyboard(edt_phone_customer);
            return;
        }
        if (!PhoneNumberUntil.isPhoneNumberNew(edt_phone_customer.getText().toString())) {
            showDialogNotify("Thông báo", "Số điện thoại không đúng định dạng");
            return;
        }
        if (objCity != null && objCity.getMATP() != null) {
            sCity = objCity.getMATP();
        } else {
            showDialogNotify("Thông báo", "Mời nhập vào thông tin tỉnh/thành phố");
            return;
        }
        if (objDistrict != null && objDistrict.getMAQH() != null) {
            sDistrict = objDistrict.getMAQH();
        } else {
            showDialogNotify("Thông báo", "Mời nhập vào thông tin quận/huyện");
            return;
        }
        if (edt_address_customer.getText().toString().length() > 0) {
            sAddress = edt_address_customer.getText().toString();
        } else {
            showDialogNotify("Thông báo", "Mời nhập vào địa chỉ khách hàng");
            KeyboardUtil.requestKeyboard(edt_address_customer);
            return;
        }
        if (mList != null && mList.size() > 0) {
            for (int i = 0; i < mList.size(); i++) {
                if (mList.get(i).getsQuantum() != null && mList.get(i).getsQuantum().length() > 0) {
                    Products obj = mList.get(i);
                    int iQuantum = Integer.parseInt(mList.get(i).getsQuantum());
                    if (iQuantum > 0) {
                        if (obj.getmLisPropeti() != null && obj.getmLisPropeti().size() > 0) {
                            String sPro = "";
                            for (PropetiObj.PropetiDetail objPropeti : obj.getmLisPropeti()) {
                                sPro = sPro + objPropeti.getSUB_ID() + ",";
                            }
                            sThuoctinh = sThuoctinh + sPro.substring(0, sPro.length() - 1) + "#";
                        } else {
                            sThuoctinh = sThuoctinh + "" + "#";
                        }
                        sCODE_PRODUCT = sCODE_PRODUCT + mList.get(i).getCODE_PRODUCT() + "#";
                        sAMOUNT = sAMOUNT + mList.get(i).getsQuantum() + "#";
                        if (obj.getPRICE_PROMOTION() != null) {
                            if (obj.getSTART_PROMOTION() != null && obj.getEND_PROMOTION() != null) {
                                sPRICE = sPRICE + mList.get(i).getPRICE_PROMOTION() + "#";
                            } else {
                                sPRICE = sPRICE + mList.get(i).getsPrice() + "#";
                            }
                        } else {
                            sPRICE = sPRICE + mList.get(i).getsPrice() + "#";
                        }
                        sMONEY = sMONEY + (iQuantum * (Integer.parseInt(mList.get(i).getsPrice()))) + "#";
                        if (mList.get(i).getCOMMISSION() != null) {
                            long commission = (Integer.parseInt(mList.get(i).getsQuantum())
                                    * Integer.parseInt(mList.get(i).getCOMMISSION())
                                    * Long.parseLong(mList.get(i).getsPrice())) / 100;
                            sBONUS = sBONUS + commission + "#";
                        }

                    }
                }
            }

        }
        sUsername = SharedPrefs.getInstance().get(Constants.KEY_SAVE_USERNAME, String.class);
        if (sThuoctinh.length() > 1)
            sThuoctinh = sThuoctinh.substring(0, (sThuoctinh.length() - 1));
        if (sCODE_PRODUCT.length() > 1)
            sCODE_PRODUCT = sCODE_PRODUCT.substring(0, (sCODE_PRODUCT.length() - 1));
        if (sBONUS.length() > 1)
            sBONUS = sBONUS.substring(0, (sBONUS.length() - 1));
        if (sPRICE.length() > 1)
            sPRICE = sPRICE.substring(0, (sPRICE.length() - 1));
        if (sAMOUNT.length() > 1)
            sAMOUNT = sAMOUNT.substring(0, (sAMOUNT.length() - 1));
        if (sMONEY.length() > 1)
            sMONEY = sMONEY.substring(0, (sMONEY.length() - 1));
        if (edt_price_distcount.getText().toString().length() > 0) {
            sDistcount = edt_price_distcount.getText().toString().trim()
                    .replaceAll(",", "")
                    .replaceAll("\\.", "")
                    .replaceAll("đ", "")
            ;
        }
        if (edt_note.getText().toString().length() > 0) {
            sNote = edt_note.getText().toString().trim();
        }
        showDialogLoading();
        mPresenter.api_order_product_3(sUsername, sCODE_PRODUCT, sAMOUNT, sPRICE, sMONEY, sBONUS, sDistcount, sNote,
                sThuoctinh, sFullName, sPhone, sCity, sDistrict, sAddress);
    }

    @Override
    public void show_error_api() {
        delete_data();
        hideDialogLoading();
        showAlertErrorNetwork();
    }

    @Override
    public void show_get_list_ctv(ResponGetLisCTV obj) {

    }

    @Override
    public void show_update_ctv(ErrorApi obj) {

    }

    @Override
    public void show_reset_pass_ctv(ErrorApi obj) {

    }

    @Override
    public void show_ctv_detail(ObjCTV objLogin) {
        hideDialogLoading();
    }

    @Override
    public void show_list_ctv_child(ResponGetLisCTV objLogin) {

    }

    @Override
    public void show_order_history(ResponHistoryOrder obj) {

    }

    @Override
    public void show_order_history_detail(ObjOrder obj) {

    }


    @Override
    public void show_order_history_detail_pd(ResponGetProduct obj) {

    }

    @Override
    public void show_edit_order_product(ErrorApi obj) {

    }

    @Override
    public void show_order_product(ErrorApi obj) {
        hideDialogLoading();
        if (obj.getsERROR().equals("0000")) {
            Toast.makeText(this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK, new Intent());
            EventBus.getDefault().post(new CustomEvent("0"));
            SharedPrefs.getInstance().put(Constants.KEY_SAVE_LIST_CART, null);
            finish();
        } else {
            delete_data();
            showDialogNotify("Thông báo", obj.getsRESULT());
        }

    }

    private void delete_data() {
        sFullName = "";
        sPhone = "";
        sAddress = "";
        sCity = "";
        sDistrict = "";
        sUsername = "";
        sCODE_PRODUCT = "";
        sAMOUNT = "";
        sPRICE = "";
        sMONEY = "";
        sBONUS = "";
        sThuoctinh = "";
    }

    @Override
    public void show_config_commission(ErrorApi obj) {
        hideDialogLoading();
        if (obj.getsERROR().equals("0000")) {
           /* if (obj.getVALUE() != null && obj.getVALUE().length() > 0) {
                int iComiss = Integer.parseInt(obj.getVALUE());
                lCommission = (iComiss * lPrice) / 100;
                txt_commission.setText(StringUtil.conventMonney_Long("" + lCommission));
                //  txt_price.setText(StringUtil.conventMonney_Long("" + lCommission));
            }*/
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.RequestCode.GET_UPDATE_CTV:
                showDialogLoading();
                String sUser = SharedPrefs.getInstance().get(Constants.KEY_SAVE_USERNAME, String.class);
                mPresenterCTV.api_get_ctv_detail(sUser, sUser,"");

                break;
            case Constants.RequestCode.GET_CITY:
                if (App.mCity != null) {
                    objCity = App.mCity;
                    txt_city.setText(App.mCity.getNAME());
                } else {
                    txt_city.setText("");
                    txt_district.setText("");
                }

                break;
            case Constants.RequestCode.GET_DISTRICT:
                if (App.mDistrict != null) {
                    objDistrict = App.mDistrict;
                    txt_district.setText(App.mDistrict.getNAME());
                } else
                    txt_district.setText("");
                break;
        }
    }

    long lPrice = 0;
    long lCommission = 0;

    private void set_price_total() {
        for (Products obj : mList) {
            if (obj != null && obj.getsQuantum() != null && obj.getsQuantum().length() > 0 && obj.getsPrice() != null) {
                if (obj.getPRICE_PROMOTION() != null) {
                    if (obj.getSTART_PROMOTION() != null && obj.getEND_PROMOTION() != null) {
                        lPrice = lPrice + (Integer.parseInt(obj.getsQuantum()) *
                                Integer.parseInt(obj.getPRICE_PROMOTION()));
                    } else {
                        lPrice = lPrice + (Integer.parseInt(obj.getsQuantum()) * Integer.parseInt(obj.getsPrice()));
                    }
                } else {
                    lPrice = lPrice + (Integer.parseInt(obj.getsQuantum()) * Integer.parseInt(obj.getsPrice()));
                }
                //   lPrice = lPrice + (Integer.parseInt(obj.getsQuantum()) * Integer.parseInt(obj.getsPrice()));

               /* if (obj.getCOMMISSION() != null) {
                    long pire = Integer.parseInt(obj.getCOMMISSION()) *
                            (Integer.parseInt(obj.getsQuantum()) * Integer.parseInt(obj.getsPrice()));
                    lCommission = lCommission + pire / 100;
                }*/
            }
        }
        String sUserName = SharedPrefs.getInstance().get(Constants.KEY_SAVE_USERNAME, String.class);
        //mPresenter.api_get_config_commission(sUserName, "" + lPrice);
        //  txt_commission.setText(StringUtil.conventMonney_Long("" + lCommission));
        txt_price.setText(StringUtil.conventMonney_Long("" + lPrice));
    }
}
