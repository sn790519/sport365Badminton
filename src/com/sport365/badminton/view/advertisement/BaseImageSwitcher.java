package com.sport365.badminton.view.advertisement;

import static java.lang.Math.min;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.sport365.badminton.R;
import com.sport365.badminton.http.base.ImageLoadeCallback;
import com.sport365.badminton.http.base.ImageLoader;

/**
 * 图片切换器.
 *
 */
public abstract class BaseImageSwitcher<T> extends RelativeLayout implements OnItemSelectedListener, OnItemClickListener, OnTouchListener, ISwithcer<T> {

	// 指示器处于上方.
	public final static int LOCATION_INDICATER_TOP = ALIGN_PARENT_TOP;

	// 指示器处于下方.
	public final static int LOCATION_INDICATER_BOTTOM = ALIGN_PARENT_BOTTOM;

	private final static boolean _DBG_ = false;

	private final static int FLAG_AUTO_SWITCH = 0x10;

	// 默认3秒跳转广告.
	private int mAutoSwitchRate = 3000;

	protected final List<T> mDatas = new ArrayList<T>();

	protected final Context mContext;
	protected final LayoutInflater mInflater;

	private final RelativeLayout.LayoutParams adContentParam = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

	private final RelativeLayout.LayoutParams adIndicaterContainerParam = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

	// 广告容器
	private MyInfiniteGallery mContent;
	private ImageSwitcherAdapter mDefaultAdapter;
	private ImageLoader mImageLoader;

	private int dmWidth;

	// 控件宽高比，(建议设置宽高比)
	private int widthRat = -1;
	private int heightRat = -1;

	// 控件具体的宽度，高度值
	private int width;
	private int height;

	// 广告指示器
	private LinearLayout mAdIndicaterContainer;
	private ImageIndexUtil mAdIndicater;
	private int mIndicaterMargin;
	private int mIndicaterVisible = View.VISIBLE;
	private int mIndicaterLocation = LOCATION_INDICATER_BOTTOM;

	// 是否自动切换
	private boolean flag_switch = true;

	// 是否当前为Manual状态
	private boolean flag_manual = false;

	// 是否已经开启自动轮播.
	private boolean flag_start = false;

	private OnItemClickListener mItemClickListener;

	// ----------------------
	private Timer mTimer = null;
	private TimerTask mTask = null;

	// 允许最大循环播放数量.
	protected int mMaxImageCount = -1; // 当前实际最大数量
	protected int mSettingMaxCount = -1; // 设置预想最大数量

