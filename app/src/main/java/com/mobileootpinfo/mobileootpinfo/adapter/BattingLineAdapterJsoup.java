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
import com.mobileootpinfo.mobileootpinfo.model.BattingLineScore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by eriqj on 3/22/2018.
 */

public class BattingLineAdapterJsoup extends ArrayAdapter<BattingLineScore> {

    private Context mContext;
    private List<BattingLineScore> battingLines = new ArrayList<BattingLineScore>();
    private String awardPrefix = "";
    private String awardPostscript = "";
    private String awardText;
    private String html_root;
    SimpleDateFormat sqliteSdf = new SimpleDateFormat("yyyy/M/d");
    DateFormat formatter = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());

    public BattingLineAdapterJsoup(@NonNull Context context, List<BattingLineScore> list, String html) {
        super(context, 0, list);
        mContext = context;
        battingLines = list;
        html_root = html;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.batting_line_layout, parent, false);

        final BattingLineScore line = battingLines.get(position);

        TextView playerInfo = listItem.findViewById(R.id.playerInfo);

        String displayName;

        if (line.getFirstName().equals("NAME") || line.getFirstName().equals("TOTAL")) {
            listItem.setBackgroundColor(mContext.getResources().getColor(R.color.retired));
        } else {
            if (position % 2 == 0) {
                listItem.setBackgroundColor(mContext.getResources().getColor(R.color.awayGame));
            }
        }

        if (line.getFirstName().equals("NAME") || line.getFirstName().equals("TOTAL")) {
            displayName = line.getFirstName();
        } else {
            if (Integer.parseInt(line.getPinch()) == 0) {
                displayName = line.getFirstName().substring(0, 1) + ". " + line.getLastName() + ", " + line.getPosition();
            } else {
                if (Integer.parseInt(line.getPa()) > 0) {
                    displayName = "\t" + line.getFirstName().substring(0, 1) + ". " + line.getLastName() + ", PH";
                } else {
                    displayName = "\t" + line.getFirstName().substring(0, 1) + ". " + line.getLastName() + ", PR";
                }
            }
        }
        playerInfo.setText(displayName);

        TextView atBats = listItem.findViewById(R.id.atBats);
        atBats.setText(line.getAb());

        TextView runs = listItem.findViewById(R.id.runs);
        runs.setText(line.getR());

        TextView hits = listItem.findViewById(R.id.hits);
        hits.setText(line.getH());

        TextView rbi = listItem.findViewById(R.id.rbi);
        rbi.setText(line.getRbi());

        TextView walks = listItem.findViewById(R.id.walks);
        walks.setText(line.getBb());

        TextView strikeouts = listItem.findViewById(R.id.strikeouts);
        strikeouts.setText(line.getK());

        return listItem;
    }
}