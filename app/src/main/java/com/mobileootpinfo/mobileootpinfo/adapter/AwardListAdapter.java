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
import com.mobileootpinfo.mobileootpinfo.model.Award;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by eriqj on 3/12/2018.
 */

public class AwardListAdapter extends ArrayAdapter<Award> {

    private Context mContext;
    private List<Award> awardList = new ArrayList<Award>();
    private String awardPrefix = "";
    private String awardPostscript = "";
    private String awardText;
    SimpleDateFormat sqliteSdf = new SimpleDateFormat("yyyy/M/d");
    DateFormat formatter = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());

    public AwardListAdapter(@NonNull Context context, List<Award> list) {
        super(context, 0, list);
        mContext = context;
        awardList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.award_list_layout, parent, false);

        final Award currentAward = awardList.get(position);

        TextView date = listItem.findViewById(R.id.awardDate);
        date.setText(String.valueOf(currentAward.getYear()));

        TextView id = listItem.findViewById(R.id.awardInfo);
        id.setText(currentAward.getSubLeague()+": "+currentAward.getRecipient());


        return listItem;
    }
}
