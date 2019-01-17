package com.mobileootpinfo.mobileootpinfo.model;

/**
 * Created by eriqj on 3/2/2018.
 */

public class Award {
    String season;
    String recipient;
    int year;
    int month;
    int day;
    String award;
    String league;
    String subLeague;
    int awardID;
    int position;

    public Award() {}

    public Award(int year, String season, String recipient, int month, int day, String award, String league, String subLeague, int awardID, int position) {
        this.season = season;
        this.recipient = recipient;
        this.year = year;
        this.month = month;
        this.day = day;
        this.award = award;
        this.league = league;
        this.subLeague = subLeague;
        this.awardID = awardID;
        this.position = position;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getAward() {
        return award;
    }

    public void setAward(String award) {
        this.award = award;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getSubLeague() {
        return subLeague;
    }

    public void setSubLeague(String subLeague) {
        this.subLeague = subLeague;
    }

    public int getAwardID() {
        return awardID;
    }

    public void setAwardID(int awardID) {
        this.awardID = awardID;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
