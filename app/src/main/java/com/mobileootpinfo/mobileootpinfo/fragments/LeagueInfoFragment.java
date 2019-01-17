package com.mobileootpinfo.mobileootpinfo.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobileootpinfo.mobileootpinfo.R;
import com.mobileootpinfo.mobileootpinfo.activity.NavDrawer;
import com.mobileootpinfo.mobileootpinfo.adapter.ChampionAdapter;
import com.mobileootpinfo.mobileootpinfo.adapter.SectionedAwardAdapter;
import com.mobileootpinfo.mobileootpinfo.adapter.SectionedLeaderboardAdapter;
import com.mobileootpinfo.mobileootpinfo.adapter.SectionedManagerAdapter;
import com.mobileootpinfo.mobileootpinfo.model.Champion;
import com.mobileootpinfo.mobileootpinfo.model.HistoricalLeader;
import com.mobileootpinfo.mobileootpinfo.model.Manager;
import com.mobileootpinfo.mobileootpinfo.util.CalligraphyDefaultTabLayout;
import com.mobileootpinfo.mobileootpinfo.util.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.greenrobot.common.ListMap;

import static com.mobileootpinfo.mobileootpinfo.util.ColorUtils.getContrastColor;
import static com.mobileootpinfo.mobileootpinfo.util.constants.battingLeaderStats;
import static com.mobileootpinfo.mobileootpinfo.util.constants.pitchingLeaderStats;

/**
 * Created by eriqj on 3/9/2018.
 */

public class LeagueInfoFragment extends Fragment {

    private View view, leagueAwardsHistory;
    private String html_root, universe_name, game_date, league_abbr;
    private int league_id;
    private int team_id;
    private String bg_color;
    private Context mContext;
    private DatabaseHandler db;
    private List<Integer> years, leaderboardYears;
    private ListView mvp, cya, roy, championsLV, managersLV, battingLeaders, pitchingLeaders;
    private CalligraphyDefaultTabLayout awardsTabLayout, leadersTabLayout;
    private RelativeLayout owners, champions, awards, ballpark;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        view = inflater.inflate(R.layout.fragment_league_info, container, false);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = getContext();
        db = new DatabaseHandler(mContext);

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

        CalligraphyDefaultTabLayout tabLayout = view.findViewById(R.id.leagueTabs);
        tabLayout.addTab(tabLayout.newTab().setText("Owners"));
        tabLayout.addTab(tabLayout.newTab().setText("Champions"));
        tabLayout.addTab(tabLayout.newTab().setText("Awards"));
        tabLayout.addTab(tabLayout.newTab().setText("Leaders"));
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        owners = view.findViewById(R.id.ownersLayout);
        champions = view.findViewById(R.id.championsLayout);
        awards = view.findViewById(R.id.awardsLayout);
        ballpark = view.findViewById(R.id.ballparkLayout);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        owners.setVisibility(View.VISIBLE);
                        champions.setVisibility(View.GONE);
                        awards.setVisibility(View.GONE);
                        ballpark.setVisibility(View.GONE);
                        break;
                    case 1:
                        owners.setVisibility(View.GONE);
                        champions.setVisibility(View.VISIBLE);
                        awards.setVisibility(View.GONE);
                        ballpark.setVisibility(View.GONE);
                        break;
                    case 2:
                        owners.setVisibility(View.GONE);
                        champions.setVisibility(View.GONE);
                        awards.setVisibility(View.VISIBLE);
                        ballpark.setVisibility(View.GONE);
                        break;
                    case 3:
                        owners.setVisibility(View.GONE);
                        champions.setVisibility(View.GONE);
                        awards.setVisibility(View.GONE);
                        ballpark.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        Map<String, String> awards = db.getAwardNames(universe_name, league_id);

        awardsTabLayout = view.findViewById(R.id.awardTabs);
        awardsTabLayout.addTab(awardsTabLayout.newTab().setText(String.valueOf(awards.get("mvp"))));
        awardsTabLayout.addTab(awardsTabLayout.newTab().setText(String.valueOf(awards.get("poy"))));
        awardsTabLayout.addTab(awardsTabLayout.newTab().setText(String.valueOf(awards.get("roy"))));

