package com.sport365.badminton.http.base;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;

import com.sport365.badminton.R;
import com.sport365.badminton.dialog.LoadingDialog;
import com.sport365.badminton.http.json.req.ServiceRequest;
import com.sport365.badminton.http.json.res.ResponseContent;
import com.sport365.badminton.params.SystemConfig;
import com.squareup.okhttp.Request;

public class BaseFragment extends Fragment implements OnClickListener {

	private HttpTaskHelper mHttpTaskHelper;
	public LoadingDialog alertDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		alertDialog = new LoadingDialog(this.getActivity());
		mHttpTaskHelper = new HttpTaskHelper(getActivity());
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
			String key = getClass().getSimpleName() + "_" + v.getTag(v.getId());
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
		mHttpTaskHelper.destoryAllRequest();
		super.onDestroy();
	}

	/**
	 * 显示弹框
	 * 
	 * @param resId
	 */
	public void showLoadingDialog(int resId) {
		showLoadingDialog(resId, false, null);
	}

	/**
	 * show dialog
	 * 
	 * @param resId
	 *            文案id
	 * @param cancelable
	 *            是否可取消
	 * @param requestCall
	 *            与dialog绑定的请求
	 */
	public void showLoadingDialog(int resId, boolean cancelable, Request requestCall) {
		String title;
		if (null == getActivity()) {
			return;
		}

		if (alertDialog == null) {
			LoadingDialog dialog = new LoadingDialog(getActivity());
			alertDialog = dialog;
		}

		// 可取消的dialog若存在则不启动新的dialog
		if (alertDialog.isShowing()) {
			if (alertDialog.getDialogCancelable()) {
				return;
			} else {
				alertDialog.dismiss();
			}
		}

		if (resId <= 0) {
			title = getResources().getString(R.string.loading_public_default);
		} else {
			title = getResources().getString(resId);
		}
		alertDialog.setCanceledOnTouchOutside(false);

		alertDialog.setCancelable(cancelable);
		alertDialog.setLoadingText(title);
		if (cancelable) {
			final Request tmpRequest = requestCall;
			alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					if (null != mHttpTaskHelper && null != tmpRequest) {
						mHttpTaskHelper.cancelRequest(tmpRequest);
					}
				}
			});
		} else {
			alertDialog.setOnDismissListener(null);
		}
		alertDialog.show();
	}

	/**
	 * 显示弹框
	 */
	public void showLoadingDialog() {
		showLoadingDialog(-1);
	}

	public void dismissLoadingDialog() {
		if (alertDialog != null) {
			alertDialog.dismiss();
		}
	}

	/**
	 * http post request with dialog
	 * 
	 * @param request
	 *            request parameters related
	 * @param listener
	 *            response callback
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
	 * @param request
	 *            request parameters related
	 * @param dialogConfig
	 *            as it is
	 * @param listener
	 *            response callback
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
		showLoadingDialog(dialogConfig.loadingMessage(), dialogConfig.cancelable(), realRequest);
		mHttpTaskHelper.sendRequest(request, realRequest, new IRequestListener() {
			@Override
			public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
				dismissLoadingDialog();
				listener.onSuccess(jsonResponse, requestInfo);
			}

			@Override
			public void onError(ResponseContent.Header header, HttpTaskHelper.RequestInfo requestInfo) {
				dismissLoadingDialog();
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
		showLoadingDialog(dialogConfig.loadingMessage(), dialogConfig.cancelable(), realRequest);
		mHttpTaskHelper.sendRequest(request, realRequest, new IRequestListener() {
			@Override
			public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
				dismissLoadingDialog();
				callback.onSuccess(jsonResponse, requestInfo);
			}

			@Override
			public void onError(ResponseContent.Header header, HttpTaskHelper.RequestInfo requestInfo) {
				dismissLoadingDialog();
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
