package com.mobileootpinfo.mobileootpinfo.model;

/**
 * Created by eriqj on 3/14/2018.
 */

public class ManagerStatLine {

    int managerID, year, w, l, hr, sb, k, madePlayoffs, wonPlayoffs;
    String name, nickname, abbr, leagueAbbr, pct, avg, era;

    public ManagerStatLine() {}

    public ManagerStatLine(int managerID, int year, int w, int l, int hr, int sb, int k, int madePlayoffs,
                           int wonPlayoffs, String name, String nickname, String abbr, String leagueAbbr, String pct, String avg, String era) {
        this.managerID = managerID;
        this.year = year;
        this.w = w;
        this.l = l;
        this.hr = hr;
        this.sb = sb;
        this.k = k;
        this.madePlayoffs = madePlayoffs;
        this.wonPlayoffs = wonPlayoffs;
        this.name = name;
        this.nickname = nickname;
        this.abbr = abbr;
        this.leagueAbbr = abbr;
        this.pct = pct;
        this.avg = avg;
        this.era = era;
    }

    public int getManagerID() {
        return managerID;
    }

    public void setManagerID(int managerID) {
        this.managerID = managerID;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
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

    public int getHr() {
        return hr;
    }

    public void setHr(int hr) {
        this.hr = hr;
    }

    public int getSb() {
        return sb;
    }

    public void setSb(int sb) {
        this.sb = sb;
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public int getMadePlayoffs() {
        return madePlayoffs;
    }

    public void setMadePlayoffs(int madePlayoffs) {
        this.madePlayoffs = madePlayoffs;
    }

    public int getWonPlayoffs() {
        return wonPlayoffs;
    }

    public void setWonPlayoffs(int wonPlayoffs) {
        this.wonPlayoffs = wonPlayoffs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    public String getPct() {
        return pct;
    }

    public void setPct(String pct) {
        this.pct = pct;
    }

    public String getAvg() {
        return avg;
    }

    public void setAvg(String avg) {
        this.avg = avg;
    }

    public String getEra() {
        return era;
    }

    public void setEra(String era) {
        this.era = era;
    }

    public String getLeagueAbbr() {
        return leagueAbbr;
    }

    public void setLeagueAbbr(String leagueAbbr) {
        this.leagueAbbr = leagueAbbr;
    }
}
