package com.sport365.badminton.activity.fragment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.sport365.badminton.BaseFragment;
import com.sport365.badminton.R;
import com.sport365.badminton.alipay.PayResult;
import com.sport365.badminton.alipay.SignUtils;
import com.sport365.badminton.entity.reqbody.AliClientPayReqBody;
import com.sport365.badminton.entity.reqbody.WeixinPayReqBody;
import com.sport365.badminton.entity.resbody.AliClientPayResBody;
import com.sport365.badminton.entity.resbody.WeixinPayResBody;
import com.sport365.badminton.entity.webservice.SportParameter;
import com.sport365.badminton.entity.webservice.SportWebService;
import com.sport365.badminton.http.base.HttpTaskHelper;
import com.sport365.badminton.http.base.IRequestProxyCallback;
import com.sport365.badminton.http.json.req.ServiceRequest;
import com.sport365.badminton.http.json.res.ResponseContent;
import com.sport365.badminton.utils.SystemConfig;
import com.sport365.badminton.utils.ULog;
import com.sport365.badminton.utils.Utilities;
import com.sport365.badminton.view.NoScrollGridView;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 充值页面
 */
public class HomePayFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener {

	private static final String PAY_ZFB = "pay_zfb";
	private static final String PAY_WX = "pay_WX";

	private NoScrollGridView gv_money_choose;
	private int[] prices = { 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000 };
	// 支付选择
	public RadioGroup rg_menu;
	private RadioButton rb_zfb_pay;
	public RadioButton rb_wx_pay;
	private String Choose_Pay = PAY_ZFB;

	// 支付按钮
	private Button btn_pay;
	private static final int SDK_PAY_FLAG = 1;
	private static final int SDK_CHECK_FLAG = 2;

	// View
	private TextView tv_name;
	private TextView tv_phone;
	private TextView tv_email;
	private TextView tv_qq;

	// 支付选择的position,默认选择充值金额第一项
	private int choosePosition = 1;

