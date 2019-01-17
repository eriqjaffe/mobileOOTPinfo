package com.mobileootpinfo.mobileootpinfo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mobileootpinfo.mobileootpinfo.R;
import com.mobileootpinfo.mobileootpinfo.model.FAQ;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eriq on 3/25/2018.
 */

public class FAQAdapter extends ArrayAdapter<FAQ> {

    private Context mContext;
    private List<FAQ> faq = new ArrayList<FAQ>();


    public FAQAdapter(@NonNull Context context, List<FAQ> list) {
        super(context, 0, list);
        mContext = context;
        faq = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.faq_layout, parent, false);

        final FAQ faqItem = faq.get(position);

        TextView question = listItem.findViewById(R.id.question);
        question.setText(faqItem.getQuestion());

        TextView answer = listItem.findViewById(R.id.answer);
        answer.setText(faqItem.getAnswer());

        return listItem;
    }
}
