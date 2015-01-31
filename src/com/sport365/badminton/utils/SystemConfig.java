package com.sport365.badminton.utils;

import android.text.TextUtils;

/**
 * Created by vincent on 15/1/31.
 */
public class SystemConfig {

    public static  String memberId="";

    public boolean isLogin()
    {
        return TextUtils.isEmpty(memberId);
    }

}
