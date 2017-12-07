package com.qixing.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnDismissListener;
import com.bigkoo.alertview.OnItemClickListener;
import com.google.gson.Gson;
import com.qixing.R;
import com.qixing.app.AppManager;
import com.qixing.app.BaseActivity;
import com.qixing.app.MyApplication;
import com.qixing.bean.BankNameBean;
import com.qixing.bean.ReviseBankJson;
import com.qixing.global.Constant;
import com.qixing.utlis.PhoneUtils;
import com.qixing.view.titlebar.BGATitlebar;
import com.qixing.widget.Toasts;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by wicep on 2015/12/23.
 * 银行卡增加
 */
public class MyBankAddActivity extends BaseActivity implements OnWheelChangedListener,OnItemClickListener, OnDismissListener {

    private BGATitlebar mTitleBar;

    private Button btn_add;

    private EditText edit_kh_ren,edit_name,edit_address,edit_carddeposit,edit_bankcode,edit_bankcode2,edit_bank_tel;

    private String str_kh_ren,str_name,str_address,str_carddeposit,str_bankcode,str_bankcode2,str_bank_tel;

    private ReviseBankJson mReviseBankJson;

    private WheelView mViewProvince;
    private WheelView mViewCity;
    private WheelView mViewDistrict;

    /**
     * 精仿iOSAlertViewController
     */
    private AlertView mAlertView;//避免创建重复View，先创建View，然后需要的时候show出来，推荐这个做法
    private AlertView mAlertViewExt;//窗口拓展例子


    private BankNameBean mBankNameBean;
    private String[] strName;
    private String strNamebank;
    private List<String> listName = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.activity_my_bank_add, null);
        setContentView(view);
        initBackListener(getWindow().getDecorView().findViewById(android.R.id.content));

        initView();
