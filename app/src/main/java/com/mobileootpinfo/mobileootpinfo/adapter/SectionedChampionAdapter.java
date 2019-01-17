package com.mobileootpinfo.mobileootpinfo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mobileootpinfo.mobileootpinfo.R;
import com.mobileootpinfo.mobileootpinfo.model.Champion;

import java.util.ArrayList;

public class SectionedChampionAdapter extends BaseAdapter {
    private ArrayList<Object> champions;
    private LayoutInflater inflater;
    private static final int TYPE_CHAMPION = 0;
    private static final int TYPE_DIVIDER = 1;
    private Context mContext;


    public SectionedChampionAdapter(Context context, ArrayList<Object> champions) {
        this.mContext = context;
        this.champions = champions;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return champions.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return champions.get(position);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) instanceof Champion) {
            return TYPE_CHAMPION;
        }
        return TYPE_DIVIDER;
    }

    @Override
    public boolean isEnabled(int position) {
        return (getItemViewType(position) == TYPE_CHAMPION);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case TYPE_CHAMPION:
                    convertView = inflater.inflate(R.layout.champion_layout, parent, false);
                    break;
                case TYPE_DIVIDER:
                    convertView = inflater.inflate(R.layout.layout_section, parent, false);
            }
        }

        switch (type) {
            case TYPE_CHAMPION:
                Champion champion = (Champion) getItem(position);

                TextView championName = convertView.findViewById(R.id.championName);
                championName.setText("Hello");
                break;

            case TYPE_DIVIDER:
                TextView sectionTitle = convertView.findViewById(R.id.sectionTitle);
                sectionTitle.setText(String.valueOf(getItem(position)));
                break;
        }
        return convertView;
    }
}
