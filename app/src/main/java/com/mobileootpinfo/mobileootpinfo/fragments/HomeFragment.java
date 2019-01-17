package com.mobileootpinfo.mobileootpinfo.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mobileootpinfo.mobileootpinfo.R;
import com.mobileootpinfo.mobileootpinfo.activity.NavDrawer;
import com.mobileootpinfo.mobileootpinfo.adapter.NewsAdapter;
import com.mobileootpinfo.mobileootpinfo.model.NewsArticle;
import com.mobileootpinfo.mobileootpinfo.util.DatabaseHandler;
import com.sdsmdg.harjot.vectormaster.VectorMasterDrawable;
import com.sdsmdg.harjot.vectormaster.models.PathModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.mobileootpinfo.mobileootpinfo.util.ColorUtils.getContrastColor;
import static com.mobileootpinfo.mobileootpinfo.util.Connectivity.isConnected;

/**
 * Created by eriqj on 3/19/2018.
 */

public class HomeFragment extends Fragment {

    private View view;
    private String league_logo, bg_color, text_color, html_root, league_name, universe_name, league_abbr;
    private int league_id;
    private DatabaseHandler db;
    private SharedPreferences preferences;

    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_blank, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = getContext();
        db = new DatabaseHandler(mContext);
        preferences = PreferenceManager.getDefaultSharedPreferences(mContext);

        final ImageView splashScreenBall = view.findViewById(R.id.splashScreenBall);

        Bundle bundle = this.getArguments();

        if (bundle != null) {
            text_color = bundle.getString("text_color");
            bg_color = bundle.getString("bg_color");
            league_logo = bundle.getString("league_logo");
            html_root = bundle.getString("html_root");
            league_name = bundle.getString("league_name");
            league_id = bundle.getInt("league_id");
            universe_name = bundle.getString("universe_name");
            league_abbr = bundle.getString("league_abbr");
            VectorMasterDrawable away_baseball = new VectorMasterDrawable(mContext, R.drawable.ic_baseball_2);
            PathModel black = away_baseball.getPathModelByName("black");
            black.setFillColor(Color.parseColor(text_color));
            PathModel white = away_baseball.getPathModelByName("white");
            white.setFillColor(Color.parseColor(bg_color));

            Glide.with(mContext)
                    .load(html_root + getString(R.string.news_html_league_logos, league_logo))
                    .apply(new RequestOptions()
                            .error(away_baseball)
                    )
                    .into(splashScreenBall);

            ((NavDrawer) getActivity())
                    .formatActionBar(league_name, null,
                            Color.parseColor(bg_color), getContrastColor(Color.parseColor(bg_color)));


            if (preferences.getBoolean("get_news", true) && isConnected(mContext)) {
                new populateNews().execute(preferences.getInt("news_articles", 15), preferences.getInt("news_timeout", 3));
            }

        } else {
            ((NavDrawer) getActivity())
                    .formatActionBar(getContext().getResources().getString(R.string.app_name), null,
                            R.color.colorPrimary, getContrastColor(R.color.colorPrimary));
        }
    }

    class NewsWrapper {
        List<NewsArticle> articles;
        boolean success;
    }

    private class populateNews extends AsyncTask<Integer, Void, NewsWrapper> {

        private AlertDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LayoutInflater li = LayoutInflater.from(mContext);
            final View downloadView = li.inflate(R.layout.get_leaders_progress, null);
            TextView message = downloadView.findViewById(R.id.headerLabel);
            message.setText(getString(R.string.checking_for_news, league_abbr));
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setView(downloadView);
            dialog = builder.create();
            dialog.show();
        }

        @Override
        protected NewsWrapper doInBackground(Integer... params) {
            List<NewsArticle> articles = db.getLeagueNews(universe_name, league_id, params[0]);
            int timeout = (params[1] > 0) ? params[1] * 1000 : 3000;
            NewsWrapper w = new NewsWrapper();
            if (articles.isEmpty() || articles == null) {
                w.success = false;
            } else {
                w.success = true;
                for (NewsArticle a : articles) {
                    Response response = null;
                    try {
                        URL url = new URL(html_root + mContext.getString(R.string.news_article, a.getLeagueID(), a.getMessageID()));
                        OkHttpClient.Builder b = new OkHttpClient.Builder();
                        b.connectTimeout(timeout, TimeUnit.MILLISECONDS);
                        OkHttpClient httpClient = b.build();
                        Call call = httpClient.newCall(new Request.Builder()
                                .url(url)
                                .addHeader("Accept-Encoding", "identity")
                                .get()
                                .build());
                        response = call.execute();
                        if (response.code() == 200) {
                            Whitelist whitelist = Whitelist.none();
                            whitelist.addTags(new String[]{"br"});
                            Document document = Jsoup.parse(new URL(html_root + mContext.getString(R.string.news_article, a.getLeagueID(), a.getMessageID())).openStream(),
                                    "ISO-8859-1", html_root + mContext.getString(R.string.news_article, a.getLeagueID(), a.getMessageID()));
                            Elements els = document.getElementsByClass("dl");
                            els.select("span").remove();
                            String prettyPrintedBodyFragment = Jsoup.clean(els.html(), "", Whitelist.none().addTags("br", "p"), new Document.OutputSettings().prettyPrint(true));
                            prettyPrintedBodyFragment = prettyPrintedBodyFragment.replaceAll("<br> \n<br> ","");
                            prettyPrintedBodyFragment = prettyPrintedBodyFragment.replaceAll("\n<br>\n<br>View Boxscore\n<br>View Game Log\n<br>", "");
                            prettyPrintedBodyFragment = prettyPrintedBodyFragment.replaceAll("\n<br>\n<br>", "\n\n");
                            a.setMessage(prettyPrintedBodyFragment);
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        w.success = false;
                    } catch (IOException e) {
                        e.printStackTrace();
                        w.success = false;
                    } finally {
                        w.articles = articles;
                        if (response != null) {
                            response.close();
                        }
                    }
                }
            }
            return w;
        }

        protected void onPostExecute(NewsWrapper w) {
            List<NewsArticle> articles = w.articles;
            if (w.success) {
                ListView listView = getActivity().findViewById(R.id.newsList);
                NewsAdapter newsAdapter = new NewsAdapter(mContext, articles, html_root);
                listView.setAdapter(newsAdapter);
            }
            dialog.dismiss();
        }
    }

}
