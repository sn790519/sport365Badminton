package com.sport365.badminton.http.entity;

import com.sport365.badminton.params.SystemConfig;

/*
 * 客户端信息
 */
public class ClientInfoObject {
	private String versionType;
	private String versionNumber;
	private String deviceId;
	private String clientIp;

	public ClientInfoObject() {
		this.deviceId = SystemConfig.deviceId;
		this.versionNumber = SystemConfig.versionNumber;
		this.versionType = SystemConfig.VersionType;
		this.setClientIp(SystemConfig.IP);
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

}
