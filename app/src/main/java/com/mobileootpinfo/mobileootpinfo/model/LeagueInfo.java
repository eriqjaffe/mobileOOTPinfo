package com.mobileootpinfo.mobileootpinfo.model;

public class LeagueInfo {
    int id;
    String name;
    String abbr;
    String logo;
    String backgroundColor;
    String textColor;
    String currentDate;
    int leagueLevel;
    String html_root;
    int wildcards;
    int universeID;

    public LeagueInfo() {}

    public LeagueInfo(int id, String name, String abbr, String logo, String backgroundColor, String textColor, String currentDate,
                      int leagueLevel, String html_root, int wildcards, int universeID) {
        this.id = id;
        this.name = name;
        this.abbr = abbr;
        this.logo = logo;
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
        this.currentDate = currentDate;
        this.leagueLevel = leagueLevel;
        this.html_root = html_root;
        this.wildcards = wildcards;
        this.universeID = universeID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public int getLeagueLevel() {
        return leagueLevel;
    }

    public void setLeagueLevel(int leagueLevel) {
        this.leagueLevel = leagueLevel;
    }

    public String getHtml_root() {
        return html_root;
    }

    public void setHtml_root(String html_root) {
        this.html_root = html_root;
    }

    public int getWildcards() {
        return wildcards;
    }

    public void setWildcards(int wildcards) {
        this.wildcards = wildcards;
    }

    public int getUniverseID() {
        return universeID;
    }

    public void setUniverseID(int universeID) {
        this.universeID = universeID;
    }
}