	PayChooseAdapter payChooseAdapter;
	public IWXAPI weixin;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);

				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();

				String resultStatus = payResult.getResultStatus();

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.makeText(getActivity(), "支付成功", Toast.LENGTH_SHORT).show();
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(getActivity(), "支付结果确认中", Toast.LENGTH_SHORT).show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(getActivity(), "支付失败", Toast.LENGTH_SHORT).show();

					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				Toast.makeText(getActivity(), "检查结果为：" + msg.obj, Toast.LENGTH_SHORT).show();
				break;
			}
			default:
				break;
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_pay_layout, container, false);
		gv_money_choose = (NoScrollGridView) view.findViewById(R.id.gv_money_choose);
		// 支付选择
		rg_menu = (RadioGroup) view.findViewById(R.id.rg_menu);
		rg_menu.setOnCheckedChangeListener(this);

		rb_zfb_pay = (RadioButton) view.findViewById(R.id.rb_zfb_pay);
		rb_wx_pay = (RadioButton) view.findViewById(R.id.rb_wx_pay);

		btn_pay = (Button) view.findViewById(R.id.btn_pay);
		btn_pay.setOnClickListener(this);
		payChooseAdapter = new PayChooseAdapter();
		gv_money_choose.setAdapter(payChooseAdapter);
		gv_money_choose.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				choosePosition = position;
				payChooseAdapter.notifyDataSetChanged();
			}
		});
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		tv_phone = (TextView) view.findViewById(R.id.tv_phone);
		tv_email = (TextView) view.findViewById(R.id.tv_email);
		tv_qq = (TextView) view.findViewById(R.id.tv_qq);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO activity 创建成功后执行getactivity();
		super.onActivityCreated(savedInstanceState);
		weixin = WXAPIFactory.createWXAPI(getActivity(), SystemConfig.WEIXIN_APP_ID);
		boolean falg = weixin.registerApp(SystemConfig.WEIXIN_APP_ID); // 将应用注册到微信
	}

	/**
	 * 初始化View的默认信息
	 */
	public void initData() {
		if (SystemConfig.loginResBody != null && tv_name != null && tv_phone != null && tv_email != null && tv_qq != null) {
			tv_name.setText(TextUtils.isEmpty(SystemConfig.loginResBody.mobile) ? "" : SystemConfig.loginResBody.nickname);
			tv_phone.setText(TextUtils.isEmpty(SystemConfig.loginResBody.mobile) ? "" : SystemConfig.loginResBody.mobile);
			tv_email.setText(TextUtils.isEmpty(SystemConfig.loginResBody.email) ? "" : SystemConfig.loginResBody.email);
			tv_qq.setText(TextUtils.isEmpty(SystemConfig.loginResBody.qq) ? "" : SystemConfig.loginResBody.qq);
		} else {
			tv_name.setText("");
			tv_phone.setText("");
			tv_email.setText("");
			tv_qq.setText("");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_pay:
			if (Choose_Pay.equals(PAY_ZFB)) {
				// 支付宝支付
				getAlipayClinet();
			} else if (Choose_Pay.equals(PAY_WX)) {
				// 微信支付
				WXPay();
			}
			break;
		}
	}

	/**
	 * call alipay sdk pay. 调用SDK支付
	 */
	public void pay(AliClientPayResBody resBody) {
		// 订单
		String orderInfo = getOrderInfo(resBody);
		// 对订单做RSA 签名
		String sign = SignUtils.sign(orderInfo, resBody.privateKey);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + "sign_type=\"RSA\"";

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(getActivity());
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * get the sdk version. 获取SDK版本号
	 */
	public void getSDKVersion() {
		PayTask payTask = new PayTask(getActivity());
		String version = payTask.getVersion();
		Toast.makeText(getActivity(), version, Toast.LENGTH_SHORT).show();
	}

	/**
	 * create the order info. 创建订单信息
	 */
	public String getOrderInfo(AliClientPayResBody resBody) {
		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + resBody.parter + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + resBody.seller + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + resBody.tradeNO + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + resBody.productName + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + resBody.productDescription + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + resBody.amount + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + resBody.notifyURL + "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	@Override
	public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
		switch (checkedId) {
		case R.id.rb_zfb_pay:
			Choose_Pay = PAY_ZFB;
			break;
		case R.id.rb_wx_pay:
			Choose_Pay = PAY_WX;
			break;
		}
	}

	// 金额的列表
	class PayChooseAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return prices.length;
		}

		@Override
		public Object getItem(int i) {
			return null;
		}

		@Override
		public long getItemId(int i) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup viewGroup) {
			convertView = mLayoutInflater.inflate(R.layout.pay_money_item, viewGroup, false);
			TextView tv_prive = (TextView) convertView.findViewById(R.id.tv_price);
			tv_prive.setText(prices[position] + "元");
			if (position == choosePosition) {
				tv_prive.setBackgroundColor(getResources().getColor(R.color.base_orange));
			} else {
				tv_prive.setBackgroundColor(getResources().getColor(R.color.pay_backgroud));

			}
			return convertView;
		}
	}

	/**
	 * 支付宝快捷支付 从接口获取快捷支付的相关信息
	 */
	private void getAlipayClinet() {
		AliClientPayReqBody reqBody = new AliClientPayReqBody();
		if (SystemConfig.loginResBody == null) {
			Toast.makeText(getActivity(), "您还没有登录，请登录", Toast.LENGTH_LONG).show();
			return;
		}
		reqBody.bookMobile = SystemConfig.loginResBody.mobile;
		reqBody.memberid = SystemConfig.loginResBody.memberId;
		reqBody.totalFee = String.valueOf(prices[choosePosition]);
		sendRequestWithDialog(new ServiceRequest(getActivity(), new SportWebService(SportParameter.ALICLIENT_PAY), reqBody), null, new IRequestProxyCallback() {

			@Override
			public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
				ResponseContent<AliClientPayResBody> de = jsonResponse.getResponseContent(AliClientPayResBody.class);
				AliClientPayResBody resBody = de.getBody();
				pay(resBody);
			}

			@Override
			public void onError(ResponseContent.Header header, HttpTaskHelper.RequestInfo requestInfo) {
				// TODO Auto-generated method stub
				super.onError(header, requestInfo);
			}
		});
	}

	/**
	 * 微信支付
	 */
	private void weixinPay(final IWXAPI weixin) {
		if (SystemConfig.loginResBody == null) {
			Toast.makeText(getActivity(), "您还没有登录，请登录", Toast.LENGTH_LONG).show();
			return;
		}
		WeixinPayReqBody reqBody = new WeixinPayReqBody();
		reqBody.bookMobile = SystemConfig.loginResBody.mobile;
		reqBody.memberid = SystemConfig.loginResBody.memberId;
		reqBody.totalFee = String.valueOf(prices[choosePosition]);
		// reqBody.totalFee = "0.1";
		sendRequestWithDialog(new ServiceRequest(getActivity(), new SportWebService(SportParameter.WEIXIN_PAY), reqBody), null, new IRequestProxyCallback() {

			@Override
			public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
				ResponseContent<WeixinPayResBody> de = jsonResponse.getResponseContent(WeixinPayResBody.class);
				WeixinPayResBody resBody = de.getBody();
				WXAPIFactory.createWXAPI(getActivity(), SystemConfig.WEIXIN_APP_ID).registerApp(SystemConfig.WEIXIN_APP_ID);
				sendPayReq(resBody, weixin);
			}

			@Override
			public void onError(ResponseContent.Header header, HttpTaskHelper.RequestInfo requestInfo) {
				// TODO Auto-generated method stub
				super.onError(header, requestInfo);
			}
		});

	}

	private void WXPay() {
		// 检查微信版本是否支持支付
		if (!weixin.isWXAppInstalled()) {
			Toast.makeText(getActivity(), "未安装微信客户端", Toast.LENGTH_LONG).show();
			return;
		}
		boolean isPaySupported = weixin.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
		if (!isPaySupported) {
			Toast.makeText(getActivity(), "您的微信版本不支持支付，请升级至最新版本", Toast.LENGTH_LONG).show();
		} else {
			weixinPay(weixin);// 微信支付
		}
	}

	// 吊起支付
	private void sendPayReq(WeixinPayResBody resBody, IWXAPI weixin) {

		PayReq req = new PayReq();
		req.appId = SystemConfig.WEIXIN_APP_ID;
		req.partnerId = resBody.partnerId;
		req.prepayId = resBody.prePayId;
		req.nonceStr = resBody.nonceStr;
		req.timeStamp = String.valueOf(resBody.timeStamp);
		req.packageValue = resBody.packageStr;// "Sign=" + packageValue;
		req.sign = resBody.sign;
		/*
		 * req.packageValue = "Sign=Wxpay";// "Sign=" + packageValue;
		 * List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		 * signParams.add(new BasicNameValuePair("appid", req.appId));
		 * signParams.add(new BasicNameValuePair("appkey", resBody.appKey));
		 * signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
		 * signParams.add(new BasicNameValuePair("package", req.packageValue));
		 * signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
		 * signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
		 * signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
		 * Log.d("wxsign", "调起支付的package串：" + req.packageValue); req.sign =
		 * genSign(signParams); Log.d("wxsign", "调起支付的sign-sha1串：" + req.sign);
		 */
		// 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
		weixin.sendReq(req);
	}

	private String genSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (; i < params.size() - 1; i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append(params.get(i).getName());
		sb.append('=');
		sb.append(params.get(i).getValue());
		Log.d("wxsign", "字典签名串：" + sb.toString());
		String sha1 = sha1(sb.toString());
		return sha1;
	}

	public static String sha1(String str) {
		if (str == null || str.length() == 0) {
			return null;
		}
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
			mdTemp.update(str.getBytes());
			byte[] md = mdTemp.digest();
			int j = md.length;
			char buf[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
				buf[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(buf);
		} catch (Exception e) {
			return null;
		}
	}
}
