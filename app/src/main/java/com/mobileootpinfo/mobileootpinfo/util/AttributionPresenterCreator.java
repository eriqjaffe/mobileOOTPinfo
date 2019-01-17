package com.mobileootpinfo.mobileootpinfo.util;

import android.content.Context;

import com.franmontiel.attributionpresenter.AttributionPresenter;
import com.franmontiel.attributionpresenter.entities.Attribution;
import com.franmontiel.attributionpresenter.entities.Library;
import com.franmontiel.attributionpresenter.entities.License;
import com.franmontiel.attributionpresenter.listeners.OnAttributionClickListener;
import com.franmontiel.attributionpresenter.listeners.OnLicenseClickListener;

public class AttributionPresenterCreator {

        private static AttributionPresenter.Builder createBaseAttributions(Context context) {
            return new AttributionPresenter.Builder(context)
                    .addAttributions(
                            new Attribution.Builder("AttributionPresenter")
                                    .addCopyrightNotice("Copyright 2017 Francisco José Montiel Navarro")
                                    .addLicense(License.APACHE)
                                    .setWebsite("https://github.com/franmontiel/AttributionPresenter")
                                    .build(),
                            new Attribution.Builder("Calligraphy")
                                    .addCopyrightNotice("Copyright 2013 Christopher Jenkins")
                                    .addLicense(License.APACHE)
                                    .setWebsite("https://github.com/chrisjenx/Calligraphy")
                                    .build(),
                            new Attribution.Builder("FastCSV")
                                    .addCopyrightNotice("Copyright 2018 Oliver Siegmar")
                                    .addLicense(License.APACHE)
                                    .setWebsite("https://github.com/osiegmar/FastCSV")
                                    .build(),
                            new Attribution.Builder("Joda-Time")
                                    .addCopyrightNotice("Copyright ©2002-2017 Joda.org")
                                    .addLicense(License.APACHE)
                                    .setWebsite("http://www.joda.org/joda-time/index.html")
                                    .build(),
                            new Attribution.Builder("VectorMaster")
                                    .addCopyrightNotice("Copyright 2016-17 Harjot Singh Oberai")
                                    .addLicense(License.MIT)
                                    .setWebsite("https://github.com/harjot-oberai/VectorMaster")
                                    .build(),
                            new Attribution.Builder("Essentials")
                                    .addCopyrightNotice("Copyright (C) 2012-2016 Markus Junginger, greenrobot (http://greenrobot.org)")
                                    .addLicense(License.APACHE)
                                    .setWebsite("https://github.com/greenrobot/essentials")
                                    .build(),
                            new Attribution.Builder("TableFixHeaders-Wrapper")
                                    .addCopyrightNotice("Copyright 2016 miguelbcr")
                                    .addLicense(License.APACHE)
                                    .setWebsite("https://github.com/miguelbcr/TableFixHeaders-Wrapper")
                                    .build(),
                            new Attribution.Builder("Android Saripaar")
                                    .addCopyrightNotice("Copyright 2012 - 2015 Mobs & Geeks")
                                    .addLicense(License.APACHE)
                                    .setWebsite("https://github.com/ragunathjawahar/android-saripaar")
                                    .build(),
                            new Attribution.Builder("jsoup")
                                    .addCopyrightNotice("© 2009 - 2017 Jonathan Hedley")
                                    .addLicense(License.MIT)
                                    .setWebsite("https://jsoup.org/")
                                    .build(),
                            new Attribution.Builder("Flowtextview")
                                    .addCopyrightNotice("Copyright 2014 Dean Wild")
                                    .addLicense(License.APACHE)
                                    .setWebsite("http://deano2390.github.io/FlowTextView/")
                                    .build()
                    )
                    .addAttributions(
                            Library.GLIDE,
                            Library.OK_HTTP
                    );
        }

        public static AttributionPresenter create(Context context) {
            return createBaseAttributions(context).build();
        }

        public static AttributionPresenter create(Context context,
                                                  OnAttributionClickListener onAttributionClickListener,
                                                  OnLicenseClickListener onLicenseClickListener) {
            return createBaseAttributions(context)
                    .setOnAttributionClickListener(onAttributionClickListener)
                    .setOnLicenseClickListener(onLicenseClickListener)
                    .build();
        }

        public static AttributionPresenter create(Context context, int itemLayout, int licenseLayout) {
            return createBaseAttributions(context)
                    .setItemLayout(itemLayout)
                    .setLicenseLayout(licenseLayout)
                    .build();
        }
}
