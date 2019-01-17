package com.mobileootpinfo.mobileootpinfo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mobileootpinfo.mobileootpinfo.R;
import com.mobileootpinfo.mobileootpinfo.model.Manager;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;

public class SectionedManagerAdapter extends BaseAdapter {
    private ArrayList<Object> managers;
    private LayoutInflater inflater;
    private static final int TYPE_PERSON = 0;
    private static final int TYPE_DIVIDER = 1;
    private Context mContext;

    public SectionedManagerAdapter(Context context, ArrayList<Object> managers) {
        this.mContext = context;
        this.managers = managers;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return managers.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return managers.get(position);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) instanceof Manager) {
            return TYPE_PERSON;
        }
        return TYPE_DIVIDER;
    }

    @Override
    public boolean isEnabled(int position) {
        return (getItemViewType(position) == TYPE_PERSON);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case TYPE_PERSON:
                    convertView = inflater.inflate(R.layout.manager_adapter_list_layout, parent, false);
                    break;
                case TYPE_DIVIDER:
                    convertView = inflater.inflate(R.layout.layout_section, parent, false);
            }
        }

        switch (type) {
            case TYPE_PERSON:
                Manager manager= (Manager)getItem(position);

                TextView id = convertView.findViewById(R.id.managerID);
                id.setText("" + manager.getHumanManagerID());

                TextView name = convertView.findViewById(R.id.managerName);
                name.setText(manager.getFirstName() + " " + manager.getLastName());
                break;
            case TYPE_DIVIDER:
                TextView sectionTitle = convertView.findViewById(R.id.sectionTitle);
                sectionTitle.setText((String)getItem(position));
                break;
        }
        return convertView;
    }

    private int age(String birthday, String today) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy/MM/dd");
        DateTime d1 = dateTimeFormatter.parseDateTime(birthday);
        DateTime now = dateTimeFormatter.parseDateTime(today);
        Period p = new Period(d1, now);
        return p.getYears();
    }
}
