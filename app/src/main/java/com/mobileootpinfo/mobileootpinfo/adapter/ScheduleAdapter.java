package com.mobileootpinfo.mobileootpinfo.adapter;

import android.content.Context;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mobileootpinfo.mobileootpinfo.R;
import com.mobileootpinfo.mobileootpinfo.fragments.BoxScore;
import com.mobileootpinfo.mobileootpinfo.model.GameSummary;
import com.mobileootpinfo.mobileootpinfo.model.ScheduleLineGame;
import com.mobileootpinfo.mobileootpinfo.util.DatabaseHandler;
import com.mobileootpinfo.mobileootpinfo.util.constants;
import com.sdsmdg.harjot.vectormaster.VectorMasterDrawable;
import com.sdsmdg.harjot.vectormaster.models.PathModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.mobileootpinfo.mobileootpinfo.util.ColorUtils.getContrastColor;

/**
 * Created by Eriq on 3/7/2018.
 */

public class ScheduleAdapter extends ArrayAdapter<ScheduleLineGame> {

    private Context mContext;
    private List<ScheduleLineGame> schedule = new ArrayList<ScheduleLineGame>();
    private String league_name, game_date, html_root, universe_name, league_abbr, game_display_date;
    private int textColor, league_id;
    private DatabaseHandler db;

    public ScheduleAdapter(@NonNull Context context, List<ScheduleLineGame> list, String league, String html, String date, String universe, int id, String abbr) {
        super(context, 0, list);
        mContext = context;
        schedule = list;
        league_name = league;
        game_date = date;
        html_root = html;
        universe_name = universe;
        league_id = id;
        league_abbr = abbr;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.schedule_list_layout, parent, false);

        final ScheduleLineGame game = schedule.get(position);

        RelativeLayout scheduleLineParent = listItem.findViewById(R.id.scheduleLineParent);

        TextView gameID = listItem.findViewById(R.id.gameID);
        gameID.setText(String.valueOf(game.getGameID()));

        TextView gameDate = listItem.findViewById(R.id.gameDate);

        try {
            SimpleDateFormat sqliteSdf = new SimpleDateFormat("yyyy/MM/dd");
            Date date = sqliteSdf.parse(game.getGameDate());
            DateFormat formatter = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
            if (game.getGameType() > 0) {
                gameDate.setText(formatter.format(date) + "\n" + constants.short_game_types[game.getGameType()]);
            } else {
                gameDate.setText(formatter.format(date));
            }
        } catch (ParseException e) {
            e.printStackTrace();
            if (game.getGameType() > 0) {
                gameDate.setText(game.getGameDate() + "\n" + constants.short_game_types[game.getGameType()]);
            } else {
                gameDate.setText(game.getGameDate());
            }
        }

        TextView opponentName = listItem.findViewById(R.id.opponentName);
        if (game.isHome()) {
            scheduleLineParent.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            opponentName.setText(game.getOpponent());
        } else {
            scheduleLineParent.setBackgroundColor(mContext.getResources().getColor(R.color.awayGame));
            opponentName.setText("@ "+game.getOpponent());
        }

        LinearLayout playedGame = listItem.findViewById(R.id.playedGame);
        LinearLayout unplayedGame = listItem.findViewById(R.id.unplayedGame);
        if (game.isPlayed()) {
            playedGame.setVisibility(View.VISIBLE);
            unplayedGame.setVisibility(View.GONE);
            TextView gameResult = listItem.findViewById(R.id.gameResult);
            TextView winLoss = listItem.findViewById(R.id.winLoss);
            if (game.isWin()) {
                winLoss.setText("WIN");
                winLoss.setTextColor(mContext.getResources().getColor(R.color.win));
                gameResult.setTextColor(mContext.getResources().getColor(R.color.win));
            } else {
                winLoss.setText("LOSS");
                winLoss.setTextColor(mContext.getResources().getColor(R.color.loss));
                gameResult.setTextColor(mContext.getResources().getColor(R.color.loss));
            }
            gameResult.setText(game.getResult());
        } else {
            playedGame.setVisibility(View.GONE);
            unplayedGame.setVisibility(View.VISIBLE);
            TextView gameTime = listItem.findViewById(R.id.gameTime);
            try {
                SimpleDateFormat time = new SimpleDateFormat("hhmm");
                SimpleDateFormat localTime = new SimpleDateFormat("h:mm a");
                Date d1 = time.parse(game.getGameTime());
                String formattedTime = localTime.format(d1);
                gameTime.setText(formattedTime);
            } catch (ParseException e) {
                e.printStackTrace();
                gameTime.setText(game.getGameTime());
            }
        }

        if (Color.parseColor(game.getBackgroundColor()) == Color.parseColor(game.getTextColor())) {
            textColor = getContrastColor(Color.parseColor(game.getTextColor()));
        } else {
            textColor = Color.parseColor(game.getTextColor());
        }

        VectorMasterDrawable baseball = new VectorMasterDrawable(getContext(), R.drawable.ic_baseball_2);
        PathModel black = baseball.getPathModelByName("black");
        black.setFillColor(textColor);
        PathModel white = baseball.getPathModelByName("white");
        white.setFillColor(Color.parseColor(game.getBackgroundColor()));

        ImageView opponentLogo = listItem.findViewById(R.id.opponentLogo);
        Glide.with(mContext)
                .load(html_root + mContext.getString(R.string.team_logo, game.getOpponentLogo()))
                .apply(new RequestOptions()
                        .error(baseball)
                )
                .into(opponentLogo);

        LinearLayout gameTypeWrapper = listItem.findViewById(R.id.gameTypeWrapper);

//        if (game.getGameType() > 0) {
//            gameTypeWrapper.setVisibility(View.VISIBLE);
//            TextView gameType = listItem.findViewById(R.id.gameType);
//            gameType.setText(constants.game_types[game.getGameType()].toUpperCase() + " " + game.getGameType());
//        }
        if (game.isPlayed()) {
            listItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    db = new DatabaseHandler(mContext);
                    GameSummary gameSummary = db.getGameSummary(universe_name, game.getGameID(), league_id, game_date);
                    TextView gameID = v.findViewById(R.id.gameID);
                    bundle.putString("html_root", html_root);
                    bundle.putString("game_date", game_date);
                    bundle.putString("universe_name", universe_name);
                    bundle.putInt("league_id", league_id);
                    bundle.putString("league_abbr", league_abbr);
                    bundle.putSerializable("game_summary", gameSummary);
                    Fragment fragment = new BoxScore();
                    fragment.setArguments(bundle);
                    FragmentTransaction ft = ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction();
                    ft.hide(((AppCompatActivity) mContext).getSupportFragmentManager().findFragmentByTag("team_card"));
                    ft.add(R.id.currentFrame, fragment, "box_score");
                    ft.addToBackStack(null);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.commit();
                }
            });
        }

        return listItem;
    }

}
