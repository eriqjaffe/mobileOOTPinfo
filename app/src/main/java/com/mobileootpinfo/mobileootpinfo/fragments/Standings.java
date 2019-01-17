package com.mobileootpinfo.mobileootpinfo.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobileootpinfo.mobileootpinfo.R;
import com.mobileootpinfo.mobileootpinfo.activity.NavDrawer;
import com.mobileootpinfo.mobileootpinfo.model.Division;
import com.mobileootpinfo.mobileootpinfo.model.StandingsLine;
import com.mobileootpinfo.mobileootpinfo.model.SubLeague;
import com.mobileootpinfo.mobileootpinfo.util.CalligraphyDefaultTabLayout;
import com.mobileootpinfo.mobileootpinfo.util.DatabaseHandler;

import java.util.List;

import static android.graphics.Color.parseColor;
import static com.mobileootpinfo.mobileootpinfo.util.ColorUtils.getContrastColor;

public class Standings extends Fragment {

    private String html_root;
    private String league_date;
    private String game_date;
    private String bg_color;
    private String text_color;
    private String universe_name;
    private String league_abbr;
    private int league_id;
    private int wildcards;
    private DatabaseHandler db;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        rootView = inflater.inflate(R.layout.fragment_standings, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Standings");

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            html_root = bundle.getString("html_root");
            league_date = bundle.getString("game_date");
            game_date = bundle.getString("game_date");
            universe_name = bundle.getString("universe_name");
            league_id = bundle.getInt("league_id");
            bg_color = bundle.getString("bg_color");
            text_color = bundle.getString("text_color");
            wildcards = bundle.getInt("wildcards");
            league_abbr = bundle.getString("league_abbr");
        }

        if (wildcards == 0) {
            RelativeLayout header = view.findViewById(R.id.standingsNavigation);
            header.setVisibility(View.GONE);
        }

        db = new DatabaseHandler(getActivity());

        List<SubLeague> subLeagueList = db.getSubLeagues(league_id, universe_name);

        // populate divisional standing

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        LinearLayout container;
        TextView name;
        TextView w;
        TextView l;
        TextView pct;
        TextView gb;
        final View divisionalView;
        final View wildcardView;
        final Button divisionButton;
        final Button wildcardButton;

        divisionalView = view.findViewById(R.id.divisionalParent);
        wildcardView = view.findViewById(R.id.wildcardParent);

        // populate divisional standing
        container = view.findViewById(R.id.divisionalLayout);
        container.setOrientation(LinearLayout.VERTICAL);

        for (SubLeague s : subLeagueList) {
            View leagueHeader = inflater.inflate(R.layout.standings_line_league_header, container, false);
            leagueHeader.setBackgroundColor(parseColor(bg_color));

            name = leagueHeader.findViewById(R.id.teamName);
            name.setTextColor(getContrastColor(parseColor(bg_color)));
            name.setText(s.getName());

            w = leagueHeader.findViewById(R.id.w);
            w.setVisibility(View.INVISIBLE);

            l = leagueHeader.findViewById(R.id.l);
            l.setVisibility(View.INVISIBLE);

            pct = leagueHeader.findViewById(R.id.pct);
            pct.setVisibility(View.INVISIBLE);

            gb = leagueHeader.findViewById(R.id.gb);
            gb.setVisibility(View.INVISIBLE);

            container.addView(leagueHeader);
            List<Division> divisionList = db.getDivisions(league_id, s.getSubLeagueId(), universe_name);
            for (Division d : divisionList) {
                View divisionHeader = inflater.inflate(R.layout.standings_line_league_header, container, false);

                divisionHeader.setBackgroundColor(Color.LTGRAY);

                name = divisionHeader.findViewById(R.id.teamName);
                name.setText(d.getName());

                w = divisionHeader.findViewById(R.id.w);
                w.setText("W");

                l = divisionHeader.findViewById(R.id.l);
                l.setText("L");

                pct = divisionHeader.findViewById(R.id.pct);
                pct.setText("PCT");

                gb = divisionHeader.findViewById(R.id.gb);
                gb.setText("GB");

                container.addView(divisionHeader);

                List<StandingsLine> standingsLines = db.getDivisionStandings(league_id, s.getSubLeagueId(), d.getDivisionId(), universe_name);
                for (StandingsLine ln : standingsLines) {
                    View line = inflater.inflate(R.layout.standings_line, container, false);
                    name = line.findViewById(R.id.teamName);
                    String team = (ln.getMagicNumber() == 0) ? "*"+ln.getName() + " " + ln.getNickname() : ln.getName() + " " + ln.getNickname();
                    name.setText(team);

                    w = line.findViewById(R.id.w);
                    w.setText("" + ln.getWins());

                    l = line.findViewById(R.id.l);
                    l.setText("" + ln.getLosses());

                    pct = line.findViewById(R.id.pct);
                    pct.setText("" + ln.getPercentage());

                    gb = line.findViewById(R.id.gb);
                    //String bgDisp = (ln.getGamesBehind() == 0) ? "-" : ""+ln.getGamesBehind();
                    gb.setText((ln.getGamesBehind() == 0) ? "-" : ""+ln.getGamesBehind());

                    container.addView(line);
                }
            }
        }

