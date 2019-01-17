package com.mobileootpinfo.mobileootpinfo.util;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Field;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

import static com.mobileootpinfo.mobileootpinfo.util.GetScreenSize.getScreenSize;

/**
 * TabLayout for use with the default Calligraphy font
 */
public class CalligraphyDefault5TabLayout extends TabLayout {

    Typeface calligraphyTypeface;

    private static final int WIDTH_INDEX = 0;
    private static final int DIVIDER_FACTOR = 5;
    private static final String SCROLLABLE_TAB_MIN_WIDTH = "mScrollableTabMinWidth";

    public CalligraphyDefault5TabLayout(Context context) {
        super(context);
        initCalligraphyTypeface();
        initTabMinWidth();
    }

    public CalligraphyDefault5TabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initCalligraphyTypeface();
        initTabMinWidth();
    }

    public CalligraphyDefault5TabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCalligraphyTypeface();
        initTabMinWidth();
    }

    @Override
    public void addTab(@NonNull Tab tab, int position, boolean setSelected) {
        super.addTab(tab, position, setSelected);
        if (calligraphyTypeface != null) {
            ViewGroup mainView = (ViewGroup) getChildAt(0);
            ViewGroup tabView = (ViewGroup) mainView.getChildAt(tab.getPosition());
            int tabChildCount = tabView.getChildCount();
            for (int i = 0; i < tabChildCount; i++) {
                View tabViewChild = tabView.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(calligraphyTypeface);
                }
            }
        }
    }

    private void initCalligraphyTypeface() {
        String fontPath = CalligraphyConfig.get().getFontPath();
        if(fontPath != null) {
            calligraphyTypeface = TypefaceUtils.load(getResources().getAssets(), fontPath);
        }
    }

    private void initTabMinWidth() {
        int[] wh = getScreenSize(getContext());
        int tabMinWidth = wh[WIDTH_INDEX] / DIVIDER_FACTOR;

        Field field;
        try {
            field = TabLayout.class.getDeclaredField(SCROLLABLE_TAB_MIN_WIDTH);
            field.setAccessible(true);
            field.set(this, tabMinWidth);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}