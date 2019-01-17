package com.mobileootpinfo.mobileootpinfo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mobileootpinfo.mobileootpinfo.R;
import com.mobileootpinfo.mobileootpinfo.model.Team;
import com.sdsmdg.harjot.vectormaster.VectorMasterDrawable;
import com.sdsmdg.harjot.vectormaster.models.PathModel;

import java.util.ArrayList;
import java.util.List;

import static com.mobileootpinfo.mobileootpinfo.util.ColorUtils.getContrastColor;

/**
 * Created by Eriq on 2/24/2018.
 */

public class TeamListAdapter extends ArrayAdapter<Team> {

    private Context mContext;
    private List<Team> teamList = new ArrayList<Team>();
    private String html_root;
    private String league_name;
    private Filter filter;
    private int textColor;

    public TeamListAdapter(@NonNull Context context, List<Team> list, String html, String league) {
        super(context, 0, list);
        mContext = context;
        teamList = list;
        html_root = html;
        league_name = league;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.team_list_layout, parent, false);

        final Team currentTeam = teamList.get(position);

        TextView id = listItem.findViewById(R.id.teamID);
        id.setText(""+currentTeam.getId());

        TextView name = listItem.findViewById(R.id.teamName);
        name.setText(currentTeam.getTeamName() + " " + currentTeam.getTeamNickname());

        if (Color.parseColor(currentTeam.getTextColor()) == Color.parseColor(currentTeam.getBackgroundColor())) {
            textColor = getContrastColor(Color.parseColor(currentTeam.getTextColor()));
        } else {
            textColor = Color.parseColor(currentTeam.getTextColor());
        }

        final VectorMasterDrawable baseball = new VectorMasterDrawable(getContext(), R.drawable.ic_baseball_2);
        PathModel black = baseball.getPathModelByName("black");
        black.setFillColor(textColor);
        PathModel white = baseball.getPathModelByName("white");
        white.setFillColor(Color.parseColor(currentTeam.getBackgroundColor()));

        final ImageView teamLogo = listItem.findViewById(R.id.teamLogo);
        teamLogo.setImageDrawable(baseball);

        Glide.with(mContext)
                .load(html_root + mContext.getString(R.string.team_logo, currentTeam.getTeamLogo()))
                .apply(new RequestOptions()
                        .error(baseball)
                )
                .into(teamLogo);

        return listItem;
    }
}
