package com.mobileootpinfo.mobileootpinfo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mobileootpinfo.mobileootpinfo.R;
import com.mobileootpinfo.mobileootpinfo.model.HistoricalAwardYear;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by eriqj on 3/12/2018.
 */

public class HistoricalAwardYearAdapter extends ArrayAdapter<HistoricalAwardYear> {

    private Context mContext;
    private List<HistoricalAwardYear> awardList = new ArrayList<HistoricalAwardYear>();
    private String awardPrefix = "";
    private String awardPostscript = "";
    private String awardText;
    SimpleDateFormat sqliteSdf = new SimpleDateFormat("yyyy/M/d");
    DateFormat formatter = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());

    public HistoricalAwardYearAdapter(@NonNull Context context, List<HistoricalAwardYear> list) {
        super(context, 0, list);
        mContext = context;
        awardList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.historical_award_list, parent, false);

        final HistoricalAwardYear year = awardList.get(position);

        TextView awardYear = listItem.findViewById(R.id.awardYear);
        awardYear.setText(String.valueOf(year.getYear()));

        TextView awardType = listItem.findViewById(R.id.awardType);
        awardType.setText(year.getAwardName());

        TextView awardRecipient = listItem.findViewById(R.id.awardRecipient);
        awardRecipient.setText(year.getHitterOfYear());


        return listItem;
    }
}
