package com.sport365.badminton.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.sport365.badminton.BaseActivity;
import com.sport365.badminton.R;
import com.sport365.badminton.entity.obj.City;
import com.sport365.badminton.entity.reqbody.GetCityListByProvinceIdReqBody;
import com.sport365.badminton.entity.reqbody.ReqBody;
import com.sport365.badminton.entity.resbody.GetCityListResBody;
import com.sport365.badminton.entity.resbody.GetProvinceListResBody;
import com.sport365.badminton.entity.resbody.Province;
import com.sport365.badminton.entity.webservice.SportParameter;
import com.sport365.badminton.entity.webservice.SportWebService;
import com.sport365.badminton.http.base.HttpTaskHelper;
import com.sport365.badminton.http.base.IRequestProxyCallback;
import com.sport365.badminton.http.json.req.ServiceRequest;
import com.sport365.badminton.http.json.res.ResponseContent;

import java.util.ArrayList;
import java.util.List;

/**
 * 城市联动页面
 * Created by kjh08490 on 2015/4/6.
 */
public class CitySelectorActivity extends BaseActivity {

	//省份
	public static final String PROVINCE = "PROVINCE";
	//城市
	public static final String CITY = "CITY";
	//区域
	public static final String COUNTRY = "COUNTRY";

	private ListView lv_province;
	private ListView lv_city;
	private ListView lv_countyid;

	// 省份数据
	private List<Province> provinceList = new ArrayList<Province>();
	private int provinceChoosePosition = -1;
	private ProvinceAdapter provinceAdapter;
	private String proviceid = "";//保存省份id


	// 城市数据
	private List<City> cityList = new ArrayList<City>();
	private int cityChoosePosition = -1;
	private CityAdapter cityAdapter;
	private String cityid = "";//保存城市id


	// 区域数据
	private List<City> countryList = new ArrayList<City>();
	private int countryChoosePosition = -1;
	private CountryAdapter countryAdapter;
	private String countryid = "";

