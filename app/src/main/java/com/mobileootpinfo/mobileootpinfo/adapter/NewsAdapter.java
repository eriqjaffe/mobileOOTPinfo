package com.mobileootpinfo.mobileootpinfo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
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
import com.mobileootpinfo.mobileootpinfo.model.NewsArticle;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import uk.co.deanwild.flowtextview.FlowTextView;

public class NewsAdapter extends ArrayAdapter<NewsArticle> {

    private Context mContext;
    private List<NewsArticle> articles = new ArrayList<NewsArticle>();
    private String html_root;
    SimpleDateFormat sqliteSdf = new SimpleDateFormat("yyyy/MM/dd");
    DateFormat formatter = DateFormat.getDateInstance(DateFormat.FULL, Locale.getDefault());

    public NewsAdapter(@NonNull Context context, List<NewsArticle> list, String html) {
        super(context, 0, list);
        mContext = context;
        articles = list;
        html_root = html;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.news_card, parent, false);

        NewsArticle article = articles.get(position);

        TextView headline = listItem.findViewById(R.id.headline);
        headline.setText(article.getSubject());

        TextView articleDate = listItem.findViewById(R.id.articleDate);
        String outDate;
        try {
            Date date = sqliteSdf.parse(article.getDate());
            outDate = formatter.format(date);
        } catch (ParseException e) {
            outDate = article.getDate();
        }
        articleDate.setText(outDate);


        ImageView articleImage = listItem.findViewById(R.id.articleImage);
        Glide.with(mContext)
                .load(html_root + mContext.getString(R.string.player_image, article.getPlayerID()))
                .apply(new RequestOptions()
                        .error(R.drawable.person_silhouette)
                )
                .into(articleImage);

        final FlowTextView articleText = listItem.findViewById(R.id.articleText);
        articleText.setTextSize(38);
        Typeface font = Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans-Regular.ttf");
        articleText.setTypeface(font);
        articleText.setColor(Color.parseColor("#757575"));

        articleText.setText(article.getMessage());

        return listItem;
    }
}
