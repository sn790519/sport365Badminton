package com.sport365.badminton.utils;

import android.text.TextUtils;
import android.util.DisplayMetrics;
import com.sport365.badminton.entity.resbody.LoginResBody;

public class SystemConfig {


    /**
     * debug模式
     */
    public static final boolean DEBUG = true;

    /**
     * 百度API_KEY
     */
    public static final String BAIDU_AK = "j93yEwcmukIygNxB7djG7YSb";

    /**
     * 微信appid
     */
    public static final String WEIXIN_APP_ID = "wx035e7d3896d0787c";
    public static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;
    /** 商家向财付通申请的商家id */
    public static final String PARTNER_ID = "1229396101";
    /**
     * 是否显示log
     */
    public static final boolean IS_OPEN_LOG_INFO = true;

    /**
     * 是否打开debug
     */
    public static final boolean IS_OPEN_DEBUG = true;

    /**
     * 网络请求地址
     */
    public static final String HostName = "http://yundong.shenghuo365.net/";

    /**
     * 网络状态 *
     */
    public static int mNetWorkState = 0;

    /**
     * 设备号 *
     */
    public static String deviceId = "68a116b07394858b";

    /**
     * sp的名字 *
     */
    public static String PREFERENCES_NAME = "badmintion";

    /**
     * 会员memberId *
     */
    public static String memberId = "";

    /**
     * 会员用户名 *
     */
    public static String userName;


    public static String aboutUs;
    public static String contactUs;

    /**
     * 版本号 *
     */
    public static String versionNumber = "1.0";

    public static final String EXCEPTION_DATADIR = "exception";

    public static final String STACKTRACE_DATADIR = "stacktrace";
    /**
     * 版本号 *
     */
    public static final String VersionType = "android";

    public static String IP = "10.228.237.141";

    public static String refId = "5866720";

    public static String networktype = "wifi";

    public static final String APIACCOUNTID = "c26b007f-c89e-431a-b8cc-493becbdd8a2";// "7187c05f-951b-4ca6-888a-b8edf8c6679f";
    public static final String APIACCOUNTKEY = "8874d8a8b8b391fbbd1a25bda6ecda11";// "fb13a093870f2028";
    public static final String APIAVERSION = "20111128102912";


    /**
     * 判断是否登陆
     *
     * @return
     */
    public static boolean isLogin() {
        return !TextUtils.isEmpty(memberId);
    }

    /**
     * 清空登录数据
     */
    public static void clearData() {
        memberId = "";
        loginResBody = null;
    }

    public static LoginResBody loginResBody;

    public static String url_end = "?memberId=";

    public static DisplayMetrics dm;

    // 首页默认的请求城市的id
    public static final String CITYID = "226";


    //合作者身份(PARTNER)
    public static String PARTNER = "2088412882989360";
    //安全校验码(Key)默认加密
    public static String ZFB_Key = "py3lk83dan4a8ae296rr27cofjdf0eqx";
    //商户私钥
    public static String private_key = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKhjS62gxkxfjEINg8zoEnbfNZd3THx7U3pbTShiH1EGw8+Hyv87j5Q1KeeL6jTxBwXDBy7dP8ygP+yFl/f2uWZoyxe153gPbWvr2zArQcku7Uf+K4uGmSGnVMSW/sZQhQJ/chjGJgHFpumabs70SBatSiX7JzBIYQNb9hWHHS9BAgMBAAECgYAlqggsbxDL1ndl8uXvOF2g7y5qkcmu8lrqvG9WwBPnKM81pVcXnffwm6+i1h3t95eth0EdD2oM2C9UNVGXqj0vuQaD7iE/5t/OwSVWmtdLz/IQ229zQBGWZBlj7SLGejvC8rGXaOZjO1OHtfalF+BdXLkdKv1d5mQ4XqorBgRVEQJBANXzFFKpRpINIpI8AAi+YHeCRWf6QGJRgMZCKk/Zuw3cOXVROXvmIXAPP9fNiy9e3qHse+9ufkRntO9zOQkbPBUCQQDJe8VYbgaYyB1TMY4w57WAAgWd2o6Tzj90b1oOBrdh2GNbrKHYIMD1BcDkGgHwgoLJkmgMewP+hekaBVZ9frV9AkBXlBBuoeJEaOIM8EinS57bbNUScgsm8+v9vBnr49nBG7PdhSk9wX9Qir18jiP6eSAzMkWtRhKj17vmBMmFI8PdAkAVQMILVufJO+qj+Ok3zWk+zHfNM92wR6Q34vsL2beEUbABXo9f3eycq/Ox2/Byb0Heg1wIXP2J3pQ9E20RJcl9AkEAyvMWrlmjB3ZaIK7JzmLtng8+bu84gTrwRgHTKAve6F1ozey5PNv6i4BpeLAZXttOwbWBVrkqhffWrzpNl4ConA==";
    //商户公钥:
    public static String public_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";


}
