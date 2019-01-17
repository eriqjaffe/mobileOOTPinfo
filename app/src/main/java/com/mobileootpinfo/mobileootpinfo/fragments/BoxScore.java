package com.mobileootpinfo.mobileootpinfo.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mobileootpinfo.mobileootpinfo.R;
import com.mobileootpinfo.mobileootpinfo.activity.NavDrawer;
import com.mobileootpinfo.mobileootpinfo.model.BattingGameNotes;
import com.mobileootpinfo.mobileootpinfo.model.BattingLineScore;
import com.mobileootpinfo.mobileootpinfo.model.GameLogInning;
import com.mobileootpinfo.mobileootpinfo.model.GameNotes;
import com.mobileootpinfo.mobileootpinfo.model.GameSummary;
import com.mobileootpinfo.mobileootpinfo.model.JsoupBoxScore;
import com.mobileootpinfo.mobileootpinfo.model.PitchingGameNotes;
import com.mobileootpinfo.mobileootpinfo.model.PitchingLineScore;
import com.mobileootpinfo.mobileootpinfo.util.CalligraphyDefaultTabLayout;
import com.mobileootpinfo.mobileootpinfo.util.DatabaseHandler;
import com.sdsmdg.harjot.vectormaster.VectorMasterDrawable;
import com.sdsmdg.harjot.vectormaster.models.PathModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyTypefaceSpan;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

import static com.mobileootpinfo.mobileootpinfo.util.ColorUtils.getContrastColor;
import static com.mobileootpinfo.mobileootpinfo.util.Connectivity.isConnected;

/**
 * Created by eriqj on 3/22/2018.
 */

public class BoxScore extends Fragment {

    private View view;
    private String html_root, game_date;
    private int league_id;
    private GameSummary gameSummary;
    private SimpleDateFormat sqliteSdf = new SimpleDateFormat("yyyy/MM/dd");

    private String scoreLine;
    private String displayDate;
    private String displayName;
    private String universe_name;
    private String league_abbr;
    private int game_type;
    private String awayDisplay, homeDisplay;
    private TextView r, h, e, spacer;

    private Context mContext;
    private DatabaseHandler db;
    
    private int rowCount = 1;
    private int textColor;

    private static String TAG = "BOX SCORE";

    private CalligraphyDefaultTabLayout headerTabLayout;

    private LayoutInflater li;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_box_score, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = getContext();

        db = new DatabaseHandler(mContext);

