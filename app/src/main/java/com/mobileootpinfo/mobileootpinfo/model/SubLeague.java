package com.mobileootpinfo.mobileootpinfo.model;

/**
 * Created by eriqj on 2/23/2018.
 */

public class SubLeague {
    int subLeagueId;
    String name;
    String abbr;

    public SubLeague() {}

    public SubLeague(int subLeagueId, String name, String abbr) {
        this.subLeagueId = subLeagueId;
        this.name = name;
        this.abbr = abbr;
    }

    public int getSubLeagueId() {
        return subLeagueId;
    }

    public void setSubLeagueId(int subLeagueId) {
        this.subLeagueId = subLeagueId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }
}


