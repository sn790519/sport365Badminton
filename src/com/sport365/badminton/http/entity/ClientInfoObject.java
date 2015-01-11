package com.sport365.badminton.http.entity;

import com.sport365.badminton.params.SystemConfig;

/*
 * 客户端信息
 */
public class ClientInfoObject {
	private String clientIp;
	private String deviceId;
	private String refId;
	private String versionNumber;
	private String versionType;
	private String networktype;

	public ClientInfoObject() {
		this.setClientIp(SystemConfig.IP);
		this.deviceId = SystemConfig.deviceId;
		this.refId = SystemConfig.refId;
		this.versionNumber = SystemConfig.versionNumber;
		this.versionType = SystemConfig.VersionType;
		this.networktype = SystemConfig.networktype;
	}

	public String getVersionType() {
		return versionType;
	}

	public void setVersionType(String versionType) {
		this.versionType = versionType;
	}

	public String getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public String getNetworktype() {
		return networktype;
	}

	public void setNetworktype(String networktype) {
		this.networktype = networktype;
	}
}
