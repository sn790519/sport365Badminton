package com.sport365.badminton;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.sport365.badminton.http.base.DialogConfig;
import com.sport365.badminton.http.base.HttpTaskHelper;
import com.sport365.badminton.http.base.IRequestListener;
import com.sport365.badminton.http.base.IRequestProxyCallback;
import com.sport365.badminton.http.base.IRequestProxyListener;
import com.sport365.badminton.http.base.ImageLoader;
import com.sport365.badminton.http.json.req.ServiceRequest;
import com.sport365.badminton.http.json.res.ResponseContent;
import com.sport365.badminton.params.SystemConfig;
import com.sport365.badminton.utils.Tools;
import com.sport365.badminton.utils.ULog;
import com.sport365.badminton.utils.Utilities;
import com.sport365.badminton.view.LoadingDialog;
import com.squareup.okhttp.Request;

public class BaseActivity extends FragmentActivity implements OnClickListener {
	public String TAG = BaseActivity.class.getSimpleName();
	private final int DEFALUT_ACTIONBAR_BG_ID = R.drawable.navibar_common_bg;
	private final int DEFAULT_ACTIONBAR_RESOURCE_ID = R.layout.main_action_bar;
	private int actionbar_resource_id = DEFAULT_ACTIONBAR_RESOURCE_ID;
	public Context mContext;
	public LoadingDialog mLoadingDialog;
	public LayoutInflater mLayoutInflater;
	public ImageLoader mImageLoader;
	private HttpTaskHelper mHttpTaskHelper;
	private ActionBar mActionBar;
	private ImageView actionbar_back, actionbar_right_menu;
	private ImageView action_icon;
	private TextView actionbar_title, actionbar_icon;
	private View mTitleView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		ULog.setTag(getClass().getSimpleName());
		ULog.debug("--->onCreate");
		initActionBar();
		init();
	}

	private void initActionBar() {
		mTitleView = getLayoutInflater().inflate(actionbar_resource_id, null);
		mActionBar = getActionBar();
		if (mActionBar != null) {
			mActionBar.setDisplayShowCustomEnabled(true);// 可以自定义actionbar
			mActionBar.setDisplayShowTitleEnabled(false);// 不显示logo
			mActionBar.setDisplayShowHomeEnabled(false);
			mActionBar.setBackgroundDrawable(getResources().getDrawable(DEFALUT_ACTIONBAR_BG_ID));
			if (actionbar_resource_id == DEFAULT_ACTIONBAR_RESOURCE_ID) {
				actionbar_back = (ImageView) mTitleView.findViewById(R.id.actionbar_back);
				actionbar_back.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						onBackPressed();
					}
				});
				actionbar_icon = (TextView) mTitleView.findViewById(R.id.actionbar_icon);
				actionbar_icon.setVisibility(View.GONE);
				action_icon = (ImageView) mTitleView.findViewById(R.id.action_icon);
				actionbar_right_menu = (ImageView) mTitleView.findViewById(R.id.actionbar_right_menu);
				action_icon.setVisibility(View.GONE);
				actionbar_title = (TextView) mTitleView.findViewById(R.id.actionbar_title);
				actionbar_title.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						/**
						 * Call this view's OnClickListener, if it is defined.
						 * Performs all normal actions associated with clicking:
						 * reporting accessibility event, playing a sound, etc.
						 * 
						 */
						actionbar_back.performClick();
					}
				});
			}
			ActionBar.LayoutParams params = new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			mActionBar.setCustomView(mTitleView, params);
		}
	}

	public void setActionBarTitle(String title) {
		if (actionbar_title != null) {
			actionbar_title.setText(title);
		}
	}

	public void setHomeBar(String text) {
		if (actionbar_icon != null && action_icon != null) {
			actionbar_back.setVisibility(View.GONE);
			actionbar_icon.setVisibility(View.VISIBLE);
			actionbar_icon.setText(text);
			action_icon.setVisibility(View.VISIBLE);
		}
	}

	public void setHomeBar() {
		if (actionbar_icon != null && action_icon != null) {
			actionbar_back.setVisibility(View.GONE);
			actionbar_icon.setVisibility(View.VISIBLE);
			action_icon.setVisibility(View.VISIBLE);
		}
	}

	public TextView getActionBarTitle() {
		return actionbar_title;
	}

	public ActionBar getmActionBar() {
		return mActionBar;
	}

	public View getActionBarTitleView() {
		return mTitleView;
	}

	/**
	 * 设置右侧按钮的背景图
	 * 
	 * @param resId
	 */
	public void setActionBarRightMenu(int resId, final RightClickListen rightClickListen) {
		if (resId == 0 || actionbar_right_menu == null) {
			return;
		} else {
			actionbar_right_menu.setVisibility(View.VISIBLE);
			actionbar_right_menu.setImageResource(resId);
			actionbar_right_menu.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					rightClickListen.onRightMenuClick();
				}
			});
		}
	}

	private void init() {
		mContext = this;
		mLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mLoadingDialog = LoadingDialog.create(mContext, mContext.getString(R.string.loading));

		if (TextUtils.isEmpty(Utilities.FILE_ROOT)) {
			Utilities.CheckFileRoot(getApplication());
			if (SystemConfig.IP.equals("")) {
				SystemConfig.IP = Tools.getPsdnIp();
			}
		}
		mImageLoader = ImageLoader.getInstance();
		mHttpTaskHelper = new HttpTaskHelper(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		ULog.debug("--->onResume");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ULog.debug("--->onDestroy");
		mHttpTaskHelper.destoryAllRequest();
	}

	@Override
	public void onClick(View v) {
	}

	/**
	 * http post request with dialog
	 * 
	 * @param request
	 *            request parameters related
	 * @param listener
	 *            response callback since it will be removed later.
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
	 * http post request with dialog
	 * 
	 * @param request
	 *            request parameters related
	 * @param dialogConfig
	 *            as it is
	 * @param listener
	 *            response callback
	 * @return real request，use for cancel since it will be removed later.
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

	@Override
	protected void onStart() {
		super.onStart();
		ULog.debug("--->onStart");
	}

	@Override
	protected void onPause() {
		super.onPause();
		ULog.debug("--->onPause");
	}

	@Override
	protected void onStop() {
		super.onStop();
		ULog.debug("--->onStop");
	}

	public interface RightClickListen {
		public void onRightMenuClick();
	}

}
