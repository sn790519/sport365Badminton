package com.sport365.badminton.http.base;

import com.sport365.badminton.params.SystemConfig;
import com.sport365.badminton.utils.Tools;

/*
 * Request header
 */
public class RequestHeaderObject {
	private String version;
	private String accountID;
	private String serviceName;
	private String digitalSign;
	private String reqTime;

	/*
	 * 初始化部分参数
	 */
	public RequestHeaderObject() {
		this.version = SystemConfig.APIAVERSION;
		this.accountID = SystemConfig.APIACCOUNTID;
	}

	/*
	 * 实例化剩余参数
	 */
	public void PartParameter(String serviceName) {
		this.serviceName = serviceName;
		reqTime = String.valueOf(System.currentTimeMillis());// ymdhmsf.format(date);

		String[] checkvalues = { "Version=" + version,
				"AccountID=" + accountID, "ServiceName=" + serviceName,
				"ReqTime=" + reqTime };

		String[] string = Tools.BubbleSort(checkvalues);

		digitalSign = Tools.GetMD5ByArray(string, SystemConfig.APIACCOUNTKEY,
				"utf-8");
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getAccountID() {
		return accountID;
	}

	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getDigitalSign() {
		return digitalSign;
	}

	public void setDigitalSign(String digitalSign) {
		this.digitalSign = digitalSign;
	}

	public String getReqTime() {
		return reqTime;
	}

	public void setReqTime(String reqTime) {
		this.reqTime = reqTime;
	}

}
