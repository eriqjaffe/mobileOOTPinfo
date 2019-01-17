package com.mobileootpinfo.mobileootpinfo.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.MenuItem;

import com.mobileootpinfo.mobileootpinfo.R;
import com.mobileootpinfo.mobileootpinfo.util.NumberPickerPreference;
import com.mobileootpinfo.mobileootpinfo.util.TimeOutPreference;
import com.mobileootpinfo.mobileootpinfo.util.TypefaceSpan;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SettingsActivity extends AppCompatPreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static Context ctx;
    private static NumberPickerPreference articlesToFetch;
    private static TimeOutPreference newsTimeout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = getApplicationContext();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/OpenSans-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SpannableString s = new SpannableString(getString(R.string.action_settings));
        s.setSpan(new TypefaceSpan(getApplicationContext(), "OpenSans-Regular.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Update the action bar title with the TypefaceSpan instance
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(s);

        // load settings fragment
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainPreferenceFragment()).commit();

        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        onSharedPreferenceChanged(sharedPrefs, getString(R.string.fetch_news_article_qty));
    }

    public static class MainPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);

            articlesToFetch = (NumberPickerPreference) findPreference("news_articles");
            newsTimeout = (TimeOutPreference) findPreference("news_timeout");

            final SharedPreferences sh = getPreferenceManager().getSharedPreferences() ;

            Preference news_articles = findPreference("news_articles");
            news_articles.setSummary(String.valueOf(sh.getInt("news_articles", 15)));

            Preference news_timeout = findPreference("news_timeout");
            news_timeout.setSummary(String.valueOf(sh.getInt("news_timeout", 3)));
        }
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        System.out.println(prefs.toString());
        if (key.equals("news_articles")) {
            articlesToFetch.setSummary(String.valueOf(prefs.getInt("news_articles", 15)));
        }
        if (key.equals("news_timeout")) {
            newsTimeout.setSummary(String.valueOf(prefs.getInt("news_timeout", 3)));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            //comingFromSettings = true;
            finish(); // close this activity and return to preview activity (if there is any)`enter code here`
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void onBackPressed() {
        //comingFromSettings = true;
        finish();
    }
}