        //populate wildcard standings
        container = view.findViewById(R.id.wildcardLayout);
        container.setOrientation(LinearLayout.VERTICAL);

        for (SubLeague s : subLeagueList) {
            View leagueHeader = inflater.inflate(R.layout.standings_line_league_header, container, false);
            leagueHeader.setBackgroundColor(parseColor(bg_color));

            name = leagueHeader.findViewById(R.id.teamName);
            name.setTextColor(getContrastColor(parseColor(bg_color)));
            name.setText(s.getName());

            w = leagueHeader.findViewById(R.id.w);
            w.setTextColor(getContrastColor(parseColor(bg_color)));
            w.setText("W");

            l = leagueHeader.findViewById(R.id.l);
            l.setTextColor(getContrastColor(parseColor(bg_color)));
            l.setText("L");

            pct = leagueHeader.findViewById(R.id.pct);
            pct.setTextColor(getContrastColor(parseColor(bg_color)));
            pct.setText("PCT");

            gb = leagueHeader.findViewById(R.id.gb);
            gb.setTextColor(getContrastColor(parseColor(bg_color)));
            gb.setText("GB");
            //gb.setText();

            container.addView(leagueHeader);

            int aWin = 0;
            int aLosses = 0;
            int teamCount = 1;

            List<StandingsLine> standingsLines = db.getWildCardStandings(league_id, s.getSubLeagueId(), universe_name);
            for (StandingsLine ln : standingsLines) {

                View line = inflater.inflate(R.layout.standings_line, container, false);
                name = line.findViewById(R.id.teamName);
                String team = (ln.getMagicNumber() == 0) ? "*"+ln.getName() + " " + ln.getNickname() : ln.getName() + " " + ln.getNickname();
                name.setText(team);

                w = line.findViewById(R.id.w);
                w.setText("" + ln.getWins());

                l = line.findViewById(R.id.l);
                l.setText("" + ln.getLosses());

                pct = line.findViewById(R.id.pct);
                pct.setText("" + ln.getPercentage());

                gb = line.findViewById(R.id.gb);

                if (teamCount == wildcards) {
                    aWin = ln.getWins();
                    aLosses = ln.getLosses();
                }
                gb.setText((teamCount <= wildcards) ? "-" : ""+gamesBehind(aWin, aLosses, ln.getWins(), ln.getLosses()));

                container.addView(line);

                teamCount++;
            }
        }

        CalligraphyDefaultTabLayout tabLayout = view.findViewById(R.id.tabLayout);

        tabLayout.setSelectedTabIndicatorColor(Color.parseColor(text_color));

        tabLayout.addTab(tabLayout.newTab().setText("Divisional"));
        tabLayout.addTab(tabLayout.newTab().setText("Wildcard"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        wildcardView.setVisibility(View.GONE);
                        divisionalView.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        wildcardView.setVisibility(View.VISIBLE);
                        divisionalView.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        ((NavDrawer) getActivity())
                .formatActionBar(league_abbr + " Standings", null,
                        Color.parseColor(bg_color), getContrastColor(Color.parseColor(bg_color)));
    }

    public double gamesBehind(int aWin, int aLoss, int bWin, int bLoss) {
        double tmp = (((double)aWin - (double)bWin) + ((double)bLoss - (double)aLoss)) /(double)2;
        return tmp;
        //return ((aWin - bWin) + (bLoss - aLoss)) / 2;
    }
}