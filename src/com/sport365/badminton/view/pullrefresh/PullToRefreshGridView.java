package com.sport365.badminton.view.pullrefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListAdapter;

public class PullToRefreshGridView extends PullToRefreshAbsListViewBase<GridView> {

	private View mHeaderView = null;

	private int mTouchSlop = 0;
	private int mLastXIntercept = 0;
	private int mLastYIntercept = 0;
	private int mLastY = 0;
	
	private static final int HEADER_VIEW_TOP = 0;
	private static final int HEADER_VIEW_CENTER = 1;
	private static final int HEADER_VIEW_BOTTOM = 2;
	private int mHeaderViewState = HEADER_VIEW_TOP;
	
	private static final int OPERATION_NONE = 0;
	private static final int OPERATION_UP = 1;
	private static final int OPERATION_DOWN = 2;
	private int mHeaderViewOperation = OPERATION_NONE;
	
	private FrameLayout.LayoutParams mHeaderParams = null;
	private FrameLayout.LayoutParams mMainParams = null;

	class InternalGridView extends GridView implements EmptyViewMethodAccessor {

		public InternalGridView(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		@Override
		public void setEmptyView(View emptyView) {
			PullToRefreshGridView.this.setEmptyView(emptyView);
		}

		@Override
		public void setEmptyViewInternal(View emptyView) {
			super.setEmptyView(emptyView);
		}

		public ContextMenuInfo getContextMenuInfo() {
			return super.getContextMenuInfo();
		}
	}

	public PullToRefreshGridView(Context context) {
		super(context);
		this.setDisableScrollingWhileRefreshing(false);
	}

	public PullToRefreshGridView(Context context, int mode) {
		super(context, mode);
		this.setDisableScrollingWhileRefreshing(false);
	}

	public PullToRefreshGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setDisableScrollingWhileRefreshing(false);
	}

	@Override
	public ContextMenuInfo getContextMenuInfo() {
		return ((InternalGridView) getRefreshableView()).getContextMenuInfo();
	}

	@Override
	protected final GridView createRefreshableView(Context context, AttributeSet attrs) {
		GridView lv = new InternalGridView(context, attrs);

		// Set it to this so it can be used in ListActivity/ListFragment
		lv.setId(android.R.id.list);
		return lv;
	}

	public void setAdapter(ListAdapter adapter) {
		refreshableView.setAdapter(adapter);
	}

	public void setOnItemClickListener(OnItemClickListener listener) {
		refreshableView.setOnItemClickListener(listener);
	}

	public void setOnItemLongClickListener(OnItemLongClickListener listener) {
		refreshableView.setOnItemLongClickListener(listener);
	}

	public void setNumColumns(int numColumns) {
		refreshableView.setNumColumns(numColumns);
	}

	public void getNumColumns() {
		refreshableView.getNumColumns();
	}

	public void setHorizontalSpacing(int horizontalSpacing) {
		refreshableView.setHorizontalSpacing(horizontalSpacing);
	}

	public void setVerticalSpacing(int verticalSpacing) {
		refreshableView.setVerticalSpacing(verticalSpacing);
	}

	public void setDrawSelectorOnTop(boolean onTop) {
		refreshableView.setDrawSelectorOnTop(onTop);
	}

	public void setSelector(int resID) {
		refreshableView.setSelector(resID);
	}

	public void setOnTouchListener(OnTouchListener l) {
		refreshableView.setOnTouchListener(l);
	}

	public void setSelection(int position) {
		refreshableView.setSelection(position);
	}

	public void smoothScrollToPositionFromTop(int position, int offset) {
		refreshableView.smoothScrollToPositionFromTop(position, offset);
	}

	public void smoothScrollToPosition(int position) {
		refreshableView.smoothScrollToPosition(position);
	}

	public ListAdapter getAdapter() {
		return refreshableView.getAdapter();
	}
	
	public void setRefreshViewTopMargin(int lastHeight) {
		mMainParams = (FrameLayout.LayoutParams) refreshableView.getLayoutParams();
		mMainParams.gravity = Gravity.TOP;
		if (lastHeight == 0) {
			mMainParams.topMargin = mHeaderView.getHeight();
			mHeaderParams.topMargin = 0;
		} else {
			mMainParams.topMargin += mHeaderView.getHeight() - lastHeight;
			if (mMainParams.topMargin < 0) {
				mHeaderParams.topMargin = -mHeaderView.getHeight();
				mMainParams.topMargin = 0;
			} else if (mMainParams.topMargin > mHeaderView.getHeight()) {
				mHeaderParams.topMargin = 0;
				mMainParams.topMargin = mHeaderView.getHeight();
			}
		}
		mHeaderView.setLayoutParams(mHeaderParams);
		refreshableView.setLayoutParams(mMainParams);
	}

