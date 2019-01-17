package com.mobileootpinfo.mobileootpinfo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.mobileootpinfo.mobileootpinfo.model.PendingGameSummary;
import com.mobileootpinfo.mobileootpinfo.util.constants;
import com.sdsmdg.harjot.vectormaster.VectorMasterDrawable;
import com.sdsmdg.harjot.vectormaster.models.PathModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.mobileootpinfo.mobileootpinfo.util.ColorUtils.getContrastColor;


public class PendingGameSummaryAdapter extends ArrayAdapter<PendingGameSummary> {

    private Context mContext;
    private List<PendingGameSummary> pendingGameSummaries = new ArrayList<PendingGameSummary>();
    private String html_root;
    private String league_name;
    private String PendingGame_date;
    private int league_id;
    private Bitmap homeLogo;
    private int textColor;

    public PendingGameSummaryAdapter(@NonNull Context context, List<PendingGameSummary> list, String html, String date, int id) {
        super(context, 0, list);
        mContext = context;
        pendingGameSummaries = list;
        html_root = html;
        PendingGame_date = date;
        league_id = id;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.pending_scoreboard_card, parent, false);

        final PendingGameSummary currentGame = pendingGameSummaries.get(position);

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

        TextView awayAbbr = listItem.findViewById(R.id.awayAbbr);
        awayAbbr.setText(" "+currentGame.getAwayTeamAbbr()+":");

        TextView homeAbbr = listItem.findViewById(R.id.homeAbbr);
        homeAbbr.setText(" "+currentGame.getHomeTeamAbbr()+":");

        TextView gameTime = listItem.findViewById(R.id.gameTime);
        try {
            SimpleDateFormat time = new SimpleDateFormat("hhmm");
            SimpleDateFormat localTime = new SimpleDateFormat("h:mm a");
            Date d1 = time.parse(currentGame.getGameTime());
            String formattedTime = localTime.format(d1);
            gameTime.setText(formattedTime);
        } catch (ParseException e) {
            e.printStackTrace();
            gameTime.setText(""+currentGame.getGameTime());
        }

        
        TextView homePitcher = listItem.findViewById(R.id.homePitcher);
        homePitcher.setText(currentGame.getHomeStarterName());

        TextView awayPitcher = listItem.findViewById(R.id.awayPitcher);
        awayPitcher.setText(currentGame.getAwayStarterName());

        TextView awayTeamRecord = listItem.findViewById(R.id.awayTeamRecord);
        awayTeamRecord.setText(currentGame.getAwayRecord());

        TextView homeTeamRecord = listItem.findViewById(R.id.homeTeamRecord);
        homeTeamRecord.setText(currentGame.getHomeRecord());

        TextView homePitcherStatLine = listItem.findViewById(R.id.homePitcherStatLine);
        homePitcherStatLine.setText(currentGame.getHomeStarterLine());

        TextView awayPitcherStatLine = listItem.findViewById(R.id.awayPitcherStatLine);
        awayPitcherStatLine.setText(currentGame.getAwayStarterLine());

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
        away_black.setFillColor(Color.parseColor(currentGame.getAwayBGColor()));

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
                        .placeholder(away_baseball)
                        .fallback(away_baseball)
                )
                .into(homeTeamLogo);

        return listItem;
    }
}
