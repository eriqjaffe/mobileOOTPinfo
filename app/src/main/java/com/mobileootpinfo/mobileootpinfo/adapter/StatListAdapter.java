package com.mobileootpinfo.mobileootpinfo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mobileootpinfo.mobileootpinfo.R;
import com.mobileootpinfo.mobileootpinfo.model.StatLeader;
import com.mobileootpinfo.mobileootpinfo.util.DatabaseHandler;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eriqj on 3/9/2018.
 */

public class StatListAdapter extends ArrayAdapter<StatLeader> {

    private Context mContext;
    private List<StatLeader> statLeaders = new ArrayList<StatLeader>();
    private String html_root, league_name, league_abbr, bg_color, text_color;
    private int league_id, sub_league_id, textColor;
    private Filter filter;
    private DatabaseHandler db;

    public StatListAdapter(@NonNull Context context, List<StatLeader> list, String html, String league, int id, int sl_id, String bg, String txt) {
        super(context, 0, list);
        mContext = context;
        statLeaders = list;
        html_root = html;
        league_name = league;
        league_id = id;
        sub_league_id = sl_id;
        bg_color = bg;
        text_color = txt;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.stat_list_layout, parent, false);

        final StatLeader currentPlayer = statLeaders.get(position);

        TextView playerID = listItem.findViewById(R.id.playerID);
        playerID.setText(String.valueOf(currentPlayer.getPlayerID()));

        TextView player = listItem.findViewById(R.id.leaderName);
        player.setText(currentPlayer.getDisplayName());

        TextView stat = listItem.findViewById(R.id.stat);
        switch (currentPlayer.getStatistic()) {
            case "WAR":
                BigDecimal war = new BigDecimal(currentPlayer.getValue());
                stat.setText(String.valueOf(war.setScale(1, RoundingMode.HALF_UP)));
                break;
            case "OUTS":
                BigDecimal tmpIP = new BigDecimal(Float.toString(Float.valueOf(currentPlayer.getValue())/3));
                tmpIP = tmpIP.setScale(1, RoundingMode.HALF_UP);
                String ip = String.valueOf(tmpIP);
                ip = ip.replace(".3", ".1");
                ip = ip.replace(".7", ".2");
                stat.setText(ip);
                break;
            default:
                stat.setText(currentPlayer.getValue());
                break;
        }

        TextView hiddenStat = listItem.findViewById(R.id.hiddenStat);
        hiddenStat.setText(currentPlayer.getStatistic());

        ImageView playerPicture = listItem.findViewById(R.id.playerPicture);

        Glide.with(mContext)
                .load(html_root+mContext.getString(R.string.player_image, currentPlayer.getPlayerID()))
                .apply(new RequestOptions()
                        .error(R.drawable.person_silhouette)
                )
                .into(playerPicture);

        return listItem;
    }
}