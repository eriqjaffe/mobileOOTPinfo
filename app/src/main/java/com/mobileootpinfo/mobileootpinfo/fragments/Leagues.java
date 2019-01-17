package com.mobileootpinfo.mobileootpinfo.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mobileootpinfo.mobileootpinfo.R;
import com.mobileootpinfo.mobileootpinfo.activity.NavDrawer;
import com.mobileootpinfo.mobileootpinfo.adapter.LeagueListAdapter;
import com.mobileootpinfo.mobileootpinfo.model.LeagueInfo;
import com.mobileootpinfo.mobileootpinfo.util.DatabaseHandler;

import java.util.List;

import static com.mobileootpinfo.mobileootpinfo.util.ColorUtils.getContrastColor;

public class Leagues extends Fragment {

    private View view;
    private String html_root;
    private String universe_name;
    private int league_id;
    private int team_id;
    private String bg_color;
    private List<LeagueInfo> leagues;
    private LeagueListAdapter leagueListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        view = inflater.inflate(R.layout.fragment_leagues, container, false);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            html_root = bundle.getString("html_root");
            universe_name = bundle.getString("universe_name");
            league_id = bundle.getInt("league_id");
            team_id = bundle.getInt("team_id");
            bg_color = bundle.getString("bg_color");
        }

        ListView listView = view.findViewById(R.id.league_list_view);

        populateListView(getActivity(), listView);

        ((NavDrawer) getActivity())
                .formatActionBar(universe_name + " " +getString(R.string._leagues), null,
                        Color.parseColor(bg_color), getContrastColor(Color.parseColor(bg_color)));
    }

    public void populateListView(Context ctx, ListView listView) {
        //ListView listView = view.findViewById(R.id.league_list_view);
        DatabaseHandler db = new DatabaseHandler(ctx);
        leagues = db.getAllDBLeagues(universe_name, html_root);
        leagueListAdapter = new LeagueListAdapter(ctx, leagues, html_root, universe_name);
        listView.setAdapter(leagueListAdapter);
    }
}
