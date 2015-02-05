package com.sport365.badminton.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.sport365.badminton.BaseActivity;
import com.sport365.badminton.R;
import com.sport365.badminton.params.SystemConfig;


/**
 * 我的账户
 * Created by vincent on 15/1/31.
 */
public class MyAccountActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_user_Id;
    private TextView tv_user_account;
    private TextView tv_user_name;
    private TextView tv_user_phone;
    private TextView tv_user_password;
    private TextView tv_user_account_money;
    private TextView tv_user_account_total;
    private TextView tv_user_post;
    private RelativeLayout rl_account;
    private RelativeLayout rl_user_name;
    private RelativeLayout rl_phone;
    private RelativeLayout rl_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarTitle("我的账户");
        mActionbar_right.setVisibility(View.GONE);
        setContentView(R.layout.activity_myacount);
        findViews();
        initData();
    }

    private void initData() {
        if (null != SystemConfig.loginResBody && SystemConfig.isLogin()) {
            tv_user_Id.setText(SystemConfig.loginResBody.memberId);
            tv_user_account.setText(SystemConfig.loginResBody.account);
            tv_user_name.setText(SystemConfig.loginResBody.userName);
            tv_user_phone.setText(SystemConfig.loginResBody.mobile);
            tv_user_password.setText("*********");
            tv_user_account_money.setText(SystemConfig.loginResBody.remainderMoney);
            tv_user_account_total.setText(SystemConfig.loginResBody.rechargeMoney);
            tv_user_post.setText(SystemConfig.loginResBody.consumeMoney);
        }
    }

    private void findViews() {
        tv_user_Id = (TextView) findViewById(R.id.tv_user_Id);
        tv_user_account = (TextView) findViewById(R.id.tv_user_account);
        tv_user_name = (TextView) findViewById(R.id.tv_user_name);
        tv_user_phone = (TextView) findViewById(R.id.tv_user_phone);
        tv_user_password = (TextView) findViewById(R.id.tv_user_password);
        tv_user_account_money = (TextView) findViewById(R.id.tv_user_account_money);
        tv_user_account_total = (TextView) findViewById(R.id.tv_user_account_total);
        tv_user_post = (TextView) findViewById(R.id.tv_user_post);
        rl_account = (RelativeLayout) findViewById(R.id.rl_account);
        rl_account.setOnClickListener(this);
        rl_user_name = (RelativeLayout) findViewById(R.id.rl_user_name);
        rl_user_name.setOnClickListener(this);
        rl_phone = (RelativeLayout) findViewById(R.id.rl_phone);
        rl_phone.setOnClickListener(this);
        rl_password = (RelativeLayout) findViewById(R.id.rl_password);
        rl_password.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.rl_account:
                break;
            case R.id.rl_user_name:

                break;
            case R.id.rl_phone:
                startActivity(new Intent(MyAccountActivity.this,ModifyPhoneActivity.class));
                break;
            case R.id.rl_password:

                break;
        }
    }
}
