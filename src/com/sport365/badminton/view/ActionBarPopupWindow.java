package com.sport365.badminton.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sport365.badminton.R;
import com.sport365.badminton.utils.ULog;

public class ActionBarPopupWindow extends PopupWindow {

    private Context mContext;
    private View contentView;
    private int popupWindowHeight, popupWindowWidth;
    private ListView lv_popup_window;
    private LayoutInflater mInflater;
    private ArrayList<PopupWindowItem> items = new ArrayList<PopupWindowItem>();
    private PopupWindowListAdapter mAdapter;


    public ActionBarPopupWindow(Context context, int popUpWindowWidth,
                                int popUpWindowHeight, ArrayList<PopupWindowItem> list) {
        super(context);
        this.mContext = context;
        this.popupWindowWidth = popUpWindowWidth;
        this.popupWindowHeight = popUpWindowHeight;
        this.items=list;
        initView();
        setPopupWindowProperties();
    }


    private void initView() {
        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = mInflater.inflate(R.layout.actionbar_popupwindow_layout,
                null);
        lv_popup_window = (ListView) contentView
                .findViewById(R.id.lv_popup_window);
        if (mAdapter == null) {
            mAdapter = new DefaultPopupWindowAdater(items);
        }
        lv_popup_window.setAdapter(mAdapter);
    }

    /**
     * 设置弹窗属性
     */
    private void setPopupWindowProperties() {
        // 设置SelectPicPopupWindow的View
        this.setContentView(contentView);
        // 设置SelectPicPopupWindow弹出窗体的宽、高
        this.setWidth(popupWindowWidth > 0 ? popupWindowWidth
                : ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(popupWindowHeight > 0 ? popupWindowHeight
                : ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(new BitmapDrawable());
    }

    /**
     * pop up window listView的adapter
     */
    public static abstract class PopupWindowListAdapter extends BaseAdapter {

        private ArrayList<PopupWindowItem> items = new ArrayList<PopupWindowItem>();

        public PopupWindowListAdapter(ArrayList<PopupWindowItem> datas) {
            this.items = datas;
        }

        @Override
        public int getCount() {
            return items == null ? 0 : items.size();
        }

        @Override
        public Object getItem(int position) {
            return items == null ? null : items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            PopupWindowItem item = items.get(position);
            return getView(item, convertView, parent, position);
        }

        public abstract View getView(PopupWindowItem itemData,
                                     View convertView, ViewGroup parent, int position);

    }

    /**
     * 默认的listView adapter
     */
    class DefaultPopupWindowAdater extends PopupWindowListAdapter {

        public DefaultPopupWindowAdater(ArrayList<PopupWindowItem> items) {
            super(items);
        }

        @Override
        public View getView(final PopupWindowItem item, View convertView,
                            ViewGroup parent, int position) {
            convertView = mInflater.inflate(
                    R.layout.list_item_actionbar_popupwindow, null);
            TextView tv = (TextView) convertView.findViewById(R.id.tv_title);
            tv.setText(item.text);
            ULog.error("------->"+item.text);
            tv.setOnClickListener(item.onClickListener);
            return convertView;
        }

    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        super.showAsDropDown(anchor, xoff, yoff);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    public ArrayList<PopupWindowItem> getItems() {
        return items;
    }


}
