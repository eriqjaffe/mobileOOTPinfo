package com.mobileootpinfo.mobileootpinfo.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mobileootpinfo.mobileootpinfo.R;
import com.mobileootpinfo.mobileootpinfo.activity.NavDrawer;
import com.mobileootpinfo.mobileootpinfo.adapter.PlayerListAdapter;
import com.mobileootpinfo.mobileootpinfo.adapter.RetiredPlayerListAdapter;
import com.mobileootpinfo.mobileootpinfo.model.Player;
import com.mobileootpinfo.mobileootpinfo.model.ShortPlayer;
import com.mobileootpinfo.mobileootpinfo.util.CalligraphyDefaultTabLayout;
import com.mobileootpinfo.mobileootpinfo.util.DatabaseHandler;

import java.util.List;

import static com.mobileootpinfo.mobileootpinfo.util.ColorUtils.getContrastColor;

/**
 * Created by eriqj on 2/20/2018.
 */

public class Players extends Fragment {

    private ListView listView;
    private List<Player> Players;
    private List<Player> RetiredPlayers;
    private View view;
    private PlayerListAdapter playerListAdapter;
    private RetiredPlayerListAdapter retiredPlayerListAdapter;
    private Context mContext;
    private String html_root;
    private String universe_name;
    private int league_id;
    private String bg_color;
    private String text_color;
    private String game_date;
    private String league_abbr;
    private CalligraphyDefaultTabLayout playerTabLayout;
    private LinearLayout activeParent, retiredParent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View view = inflater.inflate(R.layout.fragment_players, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Players");

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            html_root = bundle.getString("html_root");
            universe_name = bundle.getString("universe_name");
            league_id = bundle.getInt("league_id");
            bg_color = bundle.getString("bg_color");
            text_color = bundle.getString("text_color");
            game_date = bundle.getString("game_date");
            league_abbr = bundle.getString("league_abbr");
        }

        activeParent = view.findViewById(R.id.activeParent);
        retiredParent = view.findViewById(R.id.retiredParent);

        playerTabLayout = view.findViewById(R.id.playerTabLayout);
        playerTabLayout.addTab(playerTabLayout.newTab().setText("Active"));
        playerTabLayout.addTab(playerTabLayout.newTab().setText("Retired"));

        playerTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        activeParent.setVisibility(View.VISIBLE);
                        retiredParent.setVisibility(View.GONE);
                        break;
                    case 1:
                        activeParent.setVisibility(View.GONE);
                        retiredParent.setVisibility(View.VISIBLE);
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

        ListView listView = view.findViewById(R.id.player_list_view);
        EditText inputSearch = view.findViewById(R.id.inputSearch);

        populateListView(getActivity(), listView);

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                Players.this.playerListAdapter.getFilter().filter(cs);

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                Players.this.playerListAdapter.getFilter().filter(arg0.toString());
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                TextView playerID = view.findViewById(R.id.playerID);
                Bundle bundle = new Bundle();
                bundle.putInt("player_id", Integer.parseInt(playerID.getText().toString()));
                bundle.putString("html_root", html_root);
                bundle.putString("universe_name", universe_name);
                bundle.putString("game_date", game_date);
                bundle.putInt("league_id", league_id);
                bundle.putString("league_abbr", league_abbr);
                Fragment fragment = new PlayerCard();
                fragment.setArguments(bundle);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.hide(getActivity().getSupportFragmentManager().findFragmentByTag("nav_players"));
                ft.add(R.id.currentFrame, fragment, "player_card");
                ft.addToBackStack(null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }
        });

        ListView retiredListView = view.findViewById(R.id.retired_player_list_view);
        EditText retiredInputSearch = view.findViewById(R.id.retiredInputSearch);

        populateListView(getActivity(), listView);

        retiredInputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                Players.this.retiredPlayerListAdapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                Players.this.retiredPlayerListAdapter.getFilter().filter(arg0.toString());
            }
        });

        retiredListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                TextView playerID = view.findViewById(R.id.playerID);
                Bundle bundle = new Bundle();
                bundle.putInt("player_id", Integer.parseInt(playerID.getText().toString()));
                bundle.putString("html_root", html_root);
                bundle.putString("universe_name", universe_name);
                bundle.putString("game_date", game_date);
                bundle.putInt("league_id", league_id);
                bundle.putString("league_abbr", league_abbr);
                Fragment fragment = new PlayerCard();
                fragment.setArguments(bundle);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.hide(getActivity().getSupportFragmentManager().findFragmentByTag("nav_players"));
                ft.add(R.id.currentFrame, fragment, "player_card");
                ft.addToBackStack(null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }
        });

        populateRetiredListView(getActivity(), retiredListView);

        ((NavDrawer) getActivity())
                .formatActionBar(league_abbr + " Players", null,
                        Color.parseColor(bg_color), getContrastColor(Color.parseColor(bg_color)));
    }

    private void populateListView(Context ctx, ListView listView) {
        //ListView listView = view.findViewById(R.id.league_list_view);
        DatabaseHandler db = new DatabaseHandler(ctx);
        List<ShortPlayer> players = db.getAllPlayers(universe_name, league_id);
        playerListAdapter = new PlayerListAdapter(ctx, players, html_root, universe_name);
        listView.setAdapter(playerListAdapter);
        listView.setFastScrollEnabled(true);
    }

    private void populateRetiredListView(Context ctx, ListView listView) {
        //ListView listView = view.findViewById(R.id.league_list_view);
        DatabaseHandler db = new DatabaseHandler(ctx);
        List<ShortPlayer> players = db.getAllRetiredPlayers(universe_name, league_id);
        retiredPlayerListAdapter = new RetiredPlayerListAdapter(ctx, players, html_root, universe_name);
        listView.setAdapter(retiredPlayerListAdapter);
        listView.setFastScrollEnabled(true);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            ((NavDrawer) getActivity())
                    .formatActionBar(league_abbr + " Players", null,
                            Color.parseColor(bg_color), getContrastColor(Color.parseColor(bg_color)));
        }
    }
}
