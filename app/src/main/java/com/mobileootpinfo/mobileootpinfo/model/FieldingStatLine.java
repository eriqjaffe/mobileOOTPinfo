package com.mobileootpinfo.mobileootpinfo.model;

/**
 * Created by eriqj on 3/2/2018.
 */

public class FieldingStatLine {
    String team;
    String year;
    String position;
    String games;
    String gamesStarted;
    String putouts;
    String assists;
    String doublePlays;
    String totalChances;
    String errors;
    String inningsPlayed;
    String zoneRating;
    String rangeFactor;
    String fieldingPercentage;

    public FieldingStatLine() {}

    public FieldingStatLine(String team, String year, String position, String games, String gamesStarted, String putouts, String assists,
                            String doublePlays, String totalChances, String errors, String inningsPlayed, String zoneRating, String rangeFactor,
                            String fieldingPercentage) {
        this.team = team;
        this.year = year;
        this.position = position;
        this.games = games;
        this.gamesStarted = gamesStarted;
        this.putouts = putouts;
        this.assists = assists;
        this.doublePlays = doublePlays;
        this.totalChances = totalChances;
        this.errors = errors;
        this.inningsPlayed = inningsPlayed;
        this.zoneRating = zoneRating;
        this.rangeFactor = rangeFactor;
        this.fieldingPercentage = fieldingPercentage;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getGames() {
        return games;
    }

    public void setGames(String games) {
        this.games = games;
    }

    public String getGamesStarted() {
        return gamesStarted;
    }

    public void setGamesStarted(String gamesStarted) {
        this.gamesStarted = gamesStarted;
    }

    public String getPutouts() {
        return putouts;
    }

    public void setPutouts(String putouts) {
        this.putouts = putouts;
    }

    public String getAssists() {
        return assists;
    }

    public void setAssists(String assists) {
        this.assists = assists;
    }

    public String getDoublePlays() {
        return doublePlays;
    }

    public void setDoublePlays(String doublePlays) {
        this.doublePlays = doublePlays;
    }

    public String getTotalChances() {
        return totalChances;
    }

    public void setTotalChances(String totalChances) {
        this.totalChances = totalChances;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

    public String getInningsPlayed() {
        return inningsPlayed;
    }

    public void setInningsPlayed(String inningsPlayed) {
        this.inningsPlayed = inningsPlayed;
    }

    public String getZoneRating() {
        return zoneRating;
    }

    public void setZoneRating(String zoneRating) {
        this.zoneRating = zoneRating;
    }

    public String getRangeFactor() {
        return rangeFactor;
    }

    public void setRangeFactor(String rangeFactor) {
        this.rangeFactor = rangeFactor;
    }

    public String getFieldingPercentage() {
        return fieldingPercentage;
    }

    public void setFieldingPercentage(String fieldingPercentage) {
        this.fieldingPercentage = fieldingPercentage;
    }
}
