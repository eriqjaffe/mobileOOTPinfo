package com.mobileootpinfo.mobileootpinfo.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.inqbarna.tablefixheaders.TableFixHeaders;
import com.inqbarna.tablefixheaders.adapters.BaseTableAdapter;
import com.mobileootpinfo.mobileootpinfo.R;
import com.mobileootpinfo.mobileootpinfo.activity.NavDrawer;
import com.mobileootpinfo.mobileootpinfo.adapter.AwardAdapter;
import com.mobileootpinfo.mobileootpinfo.adapter.BattingCareerStatsAdapter;
import com.mobileootpinfo.mobileootpinfo.adapter.FieldingCareerStatsAdapter;
import com.mobileootpinfo.mobileootpinfo.adapter.PitchingCareerStatsAdapter;
import com.mobileootpinfo.mobileootpinfo.model.Award;
import com.mobileootpinfo.mobileootpinfo.model.BattingStatLine;
import com.mobileootpinfo.mobileootpinfo.model.BattingStatSummary;
import com.mobileootpinfo.mobileootpinfo.model.Contract;
import com.mobileootpinfo.mobileootpinfo.model.FieldingStatLine;
import com.mobileootpinfo.mobileootpinfo.model.PitchingStatLine;
import com.mobileootpinfo.mobileootpinfo.model.PitchingStatSummary;
import com.mobileootpinfo.mobileootpinfo.model.Player;
import com.mobileootpinfo.mobileootpinfo.util.CalligraphyDefaultTabLayout;
import com.mobileootpinfo.mobileootpinfo.util.DatabaseHandler;
import com.mobileootpinfo.mobileootpinfo.util.constants;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.co.chrisjenx.calligraphy.CalligraphyTypefaceSpan;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

import static com.mobileootpinfo.mobileootpinfo.util.ColorUtils.getContrastColor;

public class PlayerCard extends Fragment {

    private String html_root, universe_name, game_date, league_abbr, subtitle;
    private int current_season, player_id, league_id, textColor;
    private Player player;
    private DatabaseHandler db;
    private Context mContext;
    private SimpleDateFormat sqliteSdf = new SimpleDateFormat("yyyy/MM/dd");
    private TableFixHeaders tablefixheaders;
    private DateFormat formatter = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
    private AwardAdapter awardAdapter;
    private View view, summaryView, statsView, awardsView, battingStatsView, pitchingStatsView, fieldingStatsView, contractView;
    //private LayoutInflater inflater = getActivity().getLayoutInflater();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        view = inflater.inflate(R.layout.fragment_player_card, container, false);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        hideKeyboard();

        JodaTimeAndroid.init(getActivity());

