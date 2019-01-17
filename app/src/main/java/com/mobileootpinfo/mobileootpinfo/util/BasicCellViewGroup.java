package com.mobileootpinfo.mobileootpinfo.util;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mobileootpinfo.mobileootpinfo.R;

import java.util.List;

import miguelbcr.ui.tableFixHeadesWrapper.TableFixHeaderAdapter;


public class BasicCellViewGroup extends FrameLayout
        implements
        TableFixHeaderAdapter.FirstHeaderBinder<String>,
        TableFixHeaderAdapter.HeaderBinder<String>,
        TableFixHeaderAdapter.FirstBodyBinder<List<String>>,
        TableFixHeaderAdapter.BodyBinder<List<String>>,
        TableFixHeaderAdapter.SectionBinder<List<String>> {

    public TextView textView;

    public BasicCellViewGroup(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(android.R.layout.test_list_item, this, true);
        textView = (TextView) findViewById(android.R.id.text1);
        textView.setGravity(Gravity.RIGHT);
    }

    @Override
    public void bindFirstHeader(String headerName) {
        textView.setGravity(Gravity.LEFT);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setText(headerName);
    }

    @Override
    public void bindHeader(String headerName, int column) {
        textView.setTypeface(null, Typeface.BOLD);
        textView.setText(headerName);
    }

    @Override
    public void bindFirstBody(List<String> items, int row) {
        //textView.setText("Row " + (row + 1));
        if (row % 2 == 0) {
            textView.setBackgroundColor(getResources().getColor(
                    R.color.white_smoke));
        } else {
            textView.setBackgroundColor(getResources().getColor(
                    R.color.white));
        }
        textView.setGravity(Gravity.LEFT);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setText(items.get(0));
    }

    @Override
    public void bindBody(List<String> items, int row, int column) {
        if (row % 2 == 0) {
            textView.setBackgroundColor(getResources().getColor(
                    R.color.white_smoke));
        } else {
            textView.setBackgroundColor(getResources().getColor(
                    R.color.white));
        }
        textView.setText(items.get(column + 1));
    }

    @Override
    public void bindSection(List<String> item, int row, int column) {
        textView.setText(column == 0 ? "Section:" + (row + 1) : "");
    }
}
