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
import com.mobileootpinfo.mobileootpinfo.model.PitchingLineScore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by eriqj on 3/22/2018.
 */

public class PitchingLineAdapter extends ArrayAdapter<PitchingLineScore> {

    private Context mContext;
    private List<PitchingLineScore> pitchingLines = new ArrayList<PitchingLineScore>();
    private String awardPrefix = "";
    private String awardPostscript = "";
    private String awardText;
    private String html_root;
    SimpleDateFormat sqliteSdf = new SimpleDateFormat("yyyy/M/d");
    DateFormat formatter = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());

    public PitchingLineAdapter(@NonNull Context context, List<PitchingLineScore> list, String html) {
        super(context, 0, list);
        mContext = context;
        pitchingLines = list;
        html_root = html;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.pitching_line_layout, parent, false);

        final PitchingLineScore line = pitchingLines.get(position);

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
            displayName = line.getFirstName().substring(0, 1) + ". " + line.getLastName();
        }
        playerInfo.setText(displayName);

        TextView ip = listItem.findViewById(R.id.ip);
        ip.setText(line.getIp());

        TextView hits = listItem.findViewById(R.id.h);
        hits.setText(line.getH());

        TextView runs = listItem.findViewById(R.id.r);
        runs.setText(line.getR());

        TextView earnedRuns = listItem.findViewById(R.id.er);
        earnedRuns.setText(line.getEr());

        TextView walks = listItem.findViewById(R.id.walks);
        walks.setText(line.getBb());

        TextView strikeouts = listItem.findViewById(R.id.strikeouts);
        strikeouts.setText(line.getK());

        TextView hr = listItem.findViewById(R.id.hr);
        hr.setText(line.getH());

        TextView era = listItem.findViewById(R.id.era);
        era.setText("era");

        return listItem;
    }
}
