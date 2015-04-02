// Copyright 2012 Square, Inc.

package com.sport365.badminton.view.calendar;

import android.text.TextUtils;

import java.util.Date;

/**
 * Describes the state of a particular date cell in a {@link MonthView}.
 */
class MonthCellDescriptor {
	public enum RangeState {
		NONE, FIRST, MIDDLE, LAST
	}

	private final Date date;
	private final int value;
	private final boolean isCurrentMonth;
	private boolean isSelected;
	private final boolean isToday;
	private final boolean isSelectable;
	private boolean isHighlighted;
	private RangeState rangeState;

	// 场次
	private String countNum;

	MonthCellDescriptor(Date date, boolean currentMonth, boolean selectable, boolean selected,
						boolean today, boolean highlighted, int value, RangeState rangeState, String countNum) {
		this.date = date;
		isCurrentMonth = currentMonth;
		isSelectable = selectable;
		isHighlighted = highlighted;
		isSelected = selected;
		isToday = today;
		this.value = value;
		this.rangeState = rangeState;
		this.countNum = countNum;
	}

	public Date getDate() {
		return date;
	}

	public boolean isCurrentMonth() {
		return isCurrentMonth;
	}

	public boolean isSelectable() {
		return isSelectable;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean selected) {
		isSelected = selected;
	}

	boolean isHighlighted() {
		return isHighlighted;
	}

	void setHighlighted(boolean highlighted) {
		isHighlighted = highlighted;
	}

	public boolean isToday() {
		return isToday;
	}

	public RangeState getRangeState() {
		return rangeState;
	}

	public void setRangeState(RangeState rangeState) {
		this.rangeState = rangeState;
	}

	public int getValue() {
		return value;
	}

	public String getCountNum() {
		if (TextUtils.isEmpty(countNum)) {
			return "0";
		}
		return countNum;
	}

	public void setCountNum(String countNum) {
		this.countNum = countNum;
	}


	@Override
	public String toString() {
		return "MonthCellDescriptor{"
				+ "date="
				+ date
				+ ", value="
				+ value
				+ ", isCurrentMonth="
				+ isCurrentMonth
				+ ", isSelected="
				+ isSelected
				+ ", isToday="
				+ isToday
				+ ", isSelectable="
				+ isSelectable
				+ ", isHighlighted="
				+ isHighlighted
				+ ", rangeState="
				+ rangeState
				+ '}';
	}
}
