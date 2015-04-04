package com.sport365.badminton.entity.reqbody;

/**
 * 活动的报名的请求参数
 * 
 * @author Frank
 * 
 */
public class ActiveregistReqBody {
	/*
	 * {"request":
	 * {"header":{"accountID":"e10e97c7-455b-4614-9c92-ffe5691d2cf0",
	 * "serviceName":"activeregist",
	 * "version":"20150330180551","reqTime":"2015-03-30 18:05:51",
	 * "digitalSign":"ba99dcef6cc7e876ce6ba27533164164"},
	 * "body":{"clientInfo":{"deviceId"
	 * :"c26b007f-c89e-431a-b8cc-493becbdd8a2","versionNumber":"1.0.0",
	 * "versionType":"wap","refId":"5866720","clientIp":"10.228.237.141"},
	 * "memberId"
	 * :"69d400e31c076ef28ad053a2e87b96d8","activeId":"457","typeId":"0"}}}
	 */
	public String memberId;
	public String activeId;
	public String typeId;

}
