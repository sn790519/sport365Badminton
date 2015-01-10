package com.sport365.badminton.http.entity;

import java.io.Serializable;

/*
 * Response header
 */
public class ResponseHeaderObject implements Serializable ,Cloneable{
	
	private static final long serialVersionUID = -3938980182162468261L;
	private String rspType;
	private String rspCode;
	private String rspDesc;//先注释 服务器返回的类型不确定
	
	public String getRspType() {
		return rspType;
	}
	public void setRspType(String rspType) {
		this.rspType = rspType;
	}
	public String getRspCode() {
		return rspCode;
	}
	public void setRspCode(String rspCode) {
		this.rspCode = rspCode;
	}
	public String getRspDesc() {
		return rspDesc;
	}
	public void setRspDesc(String rspDesc) {
		this.rspDesc = rspDesc;
	}
	public String getMessage(){
		if ("0".equals(rspType) && "0000".equals(rspCode)) {
			return "0";
		}else
			return rspDesc;
	}
	
	public ResponseHeaderObject copy()
	{
		try {
			return (ResponseHeaderObject) clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
}
