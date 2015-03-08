package com.sport365.badminton.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.sport365.badminton.BaseActivity;
import com.sport365.badminton.R;
import com.sport365.badminton.entity.reqbody.GetCityListByProvinceIdReqBody;
import com.sport365.badminton.entity.reqbody.ModifyUserInfoReqBody;
import com.sport365.badminton.entity.reqbody.ReqBody;
import com.sport365.badminton.entity.resbody.City;
import com.sport365.badminton.entity.resbody.GetCityListResBody;
import com.sport365.badminton.entity.resbody.GetProvinceListResBody;
import com.sport365.badminton.entity.resbody.Province;
import com.sport365.badminton.entity.webservice.SportParameter;
import com.sport365.badminton.entity.webservice.SportWebService;
import com.sport365.badminton.http.base.HttpTaskHelper;
import com.sport365.badminton.http.base.IRequestProxyCallback;
import com.sport365.badminton.http.json.req.ServiceRequest;
import com.sport365.badminton.http.json.res.ResponseContent;
import com.sport365.badminton.params.SystemConfig;
import com.sport365.badminton.utils.Utilities;
import com.sport365.badminton.view.wheelwidget.ArrayWheelAdapter;
import com.sport365.badminton.view.wheelwidget.WheelView;
import com.squareup.okhttp.ResponseBody;

/**
 * 修改信息
 */
public class ModifyUserInfoActivity extends BaseActivity implements View.OnClickListener {
    //初始化时汉字会挤压，在汉字左右两边添加空格即可
    private String[] provinces;
    private Province mCurrentProvince = new Province();
    private String[] citys;
    private City mCurrentCity = new City();
    private String[] diqus;
    private City mCurrentDiqu = new City();

