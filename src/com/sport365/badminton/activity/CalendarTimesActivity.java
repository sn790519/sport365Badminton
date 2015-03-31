package com.sport365.badminton.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Toast;
import com.sport365.badminton.BaseActivity;
import com.sport365.badminton.R;
import com.sport365.badminton.entity.reqbody.GetVenueFieldPriceReqBody;
import com.sport365.badminton.entity.resbody.GetVenueFieldPriceResBody;
import com.sport365.badminton.entity.webservice.SportParameter;
import com.sport365.badminton.entity.webservice.SportWebService;
import com.sport365.badminton.http.base.HttpTaskHelper;
import com.sport365.badminton.http.base.IRequestProxyCallback;
import com.sport365.badminton.http.json.req.ServiceRequest;
import com.sport365.badminton.http.json.res.ResponseContent;
import com.sport365.badminton.utils.BundleKeys;
import com.sport365.badminton.view.calendar.CalendarPickerView;
import com.sport365.badminton.view.calendar.CalendarPickerView.SelectionMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.widget.Toast.LENGTH_SHORT;

public class CalendarTimesActivity extends BaseActivity {
	private static final String TAG = "SampleTimesSquareActivity";
	protected CalendarPickerView calendar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sample_calendar_picker);
		setActionBarTitle(getIntent().getStringExtra(BundleKeys.ACTIONBAETITLE));
		final Calendar nextYear = Calendar.getInstance();
		nextYear.add(Calendar.MONTH, 2);

		final Calendar lastYear = Calendar.getInstance();
		lastYear.add(Calendar.MONTH, 0);

		calendar = (CalendarPickerView) findViewById(R.id.calendar_view);

		Calendar today = Calendar.getInstance();
		final ArrayList<Date> dates = new ArrayList<Date>();
		for (int i = 0; i < 5; i++) {
			today.add(Calendar.DAY_OF_MONTH, 3);
			dates.add(today.getTime());
		}
		calendar.init(new Date(), nextYear.getTime()) //
				.inMode(SelectionMode.MULTIPLE) //
				.withSelectedDates(dates);


		init_Get_VenueFieldPrice_List();
		calendar.setCellClickInterceptor(new CalendarPickerView.CellClickInterceptor() {

			@Override
			public boolean onCellClicked(Date date) {
				for(int i = 0 ; i<dates.size(); i++){
					if(date.getTime() == dates.get(i).getTime()){
						String toast = "Selected: " + calendar.getSelectedDate().getTime();
						Toast.makeText(CalendarTimesActivity.this, toast, LENGTH_SHORT).show();
					}
				}
				return true;
			}
		});
	}


	/**
	 * 初始化价格日历
	 */
	private void init_Get_VenueFieldPrice_List() {
		GetVenueFieldPriceReqBody reqBody = new GetVenueFieldPriceReqBody();
		reqBody.OrderDate = "2015-03-29";
		reqBody.VenueId = "2";
		sendRequestWithDialog(new ServiceRequest(mContext, new SportWebService(SportParameter.GET_VENUE_FIELDPRICE), reqBody), null, new IRequestProxyCallback() {

			@Override
			public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
				ResponseContent<GetVenueFieldPriceResBody> de = jsonResponse.getResponseContent(GetVenueFieldPriceResBody.class);
				GetVenueFieldPriceResBody resBody = de.getBody();
			}

			@Override
			public void onError(ResponseContent.Header header, HttpTaskHelper.RequestInfo requestInfo) {
				// TODO Auto-generated method stub
				super.onError(header, requestInfo);
			}
		});
	}

}
