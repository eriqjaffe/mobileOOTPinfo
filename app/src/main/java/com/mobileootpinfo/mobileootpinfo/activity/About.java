package com.mobileootpinfo.mobileootpinfo.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.franmontiel.attributionpresenter.AttributionPresenter;
import com.franmontiel.attributionpresenter.entities.Attribution;
import com.franmontiel.attributionpresenter.entities.LicenseInfo;
import com.franmontiel.attributionpresenter.listeners.OnAttributionClickListener;
import com.franmontiel.attributionpresenter.listeners.OnLicenseClickListener;
import com.mobileootpinfo.mobileootpinfo.R;
import com.mobileootpinfo.mobileootpinfo.adapter.FAQAdapter;
import com.mobileootpinfo.mobileootpinfo.model.FAQ;
import com.mobileootpinfo.mobileootpinfo.util.AttributionPresenterCreator;
import com.mobileootpinfo.mobileootpinfo.util.CalligraphyDefaultTabLayout;
import com.mobileootpinfo.mobileootpinfo.util.TypefaceSpan;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class About extends AppCompatActivity {

    private static Context ctx;
    private View aboutView;
    private View faqView;
    private View licenseView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ctx = getApplicationContext();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/OpenSans-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SpannableString s = new SpannableString(getString(R.string.app_name));
        s.setSpan(new TypefaceSpan(getApplicationContext(), "OpenSans-Regular.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Update the action bar title with the TypefaceSpan instance
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(s);

        final AttributionPresenter attributionHelper = AttributionPresenterCreator.create(this);

        CalligraphyDefaultTabLayout tabLayout = findViewById(R.id.aboutTabs);
        tabLayout.addTab(tabLayout.newTab().setText("About"));
        tabLayout.addTab(tabLayout.newTab().setText("FAQ"));
        tabLayout.addTab(tabLayout.newTab().setText("Licenses"));

        aboutView = findViewById(R.id.aboutView);
        faqView = findViewById(R.id.faqView);
        licenseView = findViewById(R.id.licenseView);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        aboutView.setVisibility(View.VISIBLE);
                        faqView.setVisibility(View.GONE);
                        licenseView.setVisibility(View.GONE);
                        break;
                    case 1:
                        aboutView.setVisibility(View.GONE);
                        faqView.setVisibility(View.VISIBLE);
                        licenseView.setVisibility(View.GONE);
                        break;
                    case 2:
                        aboutView.setVisibility(View.GONE);
                        faqView.setVisibility(View.GONE);
                        licenseView.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        List faq = new ArrayList<FAQ>();

        String[] questions  = getResources().getStringArray(R.array.faq_questions);
        String[] answers = getResources().getStringArray(R.array.faq_answers);

        for (int i=0; i<questions.length; i++) {
            FAQ f = new FAQ();
            f.setQuestion(questions[i]);
            f.setAnswer(answers[i]);
            faq.add(f);
        }

        ListView faqList = findViewById(R.id.faqView);
        FAQAdapter faqAdapter = new FAQAdapter(this, faq);
        faqList.setAdapter(faqAdapter);

        ListView licenseView = findViewById(R.id.licenseView);
        licenseView.setAdapter(AttributionPresenterCreator.create(
                this,
                new OnAttributionClickListener() {
                    @Override
                    public boolean onAttributionClick(Attribution attribution) {
                        return false;
                    }
                },
                new OnLicenseClickListener() {
                    @Override
                    public boolean onLicenseClick(LicenseInfo licenseInfo) {
                        return true;
                    }
                }).getAdapter());
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
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

    public void onBackPressed() {
        //comingFromSettings = true;
        finish();
    }
}
