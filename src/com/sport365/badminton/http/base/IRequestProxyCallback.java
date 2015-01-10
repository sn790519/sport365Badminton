package com.sport365.badminton.http.base;

import com.sport365.badminton.http.base.HttpTaskHelper.CancelInfo;
import com.sport365.badminton.http.base.HttpTaskHelper.RequestInfo;
import com.sport365.badminton.http.json.res.ResponseContent.Header;

public abstract class IRequestProxyCallback implements IRequestProxyListener {

	@Override
	public void onError(Header header, RequestInfo requestInfo) {
	}

	@Override
	public void onCanceled(CancelInfo cancelInfo) {
	}
}
