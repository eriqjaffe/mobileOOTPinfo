package com.mobileootpinfo.mobileootpinfo.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.inqbarna.tablefixheaders.TableFixHeaders;
import com.inqbarna.tablefixheaders.adapters.BaseTableAdapter;
import com.mobileootpinfo.mobileootpinfo.R;
import com.mobileootpinfo.mobileootpinfo.activity.NavDrawer;
import com.mobileootpinfo.mobileootpinfo.adapter.ContractAdapter;
import com.mobileootpinfo.mobileootpinfo.adapter.ManagerYearLineAdapter;
import com.mobileootpinfo.mobileootpinfo.adapter.RosterAdapter;
import com.mobileootpinfo.mobileootpinfo.adapter.ScheduleAdapter;
import com.mobileootpinfo.mobileootpinfo.adapter.SectionedRosterAdapter;
import com.mobileootpinfo.mobileootpinfo.adapter.StatLeaderAdapter;
import com.mobileootpinfo.mobileootpinfo.model.Ballpark;
import com.mobileootpinfo.mobileootpinfo.model.Contract;
import com.mobileootpinfo.mobileootpinfo.model.ManagerStatLine;
import com.mobileootpinfo.mobileootpinfo.model.Player;
import com.mobileootpinfo.mobileootpinfo.model.ScheduleLineGame;
import com.mobileootpinfo.mobileootpinfo.model.StatLeader;
import com.mobileootpinfo.mobileootpinfo.model.Team;
import com.mobileootpinfo.mobileootpinfo.util.CalligraphyDefault5TabLayout;
import com.mobileootpinfo.mobileootpinfo.util.CalligraphyDefaultTabLayout;
import com.mobileootpinfo.mobileootpinfo.util.DatabaseHandler;
import com.sdsmdg.harjot.vectormaster.VectorMasterDrawable;
import com.sdsmdg.harjot.vectormaster.models.PathModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.mobileootpinfo.mobileootpinfo.util.ColorUtils.getContrastColor;

public class TeamCard extends Fragment {

    private String html_root, game_date, universe_name, league_abbr;
    private int current_season;
    private int team_id;
    private int league_id;
    private Team team;
    private DatabaseHandler db;
    private Context mContext;
    private SimpleDateFormat sqliteSdf = new SimpleDateFormat("yyyy/MM/dd");
    private int textColor;
    DateFormat formatter = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
    private RosterAdapter rosterAdapter;
    private View view, rosterView, scheduleView, statsView, historyView, ballparkView, contractsView;
    private ListView teamBatting, teamPitching;
    private ArrayList<String> battingCountingStats = new ArrayList<String>();
    private ArrayList<String> pitchingCountingStats = new ArrayList<String>();
    private View teamCard;

    public TeamCard() {
    }

    //@GlideModule
    //public final class MyAppGlideModule extends AppGlideModule {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        view = inflater.inflate(R.layout.fragment_team_card, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles

        teamCard = view.findViewById(R.id.teamCard);

        battingCountingStats.add("war");
        battingCountingStats.add("hr");
        battingCountingStats.add("rbi");
        battingCountingStats.add("sb");
        battingCountingStats.add("r");
        battingCountingStats.add("h");
        battingCountingStats.add("d");
        battingCountingStats.add("t");
        battingCountingStats.add("bb");
        battingCountingStats.add("k");
        battingCountingStats.add("hp");
        battingCountingStats.add("sh");
        battingCountingStats.add("sf");

        pitchingCountingStats.add("s");
        pitchingCountingStats.add("war");
        pitchingCountingStats.add("g");
        pitchingCountingStats.add("gs");
        pitchingCountingStats.add("cg");
        pitchingCountingStats.add("sho");
        pitchingCountingStats.add("outs");
        pitchingCountingStats.add("ha");
        pitchingCountingStats.add("hra");
        pitchingCountingStats.add("bb");