//        initDatas();
        getBankName();
    }

    private void initView(){
        mTitleBar= (BGATitlebar) findViewById(R.id.mTitleBar);
        mTitleBar.setDelegate(new BGATitlebar.BGATitlebarDelegate(){

            @Override
            public void onClickLeftCtv() {
                super.onClickLeftCtv();

                AppManager.getAppManager().finishActivity();
            }


            @Override
            public void onClickRightCtv() {
                   super.onClickRightCtv();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                // 关闭软键盘
                imm.hideSoftInputFromWindow(edit_kh_ren.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(edit_name.getWindowToken(), 0);
                str_kh_ren = edit_kh_ren.getText().toString().trim();
                str_name = edit_name.getText().toString().trim();
                str_address = edit_address.getText().toString().trim();
                str_carddeposit = edit_carddeposit.getText().toString().trim();
                str_bankcode = edit_bankcode.getText().toString().trim();
                str_bankcode2 = edit_bankcode2.getText().toString().trim();
                str_bank_tel = edit_bank_tel.getText().toString().trim();

                //        接口url：tjbank，id：用户id，bankcode：卡号，kh_ren：开户人，name：银行名，carddeposit：开户行，sfdq_tj：省，csdq_tj：市，qydq_tj：区，bank_tel：手机号
//                edit_kh_ren,edit_name,edit_address,edit_carddeposit,edit_bankcode,edit_bankcode2,edit_bank_tel;
//                str_kh_ren,str_name,str_address,str_carddeposit,str_bankcode,str_bankcode2,str_bank_tel;

                if(TextUtils.isEmpty(str_kh_ren)){
                    Toasts.show("用户名不能为空");
                }else if (TextUtils.isEmpty(str_name)){
                    Toasts.show("银行不能为空");
                }else if (TextUtils.isEmpty(edit_address.getText().toString().trim())){
                    Toasts.show("所在地不能为空");
                }else if (TextUtils.isEmpty(str_carddeposit)){
                    Toasts.show("开户行不能为空");
                }else if (TextUtils.isEmpty(str_bankcode)){
                    Toasts.show("卡号不能为空");
                }else if (TextUtils.isEmpty(str_bank_tel)){
                    Toasts.show("手机号不能为空");
                }else if(!PhoneUtils.isMobileNO(str_bank_tel)){
                    Toasts.show("您输入的手机号码格式不正确");
                }else if (!str_bankcode.equals(str_bankcode2)){
                    Toasts.show("两次卡号输入不一致");
                }else{
                    initDatas();
                }
            }
        });
//        edit_kh_ren,edit_name,edit_address,edit_carddeposit,edit_bankcode,edit_bankcode2,edit_bank_tel;
        edit_kh_ren = (EditText)findViewById(R.id.my_bank_add_edit_kh_ren);
        edit_name = (EditText)findViewById(R.id.my_bank_add_edit_name);
        edit_name.setOnClickListener(this);
        edit_address = (EditText)findViewById(R.id.my_bank_add_edit_address);
        edit_address.setOnClickListener(this);
        edit_carddeposit = (EditText)findViewById(R.id.my_bank_add_edit_carddeposit);
        edit_bankcode = (EditText)findViewById(R.id.my_bank_add_edit_bankcode);
        edit_bankcode2 = (EditText)findViewById(R.id.my_bank_add_edit_bankcode2);
        edit_bank_tel = (EditText)findViewById(R.id.my_bank_add_edit_bank_tel);

        btn_add = (Button)findViewById(R.id.my_bank_add_add);
        btn_add.setText("添加银行卡");
        btn_add.setOnClickListener(this);
        btn_add.setVisibility(View.GONE);
    }

//new String[]{"高亮按钮1", "高亮按钮2", "高亮按钮3"}
    public void alertNameShow(String[] strName) {
        mAlertView = new AlertView("选择银行", null, null, null,strName,this, AlertView.Style.Alert, this);
        mAlertView.setMarginBottom(40, 80);
        mAlertView.show();
    }


    private void getBankName(){

        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setMessage("加载中...");
            mProgressDialog.show();
        }
        RequestParams params = new RequestParams();
//        params.put("id",  MyApplication.getInstance().getUid());
        final String url = Constant.BASE_URL+Constant.URL_USERAPI_BANKLIST;//
        System.out.println("===========================个人中心银行卡url = " + url);
        mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                System.out.println("===========================个人中心银行卡 response = " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mBankNameBean = new Gson().fromJson(response.toString(), BankNameBean.class);
                    if (mBankNameBean.getResult().equals("1")) {
                        for (int i = 0; i < mBankNameBean.getBanklist().size(); i++) {
                            listName.add(mBankNameBean.getBanklist().get(i).getBank_name());
                        }
                        strName = (String[]) listName.toArray(new String[listName.size()]);
                    } else {
                        Toasts.show(mBankNameBean.getMessage());
                    }
//                    String[] array = (String[])listName.toArray(new String[size]);
                } else {
                    showErrorDialog(mContext);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                showErrorDialog(mContext);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                System.out.println("===========================throwable ,responseString =  " + responseString);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                showTimeoutDialog(mContext);
            }
        });
    }


    private void initDatas(){
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setMessage("加载中...");
            mProgressDialog.show();
        }