	@SuppressLint("HandlerLeak")
	private final Handler mAutoSwitchHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case FLAG_AUTO_SWITCH:
					switchItem();
					break;
				default:
					super.handleMessage(msg);
			}
		}

		;
	};

	public BaseImageSwitcher(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		initView();
	}

	public BaseImageSwitcher(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public BaseImageSwitcher(Context context) {
		super(context);
		this.mContext = context;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		initView();
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mContent != null) {
			mContent.onTouchEvent(event);
			return true;
		}
		return super.onTouchEvent(event);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				flag_manual = true;
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_OUTSIDE:
				flag_manual = false;
				break;
			default:
				break;
		}
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		final int true_position = position % mMaxImageCount;

		performItemClick(parent, view, position, id, true_position);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		mAdIndicater.setSelectIndex(position % mMaxImageCount);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

	/**
	 * 返回当前Image宽度(控件宽度),调用 {@link BaseImageSwitcher#setData(List)} 后有效
	 *
	 * @return 高度
	 */
	public int getImageWidth() {
		return width;
	}

	/**
	 * 返回当前Image高度(控件高度),调用 {@link BaseImageSwitcher#setData(List)} 后有效
	 *
	 * @return 高度
	 */
	public int getImageHeight() {
		return height;
	}

	/**
	 * 设置监听器.
	 *
	 * @param l
	 */
	public void setOnItemClickListener(OnItemClickListener l) {
		this.mItemClickListener = l;
	}

	/**
	 * 设置指示器的位置.
	 *
	 * @param location {@link #LOCATION_INDICATER_TOP} &
	 *                 {@link #LOCATION_INDICATER_BOTTOM}
	 */
	public void setIndicaterLocation(int location) {
		this.mIndicaterLocation = location;
	}

	/**
	 * 设置指示器显示状态.
	 *
	 * @param visibility
	 * @see {@link View#VISIBLE}
	 * @see {@link View#INVISIBLE}
	 * @see {@link View#GONE}
	 */
	public void setIndicaterVisible(int visibility) {
		if (mAdIndicater != null) {
			mAdIndicater.setVisibility(visibility);
		}
		mIndicaterVisible = visibility;
	}

	/**
	 * 设置自动播放间隔时间 (单位:ms).
	 *
	 * @param rate
	 */
	public void setAutoSwitchRate(int rate) {
		this.mAutoSwitchRate = rate;
	}

	/**
	 * @param datas
	 * @see #setData(ArrayList, boolean)
	 */
	public void setData(List<T> datas) {
		setData(datas, true);
	}

	/**
	 * 为广告填充数据.
	 *
	 * @param datas
	 * @param clearFirst 填充前是否清除之前的数据.
	 */
	public void setData(List<T> datas, boolean clearFirst) {
		if (datas == null || datas.isEmpty()) {
			return;
		}

		if (clearFirst) {
			mDatas.clear();
		}

		mDatas.addAll(datas);

		set();

		play();

		mDefaultAdapter.notifyDataSetChanged();
	}

	/**
	 * 设置广告的宽高比例，在调用setData之前才会起作用
	 *
	 * @param widthRat
	 * @param heightRat
	 */
	public void setScreenRate(int widthRat, int heightRat) {
		if (widthRat <= 0 || heightRat <= 0) {
			// throw new RuntimeException("widthRat & heightRat must > 0 !");
			return;
		}

		this.widthRat = widthRat;
		this.heightRat = heightRat;
	}

	/**
	 * 设置图片加载器.
	 *
	 * @param loader
	 */
	public final void setImageLoader(ImageLoader loader) {
		this.mImageLoader = loader;
	}

	/**
	 * 设置屏幕宽度.
	 */
	public final void setDmWidth(int w) {
		this.dmWidth = w;
	}

	/**
	 * 禁止自动播放. 于 {@code play} 前使用，{@code play} 后使用 {@code stop} 终止.
	 *
	 * @see #play()
	 * @see #stop()
	 */
	public void disabledAutoSwitch() {
		flag_switch = false;
	}

	/**
	 * 自动切换开启.
	 */
	public void play() {
		if (!flag_switch) {
			return;
		}

		if (mDatas.size() <= 1) {
			return;
		}

		if (flag_start) {
			return;
		}

		flag_start = true;

		mTimer = new Timer();

		mTask = new TimerTask() {
			@Override
			public void run() {
				notifyAutoSwitch();
			}
		};

		mTimer.schedule(mTask, mAutoSwitchRate, mAutoSwitchRate);
	}

	/**
	 * 停止自动播放.
	 */
	public void stop() {
		if (!flag_start) {
			return;
		}

		flag_start = false;

		if (mTask != null) {
			mTask.cancel();
			mTask = null;
		}

		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}

		if (mAutoSwitchHandler != null) {
			mAutoSwitchHandler.removeMessages(FLAG_AUTO_SWITCH);
		}
	}

	/**
	 * 重设指示器的位置.
	 */
	public void resetIndicater() {
		mAdIndicater.setSelectIndex(0);
	}

	/**
	 * 设置最大图片数.
	 *
	 * @param count
	 */
	public void setMaxCount(int count) {
		if (count <= 0) {
			return;
		}

		this.mSettingMaxCount = count;
	}

	/**
	 * 执行 Item-Click 事件.
	 *
	 * @param parent
	 * @param view
	 * @param position
	 * @param id
	 * @param true_position 真实的位置.
	 */
	protected boolean performItemClick(AdapterView<?> parent, View view, int position, long id, int true_position) {
		if (mItemClickListener != null) {
			return mItemClickListener.onItemClick(parent, view, position, id, true_position);
		}
		return false;
	}

	/**
	 * 通知需要切换广告
	 */
	protected void notifyAutoSwitch() {
		if (flag_manual) {
			return;
		}

		mAutoSwitchHandler.sendEmptyMessage(FLAG_AUTO_SWITCH);
	}

	/**
	 * 切换广告内容
	 */
	protected void switchItem() {
		mContent.onScroll(null, null, 1, 0);
		mContent.onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
	}

	/**
	 * 设置广告属性. 重写设置Gallery的属性.
	 *
	 * @param ad
	 */
	protected void setContentProperties(MyInfiniteGallery ad) {
		ad.setVerticalFadingEdgeEnabled(false);
		ad.setHorizontalFadingEdgeEnabled(false);
		ad.setSoundEffectsEnabled(false);
		ad.setSpacing(0);
		ad.setUnselectedAlpha(1);
	}

	@SuppressLint("ClickableViewAccessibility")
	private void initView() {
		WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metric = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(metric);

		dmWidth = metric.widthPixels;

		mIndicaterMargin = (int) (3 * metric.density + 0.5f);

		mContent = new MyInfiniteGallery(mContext.getApplicationContext());
		setContentProperties(mContent);
		mContent.setOnTouchListener(this);
		mContent.setOnItemSelectedListener(this);
		mContent.setOnItemClickListener(this);

		mDefaultAdapter = new ImageSwitcherAdapter();
		mContent.setAdapter(mDefaultAdapter);

		mAdIndicaterContainer = new LinearLayout(mContext.getApplicationContext());
		mAdIndicater = new ImageIndexUtil(mContext.getApplicationContext());

		mAdIndicaterContainer.addView(mAdIndicater);
	}

	/**
	 * set self , set & add child view.
	 */
	private void set() {
		final int ad_count = mDatas.size();

		// set self values.
		if (widthRat != -1 && heightRat != -1) {
			width = dmWidth;
			height = (int) (dmWidth * 1.0f / widthRat * heightRat);

			ViewGroup.LayoutParams rl_param = getLayoutParams();
			if (rl_param == null) {
				rl_param = new LayoutParams(width, height);
			} else {
				rl_param.width = width;
				rl_param.height = height;
			}
			setLayoutParams(rl_param);
		}

		// remove views
		if (getChildCount() > 0) {
			removeAllViews();
		}

		// set & add ad_content.
		addView(mContent, adContentParam);

		// set & add ad_indicater.
		adIndicaterContainerParam.addRule(mIndicaterLocation);
		adIndicaterContainerParam.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		adIndicaterContainerParam.bottomMargin = mIndicaterMargin;
		adIndicaterContainerParam.topMargin = mIndicaterMargin;

		// for test whether used.
		// mAdIndicaterContainer.setBackgroundColor(Color.RED);
		addView(mAdIndicaterContainer, adIndicaterContainerParam);

		mMaxImageCount = mSettingMaxCount == -1 ? ad_count : min(ad_count, mSettingMaxCount);

		mContent.setGalleryCount(mMaxImageCount);
		mAdIndicater.setTotal(mMaxImageCount);

		mAdIndicater.setVisibility(mMaxImageCount <= 1 ? View.GONE : mIndicaterVisible);
	}

	void printf(String format, Object... args) {
		if (_DBG_)
			Log.e(getClass().getSimpleName(), String.format(format, args));
	}

	public interface OnItemClickListener {

		/**
		 * OnItemClickListener
		 *
		 * @param parent
		 * @param view
		 * @param position
		 * @param id
		 * @param true_position 实际的position.
		 * @return if true - handled.
		 */
		boolean onItemClick(AdapterView<?> parent, View view, int position, long id, int true_position);
	}

	protected static class ViewHolder {
		public ImageView ad_img;
		public ProgressBar ad_pb;
	}

	/**
	 * 广告适配器基类，循环播放.
	 *
	 * @author sk. 09145
	 * @date 2014-9-25
	 */
	public class ImageSwitcherAdapter extends BaseAdapter {
		private ViewHolder mHolder;
		private ImageLoadeCallback mCallback;

		@SuppressLint("HandlerLeak")
		protected final Handler mPbHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				ProgressBar pb_loaing = (ProgressBar) msg.obj;
				if (msg.what != 0) {
					pb_loaing.setProgress(msg.what);
					pb_loaing.postInvalidate();
				}
			}

			;
		};

		public ImageSwitcherAdapter() {
			if (mImageLoader == null) {
				mImageLoader = ImageLoader.getInstance();
			}
		}

		public final int getDataSize() {
			return mDatas == null ? 0 : mDatas.size();
		}

		@Override
		public int getCount() {
			final int count = getDataSize();
			return count > 1 ? Integer.MAX_VALUE : count;
		}

		@Override
		public Object getItem(int position) {
			return mDatas == null ? null : mDatas.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.image_of_home_gallery_item, parent, false);

				mHolder = new ViewHolder();
				mHolder.ad_img = (ImageView) convertView.findViewById(R.id.img_home_gallery);
				mHolder.ad_pb = (ProgressBar) convertView.findViewById(R.id.pb_img_loading);

				mCallback = new ImageLoadeCallback(mHolder.ad_pb, mPbHandler);

				convertView.setTag(mHolder);
			} else {
				mHolder = (ViewHolder) convertView.getTag();
			}

			final int item_position = position % mMaxImageCount;
			final T data = mDatas.get(item_position);

			if (data != null) {
				mImageLoader.displayImage(getUrlString(data), mHolder.ad_img, mCallback);
			}
			return convertView;
		}
	}
}
