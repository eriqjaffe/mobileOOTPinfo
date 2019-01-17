package com.mobileootpinfo.mobileootpinfo.model;

public class ShortPlayer {
    public int id;
    public String firstName;
    public String lastName;
    public String teamAbbr;
    public String leagueAbbr;

    public ShortPlayer() {}

    public ShortPlayer(int id, String firstName, String lastName, String teamAbbr, String leagueAbbr) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.teamAbbr = teamAbbr;
        this.leagueAbbr = leagueAbbr;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