//        用户id	uid 卡号	bankcode 开户名	kh_ren 银行名	name 开户行	carddeposit 省	sfdq_tj 市	csdq_tj 区	qydq_tj 预留电话	bank_tel
//        edit_kh_ren,edit_name,edit_address,edit_carddeposit,edit_bankcode,edit_bankcode2,edit_bank_tel;

        final RequestParams params = new RequestParams();
        params.put("uid",  MyApplication.getInstance().getUid());
        params.put("bankcode", edit_bankcode.getText().toString().trim());//        卡号	bankcode
        params.put("kh_ren",  edit_kh_ren.getText().toString().trim());//        开户名	kh_ren
        params.put("name", strNamebank);//银行名strNamebank  //        银行名	name
        params.put("carddeposit",  edit_carddeposit.getText().toString().trim());    //开户行  carddeposit
        params.put("sfdq_tj", mCurrentProviceName);//省mCurrentProviceName  //        省	sfdq_tj
        params.put("csdq_tj",  mCurrentCityName);//市mCurrentCityName   //        市	csdq_tj
        params.put("qydq_tj",  mCurrentDistrictName);//区mCurrentDistrictName  //        区	qydq_tj
        params.put("bank_tel",  edit_bank_tel.getText().toString().trim());//        预留电话	bank_tel

        final String url = Constant.BASE_URL+Constant.URL_USERAPI_TJBANK;//
        System.out.println("===========================个人中心增加银行卡url = " + url);
        System.out.println("===========================params : " + params.toString());
        mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }

                System.out.println("===========================个人中心增加银行卡 response = " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mReviseBankJson = new Gson().fromJson(response.toString(), ReviseBankJson.class);
                    if (mReviseBankJson.getResult().equals("1")) {
                        AppManager.getAppManager().finishActivity();
                        Toasts.show(mReviseBankJson.getMessage());
                    } else {
                        Toasts.show(mReviseBankJson.getMessage());
                    }
                } else {
                    showErrorDialog(mContext);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                showErrorDialog(mContext);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                System.out.println("===========================throwable ,responseString =  " + responseString);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                showTimeoutDialog(mContext);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        initProvinceDatas();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.my_bank_add_edit_name://
                if("".equals(strName)||strName == null){
                    Toasts.show("银行卡列表获取失败！");
                } else {
                    alertNameShow(strName);
                }
                break;
            case R.id.my_bank_add_edit_address://
                createDialog();
                break;
            case R.id.my_bank_add_add://
//                banks();
                break;

        }
    }

    MaterialDialog mMaterialDialog;

    public void createDialog() {

        mMaterialDialog = new MaterialDialog(mContext);

        if (mMaterialDialog != null) {

            mMaterialDialog.setTitle("选择城市");
            LinearLayout layout = (LinearLayout) MyBankAddActivity.this.getLayoutInflater().inflate(R.layout.dialog_province, null);

            mViewProvince = (WheelView) layout.findViewById(R.id.id_province);
            mViewCity = (WheelView) layout.findViewById(R.id.id_city);
            mViewDistrict = (WheelView) layout.findViewById(R.id.id_district);
            mViewProvince.addChangingListener(this);
            mViewCity.addChangingListener(this);
            mViewDistrict.addChangingListener(this);
            mMaterialDialog.setContentView(layout);

            mMaterialDialog.setPositiveButton(R.string.ok, new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mMaterialDialog.dismiss();
                    edit_address.setText(mCurrentProviceName + mCurrentCityName + mCurrentDistrictName);
                }
            });

            mMaterialDialog.setNegativeButton(R.string.cancel, new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mMaterialDialog.dismiss();
                }
            });
        }
        setUpdata();
        mMaterialDialog.show();
    }

    private void setUpdata() {

        mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(
                mContext, mProvinceDatas));

        mViewProvince.setVisibleItems(5);
        mViewCity.setVisibleItems(5);
        mViewDistrict.setVisibleItems(5);
        updateCities();
        updateAreas();
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == mViewProvince) {
            updateCities();

        } else if (wheel == mViewCity) {
            updateAreas();
        } else if (wheel == mViewDistrict) {
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentProviceName + mCurrentCityName)[newValue];

            mCurrentZipCode = mZipcodeDatasMap.get(mCurrentProviceName + mCurrentCityName + mCurrentDistrictName);

            System.out.println("---------------当前区的编号是"+mCurrentZipCode);
        }
    }

    //province_data.xml
    private void updateCities() {

        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
        }

        mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(mContext,
                cities));
        mViewCity.setCurrentItem(0);
        updateAreas();
    }


    private void updateAreas() {

        int pCurrent = mViewCity.getCurrentItem();
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        String[] areas = mDistrictDatasMap.get(mCurrentProviceName + mCurrentCityName);

        if (areas == null) {
            areas = new String[]{""};

        }


        //System.out.println("---------当前区的数据是"+Arrays.toString(areas));
        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(
                mContext, areas));
        mViewDistrict.setCurrentItem(0);
        mCurrentDistrictName = areas[0];
    }

    @Override
    public void onDismiss(Object o) {
//        closeKeyboard();
    }

    @Override
    public void onItemClick(Object o, int position) {
//判断是否是拓展窗口View，而且点击的是非取消按钮
//        if(o == mAlertViewExt && position != AlertView.CANCELPOSITION){
//            String name = etName.getText().toString();
//            if(name.isEmpty()){
//                Toast.makeText(this, "啥都没填呢", Toast.LENGTH_SHORT).show();
//            }
//            else{
//                Toast.makeText(this, "hello,"+name, Toast.LENGTH_SHORT).show();
//            }
//
//            return;
//        }
//        Toasts.show("点击了第" + position + "个");
        strNamebank = strName[position];
        edit_name.setText(strName[position]);
        if(position==0){

        }else{

        }
    }
}
