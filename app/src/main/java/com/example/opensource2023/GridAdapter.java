package com.example.opensource2023;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import java.util.List;
public class GridAdapter extends ArrayAdapter<DiaryBox> {
    private static final int LAYOUT_RESOURCE_ID = R.layout.diary_item;

    private Context mContext;
    private List<DiaryBox> mItemList;

    public GridAdapter(Context a_context, List<DiaryBox> a_itemList) {
        super(a_context, LAYOUT_RESOURCE_ID, a_itemList);

        mContext = a_context;
        mItemList = a_itemList;
    }

    public View getView(int a_position, View a_convertView, ViewGroup a_parent) {
        GridIHolder holder;
        if(a_convertView == null) {
            a_convertView = LayoutInflater.from(mContext).inflate(LAYOUT_RESOURCE_ID, a_parent, false);
            holder = new GridIHolder(a_convertView);
            a_convertView.setTag(holder);
        } else {
            holder = (GridIHolder) a_convertView.getTag();
        }

        final DiaryBox countryItem = mItemList.get(a_position);

        String month = countryItem.getMonth();
        if(month.length() == 1) {
            month = " " + countryItem.getMonth() + " ";
        }

        String day = countryItem.getDay();
        if(day.length() == 1) {
            day = "0" + countryItem.getDay();
        }

        String date = month + "/" + day;

        holder.date.setText(date);

        return a_convertView;
    }
}
