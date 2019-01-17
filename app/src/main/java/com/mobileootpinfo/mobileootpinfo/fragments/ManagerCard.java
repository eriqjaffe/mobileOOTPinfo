package com.mobileootpinfo.mobileootpinfo.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.inqbarna.tablefixheaders.TableFixHeaders;
import com.inqbarna.tablefixheaders.adapters.BaseTableAdapter;
import com.mobileootpinfo.mobileootpinfo.R;
import com.mobileootpinfo.mobileootpinfo.activity.NavDrawer;
import com.mobileootpinfo.mobileootpinfo.adapter.ManagerYearLineAdapter;
import com.mobileootpinfo.mobileootpinfo.model.Manager;
import com.mobileootpinfo.mobileootpinfo.model.ManagerStatLine;
import com.mobileootpinfo.mobileootpinfo.util.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

import static com.mobileootpinfo.mobileootpinfo.util.ColorUtils.getContrastColor;

public class ManagerCard extends Fragment {

    private View view;
    private DatabaseHandler db;
    private Context mContext;
    private int manager_id, league_id;
    private String html_root, universe_name, bg_color;
    private Manager manager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        view = inflater.inflate(R.layout.fragment_manager_card, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = getActivity();

        db = new DatabaseHandler(mContext);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            html_root = bundle.getString("html_root");
            universe_name = bundle.getString("universe_name");
            manager_id = bundle.getInt("manager_id");
            league_id = bundle.getInt("league_id");
            bg_color = bundle.getString("bg_color");
        }

        manager = db.getManager(universe_name, league_id, manager_id);

        ((NavDrawer) getActivity())
                .formatActionBar(manager.getFirstName()+ " "+manager.getLastName(), null,
                        Color.parseColor(bg_color), getContrastColor(Color.parseColor(bg_color)));

        ImageView officeOverlay = view.findViewById(R.id.officeOverlay);
        String transparentColor = "#00000000";
        String opaqueColor = "#FF000000";
        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] {Color.parseColor(transparentColor),Color.parseColor(opaqueColor)});
        gd.setCornerRadius(0f);
        officeOverlay.setImageDrawable(gd);

        TextView experience = view.findViewById(R.id.experience);
        experience.setText(manager.getYears()+" Years");

        TextView wonLoss = view.findViewById(R.id.wonLoss);
        wonLoss.setText(manager.getW() + "-" + manager.getL());

        TextView winningPct = view.findViewById(R.id.winningPct);
        winningPct.setText(manager.getPct());

        TextView championships = view.findViewById(R.id.championships);
        championships.setText(manager.getChampionships() + getString(R.string._championships));

        populateManager(universe_name, manager_id);
    }

    private void populateManager(String universe_name, int manager_id) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        TableFixHeaders tablefixheaders = (TableFixHeaders) view.findViewById(R.id.managerStatTable);
        tablefixheaders.setAdapter(getManagerAdapter());
//        ListView listView = getActivity().findViewById(R.id.managerStatListView);
//        List<ManagerStatLine> statLines = db.managerHistory(universe_name, manager_id);
//        ManagerYearLineAdapter mya = new ManagerYearLineAdapter(mContext, statLines);
//        listView.setAdapter(mya);
    }

    public BaseTableAdapter getManagerAdapter() {
        ManagerYearLineAdapter adapter = new ManagerYearLineAdapter(getActivity());
        List<List<String>> body = getManagerBody();

        adapter.setFirstHeader("YEAR");
        adapter.setHeader(getManagerHeader());
        adapter.setFirstBody(body);
        adapter.setBody(body);
        adapter.setSection(body);

        return adapter;
    }

    private List<String> getManagerHeader() {
        List<String> header = new ArrayList<>();

        header.add("TEAM");
        header.add("LG");
        header.add("W");
        header.add("L");
        header.add("PCT");
        header.add("AVG");
        header.add("HR");
        header.add("SB");
        header.add("ERA");
        header.add("SO");

        return header;
    }

    private List<List<String>> getManagerBody() {
        List<List<String>> rows = new ArrayList<>();
        List<ManagerStatLine> lines = db.managerHistory(universe_name, manager_id);
        for (ManagerStatLine line : lines) {
            List<String> cols = new ArrayList<>();
            String name = line.getAbbr();
            cols.add(String.valueOf(line.getYear()));
            if (line.getMadePlayoffs() == 1) {
                name += "*";
            }
            if (line.getWonPlayoffs() == 1) {
                name += "*";
            }
            cols.add(name);
            cols.add(line.getLeagueAbbr());
            cols.add(String.valueOf(line.getW()));
            cols.add(String.valueOf(line.getL()));
            cols.add(line.getPct());
            cols.add(line.getAvg());
            cols.add(String.valueOf(line.getHr()));
            cols.add(String.valueOf(line.getSb()));
            cols.add(line.getEra());
            cols.add(String.valueOf(line.getK()));
            rows.add(cols);
        }
        return rows;
    }
}
