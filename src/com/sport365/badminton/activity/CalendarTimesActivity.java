package com.sport365.badminton.activity;

import static android.widget.Toast.LENGTH_SHORT;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.sport365.badminton.BaseActivity;
import com.sport365.badminton.R;
import com.sport365.badminton.entity.obj.ActivityDateObj;
import com.sport365.badminton.entity.reqbody.GetVenueFieldPriceReqBody;
import com.sport365.badminton.entity.resbody.GetVenueFieldPriceResBody;
import com.sport365.badminton.entity.webservice.SportParameter;
import com.sport365.badminton.entity.webservice.SportWebService;
import com.sport365.badminton.http.base.HttpTaskHelper;
import com.sport365.badminton.http.base.IRequestProxyCallback;
import com.sport365.badminton.http.json.req.ServiceRequest;
import com.sport365.badminton.http.json.res.ResponseContent;
import com.sport365.badminton.utils.BundleKeys;
import com.sport365.badminton.view.calendar.CalendarItemObj;
import com.sport365.badminton.view.calendar.CalendarPickerView;
import com.sport365.badminton.view.calendar.CalendarPickerView.FluentInitializer;
import com.sport365.badminton.view.calendar.CalendarPickerView.SelectionMode;

public class CalendarTimesActivity extends BaseActivity {
	private static final String TAG = "SampleTimesSquareActivity";
	protected CalendarPickerView calendar;
	private ArrayList<ActivityDateObj> activeCalendar = new ArrayList<ActivityDateObj>();
	private ArrayList<CalendarItemObj> activeDates = new ArrayList<CalendarItemObj>();

	private Calendar nextYear;
	private Calendar lastYear;
	private FluentInitializer fluedates;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarTitle(getIntent().getStringExtra(BundleKeys.ACTIONBAETITLE));
		nextYear = Calendar.getInstance();
		nextYear.add(Calendar.MONTH, 2);
		lastYear = Calendar.getInstance();
		lastYear.add(Calendar.MONTH, 0);
		init_Get_VenueFieldPrice_List();
	}

	/**
	 * 初始化价格日历
	 */
	private void init_Get_VenueFieldPrice_List() {
		GetVenueFieldPriceReqBody reqBody = new GetVenueFieldPriceReqBody();
		sendRequestWithDialog(new ServiceRequest(mContext, new SportWebService(SportParameter.GET_ACTIVE_CALENDARLIST), reqBody), null, new IRequestProxyCallback() {

			@Override
			public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
				ResponseContent<GetVenueFieldPriceResBody> de = jsonResponse.getResponseContent(GetVenueFieldPriceResBody.class);
				GetVenueFieldPriceResBody resBody = de.getBody();
				activeCalendar = resBody.activeCalendar;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				// 可点击的价格列表中的数据
				for (int i = 0; i < activeCalendar.size(); i++) {
					if ("1".equals(activeCalendar.get(i).isvalid)) {
						CalendarItemObj calendarItemObj = new CalendarItemObj();
						try {
							calendarItemObj.date = sdf.parse(activeCalendar.get(i).date);
							calendarItemObj.countNum = activeCalendar.get(i).amount;
							activeDates.add(calendarItemObj);
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
				}
				setContentView(R.layout.sample_calendar_picker);
				calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
				fluedates = calendar.init(new Date(), nextYear.getTime()).inMode(SelectionMode.MULTIPLE);
				// 初始化数据
				fluedates //
						.withSelectedDates(activeDates);
				calendar.setCellClickInterceptor(new CalendarPickerView.CellClickInterceptor() {

					@Override
					public boolean onCellClicked(Date date) {
						for (int i = 0; i < activeDates.size(); i++) {
							if (date.getTime() == activeDates.get(i).date.getTime()) {
								try {
									String dates = new SimpleDateFormat("yyyy-MM-dd").format(activeDates.get(i).date);
									Intent intent = new Intent(CalendarTimesActivity.this, ActivityListActivity.class);
									intent.putExtra("date", dates);
									intent.putExtra(ActivityListActivity.ACTIVITYFROM,ActivityListActivity.CADACTIVITYLIST);
									startActivity(intent);
									Toast.makeText(CalendarTimesActivity.this, dates, LENGTH_SHORT).show();
								} catch (Exception e) {
								}
							}
						}
						return true;
					}
				});
			}

			@Override
			public void onError(ResponseContent.Header header, HttpTaskHelper.RequestInfo requestInfo) {
				// TODO Auto-generated method stub
				super.onError(header, requestInfo);
			}
		});
	}

}
