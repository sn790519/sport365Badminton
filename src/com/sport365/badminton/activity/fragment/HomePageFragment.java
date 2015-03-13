package com.sport365.badminton.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.sport365.badminton.BaseFragment;
import com.sport365.badminton.R;
import com.sport365.badminton.activity.*;
import com.sport365.badminton.entity.obj.HomeKeyWordObj;
import com.sport365.badminton.entity.obj.SportAdvertismentObj;
import com.sport365.badminton.entity.obj.SportThemeObj;
import com.sport365.badminton.entity.reqbody.GetSprotHomeReqBody;
import com.sport365.badminton.entity.resbody.GetSprotHomeResBody;
import com.sport365.badminton.entity.webservice.SportParameter;
import com.sport365.badminton.entity.webservice.SportWebService;
import com.sport365.badminton.http.base.HttpTaskHelper.JsonResponse;
import com.sport365.badminton.http.base.HttpTaskHelper.RequestInfo;
import com.sport365.badminton.http.base.IRequestProxyCallback;
import com.sport365.badminton.http.base.ImageLoader;
import com.sport365.badminton.http.json.req.ServiceRequest;
import com.sport365.badminton.http.json.res.ResponseContent;
import com.sport365.badminton.http.json.res.ResponseContent.Header;
import com.sport365.badminton.utils.SystemConfig;
import com.sport365.badminton.utils.BundleKeys;
import com.sport365.badminton.utils.Utilities;
import com.sport365.badminton.view.NoScrollGridView;
import com.sport365.badminton.view.advertisement.AdvertisementView;

import java.util.ArrayList;

/**
 * 首页界面
 *
 * @author Frank
 */
public class HomePageFragment extends BaseFragment {
    private final float Module_Image_Ratio = 1.0f;// 4块项目的比例
    private final float Feature_Image_Ratio = 0.4f;// middle
    private float moduleTitleSize;

    //广告
    private ArrayList<SportAdvertismentObj> advertismentlist = new ArrayList<SportAdvertismentObj>(); // 广告top
    private ArrayList<SportAdvertismentObj> endAdvertismentlist = new ArrayList<SportAdvertismentObj>(); // 广告end
    private AdvertisementView advertisementControlLayout;
    private LinearLayout ll_ad_layout;
    private AdvertisementView advertisementControlLayout_bottom;
    private LinearLayout ll_ad_layout_bottom;

    // Top4
    private NoScrollGridView gv_pic_uploads;
    private ArrayList<HomeKeyWordObj> homeKeyWordsList = new ArrayList<HomeKeyWordObj>();//导航菜单，4个
    private ModuleAdapter moduleAdapter = null; // top