        mContext = getActivity();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            html_root = bundle.getString("html_root");
            universe_name = bundle.getString("universe_name");
            game_date = bundle.getString("game_date");
            league_id = bundle.getInt("league_id");
            team_id = bundle.getInt("team_id");
            league_abbr = bundle.getString("league_abbr");
            try {
                Date gameDate = sqliteSdf.parse(game_date);
                Calendar cal = Calendar.getInstance();
                cal.setTime(gameDate);
                current_season = cal.get(Calendar.YEAR);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        db = new DatabaseHandler(mContext);

        team = db.getTeamInfo(universe_name, team_id, league_id);

        if (Color.parseColor(team.getTextColor()) == Color.parseColor(team.getBackgroundColor())) {
            textColor = getContrastColor(Color.parseColor(team.getTextColor()));
        } else {
            textColor = Color.parseColor(team.getTextColor());
        }

        TextView wlRecord = view.findViewById(R.id.teamRecord);
        wlRecord.setText(team.getW() + "-" + team.getL());
        wlRecord.setTextColor(textColor);

        TextView pipe1 = view.findViewById(R.id.pipe1);
        pipe1.setTextColor(textColor);

        TextView teamPlace = view.findViewById(R.id.teamPlace);
        teamPlace.setText(ordinal(team.getPos()) + " Place, " + team.getDivisionName());
        teamPlace.setTextColor(textColor);

        ImageView parkOverlay = view.findViewById(R.id.ballparkOverlay);
        String transparentColor = "#00" + (team.getBackgroundColor().substring((team.getBackgroundColor().length() - 6)));
        String opaqueColor = "#FF" + (team.getBackgroundColor().substring((team.getBackgroundColor().length() - 6)));
        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{Color.parseColor(transparentColor), Color.parseColor(opaqueColor)});
        gd.setCornerRadius(0f);
        parkOverlay.setImageDrawable(gd);

        ((NavDrawer) getActivity())
                .formatActionBar(team.getTeamName() + " " + team.getTeamNickname(),
                        null,
                        Color.parseColor(team.getBackgroundColor()), textColor);


        final VectorMasterDrawable baseball = new VectorMasterDrawable(getContext(), R.drawable.ic_baseball_2);
        PathModel black = baseball.getPathModelByName("black");
        black.setFillColor(textColor);
        PathModel white = baseball.getPathModelByName("white");
        white.setFillColor(Color.parseColor(team.getBackgroundColor()));

        ImageView teamLogo = view.findViewById(R.id.teamLogo);
        teamLogo.setImageDrawable(baseball);

        Glide.with(mContext)
                .load(html_root + mContext.getString(R.string.team_logo, team.getTeamLogo()))
                .apply(new RequestOptions()
                        .error(baseball)
                )
                .into(teamLogo);

        if (team.getParentId() > -1) {
            ImageView parentLogo = view.findViewById(R.id.parentLogo);
            Glide.with(mContext)
                    .load(html_root + mContext.getString(R.string.team_logo, team.getParentLogo()))
                    .apply(new RequestOptions()
                            .error(baseball)
                    )
                    .into(parentLogo);
        }

        CalligraphyDefault5TabLayout tabLayout = view.findViewById(R.id.teamTabs);

        tabLayout.setSelectedTabIndicatorColor(getContrastColor(Color.parseColor(team.getBackgroundColor()), textColor));

        rosterView = view.findViewById(R.id.roster_list_view);
        scheduleView = view.findViewById(R.id.schedule_list_view);
        statsView = view.findViewById(R.id.stats_list_view);
        contractsView = view.findViewById(R.id.contracts_view);
        historyView = view.findViewById(R.id.teamHistoryTable);
        ballparkView = view.findViewById(R.id.ballpark_view);

