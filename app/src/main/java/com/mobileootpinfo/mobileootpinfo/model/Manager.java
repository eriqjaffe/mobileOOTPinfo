package com.mobileootpinfo.mobileootpinfo.model;

/**
 * Created by Eriq on 3/13/2018.
 */

public class Manager {

    int humanManagerID;
    String firstName;
    String lastName;
    String nickname;
    int w;
    int l;
    String pct;
    int years;
    int championships;
    int retired;

    public Manager() {}

    public Manager(int humanManagerID, String firstName, String lastName, String nickname, int w, int l, String pct, int years, int championships, int retired) {
        this.humanManagerID = humanManagerID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nickname = nickname;
        this.w = w;
        this.l = l;
        this.pct = pct;
        this.years = years;
        this.championships = championships;
        this.retired = retired;
    }

    public int getHumanManagerID() {
        return humanManagerID;
    }

    public void setHumanManagerID(int humanManagerID) {
        this.humanManagerID = humanManagerID;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public String getPct() {
        return pct;
    }

    public void setPct(String pct) {
        this.pct = pct;
    }

    public int getYears() {
        return years;
    }

    public void setYears(int years) {
        this.years = years;
    }

    public int getChampionships() {
        return championships;
    }

    public void setChampionships(int championships) {
        this.championships = championships;
    }

    public int getRetired() {
        return retired;
    }

    public void setRetired(int retired) {
        this.retired = retired;
    }
}