        mContext = getActivity();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            html_root = bundle.getString("html_root");
            universe_name = bundle.getString("universe_name");
            player_id = bundle.getInt("player_id");
            game_date = bundle.getString("game_date");
            league_id = bundle.getInt("league_id");
            league_abbr = bundle.getString("league_abbr");
        }

        try {
            Date gameDate = sqliteSdf.parse(game_date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(gameDate);
            current_season = cal.get(Calendar.YEAR);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        db = new DatabaseHandler(mContext);

        player = db.getPlayerInfo(player_id, universe_name);

        if (Color.parseColor(player.getTextColor()) == Color.parseColor(player.getBackgroundColor())) {
            textColor = getContrastColor(Color.parseColor(player.getTextColor()));
        } else {
            textColor = Color.parseColor(player.getTextColor());
        }

        TextView playerBT = view.findViewById(R.id.playerBT);
        playerBT.setText("B/T "+constants.batting[player.getPlayerBats()]+"/"+constants.batting[player.getPlayerThrows()]);
        playerBT.setTextColor(textColor);

        TextView pipe1 = view.findViewById(R.id.pipe1);
        pipe1.setTextColor(textColor);

        TextView playerHW = view.findViewById(R.id.playerHW);
        playerHW.setText(centimeterToFeet(String.valueOf(player.getHeight())) + " " + player.getWeight());
        playerHW.setTextColor(textColor);

        TextView pipe2 = view.findViewById(R.id.pipe2);
        pipe2.setTextColor(textColor);

        TextView playerAge = view.findViewById(R.id.playerAge);
        playerAge.setText("Age: "+age(player.getBirthday(), game_date));
        playerAge.setTextColor(textColor);

        if (player.getRetired() == 1) {
            subtitle = player.getTeamName() + " " + player.getTeamNickname();
        } else {
            subtitle = constants.positions[player.getPosition()] + " - " + player.getTeamName() + " " + player.getTeamNickname();
        }

        ((NavDrawer) getActivity())
                .formatActionBar("#" + player.getUniformNumber()+ " " + player.getFirstName()+ " "+player.getLastName(),
                        subtitle,
                        Color.parseColor(player.getBackgroundColor()), textColor);

        ImageView parkOverlay = view.findViewById(R.id.ballparkOverlay);
        String transparentColor = "#00"+(player.getBackgroundColor().substring((player.getBackgroundColor().length()-6)));
        String opaqueColor = "#FF"+(player.getBackgroundColor().substring((player.getBackgroundColor().length()-6)));
        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] {Color.parseColor(transparentColor),Color.parseColor(opaqueColor)});
        gd.setCornerRadius(0f);
        parkOverlay.setImageDrawable(gd);

        ImageView externalLink = view.findViewById(R.id.externalLink);

        externalLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = html_root + getString(R.string.player_link, player_id);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        externalLink.setColorFilter(textColor);

        ImageView playerImage = view.findViewById(R.id.playerImage);

        Bitmap silhouette = BitmapFactory.decodeResource(getResources(), R.drawable.person_silhouette);
        Drawable d = new BitmapDrawable(getResources(), silhouette);
        if (player.getRetired() != 1) {
            ColorFilter filter = new LightingColorFilter(Color.BLACK, textColor);
            d.setColorFilter(filter);
        }

        Glide.with(mContext)
                .load(html_root+getString(R.string.player_image, player_id))
                .apply(new RequestOptions()
                        .error(d)
                )
                .into(playerImage);

        summaryView = view.findViewById(R.id.summaryParent);
        statsView = view.findViewById(R.id.statsParent);
        awardsView = view.findViewById(R.id.award_list_view);
        contractView = view.findViewById(R.id.contractParent);

        if (player.getRetired() == 1) {
            LinearLayout currentYearParent = view.findViewById(R.id.currentYearParent);
            currentYearParent.setVisibility(View.GONE);
        }

        battingStatsView = view.findViewById(R.id.battingTable);
        pitchingStatsView = view.findViewById(R.id.pitchingTable);
        fieldingStatsView = view.findViewById(R.id.fieldingTable);

        CalligraphyDefaultTabLayout tabLayout = view.findViewById(R.id.playerTabs);

        tabLayout.setSelectedTabIndicatorColor(getContrastColor(Color.parseColor(player.getBackgroundColor()), textColor));

        tabLayout.addTab(tabLayout.newTab().setText("Summary"));
        tabLayout.addTab(tabLayout.newTab().setText("Stats"));
        tabLayout.addTab(tabLayout.newTab().setText("Awards"));
        if (player.getRetired() != 1 && player.getContractType() > 0) {
            tabLayout.addTab(tabLayout.newTab().setText("Contract"));
        }
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        
        populateSummary(player_id, universe_name, league_id);

        CalligraphyDefaultTabLayout statTabLayout = view.findViewById(R.id.statTabs);

        statTabLayout.setSelectedTabIndicatorColor(getContrastColor(Color.parseColor(player.getBackgroundColor()), textColor));

        if (player.getPosition() == 1) {
            pitchingStatsView.setVisibility(View.VISIBLE);
            battingStatsView.setVisibility(View.GONE);
            fieldingStatsView.setVisibility(View.GONE);
            statTabLayout.addTab(statTabLayout.newTab().setText(R.string.pitching));
            populatePitchingStats(player_id, universe_name, league_id);
            if (db.getCareerPlateAppearances(universe_name, player_id, league_id) > 0) {
                statTabLayout.addTab(statTabLayout.newTab().setText(R.string.batting));
                populateBattingStats(player_id, universe_name, league_id);
            }
        } else {
            pitchingStatsView.setVisibility(View.GONE);
            battingStatsView.setVisibility(View.VISIBLE);
            fieldingStatsView.setVisibility(View.GONE);
            statTabLayout.addTab(statTabLayout.newTab().setText(R.string.batting));
            populateBattingStats(player_id, universe_name, league_id);
            if (db.getCareerPitchingAppearances(universe_name, player_id, league_id) > 0) {
                statTabLayout.addTab(statTabLayout.newTab().setText(R.string.pitching));
                populatePitchingStats(player_id, universe_name, league_id);
            }
        }
        statTabLayout.addTab(statTabLayout.newTab().setText(R.string.fielding));

        populateFieldingStats(player_id, universe_name, league_id);

        statTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getText().toString()) {
                    case "Pitching":
                        pitchingStatsView.setVisibility(View.VISIBLE);
                        battingStatsView.setVisibility(View.GONE);
                        fieldingStatsView.setVisibility(View.GONE);
                        break;
                    case "Batting":
                        pitchingStatsView.setVisibility(View.GONE);
                        battingStatsView.setVisibility(View.VISIBLE);
                        fieldingStatsView.setVisibility(View.GONE);
                        break;
                    case "Fielding":
                        pitchingStatsView.setVisibility(View.GONE);
                        battingStatsView.setVisibility(View.GONE);
                        fieldingStatsView.setVisibility(View.VISIBLE);
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

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        summaryView.setVisibility(View.VISIBLE);
                        statsView.setVisibility(View.GONE);
                        awardsView.setVisibility(View.GONE);
                        contractView.setVisibility(View.GONE);
                        break;
                    case 1:
                        summaryView.setVisibility(View.GONE);
                        statsView.setVisibility(View.VISIBLE);
                        awardsView.setVisibility(View.GONE);
                        contractView.setVisibility(View.GONE);
                        break;
                    case 2:
                        summaryView.setVisibility(View.GONE);
                        statsView.setVisibility(View.GONE);
                        awardsView.setVisibility(View.VISIBLE);
                        contractView.setVisibility(View.GONE);
                        break;
                    case 3:
                        summaryView.setVisibility(View.GONE);
                        statsView.setVisibility(View.GONE);
                        awardsView.setVisibility(View.GONE);
                        contractView.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        populateAwards(player_id, universe_name, league_id);

        if (player.getRetired() != 1 && player.getContractType() > 0) {
            populateContract(player_id, universe_name, league_id);
        }
    }
    
    private void populateSummary(int player_id, String universe_name, int league_id) {
        TextView playerBorn = view.findViewById(R.id.playerBorn);
        TextView playerLocalPop = view.findViewById(R.id.playerLocalPop);
        TextView playerNationalPop = view.findViewById(R.id.playerNationalPop);
        TextView seasonStatHeader1 = view.findViewById(R.id.seasonStatHeader1);
        TextView seasonStatHeader2 = view.findViewById(R.id.seasonStatHeader2);
        TextView seasonStatHeader3 = view.findViewById(R.id.seasonStatHeader3);
        TextView seasonStatHeader4 = view.findViewById(R.id.seasonStatHeader4);
        TextView seasonStatHeader5 = view.findViewById(R.id.seasonStatHeader5);
        TextView seasonStat1 = view.findViewById(R.id.seasonStat1);
        TextView seasonStat2 = view.findViewById(R.id.seasonStat2);
        TextView seasonStat3 = view.findViewById(R.id.seasonStat3);
        TextView seasonStat4 = view.findViewById(R.id.seasonStat4);
        TextView seasonStat5 = view.findViewById(R.id.seasonStat5);
        TextView careerStatHeader1 = view.findViewById(R.id.careerStatHeader1);
        TextView careerStatHeader2 = view.findViewById(R.id.careerStatHeader2);
        TextView careerStatHeader3 = view.findViewById(R.id.careerStatHeader3);
        TextView careerStatHeader4 = view.findViewById(R.id.careerStatHeader4);
        TextView careerStatHeader5 = view.findViewById(R.id.careerStatHeader5);
        TextView careerStat1 = view.findViewById(R.id.careerStat1);
        TextView careerStat2 = view.findViewById(R.id.careerStat2);
        TextView careerStat3 = view.findViewById(R.id.careerStat3);
        TextView careerStat4 = view.findViewById(R.id.careerStat4);
        TextView careerStat5 = view.findViewById(R.id.careerStat5);
        TextView currentYearLabel = view.findViewById(R.id.currentYearLabel);
        currentYearLabel.setText(current_season + " " + league_abbr + " STATS");
        TextView careerLabel = view.findViewById(R.id.careerLabel);
        careerLabel.setText("CAREER " + league_abbr + " STATS");
        String birthSuffix = (player.getBirthCountryId() == 206 || player.getBirthCountryId() == 36) ? player.getBirthState() : player.getBirthCountry();
        try {
            String birthday = formatter.format(sqliteSdf.parse(player.getBirthday()));
            playerBorn.setText(birthday + " in " + player.getBirthCity() +", "+birthSuffix);
        } catch (ParseException e) {
            playerBorn.setText(player.getBirthday() + " in " + player.getBirthCity() +", "+birthSuffix);
        }
        playerLocalPop.setText(constants.popularity[player.getLocalPopularity()]);
        playerNationalPop.setText(constants.popularity[player.getNationalPopularity()]);
        if (player.getPosition() == 1) {
            PitchingStatSummary summary = db.currentYearPitchingSummary(universe_name, player_id, league_id, current_season);
            PitchingStatSummary career = db.careerPitchingSummary(universe_name, player_id, league_id);
            seasonStatHeader1.setText("W-L");
            seasonStatHeader2.setText("ERA");
            seasonStatHeader3.setText("IP");
            seasonStatHeader4.setText("SO");
            seasonStatHeader5.setText("WHIP");
            careerStatHeader1.setText("W-L");
            careerStatHeader2.setText("ERA");
            careerStatHeader3.setText("IP");
            careerStatHeader4.setText("SO");
            careerStatHeader5.setText("WHIP");
            seasonStat1.setText(summary.getW()+"-"+summary.getL());
            seasonStat2.setText(""+summary.getEra());
            seasonStat3.setText(""+summary.getIp());
            seasonStat4.setText(""+summary.getSo());
            seasonStat5.setText(""+summary.getWhip());
            careerStat1.setText(career.getW()+"-"+career.getL());
            careerStat2.setText(""+career.getEra());
            careerStat3.setText(""+career.getIp());
            careerStat4.setText(""+career.getSo());
            careerStat5.setText(""+career.getWhip());
        } else {
            BattingStatSummary summary = db.currentYearBattingSummary(universe_name, player_id, league_id, current_season);
            BattingStatSummary career = db.careerBattingSummary(universe_name, player_id, league_id);
            seasonStatHeader1.setText("AVG");
            seasonStatHeader2.setText("HR");
            seasonStatHeader3.setText("RBI");
            seasonStatHeader4.setText("SB");
            seasonStatHeader5.setText("OPS");
            careerStatHeader1.setText("AVG");
            careerStatHeader2.setText("HR");
            careerStatHeader3.setText("RBI");
            careerStatHeader4.setText("SB");
            careerStatHeader5.setText("OPS");
            String str = ""+summary.getAvg();
            seasonStat1.setText(str.replaceFirst("^0.", "."));
            seasonStat2.setText(""+summary.getHr());
            seasonStat3.setText(""+summary.getRbi());
            seasonStat4.setText(""+summary.getSb());
            str = ""+summary.getOps();
            seasonStat5.setText(str.replaceFirst("^0.", "."));
            str = ""+career.getAvg();
            careerStat1.setText(str.replaceFirst("^0.", "."));
            careerStat2.setText(""+career.getHr());
            careerStat3.setText(""+career.getRbi());
            careerStat4.setText(""+career.getSb());
            str = ""+career.getOps();
            careerStat5.setText(str.replaceFirst("^0.", "."));
        }
    }

    private void populateAwards(int player_id, String universe_name, int league_id) {
        ListView listView = getActivity().findViewById(R.id.award_list_view);
        List<Award> awards = db.awardList(universe_name, league_id, player_id);
        awardAdapter = new AwardAdapter(mContext, awards);
        listView.setAdapter(awardAdapter);
    }

    private void populateBattingStats(int player_id, String universe_name, int league_id) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        tablefixheaders = (TableFixHeaders) view.findViewById(R.id.battingTable);
        tablefixheaders.setAdapter(getBattingAdapter());
    }

    private void populatePitchingStats(int player_id, String universe_name, int league_id) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        tablefixheaders = (TableFixHeaders) view.findViewById(R.id.pitchingTable);
        tablefixheaders.setAdapter(getPitchingAdapter());
    }

    private void populateFieldingStats(int player_id, String universe_name, int league_id) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        tablefixheaders = (TableFixHeaders) view.findViewById(R.id.fieldingTable);
        tablefixheaders.setAdapter(getFieldingAdapter());
    }

    private void populateContract(int player_id, String universe_name, int league_id) {
        LinearLayout playerNameParent = view.findViewById(R.id.playerNameParent);
        playerNameParent.setVisibility(View.GONE);

        Contract contract = db.getPlayerContract(universe_name, player_id, league_id);

        String yearDisplay;
        if (contract.getContractLength() > 1) {
            yearDisplay = " years / ";
        } else {
            yearDisplay = " year / ";
        }

        String summaryLine = contract.getContractLength() + yearDisplay + formatSalary(contract.getTotalValue());

        String lastContractYear = String.valueOf(contract.getSignedIn()+(contract.getContractLength()-1));
        String nextLastContractYear = String.valueOf(contract.getSignedIn()+(contract.getContractLength()-2));
        String contractYears;

        if (contract.getContractLength() > 1) {
            contractYears = "("+String.valueOf(contract.getSignedIn()) + " - " + String.valueOf(contract.getSignedIn()+(contract.getContractLength()-1))+")";
        } else {
            contractYears = "("+String.valueOf(contract.getSignedIn()) + ")";
        }

        summaryLine += " " + contractYears;

        TextView contractOverview = view.findViewById(R.id.contractOverview);
        contractOverview.setText(summaryLine);

        int contractStartYear = contract.getSignedIn();

        List<Integer> yearlyList = new ArrayList<Integer>();

        yearlyList.add(contract.getSalary0());
        yearlyList.add(contract.getSalary1());
        yearlyList.add(contract.getSalary2());
        yearlyList.add(contract.getSalary3());
        yearlyList.add(contract.getSalary4());
        yearlyList.add(contract.getSalary5());
        yearlyList.add(contract.getSalary6());
        yearlyList.add(contract.getSalary7());
        yearlyList.add(contract.getSalary8());
        yearlyList.add(contract.getSalary9());

        List<String> contractBreakdown = new ArrayList<String>();

        for (int i = 0; i < contract.getContractLength(); i++) {
            int tmp = contractStartYear + i;
            contractBreakdown.add(String.valueOf(tmp) + ": " + formatSalary(yearlyList.get(i)));
        }

        TextView yearlyBreakdown = view.findViewById(R.id.yearlyBreakdown);
        yearlyBreakdown.setText(boldTags(android.text.TextUtils.join(", ", contractBreakdown)));

        LinearLayout teamNextOptionParent = view.findViewById(R.id.teamNextOptionParent);
        if (contract.getNextLastYearTeamOption() > 0) {
            TextView teamNextOption = view.findViewById(R.id.teamNextOption);
            teamNextOption.setText(nextLastContractYear + " is a team option with a "+formatSalary(contract.getNextLastYearOptionBuyout())+ " buyout.");
            teamNextOptionParent.setVisibility(View.VISIBLE);
        } else {
            teamNextOptionParent.setVisibility(View.GONE);
        }

        LinearLayout teamOptionParent = view.findViewById(R.id.teamOptionParent);
        if (contract.getLastYearTeamOption() > 0) {
            TextView teamOption = view.findViewById(R.id.teamOption);
            teamOption.setText(lastContractYear + " is a team option with a "+formatSalary(contract.getLastYearOptionBuyout())+ " buyout.");
            teamOptionParent.setVisibility(View.VISIBLE);
        } else {
            teamOptionParent.setVisibility(View.GONE);
        }

        LinearLayout playerNextOptionParent = view.findViewById(R.id.playerNextOptionParent);
        if (contract.getNextLastYearPlayerOption() > 0) {
            TextView playerNextOption = view.findViewById(R.id.playerNextOption);
            playerNextOption.setText("Has a player option for " +nextLastContractYear );
            playerNextOptionParent.setVisibility(View.VISIBLE);
        } else {
            playerNextOptionParent.setVisibility(View.GONE);
        }

        LinearLayout playerOptionParent = view.findViewById(R.id.playerOptionParent);
        if (contract.getLastYearPlayerOption() > 0) {
            TextView playerOption = view.findViewById(R.id.playerOption);
            playerOption.setText("Has a player option for " +lastContractYear );
            playerOptionParent.setVisibility(View.VISIBLE);
        } else {
            playerOptionParent.setVisibility(View.GONE);
        }

        LinearLayout vestingNextOptionParent = view.findViewById(R.id.vestingNextOptionParent);
        if (contract.getNextLastYearVestingOption() > 0) {
            TextView vestingNextOption = view.findViewById(R.id.vestingNextOption);
            vestingNextOption.setText("Has a vesting option for " +nextLastContractYear );
            vestingNextOptionParent.setVisibility(View.VISIBLE);
        } else {
            vestingNextOptionParent.setVisibility(View.GONE);
        }

        LinearLayout vestingOptionParent = view.findViewById(R.id.vestingOptionParent);
        if (contract.getLastYearVestingOption() > 0) {
            TextView vestingOption = view.findViewById(R.id.vestingOption);
            vestingOption.setText("Has a vesting option for " +lastContractYear );
            vestingOptionParent.setVisibility(View.VISIBLE);
        } else {
            vestingOptionParent.setVisibility(View.GONE);
        }

        LinearLayout paIncentiveParent = view.findViewById(R.id.paIncentiveParent);
        if (contract.getMinimumPABonus() > 0) {
            TextView paIncentive = view.findViewById(R.id.paIncentive);
            paIncentive.setText(formatSalary(contract.getMinimumPABonus()) + " bonus for making " +contract.getMinimumPA() + " plate appearances.");
            paIncentiveParent.setVisibility(View.VISIBLE);
        } else {
            paIncentiveParent.setVisibility(View.GONE);
        }

        LinearLayout ipIncentiveParent = view.findViewById(R.id.ipIncentiveParent);
        if (contract.getMinimumIPBonus() > 0) {
            TextView ipIncentive = view.findViewById(R.id.ipIncentive);
            ipIncentive.setText(formatSalary(contract.getMinimumIPBonus()) + " bonus for pitching " +contract.getMinimumIP() + " innings.");
            ipIncentiveParent.setVisibility(View.VISIBLE);
        } else {
            ipIncentiveParent.setVisibility(View.GONE);
        }

        LinearLayout mvpBonusParent = view.findViewById(R.id.mvpBonusParent);
        if (contract.getMvpBonus() > 0) {
            TextView mvpBonus = view.findViewById(R.id.mvpBonus);
            mvpBonus.setText(formatSalary(contract.getMvpBonus()) + " " +contract.getMvpAwardName() + " bonus.");
            mvpBonusParent.setVisibility(View.VISIBLE);
        } else {
            mvpBonusParent.setVisibility(View.GONE);
        }

        LinearLayout poyBonusParent = view.findViewById(R.id.poyBonusParent);
        if (contract.getPoyBonus() > 0) {
            TextView poyBonus = view.findViewById(R.id.poyBonus);
            poyBonus.setText(formatSalary(contract.getPoyBonus()) + " " +contract.getPoyAwardName() + " bonus.");
            poyBonusParent.setVisibility(View.VISIBLE);
        } else {
            poyBonusParent.setVisibility(View.GONE);
        }

        LinearLayout allStarBonusParent = view.findViewById(R.id.allStarBonusParent);
        if (contract.getAsBonus() > 0) {
            TextView allStarBonus = view.findViewById(R.id.allStarBonus);
            allStarBonus.setText(formatSalary(contract.getAsBonus()) + " bonus for being named an All-Star");
            allStarBonusParent.setVisibility(View.VISIBLE);
        } else {
            allStarBonusParent.setVisibility(View.GONE);
        }
    }

    public BaseTableAdapter getPitchingAdapter() {
        PitchingCareerStatsAdapter adapter = new PitchingCareerStatsAdapter(getActivity());
        List<List<String>> body = getPitchingBody();

        adapter.setFirstHeader("YEAR");
        adapter.setHeader(getPitchingHeader());
        adapter.setFirstBody(body);
        adapter.setBody(body);
        adapter.setSection(body);

        return adapter;
    }

    public BaseTableAdapter getBattingAdapter() {
        BattingCareerStatsAdapter adapter = new BattingCareerStatsAdapter(getActivity());
        List<List<String>> body = getBattingBody();

        adapter.setFirstHeader("YEAR");
        adapter.setHeader(getBattingHeader());
        adapter.setFirstBody(body);
        adapter.setBody(body);
        adapter.setSection(body);

        return adapter;
    }

    public BaseTableAdapter getFieldingAdapter() {
        FieldingCareerStatsAdapter adapter = new FieldingCareerStatsAdapter(getActivity());
        List<List<String>> body = getFieldingBody();

        adapter.setFirstHeader("YEAR");
        adapter.setHeader(getFieldingHeader());
        adapter.setFirstBody(body);
        adapter.setBody(body);
        adapter.setSection(body);

        return adapter;
    }

    private List<String> getBattingHeader() {
        List<String> header = new ArrayList<>();

        header.add("TEAM");
        header.add("G");
        header.add("AB");
        header.add("R");
        header.add("H");
        header.add("2B");
        header.add("3B");
        header.add("HR");
        header.add("RBI");
        header.add("SB");
        header.add("BB");
        header.add("SO");
        header.add("AVG");
        header.add("OBP");
        header.add("SLG");
        header.add("OPS");
        header.add("WAR");

        return header;
    }

    private List<String> getPitchingHeader() {
        List<String> header = new ArrayList<>();

        header.add("TEAM");
        header.add("G");
        header.add("GS");
        header.add("CG");
        header.add("W");
        header.add("L");
        header.add("Sv");
        header.add("ERA");
        header.add("IP");
        header.add("H");
        header.add("HR");
        header.add("R");
        header.add("ER");
        header.add("BB");
        header.add("K");
        header.add("WHIP");
        header.add("WAR");

        return header;
    }

    private List<String> getFieldingHeader() {
        List<String> header = new ArrayList<>();

        header.add("TEAM");
        header.add("POS");
        header.add("G");
        header.add("GS");
        header.add("PO");
        header.add("A");
        header.add("DP");
        header.add("TC");
        header.add("E");
        header.add("PCT");
        header.add("INN");
        header.add("RNG");
        header.add("ZR");

        return header;
    }

    private List<List<String>> getBattingBody() {
        List<List<String>> rows = new ArrayList<>();
        List<BattingStatLine> lines = db.battingStatLines(universe_name, league_id, player_id);
        for (BattingStatLine line : lines) {
            List<String> cols = new ArrayList<>();
            cols.add(line.getYear());
            cols.add(line.getTeam());
            cols.add(line.getGames());
            cols.add(line.getAtbats());
            cols.add(line.getRuns());
            cols.add(line.getHits());
            cols.add(line.getDoubles());
            cols.add(line.getTriples());
            cols.add(line.getHomeruns());
            cols.add(line.getRbi());
            cols.add(line.getSb());
            cols.add(line.getWalks());
            cols.add(line.getStrikeouts());
            cols.add(line.getAvg());
            cols.add(line.getObp());
            cols.add(line.getSlg());
            cols.add(line.getOps());
            cols.add(line.getWar());
            rows.add(cols);
        }
        return rows;
    }

    private List<List<String>> getPitchingBody() {
        List<List<String>> rows = new ArrayList<>();
        List<PitchingStatLine> lines = db.pitchingStatLines(universe_name, league_id, player_id);
        for (PitchingStatLine line : lines) {
            List<String> cols = new ArrayList<>();
            cols.add(line.getYear());
            cols.add(line.getTeam());
            cols.add(line.getGames());
            cols.add(line.getGamesStarted());
            cols.add(line.getCompleteGames());
            cols.add(line.getWins());
            cols.add(line.getLosses());
            cols.add(line.getSaves());
            cols.add(line.getEra());
            cols.add(line.getInningsPitched());
            cols.add(line.getHits());
            cols.add(line.getHomeRuns());
            cols.add(line.getRuns());
            cols.add(line.getEarnedRuns());
            cols.add(line.getWalks());
            cols.add(line.getStrikeouts());
            cols.add(line.getWhip());
            cols.add(line.getWar());
            rows.add(cols);
        }
        return rows;
    }

    private List<List<String>> getFieldingBody() {
        List<List<String>> rows = new ArrayList<>();
        List<FieldingStatLine> lines = db.fieldingStatLines(universe_name, league_id, player_id);
        for (FieldingStatLine line : lines) {
            List<String> cols = new ArrayList<>();
            cols.add(line.getYear());
            cols.add(line.getTeam());
            cols.add(line.getPosition());
            cols.add(line.getGames());
            cols.add(line.getGamesStarted());
            cols.add(line.getPutouts());
            cols.add(line.getAssists());
            cols.add(line.getDoublePlays());
            cols.add(line.getTotalChances());
            cols.add(line.getErrors());
            cols.add(line.getFieldingPercentage());
            cols.add(line.getInningsPlayed());
            cols.add(line.getRangeFactor());
            cols.add(line.getZoneRating());
            rows.add(cols);
        }
        return rows;
    }

    private static int age(String birthday, String today) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy/MM/dd");
        DateTime d1 = dateTimeFormatter.parseDateTime(birthday);
        DateTime now = dateTimeFormatter.parseDateTime(today);
        Period p = new Period(d1, now);
        return p.getYears();
    }

    private static String centimeterToFeet(String centemeter) {
        int feetPart = 0;
        int inchesPart = 0;
        if(!TextUtils.isEmpty(centemeter)) {
            double dCentimeter = Double.valueOf(centemeter);
            feetPart = (int) Math.floor((dCentimeter / 2.54) / 12);
            inchesPart = (int) Math.round((dCentimeter / 2.54) - (feetPart * 12));
            if (inchesPart == 12) {
                feetPart++;
                inchesPart = 0;
            }
        }
        return String.format("%d' %d\"", feetPart, inchesPart);
    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().
                getSystemService(android.content.Context.INPUT_METHOD_SERVICE);

        if (getActivity().getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(
                    getActivity().getCurrentFocus()
                            .getWindowToken(), 0);
        }
    }

    private String formatSalary(int val) {
        Float tmp = Float.valueOf(val);
        if (tmp == 0) {
            return "$0";
        } else if ((tmp/1000000) < 1) {
            return "$"+(String.valueOf(tmp/1000).replaceAll("\\.0*$", ""))+"K";
        } else {
            return "$"+(String.valueOf(tmp/1000000).replaceAll("\\.0*$", ""))+"M";
        }
    }

    private SpannableStringBuilder boldTags(String text){
        final Pattern boldMe = Pattern.compile("[0-9][0-9][0-9][0-9]:");

        final SpannableStringBuilder spannable = new SpannableStringBuilder(text);

        final Matcher matcher = boldMe.matcher(text);

        while (matcher.find()) {
            CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(mContext.getResources().getAssets(), "fonts/OpenSans-Bold.ttf"));
            spannable.setSpan(
                    typefaceSpan, matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }
        return spannable;
    }
}