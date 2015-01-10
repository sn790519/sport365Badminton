package com.sport365.badminton.http;

import java.util.Map;

import com.sport365.badminton.http.base.RequestBaseObject;

/*
 * request
 */
public class RequestObject {
	RequestBaseObject request = new RequestBaseObject();

	public RequestBaseObject getRequest() {
		return request;
	}

	public void setRequest(RequestBaseObject request) {
		this.request = request;
	}

	public void PartParameter(String serviceName) {
		request.PartParameter(serviceName);
	}

	public void appendMaptoBody(Map map) {
		request.appendMaptoBody(map);
	}
}
