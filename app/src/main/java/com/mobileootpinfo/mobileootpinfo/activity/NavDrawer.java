package com.mobileootpinfo.mobileootpinfo.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mobileootpinfo.mobileootpinfo.R;
import com.mobileootpinfo.mobileootpinfo.fragments.BattingLeaders;
import com.mobileootpinfo.mobileootpinfo.fragments.HomeFragment;
import com.mobileootpinfo.mobileootpinfo.fragments.LeagueInfoFragment;
import com.mobileootpinfo.mobileootpinfo.fragments.Leagues;
import com.mobileootpinfo.mobileootpinfo.fragments.ManageUniverses;
import com.mobileootpinfo.mobileootpinfo.fragments.PitchingLeaders;
import com.mobileootpinfo.mobileootpinfo.fragments.Players;
import com.mobileootpinfo.mobileootpinfo.fragments.Scores;
import com.mobileootpinfo.mobileootpinfo.fragments.Standings;
import com.mobileootpinfo.mobileootpinfo.fragments.Teams;
import com.mobileootpinfo.mobileootpinfo.model.LeagueInfo;
import com.mobileootpinfo.mobileootpinfo.model.Universe;
import com.mobileootpinfo.mobileootpinfo.util.DatabaseHandler;
import com.sdsmdg.harjot.vectormaster.VectorMasterDrawable;
import com.sdsmdg.harjot.vectormaster.models.PathModel;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.siegmar.fastcsv.reader.CsvContainer;
import de.siegmar.fastcsv.reader.CsvParser;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRow;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.mobileootpinfo.mobileootpinfo.util.ColorUtils.darkenColor;
import static com.mobileootpinfo.mobileootpinfo.util.ColorUtils.getContrastColor;
import static com.mobileootpinfo.mobileootpinfo.util.Connectivity.isConnected;
import static com.mobileootpinfo.mobileootpinfo.util.Connectivity.isConnectedWifi;

