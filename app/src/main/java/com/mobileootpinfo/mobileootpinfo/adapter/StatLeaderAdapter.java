package com.mobileootpinfo.mobileootpinfo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobileootpinfo.mobileootpinfo.R;
import com.mobileootpinfo.mobileootpinfo.model.StatLeader;
import com.mobileootpinfo.mobileootpinfo.util.DatabaseHandler;
import com.sdsmdg.harjot.vectormaster.VectorMasterDrawable;
import com.sdsmdg.harjot.vectormaster.models.PathModel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static com.mobileootpinfo.mobileootpinfo.util.ColorUtils.getContrastColor;

/**
 * Created by Eriq on 3/2/2018.
 */

public class StatLeaderAdapter extends ArrayAdapter<StatLeader> {

    private Context mContext;
    private List<StatLeader> statLeaders = new ArrayList<StatLeader>();
    private String html_root, league_name, league_abbr, bg_color, text_color;
    private int league_id, sub_league_id, textColor;
    private Filter filter;
    private DatabaseHandler db;

    public StatLeaderAdapter(@NonNull Context context, List<StatLeader> list, String html, String league, int id, int sl_id, String bg, String txt) {
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
            listItem = LayoutInflater.from(mContext).inflate(R.layout.stat_leaders_list_layout, parent, false);

        final StatLeader currentPlayer = statLeaders.get(position);

        //db = new DatabaseHandler(mContext);

        //LeagueInfoFragment leagueInfo = db.getSpecificLeague(league_name, html_root, league_id);

        if (Color.parseColor(text_color) == Color.parseColor(bg_color)) {
            textColor = getContrastColor(Color.parseColor(text_color));
        } else {
            textColor = Color.parseColor(text_color);
        }

        ImageView leagueLogo = listItem.findViewById(R.id.statLogo);
        VectorMasterDrawable baseball = new VectorMasterDrawable(mContext, R.drawable.ic_baseball_2);
        PathModel black = baseball.getPathModelByName("black");
        black.setFillColor(textColor);
        PathModel white = baseball.getPathModelByName("white");
        white.setFillColor(Color.parseColor(bg_color));
        leagueLogo.setImageDrawable(baseball);

        TextView statBall = listItem.findViewById(R.id.statText);
        switch (currentPlayer.getStatistic()) {
            case "D":
                statBall.setText("2B");
                break;
            case "T":
                statBall.setText("3B");
                break;
            case "K":
                statBall.setText("SO");
                break;
            case "HP":
                statBall.setText("HBP");
                break;
            case "SH":
                statBall.setText("SAC");
                break;
            case "OUTS":
                statBall.setText("IP");
                break;
            case "S":
                statBall.setText("SV");
                break;
            default:
                statBall.setText(currentPlayer.getStatistic());
                break;
        }
        statBall.setTextColor(getContrastColor(Color.parseColor(bg_color)));

        TextView player = listItem.findViewById(R.id.leaderName);
        player.setText(currentPlayer.getDisplayName());

        TextView playerID = listItem.findViewById(R.id.playerID);
        playerID.setText(String.valueOf(currentPlayer.getPlayerID()));

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

        return listItem;
    }
}