    private EditText et_name;
    private EditText et_nickname;
    private TextView tv_sex;
    private EditText et_email;
    private EditText et_qq;
    private TextView tv_province;
    private TextView tv_city;
    private TextView tv_diqu;
    private EditText et_password;
    private Button btn_push;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_user_info);
        initActionBar();
        findViews();
    }

    private void initActionBar() {
        setActionBarTitle("修改个人信息");
        mActionbar_right.setVisibility(View.GONE);
        mActionbar_right_text.setVisibility(View.GONE);
    }

    private void findViews() {
        et_name = (EditText) findViewById(R.id.et_name);
        et_nickname = (EditText) findViewById(R.id.et_nickname);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        tv_sex.setOnClickListener(this);
        et_email = (EditText) findViewById(R.id.et_email);
        et_qq = (EditText) findViewById(R.id.et_qq);
        tv_province = (TextView) findViewById(R.id.tv_province);
        tv_province.setOnClickListener(this);
        tv_city = (TextView) findViewById(R.id.tv_city);
        tv_city.setOnClickListener(this);
        tv_diqu = (TextView) findViewById(R.id.tv_diqu);
        tv_diqu.setOnClickListener(this);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_push = (Button) findViewById(R.id.btn_push);
        btn_push.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {
            case R.id.tv_sex:
                showSexDialog();
                break;
            case R.id.tv_province:
                getProvinceList();
                break;
            case R.id.tv_city:
                getCityList();
                break;
            case R.id.tv_diqu:
                getDiquList();
                break;
            case R.id.btn_push:
                modifyUserInfo();
                break;
        }
    }

    private void showSexDialog() {
        AlertDialog dialog = new AlertDialog.Builder(ModifyUserInfoActivity.this).create();
        final String[] sex = new String[]{"男", "女"};
        dialog.setTitle("选择性别");
        final WheelView catalogWheel = new WheelView(ModifyUserInfoActivity.this);
        dialog.setButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tv_sex.setText(sex[catalogWheel.getCurrentItem()]);
                dialog.dismiss();
            }
        });
        dialog.setButton2("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        catalogWheel.setVisibleItems(5);
        catalogWheel.setCyclic(false);
        catalogWheel.setAdapter(new ArrayWheelAdapter<String>(sex));
        dialog.setView(catalogWheel);
        dialog.show();
    }

    private void getProvinceList() {
        SportWebService webService = new SportWebService(SportParameter.GET_PROVINCE_LIST);
        ReqBody reqBody = new ReqBody();
        sendRequestWithDialog(new ServiceRequest(mContext, webService, reqBody), null, new IRequestProxyCallback() {

            @Override
            public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
                ResponseContent<GetProvinceListResBody> de = jsonResponse.getResponseContent(GetProvinceListResBody.class);
                final GetProvinceListResBody resBody = de.getBody();
                int size = resBody.provinceList.size();
                if (size > 0) {
                    provinces = new String[size];
                    for (int i = 0; i < size; i++) {
                        provinces[i] = " " + resBody.provinceList.get(i).provinceName + " ";
                    }
                    final AlertDialog dialog = new AlertDialog.Builder(ModifyUserInfoActivity.this).create();
                    dialog.setTitle("选择省份");
                    final WheelView catalogWheel = new WheelView(ModifyUserInfoActivity.this);
                    dialog.setButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mCurrentProvince = resBody.provinceList.get(catalogWheel.getCurrentItem());
                            tv_province.setText(mCurrentProvince.provinceName);
                            dialog.dismiss();
                        }
                    });
                    dialog.setButton2("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    catalogWheel.setVisibleItems(5);
                    catalogWheel.setCyclic(false);
                    catalogWheel.setAdapter(new ArrayWheelAdapter<String>(provinces));
                    dialog.setView(catalogWheel);
                    dialog.show();
                }

            }

            @Override
            public void onError(ResponseContent.Header header, HttpTaskHelper.RequestInfo requestInfo) {
                super.onError(header, requestInfo);
                Utilities.showToast(header.getRspDesc(), mContext);
            }
        });
    }

    private void getCityList() {
        GetCityListByProvinceIdReqBody reqBody = new GetCityListByProvinceIdReqBody();
        if (null != mCurrentProvince && !TextUtils.isEmpty(mCurrentProvince.provinceId)) {
            reqBody.provinceId = mCurrentProvince.provinceId;
        } else {
            Utilities.showToast("请先设置省份", ModifyUserInfoActivity.this);
            return;
        }
        SportWebService webService = new SportWebService(SportParameter.GET_CITY_LIST_BY_PROVINCEID);
        sendRequestWithDialog(new ServiceRequest(mContext, webService, reqBody), null, new IRequestProxyCallback() {

            @Override
            public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
                ResponseContent<GetCityListResBody> de = jsonResponse.getResponseContent(GetCityListResBody.class);
                final GetCityListResBody resBody = de.getBody();
                int size = resBody.cityList.size();
                if (size > 0) {
                    citys = new String[size];
                    for (int i = 0; i < size; i++) {
                        citys[i] = " " + resBody.cityList.get(i).cityName + " ";
                    }
                    final AlertDialog dialog = new AlertDialog.Builder(ModifyUserInfoActivity.this).create();
                    dialog.setTitle("选择城市");
                    final WheelView catalogWheel = new WheelView(ModifyUserInfoActivity.this);
                    dialog.setButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mCurrentCity = resBody.cityList.get(catalogWheel.getCurrentItem());
                            tv_city.setText(mCurrentCity.cityName);
                            dialog.dismiss();
                        }
                    });
                    dialog.setButton2("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    catalogWheel.setVisibleItems(5);
                    catalogWheel.setCyclic(false);
                    catalogWheel.setAdapter(new ArrayWheelAdapter<String>(citys));
                    dialog.setView(catalogWheel);
                    dialog.show();
                }

            }

            @Override
            public void onError(ResponseContent.Header header, HttpTaskHelper.RequestInfo requestInfo) {
                super.onError(header, requestInfo);
                Utilities.showToast(header.getRspDesc(), mContext);
            }
        });
    }

    private void getDiquList() {
        GetCityListByProvinceIdReqBody reqBody = new GetCityListByProvinceIdReqBody();
        if (null != mCurrentCity && !TextUtils.isEmpty(mCurrentCity.cityId)) {
            reqBody.provinceId = mCurrentCity.cityId;
        } else {
            Utilities.showToast("请先设置城市", ModifyUserInfoActivity.this);
            return;
        }
        SportWebService webService = new SportWebService(SportParameter.GET_CITY_LIST_BY_PROVINCEID);
        sendRequestWithDialog(new ServiceRequest(mContext, webService, reqBody), null, new IRequestProxyCallback() {

            @Override
            public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
                ResponseContent<GetCityListResBody> de = jsonResponse.getResponseContent(GetCityListResBody.class);
                final GetCityListResBody resBody = de.getBody();
                int size = resBody.cityList.size();
                if (size > 0) {
                    diqus = new String[size];
                    for (int i = 0; i < size; i++) {
                        diqus[i] = " " + resBody.cityList.get(i).cityName + " ";
                    }
                    final AlertDialog dialog = new AlertDialog.Builder(ModifyUserInfoActivity.this).create();
                    dialog.setTitle("选择地区");
                    final WheelView catalogWheel = new WheelView(ModifyUserInfoActivity.this);
                    dialog.setButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mCurrentDiqu = resBody.cityList.get(catalogWheel.getCurrentItem());
                            tv_diqu.setText(mCurrentDiqu.cityName);
                            dialog.dismiss();
                        }
                    });
                    dialog.setButton2("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    catalogWheel.setVisibleItems(5);
                    catalogWheel.setCyclic(false);
                    catalogWheel.setAdapter(new ArrayWheelAdapter<String>(diqus));
                    dialog.setView(catalogWheel);
                    dialog.show();
                }

            }

            @Override
            public void onError(ResponseContent.Header header, HttpTaskHelper.RequestInfo requestInfo) {
                super.onError(header, requestInfo);
                Utilities.showToast(header.getRspDesc(), mContext);
            }
        });

    }


    private void modifyUserInfo() {
        ModifyUserInfoReqBody reqBody = new ModifyUserInfoReqBody();
        reqBody.memberId = SystemConfig.memberId;
        reqBody.provinceId = mCurrentProvince.provinceId;
        reqBody.cityId = mCurrentCity.cityId;
        reqBody.countyId = mCurrentDiqu.cityId;
        reqBody.email = et_email.getText().toString();
        reqBody.qq = et_qq.getText().toString();
        String sex="";
        if("男".equals(tv_sex.getText().toString()))
            sex="1";
        else if("女".equals(tv_sex.getText().toString()))
            sex="0";
        reqBody.gender = sex;
        reqBody.password = et_password.getText().toString();
        reqBody.nickname = et_nickname.getText().toString();
        reqBody.username = et_name.getText().toString();


        SportWebService webService = new SportWebService(SportParameter.MODIFY_MEMBERINFO_BY_ID);
        sendRequestWithDialog(new ServiceRequest(mContext, webService, reqBody), null, new IRequestProxyCallback() {

            @Override
            public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
                ResponseContent<ResponseBody> de = jsonResponse.getResponseContent(ResponseBody.class);
                Utilities.showToast(de.getHeader().getRspDesc(), mContext);
                finish();
            }

            @Override
            public void onError(ResponseContent.Header header, HttpTaskHelper.RequestInfo requestInfo) {
                super.onError(header, requestInfo);
                Utilities.showToast(header.getRspDesc(), mContext);
            }
        });
    }
}
