package com.sport365.badminton.http.base;

import com.sport365.badminton.http.json.res.ResponseContent;

/**
 * use for dialog dismiss callback
 */
public interface IRequestProxyListener {

	/**
	 * request success callback
	 * 
	 * @param jsonResponse
	 */
	public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo);

	/**
	 * request failed callback
	 * 
	 * @param header
	 */
	public void onError(ResponseContent.Header header, HttpTaskHelper.RequestInfo requestInfo);

	/**
	 * cancel the request
	 */
	public void onCanceled(HttpTaskHelper.CancelInfo cancelInfo);

}
