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
import com.mobileootpinfo.mobileootpinfo.adapter.StatListAdapter;
import com.mobileootpinfo.mobileootpinfo.model.StatLeader;
import com.mobileootpinfo.mobileootpinfo.util.DatabaseHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mobileootpinfo.mobileootpinfo.util.ColorUtils.getContrastColor;

public class StatList extends Fragment {

    private ListView listView;
    private List<StatLeader> leaders = new ArrayList<StatLeader>();
    private View view;
    private StatListAdapter statListAdapter;
    private Context mContext;
    private String html_root, universe_name, bg_color, text_color, game_date, league_abbr, statistic, table, header_display_stat, team_abbr;
    private int league_id, sub_league_id, team_id, current_year;
    private String[] rateStats = {"avg","obp","slg","ops","babip","era","pct","bb9","k9","kbb","whip","oavg","oobp","oslg","oops","obabip"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View view = inflater.inflate(R.layout.fragment_stat_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles

        mContext = getActivity();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            html_root = bundle.getString("html_root");
            universe_name = bundle.getString("universe_name");
            league_id = bundle.getInt("league_id");
            sub_league_id = bundle.getInt("sub_league_id");
            team_id = bundle.getInt("team_id");
            bg_color = bundle.getString("bg_color");
            text_color = bundle.getString("text_color");
            league_abbr = bundle.getString("league_abbr");
            statistic = bundle.getString("statistic");
            current_year = bundle.getInt("current_year");
            game_date = bundle.getString("game_date");
            table = bundle.getString("table");
            team_id = bundle.getInt("team_id");
            team_abbr = (bundle.getString("team_abbr") != null) ? bundle.getString("team_abbr") : "";
        }

        ListView listView = view.findViewById(R.id.stat_list_view);

        DatabaseHandler db = new DatabaseHandler(getContext());
        if (Arrays.asList(rateStats).indexOf(statistic) > -1) {
            leaders = db.getRateStatList(universe_name, league_id, sub_league_id, current_year, statistic, team_id);
        } else {
            leaders = db.getCountingStatList(universe_name, league_id, sub_league_id, current_year, statistic, table, team_id);
        }
        statListAdapter = new StatListAdapter(getContext(), leaders, html_root, universe_name, league_id, sub_league_id, bg_color, text_color);
        listView.setAdapter(statListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                TextView playerID = view.findViewById(R.id.playerID);
                Bundle bundle = new Bundle();
                bundle.putInt("player_id", Integer.parseInt(playerID.getText().toString()));
                bundle.putString("html_root", html_root);
                bundle.putString("universe_name", universe_name);
                bundle.putString("game_date", game_date);
                bundle.putInt("league_id", league_id);
                bundle.putString("league_abbr", league_abbr);
                Fragment fragment = new PlayerCard();
                fragment.setArguments(bundle);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.hide(getActivity().getSupportFragmentManager().findFragmentByTag("stat_leader"));
                ft.add(R.id.currentFrame, fragment, "player_card");
                ft.addToBackStack(null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }
        });

        switch (statistic.toUpperCase()) {
            case "D":
                header_display_stat = "2B";
                break;
            case "T":
                header_display_stat = "3B";
                break;
            case "K":
                header_display_stat = "SO";
                break;
            case "HP":
                header_display_stat = "HBP";
                break;
            case "SH":
                header_display_stat = "SAC";
                break;
            case "OUTS":
                header_display_stat = "IP";
                break;
            case "S":
                header_display_stat = "SV";
                break;
            default:
                header_display_stat = statistic.toUpperCase();
                break;
        }

        if (team_id == -1) {
            ((NavDrawer) getActivity())
                    .formatActionBar(league_abbr + " " + header_display_stat + " Leaders", null,
                            Color.parseColor(bg_color), getContrastColor(Color.parseColor(bg_color)));
        } else {
            ((NavDrawer) getActivity())
                    .formatActionBar(team_abbr + " " + header_display_stat + " Leaders", null,
                            Color.parseColor(bg_color), Color.parseColor(text_color));
        }


    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (team_id == -1) {
                ((NavDrawer) getActivity())
                        .formatActionBar(league_abbr + " " + header_display_stat + " Leaders", null,
                                Color.parseColor(bg_color), getContrastColor(Color.parseColor(bg_color)));
            } else {
                ((NavDrawer) getActivity())
                        .formatActionBar(team_abbr + " " + header_display_stat + " Leaders", null,
                                Color.parseColor(bg_color), Color.parseColor(text_color));
            }
        }
    }
}


