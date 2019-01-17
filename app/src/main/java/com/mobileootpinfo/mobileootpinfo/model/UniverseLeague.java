package com.mobileootpinfo.mobileootpinfo.model;

/**
 * Created by eriqj on 3/21/2018.
 */

public class UniverseLeague {

    int leagueID;
    String leagueName;

    public UniverseLeague() {}

    public UniverseLeague(int leagueID, String leagueName) {
        this.leagueID = leagueID;
        this.leagueName = leagueName;
    }

    public int getLeagueID() {
        return leagueID;
    }

    public void setLeagueID(int leagueID) {
        this.leagueID = leagueID;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }
}