        if (team.getParentId() > -1) {
            tabLayout.addTab(tabLayout.newTab().setText("Roster"));
            tabLayout.addTab(tabLayout.newTab().setText("Schedule"));
            tabLayout.addTab(tabLayout.newTab().setText("Stats"));
            tabLayout.addTab(tabLayout.newTab().setText("History"));
            tabLayout.addTab(tabLayout.newTab().setText("Ballpark"));
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    Fragment fragment = null;
                    switch (tab.getPosition()) {
                        case 0:
                            rosterView.setVisibility(View.VISIBLE);
                            scheduleView.setVisibility(View.GONE);
                            statsView.setVisibility(View.GONE);
                            historyView.setVisibility(View.GONE);
                            ballparkView.setVisibility(View.GONE);
                            break;
                        case 1:
                            rosterView.setVisibility(View.GONE);
                            scheduleView.setVisibility(View.VISIBLE);
                            statsView.setVisibility(View.GONE);
                            historyView.setVisibility(View.GONE);
                            ballparkView.setVisibility(View.GONE);
                            break;
                        case 2:
                            rosterView.setVisibility(View.GONE);
                            scheduleView.setVisibility(View.GONE);
                            statsView.setVisibility(View.VISIBLE);
                            historyView.setVisibility(View.GONE);
                            ballparkView.setVisibility(View.GONE);
                            break;
                        case 3:
                            rosterView.setVisibility(View.GONE);
                            scheduleView.setVisibility(View.GONE);
                            statsView.setVisibility(View.GONE);
                            historyView.setVisibility(View.VISIBLE);
                            ballparkView.setVisibility(View.GONE);
                            break;
                        case 4:
                            rosterView.setVisibility(View.GONE);
                            scheduleView.setVisibility(View.GONE);
                            statsView.setVisibility(View.GONE);
                            historyView.setVisibility(View.GONE);
                            ballparkView.setVisibility(View.VISIBLE);
                            break;
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });
        } else {
            tabLayout.addTab(tabLayout.newTab().setText(R.string.roster));
            tabLayout.addTab(tabLayout.newTab().setText(R.string.schedule));
            tabLayout.addTab(tabLayout.newTab().setText(R.string.stats));
            tabLayout.addTab(tabLayout.newTab().setText(R.string.contracts));
            tabLayout.addTab(tabLayout.newTab().setText(R.string.history));
            tabLayout.addTab(tabLayout.newTab().setText(R.string.ballpark));
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    Fragment fragment = null;
                    switch (tab.getPosition()) {
                        case 0:
                            rosterView.setVisibility(View.VISIBLE);
                            scheduleView.setVisibility(View.GONE);
                            statsView.setVisibility(View.GONE);
                            contractsView.setVisibility(View.GONE);
                            historyView.setVisibility(View.GONE);
                            ballparkView.setVisibility(View.GONE);
                            break;
                        case 1:
                            rosterView.setVisibility(View.GONE);
                            scheduleView.setVisibility(View.VISIBLE);
                            statsView.setVisibility(View.GONE);
                            contractsView.setVisibility(View.GONE);
                            historyView.setVisibility(View.GONE);
                            ballparkView.setVisibility(View.GONE);
                            break;
                        case 2:
                            rosterView.setVisibility(View.GONE);
                            scheduleView.setVisibility(View.GONE);
                            statsView.setVisibility(View.VISIBLE);
                            contractsView.setVisibility(View.GONE);
                            historyView.setVisibility(View.GONE);
                            ballparkView.setVisibility(View.GONE);
                            break;
                        case 3:
                            rosterView.setVisibility(View.GONE);
                            scheduleView.setVisibility(View.GONE);
                            statsView.setVisibility(View.GONE);
                            contractsView.setVisibility(View.VISIBLE);
                            historyView.setVisibility(View.GONE);
                            ballparkView.setVisibility(View.GONE);
                            break;
                        case 4:
                            rosterView.setVisibility(View.GONE);
                            scheduleView.setVisibility(View.GONE);
                            statsView.setVisibility(View.GONE);
                            contractsView.setVisibility(View.GONE);
                            historyView.setVisibility(View.VISIBLE);
                            ballparkView.setVisibility(View.GONE);
                            break;
                        case 5:
                            rosterView.setVisibility(View.GONE);
                            scheduleView.setVisibility(View.GONE);
                            statsView.setVisibility(View.GONE);
                            contractsView.setVisibility(View.GONE);
                            historyView.setVisibility(View.GONE);
                            ballparkView.setVisibility(View.VISIBLE);
                            break;
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });
        }

        CalligraphyDefaultTabLayout statsTabLayout = view.findViewById(R.id.statsTabs);
        statsTabLayout.addTab(statsTabLayout.newTab().setText(R.string.batting));
        statsTabLayout.addTab(statsTabLayout.newTab().setText(R.string.pitching));

        statsTabLayout.setSelectedTabIndicatorColor(getContrastColor(Color.parseColor(team.getBackgroundColor()), textColor));

        teamBatting = view.findViewById(R.id.teamBatting);
        teamPitching = view.findViewById(R.id.teamPitching);

        statsTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        teamBatting.setVisibility(View.VISIBLE);
                        teamPitching.setVisibility(View.GONE);
                        break;
                    case 1:
                        teamBatting.setVisibility(View.GONE);
                        teamPitching.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        populateRoster(universe_name, team_id, league_abbr);
        populateSchedule(universe_name, team_id);
        populateContracts(universe_name, team_id, league_id);
        populateHistory(universe_name, team_id);
        populateBallpark(universe_name, team_id);
        new getStats().execute();
    }

    private void populateRoster(String universe_name, int team_id, String league_abbr) {
        final String universe_name1 = universe_name;
        final String league_abbr1 = league_abbr;
        ListView listView = getActivity().findViewById(R.id.roster_list_view);
        ArrayList<Object> roster = new ArrayList<>();
        List<Player> pitchers = db.getActiveRoster(universe_name, team_id, 1, 1);
        List<Player> catchers = db.getActiveRoster(universe_name, team_id, 2, 2);
        List<Player> infielders = db.getActiveRoster(universe_name, team_id, 3, 6);
        List<Player> outfielders = db.getActiveRoster(universe_name, team_id, 7, 9);
        List<Player> dh = db.getActiveRoster(universe_name, team_id, 10, 10);
        roster.add(getString(R.string.pitchers));
        for (Player p : pitchers) {
            roster.add(p);
        }
        roster.add(getString(R.string.catchers));
        for (Player p : catchers) {
            roster.add(p);
        }
        roster.add(getString(R.string.infielders));
        for (Player p : infielders) {
            roster.add(p);
        }
        roster.add(getString(R.string.outfielders));
        for (Player p : outfielders) {
            roster.add(p);
        }
        if (dh.size() > 0) {
            roster.add(getString(R.string.designated_hitters));
            for (Player p : dh) {
                roster.add(p);
            }
        }
        listView.setAdapter(new SectionedRosterAdapter(mContext, roster, html_root, game_date));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                TextView playerID = view.findViewById(R.id.playerID);
                Bundle bundle = new Bundle();
                bundle.putInt("player_id", Integer.parseInt(playerID.getText().toString()));
                bundle.putString("html_root", html_root);
                bundle.putString("universe_name", universe_name1);
                bundle.putString("game_date", game_date);
                bundle.putInt("league_id", league_id);
                bundle.putString("league_abbr", league_abbr1);
                Fragment fragment = new PlayerCard();
                fragment.setArguments(bundle);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.hide(getActivity().getSupportFragmentManager().findFragmentByTag("team_card"));
                ft.add(R.id.currentFrame, fragment, "player_card");
                ft.addToBackStack(null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }
        });
    }

    private void populateSchedule(String universe_name, int team_id) {
        ListView listView = getActivity().findViewById(R.id.schedule_list_view);
        int gamesPlayed = db.getGamesPlayed(universe_name, team_id);
        List<ScheduleLineGame> schedule = db.getTeamSchedule(universe_name, team_id);
        ScheduleAdapter scheduleAdpater = new ScheduleAdapter(mContext, schedule, universe_name, html_root, game_date, universe_name, league_id, league_abbr);
        listView.setAdapter(scheduleAdpater);
        listView.setSelection(gamesPlayed > 0 ? gamesPlayed - 1 : gamesPlayed);
    }

    private void populateHistory(String universe_name, int team_id) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        TableFixHeaders tablefixheaders = (TableFixHeaders) view.findViewById(R.id.teamHistoryTable);
        tablefixheaders.setAdapter(getTeamHistoryAdapter());
    }

    private void populateContracts(String universe_name, int team_id, int league_id) {
        ListView listView = getActivity().findViewById(R.id.contracts_view);
        List<Contract> contracts = db.getTeamContracts(universe_name, team_id, league_id);
        ContractAdapter contractAdapter = new ContractAdapter(mContext, contracts, html_root);
        listView.setAdapter(contractAdapter);
    }

    private void populateBallpark(String universe_name, int team_id) {
        Ballpark ballpark = db.getBallpark(universe_name, team_id);

        TextView ballparkName = getActivity().findViewById(R.id.ballparkName);
        ballparkName.setText(ballpark.getName());

        TextView ballparkCapacity = getActivity().findViewById(R.id.ballparkCapacity);
        ballparkCapacity.setText("Capacity: "+ ballpark.getCapacity());
        
        TextView leftLine = getActivity().findViewById(R.id.leftLine);
        leftLine.setText(""+ballpark.getLeftLine());

        TextView leftField = getActivity().findViewById(R.id.leftField);
        leftField.setText(""+ballpark.getLeftField());

        TextView leftCenter = getActivity().findViewById(R.id.leftCenter);
        leftCenter.setText(""+ballpark.getLeftCenter());

        TextView centerField = getActivity().findViewById(R.id.centerField);
        centerField.setText(""+ballpark.getCenterField());

        TextView rightLine = getActivity().findViewById(R.id.rightLine);
        rightLine.setText(""+ballpark.getRightLine());

        TextView rightField = getActivity().findViewById(R.id.rightField);
        rightField.setText(""+ballpark.getRightField());

        TextView rightCenter = getActivity().findViewById(R.id.rightCenter);
        rightCenter.setText(""+ballpark.getRightCenter());

        TextView avg = getActivity().findViewById(R.id.avg);
        avg.setText(ballpark.getAvg());

        TextView avgl = getActivity().findViewById(R.id.avgl);
        avgl.setText(ballpark.getAvg_l());

        TextView avgr = getActivity().findViewById(R.id.avgr);
        avgr.setText(ballpark.getAvg_r());

        TextView d = getActivity().findViewById(R.id.d);
        d.setText(ballpark.getD());

        TextView t = getActivity().findViewById(R.id.t);
        t.setText(ballpark.getT());

        TextView hr = getActivity().findViewById(R.id.hr);
        hr.setText(ballpark.getHr());

        TextView hrl = getActivity().findViewById(R.id.hrl);
        hrl.setText(ballpark.getHr_l());

        TextView hrr = getActivity().findViewById(R.id.hrr);
        hrr.setText(ballpark.getHr_r());

        ImageView fieldImage = getActivity().findViewById(R.id.fieldImage);

        Glide.with(mContext)
                .load(html_root + mContext.getString(R.string.team_logo, team.getTeamLogo()))
                .apply(new RequestOptions()
                )
                .into(fieldImage);
    }

    public static String ordinal(int i) {
        int mod100 = i % 100;
        int mod10 = i % 10;
        if(mod10 == 1 && mod100 != 11) {
            return i + "st";
        } else if(mod10 == 2 && mod100 != 12) {
            return i + "nd";
        } else if(mod10 == 3 && mod100 != 13) {
            return i + "rd";
        } else {
            return i + "th";
        }
    }

    class getStats extends AsyncTask<Void, Void, Void> {

        private AlertDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LayoutInflater li = LayoutInflater.from(mContext);
            final View downloadView = li.inflate(R.layout.get_leaders_progress, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setView(downloadView);
            dialog = builder.create();
            dialog.show();

        }

        @Override
        protected Void doInBackground(Void... params){
            List<StatLeader> battingLeaderBoard = new ArrayList<StatLeader>();
            DatabaseHandler db = new DatabaseHandler(getContext());
            battingLeaderBoard.add(db.getRateStatLeader(universe_name, league_id, team.getSubLeagueID(), current_season, "avg", team_id));
            battingLeaderBoard.add(db.getRateStatLeader(universe_name, league_id, team.getSubLeagueID(), current_season, "obp", team_id));
            battingLeaderBoard.add(db.getRateStatLeader(universe_name, league_id, team.getSubLeagueID(), current_season, "slg", team_id));
            battingLeaderBoard.add(db.getRateStatLeader(universe_name, league_id, team.getSubLeagueID(), current_season, "ops", team_id));
            for (String value : battingCountingStats) {
                battingLeaderBoard.add(db.getCountingStatLeader(universe_name, league_id, team.getSubLeagueID(), current_season, value, "batting", team_id));
            }
            battingLeaderBoard.add(db.getRateStatLeader(universe_name, league_id, team.getSubLeagueID(), current_season, "babip", team_id));
            StatLeaderAdapter statLeaderAdapter = new StatLeaderAdapter(mContext,
                    battingLeaderBoard,
                    html_root,
                    universe_name,
                    league_id, team.getSubLeagueID(),
                    team.getBackgroundColor(),
                    team.getTextColor());
            teamBatting.setAdapter(statLeaderAdapter);

            teamBatting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    TextView hiddenStat = view.findViewById(R.id.hiddenStat);
                    Bundle bundle = new Bundle();
                    bundle.putString("html_root", html_root);
                    bundle.putString("universe_name", universe_name);
                    bundle.putInt("league_id", league_id);
                    bundle.putInt("sub_league_id", team.getSubLeagueID());
                    bundle.putString("league_abbr", league_abbr);
                    bundle.putString("bg_color", team.getBackgroundColor());
                    bundle.putString("text_color", team.getTextColor());
                    bundle.putString("statistic", hiddenStat.getText().toString().toLowerCase());
                    bundle.putInt("current_year", current_season);
                    bundle.putString("game_date", game_date);
                    bundle.putString("table", "batting");
                    bundle.putInt("team_id", team.getId());
                    bundle.putString("team_abbr", team.getTeamAbbr());
                    Fragment fragment = new StatList();
                    fragment.setArguments(bundle);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.hide(getActivity().getSupportFragmentManager().findFragmentByTag("team_card"));
                    ft.add(R.id.currentFrame, fragment, "stat_leader");
                    ft.addToBackStack(null);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.commit();
                }
            });

            List<StatLeader> pitchingLeaderBoard = new ArrayList<StatLeader>();;
            pitchingLeaderBoard.add(db.getRateStatLeader(universe_name, league_id, team.getSubLeagueID(), current_season, "era", team_id));
            pitchingLeaderBoard.add(db.getCountingStatLeader(universe_name, league_id, team.getSubLeagueID(), current_season, "w", "pitching", team_id));
            pitchingLeaderBoard.add(db.getCountingStatLeader(universe_name, league_id, team.getSubLeagueID(), current_season, "l", "pitching", team_id));
            pitchingLeaderBoard.add(db.getRateStatLeader(universe_name, league_id, team.getSubLeagueID(), current_season, "pct", team_id));
            for (String value : pitchingCountingStats) {
                pitchingLeaderBoard.add(db.getCountingStatLeader(universe_name, league_id, team.getSubLeagueID(), current_season, value, "pitching", team_id));
            }
            pitchingLeaderBoard.add(db.getRateStatLeader(universe_name, league_id, team.getSubLeagueID(), current_season, "bb9", team_id));
            pitchingLeaderBoard.add(db.getCountingStatLeader(universe_name, league_id, team.getSubLeagueID(), current_season, "k", "pitching", team_id));
            pitchingLeaderBoard.add(db.getRateStatLeader(universe_name, league_id, team.getSubLeagueID(), current_season, "k9", team_id));
            pitchingLeaderBoard.add(db.getRateStatLeader(universe_name, league_id, team.getSubLeagueID(), current_season, "kbb", team_id));
            pitchingLeaderBoard.add(db.getRateStatLeader(universe_name, league_id, team.getSubLeagueID(), current_season, "whip", team_id));
            pitchingLeaderBoard.add(db.getRateStatLeader(universe_name, league_id, team.getSubLeagueID(), current_season, "oavg", team_id));
            pitchingLeaderBoard.add(db.getRateStatLeader(universe_name, league_id, team.getSubLeagueID(), current_season, "oobp", team_id));
            pitchingLeaderBoard.add(db.getRateStatLeader(universe_name, league_id, team.getSubLeagueID(), current_season, "oslg", team_id));
            pitchingLeaderBoard.add(db.getRateStatLeader(universe_name, league_id, team.getSubLeagueID(), current_season, "oops", team_id));
            pitchingLeaderBoard.add(db.getRateStatLeader(universe_name, league_id, team.getSubLeagueID(), current_season, "obabip", team_id));
            StatLeaderAdapter statLeaderAdapter2 = new StatLeaderAdapter(mContext,
                    pitchingLeaderBoard,
                    html_root,
                    universe_name,
                    league_id, team.getSubLeagueID(),
                    team.getBackgroundColor(),
                    team.getTextColor());
            teamPitching.setAdapter(statLeaderAdapter2);

            teamPitching.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    TextView hiddenStat = view.findViewById(R.id.hiddenStat);
                    Bundle bundle = new Bundle();
                    bundle.putString("html_root", html_root);
                    bundle.putString("universe_name", universe_name);
                    bundle.putInt("league_id", league_id);
                    bundle.putInt("sub_league_id", team.getSubLeagueID());
                    bundle.putString("league_abbr", league_abbr);
                    bundle.putString("bg_color", team.getBackgroundColor());
                    bundle.putString("text_color", team.getTextColor());
                    bundle.putString("statistic", hiddenStat.getText().toString().toLowerCase());
                    bundle.putInt("current_year", current_season);
                    bundle.putString("game_date", game_date);
                    bundle.putString("table", "pitching");
                    bundle.putInt("team_id", team.getId());
                    bundle.putString("team_abbr", team.getTeamAbbr());
                    Fragment fragment = new StatList();
                    fragment.setArguments(bundle);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.hide(getActivity().getSupportFragmentManager().findFragmentByTag("team_card"));
                    ft.add(R.id.currentFrame, fragment, "stat_leader");
                    ft.addToBackStack(null);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.commit();
                }
            });

            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            dialog.hide();
            teamCard.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            ((NavDrawer) getActivity())
                    .formatActionBar(team.getTeamName()+ " "+team.getTeamNickname(),
                            null,
                            Color.parseColor(team.getBackgroundColor()), textColor);
        }
    }



    public BaseTableAdapter getTeamHistoryAdapter() {
        ManagerYearLineAdapter adapter = new ManagerYearLineAdapter(getActivity());
        List<List<String>> body = getTeamHistoryBody();

        adapter.setFirstHeader("YEAR");
        adapter.setHeader(getTeamHistoryHeader());
        adapter.setFirstBody(body);
        adapter.setBody(body);
        adapter.setSection(body);

        return adapter;
    }

    private List<String> getTeamHistoryHeader() {
        List<String> header = new ArrayList<>();

        header.add("TEAM");
        header.add("LG");
        header.add("W");
        header.add("L");
        header.add("PCT");
        header.add("AVG");
        header.add("HR");
        header.add("SB");
        header.add("ERA");
        header.add("SO");

        return header;
    }

    private List<List<String>> getTeamHistoryBody() {
        List<List<String>> rows = new ArrayList<>();
        List<ManagerStatLine> lines = db.teamHistory(universe_name, team_id);
        for (ManagerStatLine line : lines) {
            List<String> cols = new ArrayList<>();
            String name = line.getAbbr();
            cols.add(String.valueOf(line.getYear()));
            if (line.getMadePlayoffs() == 1) {
                name += "*";
            }
            if (line.getWonPlayoffs() == 1) {
                name += "*";
            }
            cols.add(name);
            cols.add(line.getLeagueAbbr());
            cols.add(String.valueOf(line.getW()));
            cols.add(String.valueOf(line.getL()));
            cols.add(line.getPct());
            cols.add(line.getAvg());
            cols.add(String.valueOf(line.getHr()));
            cols.add(String.valueOf(line.getSb()));
            cols.add(line.getEra());
            cols.add(String.valueOf(line.getK()));
            rows.add(cols);
        }
        return rows;
    }
}
