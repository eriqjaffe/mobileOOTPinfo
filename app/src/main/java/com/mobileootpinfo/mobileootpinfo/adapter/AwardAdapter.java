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
import com.mobileootpinfo.mobileootpinfo.util.constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by eriqj on 2/14/2018.
 */

public class AwardAdapter extends ArrayAdapter<Award> {

    private Context mContext;
    private List<Award> awardList = new ArrayList<Award>();
    private String awardPrefix = "";
    private String awardPostscript = "";
    private String awardText;
    SimpleDateFormat sqliteSdf = new SimpleDateFormat("yyyy/M/d");
    DateFormat formatter = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());

    public AwardAdapter(@NonNull Context context, List<Award> list) {
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

        Calendar cal = Calendar.getInstance();
        cal.set(currentAward.getYear(), currentAward.getMonth(), currentAward.getDay());
        String awardDate = formatter.format(cal.getTime());

        TextView date = listItem.findViewById(R.id.awardDate);
        date.setText(awardDate+": ");

        if (currentAward.getAwardID() < 4) {
            awardText = "Named the "+currentAward.getSubLeague()+" "+currentAward.getAward()+".";
        } else if (currentAward.getAwardID() < 7) {
            awardText = "Won the "+currentAward.getSubLeague()+" "+currentAward.getAward()+".";
        } else if (currentAward.getAwardID() < 8) {
            awardText = "Won the "+currentAward.getSubLeague()+" "+currentAward.getAward()+" at "+ constants.positions[currentAward.getPosition()]+".";
        } else if (currentAward.getAwardID() == 9) {
            awardText = "Named to the "+currentAward.getSubLeague()+" All-Star Team.";
        } else if (currentAward.getAwardID() == 14) {
            awardPrefix = "Won the " + currentAward.getSeason() + " " + currentAward.getLeague() + " Championship.";
        } else if (currentAward.getAwardID() == 14) {
            awardText = "Named the "+currentAward.getSubLeague()+" "+currentAward.getAward()+".";
        }

        TextView id = listItem.findViewById(R.id.awardInfo);
        id.setText(awardText);


        return listItem;
    }
}
