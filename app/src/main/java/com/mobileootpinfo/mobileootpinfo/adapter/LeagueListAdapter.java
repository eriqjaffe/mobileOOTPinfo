package com.mobileootpinfo.mobileootpinfo.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.mobileootpinfo.mobileootpinfo.activity.NavDrawer;
import com.mobileootpinfo.mobileootpinfo.model.LeagueInfo;
import com.mobileootpinfo.mobileootpinfo.util.constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eriq on 2/24/2018.
 */

public class LeagueListAdapter extends ArrayAdapter<LeagueInfo> {

    private Context mContext;
    private List<LeagueInfo> leagueList = new ArrayList<LeagueInfo>();
    private String html_root;
    private String league_name;
    private Filter filter;

    public LeagueListAdapter(@NonNull Context context, List<LeagueInfo> list, String html, String league) {
        super(context, 0, list);
        mContext = context;
        leagueList = list;
        html_root = html;
        league_name = league;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.league_adapter_list_layout, parent, false);

        final LeagueInfo currentLeague = leagueList.get(position);

        TextView id = listItem.findViewById(R.id.leagueID);
        id.setText(""+currentLeague.getId());

        TextView universe = listItem.findViewById(R.id.universeID);
        universe.setText(""+currentLeague.getUniverseID());

        TextView name = listItem.findViewById(R.id.leagueName);
        name.setText(currentLeague.getName());

        TextView level = listItem.findViewById(R.id.leagueLevel);
        level.setText(constants.league_level[currentLeague.getLeagueLevel()]);

        ImageView leagueLogo = listItem.findViewById(R.id.leagueLogo);
        Glide.with(mContext)
                .load(html_root + mContext.getString(R.string.news_html_league_logos, currentLeague.getLogo()))
                .apply(new RequestOptions()
                        .error(R.drawable.ic_baseball)
                )
                .into(leagueLogo);

        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("new_league", currentLeague.getName());
                bundle.putString("html_root", currentLeague.getHtml_root());
                bundle.putString("league_name", currentLeague.getName());
                bundle.putInt("league_id", currentLeague.getId());
                bundle.putInt("universe_id", currentLeague.getUniverseID());
                Intent intent = new Intent(mContext, NavDrawer.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

        return listItem;
    }
}