	public void addHeaderView(View headerView) {
		mHeaderView = headerView;
		
		mHeaderParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		mHeaderParams.gravity = Gravity.TOP;
		mHeaderView.setLayoutParams(mHeaderParams);
		((ViewGroup) refreshableView.getParent()).addView(mHeaderView);
	}
	
	@Override
	protected boolean isReadyForPullDown() {
		if (mHeaderView == null) {
			return super.isReadyForPullDown();
		} else {
			if (mHeaderViewState == HEADER_VIEW_TOP) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		
		if (mHeaderView == null) {
			return super.onInterceptTouchEvent(event);
		}
		
		int x = (int) event.getX();
		int y = (int) event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			mLastXIntercept = x;
			mLastYIntercept = y;
			mLastY = y;
			mHeaderViewOperation = OPERATION_NONE;
			if (mHeaderView.getTop() == 0) {
				mHeaderViewState = HEADER_VIEW_TOP;
			} else if (mHeaderView.getTop() > -mHeaderView.getHeight() && mHeaderView.getTop() < 0) {
				mHeaderViewState = HEADER_VIEW_CENTER;
			} else if (mHeaderView.getTop() == -mHeaderView.getHeight()) {
				mHeaderViewState = HEADER_VIEW_BOTTOM;
			}
			break;
		}
		case MotionEvent.ACTION_MOVE: {
			if (isHeaderMove(x, y)) {
				return true;
			}
			break;
		}
		case MotionEvent.ACTION_UP: {
			mLastXIntercept = mLastYIntercept = 0;
			break;
		}
		default:
			break;
		}

		return super.onInterceptTouchEvent(event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		if (mHeaderView == null) {
			return super.onTouchEvent(event);
		}
		
		int x = (int) event.getX();
		int y = (int) event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			return true;
			
		case MotionEvent.ACTION_MOVE: {
			if (mHeaderViewOperation != OPERATION_NONE || isHeaderMove(x, y)) {
				int deltaY = y - mLastY;
				moveHeaderView(deltaY);
				mLastY = y;
				return true;
			}
			break;
		}
		default:
			break;
		}
		mLastY = y;
		return super.onTouchEvent(event);
	}
	
	private boolean isHeaderMove(int x, int y) {
		int deltaX = x - mLastXIntercept;
		int deltaY = y - mLastYIntercept;
		if (Math.abs(deltaY) <= Math.abs(deltaX)) {
			return false;
		} else {
			if (deltaY <= -mTouchSlop && (mHeaderViewState == HEADER_VIEW_TOP || mHeaderViewState == HEADER_VIEW_CENTER)) {
				mHeaderViewOperation = OPERATION_UP;
				return true;
			} else if (deltaY >= mTouchSlop) {
				if (mHeaderViewState == HEADER_VIEW_CENTER || (mHeaderViewState == HEADER_VIEW_BOTTOM && refreshableView.getFirstVisiblePosition() == 0 && refreshableView.getChildAt(0).getTop() > refreshableView.getTop())) {
					mHeaderViewOperation = OPERATION_DOWN;
					return true;
				}
			}
		}
		return false;
	}
	
	public void moveHeaderView(int y) {
		
		y /= FRICTION;
		
		if (mHeaderParams == null) {
			mHeaderParams = (FrameLayout.LayoutParams) mHeaderView.getLayoutParams();
		}
		if (mMainParams == null) {
			mMainParams = (FrameLayout.LayoutParams) refreshableView.getLayoutParams();
		}
		
		mHeaderParams.topMargin += y;
		mMainParams.topMargin = mHeaderParams.topMargin + mHeaderView.getHeight();
		if (mMainParams.topMargin < 0) {
			mHeaderParams.topMargin = -mHeaderView.getHeight();
			mMainParams.topMargin = 0;
		} else if (mMainParams.topMargin > mHeaderView.getHeight()) {
			mHeaderParams.topMargin = 0;
			mMainParams.topMargin = mHeaderView.getHeight();
		}
		mHeaderView.setLayoutParams(mHeaderParams);
		refreshableView.setLayoutParams(mMainParams);
	}
	
	public void restoreTopLayoutParams(FrameLayout.LayoutParams[] params) {
		if (mHeaderView == null) {
			return;
		}
		mHeaderParams = params[0];
		mMainParams = params[1];
		mHeaderView.setLayoutParams(params[0]);
		refreshableView.setLayoutParams(params[1]);
	}
	
	public FrameLayout.LayoutParams[] saveTopLayoutParams() {
		if (mHeaderView == null) {
			return null;
		}
		return new FrameLayout.LayoutParams[] { mHeaderParams, mMainParams };
	}
}
