package com.mobileootpinfo.mobileootpinfo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

public class SectionedRosterAdapter extends BaseAdapter {
    private ArrayList<Object> roster;
    private LayoutInflater inflater;
    private static final int TYPE_PERSON = 0;
    private static final int TYPE_DIVIDER = 1;
    private Context mContext;
    private String html_root, game_date;

    public SectionedRosterAdapter(Context context, ArrayList<Object> roster, String html_root, String game_date) {
        this.mContext = context;
        this.roster = roster;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.html_root = html_root;
        this.game_date = game_date;
    }

    @Override
    public int getCount() {
        return roster.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return roster.get(position);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) instanceof Player) {
            return TYPE_PERSON;
        }
        return TYPE_DIVIDER;
    }

    @Override
    public boolean isEnabled(int position) {
        return (getItemViewType(position) == TYPE_PERSON);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case TYPE_PERSON:
                    convertView = inflater.inflate(R.layout.roster_list_layout, parent, false);
                    break;
                case TYPE_DIVIDER:
                    convertView = inflater.inflate(R.layout.layout_section, parent, false);
            }
        }

        switch (type) {
            case TYPE_PERSON:
                Player player= (Player)getItem(position);
                TextView id = convertView.findViewById(R.id.playerID);
                id.setText(""+player.getId());

                TextView uniformNumber = convertView.findViewById(R.id.uniformNumber);
                uniformNumber.setText(""+player.getUniformNumber());

                TextView playerName = convertView.findViewById(R.id.playerName);
                playerName.setText(player.getFirstName() + " " + player.getLastName());

                TextView playerPosition = convertView.findViewById(R.id.playerPosition);
                playerPosition.setText(constants.positions[player.getPosition()]);

                TextView playerAge = convertView.findViewById(R.id.playerAge);
                playerAge.setText(""+age(player.getBirthday(), game_date));

                TextView playerBT = convertView.findViewById(R.id.playerBT);
                playerBT.setText(constants.batting[player.getPlayerBats()]+"/"+constants.throwing[player.getPlayerThrows()]);

                ImageView playerPicture = convertView.findViewById(R.id.playerPicture);

                Glide.with(mContext)
                        .load(html_root+mContext.getString(R.string.player_image, player.getId()))
                        .apply(new RequestOptions()
                                .error(R.drawable.person_silhouette)
                        )
                        .into(playerPicture);
                break;
            case TYPE_DIVIDER:
                TextView sectionTitle = convertView.findViewById(R.id.sectionTitle);
                sectionTitle.setText((String)getItem(position));
                break;
        }
        return convertView;
    }

    private int age(String birthday, String today) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy/MM/dd");
        DateTime d1 = dateTimeFormatter.parseDateTime(birthday);
        DateTime now = dateTimeFormatter.parseDateTime(today);
        Period p = new Period(d1, now);
        return p.getYears();
    }
}
