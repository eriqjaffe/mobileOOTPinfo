package com.mobileootpinfo.mobileootpinfo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mobileootpinfo.mobileootpinfo.R;
import com.mobileootpinfo.mobileootpinfo.model.Player;
import com.mobileootpinfo.mobileootpinfo.util.constants;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eriq on 3/7/2018.
 */

public class RosterAdapter extends ArrayAdapter<Player> {

    private Context mContext;
    private List<Player> roster = new ArrayList<Player>();
    private String league_name, game_date, html_root;

    public RosterAdapter(@NonNull Context context, List<Player> list, String html, String date, String league) {
        super(context, 0, list);
        mContext = context;
        roster = list;
        league_name = league;
        game_date = date;
        html_root = html;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.roster_list_layout, parent, false);

        final Player player = roster.get(position);

        TextView id = listItem.findViewById(R.id.playerID);
        id.setText(""+player.getId());

        TextView uniformNumber = listItem.findViewById(R.id.uniformNumber);
        uniformNumber.setText(""+player.getUniformNumber());

        TextView playerName = listItem.findViewById(R.id.playerName);
        playerName.setText(player.getFirstName() + " " + player.getLastName());

        TextView playerPosition = listItem.findViewById(R.id.playerPosition);
        playerPosition.setText(constants.positions[player.getPosition()]);

        TextView playerAge = listItem.findViewById(R.id.playerAge);
        playerAge.setText(""+age(player.getBirthday(), game_date));

        TextView playerBT = listItem.findViewById(R.id.playerBT);
        playerBT.setText(constants.batting[player.getPlayerBats()]+"/"+constants.throwing[player.getPlayerThrows()]);

        ImageView playerPicture = listItem.findViewById(R.id.playerPicture);

        Glide.with(mContext)
                .load(html_root+mContext.getString(R.string.player_image, player.getId()))
                .apply(new RequestOptions()
                        .error(R.drawable.person_silhouette)
                )
                .into(playerPicture);

        return listItem;
    }

    private static int age(String birthday, String today) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy/MM/dd");
        DateTime d1 = dateTimeFormatter.parseDateTime(birthday);
        DateTime now = dateTimeFormatter.parseDateTime(today);
        Period p = new Period(d1, now);
        return p.getYears();
    }
}
