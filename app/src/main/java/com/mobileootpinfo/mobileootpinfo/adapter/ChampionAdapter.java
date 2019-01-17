package com.mobileootpinfo.mobileootpinfo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mobileootpinfo.mobileootpinfo.R;
import com.mobileootpinfo.mobileootpinfo.model.Champion;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by eriqj on 3/13/2018.
 */

public class ChampionAdapter extends ArrayAdapter<Champion> {

    private Context mContext;
    private List<Champion> championList = new ArrayList<Champion>();
    private String awardPrefix = "";
    private String awardPostscript = "";
    private String awardText;
    private String html_root;
    SimpleDateFormat sqliteSdf = new SimpleDateFormat("yyyy/M/d");
    DateFormat formatter = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());

    public ChampionAdapter(@NonNull Context context, List<Champion> list, String html) {
        super(context, 0, list);
        mContext = context;
        championList = list;
        html_root = html;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.champion_layout, parent, false);

        final Champion champion = championList.get(position);

        TextView championYear = listItem.findViewById(R.id.championYear);
        championYear.setText(String.valueOf(champion.getYear()));

        TextView championName = listItem.findViewById(R.id.championName);
        championName.setText(champion.getName()+" "+champion.getNickname());

        String fName = champion.getFirstName() == null ? "" : champion.getFirstName();
        String lName = champion.getLastName() == null ? "" : champion.getLastName();
        TextView managerName = listItem.findViewById(R.id.managerName);
        managerName.setText(fName+" "+lName);

        TextView record = listItem.findViewById(R.id.record);
        record.setText(champion.getW()+"-"+champion.getL());

        TextView avg = listItem.findViewById(R.id.avg);
        avg.setText("AVG: "+champion.getAvg());

        TextView hr = listItem.findViewById(R.id.hr);
        hr.setText("HR:"+champion.getHr());

        TextView sb = listItem.findViewById(R.id.sb);
        sb.setText("SB: "+champion.getSb());

        TextView era = listItem.findViewById(R.id.era);
        era.setText("ERA: "+champion.getEra());

        TextView k = listItem.findViewById(R.id.k);
        k.setText("K: "+champion.getK());

        String team_logo = champion.getName().toLowerCase()+"_"+champion.getNickname().toLowerCase()+".png";
        team_logo = team_logo.replaceAll("\\s", "_");

        ImageView teamLogo = listItem.findViewById(R.id.teamLogo);
        Glide.with(mContext)
                .load(html_root + mContext.getString(R.string.team_logo, team_logo))
                .apply(new RequestOptions()
                        .error(R.drawable.ic_baseball_2)
                )
                .into(teamLogo);

        return listItem;
    }
}
