package com.sport365.badminton;

import android.support.v4.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;

import com.sport365.badminton.http.base.DialogConfig;
import com.sport365.badminton.http.base.HttpTaskHelper;
import com.sport365.badminton.http.base.IRequestListener;
import com.sport365.badminton.http.base.IRequestProxyCallback;
import com.sport365.badminton.http.base.IRequestProxyListener;
import com.sport365.badminton.http.json.req.ServiceRequest;
import com.sport365.badminton.http.json.res.ResponseContent;
import com.sport365.badminton.params.SystemConfig;
import com.sport365.badminton.view.LoadingDialog;
import com.squareup.okhttp.Request;

public class BaseFragment extends Fragment implements OnClickListener {

	private HttpTaskHelper mHttpTaskHelper;
	public LoadingDialog mLoadingDialog;
	public FragmentManager mFragmentManager;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLoadingDialog = LoadingDialog.create(getActivity(), getActivity().getString(R.string.loading));
		mHttpTaskHelper = new HttpTaskHelper(getActivity());
		mFragmentManager = getActivity().getSupportFragmentManager();
		mLoadingDialog = LoadingDialog.create(getActivity(), getActivity().getString(R.string.loading));
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (savedInstanceState != null) {
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getTag(v.getId()) != null) {
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mHttpTaskHelper.destoryAllRequest();
		if (null != mLoadingDialog) {
			mLoadingDialog.dismiss();
		}
	}



	/**
	 * http post request with dialog
	 *
	 * @param request  request parameters related
	 * @param listener response callback
	 * @return real request，use for cancel since it will be removed later.
	 */
	public Request sendRequestWithNoDialog(ServiceRequest request, final IRequestProxyListener listener) {
		if (null == request) {
			if (SystemConfig.IS_OPEN_DEBUG) {
				throw new IllegalArgumentException("ServiceRequest == null");
			} else {
				return null;
			}
		}

		return mHttpTaskHelper.sendRequest(request, new IRequestListener() {
			@Override
			public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
				if (null != listener) {
					listener.onSuccess(jsonResponse, requestInfo);
				}
			}

			@Override
			public void onError(ResponseContent.Header header, HttpTaskHelper.RequestInfo requestInfo) {
				if (null != listener) {
					listener.onError(header, requestInfo);
				}
			}

			@Override
			public void onCanceled(HttpTaskHelper.CancelInfo cancelInfo) {
				if (null != listener) {
					listener.onCanceled(cancelInfo);
				}
			}
		});
	}

	/**
	 * Be similar to
	 * {@link #sendRequestWithNoDialog(ServiceRequest, IRequestProxyListener)},
	 * but no need to implement all callback functions in
	 * {@link IRequestProxyCallback}, only
	 * {@link IRequestProxyCallback#onSuccess(com.tongcheng.android.base.HttpTaskHelper.JsonResponse, com.tongcheng.android.base.HttpTaskHelper.RequestInfo)}
	 * is must.
	 */
	public Request sendRequestWithNoDialog(ServiceRequest request, final IRequestProxyCallback callback) {
		if (null == request) {
			if (SystemConfig.IS_OPEN_DEBUG) {
				throw new IllegalArgumentException("ServiceRequest == null");
			} else {
				return null;
			}
		}

		return mHttpTaskHelper.sendRequest(request, new IRequestListener() {
			@Override
			public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
				if (null != callback) {
					callback.onSuccess(jsonResponse, requestInfo);
				}
			}

			@Override
			public void onError(ResponseContent.Header header, HttpTaskHelper.RequestInfo requestInfo) {
				if (null != callback) {
					callback.onError(header, requestInfo);
				}
			}

			@Override
			public void onCanceled(HttpTaskHelper.CancelInfo cancelInfo) {
				if (null != callback) {
					callback.onCanceled(cancelInfo);
				}
			}
		});
	}

	/**
	 * http post request with no dialog
	 *
	 * @param request      request parameters related
	 * @param dialogConfig as it is
	 * @param listener     response callback
	 * @return real request，use for cancel
	 */
	public Request sendRequestWithDialog(ServiceRequest request, DialogConfig dialogConfig, final IRequestProxyListener listener) {
		if (null == request) {
			if (SystemConfig.IS_OPEN_DEBUG) {
				throw new IllegalArgumentException("ServiceRequest == null");
			} else {
				return null;
			}
		}
		if (null == listener) {
			if (SystemConfig.IS_OPEN_DEBUG) {
				throw new IllegalArgumentException("IRequestPoxyListener == null");
			} else {
				return null;
			}
		}

		Request realRequest = mHttpTaskHelper.createConnectionRequest(request);
		mLoadingDialog.show();
		mHttpTaskHelper.sendRequest(request, realRequest, new IRequestListener() {
			@Override
			public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
				mLoadingDialog.dismiss();
				listener.onSuccess(jsonResponse, requestInfo);
			}

			@Override
			public void onError(ResponseContent.Header header, HttpTaskHelper.RequestInfo requestInfo) {
				mLoadingDialog.dismiss();
				listener.onError(header, requestInfo);
			}

			@Override
			public void onCanceled(HttpTaskHelper.CancelInfo cancelInfo) {
				listener.onCanceled(cancelInfo);
			}
		});
		return realRequest;
	}

	/**
	 * Be similar to
	 * {@link #sendRequestWithDialog(ServiceRequest, DialogConfig, IRequestProxyListener)}
	 * , but no need to implement all callback functions in
	 * {@link IRequestProxyCallback}, only
	 * {@link IRequestProxyCallback#onSuccess(com.tongcheng.android.base.HttpTaskHelper.JsonResponse, com.tongcheng.android.base.HttpTaskHelper.RequestInfo)}
	 * is must.
	 */
	public Request sendRequestWithDialog(ServiceRequest request, DialogConfig dialogConfig, final IRequestProxyCallback callback) {
		if (null == request) {
			if (SystemConfig.IS_OPEN_DEBUG) {
				throw new IllegalArgumentException("ServiceRequest == null");
			} else {
				return null;
			}
		}
		if (null == callback) {
			if (SystemConfig.IS_OPEN_DEBUG) {
				throw new IllegalArgumentException("IRequestPoxyListener == null");
			} else {
				return null;
			}
		}

		Request realRequest = mHttpTaskHelper.createConnectionRequest(request);
		mLoadingDialog.show();
		mHttpTaskHelper.sendRequest(request, realRequest, new IRequestListener() {
			@Override
			public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
				mLoadingDialog.dismiss();
				callback.onSuccess(jsonResponse, requestInfo);
			}

			@Override
			public void onError(ResponseContent.Header header, HttpTaskHelper.RequestInfo requestInfo) {
				mLoadingDialog.dismiss();
				callback.onError(header, requestInfo);
			}

			@Override
			public void onCanceled(HttpTaskHelper.CancelInfo cancelInfo) {
				callback.onCanceled(cancelInfo);
			}
		});
		return realRequest;
	}

	public void cancelRequest(Request request) {
		if (null == request)
			throw new IllegalArgumentException("Request == null");
		mHttpTaskHelper.cancelRequest(request);
	}

}
