package com.mobileootpinfo.mobileootpinfo.model;

/**
 * Created by eriqj on 3/13/2018.
 */

public class Champion {
    int teamID, year, w, l, r, hr, sb, sho, k;
    String name, nickname, firstName, lastName, pct, avg, era;

    public Champion() {}

    public Champion(int teamID, int year, int w, int l, int r, int hr, int sb, int sho, int k,
                    String name, String nickname, String firstName, String lastName, String pct, String avg,
                    String era) {
        this.teamID = teamID;
        this.year = year;
        this.w = w;
        this.l = l;
        this.r = r;
        this.hr = hr;
        this.sb = sb;
        this.sho = sho;
        this.k = k;
        this.name = name;
        this.nickname = nickname;
        this.firstName = firstName;
        this.lastName = lastName;
        this.pct = pct;
        this.avg = avg;
        this.era = era;
    }

    public int getTeamID() {
        return teamID;
    }

    public void setTeamID(int teamID) {
        this.teamID = teamID;
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

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
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

    public int getSho() {
        return sho;
    }

    public void setSho(int sho) {
        this.sho = sho;
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
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
}
