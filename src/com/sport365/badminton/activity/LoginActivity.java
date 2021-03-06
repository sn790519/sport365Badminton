package com.sport365.badminton.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.sport365.badminton.BaseActivity;
import com.sport365.badminton.R;
import com.sport365.badminton.entity.reqbody.LoginReqBody;
import com.sport365.badminton.entity.resbody.LoginResBody;
import com.sport365.badminton.entity.webservice.SportParameter;
import com.sport365.badminton.entity.webservice.SportWebService;
import com.sport365.badminton.http.base.HttpTaskHelper;
import com.sport365.badminton.http.base.IRequestProxyCallback;
import com.sport365.badminton.http.json.req.ServiceRequest;
import com.sport365.badminton.http.json.res.ResponseContent;
import com.sport365.badminton.utils.SystemConfig;
import com.sport365.badminton.utils.Utilities;

/**
 * 登录界面
 * Created by vincent on 15/1/31.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {


    private EditText et_login_name;
    private EditText et_login_pwd;
    private Button btn_login;
    private TextView tv_find_pwd;
    private TextView tv_register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initActionBar();
        findViews();
    }

    private void initActionBar() {
        setActionBarTitle("登录");
        mActionbar_right.setVisibility(View.GONE);
    }

    private void findViews() {
        et_login_name = (EditText) findViewById(R.id.et_login_name);
        et_login_pwd = (EditText) findViewById(R.id.et_login_pwd);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        tv_find_pwd = (TextView) findViewById(R.id.tv_find_pwd);
        tv_find_pwd.setOnClickListener(this);
        tv_register = (TextView) findViewById(R.id.tv_register);
        tv_register.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {
            case R.id.btn_login:
                if (TextUtils.isEmpty(et_login_name.getText().toString())) {
                    Utilities.showToast(getString(R.string.account_hint), mContext);
                    return;
                }
                if (TextUtils.isEmpty(et_login_pwd.getText().toString())) {
                    Utilities.showToast(getString(R.string.account_pasword_hint), mContext);
                    return;
                }
                LoginReqBody reqBody = new LoginReqBody();
                reqBody.mobile = et_login_name.getText().toString();
                reqBody.Password = et_login_pwd.getText().toString();
//				reqBody.mobile = "13052892875";
//				reqBody.Password = "5778763";
                login(reqBody);
                break;
            case R.id.tv_find_pwd:
                Intent i = new Intent(LoginActivity.this, FindPwdActivity.class);
                startActivity(i);
                break;
            case R.id.tv_register:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent, RESULT_FIRST_USER);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_FIRST_USER && null != data && null != data.getExtras()) {
            String phone = data.getExtras().getString("phone");
            if (!TextUtils.isEmpty(phone)) {
                LoginReqBody reqBody = new LoginReqBody();
                reqBody.mobile = phone;
                reqBody.Password = phone;
                login(reqBody);
            }
        }
    }

    private void login(final LoginReqBody reqBody) {
        SportWebService webService = new SportWebService(SportParameter.LOGIN);
        sendRequestWithDialog(new ServiceRequest(mContext, webService, reqBody), null, new IRequestProxyCallback() {

            @Override
            public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
                ResponseContent<LoginResBody> de = jsonResponse.getResponseContent(LoginResBody.class);
                LoginResBody resBody = de.getBody();
                //保存登录状态
                SystemConfig.memberId = resBody.memberId;
                SystemConfig.loginResBody = resBody;
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onError(ResponseContent.Header header, HttpTaskHelper.RequestInfo requestInfo) {
                super.onError(header, requestInfo);
            }
        });
    }


}
