package com.sport365.badminton.http.base;

import java.util.HashMap;
import java.util.Map;

/*
 * request
 */
public class RequestBaseObject {
	private RequestHeaderObject header = new RequestHeaderObject();
	private Map body = new HashMap();

	public RequestBaseObject() {
		// can load from static object
		ClientInfoObject clientInfo = new ClientInfoObject();
		body.put("clientInfo", clientInfo);
	}

	public RequestHeaderObject getHeader() {
		return header;
	}

	public void setHeader(RequestHeaderObject header) {
		this.header = header;
	}

	public Map getBody() {
		return body;
	}

	public void setBody(Map body) {
		this.body = body;
	}

	public void appendMaptoBody(Map map) {
		this.body.putAll(map);
	}

	public void PartParameter(String serviceName) {
		header.PartParameter(serviceName);
	}
}
