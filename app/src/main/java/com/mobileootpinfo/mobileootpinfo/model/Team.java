package com.mobileootpinfo.mobileootpinfo.model;

/**
 * Created by Eriq on 2/19/2018.
 */

public class Team {

    int id;
    String teamName;
    String teamNickname;
    String teamLogo;
    int level;
    String backgroundColor;
    String textColor;
    int w;
    int l;
    int pos;
    float pct;
    float gb;
    String leagueName;
    String leagueAbbr;
    int leagueID;
    String subLeagueName;
    String subLeagueAbbr;
    int subLeagueID;
    String divisionName;
    String managerFirstName;
    String managerLastName;
    int parentId;
    String parentLogo;
    String teamAbbr;

    public Team() {};

    public Team(int id, String teamName, String teamNickname, String teamAbbr, String teamLogo, int level, String backgroundColor, String textColor,
                int w, int l, int pos, float gb, String leagueName, String leagueAbbr, int leagueID, String subLeagueName, String subLeagueAbbr, int subLeagueID,
                String divisionName, String managerFirstName, String managerLastName, int parentId, String parentLogo) {
        this.id = id;
        this.teamName = teamName;
        this.teamNickname = teamNickname;
        this.teamAbbr = teamAbbr;
        this.teamLogo = teamLogo;
        this.level = level;
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
        this.w = w;
        this.l = l;
        this.pos = pos;
        this.pct = pct;
        this.gb = gb;
        this.leagueName = leagueName;
        this.leagueAbbr = leagueAbbr;
        this.leagueID = leagueID;
        this.subLeagueName = subLeagueName;
        this.subLeagueAbbr = subLeagueAbbr;
        this.subLeagueID = subLeagueID;
        this.divisionName = divisionName;
        this.managerFirstName = managerFirstName;
        this.managerLastName = managerLastName;
        this.parentId = parentId;
        this.parentLogo = parentLogo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getTeamLogo() {
        return teamLogo;
    }

    public void setTeamLogo(String teamLogo) {
        this.teamLogo = teamLogo;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
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

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getL() {
        return l;
    }

    public void setL(int l) {
        this.l = l;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public float getPct() {
        return pct;
    }

    public void setPct(float pct) {
        this.pct = pct;
    }

    public float getGb() {
        return gb;
    }

    public void setGb(float gb) {
        this.gb = gb;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    public String getLeagueAbbr() {
        return leagueAbbr;
    }

    public void setLeagueAbbr(String leagueAbbr) {
        this.leagueAbbr = leagueAbbr;
    }

    public int getLeagueID() {
        return leagueID;
    }

    public void setLeagueID(int leagueID) {
        this.leagueID = leagueID;
    }

    public String getSubLeagueName() {
        return subLeagueName;
    }

    public void setSubLeagueName(String subLeagueName) {
        this.subLeagueName = subLeagueName;
    }

    public String getSubLeagueAbbr() {
        return subLeagueAbbr;
    }

    public void setSubLeagueAbbr(String subLeagueAbbr) {
        this.subLeagueAbbr = subLeagueAbbr;
    }

    public int getSubLeagueID() {
        return subLeagueID;
    }

    public void setSubLeagueID(int subLeagueID) {
        this.subLeagueID = subLeagueID;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public String getManagerFirstName() {
        return managerFirstName;
    }

    public void setManagerFirstName(String managerFirstName) {
        this.managerFirstName = managerFirstName;
    }

    public String getManagerLastName() {
        return managerLastName;
    }

    public void setManagerLastName(String managerLastName) {
        this.managerLastName = managerLastName;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getParentLogo() {
        return parentLogo;
    }

    public void setParentLogo(String parentLogo) {
        this.parentLogo = parentLogo;
    }
}
