package com.mobileootpinfo.mobileootpinfo.model;

public class HistoricalLeader {
    String playerID, firstName, lastName, abbr, subLeague, year, amount, place;
    int subLeagueID, category;

    public HistoricalLeader() {}

    public HistoricalLeader(String playerID, String firstName, String lastName, String abbr,
                            String subLeague, String year, int category, String amount, String place, int subLeagueID) {
        this.playerID = playerID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.abbr = abbr;
        this.subLeague = subLeague;
        this.year = year;
        this.category = category;
        this.amount = amount;
        this.place = place;
        this.subLeagueID = subLeagueID;
    }

    public String getPlayerID() {
        return playerID;
    }

    public void setPlayerID(String playerID) {
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

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    public String getSubLeague() {
        return subLeague;
    }

    public void setSubLeague(String subLeague) {
        this.subLeague = subLeague;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public int getSubLeagueID() {
        return subLeagueID;
    }

    public void setSubLeagueID(int subLeagueID) {
        this.subLeagueID = subLeagueID;
    }
}
