package com.sport365.badminton.entity.resbody;

import java.io.Serializable;

/**
 * Created by kjh08490 on 2015/2/26.
 */
public class WeixinPayResBody implements Serializable {

			/*"tcSerCode":"201502131950472",
			"outOrderNum":"1863609794",
			"amount":"1",
			"channelId":"1",
			"appId":"wx035e7d3896d0787c",
			"partnerId":"1229396101",
			"prePayId":"1201000000150213820a488f73f1fefc",
			"nonceStr":"201502131950472",
			"timeStamp":"1423857047",
			"package":"addition=精羽门费用在线充值&bank_type=WX&body=精羽门费用在线充值&fee_type=1&input_charset=UTF-8&notify_url=http%3a%2f%2fyundong365.net%2fWeixinPay%2fpayNotifyUrl.aspx&out_trade_no=2032021807&partner=1229396101&spbill_create_ip=http://127.0.0.1&total_fee=1&sign=BE761D936D614AB96EC2A7F621427A1F",
			"sign":"3e747822161aa75b257cd8f4fab31713d3e26308",
			"signType":"sha1",
			"accessToken":"MyF9dWMC0pD8nR6-f72GysjcC4qsM0hJwRtECXHkzpsXVQrsK_jtlX0NK_po7PA3g9BPtBkKq3IlT9PbUJ-ka35kcvnJ5THdv6O8luI-FPg",
			"attach":"2&13052892875&1&1863609794",
			"payNotifyUrl":"http://yundong.shenghuo365.net/WeixinPay/payNotifyUrl.aspx"*/

	public String tcSerCode;
	public String outOrderNum;
	public String amount;
	public String channelId;
	public String appId;
	public String prePayId;
	public String partnerId;
	public String nonceStr;
	public String timeStamp;
	public String packageValue;
	public String packageStr;
	public String sign;
	public String signType;
	public String accessToken;
	public String attach;
	public String payNotifyUrl;

}
