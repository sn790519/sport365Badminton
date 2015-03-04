package com.sport365.badminton.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.sport365.badminton.BaseActivity;
import com.sport365.badminton.R;
import com.sport365.badminton.entity.reqbody.ReqBody;
import com.sport365.badminton.entity.resbody.GetProvinceListResBody;
import com.sport365.badminton.entity.webservice.SportParameter;
import com.sport365.badminton.entity.webservice.SportWebService;
import com.sport365.badminton.http.base.HttpTaskHelper;
import com.sport365.badminton.http.base.IRequestProxyCallback;
import com.sport365.badminton.http.json.req.ServiceRequest;
import com.sport365.badminton.http.json.res.ResponseContent;
import com.sport365.badminton.utils.Utilities;
import com.sport365.badminton.view.wheelwidget.ArrayWheelAdapter;
import com.sport365.badminton.view.wheelwidget.WheelView;

/**
 * 修改信息
 */
public class ModifyUserInfoActivity extends BaseActivity implements View.OnClickListener {
    //初始化时汉字会挤压，在汉字左右两边添加空格即可
    private String[] names;
    private Button btn_login;

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
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {
            case R.id.btn_login:
                getProvinceList();
                break;
        }
    }

    private void getProvinceList() {
        SportWebService webService = new SportWebService(SportParameter.GET_PROVINCE_LIST);
        ReqBody reqBody = new ReqBody();
        sendRequestWithDialog(new ServiceRequest(mContext, webService, reqBody), null, new IRequestProxyCallback() {

            @Override
            public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
                ResponseContent<GetProvinceListResBody> de = jsonResponse.getResponseContent(GetProvinceListResBody.class);
                GetProvinceListResBody resBody = de.getBody();
                int size = resBody.provinceList.size();
                if (size > 0) {
                    names = new String[size];
                    for (int i = 0; i < size; i++) {
                        names[i] = " " + resBody.provinceList.get(i).provinceName + " ";
                    }
                    final AlertDialog dialog = new AlertDialog.Builder(ModifyUserInfoActivity.this).create();
                    dialog.setTitle("选择省份");
                    final WheelView catalogWheel = new WheelView(ModifyUserInfoActivity.this);
                    dialog.setButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
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
                    catalogWheel.setAdapter(new ArrayWheelAdapter<String>(names));
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


}
