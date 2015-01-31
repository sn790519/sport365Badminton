package com.sport365.badminton.activity;

import android.os.Bundle;
import com.sport365.badminton.BaseActivity;
import com.sport365.badminton.R;


/**
 * 我的账户
 * Created by vincent on 15/1/31.
 */
public class MyAcountActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarTitle("我的账户");
        setContentView(R.layout.activity_myacount);

    }
}
