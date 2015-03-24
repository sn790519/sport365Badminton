package com.sport365.badminton.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.sport365.badminton.BaseActivity;
import com.sport365.badminton.R;
import com.sport365.badminton.entity.obj.ActivityObj;
import com.sport365.badminton.entity.reqbody.GetActiveListByMemberIdReqBody;
import com.sport365.badminton.entity.resbody.GetActiveListByMemberIdResBody;
import com.sport365.badminton.entity.webservice.SportParameter;
import com.sport365.badminton.entity.webservice.SportWebService;
import com.sport365.badminton.http.base.HttpTaskHelper;
import com.sport365.badminton.http.base.IRequestProxyCallback;
import com.sport365.badminton.http.json.req.ServiceRequest;
import com.sport365.badminton.http.json.res.ResponseContent;
import com.sport365.badminton.utils.SystemConfig;
import com.sport365.badminton.utils.Utilities;

import java.util.ArrayList;

/**
 * 我的群活动
 */
public class MyGroupActivity extends BaseActivity {
    private Context mContext;
    private ListAdapter adapter;
    private ListView listview;
    private GetActiveListByMemberIdResBody resBody = new GetActiveListByMemberIdResBody();

    private int mCurrentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_group);
        mContext = MyGroupActivity.this;
        initActionBar();
        listview = (ListView) findViewById(R.id.listview);
        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 当不滚动时
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    // 判断是否滚动到底部
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        if (mCurrentPage * 20 < Integer.parseInt(resBody.totalCount)) {
                            //加载更多功能的代码
                            getMyGroupActivity(mCurrentPage + "");
                            Utilities.showToast("正在加载中", mContext);
                        } else {
                            Utilities.showToast("没有更多数据", mContext);
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                startActivity(new Intent());
            }
        });
        getMyGroupActivity(mCurrentPage + "1");
    }

    private void initActionBar() {
        setActionBarTitle("我的群活动");
        mActionbar_right.setVisibility(View.GONE);
        mActionbar_right_text.setVisibility(View.GONE);
    }


    private void getMyGroupActivity(String page) {
        final GetActiveListByMemberIdReqBody reqBody = new GetActiveListByMemberIdReqBody();
        reqBody.memberId = SystemConfig.memberId;
        reqBody.Page = page;
        reqBody.PageSize = "20";
        SportWebService webService = new SportWebService(SportParameter.GET_ACTIVE_LIST_BY_MEMBERID);
        sendRequestWithDialog(new ServiceRequest(mContext, webService, reqBody), null, new IRequestProxyCallback() {

            @Override
            public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
                ResponseContent<GetActiveListByMemberIdResBody> de = jsonResponse.getResponseContent(GetActiveListByMemberIdResBody.class);
                if (mCurrentPage == 0) {
                    resBody = de.getBody();
                    adapter = new ListAdapter(mContext, resBody.alctiveList);
                    listview.setAdapter(adapter);
                } else {
                    resBody.alctiveList.addAll(de.getBody().alctiveList);
                    adapter.notifyDataSetChanged();
                }
                mCurrentPage++;
            }

            @Override
            public void onError(ResponseContent.Header header, HttpTaskHelper.RequestInfo requestInfo) {
                super.onError(header, requestInfo);
                Utilities.showToast(header.getRspDesc(), mContext);
            }
        });
    }


    private class ListAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<ActivityObj> alctiveList = new ArrayList<ActivityObj>();

        public ListAdapter(Context context, ArrayList<ActivityObj> alctiveList) {
            this.context = context;
            this.alctiveList = alctiveList;
        }


        @Override
        public int getCount() {
            return alctiveList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View currentView, ViewGroup parent) {
            ViewHolder holder = null;
            if (currentView == null) {
                holder = new ViewHolder();
                currentView = LayoutInflater.from(context).inflate(R.layout.group_item, null);
                holder.tv_screenings = (TextView) currentView.findViewById(R.id.tv_screenings);
                holder.tv_time = (TextView) currentView.findViewById(R.id.tv_time);
                holder.tv_last = (TextView)currentView. findViewById(R.id.tv_last);
                holder.tv_place = (TextView) currentView.findViewById(R.id.tv_place);
                holder.iv_tag_top = (ImageView)currentView. findViewById(R.id.iv_tag_top);
                holder.btn_ing = (Button) currentView.findViewById(R.id.btn_ing);
                currentView.setTag(holder);
            } else {
                holder = (ViewHolder) currentView.getTag();
            }
            ActivityObj obj = alctiveList.get(position);
            holder.tv_screenings.setText(obj.activeId + "    " + obj.activeTitle);
            holder.tv_time.setText(obj.activeDate);
            holder.tv_place.setText(obj.venueName);
            holder.tv_last.setText(obj.activeHours + "个小时");
            if ("0".equals(obj.isSpecial)) {
                holder.iv_tag_top.setVisibility(View.GONE);
            } else if ("1".equals(obj.isSpecial)) {
                holder.iv_tag_top.setVisibility(View.VISIBLE);
            }

            if ("-1".equals(obj.isOn)) {
                holder.btn_ing.setVisibility(View.GONE);
            } else if ("0".equals(obj.isOn)) {
                holder.btn_ing.setVisibility(View.VISIBLE);
                holder.btn_ing.setText("已结束");
            } else if ("1".equals(obj.isOn)) {
                holder.btn_ing.setVisibility(View.VISIBLE);
                holder.btn_ing.setText("进行中");
            }
            return currentView;
        }

        private class ViewHolder {
            private ImageView iv_tag_top;
            private TextView tv_screenings;
            private TextView tv_time;
            private TextView tv_place;
            private TextView tv_last;
            private Button btn_ing;
        }
    }

}
