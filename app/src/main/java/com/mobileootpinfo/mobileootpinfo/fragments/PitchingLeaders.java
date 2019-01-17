package com.mobileootpinfo.mobileootpinfo.fragments;

import android.content.Context;
import android.graphics.Color;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mobileootpinfo.mobileootpinfo.R;
import com.mobileootpinfo.mobileootpinfo.activity.NavDrawer;
import com.mobileootpinfo.mobileootpinfo.adapter.StatLeaderAdapter;
import com.mobileootpinfo.mobileootpinfo.model.StatLeader;
import com.mobileootpinfo.mobileootpinfo.model.SubLeague;
import com.mobileootpinfo.mobileootpinfo.util.CalligraphyDefaultTabLayout;
import com.mobileootpinfo.mobileootpinfo.util.DatabaseHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.mobileootpinfo.mobileootpinfo.util.ColorUtils.getContrastColor;

public class PitchingLeaders extends Fragment {

    private View view;
    private DatabaseHandler db;
    private Context mContext;
    private StatLeaderAdapter statLeaderAdapter;
    private String html_root, league_name, game_date, league_abbr, bg_color, text_color;
    private int player_id, league_id, current_season, subLeagueCount, subLeaguesProcessed;
    private final String TAG = "StatsFragment";
    private SimpleDateFormat sqliteSdf = new SimpleDateFormat("yyyy/MM/dd");
    private ArrayList<String> pitchingCountingStats1 = new ArrayList<String>();
    private CalligraphyDefaultTabLayout tabLayout;
    private ListView sl0, sl1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_stats, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getActivity();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            html_root = bundle.getString("html_root");
            league_name = bundle.getString("universe_name");
            player_id = bundle.getInt("player_id");
            game_date = bundle.getString("game_date");
            league_id = bundle.getInt("league_id");
            league_abbr = bundle.getString("league_abbr");
            bg_color = bundle.getString("bg_color");
            text_color = bundle.getString("text_color");
        }

        try {
            Date gameDate = sqliteSdf.parse(game_date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(gameDate);
            current_season = cal.get(Calendar.YEAR);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        db = new DatabaseHandler(getActivity());

        pitchingCountingStats1.add("s");
        pitchingCountingStats1.add("war");
        pitchingCountingStats1.add("g");
        pitchingCountingStats1.add("gs");
        pitchingCountingStats1.add("cg");
        pitchingCountingStats1.add("sho");
        pitchingCountingStats1.add("outs");
        pitchingCountingStats1.add("ha");
        pitchingCountingStats1.add("hra");
        pitchingCountingStats1.add("bb");

        List<SubLeague> subLeagueList = db.getSubLeagues(league_id, league_name);

        subLeagueCount = subLeagueList.size();

        if (subLeagueList.size() > 1) {
            tabLayout = view.findViewById(R.id.subLeagueTabs);
            tabLayout.setVisibility(View.GONE);
            sl0 = view.findViewById(R.id.subLeague0);
            sl1 = view.findViewById(R.id.subLeague1);
            for (SubLeague subLeague : subLeagueList) {

                TaskArgs args = new TaskArgs();
                args.multipleSubs = true;
                args.leagueName = league_name;
                args.leagueID = league_id;
                args.subLeagueID = subLeague.getSubLeagueId();
                args.currentSeason = current_season;
                args.bgColor = bg_color;
                args.txtColor = text_color;
                args.htmlRoot = html_root;
                args.subLeagueName = subLeague.getName();
                new getItemLists(mContext).execute(args);
            }

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    switch (tab.getPosition()) {
                        case 0:
                            sl0.setVisibility(View.VISIBLE);
                            sl1.setVisibility(View.GONE);
                            break;
                        case 1:
                            sl0.setVisibility(View.GONE);
                            sl1.setVisibility(View.VISIBLE);
                            break;
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {}

                @Override
                public void onTabReselected(TabLayout.Tab tab) {}
            });
        } else {
            tabLayout = view.findViewById(R.id.subLeagueTabs);
            sl0 = view.findViewById(R.id.subLeague0);
            TaskArgs args = new TaskArgs();
            args.multipleSubs = false;
            args.leagueName = league_name;
            args.leagueID = league_id;
            args.subLeagueID = subLeagueList.get(0).getSubLeagueId();
            args.currentSeason = current_season;
            args.bgColor = bg_color;
            args.txtColor = text_color;
            args.htmlRoot = html_root;
            args.subLeagueName = subLeagueList.get(0).getName();
            new getItemLists(mContext).execute(args);
        }

        ((NavDrawer) getActivity())
                .formatActionBar(league_abbr + " " + getString(R.string.pitching_leaders), null,
                        Color.parseColor(bg_color), getContrastColor(Color.parseColor(bg_color)));
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            ((NavDrawer) getActivity())
                    .formatActionBar(league_abbr + " "+ getString(R.string.pitching_leaders), null,
                            Color.parseColor(bg_color), getContrastColor(Color.parseColor(bg_color)));
        }
    }

    public class TaskArgs
    {
        public boolean multipleSubs;
        public String leagueName;
        public int leagueID;
        public int subLeagueID;
        public int currentSeason;
        public String bgColor;
        public String txtColor;
        public String htmlRoot;
        public String subLeagueName;
    }

    public class PostArgs
    {
        public List<StatLeader> statLeaders;
        public String leagueName;
        public int leagueID;
        public int subLeagueID;
        public int currentSeason;
        public String bgColor;
        public String txtColor;
        public String htmlRoot;
        public ListView listView;
        public boolean multipleSubs;
    }

    class getItemLists extends AsyncTask<TaskArgs, TaskArgs, PostArgs> {

        private Context mContext;
        private AlertDialog dialog;

        public getItemLists (Context context) {
            mContext = context;
        }

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
        protected PostArgs doInBackground(TaskArgs... params) {
            List<StatLeader> leaderBoard = new ArrayList<StatLeader>();
            ListView listView;

            TaskArgs arg = params[0];
            boolean multipleSubs = arg.multipleSubs;
            String leagueName = arg.leagueName;
            int leagueID = arg.leagueID;
            int sub = arg.subLeagueID;
            int currentSeason = arg.currentSeason;
            String bgColor = arg.bgColor;
            String txtColor = arg.txtColor;
            String htmlRoot = arg.htmlRoot;
            String subLeagueName = arg.subLeagueName;
            if (multipleSubs == true) {
                tabLayout.addTab(tabLayout.newTab().setText(subLeagueName));
                if (sub == 0) {
                    listView = sl0;
                } else {
                    listView = sl1;
                }
            } else {
                listView = sl0;
            }
            DatabaseHandler db = new DatabaseHandler(getContext());
            leaderBoard.add(db.getRateStatLeader(leagueName, leagueID, sub, currentSeason, "era", -1));
            leaderBoard.add(db.getCountingStatLeader(leagueName, leagueID, sub, currentSeason, "w", "pitching", -1));
            leaderBoard.add(db.getCountingStatLeader(leagueName, leagueID, sub, currentSeason, "l", "pitching", -1));
            leaderBoard.add(db.getRateStatLeader(leagueName, leagueID, sub, currentSeason, "pct", -1));
            for (String value : pitchingCountingStats1) {
                leaderBoard.add(db.getCountingStatLeader(leagueName, leagueID, sub, currentSeason, value, "pitching", -1));
            }
            leaderBoard.add(db.getRateStatLeader(leagueName, leagueID, sub, currentSeason, "bb9", -1));
            leaderBoard.add(db.getCountingStatLeader(leagueName, leagueID, sub, currentSeason, "k", "pitching", -1));
            leaderBoard.add(db.getRateStatLeader(leagueName, leagueID, sub, currentSeason, "k9", -1));
            leaderBoard.add(db.getRateStatLeader(leagueName, leagueID, sub, currentSeason, "kbb", -1));
            leaderBoard.add(db.getRateStatLeader(leagueName, leagueID, sub, currentSeason, "whip", -1));
            leaderBoard.add(db.getRateStatLeader(leagueName, leagueID, sub, currentSeason, "oavg", -1));
            leaderBoard.add(db.getRateStatLeader(leagueName, leagueID, sub, currentSeason, "oobp", -1));
            leaderBoard.add(db.getRateStatLeader(leagueName, leagueID, sub, currentSeason, "oslg", -1));
            leaderBoard.add(db.getRateStatLeader(leagueName, leagueID, sub, currentSeason, "oops", -1));
            leaderBoard.add(db.getRateStatLeader(leagueName, leagueID, sub, currentSeason, "obabip", -1));
            PostArgs pa = new PostArgs();
            pa.statLeaders = leaderBoard;
            pa.leagueName = leagueName;
            pa.leagueID = leagueID;
            pa.subLeagueID = sub;
            pa.currentSeason = currentSeason;
            pa.bgColor = bgColor;
            pa.txtColor = txtColor;
            pa.htmlRoot = htmlRoot;
            pa.listView = listView;
            pa.multipleSubs = multipleSubs;
            return pa;
        }

        @Override
        protected void onPostExecute(PostArgs result) {
            super.onPostExecute(result);

            final PostArgs postArgs = result;

            statLeaderAdapter = new StatLeaderAdapter(mContext,
                    postArgs.statLeaders,
                    postArgs.htmlRoot,
                    postArgs.leagueName,
                    postArgs.leagueID,
                    postArgs.subLeagueID,
                    postArgs.bgColor,
                    postArgs.txtColor);
            postArgs.listView.setAdapter(statLeaderAdapter);

            postArgs.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    TextView hiddenStat = view.findViewById(R.id.hiddenStat);
                    Bundle bundle = new Bundle();
                    bundle.putString("html_root", html_root);
                    bundle.putString("universe_name", league_name);
                    bundle.putInt("league_id", league_id);
                    bundle.putInt("sub_league_id", postArgs.subLeagueID);
                    bundle.putString("league_abbr", league_abbr);
                    bundle.putString("bg_color", postArgs.bgColor);
                    bundle.putString("text_color", postArgs.txtColor);
                    bundle.putString("statistic", hiddenStat.getText().toString().toLowerCase());
                    bundle.putInt("current_year", postArgs.currentSeason);
                    bundle.putString("game_date", game_date);
                    bundle.putString("table", "pitching");
                    bundle.putInt("team_id", -1);
                    Fragment fragment = new StatList();
                    fragment.setArguments(bundle);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.hide(getActivity().getSupportFragmentManager().findFragmentByTag("nav_pitching_leaders"));
                    ft.add(R.id.currentFrame, fragment, "stat_leader");
                    ft.addToBackStack(null);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.commit();
                }
            });

            subLeaguesProcessed++;

            if (subLeaguesProcessed == subLeagueCount) {
                LinearLayout statFragmentParent = view.findViewById(R.id.statFragmentParent);
                statFragmentParent.setVisibility(View.VISIBLE);
                if (result.multipleSubs && result.subLeagueID > 0) {
                    tabLayout.setVisibility(View.VISIBLE);
                    sl0.setVisibility(View.VISIBLE);
                } else {
                    tabLayout.setVisibility(View.GONE);
                    sl0.setVisibility(View.VISIBLE);
                }
            }

            dialog.hide();
        }
    }
}
