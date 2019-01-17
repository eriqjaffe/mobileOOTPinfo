package com.mobileootpinfo.mobileootpinfo.model;

/**
 * Created by Eriq on 3/10/2018.
 */

public class LeagueSpinner {
    int id;
    int name;

    public LeagueSpinner() {}

    public LeagueSpinner(int id, int name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }
}
