package com.mobileootpinfo.mobileootpinfo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobileootpinfo.mobileootpinfo.R;
import com.mobileootpinfo.mobileootpinfo.model.HistoricalLeader;
import com.mobileootpinfo.mobileootpinfo.util.NumberToWordsConverter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;

import static android.view.View.GONE;

/**
 * Created by eriqj on 3/15/2018.
 */

public class SectionedLeaderboardAdapter extends BaseAdapter {
    private ArrayList<Object> leaders;
    private LayoutInflater inflater;
    private static final int TYPE_LEADER = 0;
    private static final int TYPE_DIVIDER = 1;
    private Context mContext;
    private int subLeagues;
    private Integer[] oneDecimal = {58, 59, 22, 34, 45, 46, 47, 48};
    private Integer[] twoDecimal = {40, 42};
    private Integer[] threeDecimal = {18, 19, 20, 23, 25, 31, 43, 44};
    private String displayName;
    private String displayStat;


    public SectionedLeaderboardAdapter(Context context, ArrayList<Object> leaders, int subLeagues) {
        this.mContext = context;
        this.leaders = leaders;
        this.subLeagues = subLeagues;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return leaders.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return leaders.get(position);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) instanceof String) {
            return TYPE_DIVIDER;
        }
        return TYPE_LEADER;
    }

    @Override
    public boolean isEnabled(int position) {
        return (getItemViewType(position) == TYPE_LEADER);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case TYPE_LEADER:
                    //if (subLeagues == 1) {
                        convertView = inflater.inflate(R.layout.stat_leaders_list_layout, parent, false);
                    //} else {
                        //convertView = inflater.inflate(R.layout.stat_leaders_list_multiple_subleagues_layout, parent, false);
                    //}
                    break;
                case TYPE_DIVIDER:
                    convertView = inflater.inflate(R.layout.layout_section, parent, false);
            }
        }

        switch (type) {
            case TYPE_LEADER:
                //ArrayList<HistoricalLeader> arrayList = (ArrayList) getItem(position);
                HistoricalLeader hl = (HistoricalLeader) getItem(position);

                RelativeLayout logoParent = convertView.findViewById(R.id.logoParent);
                logoParent.setVisibility(GONE);

                if (hl.getSubLeague() != null) {
                    if (android.text.TextUtils.isDigitsOnly(hl.getFirstName())) {
                        displayName = hl.getSubLeague()+": "+NumberToWordsConverter.convert(Integer.parseInt(hl.getFirstName()))+" "+hl.getLastName();
                    } else {
                        displayName = hl.getSubLeague()+": "+hl.getFirstName()+" "+hl.getLastName()+", "+hl.getAbbr();
                    }
                } else {
                    displayName = "";
                }

                TextView leaderName = convertView.findViewById(R.id.leaderName);
                leaderName.setText(displayName);

                displayStat = hl.getAmount() != null ? hl.getAmount() : "";
                TextView stat = convertView.findViewById(R.id.stat);
                stat.setText(formattedStat(displayStat, hl.getCategory()));

                TextView playerID = convertView.findViewById(R.id.playerID);
                playerID.setText(hl.getPlayerID());
                break;

            case TYPE_DIVIDER:
                TextView sectionTitle = convertView.findViewById(R.id.sectionTitle);
                sectionTitle.setText(String.valueOf(getItem(position)).toUpperCase());
                break;
        }
        return convertView;
    }

    private String formattedStat(String stat, int category) {
        Float tmpStat = Float.valueOf(stat);
        if (Arrays.asList(oneDecimal).contains(category)) {
            BigDecimal tmp = new BigDecimal(Float.toString(tmpStat));
            tmp = tmp.setScale(1, RoundingMode.HALF_UP);
            return(String.valueOf(tmp));
        } else if (Arrays.asList(twoDecimal).contains(category)) {
            BigDecimal tmp = new BigDecimal(Float.toString(tmpStat));
            tmp = tmp.setScale(2, RoundingMode.HALF_UP);
            return(String.valueOf(tmp));
        } else if (Arrays.asList(threeDecimal).contains(category)) {
            BigDecimal tmp = new BigDecimal(Float.toString(tmpStat));
            tmp = tmp.setScale(3, RoundingMode.HALF_UP);
            return(String.valueOf(tmp).replaceFirst("^0.", "."));
        } else {
            return stat;
        }
    }
}
