package com.sport365.badminton.http.base;

import java.io.Serializable;



/*
 * response
 * T can be OrderListObject OrderDetailObject
 */
/**
 * @author wj5964
 *
 * @param <T>
 */
public class ResponseTBaseObject<T> implements Serializable{
	private static final long serialVersionUID = -2148419428062958883L;
	
	private ResponseHeaderObject header = new ResponseHeaderObject();
	private T body;
	
	public ResponseTBaseObject(){
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
