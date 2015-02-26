package com.sport365.badminton.entity.resbody;

import java.io.Serializable;

/**
 * Created by kjh08490 on 2015/2/26.
 */
public class AliClientPayResBody implements Serializable {

			/*"parter":"2088412882989360",
			"seller":"1392733498@qq.com",
			"tradeNO":"2090692768",
			"productName":"精羽门费用在线充值",
			"productDescription":"精羽门费用在线充值",
			"amount":"",
			"notifyURL":"http://yundong.shenghuo365.net/AlipayClient/notifyUrl.aspx",
			"privateKey":"2oszcsdv6wu875gg2rt7n6g6z73lf8dw",
			"reqId":"20150209160926"*/

	public String parter;
	public String seller;
	public String tradeNO;
	public String productName;
	public String productDescription;
	public String amount;
	public String notifyURL;
	public String privateKey;
	public String reqId;
	// 客户端返回的xml
	public String requestUionXml;

}
