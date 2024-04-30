package com.example.pentimento;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ToDel_GridViewAdapter extends BaseAdapter {

    private Context myContext;
    private String[] mItems;


    public ToDel_GridViewAdapter(Context context, String[] items) {
        this.myContext = context;
        this.mItems = items;
    }


    //TODO - set the right item to the functions
    @Override
    public int getCount() {
        return 0;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

}
