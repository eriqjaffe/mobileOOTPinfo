package com.mobileootpinfo.mobileootpinfo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mobileootpinfo.mobileootpinfo.R;

import java.util.ArrayList;

/**
 * Created by eriqj on 3/13/2018.
 */

public class SectionedAwardAdapter extends BaseAdapter {
    private ArrayList<Object> awards;
    private LayoutInflater inflater;
    private static final int TYPE_AWARD = 0;
    private static final int TYPE_DIVIDER = 1;
    private Context mContext;


    public SectionedAwardAdapter(Context context, ArrayList<Object> awards) {
        this.mContext = context;
        this.awards = awards;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return awards.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return awards.get(position);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) instanceof String) {
            return TYPE_AWARD;
        }
        return TYPE_DIVIDER;
    }

    @Override
    public boolean isEnabled(int position) {
        return (getItemViewType(position) == TYPE_AWARD);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case TYPE_AWARD:
                    convertView = inflater.inflate(R.layout.layout_plain_line, parent, false);
                    break;
                case TYPE_DIVIDER:
                    convertView = inflater.inflate(R.layout.layout_section, parent, false);
            }
        }

        switch (type) {
            case TYPE_AWARD:
                TextView line = convertView.findViewById(R.id.line);
                line.setText((String)getItem(position));
                break;

            case TYPE_DIVIDER:
                TextView sectionTitle = convertView.findViewById(R.id.sectionTitle);
                sectionTitle.setText(String.valueOf(getItem(position)));
                break;
        }
        return convertView;
    }
}
