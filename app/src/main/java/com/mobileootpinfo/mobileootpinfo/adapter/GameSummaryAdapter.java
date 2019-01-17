package com.mobileootpinfo.mobileootpinfo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mobileootpinfo.mobileootpinfo.R;
import com.mobileootpinfo.mobileootpinfo.fragments.BoxScore;
import com.mobileootpinfo.mobileootpinfo.model.GameSummary;
import com.mobileootpinfo.mobileootpinfo.util.constants;
import com.sdsmdg.harjot.vectormaster.VectorMasterDrawable;
import com.sdsmdg.harjot.vectormaster.models.PathModel;

import java.util.ArrayList;
import java.util.List;

import static com.mobileootpinfo.mobileootpinfo.util.ColorUtils.getContrastColor;


public class GameSummaryAdapter extends ArrayAdapter<GameSummary> {

    private Context mContext;
    private List<GameSummary> gameSummaries = new ArrayList<GameSummary>();
    private String html_root;
    private String universe_name;
    private String game_date;
    private String league_abbr;
    private Bitmap homeLogo;
    private int textColor;
    private int league_id;

    public GameSummaryAdapter(@NonNull Context context, List<GameSummary> list, String html, String date, int id, String universe, String abbr) {
        super(context, 0, list);
        mContext = context;
        gameSummaries = list;
        html_root = html;
        game_date = date;
        league_id = id;
        universe_name = universe;
        league_abbr = abbr;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.scoreboard_card, parent, false);

        final GameSummary currentGame = gameSummaries.get(position);

        TextView gameID = listItem.findViewById(R.id.gameID);
        gameID.setText(String.valueOf(currentGame.getGameId()));

        LinearLayout gameTypeWrapper = listItem.findViewById(R.id.gameTypeWrapper);

        if (currentGame.getGameType() > 0) {
            gameTypeWrapper.setVisibility(View.VISIBLE);
            TextView gameType = listItem.findViewById(R.id.gameType);
            gameType.setText(constants.game_types[currentGame.getGameType()].toUpperCase());
        }

        TextView awayTeam = listItem.findViewById(R.id.awayTeam);
        awayTeam.setText(currentGame.getAwayTeamName() + " "+currentGame.getAwayTeamNickname());

        TextView homeTeam = listItem.findViewById(R.id.homeTeam);
        homeTeam.setText(currentGame.getHomeTeamName() + " "+currentGame.getHomeTeamNickhame());

        TextView awayScore = listItem.findViewById(R.id.awayScore);
        awayScore.setText(""+currentGame.getAwayScore());

        TextView homeScore = listItem.findViewById(R.id.homeScore);
        homeScore.setText(""+currentGame.getHomeScore());

        TextView winningPitcher = listItem.findViewById(R.id.winningPitcher);
        winningPitcher.setText(currentGame.getWinningPitcherName());

        TextView losingPitcher = listItem.findViewById(R.id.losingPitcher);
        losingPitcher.setText(currentGame.getLosingPitcherName());

        TextView awayTeamRecord = listItem.findViewById(R.id.awayTeamRecord);
        awayTeamRecord.setText(currentGame.getAwayRecord());

        TextView homeTeamRecord = listItem.findViewById(R.id.homeTeamRecord);
        homeTeamRecord.setText(currentGame.getHomeRecord());

        TextView winningPitcherLine = listItem.findViewById(R.id.winningPitcherStatLine);
        winningPitcherLine.setText(currentGame.getWinningPitcherLine());

        TextView losingPitcherLine = listItem.findViewById(R.id.losingPitcherStatLine);
        losingPitcherLine.setText(currentGame.getLosingPitcherLine());

        TextView s = listItem.findViewById(R.id.s);
        TextView savingPitcher = listItem.findViewById(R.id.savingPitcher);
        TextView savingPitcherLine = listItem.findViewById(R.id.savingPitcherStatLine);
        if (currentGame.getSavingPitcher() > 0) {
            s.setVisibility(View.VISIBLE);
            savingPitcher.setVisibility(View.VISIBLE);
            savingPitcherLine.setVisibility(View.VISIBLE);
            savingPitcher.setText(currentGame.getSavingPitcherName());
            savingPitcherLine.setText(currentGame.getSavingPitcherLine());
        } else {
            s.setVisibility(View.GONE);
            savingPitcher.setVisibility(View.GONE);
            savingPitcherLine.setVisibility(View.GONE);
        }

        if (Color.parseColor(currentGame.getAwayBGColor()) == Color.parseColor(currentGame.getAwayTextColor())) {
            textColor = getContrastColor(Color.parseColor(currentGame.getAwayTextColor()));
        } else {
            textColor = Color.parseColor(currentGame.getAwayTextColor());
        }

        ImageView awayTeamLogo = listItem.findViewById(R.id.awayLogo);

        VectorMasterDrawable away_baseball = new VectorMasterDrawable(getContext(), R.drawable.ic_baseball_2);
        PathModel away_black = away_baseball.getPathModelByName("black");
        away_black.setFillColor(textColor);
        PathModel away_white = away_baseball.getPathModelByName("white");
        away_white.setFillColor(Color.parseColor(currentGame.getAwayBGColor()));

        Glide.with(mContext)
                .load(html_root + mContext.getString(R.string.team_logo, currentGame.getAwayTeamLogo()))
                .apply(new RequestOptions()
                    .error(away_baseball)
                )
                .into(awayTeamLogo);

        if (Color.parseColor(currentGame.getHomeBGColor()) == Color.parseColor(currentGame.getHomeTextColor())) {
            textColor = getContrastColor(Color.parseColor(currentGame.getHomeTextColor()));
        } else {
            textColor = Color.parseColor(currentGame.getHomeTextColor());
        }

        ImageView homeTeamLogo = listItem.findViewById(R.id.homeLogo);

        VectorMasterDrawable home_baseball = new VectorMasterDrawable(getContext(), R.drawable.ic_baseball_2);
        PathModel home_black = away_baseball.getPathModelByName("black");
        home_black.setFillColor(textColor);
        PathModel home_white = away_baseball.getPathModelByName("white");
        home_white.setFillColor(Color.parseColor(currentGame.getAwayBGColor()));

        Glide.with(mContext)
                .load(html_root + mContext.getString(R.string.team_logo, currentGame.getHomeTeamLogo()))
                .apply(new RequestOptions()
                    .error(away_baseball)
                )
                .into(homeTeamLogo);

        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                TextView gameID = v.findViewById(R.id.gameID);
                bundle.putString("html_root", html_root);
                bundle.putString("game_date", game_date);
                bundle.putString("universe_name", universe_name);
                bundle.putInt("league_id", league_id);
                bundle.putString("league_abbr", league_abbr);
                bundle.putInt("game_type", currentGame.getGameType());
                bundle.putSerializable("game_summary", currentGame);
                Fragment fragment = new BoxScore();
                fragment.setArguments(bundle);
                FragmentTransaction ft = ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction();
                ft.hide(((AppCompatActivity) mContext).getSupportFragmentManager().findFragmentByTag("nav_scores"));
                ft.add(R.id.currentFrame, fragment, "box_score");
                ft.addToBackStack(null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }
        });

        return listItem;
    }
}