        mvp = view.findViewById(R.id.mvpListView);
        cya = view.findViewById(R.id.cyaListView);
        roy = view.findViewById(R.id.royListView);
        championsLV = view.findViewById(R.id.championsListView);
        managersLV = view.findViewById(R.id.managersListView);
        battingLeaders = view.findViewById(R.id.batting_leaderboard_view);
        pitchingLeaders = view.findViewById(R.id.pitching_leaderboard_view);

        years = db.getAllYears(universe_name, league_id);
        leaderboardYears = db.getLeaderboardYears(universe_name, league_id);

        populateMVP(universe_name, league_id, years);
        populateCYA(universe_name, league_id, years);
        populateROY(universe_name, league_id, years);
        populateChampions(universe_name, league_id, years);
        populateManagers(universe_name, league_id);

        if (leaderboardYears.size() > 0) {
            final TextView displayedYear = view.findViewById(R.id.displayedYear);
            displayedYear.setText(String.valueOf(leaderboardYears.get(0)));
            populateLeaderboard(universe_name, league_id, leaderboardYears.get(0));

            final ImageButton yearBack = view.findViewById(R.id.yearBack);
            final ImageButton yearForward = view.findViewById(R.id.yearForward);

            yearBack.setEnabled(false);

            displayedYear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LayoutInflater li = LayoutInflater.from(view.getContext());
                    final View promptsView = li.inflate(R.layout.year_picker, null);
                    final NumberPicker np = promptsView.findViewById(R.id.numberPicker);
                    np.setMinValue(leaderboardYears.get(0));
                    np.setMaxValue(leaderboardYears.get(leaderboardYears.size() - 1));
                    np.setValue(Integer.parseInt(displayedYear.getText().toString()));
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                    builder.setView(promptsView);
                    builder
                            .setCancelable(true)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            displayedYear.setText(String.valueOf(np.getValue()));
                                            populateLeaderboard(universe_name, league_id, np.getValue());
                                        }
                                    })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            dialog.cancel();
                                        }
                                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });

            yearBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int year = Integer.parseInt(displayedYear.getText().toString());
                    year--;

                    displayedYear.setText(String.valueOf(year));

                    if (year > leaderboardYears.get(0)) {
                        yearBack.setEnabled(true);
                    } else {
                        yearBack.setEnabled(false);
                    }

                    populateLeaderboard(universe_name, league_id, year);

                }
            });

            yearForward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int year = Integer.parseInt(displayedYear.getText().toString());
                    year++;

                    displayedYear.setText(String.valueOf(year));

                    if (year > leaderboardYears.get(0)) {
                        yearBack.setEnabled(true);
                    } else {
                        yearBack.setEnabled(false);
                    }

                    if (year < leaderboardYears.get(leaderboardYears.size() - 1)) {
                        yearForward.setEnabled(true);
                    } else {
                        yearForward.setEnabled(false);
                    }

                    populateLeaderboard(universe_name, league_id, year);

                }
            });
        } else {
            tabLayout.removeTabAt(3);
        }

        awardsTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        mvp.setVisibility(View.VISIBLE);
                        cya.setVisibility(View.GONE);
                        roy.setVisibility(View.GONE);
                        break;
                    case 1:
                        mvp.setVisibility(View.GONE);
                        cya.setVisibility(View.VISIBLE);
                        roy.setVisibility(View.GONE);
                        break;
                    case 2:
                        mvp.setVisibility(View.GONE);
                        cya.setVisibility(View.GONE);
                        roy.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        leadersTabLayout = view.findViewById(R.id.leaderTabs);
        leadersTabLayout.addTab(leadersTabLayout.newTab().setText(getString(R.string.batting).toUpperCase()));
        leadersTabLayout.addTab(leadersTabLayout.newTab().setText(getString(R.string.pitching).toUpperCase()));

        leadersTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        battingLeaders.setVisibility(View.VISIBLE);
                        pitchingLeaders.setVisibility(View.GONE);
                        break;
                    case 1:
                        battingLeaders.setVisibility(View.GONE);
                        pitchingLeaders.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        ((NavDrawer) getActivity())
                .formatActionBar(universe_name + " " + getString(R.string._league_info), null,
                        Color.parseColor(bg_color), getContrastColor(Color.parseColor(bg_color)));
    }

    private void populateMVP(String universe_name, int league_id, List<Integer> years) {
        ListMap<Integer, String> awardList = db.getMVPHistory(universe_name, league_id);
        ArrayList<Object> awardAL = new ArrayList<>();

        for (Integer year : years) {
            if (awardList.get(year) != null) {
                awardAL.add(year);
                List<String> tmp = awardList.get(year);
                for (String thing : tmp) {
                    awardAL.add(thing);
                }
            }
        }
        mvp.setAdapter(new SectionedAwardAdapter(mContext, awardAL));
    }

    private void populateCYA(String universe_name, int league_id, List<Integer> years) {
        ListMap<Integer, String> awardList = db.getCYAHistory(universe_name, league_id);
        ArrayList<Object> awardAL = new ArrayList<>();

        for (Integer year : years) {
            if (awardList.get(year) != null) {
                awardAL.add(year);
                List<String> tmp = awardList.get(year);
                for (String thing : tmp) {
                    awardAL.add(thing);
                }
            }
        }
        cya.setAdapter(new SectionedAwardAdapter(mContext, awardAL));
    }

    private void populateROY(String universe_name, int league_id, List<Integer> years) {
        ListMap<Integer, String> awardList = db.getROYHistory(universe_name, league_id);
        ArrayList<Object> awardAL = new ArrayList<>();
        if (awardList.size() < 1) {
            awardsTabLayout.removeTab(awardsTabLayout.getTabAt(2));
        } else {
            for (Integer year : years) {
                if (awardList.get(year) != null) {
                    awardAL.add(year);
                    List<String> tmp = awardList.get(year);
                    for (String thing : tmp) {
                        awardAL.add(thing);
                    }
                }
            }
            roy.setAdapter(new SectionedAwardAdapter(mContext, awardAL));
        }
    }

    private void populateChampions(String universe_name, int league_id, List<Integer> years) {
        List<Champion> champions = db.getChampions(universe_name, league_id);
        ArrayList<Object> championAL = new ArrayList<>();
        championsLV.setAdapter(new ChampionAdapter(mContext, champions, html_root));
    }

    private void populateManagers(String universe_name, int league_id) {
        final String universe_name1 = universe_name;
        final int league_id1 = league_id;
        ArrayList<Object> managers = new ArrayList<>();
        List<Manager> activeManagers = db.getManagers(universe_name, league_id, 0);
        List<Manager> retiredManagers = db.getManagers(universe_name, league_id, 1);
        managers.add(getString(R.string.active_managers));
        for (Manager m : activeManagers) {
            managers.add(m);
        }
        managers.add(getString(R.string.retired_managers));
        for (Manager m : retiredManagers) {
            managers.add(m);
        }
        managersLV.setAdapter(new SectionedManagerAdapter(mContext, managers));

        managersLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                TextView managerID = view.findViewById(R.id.managerID);
                Bundle bundle = new Bundle();
                bundle.putInt("manager_id", Integer.parseInt(managerID.getText().toString()));
                bundle.putString("html_root", html_root);
                bundle.putString("universe_name", universe_name1);
                bundle.putInt("league_id", league_id1);
                bundle.putString("bg_color", bg_color);
                Fragment fragment = new ManagerCard();
                fragment.setArguments(bundle);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.hide(getActivity().getSupportFragmentManager().findFragmentByTag("nav_info"));
                ft.add(R.id.currentFrame, fragment, "manager_card");
                ft.addToBackStack(null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }
        });
    }

    private void populateLeaderboard(final String universe_name, final int league_id, int year) {
        int subLeagues = db.getSubLeagueCount(universe_name, league_id);
        ListMap<Integer, HistoricalLeader> battingAwardList = db.getHistoricalLeaderboard(universe_name, league_id, year, "batting");
        ArrayList<Object> battingLeaders = new ArrayList<Object>();
        ListMap<Integer, HistoricalLeader> pitchingAwardList = db.getHistoricalLeaderboard(universe_name, league_id, year, "pitching");
        ArrayList<Object> pitchingLeaders = new ArrayList<Object>();

        for (Map.Entry<String, Integer> entry : battingLeaderStats.entrySet()) {
            if (battingAwardList.get(entry.getValue()) != null) {
                battingLeaders.add(entry.getKey());
                List<HistoricalLeader> tmp = battingAwardList.get(entry.getValue());
                List<HistoricalLeader> sl0 = new ArrayList<HistoricalLeader>();
                List<HistoricalLeader> sl1 = new ArrayList<HistoricalLeader>();

                for (HistoricalLeader hl : tmp) {
                    if (hl.getSubLeagueID() == 0) {
                        sl0.add(hl);
                    }
                    if (hl.getSubLeagueID() == 1) {
                        sl1.add(hl);
                    }
                }

                if (sl0.size() == 1) {
                    HistoricalLeader output = new HistoricalLeader();
                    HistoricalLeader input = sl0.get(0);
                    output.setAbbr(input.getAbbr());
                    output.setAmount(input.getAmount());
                    output.setCategory(input.getCategory());
                    output.setFirstName(input.getFirstName());
                    output.setLastName(input.getLastName());
                    output.setPlace(input.getPlace());
                    output.setPlayerID(input.getPlayerID());
                    output.setSubLeague(input.getSubLeague());
                    output.setSubLeagueID(input.getSubLeagueID());
                    output.setYear(input.getYear());
                    battingLeaders.add(output);
                } else  if (sl0.size() > 1) {
                    HistoricalLeader output = new HistoricalLeader();
                    HistoricalLeader input = sl0.get(0);
                    output.setAbbr("");
                    output.setAmount(input.getAmount());
                    output.setCategory(input.getCategory());
                    output.setFirstName(String.valueOf(sl0.size()));
                    output.setLastName("Players");
                    output.setPlace(input.getPlace());
                    output.setPlayerID("-1");
                    output.setSubLeague(input.getSubLeague());
                    output.setSubLeagueID(input.getSubLeagueID());
                    output.setYear(input.getYear());
                    battingLeaders.add(output);
                }

                if (sl1.size() == 1) {
                    HistoricalLeader output = new HistoricalLeader();
                    HistoricalLeader input = sl1.get(0);
                    output.setAbbr(input.getAbbr());
                    output.setAmount(input.getAmount());
                    output.setCategory(input.getCategory());
                    output.setFirstName(input.getFirstName());
                    output.setLastName(input.getLastName());
                    output.setPlace(input.getPlace());
                    output.setPlayerID(input.getPlayerID());
                    output.setSubLeague(input.getSubLeague());
                    output.setSubLeagueID(input.getSubLeagueID());
                    output.setYear(input.getYear());
                    battingLeaders.add(output); 
                } else  if (sl1.size() > 1) {
                    HistoricalLeader output = new HistoricalLeader();
                    HistoricalLeader input = sl1.get(0);
                    output.setAbbr("");
                    output.setAmount(input.getAmount());
                    output.setCategory(input.getCategory());
                    output.setFirstName(String.valueOf(sl1.size()));
                    output.setLastName("Players");
                    output.setPlace(input.getPlace());
                    output.setPlayerID("-1");
                    output.setSubLeague(input.getSubLeague());
                    output.setSubLeagueID(input.getSubLeagueID());
                    output.setYear(input.getYear());
                    battingLeaders.add(output);
                }
            }
        }
        
        ListView battingLV = view.findViewById(R.id.batting_leaderboard_view);
        battingLV.setAdapter(new SectionedLeaderboardAdapter(mContext, battingLeaders, subLeagues));

        for (Map.Entry<String, Integer> entry : pitchingLeaderStats.entrySet()) {
            if (pitchingAwardList.get(entry.getValue()) != null) {
                pitchingLeaders.add(entry.getKey());
                List<HistoricalLeader> tmp = pitchingAwardList.get(entry.getValue());
                List<HistoricalLeader> sl0 = new ArrayList<HistoricalLeader>();
                List<HistoricalLeader> sl1 = new ArrayList<HistoricalLeader>();
                for (HistoricalLeader hl : tmp) {
                    if (hl.getSubLeagueID() == 0) {
                        sl0.add(hl);
                    }
                    if (hl.getSubLeagueID() == 1) {
                        sl1.add(hl);
                    }
                }

                if (sl0.size() == 1) {
                    HistoricalLeader output = new HistoricalLeader();
                    HistoricalLeader input = sl0.get(0);
                    output.setAbbr(input.getAbbr());
                    output.setAmount(input.getAmount());
                    output.setCategory(input.getCategory());
                    output.setFirstName(input.getFirstName());
                    output.setLastName(input.getLastName());
                    output.setPlace(input.getPlace());
                    output.setPlayerID(input.getPlayerID());
                    output.setSubLeague(input.getSubLeague());
                    output.setSubLeagueID(input.getSubLeagueID());
                    output.setYear(input.getYear());
                    pitchingLeaders.add(output);
                } else  if (sl0.size() > 1) {
                    HistoricalLeader output = new HistoricalLeader();
                    HistoricalLeader input = sl0.get(0);
                    output.setAbbr("");
                    output.setAmount(input.getAmount());
                    output.setCategory(input.getCategory());
                    output.setFirstName(String.valueOf(sl0.size()));
                    output.setLastName("Players");
                    output.setPlace(input.getPlace());
                    output.setPlayerID("-1");
                    output.setSubLeague(input.getSubLeague());
                    output.setSubLeagueID(input.getSubLeagueID());
                    output.setYear(input.getYear());
                    pitchingLeaders.add(output);
                }

                if (sl1.size() == 1) {
                    HistoricalLeader output = new HistoricalLeader();
                    HistoricalLeader input = sl1.get(0);
                    output.setAbbr(input.getAbbr());
                    output.setAmount(input.getAmount());
                    output.setCategory(input.getCategory());
                    output.setFirstName(input.getFirstName());
                    output.setLastName(input.getLastName());
                    output.setPlace(input.getPlace());
                    output.setPlayerID(input.getPlayerID());
                    output.setSubLeague(input.getSubLeague());
                    output.setSubLeagueID(input.getSubLeagueID());
                    output.setYear(input.getYear());
                    pitchingLeaders.add(output);
                } else  if (sl1.size() > 1) {
                    HistoricalLeader output = new HistoricalLeader();
                    HistoricalLeader input = sl1.get(0);
                    output.setAbbr("");
                    output.setAmount(input.getAmount());
                    output.setCategory(input.getCategory());
                    output.setFirstName(String.valueOf(sl1.size()));
                    output.setLastName("Players");
                    output.setPlace(input.getPlace());
                    output.setPlayerID("-1");
                    output.setSubLeague(input.getSubLeague());
                    output.setSubLeagueID(input.getSubLeagueID());
                    output.setYear(input.getYear());
                    pitchingLeaders.add(output);
                }
            }
        }

        ListView pitchingLV = view.findViewById(R.id.pitching_leaderboard_view);
        pitchingLV.setAdapter(new SectionedLeaderboardAdapter(mContext, pitchingLeaders, subLeagues));

        battingLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                TextView playerID = view.findViewById(R.id.playerID);
                if (Integer.parseInt(playerID.getText().toString()) > -1) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("player_id", Integer.parseInt(playerID.getText().toString()));
                    bundle.putString("html_root", html_root);
                    bundle.putString("universe_name", universe_name);
                    bundle.putString("game_date", game_date);
                    bundle.putInt("league_id", league_id);
                    bundle.putString("league_abbr", league_abbr);
                    bundle.putString("bg_color", bg_color);
                    Fragment fragment = new PlayerCard();
                    fragment.setArguments(bundle);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.hide(getActivity().getSupportFragmentManager().findFragmentByTag("nav_info"));
                    ft.add(R.id.currentFrame, fragment, "player_card");
                    ft.addToBackStack(null);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.commit();
                }
            }
        });

        pitchingLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                TextView playerID = view.findViewById(R.id.playerID);
                if (Integer.parseInt(playerID.getText().toString()) > -1) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("player_id", Integer.parseInt(playerID.getText().toString()));
                    bundle.putString("html_root", html_root);
                    bundle.putString("universe_name", universe_name);
                    bundle.putString("game_date", game_date);
                    bundle.putInt("league_id", league_id);
                    bundle.putString("league_abbr", league_abbr);
                    bundle.putString("bg_color", bg_color);
                    Fragment fragment = new PlayerCard();
                    fragment.setArguments(bundle);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.hide(getActivity().getSupportFragmentManager().findFragmentByTag("nav_info"));
                    ft.add(R.id.currentFrame, fragment, "player_card");
                    ft.addToBackStack(null);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.commit();
                }
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            ((NavDrawer) getActivity())
                    .formatActionBar(universe_name + " " +getString(R.string._league_info), null,
                            Color.parseColor(bg_color), getContrastColor(Color.parseColor(bg_color)));
        }
    }


}
