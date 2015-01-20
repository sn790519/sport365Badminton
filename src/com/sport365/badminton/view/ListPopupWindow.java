package com.sport365.badminton.view;

import android.content.Context;
import android.widget.PopupWindow;

import java.util.ArrayList;

/**
 * Created by vincent on 15/1/20.
 */
public class ListPopupWindow {

    private Context mContext;
   private ArrayList<PopupWindowItem> mList;


    public ListPopupWindow(Context context,ArrayList<PopupWindowItem> list)
    {
        this.mContext=context;
        this.mList=list;
    }




    private void initPopuptWindow() {



    }
}


