package com.mobileootpinfo.mobileootpinfo.model;

/**
 * Created by eriqj on 3/12/2018.
 */

public class HistoricalAwardYear {
    int year, hitterOfYearID, pitcherOfYearID, rookieOfYearID, hitterTeamID, pitcherTeamID, rookieTeamID, awardID;
    String subLeague, hitterOfYear, pitcherOfYear, rookieOfYear, hitterTeam, pitcherTeam, rookieTeam, hitterTeamAbbr, pitcherTeamAbbr, rookieTeamAbbr, awardName;

    public HistoricalAwardYear() {}

    public HistoricalAwardYear(int year, int hitterOfYearID, int pitcherOfYearID, int rookieOfYearID, int hitterTeamID, int pitcherTeamID,
                               int rookieTeamID, int awardID, String subLeague, String hitterOfYear, String pitcherOfYear, String rookieOfYear,
                               String hitterTeam, String pitcherTeam, String rookieTeam, String hitterTeamAbbr, String pitcherTeamAbbr,
                               String rookieTeamAbbr, String awardName) {
        this.year = year;
        this.hitterOfYearID = hitterOfYearID;
        this.pitcherOfYearID = pitcherOfYearID;
        this.rookieOfYearID = rookieOfYearID;
        this.hitterTeamID = hitterTeamID;
        this.pitcherTeamID = pitcherTeamID;
        this.rookieTeamID = rookieTeamID;
        this.awardID = awardID;
        this.subLeague = subLeague;
        this.hitterOfYear = hitterOfYear;
        this.pitcherOfYear = pitcherOfYear;
        this.rookieOfYear = rookieOfYear;
        this.hitterTeam = hitterTeam;
        this.pitcherTeam = pitcherTeam;
        this.rookieTeam = rookieTeam;
        this.hitterTeamAbbr = hitterTeamAbbr;
        this.pitcherTeamAbbr = pitcherTeamAbbr;
        this.rookieTeamAbbr = rookieTeamAbbr;
        this.awardName = awardName;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getHitterOfYearID() {
        return hitterOfYearID;
    }

    public void setHitterOfYearID(int hitterOfYearID) {
        this.hitterOfYearID = hitterOfYearID;
    }

    public int getPitcherOfYearID() {
        return pitcherOfYearID;
    }

    public void setPitcherOfYearID(int pitcherOfYearID) {
        this.pitcherOfYearID = pitcherOfYearID;
    }

    public int getRookieOfYearID() {
        return rookieOfYearID;
    }

    public void setRookieOfYearID(int rookieOfYearID) {
        this.rookieOfYearID = rookieOfYearID;
    }

    public int getHitterTeamID() {
        return hitterTeamID;
    }

    public void setHitterTeamID(int hitterTeamID) {
        this.hitterTeamID = hitterTeamID;
    }

    public int getPitcherTeamID() {
        return pitcherTeamID;
    }

    public void setPitcherTeamID(int pitcherTeamID) {
        this.pitcherTeamID = pitcherTeamID;
    }

    public int getRookieTeamID() {
        return rookieTeamID;
    }

    public void setRookieTeamID(int rookieTeamID) {
        this.rookieTeamID = rookieTeamID;
    }

    public int getAwardID() {
        return awardID;
    }

    public void setAwardID(int awardID) {
        this.awardID = awardID;
    }

    public String getSubLeague() {
        return subLeague;
    }

    public void setSubLeague(String subLeague) {
        this.subLeague = subLeague;
    }

    public String getHitterOfYear() {
        return hitterOfYear;
    }

    public void setHitterOfYear(String hitterOfYear) {
        this.hitterOfYear = hitterOfYear;
    }

    public String getPitcherOfYear() {
        return pitcherOfYear;
    }

    public void setPitcherOfYear(String pitcherOfYear) {
        this.pitcherOfYear = pitcherOfYear;
    }

    public String getRookieOfYear() {
        return rookieOfYear;
    }

    public void setRookieOfYear(String rookieOfYear) {
        this.rookieOfYear = rookieOfYear;
    }

    public String getHitterTeam() {
        return hitterTeam;
    }

    public void setHitterTeam(String hitterTeam) {
        this.hitterTeam = hitterTeam;
    }

    public String getPitcherTeam() {
        return pitcherTeam;
    }

    public void setPitcherTeam(String pitcherTeam) {
        this.pitcherTeam = pitcherTeam;
    }

    public String getRookieTeam() {
        return rookieTeam;
    }

    public void setRookieTeam(String rookieTeam) {
        this.rookieTeam = rookieTeam;
    }

    public String getHitterTeamAbbr() {
        return hitterTeamAbbr;
    }

    public void setHitterTeamAbbr(String hitterTeamAbbr) {
        this.hitterTeamAbbr = hitterTeamAbbr;
    }

    public String getPitcherTeamAbbr() {
        return pitcherTeamAbbr;
    }

    public void setPitcherTeamAbbr(String pitcherTeamAbbr) {
        this.pitcherTeamAbbr = pitcherTeamAbbr;
    }

    public String getRookieTeamAbbr() {
        return rookieTeamAbbr;
    }

    public void setRookieTeamAbbr(String rookieTeamAbbr) {
        this.rookieTeamAbbr = rookieTeamAbbr;
    }

    public String getAwardName() {
        return awardName;
    }

    public void setAwardName(String awardName) {
        this.awardName = awardName;
    }
}
