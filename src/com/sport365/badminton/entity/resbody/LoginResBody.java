package com.sport365.badminton.entity.resbody;//

import java.io.Serializable;

/**
 * Created by vincent on 15/1/31.
 */
public class LoginResBody implements Serializable {

    /**
     * 会员Id,不使用
     */
    public String id;
    /**
     * 会员Id
     */
    public String memberId;

    public String account;//账户名称
    public String userName;// admin ,//用户名
    public String nickname;// admin ,//昵称
    public String gender;// 男 ,//性别
    public String mobile;// 13052892875 //
    public String email;// 1392733498@qq.com ,
    public String qq;// 1392733498 ,
    public String rank;// 1 ,//会员等级
    public String type;// 2 ,//会员类型
    public String provinceId;// 17 ,//省份ID
    public String cityId;// 220 ,//城市ID
    public String countyId;// 2143 ,//行政区域ID
    public String rechargeMoney;// 1501.13 ,//总消费额
    public String consumeMoney;// 1488.13 ,//消费金额
    public String remainderMoney;// 13.00 ,//余额
    public String loginTime;// 371 ,//登录次数
    public String isValid;// 1 ,//是否有效
    public String isDel;// 0 ,//是否删除
    public String lastLoginTime;// 2015-1-31 17:48:01 //最近登录时间

    public String pointValue;
    public String decryptMemberId;
}
