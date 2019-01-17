package com.mobileootpinfo.mobileootpinfo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mobileootpinfo.mobileootpinfo.R;
import com.mobileootpinfo.mobileootpinfo.model.ShortPlayer;

import java.text.Collator;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class RetiredPlayerListAdapter extends ArrayAdapter<ShortPlayer> implements SectionIndexer {

    private Context mContext;
    private List<ShortPlayer> playerList = new ArrayList<ShortPlayer>();
    private String html_root;
    private String league_name;
    private Filter filter;
    private ArrayList<ShortPlayer> original;
    private ArrayList<ShortPlayer> fitems;
    private HashMap<String, Integer> alphaIndexer;
    private String[] sections;

    public RetiredPlayerListAdapter(@NonNull Context context, List<ShortPlayer> list, String html, String league) {
        super(context, 0, list);
        mContext = context;
        playerList = list;
        html_root = html;
        league_name = league;
        this.original = new ArrayList<ShortPlayer>(list);
        this.fitems = new ArrayList<ShortPlayer>(list);
        alphaIndexer = new HashMap<String, Integer>();
        for (int i = 0; i < list.size(); i++)
        {
            String s = list.get(i).lastName.substring(0, 1).toUpperCase();
            if (!alphaIndexer.containsKey(s))
                alphaIndexer.put(s, i);
        }
        Set<String> sectionLetters = alphaIndexer.keySet();
        ArrayList<String> sectionList = new ArrayList<String>(sectionLetters);
        Collator coll = Collator.getInstance(Locale.getDefault());
        coll.setStrength(Collator.PRIMARY);
        Collections.sort(sectionList, coll);
        sections = new String[sectionList.size()];
        for (int i = 0; i < sectionList.size(); i++)
            sections[i] = sectionList.get(i);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.player_list_layout, parent, false);

        final ShortPlayer currentPlayer = playerList.get(position);

        TextView id = listItem.findViewById(R.id.playerID);
        id.setText(""+currentPlayer.getId());

        TextView name = listItem.findViewById(R.id.playerDetail);
        name.setText(currentPlayer.getFirstName() + " " + currentPlayer.getLastName());

        ImageView playerPicture = listItem.findViewById(R.id.playerPicture);

        Glide.with(mContext)
                .load(html_root+mContext.getString(R.string.player_image, currentPlayer.getId()))
                .apply(new RequestOptions()
                        .error(R.drawable.person_silhouette)
                )
                .into(playerPicture);

        return listItem;
    }

    public int getPositionForSection(int section)
    {
        return alphaIndexer.get(sections[section]);
    }

    public int getSectionForPosition(int position)
    {
        return 1;
    }

    public Object[] getSections()
    {
        return sections;
    }

    @Override
    public Filter getFilter()
    {
        if (filter == null)
            filter = new PlayerFilter();

        return filter;
    }

    private class PlayerFilter extends Filter
    {
        @Override
        protected FilterResults performFiltering(CharSequence constraint)
        {
            FilterResults results = new FilterResults();
            String prefix = constraint.toString().toLowerCase();

            if (prefix == null || prefix.length() == 0)
            {
                ArrayList<ShortPlayer> list = new ArrayList<ShortPlayer>(original);
                results.values = list;
                results.count = list.size();
            }
            else
            {
                final ArrayList<ShortPlayer> list = new ArrayList<ShortPlayer>(original);
                final ArrayList<ShortPlayer> nlist = new ArrayList<ShortPlayer>();
                int count = list.size();

                for (int i=0; i<count; i++)
                {
                    final ShortPlayer player = list.get(i);
                    final String value = Normalizer.normalize(player.getFirstName().toLowerCase() + " " + player.getLastName().toLowerCase(), Normalizer.Form.NFD)
                            .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

                    if (value.contains(prefix))
                    {
                        nlist.add(player);
                    }
                }
                results.values = nlist;
                results.count = nlist.size();
            }
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            fitems = (ArrayList<ShortPlayer>)results.values;

            clear();
            int count = fitems.size();
            for (int i=0; i<count; i++)
            {
                ShortPlayer player = (ShortPlayer)fitems.get(i);
                add(player);
            }
        }

    }
}
