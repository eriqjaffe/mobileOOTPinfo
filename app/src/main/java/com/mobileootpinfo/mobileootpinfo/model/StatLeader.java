package com.mobileootpinfo.mobileootpinfo.model;

/**
 * Created by Eriq on 3/2/2018.
 */

public class StatLeader {
    String firstName;
    String lastName;
    String teamName;
    String teamNickname;
    String teamAbbr;
    String leagueAbbr;
    String subLeagueAbbr;
    String statistic;
    String value;
    String displayName;
    int playerID;

    public StatLeader() {}

    public StatLeader(String firstName, String lastName, String teamName, String teamNickname, String teamAbbr,
                      String leagueAbbr, String subLeagueAbbr, String statistic, String value, String displayName,
                      int playerID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.teamName = teamName;
        this.teamNickname = teamNickname;
        this.teamAbbr = teamAbbr;
        this.leagueAbbr = leagueAbbr;
        this.subLeagueAbbr = subLeagueAbbr;
        this.statistic = statistic;
        this.value = value;
        this.displayName = displayName;
        this.playerID = playerID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamNickname() {
        return teamNickname;
    }

    public void setTeamNickname(String teamNickname) {
        this.teamNickname = teamNickname;
    }

    public String getTeamAbbr() {
        return teamAbbr;
    }

    public void setTeamAbbr(String teamAbbr) {
        this.teamAbbr = teamAbbr;
    }

    public String getLeagueAbbr() {
        return leagueAbbr;
    }

    public void setLeagueAbbr(String leagueAbbr) {
        this.leagueAbbr = leagueAbbr;
    }

    public String getSubLeagueAbbr() {
        return subLeagueAbbr;
    }

    public void setSubLeagueAbbr(String subLeagueAbbr) {
        this.subLeagueAbbr = subLeagueAbbr;
    }

    public String getStatistic() {
        return statistic;
    }

    public void setStatistic(String statistic) {
        this.statistic = statistic;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }
}
