package com.sport365.badminton.view.pullrefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.*;
import android.widget.AbsListView.OnScrollListener;

public abstract class PullToRefreshAbsListViewBase<T extends AbsListView> extends PullToRefreshBase<T> implements
		OnScrollListener {
	
	/**
	 * 滑动监听器（与AbsListView.OnScrollListener的作用相同）
	 *
	 */
	public interface PullToRefreshOnScrollListener extends OnScrollListener{}
	
	private int lastSavedBottomVisibleItem=-1;
	private PullToRefreshOnScrollListener pullToRefreshOnScrollListener;
	private View emptyView;
	private FrameLayout refreshableViewHolder;
	private int mtotalItemCount;
	private int bottomVisibleItem;
	private int ADVANCE_COUNT=0;//预加载提前量
//	private int firstItem = 0,pad,index;// 滑动之前第一个item的位置,ListView的padding值,滑动的第一个可见Item值
//	private static Animation translate_foot_bottom_to_top,//底部TAB显示动画
//							translate_foot_top_to_bottom,//底部TAB隐藏动画
//							translate_header_bottom_to_top,//头部TAb隐藏动画
//							translate_header_top_to_bottom;//头部TAb显示动画
//	private View tabViewBottom,tabViewHeader,myView;
//	private boolean inScroll = false,isHeaderAnimationFinish =true,isBottomAnimationFinish = true;//是否已经滑动过,动画是否已经结束
	
	public PullToRefreshAbsListViewBase(Context context) {
		super(context);
	}

	public PullToRefreshAbsListViewBase(Context context, int mode) {
		super(context, mode);
	}

	public PullToRefreshAbsListViewBase(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	
	abstract public ContextMenuInfo getContextMenuInfo();

	@Override
	public  void onScroll( AbsListView view,  int firstVisibleItem,  int visibleItemCount,
		final int totalItemCount) {
		mtotalItemCount=totalItemCount;
		bottomVisibleItem=firstVisibleItem + visibleItemCount;
//		checkRefreshAble();
		if (pullToRefreshOnScrollListener!=null) {
				pullToRefreshOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
			}
//		myView = view;
//		index = firstVisibleItem;
//		//设置列表滑动时TAB隐藏和显示的监听
//		if(tabViewHeader!=null){
//			tabViewHeader.bringToFront();//将当前动画组件放在最上层
//		}
//		if(tabViewBottom!=null){
//			tabViewBottom.bringToFront();//将当前动画组件放在最上层
//		}
//			
//		if (!(view.getLastVisiblePosition() == totalItemCount - 1)) {
//			if (firstVisibleItem - firstItem > 0 ) {
//				if(tabViewHeader != null){
//					if(tabViewHeader.getVisibility() == View.VISIBLE){
//						tabViewHeader.clearAnimation();
//						tabViewHeader.startAnimation(translate_header_bottom_to_top);
//						tabViewHeader.setVisibility(View.GONE);
//						if(view.getPaddingTop() != 0){
//							pad = view.getPaddingTop();
//						}
//						view.setPadding(0, 0, 0, 0);
//					}
//				}
//				if(tabViewBottom != null){
//					if(tabViewBottom.getVisibility() == View.VISIBLE){
//						tabViewBottom.clearAnimation();
//						tabViewBottom.startAnimation(translate_foot_top_to_bottom);
//						tabViewBottom.setVisibility(View.GONE);
//					}
//				}
//			}
//		}
//		if(view.getFirstVisiblePosition() == 0 && inScroll){//到达头部
//			if(tabViewHeader != null){
//				if(tabViewHeader.getVisibility() == View.VISIBLE){
//					if(view.getPaddingTop() == 0){
//						view.setPadding(0, pad, 0, 0);
//					}
//				}
//			}
//			
//			if(tabViewBottom != null){
//				tabViewBottom.setVisibility(View.VISIBLE);
//			}
//			view.setSelection(0);
//			inScroll = false;
//		}
		
	}

	@Override
	public  void onScrollStateChanged( AbsListView view,  int scrollState) {
		if ((getMode()==MODE_AUTO_REFRESH||getMode()==(MODE_AUTO_REFRESH|MODE_PULL_DOWN_TO_REFRESH))&&bottomVisibleItem == mtotalItemCount-ADVANCE_COUNT /*&& scrollState == OnScrollListener.SCROLL_STATE_IDLE*/) {//到达底部&&在滑动
			if(lastSavedBottomVisibleItem!=bottomVisibleItem&&getOnRefreshListener()!=null){
				lastSavedBottomVisibleItem=bottomVisibleItem;
				if(getOnRefreshListener().onRefresh(MODE_AUTO_REFRESH))
					setRefreshingInternal(true);
			}
		}
		if (pullToRefreshOnScrollListener!=null) {
			pullToRefreshOnScrollListener.onScrollStateChanged(view, scrollState);
		}
		
		
//		firstItem = view.getFirstVisiblePosition();
//  		if (scrollState == SCROLL_STATE_IDLE) {
//  			inScroll =true;
//			if(tabViewHeader != null){
//				if(tabViewHeader.getVisibility() == View.GONE){
//					if(isHeaderAnimationFinish){
//						tabViewHeader.clearAnimation();
//						tabViewHeader.startAnimation(translate_header_top_to_bottom);
//						isHeaderAnimationFinish =false;
//					}
//				}
//			}
//			if(tabViewBottom != null){
//				if(tabViewBottom.getVisibility() == View.GONE){
////					if(isBottomAnimationFinish){
//						tabViewBottom.clearAnimation();
//						tabViewBottom.startAnimation(translate_foot_bottom_to_top);
//						isBottomAnimationFinish = false;
////					}
//				}
//			}
//		}
	}
	
//	/**
//	 * 检查是否可以继续加载
//	 */
//	private void checkRefreshAble(){
//		if(bottomVisibleItem == mtotalItemCount-ADVANCE_COUNT){
//			if(lastSavedBottomVisibleItem!=bottomVisibleItem&&getOnRefreshListener()!=null){
//				lastSavedBottomVisibleItem=bottomVisibleItem;
//			if(getOnRefreshListener().onRefresh())
//				showBottomRefreshing();
//			}
//		}
//	}
	
	/**
	 * 刷新提前量（默认到底部刷新）
	 * @param count
	 */
	public void setAdvanceCount(int count){
		ADVANCE_COUNT=count;
	}
	
	/**
	 * 使用场景：<br/><br/>
	 * 1	列表底部加载后(已记录该位置已加载)，由于网络问题等原因数据未加载成功,需要再次重新加载，这时就要调用
	 * 此方法重新设置此位置可加载<br/><br/>
	 * 
	 * 2	多数据集共享列表：当前菜单位置,列表请求第1页数据后(假设一页数据为20条)，滑动到底部加载第2页，此时position=20的位置已记录刷新，
	 * 然后再点击其他菜单加载数据后，列表再次加载第2页数据时就不会在自动刷新了，
	 * 因为之前菜单状态已记录过该位置(20)刷新过，这时就要调用此方法重新设置该底部可加载<br/>
	 * <b>ps:建议每次点击菜单的时候调用下此方法</b><br/><br/>
	 * 
	 * 3	业务需要<br/>
	 * 
	 * @param refreshAble
	 * 	true :当前底部可自动刷新 ; false 当前底部不可刷新
	 */
	public void setCurrentBottomAutoRefreshAble(boolean refreshAble){
		if(refreshAble){
			lastSavedBottomVisibleItem=-1;
		}else{
			if(refreshableView.getAdapter()!=null)
				lastSavedBottomVisibleItem=refreshableView.getAdapter().getCount()-ADVANCE_COUNT;
		}
	}
	
	public void setBackToTopView(ImageView mTopImageView){
		mTopImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (refreshableView instanceof ListView ) {
					((ListView)refreshableView).setSelection(0);
				}else if(refreshableView instanceof GridView){
					((GridView)refreshableView).setSelection(0);
				}
			}
		});
	}

	/**
	 * Sets the Empty View to be used by the Adapter View.
	 * 
	 * We need it handle it ourselves so that we can Pull-to-Refresh when the
	 * Empty View is shown.
	 * 
	 * Please note, you do <strong>not</strong> usually need to call this method
	 * yourself. Calling setEmptyView on the AdapterView will automatically call
	 * this method and set everything up. This includes when the Android
	 * Framework automatically sets the Empty View based on it's ID.
	 * 
	 * @param newEmptyView
	 *            - Empty View to be used
	 */
	public final void setEmptyView(View newEmptyView) {
		// If we already have an Empty View, remove it
		if (null != emptyView) {
			refreshableViewHolder.removeView(emptyView);
		}

		if (null != newEmptyView) {
			ViewParent newEmptyViewParent = newEmptyView.getParent();
			if (null != newEmptyViewParent && newEmptyViewParent instanceof ViewGroup) {
				((ViewGroup) newEmptyViewParent).removeView(newEmptyView);
			}

			this.refreshableViewHolder.addView(newEmptyView, ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.FILL_PARENT);
		}

		if (refreshableView instanceof EmptyViewMethodAccessor) {
			((EmptyViewMethodAccessor) refreshableView).setEmptyViewInternal(newEmptyView);
		} else {
			this.refreshableView.setEmptyView(newEmptyView);
		}
	}

	

	public final PullToRefreshOnScrollListener getOnScrollListener() {
		return pullToRefreshOnScrollListener;
	}

	public final void setOnScrollListener(PullToRefreshOnScrollListener onScrollListener) {
		this.pullToRefreshOnScrollListener = onScrollListener;
		if(getMode()!=MODE_AUTO_REFRESH&&getMode()!=(MODE_AUTO_REFRESH|MODE_PULL_DOWN_TO_REFRESH))
			refreshableView.setOnScrollListener(pullToRefreshOnScrollListener);
	}

	protected void addRefreshableView(Context context, T refreshableView) {
		refreshableViewHolder = new FrameLayout(context);
		refreshableViewHolder.addView(refreshableView, ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.FILL_PARENT);
		addView(refreshableViewHolder, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 0, 1.0f));
	};

	protected boolean isReadyForPullDown() {
		return isFirstItemVisible();
	}

	protected boolean isReadyForPullUp() {
		return isLastItemVisible();
	}

	private boolean isFirstItemVisible() {
		if (this.refreshableView.getCount() == 0) {
			return true;
		} else if (refreshableView.getFirstVisiblePosition() == 0) {
			
			final View firstVisibleChild = refreshableView.getChildAt(0);
			
			if (firstVisibleChild != null) {
				return firstVisibleChild.getTop() >= refreshableView.getTop();
			}
		}
		
		return false;
	}

	private boolean isLastItemVisible() {
		final int count = this.refreshableView.getCount();
		final int lastVisiblePosition = refreshableView.getLastVisiblePosition();

		if (count == 0) {
			return true;
		} else if (lastVisiblePosition == count - 1) {

			final int childIndex = lastVisiblePosition - refreshableView.getFirstVisiblePosition();
			final View lastVisibleChild = refreshableView.getChildAt(childIndex);

			if (lastVisibleChild != null) {
				return lastVisibleChild.getBottom() <= refreshableView.getBottom();
			}
		}
		return false;
	}
	
	
	/*
	 *考虑到列表滑动流畅性，在不是MODE_AUTO_REFRESH的模式下不设置对OnScroll的监听 
	 */
	@Override
	public void setMode(int mode) {
		super.setMode(mode);
		switch (mode) {
		case MODE_AUTO_REFRESH|MODE_PULL_DOWN_TO_REFRESH:
		case MODE_AUTO_REFRESH:
			refreshableView.setOnScrollListener(this);
//			setPadding(0, -getHeaderHeight() ,0, getHeaderHeight());
//			if(getFooterLayout()!=null){
//				getFooterLayout().setVisibility(View.GONE);
//				getFooterLayout().refreshing();
//			}
			break;
		default:
			refreshableView.setOnScrollListener(pullToRefreshOnScrollListener);
//			setPadding(0, -getHeaderHeight() ,0, -getHeaderHeight());
//			if(getFooterLayout()!=null)
//				getFooterLayout().setVisibility(View.VISIBLE);
			break;
		}
	}
	/**
	 * 设置列表滑动TAB隐藏和显示
	 * @param mContext
	 * @param tabViewBottom  底部隐藏按钮
	 * @param tabViewHeader 头部隐藏按钮
	 */
