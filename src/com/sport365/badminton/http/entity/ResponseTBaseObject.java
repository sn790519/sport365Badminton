package com.sport365.badminton.http.entity;

import java.io.Serializable;

public class ResponseTBaseObject<T> implements Serializable {
	private static final long serialVersionUID = -2148419428062958883L;

	private ResponseHeaderObject header = new ResponseHeaderObject();
	private T body;

	public ResponseTBaseObject() {
	}

	public ResponseHeaderObject getHeader() {
		return header;
	}

	public void setHeader(ResponseHeaderObject header) {
		this.header = header;
	}

	public T getBody() {
		return body;
	}

	public void setBody(T body) {
		this.body = body;
	}
}
