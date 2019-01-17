package com.mobileootpinfo.mobileootpinfo.model;

/**
 * Created by eriqj on 3/1/2018.
 */

public class PitchingStatLine {
    String team;
    String year;
    String wins;
    String losses;
    String games;
    String gamesStarted;
    String completeGames;
    String shutouts;
    String saves;
    String era;
    String inningsPitched;
    String hits;
    String homeRuns;
    String runs;
    String earnedRuns;
    String walks;
    String strikeouts;
    String whip;
    String war;

    public PitchingStatLine() {}

    public PitchingStatLine(String team, String year, String wins, String losses, String games, String gamesStarted, String completeGames, String shutouts,
                            String saves, String era, String inningsPitched, String hits, String homeRuns, String runs, String earnedRuns,
                            String walks, String strikeouts, String whip, String war) {
        this.team = team;
        this.year = year;
        this.wins = wins;
        this.losses = losses;
        this.games = games;
        this.gamesStarted = gamesStarted;
        this.completeGames = completeGames;
        this.shutouts = shutouts;
        this.saves = saves;
        this.era = era;
        this.inningsPitched = inningsPitched;
        this.hits = hits;
        this.homeRuns = homeRuns;
        this.runs = runs;
        this.earnedRuns = earnedRuns;
        this.walks = walks;
        this.strikeouts = strikeouts;
        this.whip = whip;
        this.war = war;
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

    public String getWins() {
        return wins;
    }

    public void setWins(String wins) {
        this.wins = wins;
    }

    public String getLosses() {
        return losses;
    }

    public void setLosses(String losses) {
        this.losses = losses;
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

    public String getCompleteGames() {
        return completeGames;
    }

    public void setCompleteGames(String completeGames) {
        this.completeGames = completeGames;
    }

    public String getShutouts() {
        return shutouts;
    }

    public void setShutouts(String shutouts) {
        this.shutouts = shutouts;
    }

    public String getSaves() {
        return saves;
    }

    public void setSaves(String saves) {
        this.saves = saves;
    }

    public String getEra() {
        return era;
    }

    public void setEra(String era) {
        this.era = era;
    }

    public String getInningsPitched() {
        return inningsPitched;
    }

    public void setInningsPitched(String inningsPitched) {
        this.inningsPitched = inningsPitched;
    }

    public String getHits() {
        return hits;
    }

    public void setHits(String hits) {
        this.hits = hits;
    }

    public String getHomeRuns() {
        return homeRuns;
    }

    public void setHomeRuns(String homeRuns) {
        this.homeRuns = homeRuns;
    }

    public String getRuns() {
        return runs;
    }

    public void setRuns(String runs) {
        this.runs = runs;
    }

    public String getEarnedRuns() {
        return earnedRuns;
    }

    public void setEarnedRuns(String earnedRuns) {
        this.earnedRuns = earnedRuns;
    }

    public String getWalks() {
        return walks;
    }

    public void setWalks(String walks) {
        this.walks = walks;
    }

    public String getStrikeouts() {
        return strikeouts;
    }

    public void setStrikeouts(String strikeouts) {
        this.strikeouts = strikeouts;
    }

    public String getWhip() {
        return whip;
    }

    public void setWhip(String whip) {
        this.whip = whip;
    }

    public String getWar() {
        return war;
    }

    public void setWar(String war) {
        this.war = war;
    }
}