        li = LayoutInflater.from(mContext);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            html_root = bundle.getString("html_root");
            game_date = bundle.getString("game_date");
            universe_name = bundle.getString("universe_name");
            league_id = bundle.getInt("league_id");
            league_abbr = bundle.getString("league_abbr");
            game_type = bundle.getInt("game_type");
            gameSummary = (GameSummary) bundle.getSerializable("game_summary");
        }

        headerTabLayout = view.findViewById(R.id.boxScoreTabLayout);
        headerTabLayout.addTab(headerTabLayout.newTab().setText("BOX SCORE"));
        headerTabLayout.addTab(headerTabLayout.newTab().setText("GAME LOG"));

        final View boxScoreLayout = view.findViewById(R.id.boxScoreLayout);
        final View gameLogLayout = view.findViewById(R.id.gameLogScrollview);

        headerTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        boxScoreLayout.setVisibility(View.VISIBLE);
                        gameLogLayout.setVisibility(View.GONE);
                        break;
                    case 1:
                        boxScoreLayout.setVisibility(View.GONE);
                        gameLogLayout.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        ImageView awayTeamLogo = view.findViewById(R.id.awayTeamLogo);

        if (Color.parseColor(gameSummary.getAwayBGColor()) == Color.parseColor(gameSummary.getAwayTextColor())) {
            textColor = getContrastColor(Color.parseColor(gameSummary.getAwayTextColor()));
        } else {
            textColor = Color.parseColor(gameSummary.getAwayTextColor());
        }

        VectorMasterDrawable away_baseball = new VectorMasterDrawable(getContext(), R.drawable.ic_baseball_2);
        PathModel away_black = away_baseball.getPathModelByName("black");
        away_black.setFillColor(textColor);
        PathModel away_white = away_baseball.getPathModelByName("white");
        away_white.setFillColor(Color.parseColor(gameSummary.getAwayBGColor()));

        Glide.with(mContext)
                .load(html_root + getString(R.string.team_logo, gameSummary.getAwayTeamLogo()))
                .apply(new RequestOptions()
                        .error(away_baseball)
                )
                .into(awayTeamLogo);

        ImageView homeTeamLogo = view.findViewById(R.id.homeTeamLogo);

        VectorMasterDrawable home_baseball = new VectorMasterDrawable(getContext(), R.drawable.ic_baseball_2);
        PathModel home_black = home_baseball.getPathModelByName("black");
        home_black.setFillColor(textColor);
        PathModel home_white = home_baseball.getPathModelByName("white");
        home_white.setFillColor(Color.parseColor(gameSummary.getHomeBGColor()));

        Glide.with(mContext)
                .load(html_root + getString(R.string.team_logo, gameSummary.getHomeTeamLogo()))
                .apply(new RequestOptions()
                        .error(home_baseball)
                )
                .into(homeTeamLogo);

        TextView awayTeamRecord = view.findViewById(R.id.awayTeamRecord);
        awayTeamRecord.setText(gameSummary.getAwayRecord());

        TextView homeTeamRecord = view.findViewById(R.id.homeTeamRecord);
        homeTeamRecord.setText(gameSummary.getHomeRecord());

        if (gameSummary.getGameType() == 4) {
            homeDisplay = gameSummary.getHomeTeamAbbr() + " " + gameSummary.getHomeTeamNickhame();
            awayDisplay = gameSummary.getAwayTeamAbbr() + " " + gameSummary.getAwayTeamNickname();
        } else {
            homeDisplay = gameSummary.getHomeTeamNickhame();
            awayDisplay = gameSummary.getAwayTeamNickname();
        }

        if (gameSummary.getHomeScore() > gameSummary.getAwayScore()) {
            scoreLine = homeDisplay + " " + gameSummary.getHomeScore() + ", "
                    + awayDisplay + " " + gameSummary.getAwayScore();
        } else {
            scoreLine = awayDisplay + " " + gameSummary.getAwayScore() + ", "
                    + homeDisplay + " " + gameSummary.getHomeScore();
        }

        TextView finalScore = view.findViewById(R.id.finalScore);
        finalScore.setText(scoreLine);

        try {
            Date tmpDate = sqliteSdf.parse(game_date);
            DateFormat formatter = DateFormat.getDateInstance(DateFormat.FULL, Locale.getDefault());
            displayDate = formatter.format(tmpDate);
        } catch (ParseException e) {
            displayDate = game_date;
        }

        TextView gameDate = view.findViewById(R.id.gameDate);
        gameDate.setText(displayDate);

        populateLineScore();

        TextView winningPitcher = view.findViewById(R.id.winningPitcher);
        winningPitcher.setText(gameSummary.getWinningPitcherName());

        TextView losingPitcher = view.findViewById(R.id.losingPitcher);
        losingPitcher.setText(gameSummary.getLosingPitcherName());

        TextView winningPitcherLine = view.findViewById(R.id.winningPitcherStatLine);
        winningPitcherLine.setText(gameSummary.getWinningPitcherLine());

        TextView losingPitcherLine = view.findViewById(R.id.losingPitcherStatLine);
        losingPitcherLine.setText(gameSummary.getLosingPitcherLine());

        TextView s = view.findViewById(R.id.s);
        TextView savingPitcher = view.findViewById(R.id.savingPitcher);
        TextView savingPitcherLine = view.findViewById(R.id.savingPitcherStatLine);
        if (gameSummary.getSavingPitcher() > 0) {

            s.setVisibility(View.VISIBLE);
            savingPitcher.setVisibility(View.VISIBLE);
            savingPitcherLine.setVisibility(View.VISIBLE);
            savingPitcher.setText(gameSummary.getSavingPitcherName());
            savingPitcherLine.setText(gameSummary.getSavingPitcherLine());
        } else {
            s.setVisibility(View.GONE);
            savingPitcher.setVisibility(View.GONE);
            savingPitcherLine.setVisibility(View.GONE);
        }

        CalligraphyDefaultTabLayout tabLayout = view.findViewById(R.id.teamTabs);
        tabLayout.addTab(tabLayout.newTab().setText(awayDisplay));
        tabLayout.addTab(tabLayout.newTab().setText(homeDisplay));

        final View awayTeamLayout = view.findViewById(R.id.awayTeamLayout);
        final View homeTeamLayout = view.findViewById(R.id.homeTeamLayout);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        awayTeamLayout.setVisibility(View.VISIBLE);
                        homeTeamLayout.setVisibility(View.GONE);
                        break;
                    case 1:
                        awayTeamLayout.setVisibility(View.GONE);
                        homeTeamLayout.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        BoxScoreWrapper bsw = new BoxScoreWrapper();
        bsw.gameID = gameSummary.getGameId();
        bsw.awayAbbr = gameSummary.getAwayTeamAbbr();
        bsw.homeAbbr = gameSummary.getHomeTeamAbbr();

        new populateLines().execute(bsw);

        ((NavDrawer) getActivity())
                .formatActionBar(league_abbr+ " Box Score", null,
                        getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.white));
    }

    private class BoxScoreWrapper {
        int gameID;
        String awayAbbr;
        String homeAbbr;
    }

    private void populateLineScore() {
        TextView inning;
        View inn, total;

        TableRow lineScoreHeader = view.findViewById(R.id.lineScoreHeader);
        TableRow lineScoreAway = view.findViewById(R.id.lineScoreAway);
        TableRow lineScoreHome = view.findViewById(R.id.lineScoreHome);

        //LayoutInflater li = LayoutInflater.from(mContext);

        TableRow.LayoutParams params1=new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1.0f);
        TableRow.LayoutParams params2=new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,0.5f);

        for (int i = 1; i <= gameSummary.getAwayLine().size() ; i++) {
            inn = li.inflate(R.layout.inning, null);
            inning = inn.findViewById(R.id.inning);
            inning.setText(String.valueOf(i));
            inning.setLayoutParams(params1);
            lineScoreHeader.addView(inn);
        }

        View spacer = li.inflate(R.layout.inning_spacer, null);
        spacer.setLayoutParams(params2);
        lineScoreHeader.addView(spacer);

        total = li.inflate(R.layout.inning_bold, null);
        inning = total.findViewById(R.id.inning);
        inning.setText("R");
        inning.setLayoutParams(params1);
        lineScoreHeader.addView(total);

        total = li.inflate(R.layout.inning_bold, null);
        inning = total.findViewById(R.id.inning);
        inning.setText("H");
        inning.setLayoutParams(params1);
        lineScoreHeader.addView(total);

        total = li.inflate(R.layout.inning_bold, null);
        inning = total.findViewById(R.id.inning);
        inning.setText("E");
        inning.setLayoutParams(params1);
        lineScoreHeader.addView(total);

        TextView lineScoreAwayTeamAbbr = view.findViewById(R.id.lineScoreAwayTeamAbbr);
        lineScoreAwayTeamAbbr.setText(gameSummary.getAwayTeamAbbr());

        for (int i = 0; i < gameSummary.getAwayLine().size() ; i++) {
            inn = li.inflate(R.layout.inning, null);
            inning = inn.findViewById(R.id.inning);
            inning.setText(String.valueOf(gameSummary.getAwayLine().get(i)));
            inning.setLayoutParams(params1);
            lineScoreAway.addView(inn);
        }

        spacer = li.inflate(R.layout.inning_spacer, null);
        spacer.setLayoutParams(params2);
        lineScoreAway.addView(spacer);
        
        total = li.inflate(R.layout.inning_bold, null);
        inning = total.findViewById(R.id.inning);
        inning.setText(String.valueOf(gameSummary.getAwayScore()));
        inning.setLayoutParams(params1);
        lineScoreAway.addView(total);

        total = li.inflate(R.layout.inning_bold, null);
        inning = total.findViewById(R.id.inning);
        inning.setText(String.valueOf(gameSummary.getAwayHits()));
        inning.setLayoutParams(params1);
        lineScoreAway.addView(total);

        total = li.inflate(R.layout.inning_bold, null);
        inning = total.findViewById(R.id.inning);
        inning.setText(String.valueOf(gameSummary.getAwayErrors()));
        inning.setLayoutParams(params1);
        lineScoreAway.addView(total);

        TextView lineScoreHomeTeamAbbr = view.findViewById(R.id.lineScoreHomeTeamAbbr);
        lineScoreHomeTeamAbbr.setText(gameSummary.getHomeTeamAbbr());

        for (int i = 0; i < gameSummary.getHomeLine().size() ; i++) {
            inn = li.inflate(R.layout.inning, null);
            inning = inn.findViewById(R.id.inning);
            inning.setText(String.valueOf(gameSummary.getHomeLine().get(i)));
            inning.setLayoutParams(params1);
            lineScoreHome.addView(inn);
        }

        spacer = li.inflate(R.layout.inning_spacer, null);
        spacer.setLayoutParams(params2);
        lineScoreHome.addView(spacer);

        total = li.inflate(R.layout.inning_bold, null);
        inning = total.findViewById(R.id.inning);
        inning.setText(String.valueOf(gameSummary.getHomeScore()));
        inning.setLayoutParams(params1);
        lineScoreHome.addView(total);

        total = li.inflate(R.layout.inning_bold, null);
        inning = total.findViewById(R.id.inning);
        inning.setText(String.valueOf(gameSummary.getHomeHits()));
        inning.setLayoutParams(params1);
        lineScoreHome.addView(total);

        total = li.inflate(R.layout.inning_bold, null);
        inning = total.findViewById(R.id.inning);
        inning.setText(String.valueOf(gameSummary.getHomeErrors()));
        inning.setLayoutParams(params1);
        lineScoreHome.addView(total);
    }

    private class populateLines extends AsyncTask<BoxScoreWrapper, Void, JsoupBoxScore> {

        private AlertDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LayoutInflater li = LayoutInflater.from(mContext);
            final View downloadView = li.inflate(R.layout.get_leaders_progress, null);
            TextView message = downloadView.findViewById(R.id.headerLabel);
            message.setText("Getting box score...");
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setView(downloadView);
            dialog = builder.create();
            dialog.show();
        }

        @Override
        protected JsoupBoxScore doInBackground(BoxScoreWrapper... params) {
            BoxScoreWrapper bsw = params[0];
            Response response = null;
            JsoupBoxScore jsoupBoxScore = new JsoupBoxScore();
            jsoupBoxScore.setJsoup(false);
            if (isConnected(mContext)) {
                try {
                    String boxScoreURL = html_root + "news/html/box_scores/game_box_"+bsw.gameID+".html";
                    URL url = new URL(boxScoreURL);
                    OkHttpClient.Builder b = new OkHttpClient.Builder();
                    b.connectTimeout(2000, TimeUnit.MILLISECONDS);
                    OkHttpClient httpClient = b.build();
                    Call call = httpClient.newCall(new Request.Builder()
                            .url(url)
                            .addHeader("Accept-Encoding", "identity")
                            .get()
                            .build());
                    response = call.execute();
                    if (response.code() == 200) {
                        Document document = Jsoup.parse(new URL(boxScoreURL).openStream(),
                                "ISO-8859-1", boxScoreURL);
                        Elements statLines = document.getElementsByClass("sortable");

                        Elements awayBattingRows = statLines.get(0).select("tr");
                        List<BattingLineScore> awayBattingLines = generateBattingJsoup(awayBattingRows);

                        Element awayBattingStuff = statLines.get(0).nextElementSibling();
                        BattingGameNotes awayBattingNotes = battingNotes(awayBattingStuff);

                        Elements homeBattingRows = statLines.get(1).select("tr");
                        List<BattingLineScore> homeBattingLines = generateBattingJsoup(homeBattingRows);

                        Element homeBattingStuff = statLines.get(1).nextElementSibling();
                        BattingGameNotes homeBattingNotes = battingNotes(homeBattingStuff);

                        Elements awayPitchingRows = statLines.get(2).select("tr");
                        List<PitchingLineScore> awayPitchingLines = generatePitchingJsoup(awayPitchingRows);

                        PitchingGameNotes awayPitchingNotes = pitchingNotes(statLines.get(2).nextElementSibling());

                        Elements homePitchingRows = statLines.get(3).select("tr");
                        List<PitchingLineScore> homePitchingLines = generatePitchingJsoup(homePitchingRows);

                        PitchingGameNotes homePitchingNotes = pitchingNotes(statLines.get(3).nextElementSibling());
                        Elements noteStuff = document.getElementsByClass("databg");
                        GameNotes gameNotes = gameNotes(noteStuff.get(4));

                        jsoupBoxScore.setAwayBattingLines(awayBattingLines);
                        jsoupBoxScore.setHomeBattingLines(homeBattingLines);
                        jsoupBoxScore.setAwayPitchingLines(awayPitchingLines);
                        jsoupBoxScore.setHomePitchingLines(homePitchingLines);
                        jsoupBoxScore.setAwayBattingNotes(awayBattingNotes);
                        jsoupBoxScore.setHomeBattingNotes(homeBattingNotes);
                        jsoupBoxScore.setAwayPitchingNotes(awayPitchingNotes);
                        jsoupBoxScore.setHomePitchingNotes(homePitchingNotes);
                        jsoupBoxScore.setGameNotes(gameNotes);
                        jsoupBoxScore.setAwayAbbr(bsw.awayAbbr);
                        jsoupBoxScore.setHomeAbbr(bsw.homeAbbr);
                        jsoupBoxScore.setJsoup(true);
                    } else {
                        jsoupBoxScore = getCSVBox();
                        jsoupBoxScore.setJsoup(false);
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    jsoupBoxScore = getCSVBox();
                    headerTabLayout.setVisibility(View.GONE);
                } catch (IOException e) {
                    e.printStackTrace();
                    jsoupBoxScore = getCSVBox();
                    headerTabLayout.setVisibility(View.GONE);
                } finally {
                    if (response != null) {
                        response.close();
                    }
                }

                try {
                    String gameLogURL = html_root + "news/html/game_logs/log_" + bsw.gameID + ".html";
                    URL url = new URL(gameLogURL);
                    OkHttpClient.Builder b = new OkHttpClient.Builder();
                    b.connectTimeout(2000, TimeUnit.MILLISECONDS);
                    OkHttpClient httpClient = b.build();
                    Call call = httpClient.newCall(new Request.Builder()
                            .url(url)
                            .addHeader("Accept-Encoding", "identity")
                            .get()
                            .build());
                    response = call.execute();
                    if (response.code() == 200) {
                        headerTabLayout.setVisibility(View.VISIBLE);
                        List<GameLogInning> gameLog = new ArrayList<GameLogInning>();
                        Document document = Jsoup.parse(new URL(gameLogURL).openStream(),
                                "ISO-8859-1", gameLogURL);
                        Elements halves = document.getElementsByClass("data");

                        for (Element half : halves) {
                            GameLogInning inning = new GameLogInning();
                            Elements rows = half.select("tr");
                            inning.setHeader(rows.first().text());
                            inning.setSubHeader(rows.get(1).text().replace(" - ", "\n"));
                            inning.setFooter(rows.last().text().replace("; ", "\n"));
                            List<Map<String, String>> playerRows = new ArrayList<Map<String, String>>();
                            for (int i = 2; i < rows.size() - 1; i++) {
                                Map<String, String> playerRow = new HashMap<String, String>();
                                Elements cells = rows.get(i).select("td");
                                String result = Jsoup.clean(cells.get(1).html(), "", Whitelist.none().addTags("br"), new Document.OutputSettings().prettyPrint(true));
                                playerRow.put(cells.get(0).text(), result.replace("<br>", "\n").replace("\n\n", "\n"));
                                playerRows.add(playerRow);

                            }
                            inning.setPlayerLines(playerRows);
                            gameLog.add(inning);
                        }
                        jsoupBoxScore.setGameLog(gameLog);
                    } else {
                        headerTabLayout.setVisibility(View.GONE);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    headerTabLayout.setVisibility(View.GONE);
                } catch (IOException e) {
                    e.printStackTrace();
                    headerTabLayout.setVisibility(View.GONE);
                } finally {
                    if (response != null) {
                        response.close();
                    }
                }
            } else {
                jsoupBoxScore = getCSVBox();
                jsoupBoxScore.setJsoup(false);
            }

            return jsoupBoxScore;
        }

        protected void onPostExecute(JsoupBoxScore jsoupBoxScore) {
            if (jsoupBoxScore.isJsoup()) {
                List<BattingLineScore> awayBattingLines = jsoupBoxScore.getAwayBattingLines();
                List<BattingLineScore> homeBattingLines = jsoupBoxScore.getHomeBattingLines();
                List<PitchingLineScore> awayPitchingLines = jsoupBoxScore.getAwayPitchingLines();
                List<PitchingLineScore> homePitchingLines = jsoupBoxScore.getHomePitchingLines();
                BattingGameNotes awayBattingNotes = jsoupBoxScore.getAwayBattingNotes();
                BattingGameNotes homeBattingNotes = jsoupBoxScore.getHomeBattingNotes();
                PitchingGameNotes awayPitchingNotes = jsoupBoxScore.getAwayPitchingNotes();
                PitchingGameNotes homePitchingNotes = jsoupBoxScore.getHomePitchingNotes();
                GameNotes gameNotes = jsoupBoxScore.getGameNotes();
                String awayAbbr = jsoupBoxScore.getAwayAbbr();
                String homeAbbr = jsoupBoxScore.getHomeAbbr();

                LinearLayout awayTeamBatting = view.findViewById(R.id.awayTeamBatting);
                populateBattingJsoup(awayTeamBatting, awayBattingLines, awayAbbr);

                if (awayBattingNotes.getSubs() != null) {
                    LinearLayout awayTeamSubstitutions = view.findViewById(R.id.awayTeamSubstitutions);
                    awayTeamSubstitutions.setVisibility(View.VISIBLE);
                    populateBattingSubstitionsJsoup(awayTeamSubstitutions, awayBattingNotes.getSubs());
                }

                LinearLayout awayTeamBattingNotes = view.findViewById(R.id.awayTeamBattingNotes);
                populateBattingNotesJsoup(awayTeamBattingNotes, awayBattingNotes);

                if (awayBattingNotes.getSb() != null || awayBattingNotes.getCs() != null) {
                    LinearLayout awayTeamBaserunningNotes = view.findViewById(R.id.awayTeamBaserunningNotes);
                    awayTeamBaserunningNotes.setVisibility(View.VISIBLE);
                    populateBaserunningNotesJsoup(awayTeamBaserunningNotes, awayBattingNotes);
                }

                if (awayBattingNotes.getErrors() != null || awayBattingNotes.getDoublePlays() != null) {
                    LinearLayout awayTeamFieldingNotes = view.findViewById(R.id.awayTeamFieldingNotes);
                    awayTeamFieldingNotes.setVisibility(View.VISIBLE);
                    populateFieldingNotesJsoup(awayTeamFieldingNotes, awayBattingNotes);
                }

                LinearLayout awayTeamPitching = view.findViewById(R.id.awayTeamPitching);
                populatePitchingJsoup(awayTeamPitching, awayPitchingLines, awayAbbr);

                LinearLayout awayTeamPitchingNotes = view.findViewById(R.id.awayTeamPitchingNotes);
                populatePitchingNotesJsoup(awayTeamPitchingNotes, awayPitchingNotes);

                populateGameNotesJsoup(gameNotes);

                if (homeBattingNotes.getSubs() != null) {
                    LinearLayout homeTeamSubstitutions = view.findViewById(R.id.homeTeamSubstitutions);
                    homeTeamSubstitutions.setVisibility(View.VISIBLE);
                    populateBattingSubstitionsJsoup(homeTeamSubstitutions, homeBattingNotes.getSubs());
                }

                LinearLayout homeTeamBatting = view.findViewById(R.id.homeTeamBatting);
                populateBattingJsoup(homeTeamBatting, homeBattingLines, homeAbbr);

                LinearLayout homeTeamBattingNotes = view.findViewById(R.id.homeTeamBattingNotes);
                populateBattingNotesJsoup(homeTeamBattingNotes, homeBattingNotes);

                if (homeBattingNotes.getSb() != null || homeBattingNotes.getCs() != null) {
                    LinearLayout homeTeamBaserunningNotes = view.findViewById(R.id.homeTeamBaserunningNotes);
                    homeTeamBaserunningNotes.setVisibility(View.VISIBLE);
                    populateBaserunningNotesJsoup(homeTeamBaserunningNotes, homeBattingNotes);
                }

                if (homeBattingNotes.getErrors() != null || homeBattingNotes.getDoublePlays() != null) {
                    LinearLayout homeTeamFieldingNotes = view.findViewById(R.id.homeTeamFieldingNotes);
                    homeTeamFieldingNotes.setVisibility(View.VISIBLE);
                    populateFieldingNotesJsoup(homeTeamFieldingNotes, homeBattingNotes);
                }

                LinearLayout homeTeamPitching = view.findViewById(R.id.homeTeamPitching);
                populatePitchingJsoup(homeTeamPitching, homePitchingLines, homeAbbr);

                LinearLayout homeTeamPitchingNotes = view.findViewById(R.id.homeTeamPitchingNotes);
                populatePitchingNotesJsoup(homeTeamPitchingNotes, homePitchingNotes);
            } else {
                List<BattingLineScore> awayBattingLines = jsoupBoxScore.getAwayBattingLines();
                List<BattingLineScore> homeBattingLines = jsoupBoxScore.getHomeBattingLines();
                List<PitchingLineScore> awayPitchingLines = jsoupBoxScore.getAwayPitchingLines();
                List<PitchingLineScore> homePitchingLines = jsoupBoxScore.getHomePitchingLines();
                GameNotes gameNotes = jsoupBoxScore.getGameNotes();
                String awayAbbr = jsoupBoxScore.getAwayAbbr();
                String homeAbbr = jsoupBoxScore.getHomeAbbr();

                LinearLayout awayTeamBatting = view.findViewById(R.id.awayTeamBatting);
                LinearLayout awayTeamBattingNotes = view.findViewById(R.id.awayTeamBattingNotes);
                populateBattingCSV(awayTeamBatting, awayTeamBattingNotes, awayBattingLines, awayAbbr);

                LinearLayout awayTeamPitching = view.findViewById(R.id.awayTeamPitching);
                LinearLayout awayTeamPitchingNotes = view.findViewById(R.id.awayTeamPitchingNotes);
                populatePitchingCSV(awayTeamPitching, awayTeamPitchingNotes, awayPitchingLines, jsoupBoxScore.getAwayAbbr(), gameSummary.getWinningPitcherLine(),
                        gameSummary.getLosingPitcherLine(), gameSummary.getSavingPitcherLine());

                LinearLayout homeTeamBatting = view.findViewById(R.id.homeTeamBatting);
                LinearLayout homeTeamBattingNotes = view.findViewById(R.id.homeTeamBattingNotes);
                populateBattingCSV(homeTeamBatting, homeTeamBattingNotes, homeBattingLines, homeAbbr);

                LinearLayout homeTeamPitching = view.findViewById(R.id.homeTeamPitching);
                LinearLayout homeTeamPitchingNotes = view.findViewById(R.id.homeTeamPitchingNotes);
                populatePitchingCSV(homeTeamPitching, homeTeamPitchingNotes, homePitchingLines, jsoupBoxScore.getHomeAbbr(), gameSummary.getWinningPitcherLine(),
                        gameSummary.getLosingPitcherLine(), gameSummary.getSavingPitcherLine());

                populateGameNotesJsoup(gameNotes);
            }

            if (jsoupBoxScore.getGameLog() != null) {
                List<GameLogInning> gameLog = jsoupBoxScore.getGameLog();
                LinearLayout gameLogParent = view.findViewById(R.id.gameLogLayout);
                for (GameLogInning inning : gameLog) {
                    View inningLayout = li.inflate(R.layout.game_log_half_inning, null);
                    TextView inningHeader = inningLayout.findViewById(R.id.inningHeader);
                    inningHeader.setText(inning.getHeader());

                    TextView inningSubHeader = inningLayout.findViewById(R.id.inningSubHeader);
                    inningSubHeader.setText(inning.getSubHeader());

                    TextView inningFooter = inningLayout.findViewById(R.id.inningFooter);
                    inningFooter.setText(inning.getFooter());

                    LinearLayout inningRowContainer = inningLayout.findViewById(R.id.inningRowContainer);

                    List<Map<String, String>> playerRows = inning.getPlayerLines();
                    for (Map<String, String> playerRow : playerRows) {
                        View line = li.inflate(R.layout.game_log_line, null);
                        for (String key : playerRow.keySet()) {
                            TextView personInfo = line.findViewById(R.id.personInfo);
                            personInfo.setText(key);
                            TextView atBatLog = line.findViewById(R.id.atBatLog);
                            atBatLog.setText(playerRow.get(key));
                            inningRowContainer.addView(line);
                            View divider = li.inflate(R.layout.horizontal_line, null);
                            inningRowContainer.addView(divider);
                        }
                    }

                    gameLogParent.addView(inningLayout);
                }
                System.out.println("FOUND A GAME LOG");
            }

            RelativeLayout boxScoreParent = view.findViewById(R.id.boxScoreParent);
            boxScoreParent.setVisibility(View.VISIBLE);
            dialog.dismiss();
        }
    }

    private JsoupBoxScore getCSVBox() {
        JsoupBoxScore jsoupBoxScore = new JsoupBoxScore();
        List<BattingLineScore> awayBattingLines = db.battingLineScores(universe_name, gameSummary.getGameId(),
                gameSummary.getAwayTeamId(), league_id, gameSummary.getGameType(), gameSummary.getDate());
        List<PitchingLineScore> awayPitchingLines = db.pitchingLineScores(universe_name, gameSummary.getGameId(), gameSummary.getAwayTeamId(),
                league_id, gameSummary.getGameType(), gameSummary.getDate());
        List<BattingLineScore> homeBattingLines = db.battingLineScores(universe_name, gameSummary.getGameId(),
                gameSummary.getHomeTeamId(), league_id, gameSummary.getGameType(), gameSummary.getDate());
        List<PitchingLineScore> homePitchingLines = db.pitchingLineScores(universe_name, gameSummary.getGameId(), gameSummary.getHomeTeamId(),
                league_id, gameSummary.getGameType(), gameSummary.getDate());

        GameNotes gameNotes = new GameNotes();

        String formattedTime;
        try {
            SimpleDateFormat time = new SimpleDateFormat("hhmm");
            SimpleDateFormat localTime = new SimpleDateFormat("h:mm a");
            Date d1 = time.parse(gameSummary.getTime());
            formattedTime = localTime.format(d1);
        } catch (ParseException e) {
            formattedTime = gameSummary.getTime();
        }
        gameNotes.setStartTime(formattedTime);
        gameNotes.setAttendance(String.valueOf(gameSummary.getAttendance()));
        gameNotes.setBallpark(gameSummary.getPark());

        jsoupBoxScore.setAwayBattingLines(awayBattingLines);
        jsoupBoxScore.setHomeBattingLines(homeBattingLines);
        jsoupBoxScore.setAwayPitchingLines(awayPitchingLines);
        jsoupBoxScore.setHomePitchingLines(homePitchingLines);
        jsoupBoxScore.setGameNotes(gameNotes);
        jsoupBoxScore.setAwayAbbr(gameSummary.getAwayTeamAbbr());
        jsoupBoxScore.setHomeAbbr(gameSummary.getHomeTeamAbbr());
        jsoupBoxScore.setJsoup(false);

        return jsoupBoxScore;
    }

    private List<BattingLineScore> generateBattingJsoup(Elements el) {
        Whitelist wl = new Whitelist().none();
        List<BattingLineScore> bls = new ArrayList<BattingLineScore>();
        for (Element row : el) {
            Elements cells = row.select("th,td");
            BattingLineScore line = new BattingLineScore();
            line.setPlayerName(Jsoup.clean(cells.get(0).html(), wl).replace("&nbsp;",""));
            line.setAb(Jsoup.clean(cells.get(1).text(), wl));
            line.setR(Jsoup.clean(cells.get(2).text(), wl));
            line.setH(Jsoup.clean(cells.get(3).text(), wl));
            line.setRbi(Jsoup.clean(cells.get(4).text(), wl));
            line.setBb(Jsoup.clean(cells.get(5).text(), wl));
            line.setK(Jsoup.clean(cells.get(6).text(), wl));
            line.setAvg(Jsoup.clean(cells.get(8).text(), wl));
            bls.add(line);
        }
        return bls;
    }

    private List<PitchingLineScore> generatePitchingJsoup(Elements el) {
        Whitelist wl = new Whitelist().none();
        List<PitchingLineScore> pls = new ArrayList<PitchingLineScore>();
        int outs = 0;
        int ip = 0;
        int h = 0;
        int r = 0;
        int er = 0;
        int bb = 0;
        int k = 0;
        int pi = 0;
        int hr = 0;
        for (Element row : el) {
            Elements cells = row.select("th,td");
            PitchingLineScore line = new PitchingLineScore();
            line.setPlayerName(Jsoup.clean(cells.get(0).text(), wl));
            line.setIp(Jsoup.clean(cells.get(1).text(), wl));
            line.setH(Jsoup.clean(cells.get(2).text(), wl));
            line.setR(Jsoup.clean(cells.get(3).text(), wl));
            line.setEr(Jsoup.clean(cells.get(4).text(), wl));
            line.setBb(Jsoup.clean(cells.get(5).text(), wl));
            line.setK(Jsoup.clean(cells.get(6).text(), wl));
            line.setHr(Jsoup.clean(cells.get(7).text(), wl));
            line.setPi(Jsoup.clean(cells.get(8).text(), wl));
            line.setEra(Jsoup.clean(cells.get(10).text(), wl));
            if (!Jsoup.clean(cells.get(0).text(), wl).equals("Player")) {
                outs += ipToOuts(Jsoup.clean(cells.get(1).text(), wl));
                h += Integer.parseInt(Jsoup.clean(cells.get(2).text(), wl));
                r += Integer.parseInt(Jsoup.clean(cells.get(3).text(), wl));
                er += Integer.parseInt(Jsoup.clean(cells.get(4).text(), wl));
                bb += Integer.parseInt(Jsoup.clean(cells.get(5).text(), wl));
                k += Integer.parseInt(Jsoup.clean(cells.get(6).text(), wl));
                hr += Integer.parseInt(Jsoup.clean(cells.get(7).text(), wl));
                pi += Integer.parseInt(Jsoup.clean(cells.get(8).text(), wl));
            }
            pls.add(line);
        }
        PitchingLineScore totals = new PitchingLineScore();
        totals.setPlayerName("TOTAL");
        totals.setIp(String.valueOf(outsToInnings(outs)));
        totals.setH(String.valueOf(h));
        totals.setR(String.valueOf(r));
        totals.setEr(String.valueOf(er));
        totals.setBb(String.valueOf(bb));
        totals.setK(String.valueOf(k));
        totals.setPi(String.valueOf(pi));
        totals.setHr(String.valueOf(hr));
        totals.setEra("");
        pls.add(totals);
        return pls;
    }

    private void populatePitchingJsoup(LinearLayout parent, List<PitchingLineScore> lines, String abbr) {
        rowCount = 1;
        for (PitchingLineScore line : lines) {
            
            View row = li.inflate(R.layout.pitching_line_layout, null);

            if (line.getPlayerName().equals("Player") || line.getPlayerName().equals("TOTAL")) {
                row.setBackgroundColor(getResources().getColor(R.color.retired));
            } else {
                if (rowCount % 2 == 0) {
                    row.setBackgroundColor(mContext.getResources().getColor(R.color.awayGame));
                }
            }

            TextView playerInfo = row.findViewById(R.id.playerInfo);
            playerInfo.setText(line.getPlayerName());

            if (line.getPlayerName().equals("Player")) {
                playerInfo.setText(abbr + " PITCHERS");
            }

            TextView ip = row.findViewById(R.id.ip);
            ip.setText(line.getIp());

            TextView hits = row.findViewById(R.id.h);
            hits.setText(line.getH());

            TextView runs = row.findViewById(R.id.r);
            runs.setText(line.getR());

            TextView earnedRuns = row.findViewById(R.id.er);
            earnedRuns.setText(line.getEr());

            TextView walks = row.findViewById(R.id.walks);
            walks.setText(line.getBb());

            TextView strikeouts = row.findViewById(R.id.strikeouts);
            strikeouts.setText(line.getK());

            TextView hr = row.findViewById(R.id.hr);
            hr.setText(line.getHr());

            TextView era = row.findViewById(R.id.era);
            era.setText(line.getEra());

            parent.addView(row);

            rowCount++;
        }
    }
    
    private void populateBattingJsoup(LinearLayout parent, List<BattingLineScore> lines, String abbr) {
        rowCount = 1;
        for (BattingLineScore line : lines) {
            View row = li.inflate(R.layout.batting_line_layout, null);

            if (line.getPlayerName().equals("Player") || line.getPlayerName().equals("Totals")) {
                row.setBackgroundColor(getResources().getColor(R.color.retired));
            } else {
                if (rowCount % 2 == 0) {
                    row.setBackgroundColor(mContext.getResources().getColor(R.color.awayGame));
                }
            }

            TextView playerInfo = row.findViewById(R.id.playerInfo);
            if (isStarter(line.getPlayerName())) {
                playerInfo.setText(line.getPlayerName());
            } else {
                playerInfo.setText("\t" + line.getPlayerName());
            }

            if (line.getPlayerName().equals("Player")) {
                playerInfo.setText(abbr + " BATTERS");
            }

            if (line.getPlayerName().equals("Totals")) {
                playerInfo.setText("TOTAL");
            }

            TextView atBats = row.findViewById(R.id.atBats);
            atBats.setText(line.getAb());

            TextView runs = row.findViewById(R.id.runs);
            runs.setText(line.getR());

            TextView hits = row.findViewById(R.id.hits);
            hits.setText(line.getH());

            TextView rbi = row.findViewById(R.id.rbi);
            rbi.setText(line.getRbi());

            TextView walks = row.findViewById(R.id.walks);
            walks.setText(line.getBb());

            TextView strikeouts = row.findViewById(R.id.strikeouts);
            strikeouts.setText(line.getK());

            TextView avg = row.findViewById(R.id.avg);
            avg.setText(line.getAvg());

            parent.addView(row);

            rowCount++;
        }
    }

    private void populateBattingSubstitionsJsoup(LinearLayout parent, List<String> subs) {
        for (String sub : subs) {
            View noteLine = li.inflate(R.layout.box_score_footer_layout, null);
            TextView list = noteLine.findViewById(R.id.list);
            list.setText(sub);
            parent.addView(noteLine);
        }
    }

    private void populateBattingNotesJsoup(LinearLayout parent, BattingGameNotes notes) {
        SpannableStringBuilder sBuilder = new SpannableStringBuilder();
        String thing;
        if (notes.getDoubles() != null) {
            View noteLine = li.inflate(R.layout.box_score_footer_layout, null);
            TextView list = noteLine.findViewById(R.id.list);
            sBuilder.clear();
            thing = "Doubles: ";
            sBuilder.append(thing);
            sBuilder.append(notes.getDoubles());
            CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getResources().getAssets(), "fonts/OpenSans-Bold.ttf"));
            sBuilder.setSpan(typefaceSpan, 0, thing.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            list.setText(sBuilder, TextView.BufferType.SPANNABLE);
            parent.addView(noteLine);
        }

        if (notes.getTriples() != null) {
            View noteLine = li.inflate(R.layout.box_score_footer_layout, null);
            TextView list = noteLine.findViewById(R.id.list);
            sBuilder.clear();
            thing = "Triples: ";
            sBuilder.append(thing);
            sBuilder.append(notes.getTriples());
            CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getResources().getAssets(), "fonts/OpenSans-Bold.ttf"));
            sBuilder.setSpan(typefaceSpan, 0, thing.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            list.setText(sBuilder, TextView.BufferType.SPANNABLE);
            parent.addView(noteLine);
        }

        if (notes.getHomeRuns() != null) {
            View noteLine = li.inflate(R.layout.box_score_footer_layout, null);
            TextView list = noteLine.findViewById(R.id.list);
            sBuilder.clear();
            thing = "Home Runs: ";
            sBuilder.append(thing);
            sBuilder.append(notes.getHomeRuns());
            CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getResources().getAssets(), "fonts/OpenSans-Bold.ttf"));
            sBuilder.setSpan(typefaceSpan, 0, thing.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            list.setText(sBuilder, TextView.BufferType.SPANNABLE);
            parent.addView(noteLine);
        }

        if (notes.getTotalBases() != null) {
            View noteLine = li.inflate(R.layout.box_score_footer_layout, null);
            TextView list = noteLine.findViewById(R.id.list);
            sBuilder.clear();
            thing = "Total Bases: ";
            sBuilder.append(thing);
            sBuilder.append(notes.getTotalBases());
            CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getResources().getAssets(), "fonts/OpenSans-Bold.ttf"));
            sBuilder.setSpan(typefaceSpan, 0, thing.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            list.setText(sBuilder, TextView.BufferType.SPANNABLE);
            parent.addView(noteLine);
        }

        if (notes.getTwoOutRBI() != null) {
            View noteLine = li.inflate(R.layout.box_score_footer_layout, null);
            TextView list = noteLine.findViewById(R.id.list);
            sBuilder.clear();
            thing = "2-out RBI: ";
            sBuilder.append(thing);
            sBuilder.append(notes.getTwoOutRBI());
            CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getResources().getAssets(), "fonts/OpenSans-Bold.ttf"));
            sBuilder.setSpan(typefaceSpan, 0, thing.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            list.setText(sBuilder, TextView.BufferType.SPANNABLE);
            parent.addView(noteLine);
        }

        if (notes.getRisp2Outs() != null) {
            View noteLine = li.inflate(R.layout.box_score_footer_layout, null);
            TextView list = noteLine.findViewById(R.id.list);
            sBuilder.clear();
            thing = "Runners left in scoring position, 2 outs: ";
            sBuilder.append(thing);
            sBuilder.append(notes.getRisp2Outs());
            CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getResources().getAssets(), "fonts/OpenSans-Bold.ttf"));
            sBuilder.setSpan(typefaceSpan, 0, thing.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            list.setText(sBuilder, TextView.BufferType.SPANNABLE);
            parent.addView(noteLine);
        }

        if (notes.getGidp() != null) {
            View noteLine = li.inflate(R.layout.box_score_footer_layout, null);
            TextView list = noteLine.findViewById(R.id.list);
            sBuilder.clear();
            thing = "GIDP: ";
            sBuilder.append(thing);
            sBuilder.append(notes.getGidp());
            CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getResources().getAssets(), "fonts/OpenSans-Bold.ttf"));
            sBuilder.setSpan(typefaceSpan, 0, thing.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            list.setText(sBuilder, TextView.BufferType.SPANNABLE);
            parent.addView(noteLine);
        }

        if (notes.getSacBunt() != null) {
            View noteLine = li.inflate(R.layout.box_score_footer_layout, null);
            TextView list = noteLine.findViewById(R.id.list);
            sBuilder.clear();
            thing = "Sac Bunt: ";
            sBuilder.append(thing);
            sBuilder.append(notes.getSacBunt());
            CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getResources().getAssets(), "fonts/OpenSans-Bold.ttf"));
            sBuilder.setSpan(typefaceSpan, 0, thing.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            list.setText(sBuilder, TextView.BufferType.SPANNABLE);
            parent.addView(noteLine);
        }

        if (notes.getSacFly() != null) {
            View noteLine = li.inflate(R.layout.box_score_footer_layout, null);
            TextView list = noteLine.findViewById(R.id.list);
            sBuilder.clear();
            thing = "Sac Fly: ";
            sBuilder.append(thing);
            sBuilder.append(notes.getSacFly());
            CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getResources().getAssets(), "fonts/OpenSans-Bold.ttf"));
            sBuilder.setSpan(typefaceSpan, 0, thing.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            list.setText(sBuilder, TextView.BufferType.SPANNABLE);
            parent.addView(noteLine);
        }

        if (notes.getHBP() != null) {
            View noteLine = li.inflate(R.layout.box_score_footer_layout, null);
            TextView list = noteLine.findViewById(R.id.list);
            sBuilder.clear();
            thing = "Hit By Pitch: ";
            sBuilder.append(thing);
            sBuilder.append(notes.getHBP());
            CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getResources().getAssets(), "fonts/OpenSans-Bold.ttf"));
            sBuilder.setSpan(typefaceSpan, 0, thing.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            list.setText(sBuilder, TextView.BufferType.SPANNABLE);
            parent.addView(noteLine);
        }
    }

    private void populateBaserunningNotesJsoup(LinearLayout parent, BattingGameNotes notes) {
        SpannableStringBuilder sBuilder = new SpannableStringBuilder();
        String thing;
        if (notes.getSb() != null) {
            View noteLine = li.inflate(R.layout.box_score_footer_layout, null);
            TextView list = noteLine.findViewById(R.id.list);
            sBuilder.clear();
            thing = "SB: ";
            sBuilder.append(thing);
            sBuilder.append(notes.getSb());
            CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getResources().getAssets(), "fonts/OpenSans-Bold.ttf"));
            sBuilder.setSpan(typefaceSpan, 0, thing.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            list.setText(sBuilder, TextView.BufferType.SPANNABLE);
            parent.addView(noteLine);
        }

        if (notes.getCs() != null) {
            View noteLine = li.inflate(R.layout.box_score_footer_layout, null);
            TextView list = noteLine.findViewById(R.id.list);
            sBuilder.clear();
            thing = "CS ";
            sBuilder.append(thing);
            sBuilder.append(notes.getCs());
            CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getResources().getAssets(), "fonts/OpenSans-Bold.ttf"));
            sBuilder.setSpan(typefaceSpan, 0, thing.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            list.setText(sBuilder, TextView.BufferType.SPANNABLE);
            parent.addView(noteLine);
        }
    }

    private void populateFieldingNotesJsoup(LinearLayout parent, BattingGameNotes notes) {
        SpannableStringBuilder sBuilder = new SpannableStringBuilder();
        String thing;
        if (notes.getErrors() != null) {
            View noteLine = li.inflate(R.layout.box_score_footer_layout, null);
            TextView list = noteLine.findViewById(R.id.list);
            sBuilder.clear();
            thing = "Errors: ";
            sBuilder.append(thing);
            sBuilder.append(notes.getErrors());
            CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getResources().getAssets(), "fonts/OpenSans-Bold.ttf"));
            sBuilder.setSpan(typefaceSpan, 0, thing.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            list.setText(sBuilder, TextView.BufferType.SPANNABLE);
            parent.addView(noteLine);
        }

        if (notes.getDoublePlays() != null) {
            View noteLine = li.inflate(R.layout.box_score_footer_layout, null);
            TextView list = noteLine.findViewById(R.id.list);
            sBuilder.clear();
            thing = "Double Plays: ";
            sBuilder.append(thing);
            sBuilder.append(notes.getDoublePlays());
            CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getResources().getAssets(), "fonts/OpenSans-Bold.ttf"));
            sBuilder.setSpan(typefaceSpan, 0, thing.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            list.setText(sBuilder, TextView.BufferType.SPANNABLE);
            parent.addView(noteLine);
        }
    }
    
    private void populatePitchingNotesJsoup(LinearLayout parent, PitchingGameNotes notes) {
        SpannableStringBuilder sBuilder = new SpannableStringBuilder();
        String thing;
        if (notes.getGameScore() != null) {
            View noteLine = li.inflate(R.layout.box_score_footer_layout, null);
            TextView list = noteLine.findViewById(R.id.list);
            sBuilder.clear();
            thing = "Game Score: ";
            sBuilder.append(thing);
            sBuilder.append(notes.getGameScore());
            CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getResources().getAssets(), "fonts/OpenSans-Bold.ttf"));
            sBuilder.setSpan(typefaceSpan, 0, thing.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            list.setText(sBuilder, TextView.BufferType.SPANNABLE);
            parent.addView(noteLine);
        }

        if (notes.getBattersFaced() != null) {
            View noteLine = li.inflate(R.layout.box_score_footer_layout, null);
            TextView list = noteLine.findViewById(R.id.list);
            sBuilder.clear();
            thing = "Batters Faced: ";
            sBuilder.append(thing);
            sBuilder.append(notes.getBattersFaced());
            CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getResources().getAssets(), "fonts/OpenSans-Bold.ttf"));
            sBuilder.setSpan(typefaceSpan, 0, thing.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            list.setText(sBuilder, TextView.BufferType.SPANNABLE);
            parent.addView(noteLine);
        }

        if (notes.getGoFo() != null) {
            View noteLine = li.inflate(R.layout.box_score_footer_layout, null);
            TextView list = noteLine.findViewById(R.id.list);
            sBuilder.clear();
            thing = "Ground Outs - Fly Outs: ";
            sBuilder.append(thing);
            sBuilder.append(notes.getGoFo());
            CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getResources().getAssets(), "fonts/OpenSans-Bold.ttf"));
            sBuilder.setSpan(typefaceSpan, 0, thing.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            list.setText(sBuilder, TextView.BufferType.SPANNABLE);
            parent.addView(noteLine);
        }

        if (notes.getPitchesStrikes() != null) {
            View noteLine = li.inflate(R.layout.box_score_footer_layout, null);
            TextView list = noteLine.findViewById(R.id.list);
            sBuilder.clear();
            thing = "Pitches - Strikes: ";
            sBuilder.append(thing);
            sBuilder.append(notes.getPitchesStrikes());
            CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getResources().getAssets(), "fonts/OpenSans-Bold.ttf"));
            sBuilder.setSpan(typefaceSpan, 0, thing.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            list.setText(sBuilder, TextView.BufferType.SPANNABLE);
            parent.addView(noteLine);
        }

        if (notes.getIrScored() != null) {
            View noteLine = li.inflate(R.layout.box_score_footer_layout, null);
            TextView list = noteLine.findViewById(R.id.list);
            sBuilder.clear();
            thing = "Inherited Runners - Scored: ";
            sBuilder.append(thing);
            sBuilder.append(notes.getIrScored());
            CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getResources().getAssets(), "fonts/OpenSans-Bold.ttf"));
            sBuilder.setSpan(typefaceSpan, 0, thing.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            list.setText(sBuilder, TextView.BufferType.SPANNABLE);
            parent.addView(noteLine);
        }

        if (notes.getHitBatsmen() != null) {
            View noteLine = li.inflate(R.layout.box_score_footer_layout, null);
            TextView list = noteLine.findViewById(R.id.list);
            sBuilder.clear();
            thing = "Hit Batsmen: ";
            sBuilder.append(thing);
            sBuilder.append(notes.getHitBatsmen());
            CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getResources().getAssets(), "fonts/OpenSans-Bold.ttf"));
            sBuilder.setSpan(typefaceSpan, 0, thing.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            list.setText(sBuilder, TextView.BufferType.SPANNABLE);
            parent.addView(noteLine);
        }

        if (notes.getWildPitch() != null) {
            View noteLine = li.inflate(R.layout.box_score_footer_layout, null);
            TextView list = noteLine.findViewById(R.id.list);
            sBuilder.clear();
            thing = "Wild Pitches: ";
            sBuilder.append(thing);
            sBuilder.append(notes.getWildPitch());
            CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getResources().getAssets(), "fonts/OpenSans-Bold.ttf"));
            sBuilder.setSpan(typefaceSpan, 0, thing.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            list.setText(sBuilder, TextView.BufferType.SPANNABLE);
            parent.addView(noteLine);
        }

        if (notes.getBalk() != null) {
            View noteLine = li.inflate(R.layout.box_score_footer_layout, null);
            TextView list = noteLine.findViewById(R.id.list);
            sBuilder.clear();
            thing = "Balks: ";
            sBuilder.append(thing);
            sBuilder.append(notes.getBalk());
            CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getResources().getAssets(), "fonts/OpenSans-Bold.ttf"));
            sBuilder.setSpan(typefaceSpan, 0, thing.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            list.setText(sBuilder, TextView.BufferType.SPANNABLE);
            parent.addView(noteLine);
        }
    }

    private void populateGameNotesJsoup(GameNotes notes) {
        SpannableStringBuilder sBuilder = new SpannableStringBuilder();
        String thing;

        LinearLayout awayParent = view.findViewById(R.id.awayTeamGameNotes);
        LinearLayout homeParent = view.findViewById(R.id.homeTeamGameNotes);

        if (notes.getPotg() != null) {
            View noteLine = li.inflate(R.layout.box_score_footer_layout, null);
            View noteLine2 = li.inflate(R.layout.box_score_footer_layout, null);
            TextView list = noteLine.findViewById(R.id.list);
            TextView list2 = noteLine2.findViewById(R.id.list);
            sBuilder.clear();
            thing = "Player of the Game: ";
            sBuilder.append(thing);
            sBuilder.append(notes.getPotg());
            CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getResources().getAssets(), "fonts/OpenSans-Bold.ttf"));
            sBuilder.setSpan(typefaceSpan, 0, thing.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            list.setText(sBuilder, TextView.BufferType.SPANNABLE);
            list2.setText(sBuilder, TextView.BufferType.SPANNABLE);
            awayParent.addView(noteLine);
            homeParent.addView(noteLine2);
        }

        if (notes.getBallpark() != null) {
            View noteLine = li.inflate(R.layout.box_score_footer_layout, null);
            View noteLine2 = li.inflate(R.layout.box_score_footer_layout, null);
            TextView list = noteLine.findViewById(R.id.list);
            TextView list2 = noteLine2.findViewById(R.id.list);
            sBuilder.clear();
            thing = "Ballpark: ";
            sBuilder.append(thing);
            sBuilder.append(notes.getBallpark());
            CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getResources().getAssets(), "fonts/OpenSans-Bold.ttf"));
            sBuilder.setSpan(typefaceSpan, 0, thing.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            list.setText(sBuilder, TextView.BufferType.SPANNABLE);
            list2.setText(sBuilder, TextView.BufferType.SPANNABLE);
            awayParent.addView(noteLine);
            homeParent.addView(noteLine2);
        }

        if (notes.getWeather() != null) {
            View noteLine = li.inflate(R.layout.box_score_footer_layout, null);
            View noteLine2 = li.inflate(R.layout.box_score_footer_layout, null);
            TextView list = noteLine.findViewById(R.id.list);
            TextView list2 = noteLine2.findViewById(R.id.list);
            sBuilder.clear();
            thing = "Weather: ";
            sBuilder.append(thing);
            sBuilder.append(notes.getWeather());
            CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getResources().getAssets(), "fonts/OpenSans-Bold.ttf"));
            sBuilder.setSpan(typefaceSpan, 0, thing.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            list.setText(sBuilder, TextView.BufferType.SPANNABLE);
            list2.setText(sBuilder, TextView.BufferType.SPANNABLE);
            awayParent.addView(noteLine);
            homeParent.addView(noteLine2);
        }

        if (notes.getStartTime() != null) {
            View noteLine = li.inflate(R.layout.box_score_footer_layout, null);
            View noteLine2 = li.inflate(R.layout.box_score_footer_layout, null);
            TextView list = noteLine.findViewById(R.id.list);
            TextView list2 = noteLine2.findViewById(R.id.list);
            sBuilder.clear();
            thing = "Start Time: ";
            sBuilder.append(thing);
            sBuilder.append(notes.getStartTime());
            CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getResources().getAssets(), "fonts/OpenSans-Bold.ttf"));
            sBuilder.setSpan(typefaceSpan, 0, thing.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            list.setText(sBuilder, TextView.BufferType.SPANNABLE);
            list2.setText(sBuilder, TextView.BufferType.SPANNABLE);
            awayParent.addView(noteLine);
            homeParent.addView(noteLine2);
        }

        if (notes.getTime() != null) {
            View noteLine = li.inflate(R.layout.box_score_footer_layout, null);
            View noteLine2 = li.inflate(R.layout.box_score_footer_layout, null);
            TextView list = noteLine.findViewById(R.id.list);
            TextView list2 = noteLine2.findViewById(R.id.list);
            sBuilder.clear();
            thing = "Time: ";
            sBuilder.append(thing);
            sBuilder.append(notes.getTime());
            CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getResources().getAssets(), "fonts/OpenSans-Bold.ttf"));
            sBuilder.setSpan(typefaceSpan, 0, thing.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            list.setText(sBuilder, TextView.BufferType.SPANNABLE);
            list2.setText(sBuilder, TextView.BufferType.SPANNABLE);
            awayParent.addView(noteLine);
            homeParent.addView(noteLine2);
        }

        if (notes.getAttendance() != null) {
            View noteLine = li.inflate(R.layout.box_score_footer_layout, null);
            View noteLine2 = li.inflate(R.layout.box_score_footer_layout, null);
            TextView list = noteLine.findViewById(R.id.list);
            TextView list2 = noteLine2.findViewById(R.id.list);
            sBuilder.clear();
            thing = "Attendance: ";
            sBuilder.append(thing);
            NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
            sBuilder.append(numberFormat.format(Integer.parseInt(notes.getAttendance())));
            CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getResources().getAssets(), "fonts/OpenSans-Bold.ttf"));
            sBuilder.setSpan(typefaceSpan, 0, thing.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            list.setText(sBuilder, TextView.BufferType.SPANNABLE);
            list2.setText(sBuilder, TextView.BufferType.SPANNABLE);
            awayParent.addView(noteLine);
            homeParent.addView(noteLine2);
        }

        if (notes.getNotes() != null) {
            View noteLine = li.inflate(R.layout.box_score_footer_layout, null);
            View noteLine2 = li.inflate(R.layout.box_score_footer_layout, null);
            TextView list = noteLine.findViewById(R.id.list);
            TextView list2 = noteLine2.findViewById(R.id.list);
            sBuilder.clear();
            thing = "Special Notes: ";
            sBuilder.append(thing);
            sBuilder.append(notes.getNotes());
            CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getResources().getAssets(), "fonts/OpenSans-Bold.ttf"));
            sBuilder.setSpan(typefaceSpan, 0, thing.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            list.setText(sBuilder, TextView.BufferType.SPANNABLE);
            list2.setText(sBuilder, TextView.BufferType.SPANNABLE);
            awayParent.addView(noteLine);
            homeParent.addView(noteLine2);
        }
        
    }

    private void populateBattingCSV(LinearLayout parent, LinearLayout notes, List data, String teamAbbr) {
        LayoutInflater li = LayoutInflater.from(mContext);

        LinearLayout teamBatting = parent;
        View header = li.inflate(R.layout.batting_line_layout, null);
        header.setBackgroundColor(getResources().getColor(R.color.retired));
        TextView headerText = header.findViewById(R.id.playerInfo);
        headerText.setText(teamAbbr+" Batters");
        teamBatting.addView(header);
        List<BattingLineScore> battingLines = data;
        List doubles = new ArrayList<String>();
        List triples = new ArrayList<String>();
        List homeRuns = new ArrayList<String>();
        List totalBases = new ArrayList<String>();
        List gdp = new ArrayList<String>();
        List sac = new ArrayList<String>();
        List sacFly = new ArrayList<String>();
        List hbp = new ArrayList<String>();

        for (BattingLineScore line : battingLines) {

            if (Integer.valueOf(line.getD()) > 0 && !line.getFirstName().equals("TOTAL")) {
                //int cumulativeDoubles =
                String totalD = db.countingBattingStatToDate("d", universe_name, line.getPlayerID(), gameSummary.getGameType(), league_id, game_date);
                if (Integer.valueOf(line.getD()) == 1) {
                    doubles.add(line.getFirstName().substring(0, 1) + ". " + line.getLastName() + " ("+totalD+")");
                } else {
                    doubles.add(line.getFirstName().substring(0, 1) + ". " + line.getLastName()+" "+line.getD() + " ("+totalD+")");
                }
            }

            if (Integer.valueOf(line.getT()) > 0 && !line.getFirstName().equals("TOTAL")) {
                String totalT = db.countingBattingStatToDate("t", universe_name, line.getPlayerID(), gameSummary.getGameType(), league_id, game_date);
                if (Integer.valueOf(line.getT()) == 1) {
                    triples.add(line.getFirstName().substring(0, 1) + ". " + line.getLastName() + " ("+totalT+")");
                } else {
                    triples.add(line.getFirstName().substring(0, 1) + ". " + line.getLastName()+" "+line.getT() + " ("+totalT+")");
                }
            }

            if (Integer.valueOf(line.getHr()) > 0 && !line.getFirstName().equals("TOTAL")) {
                String totalHR = db.countingBattingStatToDate("hr", universe_name, line.getPlayerID(), gameSummary.getGameType(), league_id, game_date);
                if (Integer.valueOf(line.getHr()) == 1) {
                    homeRuns.add(line.getFirstName().substring(0, 1) + ". " + line.getLastName() + " ("+totalHR+")");
                } else {
                    homeRuns.add(line.getFirstName().substring(0, 1) + ". " + line.getLastName()+" "+line.getHr() + " ("+totalHR+")");
                }
            }

            if (Integer.valueOf(line.getTotalBases()) > 0 && !line.getFirstName().equals("TOTAL")) {
                if (Integer.valueOf(line.getTotalBases()) == 1) {
                    totalBases.add(line.getFirstName().substring(0, 1) + ". " + line.getLastName());
                } else {
                    totalBases.add(line.getFirstName().substring(0, 1) + ". " + line.getLastName()+" "+line.getTotalBases());
                }
            }

            if (Integer.valueOf(line.getGdp()) > 0 && !line.getFirstName().equals("TOTAL")) {
                if (Integer.valueOf(line.getGdp()) == 1) {
                    gdp.add(line.getFirstName().substring(0, 1) + ". " + line.getLastName());
                } else {
                    gdp.add(line.getFirstName().substring(0, 1) + ". " + line.getLastName()+" "+line.getGdp());
                }
            }

            if (Integer.valueOf(line.getSh()) > 0 && !line.getFirstName().equals("TOTAL")) {
                if (Integer.valueOf(line.getSh()) == 1) {
                    sac.add(line.getFirstName().substring(0, 1) + ". " + line.getLastName());
                } else {
                    sac.add(line.getFirstName().substring(0, 1) + ". " + line.getLastName()+" "+line.getSh());
                }
            }

            if (Integer.valueOf(line.getSf()) > 0 && !line.getFirstName().equals("TOTAL")) {
                if (Integer.valueOf(line.getSf()) == 1) {
                    sacFly.add(line.getFirstName().substring(0, 1) + ". " + line.getLastName());
                } else {
                    sacFly.add(line.getFirstName().substring(0, 1) + ". " + line.getLastName()+" "+line.getSf());
                }
            }

            if (Integer.valueOf(line.getHbp()) > 0 && !line.getFirstName().equals("TOTAL")) {
                if (Integer.valueOf(line.getHbp()) == 1) {
                    hbp.add(line.getFirstName().substring(0, 1) + ". " + line.getLastName());
                } else {
                    hbp.add(line.getFirstName().substring(0, 1) + ". " + line.getLastName()+" "+line.getHbp());
                }
            }

            View row = li.inflate(R.layout.batting_line_layout, null);
            if (rowCount % 2 == 0) {
                row.setBackgroundColor(mContext.getResources().getColor(R.color.awayGame));
            }
            TextView playerInfo = row.findViewById(R.id.playerInfo);
            if (line.getFirstName().equals("TOTAL")) {
                displayName = line.getFirstName();
                row.setBackgroundColor(mContext.getResources().getColor(R.color.retired));
            } else {
                if (Integer.parseInt(line.getPinch()) == 0) {
                    displayName = line.getFirstName().substring(0, 1) + ". " + line.getLastName() + ", " + line.getPosition();
                } else {
                    if (Integer.parseInt(line.getPa()) > 0) {
                        displayName = "\t" + line.getFirstName().substring(0, 1) + ". " + line.getLastName() + ", PH";
                    } else {
                        displayName = "\t" + line.getFirstName().substring(0, 1) + ". " + line.getLastName() + ", PR";
                    }
                }
            }
            playerInfo.setText(displayName);

            TextView atBats = row.findViewById(R.id.atBats);
            atBats.setText(line.getAb());

            TextView runs = row.findViewById(R.id.runs);
            runs.setText(line.getR());

            TextView hits = row.findViewById(R.id.hits);
            hits.setText(line.getH());

            TextView rbi = row.findViewById(R.id.rbi);
            rbi.setText(line.getRbi());

            TextView walks = row.findViewById(R.id.walks);
            walks.setText(line.getBb());

            TextView strikeouts = row.findViewById(R.id.strikeouts);
            strikeouts.setText(line.getK());

            TextView avg = row.findViewById(R.id.avg);
            if (line.getFirstName().equals("TOTAL")) {
                avg.setText("");
            } else {
                avg.setText(line.getAvg());
            }

            teamBatting.addView(row);
            rowCount++;
        }

        LinearLayout battingNotes = notes;
        SpannableStringBuilder sBuilder = new SpannableStringBuilder();

        if (doubles.size() > 0) {
            View noteLine = li.inflate(R.layout.box_score_footer_layout, null);
            TextView list = noteLine.findViewById(R.id.list);
            sBuilder.clear();
            sBuilder.append("Doubles: ");
            sBuilder.append(TextUtils.join(", ", doubles));
            CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getResources().getAssets(), "fonts/OpenSans-Bold.ttf"));
            sBuilder.setSpan(typefaceSpan, 0, 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            list.setText(sBuilder, TextView.BufferType.SPANNABLE);

            battingNotes.addView(noteLine);
        }

        if (triples.size() > 0) {
            View noteLine = li.inflate(R.layout.box_score_footer_layout, null);
            TextView list = noteLine.findViewById(R.id.list);
            sBuilder.clear();
            sBuilder.append("Triples: ");
            sBuilder.append(TextUtils.join(", ", triples));
            CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getResources().getAssets(), "fonts/OpenSans-Bold.ttf"));
            sBuilder.setSpan(typefaceSpan, 0, 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            list.setText(sBuilder, TextView.BufferType.SPANNABLE);

            battingNotes.addView(noteLine);
        }

        if (homeRuns.size() > 0) {
            View noteLine = li.inflate(R.layout.box_score_footer_layout, null);
            TextView list = noteLine.findViewById(R.id.list);
            sBuilder.clear();
            sBuilder.append("Home Runs: ");
            sBuilder.append(TextUtils.join(", ", homeRuns));
            CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getResources().getAssets(), "fonts/OpenSans-Bold.ttf"));
            sBuilder.setSpan(typefaceSpan, 0, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            list.setText(sBuilder, TextView.BufferType.SPANNABLE);

            battingNotes.addView(noteLine);
        }

        if (totalBases.size() > 0) {
            View noteLine = li.inflate(R.layout.box_score_footer_layout, null);
            TextView list = noteLine.findViewById(R.id.list);
            sBuilder.clear();
            sBuilder.append("Total Bases: ");
            sBuilder.append(TextUtils.join(", ", totalBases));
            CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getResources().getAssets(), "fonts/OpenSans-Bold.ttf"));
            sBuilder.setSpan(typefaceSpan, 0, 13, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            list.setText(sBuilder, TextView.BufferType.SPANNABLE);

            battingNotes.addView(noteLine);
        }

        if (gdp.size() > 0) {
            View noteLine = li.inflate(R.layout.box_score_footer_layout, null);
            TextView list = noteLine.findViewById(R.id.list);
            sBuilder.clear();
            sBuilder.append("GIDP: ");
            sBuilder.append(TextUtils.join(", ", gdp));
            CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getResources().getAssets(), "fonts/OpenSans-Bold.ttf"));
            sBuilder.setSpan(typefaceSpan, 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            list.setText(sBuilder, TextView.BufferType.SPANNABLE);

            battingNotes.addView(noteLine);
        }

        if (sac.size() > 0) {
            View noteLine = li.inflate(R.layout.box_score_footer_layout, null);
            TextView list = noteLine.findViewById(R.id.list);
            sBuilder.clear();
            sBuilder.append("Sac Bunt: ");
            sBuilder.append(TextUtils.join(", ", sac));
            CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getResources().getAssets(), "fonts/OpenSans-Bold.ttf"));
            sBuilder.setSpan(typefaceSpan, 0, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            list.setText(sBuilder, TextView.BufferType.SPANNABLE);

            battingNotes.addView(noteLine);
        }

        if (sacFly.size() > 0) {
            View noteLine = li.inflate(R.layout.box_score_footer_layout, null);
            TextView list = noteLine.findViewById(R.id.list);
            sBuilder.clear();
            sBuilder.append("Sac Fly: ");
            sBuilder.append(TextUtils.join(", ", sacFly));
            CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getResources().getAssets(), "fonts/OpenSans-Bold.ttf"));
            sBuilder.setSpan(typefaceSpan, 0, 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            list.setText(sBuilder, TextView.BufferType.SPANNABLE);

            battingNotes.addView(noteLine);
        }

        if (hbp.size() > 0) {
            View noteLine = li.inflate(R.layout.box_score_footer_layout, null);
            TextView list = noteLine.findViewById(R.id.list);
            sBuilder.clear();
            sBuilder.append("Hit By Pitch: ");
            sBuilder.append(TextUtils.join(", ", hbp));
            CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getResources().getAssets(), "fonts/OpenSans-Bold.ttf"));
            sBuilder.setSpan(typefaceSpan, 0, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            list.setText(sBuilder, TextView.BufferType.SPANNABLE);

            battingNotes.addView(noteLine);
        }
    }

    private void populatePitchingCSV(LinearLayout parent, LinearLayout notes, List data, String teamAbbr, String winLine, String loseLine, String saveLine) {
        LayoutInflater li = LayoutInflater.from(mContext);

        LinearLayout teamPitching = parent;
        final View header = li.inflate(R.layout.pitching_line_layout, null);
        header.setBackgroundColor(getResources().getColor(R.color.retired));
        TextView headerText = header.findViewById(R.id.playerInfo);
        headerText.setText(teamAbbr+" Pitchers");
        teamPitching.addView(header);
        List<PitchingLineScore> pitchingLines = data;
        String save = "";
        List bf = new ArrayList<String>();
        List pitchesThrown = new ArrayList<String>();
        List inherited = new ArrayList<String>();
        List gb_fb = new ArrayList<String>();
        List hbp = new ArrayList<String>();
        List balk = new ArrayList<String>();

        String[] tmp = winLine.split(",");
        String win = tmp[0].substring(1);

        tmp =  loseLine.split(",");
        String lose = tmp[0].substring(1);

        if (saveLine != null) {
            save = saveLine.substring(0, 1) + "S, " + saveLine.substring(1);
        }

        LinearLayout pitchingNotes = notes;
        SpannableStringBuilder sBuilder = new SpannableStringBuilder();

        for (PitchingLineScore line : pitchingLines) {
            if (line.getIr() > 0 && !line.getFirstName().equals("TOTAL")) {
                inherited.add(line.getFirstName().substring(0, 1) + ". " + line.getLastName() + " " + line.getIr()+"-"+line.getIrs());
            }

            if (line.getGb() > 0 && line.getFb() > 0 && !line.getFirstName().equals("TOTAL")) {
                gb_fb.add(line.getFirstName().substring(0, 1) + ". " + line.getLastName() + " " + line.getGb()+"-"+line.getFb());
            }

            if (line.getHb() > 0 && !line.getFirstName().equals("TOTAL")) {
                if (line.getHb() > 1) {
                    hbp.add(line.getFirstName().substring(0, 1) + ". " + line.getLastName() + " " + line.getHb());
                } else {
                    hbp.add(line.getFirstName().substring(0, 1) + ". " + line.getLastName());
                }
            }

            if (line.getBalk() > 0 && !line.getFirstName().equals("TOTAL")) {
                if (line.getBalk() > 1) {
                    balk.add(line.getFirstName().substring(0, 1) + ". " + line.getLastName() + " " + line.getBalk());
                } else {
                    balk.add(line.getFirstName().substring(0, 1) + ". " + line.getLastName());
                }
            }

            View row = li.inflate(R.layout.pitching_line_layout, null);
            if (rowCount % 2 == 0) {
                row.setBackgroundColor(mContext.getResources().getColor(R.color.awayGame));
            }
            TextView playerInfo = row.findViewById(R.id.playerInfo);
            if (line.getFirstName().equals("TOTAL")) {
                displayName = line.getFirstName();
                row.setBackgroundColor(mContext.getResources().getColor(R.color.retired));
            } else {
                displayName = line.getFirstName().substring(0, 1) + ". " + line.getLastName();
                if (line.getW().equals("1")) {
                    displayName += " (W, " + win + ")";
                }
                if (line.getL().equals("1")) {
                    displayName += " (L, " + lose + ")";
                }
                if (line.getS().equals("1")) {
                    displayName += " " + save;
                }
            }

            playerInfo.setText(displayName);

            TextView ip = row.findViewById(R.id.ip);
            ip.setText(line.getIp());

            TextView hits = row.findViewById(R.id.h);
            hits.setText(line.getH());

            TextView runs = row.findViewById(R.id.r);
            runs.setText(line.getR());

            TextView earnedRuns = row.findViewById(R.id.er);
            earnedRuns.setText(line.getEr());

            TextView walks = row.findViewById(R.id.walks);
            walks.setText(line.getBb());

            TextView strikeouts = row.findViewById(R.id.strikeouts);
            strikeouts.setText(line.getK());

            TextView hr = row.findViewById(R.id.hr);
            hr.setText(line.getHr());

            TextView era = row.findViewById(R.id.era);
            era.setText(line.getEra());

            teamPitching.addView(row);

            if (line.getStart() == 1 && !line.getFirstName().equals("TOTAL")) {
                int gameScore = 50;
                gameScore += Integer.parseInt(line.getOuts());
                if (Double.parseDouble(line.getIp()) > 4) {
                    int i = (int) Double.parseDouble(line.getIp());
                    gameScore += ((i - 4) * 2);
                }
                gameScore += Integer.parseInt(line.getK());
                gameScore -= (Integer.parseInt(line.getH()) * 2);
                gameScore -= (Integer.parseInt(line.getHr()) * 4);
                gameScore -= (Integer.parseInt(line.getEr()) * 4);
                gameScore -= ((Integer.parseInt(line.getEr()) - Integer.parseInt(line.getR())) * 2);
                gameScore -= Integer.parseInt(line.getBb());

                View noteLine = li.inflate(R.layout.box_score_footer_layout, null);
                TextView list = noteLine.findViewById(R.id.list);
                sBuilder.clear();
                sBuilder.append("Game Score: ");
                sBuilder.append(String.valueOf(line.getFirstName().substring(0, 1) + ". " + line.getLastName() + " "+gameScore));
                CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getResources().getAssets(), "fonts/OpenSans-Bold.ttf"));
                sBuilder.setSpan(typefaceSpan, 0, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                list.setText(sBuilder);

                pitchingNotes.addView(noteLine);
            }

            if (!line.getFirstName().equals("TOTAL")) {
                bf.add(line.getFirstName().substring(0, 1) + ". " + line.getLastName() + " " + line.getBf());
            }

            if (!line.getFirstName().equals("TOTAL")) {
                pitchesThrown.add(line.getFirstName().substring(0, 1) + ". " + line.getLastName() + " " + line.getPi());
            }

            rowCount++;
        }

        View noteLine = li.inflate(R.layout.box_score_footer_layout, null);
        TextView list = noteLine.findViewById(R.id.list);
        sBuilder.clear();
        sBuilder.append("Batters Faced: ");
        sBuilder.append(TextUtils.join(", ", bf));
        CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getResources().getAssets(), "fonts/OpenSans-Bold.ttf"));
        sBuilder.setSpan(typefaceSpan, 0, 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        list.setText(sBuilder, TextView.BufferType.SPANNABLE);

        pitchingNotes.addView(noteLine);

        noteLine = li.inflate(R.layout.box_score_footer_layout, null);
        list = noteLine.findViewById(R.id.list);
        sBuilder.clear();
        sBuilder.append("Pitches Thrown: ");
        sBuilder.append(TextUtils.join(", ", pitchesThrown));
        typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getResources().getAssets(), "fonts/OpenSans-Bold.ttf"));
        sBuilder.setSpan(typefaceSpan, 0, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        list.setText(sBuilder, TextView.BufferType.SPANNABLE);

        pitchingNotes.addView(noteLine);

        if (gb_fb.size() > 0) {
            noteLine = li.inflate(R.layout.box_score_footer_layout, null);
            list = noteLine.findViewById(R.id.list);
            sBuilder.clear();
            sBuilder.append("Ground Outs - Fly Outs: ");
            sBuilder.append(TextUtils.join(", ", gb_fb));
            typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getResources().getAssets(), "fonts/OpenSans-Bold.ttf"));
            sBuilder.setSpan(typefaceSpan, 0, 24, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            list.setText(sBuilder, TextView.BufferType.SPANNABLE);

            pitchingNotes.addView(noteLine);
        }

        if (inherited.size() > 0) {
            noteLine = li.inflate(R.layout.box_score_footer_layout, null);
            list = noteLine.findViewById(R.id.list);
            sBuilder.clear();
            sBuilder.append("Inherited Runners - Scored: ");
            sBuilder.append(TextUtils.join(", ", inherited));
            typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getResources().getAssets(), "fonts/OpenSans-Bold.ttf"));
            sBuilder.setSpan(typefaceSpan, 0, 28, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            list.setText(sBuilder, TextView.BufferType.SPANNABLE);

            pitchingNotes.addView(noteLine);
        }

        if (hbp.size() > 0) {
            noteLine = li.inflate(R.layout.box_score_footer_layout, null);
            list = noteLine.findViewById(R.id.list);
            sBuilder.clear();
            sBuilder.append("Hit Batsmen: ");
            sBuilder.append(TextUtils.join(", ", hbp));
            typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getResources().getAssets(), "fonts/OpenSans-Bold.ttf"));
            sBuilder.setSpan(typefaceSpan, 0, 13, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            list.setText(sBuilder, TextView.BufferType.SPANNABLE);

            pitchingNotes.addView(noteLine);
        }

        if (balk.size() > 0) {
            noteLine = li.inflate(R.layout.box_score_footer_layout, null);
            list = noteLine.findViewById(R.id.list);
            sBuilder.clear();
            sBuilder.append("Balks: ");
            sBuilder.append(TextUtils.join(", ", balk));
            typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getResources().getAssets(), "fonts/OpenSans-Bold.ttf"));
            sBuilder.setSpan(typefaceSpan, 0, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            list.setText(sBuilder, TextView.BufferType.SPANNABLE);

            pitchingNotes.addView(noteLine);
        }
    }

    private BattingGameNotes battingNotes(Element element) {
        BattingGameNotes notes = new BattingGameNotes();
        String[] arr = element.html().split("<br>");
        List<Integer> subs = getArrayIndex(arr);
        if (!subs.isEmpty()) {
            List<String> subList = new ArrayList<String>();
            for (int i : subs) {
                Document tmp = Jsoup.parse(arr[i]);
                subList.add(tmp.text());
            }
            notes.setSubs(subList);
        }
        int doublesIndex = getArrayIndex(arr, "Doubles:");
        if (doublesIndex > -1) {
            Document tmp = Jsoup.parse(arr[doublesIndex]);
            tmp.select("b").remove();
            notes.setDoubles(tmp.text());
        }
        int triplesIndex = getArrayIndex(arr, "Triples:");
        if (triplesIndex > -1) {
            Document tmp = Jsoup.parse(arr[triplesIndex]);
            tmp.select("b").remove();
            notes.setTriples(tmp.text());
        }
        int homeRunsIndex = getArrayIndex(arr, "Home Runs:");
        if (homeRunsIndex > -1) {
            Document tmp = Jsoup.parse(arr[homeRunsIndex]);
            tmp.select("b").remove();
            notes.setHomeRuns(tmp.text());
        }
        int totalBasesIndex = getArrayIndex(arr, "Total Bases:");
        if (totalBasesIndex > -1) {
            Document tmp = Jsoup.parse(arr[totalBasesIndex]);
            tmp.select("b").remove();
            notes.setTotalBases(tmp.text());
        }
        int twoOutRBIIndex = getArrayIndex(arr, "2-out RBI:");
        if (twoOutRBIIndex > -1) {
            Document tmp = Jsoup.parse(arr[twoOutRBIIndex]);
            tmp.select("b").remove();
            notes.setTwoOutRBI(tmp.text());
        }
        int risp2OutsIndex = getArrayIndex(arr, "Runners left in scoring position, 2 outs:");
        if (risp2OutsIndex > -1) {
            Document tmp = Jsoup.parse(arr[risp2OutsIndex]);
            tmp.select("b").remove();
            notes.setRisp2Outs(tmp.text());
        }
        int gidpIndex = getArrayIndex(arr, "GIDP:");
        if (gidpIndex > -1) {
            Document tmp = Jsoup.parse(arr[gidpIndex]);
            tmp.select("b").remove();
            notes.setGidp(tmp.text());
        }
        int hbpIndex = getArrayIndex(arr, "Hit by Pitch:");
        if (hbpIndex > -1) {
            Document tmp = Jsoup.parse(arr[hbpIndex]);
            tmp.select("b").remove();
            notes.setHBP(tmp.text());
        }
        int sacBuntIndex = getArrayIndex(arr, "Sac Bunt:");
        if (sacBuntIndex > -1) {
            Document tmp = Jsoup.parse(arr[sacBuntIndex]);
            tmp.select("b").remove();
            notes.setSacBunt(tmp.text());
        }
        int sacFlyIndex = getArrayIndex(arr, "Sac Fly:");
        if (sacFlyIndex > -1) {
            Document tmp = Jsoup.parse(arr[sacFlyIndex]);
            tmp.select("b").remove();
            notes.setSacFly(tmp.text());
        }
        int teamLOBIndex = getArrayIndex(arr, "Team LOB:");
        if (teamLOBIndex > -1) {
            Document tmp = Jsoup.parse(arr[teamLOBIndex]);
            tmp.select("b").remove();
            notes.setTeamLOB(tmp.text());
        }
        int sbIndex = getArrayIndex(arr, "SB:");
        if (sbIndex > -1) {
            Document tmp = Jsoup.parse(arr[sbIndex]);
            tmp.select("b").remove();
            notes.setSb(tmp.text());
        }
        int csIndex = getArrayIndex(arr, "CS:");
        if (csIndex > -1) {
            Document tmp = Jsoup.parse(arr[csIndex]);
            tmp.select("b").remove();
            notes.setCs(tmp.text());
        }
        int errorIndex = getArrayIndex(arr, "Errors:");
        if (errorIndex > -1) {
            Document tmp = Jsoup.parse(arr[errorIndex]);
            tmp.select("b").remove();
            notes.setErrors(tmp.text());
        }
        int doublePlaysIndex = getArrayIndex(arr, "Double Plays:");
        if (doublePlaysIndex > -1) {
            Document tmp = Jsoup.parse(arr[doublePlaysIndex]);
            tmp.select("b").remove();
            notes.setDoublePlays(tmp.text());
        }
        return notes;
    }

    private PitchingGameNotes pitchingNotes(Element element) {
        PitchingGameNotes notes = new PitchingGameNotes();
        String[] arr = element.html().split("<br>");
        int gameScoreIndex = getArrayIndex(arr, "Game Score:");
        if (gameScoreIndex > -1) {
            Document tmp = Jsoup.parse(arr[gameScoreIndex]);
            tmp.select("b").remove();
            notes.setGameScore(tmp.text());
        }
        int battersFacedIndex = getArrayIndex(arr, "Batters Faced:");
        if (battersFacedIndex > -1) {
            Document tmp = Jsoup.parse(arr[battersFacedIndex]);
            tmp.select("b").remove();
            notes.setBattersFaced(tmp.text());
        }
        int goFoIndex = getArrayIndex(arr, "Ground Outs - Fly Outs:");
        if (goFoIndex > -1) {
            Document tmp = Jsoup.parse(arr[goFoIndex]);
            tmp.select("b").remove();
            notes.setGoFo(tmp.text());
        }
        int pitchesStrikesIndex = getArrayIndex(arr, "Pitches - Strikes:");
        if (pitchesStrikesIndex > -1) {
            Document tmp = Jsoup.parse(arr[pitchesStrikesIndex]);
            tmp.select("b").remove();
            notes.setPitchesStrikes(tmp.text());
        }
        int irScoredIndex = getArrayIndex(arr, "Inherited Runners - Scored:");
        if (irScoredIndex > -1) {
            Document tmp = Jsoup.parse(arr[irScoredIndex]);
            tmp.select("b").remove();
            notes.setIrScored(tmp.text());
        }
        int hitBatsmenIndex = getArrayIndex(arr, "Hit Batsmen:");
        if (hitBatsmenIndex > -1) {
            Document tmp = Jsoup.parse(arr[hitBatsmenIndex]);
            tmp.select("b").remove();
            notes.setHitBatsmen(tmp.text());
        }
        int wildPitchIndex = getArrayIndex(arr, "WP:");
        if (wildPitchIndex > -1) {
            Document tmp = Jsoup.parse(arr[wildPitchIndex]);
            tmp.select("b").remove();
            notes.setWildPitch(tmp.text());
        }
        int balkIndex = getArrayIndex(arr, "Balk:");
        if (balkIndex > -1) {
            Document tmp = Jsoup.parse(arr[balkIndex]);
            tmp.select("b").remove();
            notes.setBalk(tmp.text());
        }
        return notes;
    }
    
    private GameNotes gameNotes(Element element) {
        GameNotes notes = new GameNotes();
        String[] arr = element.html().split("<br>");
        int potgIndex = getArrayIndex(arr, "Player of the Game:");
        if (potgIndex > -1) {
            Document tmp = Jsoup.parse(arr[potgIndex]);
            tmp.select("b").remove();
            notes.setPotg(tmp.text().trim());
        }
        int ballparkIndex = getArrayIndex(arr, "Ballpark:");
        if (ballparkIndex > -1) {
            Document tmp = Jsoup.parse(arr[ballparkIndex]);
            tmp.select("b").remove();
            notes.setBallpark(tmp.text());
        }
        int weatherIndex = getArrayIndex(arr, "Weather:");
        if (weatherIndex > -1) {
            Document tmp = Jsoup.parse(arr[weatherIndex]);
            tmp.select("b").remove();
            notes.setWeather(tmp.text());
        }
        int startTimeIndex = getArrayIndex(arr, "Start Time:");
        if (startTimeIndex > -1) {
            Document tmp = Jsoup.parse(arr[startTimeIndex]);
            tmp.select("b").remove();
            notes.setStartTime(tmp.text());
        }
        int timeIndex = getArrayIndex(arr, "<b>Time: ");
        if (timeIndex > -1) {
            Document tmp = Jsoup.parse(arr[timeIndex]);
            tmp.select("b").remove();
            notes.setTime(tmp.text());
        }
        int attendanceIndex = getArrayIndex(arr, "Attendance: ");
        if (attendanceIndex > -1) {
            Document tmp = Jsoup.parse(arr[attendanceIndex]);
            tmp.select("b").remove();
            notes.setAttendance(tmp.text());
        }
        int notesIndex = getArrayIndex(arr, "Special Notes: ");
        if (notesIndex > -1) {
            Document tmp = Jsoup.parse(arr[notesIndex]);
            tmp.select("b").remove();
            notes.setNotes(tmp.text());
        }
        return notes;
    }

    private int getArrayIndex(String[] haystack, String needle) {
        int index = -1;
        for (int i=0;i<haystack.length;i++) {
            if (haystack[i].contains(needle)) {
                index = i;
                break;
            }
        }
        return index;
    }

    private List<Integer> getArrayIndex(String[] haystack) {
        List<Integer> list = new ArrayList<Integer>();
        for (int i=0;i<haystack.length;i++) {
            if (haystack[i].contains("pinch hit") || haystack[i].contains("pinch ran") || haystack[i].contains("substituted for ")) {
                list.add(i);
            }
        }
        return list;
    }

    private boolean isStarter(String player) {
        return Character.isUpperCase(player.codePointAt(0));
    }

    private int ipToOuts(String inn) {
        int outs = 0;
        String[] parts = inn.split("\\.");
        outs += (Integer.parseInt(parts[0]) * 3);
        outs += (Integer.parseInt(parts[1]));
        return outs;
    }

    private String outsToInnings(int outs) {
        Float inn = (float) outs / 3;
        BigDecimal bdInn = new BigDecimal(Float.toString(inn));
        bdInn = bdInn.setScale(1, RoundingMode.HALF_UP);
        String innings = String.valueOf(bdInn);
        innings = innings.replace(".3", ".1");
        innings = innings.replace(".7", ".2");
        return innings;
    }
}
