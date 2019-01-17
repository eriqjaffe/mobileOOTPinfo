package com.mobileootpinfo.mobileootpinfo.util;

/**
 * Created by eriqj on 3/28/2018.
 */

public class FormatSalary {

    public static String formatSalary(int val) {
        Float tmp = Float.valueOf(val);
        if (tmp == 0) {
            return "$0";
        } else if ((tmp/1000000) < 1) {
            return "$"+(String.valueOf(tmp/1000).replaceAll("\\.0*$", ""))+"K";
        } else {
            return "$"+(String.valueOf(tmp/1000000).replaceAll("\\.0*$", ""))+"M";
        }
    }

}
