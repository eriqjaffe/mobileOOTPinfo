package com.mobileootpinfo.mobileootpinfo.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobileootpinfo.mobileootpinfo.R;
import com.mobileootpinfo.mobileootpinfo.activity.NavDrawer;
import com.mobileootpinfo.mobileootpinfo.adapter.GameSummaryAdapter;
import com.mobileootpinfo.mobileootpinfo.adapter.PendingGameSummaryAdapter;
import com.mobileootpinfo.mobileootpinfo.model.GameSummary;
import com.mobileootpinfo.mobileootpinfo.model.PendingGameSummary;
import com.mobileootpinfo.mobileootpinfo.util.DatabaseHandler;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static com.mobileootpinfo.mobileootpinfo.util.ColorUtils.getContrastColor;

public class Scores extends Fragment {

    private String html_root;
    private String league_date;
    private String game_date;
    private String league_name;
    private String bg_color;
    private String league_abbr;
    private String universe_name;
    private int league_id;
    private GameSummaryAdapter gameSummaryAdapter;
    private PendingGameSummaryAdapter pendingGameSummaryAdapter;
    private SimpleDateFormat sqliteSdf;
    private Date scoreboardDate;
    private Fragment fragment;
    private ListView listView;
    private TextView displayedDate;
    private DateFormat formatter;
    private DatabaseHandler db;
    private View mainView;
    private Context mContext;
    private DatePickerDialog dpd;
    private GestureDetectorCompat mDetector;
    private Date calendarMin;
    private Date calendarMax;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();
        mainView = inflater.inflate(R.layout.fragment_scores, container, false);
        return mainView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Scores");