	private Button btn_choose;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.city_selector_layout);
		lv_province = (ListView) findViewById(R.id.lv_province);
		provinceAdapter = new ProvinceAdapter();
		lv_province.setAdapter(provinceAdapter);
		lv_province.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				proviceid = provinceList.get(position).provinceId;
				provinceChoosePosition = position;
				provinceAdapter.notifyDataSetChanged();
				clickProvice(proviceid);
			}
		});

		lv_city = (ListView) findViewById(R.id.lv_city);
		cityAdapter = new CityAdapter();
		lv_city.setAdapter(cityAdapter);
		lv_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				cityid = cityList.get(position).cityId;
				cityChoosePosition = position;
				cityAdapter.notifyDataSetChanged();
				clickCity(cityid);
			}
		});


		lv_countyid = (ListView) findViewById(R.id.lv_countyid);
		countryAdapter = new CountryAdapter();
		lv_countyid.setAdapter(countryAdapter);
		lv_countyid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				countryid = countryList.get(position).cityId;
				countryChoosePosition = position;
				countryAdapter.notifyDataSetChanged();
			}
		});
		initProvince();

		btn_choose = (Button) findViewById(R.id.btn_choose);
		btn_choose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra(PROVINCE, proviceid);
				intent.putExtra(CITY, cityid);
				intent.putExtra(COUNTRY, countryid);
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		});
	}

	/**
	 * 初始化省份信息
	 */
	private void initProvince() {
		SportWebService webService = new SportWebService(SportParameter.GET_PROVINCE_LIST);
		ReqBody reqBody = new ReqBody();
		sendRequestWithDialog(new ServiceRequest(mContext, webService, reqBody), null, new IRequestProxyCallback() {

			@Override
			public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
				ResponseContent<GetProvinceListResBody> de = jsonResponse.getResponseContent(GetProvinceListResBody.class);
				final GetProvinceListResBody resBody = de.getBody();
				int size = resBody.provinceList.size();
				if (size > 0) {
					provinceList = resBody.provinceList;
					provinceAdapter.notifyDataSetChanged();
				}
			}

			@Override
			public void onError(ResponseContent.Header header, HttpTaskHelper.RequestInfo requestInfo) {
				super.onError(header, requestInfo);
			}
		});
	}

	/**
	 * 点击选择省份时候
	 *
	 * @param proviceid
	 */
	private void clickProvice(String proviceid) {
		cityList.clear();
		cityAdapter.notifyDataSetChanged();
		countryList.clear();
		countryAdapter.notifyDataSetChanged();
		initCity(proviceid);
	}

	/**
	 * 初始化城市信息
	 */
	private void initCity(String provinceid) {
		GetCityListByProvinceIdReqBody reqBody = new GetCityListByProvinceIdReqBody();
		reqBody.provinceId = provinceid;
		SportWebService webService = new SportWebService(SportParameter.GET_CITY_LIST_BY_PROVINCEID);
		sendRequestWithDialog(new ServiceRequest(mContext, webService, reqBody), null, new IRequestProxyCallback() {

			@Override
			public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
				ResponseContent<GetCityListResBody> de = jsonResponse.getResponseContent(GetCityListResBody.class);
				final GetCityListResBody resBody = de.getBody();
				int size = resBody.cityList.size();
				cityList = resBody.cityList;
				cityAdapter.notifyDataSetChanged();
			}

			@Override
			public void onError(ResponseContent.Header header, HttpTaskHelper.RequestInfo requestInfo) {
				super.onError(header, requestInfo);
			}
		});
	}

	private void clickCity(String cityid) {
		countryList.clear();
		countryAdapter.notifyDataSetChanged();
		initCountry(cityid);
	}

	/**
	 * 初始化区域信息
	 */
	private void initCountry(String cityid) {
		GetCityListByProvinceIdReqBody reqBody = new GetCityListByProvinceIdReqBody();
		reqBody.provinceId = cityid;
		SportWebService webService = new SportWebService(SportParameter.GET_CITY_LIST_BY_PROVINCEID);
		sendRequestWithDialog(new ServiceRequest(mContext, webService, reqBody), null, new IRequestProxyCallback() {

			@Override
			public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
				ResponseContent<GetCityListResBody> de = jsonResponse.getResponseContent(GetCityListResBody.class);
				final GetCityListResBody resBody = de.getBody();
				int size = resBody.cityList.size();
				if (size > 0) {
					countryList = resBody.cityList;
					countryAdapter.notifyDataSetChanged();
				}

			}

			@Override
			public void onError(ResponseContent.Header header, HttpTaskHelper.RequestInfo requestInfo) {
				super.onError(header, requestInfo);
			}
		});
	}


	/**
	 * 省份数据源的信息
	 */
	class ProvinceAdapter extends BaseAdapter {


		@Override
		public int getCount() {
			return provinceList.size();
		}

		@Override
		public Object getItem(int position) {
			return provinceList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup viewGroup) {
			convertView = mLayoutInflater.inflate(R.layout.city_selector_item,
					viewGroup, false);
			TextView tv_prive = (TextView) convertView
					.findViewById(R.id.tv_price);
			tv_prive.setText("" + provinceList.get(position).provinceName);
			if (position == provinceChoosePosition) {
				tv_prive.setBackgroundColor(getResources().getColor(
						R.color.base_orange));
			} else {
				tv_prive.setBackgroundColor(getResources().getColor(
						R.color.white));

			}
			return convertView;
		}
	}

	/**
	 * 城市和区域数据源的信息
	 */
	class CityAdapter extends BaseAdapter {


		@Override
		public int getCount() {
			return cityList.size();
		}

		@Override
		public Object getItem(int position) {
			return cityList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup viewGroup) {
			convertView = mLayoutInflater.inflate(R.layout.city_selector_item,
					viewGroup, false);
			TextView tv_prive = (TextView) convertView
					.findViewById(R.id.tv_price);
			tv_prive.setText("" + cityList.get(position).cityName);
			if (position == cityChoosePosition) {
				tv_prive.setBackgroundColor(getResources().getColor(
						R.color.base_orange));
			} else {
				tv_prive.setBackgroundColor(getResources().getColor(
						R.color.white));

			}
			return convertView;
		}
	}

	/**
	 * 区域数据源的信息
	 */
	class CountryAdapter extends BaseAdapter {


		@Override
		public int getCount() {
			return countryList.size();
		}

		@Override
		public Object getItem(int position) {
			return countryList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup viewGroup) {
			convertView = mLayoutInflater.inflate(R.layout.city_selector_item,
					viewGroup, false);
			TextView tv_prive = (TextView) convertView
					.findViewById(R.id.tv_price);
			tv_prive.setText("" + countryList.get(position).cityName);
			if (position == countryChoosePosition) {
				tv_prive.setBackgroundColor(getResources().getColor(
						R.color.base_orange));
			} else {
				tv_prive.setBackgroundColor(getResources().getColor(
						R.color.white));

			}
			return convertView;
		}
	}

}
