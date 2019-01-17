package com.mobileootpinfo.mobileootpinfo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobileootpinfo.mobileootpinfo.R;
import com.mobileootpinfo.mobileootpinfo.model.Contract;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.co.chrisjenx.calligraphy.CalligraphyTypefaceSpan;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

/**
 * Created by eriqj on 3/26/2018.
 */

public class ContractAdapter extends ArrayAdapter<Contract> {

    private Context mContext;
    private List<Contract> contractList = new ArrayList<Contract>();
    private String awardPrefix = "";
    private String awardPostscript = "";
    private String awardText;
    private String html_root;
    SimpleDateFormat sqliteSdf = new SimpleDateFormat("yyyy/M/d");
    DateFormat formatter = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());

    public ContractAdapter(@NonNull Context context, List<Contract> list, String html) {
        super(context, 0, list);
        mContext = context;
        contractList = list;
        html_root = html;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.contract_layout, parent, false);

        final Contract contract = contractList.get(position);

        TextView playerName = listItem.findViewById(R.id.playerName);
        playerName.setText(contract.getFirstName()+" "+contract.getLastName());

        TextView playerPosition = listItem.findViewById(R.id.playerPosition);
        String displayPosition = contract.getPosition();
        if (contract.getPosition().equals("P")) {
            displayPosition = contract.getThrowingHand()+"H"+contract.getPosition();
        }
        playerPosition.setText(displayPosition);

        String yearDisplay;
        if (contract.getContractLength() > 1) {
            yearDisplay = " years / ";
        } else {
            yearDisplay = " year / ";
        }

        String summaryLine = contract.getContractLength() + yearDisplay + formatSalary(contract.getTotalValue());

        String lastContractYear = String.valueOf(contract.getSignedIn()+(contract.getContractLength()-1));
        String nextLastContractYear = String.valueOf(contract.getSignedIn()+(contract.getContractLength()-2));
        String contractYears;

        if (contract.getContractLength() > 1) {
            contractYears = "("+String.valueOf(contract.getSignedIn()) + " - " + String.valueOf(contract.getSignedIn()+(contract.getContractLength()-1))+")";
        } else {
            contractYears = "("+String.valueOf(contract.getSignedIn()) + ")";
        }

        summaryLine += " " + contractYears;

        TextView contractOverview = listItem.findViewById(R.id.contractOverview);
        contractOverview.setText(summaryLine);

        int contractStartYear = contract.getSignedIn();

        List<Integer> yearlyList = new ArrayList<Integer>();
        
        yearlyList.add(contract.getSalary0());
        yearlyList.add(contract.getSalary1());
        yearlyList.add(contract.getSalary2());
        yearlyList.add(contract.getSalary3());
        yearlyList.add(contract.getSalary4());
        yearlyList.add(contract.getSalary5());
        yearlyList.add(contract.getSalary6());
        yearlyList.add(contract.getSalary7());
        yearlyList.add(contract.getSalary8());
        yearlyList.add(contract.getSalary9());
                
        List<String> contractBreakdown = new ArrayList<String>();

        for (int i = 0; i < contract.getContractLength(); i++) {
            int tmp = contractStartYear + i;
            contractBreakdown.add(String.valueOf(tmp) + ": " + formatSalary(yearlyList.get(i)));
        }

        TextView yearlyBreakdown = listItem.findViewById(R.id.yearlyBreakdown);
        yearlyBreakdown.setText(boldTags(android.text.TextUtils.join(", ", contractBreakdown)));

        LinearLayout teamNextOptionParent = listItem.findViewById(R.id.teamNextOptionParent);
        if (contract.getNextLastYearTeamOption() > 0) {
            TextView teamNextOption = listItem.findViewById(R.id.teamNextOption);
            teamNextOption.setText(nextLastContractYear + " is a team option with a "+formatSalary(contract.getNextLastYearOptionBuyout())+ " buyout.");
            teamNextOptionParent.setVisibility(View.VISIBLE);
        } else {
            teamNextOptionParent.setVisibility(View.GONE);
        }

        LinearLayout teamOptionParent = listItem.findViewById(R.id.teamOptionParent);
        if (contract.getLastYearTeamOption() > 0) {
            TextView teamOption = listItem.findViewById(R.id.teamOption);
            teamOption.setText(lastContractYear + " is a team option with a "+formatSalary(contract.getLastYearOptionBuyout())+ " buyout.");
            teamOptionParent.setVisibility(View.VISIBLE);
        } else {
            teamOptionParent.setVisibility(View.GONE);
        }

        LinearLayout playerNextOptionParent = listItem.findViewById(R.id.playerNextOptionParent);
        if (contract.getNextLastYearPlayerOption() > 0) {
            TextView playerNextOption = listItem.findViewById(R.id.playerNextOption);
            playerNextOption.setText("Has a player option for " +nextLastContractYear );
            playerNextOptionParent.setVisibility(View.VISIBLE);
        } else {
            playerNextOptionParent.setVisibility(View.GONE);
        }

        LinearLayout playerOptionParent = listItem.findViewById(R.id.playerOptionParent);
        if (contract.getLastYearPlayerOption() > 0) {
            TextView playerOption = listItem.findViewById(R.id.playerOption);
            playerOption.setText("Has a player option for " +lastContractYear );
            playerOptionParent.setVisibility(View.VISIBLE);
        } else {
            playerOptionParent.setVisibility(View.GONE);
        }

        LinearLayout vestingNextOptionParent = listItem.findViewById(R.id.vestingNextOptionParent);
        if (contract.getNextLastYearVestingOption() > 0) {
            TextView vestingNextOption = listItem.findViewById(R.id.vestingNextOption);
            vestingNextOption.setText("Has a vesting option for " +nextLastContractYear );
            vestingNextOptionParent.setVisibility(View.VISIBLE);
        } else {
            vestingNextOptionParent.setVisibility(View.GONE);
        }

        LinearLayout vestingOptionParent = listItem.findViewById(R.id.vestingOptionParent);
        if (contract.getLastYearVestingOption() > 0) {
            TextView vestingOption = listItem.findViewById(R.id.vestingOption);
            vestingOption.setText("Has a vesting option for " +lastContractYear );
            vestingOptionParent.setVisibility(View.VISIBLE);
        } else {
            vestingOptionParent.setVisibility(View.GONE);
        }

        LinearLayout paIncentiveParent = listItem.findViewById(R.id.paIncentiveParent);
        if (contract.getMinimumPABonus() > 0) {
            TextView paIncentive = listItem.findViewById(R.id.paIncentive);
            paIncentive.setText(formatSalary(contract.getMinimumPABonus()) + " bonus for making " +contract.getMinimumPA() + " plate appearances.");
            paIncentiveParent.setVisibility(View.VISIBLE);
        } else {
            paIncentiveParent.setVisibility(View.GONE);
        }

        LinearLayout ipIncentiveParent = listItem.findViewById(R.id.ipIncentiveParent);
        if (contract.getMinimumIPBonus() > 0) {
            TextView ipIncentive = listItem.findViewById(R.id.ipIncentive);
            ipIncentive.setText(formatSalary(contract.getMinimumIPBonus()) + " bonus for pitching " +contract.getMinimumIP() + " innings.");
            ipIncentiveParent.setVisibility(View.VISIBLE);
        } else {
            ipIncentiveParent.setVisibility(View.GONE);
        }
        
        LinearLayout mvpBonusParent = listItem.findViewById(R.id.mvpBonusParent);
        if (contract.getMvpBonus() > 0) {
            TextView mvpBonus = listItem.findViewById(R.id.mvpBonus);
            mvpBonus.setText(formatSalary(contract.getMvpBonus()) + " " +contract.getMvpAwardName() + " bonus.");
            mvpBonusParent.setVisibility(View.VISIBLE);
        } else {
            mvpBonusParent.setVisibility(View.GONE);
        }

        LinearLayout poyBonusParent = listItem.findViewById(R.id.poyBonusParent);
        if (contract.getPoyBonus() > 0) {
            TextView poyBonus = listItem.findViewById(R.id.poyBonus);
            poyBonus.setText(formatSalary(contract.getPoyBonus()) + " " +contract.getPoyAwardName() + " bonus.");
            poyBonusParent.setVisibility(View.VISIBLE);
        } else {
            poyBonusParent.setVisibility(View.GONE);
        }

        LinearLayout allStarBonusParent = listItem.findViewById(R.id.allStarBonusParent);
        if (contract.getAsBonus() > 0) {
            TextView allStarBonus = listItem.findViewById(R.id.allStarBonus);
            allStarBonus.setText(formatSalary(contract.getAsBonus()) + " bonus for being named an All-Star");
            allStarBonusParent.setVisibility(View.VISIBLE);
        } else {
            allStarBonusParent.setVisibility(View.GONE);
        }
        
        return listItem;
    }

    private String formatSalary(int val) {
        Float tmp = Float.valueOf(val);
        if (tmp == 0) {
            return "$0";
        } else if ((tmp/1000000) < 1) {
            return "$"+(String.valueOf(tmp/1000).replaceAll("\\.0*$", ""))+"K";
        } else {
            return "$"+(String.valueOf(tmp/1000000).replaceAll("\\.0*$", ""))+"M";
        }
    }

    private SpannableStringBuilder boldTags(String text){
        final Pattern boldMe = Pattern.compile("[0-9][0-9][0-9][0-9]:");

        final SpannableStringBuilder spannable = new SpannableStringBuilder(text);

        final Matcher matcher = boldMe.matcher(text);

        while (matcher.find()) {
            CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(mContext.getResources().getAssets(), "fonts/OpenSans-Bold.ttf"));
            spannable.setSpan(
                    typefaceSpan, matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }
        return spannable;
    }
}