        db = new DatabaseHandler(getActivity());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            html_root = bundle.getString("html_root");
            league_date = bundle.getString("game_date");
            game_date = bundle.getString("game_date");
            league_name = bundle.getString("universe_name");
            league_id = bundle.getInt("league_id");
            bg_color = bundle.getString("bg_color");
            league_abbr = bundle.getString("league_abbr");
            universe_name = bundle.getString("universe_name");
        }

        displayedDate = view.findViewById(R.id.displayedDate);

        sqliteSdf = new SimpleDateFormat("yyyy/MM/dd");
        formatter = DateFormat.getDateInstance(DateFormat.FULL, Locale.getDefault());
        try {
            scoreboardDate = sqliteSdf.parse(game_date);
            displayedDate.setText(formatter.format(scoreboardDate));
        } catch (ParseException e) {
            e.printStackTrace();
            displayedDate.setText(game_date);
        }

        checkTodaysSchedule(view);

        calendarMin = db.getMinMaxGame(universe_name, league_id, "min");
        calendarMax = db.getMinMaxGame(universe_name, league_id, "max");

        displayedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                try {
                    calendar.setTime(sqliteSdf.parse(game_date));
                    dpd = new DatePickerDialog(getActivity(),
                            R.style.DialogTheme, datePickerListener,
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH));
                    DatePicker dp = dpd.getDatePicker();
                    if (calendarMin == null) {
                        calendar.set(Calendar.MONTH, Calendar.JANUARY);
                        calendar.set(Calendar.DAY_OF_MONTH, 1);
                        dp.setMinDate(calendar.getTimeInMillis());
                    } else {
                        dp.setMinDate(calendarMin.getTime());
                    }
                    if (calendarMax == null) {
                        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
                        calendar.set(Calendar.DAY_OF_MONTH, 31);
                        dp.setMaxDate(calendar.getTimeInMillis());
                    } else {
                        dp.setMaxDate(calendarMax.getTime());
                    }

                    dpd.setCancelable(true);
                    dpd.show();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        ImageButton backButton = view.findViewById(R.id.dayBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    scoreboardDate = sqliteSdf.parse(game_date);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(scoreboardDate);
                    cal.add(Calendar.DATE, -1);
                    game_date = sqliteSdf.format(cal.getTime());
                    if (!sqliteSdf.parse(game_date).before(calendarMin)) {
                        checkTodaysSchedule(mainView);
                        sqliteSdf = new SimpleDateFormat("yyyy/MM/dd");
                        formatter = DateFormat.getDateInstance(DateFormat.FULL, Locale.getDefault());
                        try {
                            scoreboardDate = sqliteSdf.parse(game_date);
                            displayedDate.setText(formatter.format(scoreboardDate));
                        } catch (ParseException e) {
                            e.printStackTrace();
                            displayedDate.setText(game_date);
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        ImageButton forwardButton = view.findViewById(R.id.dayForward);
        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    scoreboardDate = sqliteSdf.parse(game_date);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(scoreboardDate);
                    cal.add(Calendar.DATE, 1);
                    game_date = sqliteSdf.format(cal.getTime());
                    if (!sqliteSdf.parse(game_date).after(calendarMax)) {
                        checkTodaysSchedule(mainView);
                        sqliteSdf = new SimpleDateFormat("yyyy/MM/dd");
                        formatter = DateFormat.getDateInstance(DateFormat.FULL, Locale.getDefault());
                        try {
                            scoreboardDate = sqliteSdf.parse(game_date);
                            displayedDate.setText(formatter.format(scoreboardDate));
                        } catch (ParseException e) {
                            e.printStackTrace();
                            displayedDate.setText(game_date);
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        ((NavDrawer) getActivity())
                .formatActionBar(league_abbr + " Scoreboard", null,
                        Color.parseColor(bg_color), getContrastColor(Color.parseColor(bg_color)));
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            String year1 = String.valueOf(selectedYear);
            String month1 = String.valueOf(selectedMonth + 1);
            String day1 = String.valueOf(selectedDay);
            Calendar cal = Calendar.getInstance();
            cal.set(selectedYear, selectedMonth, selectedDay);
            game_date = sqliteSdf.format(cal.getTime());
            checkTodaysSchedule(mainView);
            sqliteSdf = new SimpleDateFormat("yyyy/MM/dd");
            formatter = DateFormat.getDateInstance(DateFormat.FULL, Locale.getDefault());
            try {
                scoreboardDate = sqliteSdf.parse(game_date);
                displayedDate.setText(formatter.format(scoreboardDate));
            } catch (ParseException e) {
                e.printStackTrace();
                displayedDate.setText(game_date);
            }
        }
    };

    public void checkTodaysSchedule(View view) {
        Boolean gamesToday = db.gamesToday(game_date, league_id, league_name);
        Boolean pendingToday = db.pendingGamesToday(game_date, league_id, league_name);
        RelativeLayout no_games_view = view.findViewById(R.id.no_games_view);
        ListView future_scoreboard_view = view.findViewById(R.id.future_scoreboard_view);
        ListView scoreboard_view = view.findViewById(R.id.scoreboard_view);
        if (!gamesToday) {
            //listView = no_games_view;
            no_games_view.setVisibility(View.VISIBLE);
            future_scoreboard_view.setVisibility(View.GONE);
            scoreboard_view.setVisibility(View.GONE);
        } else if (gamesToday && pendingToday) {
            listView = view.findViewById(R.id.future_scoreboard_view);
            listView.setVisibility(View.VISIBLE);
            no_games_view.setVisibility(View.GONE);
            scoreboard_view.setVisibility(View.GONE);
            populatePendingListView(getActivity(), listView, game_date);
        } else if (gamesToday && !pendingToday) {
            listView = view.findViewById(R.id.scoreboard_view);
            no_games_view.setVisibility(View.GONE);
            future_scoreboard_view.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            populatePlayedListView(getActivity(), listView, game_date);
        }
    }

    public void populatePlayedListView(Context ctx, ListView listView, String date) {
        List<GameSummary> games = db.getScoreboard(date, league_id, league_name, html_root);
        gameSummaryAdapter = new GameSummaryAdapter(ctx, games, html_root, date, league_id, universe_name, league_abbr);
        listView.setAdapter(gameSummaryAdapter);
    }

    public void populatePendingListView(Context ctx, ListView listView, String date) {
        List<PendingGameSummary> games = db.getPendingScoreboard(date, league_id, league_name, html_root);
        pendingGameSummaryAdapter = new PendingGameSummaryAdapter(ctx, games, html_root, date, league_id);
        listView.setAdapter(pendingGameSummaryAdapter);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            ((NavDrawer) getActivity())
                    .formatActionBar(league_abbr + " Scoreboard", null,
                            Color.parseColor(bg_color), getContrastColor(Color.parseColor(bg_color)));
        }
    }
}
