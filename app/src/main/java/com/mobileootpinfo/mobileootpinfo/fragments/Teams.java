package com.mobileootpinfo.mobileootpinfo.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.mobileootpinfo.mobileootpinfo.R;
import com.mobileootpinfo.mobileootpinfo.activity.NavDrawer;
import com.mobileootpinfo.mobileootpinfo.adapter.TeamListAdapter;
import com.mobileootpinfo.mobileootpinfo.model.Team;
import com.mobileootpinfo.mobileootpinfo.util.DatabaseHandler;

import java.util.List;

import static com.mobileootpinfo.mobileootpinfo.util.ColorUtils.getContrastColor;

public class Teams extends Fragment {

    private ListView listView;
    private List<Team> teams;
    private View view;
    private TeamListAdapter teamListAdapter;
    private Context mContext;
    private String html_root;
    private String universe_name;
    private int league_id;
    private int team_id;
    private String bg_color;
    private String game_date;
    private String league_abbr;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View view = inflater.inflate(R.layout.fragment_teams, container, false);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            html_root = bundle.getString("html_root");
            universe_name = bundle.getString("universe_name");
            league_id = bundle.getInt("league_id");
            team_id = bundle.getInt("team_id");
            bg_color = bundle.getString("bg_color");
            game_date = bundle.getString("game_date");
            league_abbr = bundle.getString("league_abbr");
        }

        ListView listView = view.findViewById(R.id.team_list_view);

        populateListView(getActivity(), listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                TextView teamId = view.findViewById(R.id.teamID);
                Bundle bundle = new Bundle();
                bundle.putInt("team_id", Integer.parseInt(teamId.getText().toString()));
                bundle.putString("html_root", html_root);
                bundle.putString("universe_name", universe_name);
                bundle.putInt("league_id", league_id);
                bundle.putString("game_date", game_date);
                bundle.putString("league_abbr", league_abbr);
                Fragment fragment = new com.mobileootpinfo.mobileootpinfo.fragments.TeamCard();
                fragment.setArguments(bundle);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.currentFrame, fragment, "team_card");
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        ((NavDrawer) getActivity())
                .formatActionBar(league_abbr + " Teams", null,
                        Color.parseColor(bg_color), getContrastColor(Color.parseColor(bg_color)));

    }

    public void populateListView(Context ctx, ListView listView) {
        //ListView listView = view.findViewById(R.id.league_list_view);
        DatabaseHandler db = new DatabaseHandler(ctx);
        teams = db.getAllTeams(universe_name, team_id, league_id);
        teamListAdapter = new TeamListAdapter(ctx, teams, html_root, universe_name);
        listView.setAdapter(teamListAdapter);
    }
}