//	public void setTabView(Context mContext, final View tabViewBottom , final View tabViewHeader){
//		this.tabViewBottom = tabViewBottom;
//		this.tabViewHeader = tabViewHeader;
//		System.out.println(tabViewHeader);
//		if(tabViewHeader !=null ){//头部TAB
//			translate_header_top_to_bottom = AnimationUtils.loadAnimation(
//					mContext, R.anim.tab_show_header_top_bottom);//头部TAB显示动画
//			translate_header_bottom_to_top = AnimationUtils.loadAnimation(
//					mContext, R.anim.tab_show_header_bottom_top);//头部TAB隐藏动画
//			
//			//头部动画监听
//			translate_header_bottom_to_top.setAnimationListener(new AnimationListener() {
//				
//				@Override
//				public void onAnimationStart(Animation animation) {
//					if(translate_header_bottom_to_top.hasStarted()){
//						translate_header_bottom_to_top.reset();
//						
//					 }
//				}
//				
//				@Override
//				public void onAnimationRepeat(Animation animation) {
//				}
//				
//				@Override
//				public void onAnimationEnd(Animation animation) {
//					if(tabViewHeader != null){
//						tabViewHeader.setVisibility(View.GONE);
//					}
//					
//				}
//			});
//			translate_header_top_to_bottom.setAnimationListener(new AnimationListener() {
//				
//				@Override
//				public void onAnimationStart(Animation animation) {
//					 if(translate_header_top_to_bottom.hasStarted()){
//						 translate_header_top_to_bottom.reset();
//					 }
//				}
//				
//				@Override
//				public void onAnimationRepeat(Animation animation) {
//				}
//				
//				@Override
//				public void onAnimationEnd(Animation animation) {
//					isHeaderAnimationFinish = true;
//					if(tabViewHeader != null){
//						tabViewHeader.setVisibility(View.VISIBLE);
//					}
//					if(index == 0){
//						if(myView.getPaddingTop() == 0){
//							myView.setPadding(0, pad, 0, 0);
//						}
//					}
//				}
//			});
//		}
//	  if(tabViewBottom != null){//底部TAB
//			translate_foot_bottom_to_top = AnimationUtils.loadAnimation(
//					mContext, R.anim.alpha_flight_sort_in);//底部TAB显示
//			translate_foot_top_to_bottom = AnimationUtils.loadAnimation(
//					mContext, R.anim.flight_sort_top_to_bottom);//底部TAB隐藏动画
//		
//			//底部动画监听
//			translate_foot_bottom_to_top.setAnimationListener(new AnimationListener() {
//				
//				@Override
//				public void onAnimationStart(Animation animation) {
//					if(translate_foot_bottom_to_top.hasStarted()){
//						translate_foot_bottom_to_top.reset();
//					}
//				}
//				
//				@Override
//				public void onAnimationRepeat(Animation animation) {
//					
//				}
//				
//				@Override
//				public void onAnimationEnd(Animation animation) {
//					isBottomAnimationFinish = true;
//					if(tabViewBottom != null){
//						tabViewBottom.setVisibility(View.VISIBLE);
//					}
//				}
//			});
//			
//			translate_foot_top_to_bottom.setAnimationListener(new AnimationListener() {
//				
//				@Override
//				public void onAnimationStart(Animation animation) {
//					if(translate_foot_top_to_bottom.hasStarted()){
//						translate_foot_top_to_bottom.reset();
//					}
//				}
//				
//				@Override
//				public void onAnimationRepeat(Animation animation) {
//					
//				}
//				
//				@Override
//				public void onAnimationEnd(Animation animation) {
//					if(tabViewBottom != null){
//						tabViewBottom.setVisibility(View.GONE);
//					}
//				}
//			});
//		}
//	}
}