public class NavDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Context mContext;
    private DatabaseHandler db;
    private LeagueInfo leagueInfo;
    private List<LeagueInfo> leagueList;
    private List<Universe> universeList;
    private Universe chosenUniverse;
    private Toolbar toolbar;
    private String html_root;
    private String csv_root;
    private String universe_name;
    private String TAG = "NAV_DRAWER";
    private SimpleDateFormat sqliteSdf = new SimpleDateFormat("yyyy/MM/dd");
    private Snackbar mySnackbar;
    private DrawerLayout rootLayout;
    private View headerView;
    private int textColor;
    private String league_date;
    private TextView headerLargeText, headerSmallText, headerLeagueDate;
    private int league_id;
    private String fragmentTag;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mContext = getApplicationContext();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/OpenSans-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        preferences = PreferenceManager.getDefaultSharedPreferences(mContext);

        league_id = -1;

        setContentView(R.layout.activity_nav_drawer);
        rootLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        headerView = navigationView.getHeaderView(0);
        headerLargeText = headerView.findViewById(R.id.headerLargeText);
        headerSmallText = headerView.findViewById(R.id.headerSmallText);
        headerLeagueDate = headerView.findViewById(R.id.headerLeagueDate);

        db = new DatabaseHandler(this);

        universeList = db.getAllAppUniverses();

        if (universeList.size() > 0) {

            if (getIntent().getExtras() != null) {
                Bundle extras = getIntent().getExtras();
                if (extras.getLong("profile") == -1) {
                    chosenUniverse = db.getDefaultAppUniverse();
                    if (chosenUniverse == null) {
                        chosenUniverse = universeList.get(0);
                    }
                    league_id = chosenUniverse.getDefaultLeague();
                } else {
                    league_id = extras.getInt("league_id");
                    chosenUniverse = db.getAppUniverse(extras.getInt("universe_id"));
                }
            } else {
                chosenUniverse = db.getDefaultAppUniverse();
                if (chosenUniverse == null) {
                    chosenUniverse = universeList.get(0);
                }
                league_id = chosenUniverse.getDefaultLeague();
            }

            universe_name = chosenUniverse.getName();
            html_root = chosenUniverse.getHTML();
            csv_root = chosenUniverse.getCSV();

            if (!db.verifyUniverseTableExists(chosenUniverse) || sqliteSdf.format(db.getUniverseDate(chosenUniverse)).equals("1000/01/01")) {
                db.createNewUniverse(chosenUniverse, mContext);
                Menu menuNav = navigationView.getMenu();
                MenuItem tmp = menuNav.findItem(R.id.nav_leagues);
                tmp.setEnabled(false);
                tmp = menuNav.findItem(R.id.nav_teams);
                tmp.setEnabled(false);
                tmp = menuNav.findItem(R.id.nav_players);
                tmp.setEnabled(false);
                tmp = menuNav.findItem(R.id.nav_batting_leaders);
                tmp.setEnabled(false);
                tmp = menuNav.findItem(R.id.nav_pitching_leaders);
                tmp.setEnabled(false);
                tmp = menuNav.findItem(R.id.nav_standings);
                tmp.setEnabled(false);
                tmp = menuNav.findItem(R.id.nav_scores);
                tmp.setEnabled(false);
                tmp = menuNav.findItem(R.id.nav_info);
                tmp.setEnabled(false);
                tmp = menuNav.findItem(R.id.nav_refresh);
                tmp.setEnabled(true);
                headerLargeText.setText(chosenUniverse.getName());
                headerSmallText.setText("Universe not initialized");
                AlertDialog.Builder builder = new AlertDialog.Builder(NavDrawer.this);
                builder.setMessage("The universe \""+universe_name+"\" does not appear to be initialized.  Would you like to initialize it now?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        initOrUpdate();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.currentFrame, new HomeFragment(), fragmentTag);
                ft.commit();

                AlertDialog alert = builder.create();
                alert.show();

            } else {

                if (league_id == -1) {
                    leagueList = db.getAllDBLeagues(chosenUniverse.getName(), chosenUniverse.getHTML());
                    leagueInfo = leagueList.get(0);
                } else {
                    leagueInfo = db.getSpecificLeague(chosenUniverse.getName(), chosenUniverse.getHTML(), league_id);
                }

                headerView = navigationView.getHeaderView(0);

                TextView headerLargeText = headerView.findViewById(R.id.headerLargeText);
                TextView headerSmallText = headerView.findViewById(R.id.headerSmallText);
                TextView headerLeagueDate = headerView.findViewById(R.id.headerLeagueDate);
                headerLargeText.setText(leagueInfo.getAbbr());
                headerSmallText.setText(leagueInfo.getName());

                try {
                    DateFormat formatter = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
                    headerLeagueDate.setText(formatter.format(sqliteSdf.parse(leagueInfo.getCurrentDate())));
                } catch (ParseException e) {
                    headerLeagueDate.setText(leagueInfo.getCurrentDate());
                }
                headerLeagueDate.setVisibility(View.VISIBLE);

                LinearLayout header = headerView.findViewById(R.id.headerLayout);
                int startColor = Color.parseColor(leagueInfo.getBackgroundColor());
                int endColor = darkenColor(darkenColor(darkenColor(startColor)));
                GradientDrawable gd = new GradientDrawable(
                        GradientDrawable.Orientation.LEFT_RIGHT,
                        new int[]{endColor, startColor});
                gd.setCornerRadius(0f);
                header.setBackground(gd);

                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(leagueInfo.getBackgroundColor())));
                int txtColor = getContrastColor(Color.parseColor(leagueInfo.getBackgroundColor()));
                getSupportActionBar().setTitle(Html.fromHtml("<font color='"+leagueInfo.getTextColor()+"'>"+leagueInfo.getName()+"</font>"));
                Drawable bg = VectorDrawableCompat.create(getResources(), R.drawable.ic_menu_black_24dp, null);
                DrawableCompat.setTint(bg, txtColor);
                toolbar.setNavigationIcon(bg);
                Drawable drawable = toolbar.getOverflowIcon();
                if(drawable != null) {
                    drawable = DrawableCompat.wrap(drawable);
                    DrawableCompat.setTint(drawable.mutate(), txtColor);
                    toolbar.setOverflowIcon(drawable);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    int windowColor = darkenColor(startColor);
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(windowColor);
                }

                if (Color.parseColor(leagueInfo.getTextColor()) == Color.parseColor(leagueInfo.getBackgroundColor())) {
                    textColor = getContrastColor(Color.parseColor(leagueInfo.getTextColor()));
                } else {
                    textColor = Color.parseColor(leagueInfo.getTextColor());
                }

                ImageView leagueLogo = headerView.findViewById(R.id.imageView);
                VectorMasterDrawable away_baseball = new VectorMasterDrawable(mContext, R.drawable.ic_baseball_2);
                PathModel away_black = away_baseball.getPathModelByName("black");
                away_black.setFillColor(textColor);
                PathModel away_white = away_baseball.getPathModelByName("white");
                away_black.setFillColor(Color.parseColor(leagueInfo.getBackgroundColor()));

                Glide.with(mContext)
                        .load(html_root + getString(R.string.news_html_league_logos, leagueInfo.getLogo()))
                        .apply(new RequestOptions()
                                .error(away_baseball)
                        )
                        .into(leagueLogo);

                Menu menuNav = navigationView.getMenu();
                MenuItem tmp = menuNav.findItem(R.id.nav_info);
                tmp.setTitle(leagueInfo.getAbbr() + " History & Info");

                Bundle bundle = new Bundle();
                bundle.putString("html_root", leagueInfo.getHtml_root());
                bundle.putString("bg_color", leagueInfo.getBackgroundColor());
                bundle.putString("text_color", leagueInfo.getTextColor());
                bundle.putString("league_logo", leagueInfo.getLogo());
                bundle.putString("league_name", leagueInfo.getName());
                bundle.putInt("league_id", leagueInfo.getId());
                bundle.putString("universe_name", universe_name);
                bundle.putString("league_abbr", leagueInfo.getAbbr());
                Fragment fragment = new HomeFragment();
                fragment.setArguments(bundle);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.currentFrame, fragment, fragmentTag);
                ft.commit();
            }
        } else {
            mySnackbar = Snackbar.make(rootLayout, R.string.no_universes_defined, Snackbar.LENGTH_SHORT);
            mySnackbar.show();
            Menu menuNav = navigationView.getMenu();
            MenuItem tmp = menuNav.findItem(R.id.nav_leagues);
            tmp.setEnabled(false);
            tmp = menuNav.findItem(R.id.nav_teams);
            tmp.setEnabled(false);
            tmp = menuNav.findItem(R.id.nav_players);
            tmp.setEnabled(false);
            tmp = menuNav.findItem(R.id.nav_batting_leaders);
            tmp.setEnabled(false);
            tmp = menuNav.findItem(R.id.nav_pitching_leaders);
            tmp.setEnabled(false);
            tmp = menuNav.findItem(R.id.nav_standings);
            tmp.setEnabled(false);
            tmp = menuNav.findItem(R.id.nav_scores);
            tmp.setEnabled(false);
            tmp = menuNav.findItem(R.id.nav_info);
            tmp.setEnabled(false);
            tmp = menuNav.findItem(R.id.nav_refresh);
            tmp.setEnabled(false);
            headerLargeText.setText(R.string.app_name);
            headerSmallText.setText(R.string.add_a_universe_to_get_started);
            headerLeagueDate.setVisibility(View.GONE);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.currentFrame, new HomeFragment(), fragmentTag);
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            return true;
        }

        if (id == R.id.action_about) {
            startActivity(new Intent(getApplicationContext(), About.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        Fragment fragment = null;
        int id = item.getItemId();

        Bundle bundle = new Bundle();

        if (id == R.id.nav_switch) {
            fragment = new ManageUniverses();
            fragmentTag = "nav_switch";
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.currentFrame, fragment, fragmentTag);
            ft.commit();
        } else {
            if (universeList.size() > 0) {
                bundle.putString("html_root", html_root);
                bundle.putString("universe_name", universe_name);
                bundle.putString("league_abbr", leagueInfo.getAbbr());
                bundle.putInt("league_id", leagueInfo.getId());
                bundle.putString("game_date", leagueInfo.getCurrentDate());
                bundle.putString("bg_color", leagueInfo.getBackgroundColor());
                bundle.putString("text_color", leagueInfo.getTextColor());
                bundle.putInt("wildcards", leagueInfo.getWildcards());
                bundle.putInt("universe_id", leagueInfo.getUniverseID());
            }
            if (id == R.id.nav_leagues) {
                fragmentTag = "nav_leagues";
                fragment = new Leagues();
            } else if (id == R.id.nav_teams) {
                fragmentTag = "nav_teams";
                fragment = new Teams();
            } else if (id == R.id.nav_players) {
                fragmentTag = "nav_players";
                fragment = new Players();
            } else if (id == R.id.nav_batting_leaders) {
                fragmentTag = "nav_batting_leaders";
                fragment = new BattingLeaders();
            } else if (id == R.id.nav_pitching_leaders) {
                fragmentTag = "nav_pitching_leaders";
                fragment = new PitchingLeaders();
            } else if (id == R.id.nav_standings) {
                fragmentTag = "nav_standings";
                fragment = new Standings();
            } else if (id == R.id.nav_scores) {
                fragmentTag = "nav_scores";
                fragment = new Scores();
            } else if (id == R.id.nav_info) {
                fragmentTag = "nav_info";
                fragment = new LeagueInfoFragment();
            } else if (id == R.id.nav_refresh) {
                Map<String,?> keys = preferences.getAll();
                initOrUpdate();
            }

            if (fragment != null) {
                if (universeList.size() > 0) {
                    fragment.setArguments(bundle);
                }
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.currentFrame, fragment, fragmentTag);
                ft.commit();
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void formatActionBar(String title, String subtitle, int bgcolor, int textcolor) {
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setSubtitle(subtitle);
        toolbar.setBackgroundColor(bgcolor);
        int txtColor = getContrastColor(bgcolor, textcolor);
        toolbar.setTitleTextColor(textcolor);
        toolbar.setSubtitleTextColor(textcolor);
        Drawable bg = VectorDrawableCompat.create(getResources(), R.drawable.ic_menu_black_24dp, null);
        DrawableCompat.setTint(bg, textcolor);
        toolbar.setNavigationIcon(bg);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int windowColor = darkenColor(bgcolor);
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(windowColor);
        }
        Drawable drawable = toolbar.getOverflowIcon();
        if(drawable != null) {
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable.mutate(), textcolor);
            toolbar.setOverflowIcon(drawable);
        }
    }

    class GetUniverseDate extends AsyncTask<Universe, String, UniverseWrapper> {

        private Context mContext;

        public GetUniverseDate (Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected UniverseWrapper doInBackground(Universe... args) {
            Universe universe = args[0];
            int count;
            InputStream input = null;
            OutputStream output = null;
            File outFile = null;
            URLConnection conn = null;
            UniverseWrapper w = new UniverseWrapper();
            try {
                String dataDir = getDataDir(mContext);
                File tmpFile = new File(dataDir + "/tmp_leagues.csv");
                if (tmpFile.exists()) {
                    tmpFile.delete();
                }
                URL url = new URL(universe.getCSV() + "leagues.csv");
                conn = url.openConnection();
                conn.setConnectTimeout(5000);
                conn.connect();
                input = new BufferedInputStream(url.openStream(), 8192);
                output = new FileOutputStream(dataDir + "/tmp_leagues.csv");
                byte data[] = new byte[1024];
                while ((count = input.read(data)) != -1) {
                    output.write(data, 0, count);
                }
                outFile = new File(dataDir + "/tmp_leagues.csv");
                w.file = outFile;
                w.universe = universe;
                w.context = mContext;
            } catch (IOException e) {
                e.printStackTrace();
                w = null;
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
                w = null;
            } finally {
                try {
                    if (output != null) {
                        output.flush();
                        output.close();
                    }
                    if (input != null) {
                        input.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    w = null;
                }
            }
            return w;
        }

        @Override
        protected void onPostExecute(UniverseWrapper w) {
            super.onPostExecute(w);
            if (w != null) {
                final UniverseWrapper uw = w;
                File file = w.file;
                Universe universe = w.universe;
                Context context = w.context;
                CsvReader csvReader = new CsvReader();
                csvReader.setContainsHeader(true);
                try {
                    FileReader reader = new FileReader(file);
                    CsvContainer csv = csvReader.read(reader);
                    CsvRow row = csv.getRow(0);
                    Date dbDate = db.getUniverseDate(universe);
                    Date csvDate = tryParse(row.getField("current_date"));
                    reader.close();
                    DateFormat formatter = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
                    w.csvDate = formatter.format(csvDate);
                    w.dbDate = formatter.format(dbDate);
                    if (csvDate.after(dbDate)) {
                        new UpdateUniverse().execute(w);
                    } else {
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        new UpdateUniverse().execute(uw);
                                        break;
                                    case DialogInterface.BUTTON_NEGATIVE:
                                        break;
                                }
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(NavDrawer.this);
                        builder.setMessage(R.string.force_refresh_question).setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).show();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    mySnackbar = Snackbar.make(rootLayout, getString(R.string.something_went_wrong)+e.getLocalizedMessage(), Snackbar.LENGTH_SHORT);
                    mySnackbar.show();
                }
            } else {
                mySnackbar = Snackbar.make(rootLayout, R.string.something_went_wrong_but, Snackbar.LENGTH_SHORT);
                mySnackbar.show();
            }
        }
    }

    class UpdateUniverse extends AsyncTask<UniverseWrapper, String, UniverseWrapper> {

        private AlertDialog dialog;
        private AsyncTask<UniverseWrapper, String, UniverseWrapper> UpdateUniverse = null;
        private volatile boolean running = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            UpdateUniverse = this;
            LayoutInflater li = LayoutInflater.from(NavDrawer.this);
            final View downloadView = li.inflate(R.layout.download_progress, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(NavDrawer.this);
            builder.setView(downloadView);
            builder.setPositiveButton("Cancel",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            UpdateUniverse.cancel(true);
                            dialog.cancel();

                        }
                    });;
            dialog = builder.create();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected void onCancelled() {
            running = false;
        }

        @Override
        protected UniverseWrapper doInBackground(UniverseWrapper... args) {
            while (running) {
                UniverseWrapper w = args[0];
                Universe universe = w.universe;
                Map<Pair, String> importData = new HashMap<>();
                JSONObject json = getJsonFromResource(w.context);
                try {
                    String dataDir = getDataDir(w.context);
                    int totalFiles = iterCount(json);
                    Iterator<String> i = json.keys();
                    int currentFile = 0;
                    while (i.hasNext()) {
                        int previousProgress = 0;
                        if (importData != null) { importData.clear(); }
                        currentFile++;
                        String action;
                        String fileIndicator;
                        String progress;
                        String percentage;
                        String key = i.next();
                        URL url = new URL(universe.getCSV() + key + ".csv");
                        OkHttpClient httpClient = new OkHttpClient();
                        Call call = httpClient.newCall(new Request.Builder()
                                .url(url)
                                .addHeader("Accept-Encoding", "identity")
                                .get()
                                .build());
                        Response response = null;
                        try {
                            response = call.execute();
                            if (response.code() == 200) {
                                InputStream inputStream = null;
                                try {
                                    inputStream = response.body().byteStream();
                                    byte[] buff = new byte[1024 * 8];
                                    long downloaded = 0;
                                    long target = response.body().contentLength();
                                    FileOutputStream output = new FileOutputStream(dataDir + "/"+universe.getName()+"_"+key+".csv");
                                    while (true) {
                                        int readed = inputStream.read(buff);
                                        if(readed == -1){
                                            break;
                                        }
                                        downloaded += readed;
                                        output.write(buff, 0, readed);
                                        action = "Downloading "+key;
                                        fileIndicator = getString(R.string.file_x_of_y, currentFile, totalFiles);
                                        progress = ""+(downloaded/1024)+" kB";
                                        percentage = String.valueOf((int) (downloaded * 100 / target));
                                        if ((int) (downloaded * 100 / target) > previousProgress) {
                                            previousProgress = (int) (downloaded * 100 / target);
                                            publishProgress(action, fileIndicator, progress, percentage, w.csvDate);
                                        }
                                        if (isCancelled()) {
                                            return null;
                                        }
                                    }
                                } catch (IOException ignore) {
                                    ignore.printStackTrace();
                                } finally {
                                    if (inputStream != null) {
                                        inputStream.close();
                                    }
                                    if (response != null) {
                                        response.close();
                                    }

                                }
                            }
                        } catch (IOException e) {
                            mySnackbar = Snackbar.make(rootLayout, e.getLocalizedMessage(), Snackbar.LENGTH_LONG);
                            mySnackbar.show();
                            UpdateUniverse.cancel(true);
                            e.printStackTrace();
                        } finally {
                            if (response != null) {
                                response.close();
                            }
                        }

                        DatabaseHandler dbh = new DatabaseHandler(mContext);

                        dbh.truncateTable(universe, key);

                        File file = new File(dataDir + "/"+universe.getName()+"_"+key+".csv");
                        JSONObject fields = json.getJSONObject(key);
                        SQLiteDatabase db = null;

                        CsvReader csvReader = new CsvReader();
                        csvReader.setContainsHeader(true);
                        CsvRow row;
                        long currentRow = 0;
                        int totalRows = rowCount(file);
                        try {
                            db = dbh.getWritableDatabase();
                            db.beginTransaction();
                            FileReader reader = new FileReader(file);
                            try {
                                CsvParser csvParser = csvReader.parse(reader);
                                previousProgress = 0;
                                while ((row = csvParser.nextRow()) != null) {
                                    currentRow++;
                                    Iterator<String> j = fields.keys();
                                    ContentValues values = new ContentValues();
                                    while (j.hasNext()) {
                                        String field = j.next();
                                        switch (fields.optString(field)) {
                                            case "text":
                                                values.put(field, row.getField(field));
                                                break;
                                            case "datetime":
                                                values.put(field, sqliteSdf.format(tryParse(row.getField(field))));
                                                break;
                                            case "integer":
                                                if (row.getField(field) == "") {
                                                    values.put(field, 0);
                                                } else if (row.getField(field).contains(".")) {
                                                    values.put(field, Float.parseFloat(row.getField(field)));
                                                } else if (row.getField(field).contains("''")) {
                                                    values.put(field, 0);
                                                } else {
                                                    values.put(field, Integer.parseInt(row.getField(field)));
                                                }
                                                break;
                                            case "float":
                                                values.put(field, Float.parseFloat(row.getField(field)));
                                                break;
                                            default:
                                                values.put(field, row.getField(field));
                                                break;
                                        }
                                    }
                                    action = "Updating "+key;
                                    fileIndicator = getString(R.string.file_x_of_y, currentFile, totalFiles);
                                    progress = getString(R.string.row_x_of_y, currentRow, totalRows);
                                    percentage = String.valueOf((int) (currentRow * 100 / totalRows));
                                    if ((int) (currentRow * 100 / totalRows) > previousProgress) {
                                        previousProgress = (int) (currentRow * 100 / totalRows);
                                        publishProgress(action, fileIndicator, progress, percentage, w.csvDate);
                                    }
                                    db.insert(universe.getName()+"_"+key, null, values);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            db.setTransactionSuccessful();
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            db.endTransaction();
                            db.close();
                            file.delete();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return w;
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            TextView progressLabel = dialog.findViewById(R.id.progressLabel);
            TextView statusLabel = dialog.findViewById(R.id.statusLabel);
            TextView countLabel = dialog.findViewById(R.id.fileCount);
            TextView header = dialog.findViewById(R.id.headerLabel);
            header.setText("Universe date is "+values[4]);
            ProgressBar progressBar = dialog.findViewById(R.id.progressBar);
            progressLabel.setText(values[0]);
            statusLabel.setText(values[2]);
            countLabel.setText(values[1]);
            progressBar.setProgress(Integer.parseInt(values[3]));
        }

        @Override
        protected void onPostExecute(UniverseWrapper w) {
            super.onPostExecute(w);
            dialog.hide();
            if (w != null) {
                Intent intent = new Intent(mContext, NavDrawer.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("html_root", w.universe.getHTML());
                mBundle.putString("universe_name", w.universe.getName());
                mBundle.putInt("universe_id", w.universe.getID());
                mBundle.putInt("league_id", -1);
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        }

    }

    public class UniverseWrapper
    {
        public File file;
        public Universe universe;
        public Context context;
        public String dbDate;
        public String csvDate;
        public String errorStatus;
    }

    @Nullable
    private Date tryParse(String dateString) {
        List<String> formatStrings = Arrays.asList("y-M-d", "M/d/y");
        for (String formatString : formatStrings) {
            try {
                Date tmp = new SimpleDateFormat(formatString).parse(dateString);
                return tmp;
            }
            catch (ParseException e) {
                try {
                    Date tmp = new SimpleDateFormat("M/d/y").parse("1/1/1980");
                    return tmp;
                } catch (ParseException f) {}
            }
        }
        return null;
    }

    private Integer iterCount(JSONObject json) {
        int cnt = 0;
        Iterator<String> i = json.keys();
        while (i.hasNext()) {
            String tmp = i.next();
            cnt++;
        }
        return cnt;
    }

    private Integer rowCount(File file) {
        int lineCount = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                lineCount++;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return lineCount;
    }

    private JSONObject getJsonFromResource(Context context) {
        JSONObject json = null;
        InputStream is = context.getResources().openRawResource(R.raw._csv);
        //InputStream is = context.getResources().openRawResource(R.raw._league_only);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
            json = new JSONObject(writer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return json;
    }

    public static String getDataDir(Context context) throws Exception {
        return context.getPackageManager()
                .getPackageInfo(context.getPackageName(), 0)
                .applicationInfo.dataDir;
    }

    public Reader getReader(String relativePath) {
        Reader reader = null;
        try {
            reader = new InputStreamReader(this.getClass().getResourceAsStream(relativePath), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reader;
    }

    private void initOrUpdate() {
        if (!isConnected(mContext)) {
            mySnackbar = Snackbar.make(rootLayout, R.string.no_internet, Snackbar.LENGTH_LONG);
            mySnackbar.show();
        } else {
            switch (preferences.getString("download_pref", "2")) {
                case "0":
                    new GetUniverseDate(mContext).execute(chosenUniverse);
                    break;
                case "1":
                    if (!isConnectedWifi(mContext)) {
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        new GetUniverseDate(mContext).execute(chosenUniverse);
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        break;
                                }
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(NavDrawer.this);
                        builder.setMessage(R.string.no_wifi_warning).setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).show();

                    } else {
                        new GetUniverseDate(mContext).execute(chosenUniverse);
                    }
                    break;
                case "2":
                    if (!isConnectedWifi(mContext)) {
                        mySnackbar = Snackbar.make(rootLayout, R.string.no_wifi_block, Snackbar.LENGTH_LONG);
                        mySnackbar.show();
                    } else {
                        new GetUniverseDate(mContext).execute(chosenUniverse);
                    }
                    break;
                default:
                    mySnackbar = Snackbar.make(rootLayout, R.string.something_not_set, Snackbar.LENGTH_LONG);
                    mySnackbar.show();
                    break;
            }
        }
    }
}