    // middle4
    private NoScrollGridView gv_middle_uploads;
    public ArrayList<SportThemeObj> sportThemeList = new ArrayList<SportThemeObj>();
    private FeatureAdapter mFeatureAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_page_layout, container, false);
        gv_pic_uploads = (NoScrollGridView) view.findViewById(R.id.gv_pic_uploads);
        gv_middle_uploads = (NoScrollGridView) view.findViewById(R.id.gv_middle_uploads);
        ll_ad_layout = (LinearLayout) view.findViewById(R.id.ll_ad_layout);
        ll_ad_layout_bottom = (LinearLayout) view.findViewById(R.id.ll_ad_layout_bottom);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO activity 创建成功后执行getactivity();
        super.onActivityCreated(savedInstanceState);
        initMainPageRequest();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent = null;
        switch (v.getId()) {
//			case R.id.tv_sport_field:
//				intent = new Intent(getActivity(), ActivityCenterListAtivity.class);
//				startActivity(intent);
//				break;
//			case R.id.tv_club:
//				intent = new Intent(getActivity(), ClubListActivity.class);
//				startActivity(intent);
//				break;
//			case R.id.tv_activity:
//				intent = new Intent(getActivity(), ActivityListActivity.class);
//				startActivity(intent);
//				break;
//			case R.id.tv_game:
//				intent = new Intent(getActivity(), PlayListActivity.class);
//				startActivity(intent);
//				break;
//			case R.id.iv_sport_calendar:
//				intent = new Intent(getActivity(), CalendarTimesActivity.class);
//				startActivity(intent);
//				break;
        }
    }

    /**
     * TODO 首页接口的请求
     */
    private void initMainPageRequest() {
        GetSprotHomeReqBody reqBody = new GetSprotHomeReqBody();
        reqBody.CityId = "226";
        sendRequestWithDialog(new ServiceRequest(getActivity(), new SportWebService(SportParameter.GET_SPROT_HOME), reqBody), null, new IRequestProxyCallback() {

            @Override
            public void onSuccess(JsonResponse jsonResponse, RequestInfo requestInfo) {
                ResponseContent<GetSprotHomeResBody> de = jsonResponse.getResponseContent(GetSprotHomeResBody.class);
                GetSprotHomeResBody resBody = de.getBody();
                initFragmentView(resBody);
            }

            @Override
            public void onError(Header header, RequestInfo requestInfo) {
                // TODO Auto-generated method stub
                super.onError(header, requestInfo);
            }
        });
    }

    // 根据接口初始化view
    private void initFragmentView(GetSprotHomeResBody resBody) {
        SystemConfig.aboutUs = resBody.aboutUs;
        SystemConfig.contactUs = resBody.contactUs;

        // 顶部广告数据源
        advertismentlist = resBody.sportAdvertismentList;
        // 底部广告数据源
        SportAdvertismentObj endAD = new SportAdvertismentObj();
        endAD.imageUrl = resBody.homeImgUrl;
        endAdvertismentlist.add(endAD);
        initADS();

        // top4块内容
        homeKeyWordsList = resBody.homeKeyWordsList;
        int moduleWidth = dm.widthPixels / 4;
        int moduleHeight = (int) (moduleWidth * Module_Image_Ratio);
        moduleAdapter = new ModuleAdapter(moduleWidth, moduleHeight);
        gv_pic_uploads.setAdapter(moduleAdapter);

        // middle4块内容
        sportThemeList = resBody.sportThemeList;
        int featureWidth = dm.widthPixels / 2;
        int featureHeight = (int) (featureWidth * Feature_Image_Ratio);
        mFeatureAdapter = new FeatureAdapter(featureWidth, featureHeight);
        gv_middle_uploads.setAdapter(mFeatureAdapter);
    }

    // 初始化广告
    private void initADS() {
        advertisementControlLayout = new AdvertisementView(getActivity());
        advertisementControlLayout.setAdvertisementRate(8, 3);
        advertisementControlLayout.setImageLoader(ImageLoader.getInstance());
        ll_ad_layout.addView(advertisementControlLayout);
        if (advertismentlist != null && advertismentlist.size() > 0) {
            advertisementControlLayout.setAdvertisementData(advertismentlist);
            ll_ad_layout.setVisibility(View.VISIBLE);
        }

        advertisementControlLayout_bottom = new AdvertisementView(getActivity());
        advertisementControlLayout_bottom.setAdvertisementRate(8, 3);
        advertisementControlLayout_bottom.setImageLoader(ImageLoader.getInstance());
        ll_ad_layout_bottom.addView(advertisementControlLayout_bottom);
        if (endAdvertismentlist != null && endAdvertismentlist.size() > 0) {
            advertisementControlLayout_bottom.setAdvertisementData(endAdvertismentlist);
            ll_ad_layout_bottom.setVisibility(View.VISIBLE);
        }

        moduleTitleSize = (float) getAdjustSize(13);

    }

    // Home Top girdView
    protected class ModuleAdapter extends BaseAdapter {
        private int itemWidth;
        private int itemHeight;

        public ModuleAdapter(int itemWidth, int itemHeight) {
            super();
            this.itemWidth = itemWidth;
            this.itemHeight = itemHeight;
        }

        @Override
        public int getCount() {
            return homeKeyWordsList.size();
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            convertView = mLayoutInflater.inflate(
                    R.layout.home_top_item_layout, parent, false);
            TextView tv_module = (TextView) convertView.findViewById(R.id.tv_module);
            ImageView iv_module = (ImageView) convertView.findViewById(R.id.iv_module);
            AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
                    itemWidth, AbsListView.LayoutParams.WRAP_CONTENT);
            convertView.setLayoutParams(layoutParams);
            int ivWidth = itemWidth * 3 / 5;
            int ivHeight = (int) (ivWidth * Module_Image_Ratio);
            RelativeLayout.LayoutParams iv_layoutParams = new RelativeLayout.LayoutParams(ivWidth, ivHeight);
            iv_layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, -1);
            iv_layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, -1);
            iv_module.setLayoutParams(iv_layoutParams);
            final HomeKeyWordObj mItemKeywordObj = homeKeyWordsList.get(position);
            String title = "";
            if (mItemKeywordObj != null && !TextUtils.isEmpty(mItemKeywordObj.title)) {
                title = mItemKeywordObj.title;
            }
            tv_module.setTextSize(TypedValue.COMPLEX_UNIT_SP, moduleTitleSize);
            tv_module.setText(title);
            mImageLoader.displayImage(homeKeyWordsList.get(position).imageUrl, iv_module);
            // 4项目点击事件
            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //public String urlType;// 0:客户端跳转 1:跳h5
                    //public String typeId;1:运动会所 2:俱乐部 3:活动 4:比赛
                    Intent intent = null;
                    if (mItemKeywordObj != null && !TextUtils.isEmpty(mItemKeywordObj.typeId)) {
                        if (!TextUtils.isEmpty(mItemKeywordObj.urlType) && "0".equals(mItemKeywordObj.urlType)) {
                            if ("1".equals(mItemKeywordObj.typeId)) {
                                intent = new Intent(getActivity(), ActivityCenterListAtivity.class);
                                intent.putExtra(BundleKeys.ACTIONBAETITLE, mItemKeywordObj.title);
                                startActivity(intent);
                            } else if ("2".equals(mItemKeywordObj.typeId)) {
                                intent = new Intent(getActivity(), ClubListActivity.class);
                                intent.putExtra(BundleKeys.ACTIONBAETITLE, mItemKeywordObj.title);
                                startActivity(intent);
                            } else if ("3".equals(mItemKeywordObj.typeId)) {
                                intent = new Intent(getActivity(), ActivityListActivity.class);
                                intent.putExtra(BundleKeys.ACTIONBAETITLE, mItemKeywordObj.title);
                                startActivity(intent);
                            } else if ("4".equals(mItemKeywordObj.typeId)) {
                                intent = new Intent(getActivity(), PlayListActivity.class);
                                intent.putExtra(BundleKeys.ACTIONBAETITLE, mItemKeywordObj.title);
                                startActivity(intent);
                            } else {
                                intent = new Intent(getActivity(), MyWebViewActivity.class);
                                intent.putExtra(BundleKeys.WEBVIEEW_LOADURL, mItemKeywordObj.jumpUrl);
                                intent.putExtra(BundleKeys.WEBVIEEW_TITLE, mItemKeywordObj.title);
                                startActivity(intent);
                            }
                        } else {
                            intent = new Intent(getActivity(), MyWebViewActivity.class);
                            intent.putExtra(BundleKeys.WEBVIEEW_LOADURL, mItemKeywordObj.jumpUrl);
                            intent.putExtra(BundleKeys.WEBVIEEW_TITLE, mItemKeywordObj.title);
                            startActivity(intent);
                        }
                    } else {
                        Utilities.showToast("Data error", getActivity());
                    }
                }
            });
            return convertView;
        }
    }

    // 转换字体大小
    private double getAdjustSize(double size) {
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        int width = mDisplayMetrics.widthPixels;
        float density = mDisplayMetrics.density;
        size = (width / 540.00) / (density / 1.5) * size;
        return size;
    }


    /**
     * middle 4 adapter
     */
    class FeatureAdapter extends BaseAdapter {
        private int itemWidth;
        private int itemHeight;

        public FeatureAdapter(int itemWidth, int itemHeight) {
            super();
            this.itemWidth = itemWidth;
            this.itemHeight = itemHeight;
        }

        @Override
        public int getCount() {
            return sportThemeList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            convertView = mLayoutInflater.inflate(
                    R.layout.home_middle_item_layout, parent, false);
            ImageView featureImageView = (ImageView) convertView
                    .findViewById(R.id.iv_feature_item);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    itemWidth, itemHeight);
            featureImageView.setLayoutParams(layoutParams);
            final SportThemeObj mItemSportObj = sportThemeList.get(position);
            // 下载并显示广告图
            if (mItemSportObj != null) {
                String imgUrl = mItemSportObj.imageUrl;
                if (!TextUtils.isEmpty(imgUrl)) {
                    mImageLoader.displayImage(imgUrl,
                            featureImageView);
                } else {
                    featureImageView
                            .setImageResource(R.drawable.bg_default_common);
                }
            }

            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //public String urlType;// 0:客户端跳转 1:跳h5
                    //public String typeId;// 1:我身边（定位） -- 场馆列表 2:运动日历 -- 活动列表 3:抽奖： h5 4:期待：h5
                    Intent intent = null;
                    if (mItemSportObj != null && !TextUtils.isEmpty(mItemSportObj.typeId)) {
                        if (!TextUtils.isEmpty(mItemSportObj.urlType) && "0".equals(mItemSportObj.urlType)) {
                            if ("1".equals(mItemSportObj.typeId)) {
                                intent = new Intent(getActivity(), ClubListActivity.class);
                                intent.putExtra(BundleKeys.ACTIONBAETITLE, mItemSportObj.title);
                                startActivity(intent);
                            } else if ("2".equals(mItemSportObj.typeId)) {
                                intent = new Intent(getActivity(), CalendarTimesActivity.class);
                                intent.putExtra(BundleKeys.ACTIONBAETITLE, mItemSportObj.title);
                                startActivity(intent);
                            } else {
                                intent = new Intent(getActivity(), MyWebViewActivity.class);
                                intent.putExtra(BundleKeys.WEBVIEEW_LOADURL, mItemSportObj.jumpUrl);
                                intent.putExtra(BundleKeys.WEBVIEEW_TITLE, mItemSportObj.title);
                                startActivity(intent);
                            }
                        } else {
                            intent = new Intent(getActivity(), MyWebViewActivity.class);
                            intent.putExtra(BundleKeys.WEBVIEEW_LOADURL, mItemSportObj.jumpUrl);
                            intent.putExtra(BundleKeys.WEBVIEEW_TITLE, mItemSportObj.title);
                            startActivity(intent);
                        }
                    } else {
                        Utilities.showToast("Data error", getActivity());
                        intent = new Intent(getActivity(), CalendarTimesActivity.class);
                        intent.putExtra(BundleKeys.ACTIONBAETITLE, mItemSportObj.title);
                        startActivity(intent);
                    }
                }
            });
            return convertView;
        }

    }
}
