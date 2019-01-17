package com.mobileootpinfo.mobileootpinfo.model;

import java.util.List;

public class JsoupBoxScore {

    List<BattingLineScore> awayBattingLines, homeBattingLines;
    List<PitchingLineScore> awayPitchingLines, homePitchingLines;
    BattingGameNotes awayBattingNotes, homeBattingNotes;
    PitchingGameNotes awayPitchingNotes, homePitchingNotes;
    GameNotes gameNotes;
    List<GameLogInning> gameLog;
    String awayAbbr, homeAbbr;
    boolean jsoup;

    public JsoupBoxScore() {
    }

    public JsoupBoxScore(List<BattingLineScore> awayBattingLines, List<BattingLineScore> homeBattingLines, List<PitchingLineScore> awayPitchingLines,
                         List<PitchingLineScore> homePitchingLines, BattingGameNotes awayBattingNotes, BattingGameNotes homeBattingNotes,
                         PitchingGameNotes awayPitchingNotes, PitchingGameNotes homePitchingNotes, GameNotes gameNotes, String homeAbbr,
                         String awayAbbr, boolean jsoup, List<GameLogInning> gameLog) {
        this.awayBattingLines = awayBattingLines;
        this.homeBattingLines = homeBattingLines;
        this.awayPitchingLines = awayPitchingLines;
        this.homePitchingLines = homePitchingLines;
        this.awayBattingNotes = awayBattingNotes;
        this.homeBattingNotes = homeBattingNotes;
        this.awayPitchingNotes = awayPitchingNotes;
        this.homePitchingNotes = homePitchingNotes;
        this.gameNotes = gameNotes;
        this.homeAbbr = homeAbbr;
        this.awayAbbr = awayAbbr;
        this.jsoup = jsoup;
        this.gameLog = gameLog;
    }

    public List<BattingLineScore> getAwayBattingLines() {
        return awayBattingLines;
    }

    public void setAwayBattingLines(List<BattingLineScore> awayBattingLines) {
        this.awayBattingLines = awayBattingLines;
    }

    public List<BattingLineScore> getHomeBattingLines() {
        return homeBattingLines;
    }

    public void setHomeBattingLines(List<BattingLineScore> homeBattingLines) {
        this.homeBattingLines = homeBattingLines;
    }

    public List<PitchingLineScore> getAwayPitchingLines() {
        return awayPitchingLines;
    }

    public void setAwayPitchingLines(List<PitchingLineScore> awayPitchingLines) {
        this.awayPitchingLines = awayPitchingLines;
    }

    public List<PitchingLineScore> getHomePitchingLines() {
        return homePitchingLines;
    }

    public void setHomePitchingLines(List<PitchingLineScore> homePitchingLines) {
        this.homePitchingLines = homePitchingLines;
    }

    public BattingGameNotes getAwayBattingNotes() {
        return awayBattingNotes;
    }

    public void setAwayBattingNotes(BattingGameNotes awayBattingNotes) {
        this.awayBattingNotes = awayBattingNotes;
    }

    public BattingGameNotes getHomeBattingNotes() {
        return homeBattingNotes;
    }

    public void setHomeBattingNotes(BattingGameNotes homeBattingNotes) {
        this.homeBattingNotes = homeBattingNotes;
    }

    public PitchingGameNotes getAwayPitchingNotes() {
        return awayPitchingNotes;
    }

    public void setAwayPitchingNotes(PitchingGameNotes awayPitchingNotes) {
        this.awayPitchingNotes = awayPitchingNotes;
    }

    public PitchingGameNotes getHomePitchingNotes() {
        return homePitchingNotes;
    }

    public void setHomePitchingNotes(PitchingGameNotes homePitchingNotes) {
        this.homePitchingNotes = homePitchingNotes;
    }

    public GameNotes getGameNotes() {
        return gameNotes;
    }

    public void setGameNotes(GameNotes gameNotes) {
        this.gameNotes = gameNotes;
    }

    public String getAwayAbbr() {
        return awayAbbr;
    }

    public void setAwayAbbr(String awayAbbr) {
        this.awayAbbr = awayAbbr;
    }

    public String getHomeAbbr() {
        return homeAbbr;
    }

    public void setHomeAbbr(String homeAbbr) {
        this.homeAbbr = homeAbbr;
    }

    public boolean isJsoup() {
        return jsoup;
    }

    public void setJsoup(boolean jsoup) {
        this.jsoup = jsoup;
    }

    public List<GameLogInning> getGameLog() {
        return gameLog;
    }

    public void setGameLog(List<GameLogInning> gameLog) {
        this.gameLog = gameLog;
    }
}
