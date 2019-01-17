package com.mobileootpinfo.mobileootpinfo.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by eriqj on 2/22/2018.
 */

public class GameSummary implements Serializable {
    List<Integer> awayLine, homeLine, innings;
    String awayBGColor, awayRecord, awayTeamAbbr, awayTeamLogo, awayTeamName, awayTeamNickname, awayTextColor, homeBGColor, homeRecord, homeTeamAbbr,
            homeTeamLogo, homeTeamName, homeTeamNickhame, homeTextColor, losingPitcherLine, losingPitcherName, savingPitcherLine, savingPitcherName,
            winningPitcherLine, winningPitcherName, park, date, time;
    int awayErrors, awayHits, awayScore, awayTeamId, gameId, gameType, homeErrors, homeHits, homeScore, homeTeamId, losingPitcher,
            savingPitcher, winningPitcher, attendance;

    public GameSummary() {}

    public GameSummary(List<Integer> awayLine, List<Integer> homeLine, List<Integer> innings, String awayBGColor, String awayRecord,
                       String awayTeamAbbr, String awayTeamLogo, String awayTeamName, String awayTeamNickname, String awayTextColor,
                       String homeBGColor, String homeRecord, String homeTeamAbbr, String homeTeamLogo, String homeTeamName, String homeTeamNickhame,
                       String homeTextColor, String losingPitcherLine, String losingPitcherName, String savingPitcherLine, String savingPitcherName,
                       String winningPitcherLine, String winningPitcherName, int awayErrors, int awayHits, int awayScore, int awayTeamId,
                       int gameId, int gameType, int homeErrors, int homeHits, int homeScore, int homeTeamId, int losingPitcher,
                       int savingPitcher, int winningPitcher, String park, int attendance, String date, String time) {
        this.awayLine = awayLine;
        this.homeLine = homeLine;
        this.innings = innings;
        this.awayBGColor = awayBGColor;
        this.awayRecord = awayRecord;
        this.awayTeamAbbr = awayTeamAbbr;
        this.awayTeamLogo = awayTeamLogo;
        this.awayTeamName = awayTeamName;
        this.awayTeamNickname = awayTeamNickname;
        this.awayTextColor = awayTextColor;
        this.homeBGColor = homeBGColor;
        this.homeRecord = homeRecord;
        this.homeTeamAbbr = homeTeamAbbr;
        this.homeTeamLogo = homeTeamLogo;
        this.homeTeamName = homeTeamName;
        this.homeTeamNickhame = homeTeamNickhame;
        this.homeTextColor = homeTextColor;
        this.losingPitcherLine = losingPitcherLine;
        this.losingPitcherName = losingPitcherName;
        this.savingPitcherLine = savingPitcherLine;
        this.savingPitcherName = savingPitcherName;
        this.winningPitcherLine = winningPitcherLine;
        this.winningPitcherName = winningPitcherName;
        this.awayErrors = awayErrors;
        this.awayHits = awayHits;
        this.awayScore = awayScore;
        this.awayTeamId = awayTeamId;
        this.gameId = gameId;
        this.gameType = gameType;
        this.homeErrors = homeErrors;
        this.homeHits = homeHits;
        this.homeScore = homeScore;
        this.homeTeamId = homeTeamId;
        this.losingPitcher = losingPitcher;
        this.savingPitcher = savingPitcher;
        this.winningPitcher = winningPitcher;
        this.park = park;
        this.attendance = attendance;
        this.date = date;
        this.time = time;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getHomeTeamName() {
        return homeTeamName;
    }

    public void setHomeTeamName(String homeTeamName) {
        this.homeTeamName = homeTeamName;
    }

    public String getHomeTeamNickhame() {
        return homeTeamNickhame;
    }

    public void setHomeTeamNickhame(String homeTeamNickhame) {
        this.homeTeamNickhame = homeTeamNickhame;
    }

    public String getHomeTeamLogo() {
        return homeTeamLogo;
    }

    public void setHomeTeamLogo(String homeTeamLogo) {
        this.homeTeamLogo = homeTeamLogo;
    }

    public String getAwayTeamName() {
        return awayTeamName;
    }

    public void setAwayTeamName(String awayTeamName) {
        this.awayTeamName = awayTeamName;
    }

    public String getAwayTeamNickname() {
        return awayTeamNickname;
    }

    public void setAwayTeamNickname(String awayTeamNickname) {
        this.awayTeamNickname = awayTeamNickname;
    }

    public String getAwayTeamLogo() {
        return awayTeamLogo;
    }

    public void setAwayTeamLogo(String awayTeamLogo) {
        this.awayTeamLogo = awayTeamLogo;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(int awayScore) {
        this.awayScore = awayScore;
    }

    public int getWinningPitcher() {
        return winningPitcher;
    }

    public void setWinningPitcher(int winningPitcher) {
        this.winningPitcher = winningPitcher;
    }

    public String getWinningPitcherName() {
        return winningPitcherName;
    }

    public void setWinningPitcherName(String winningPitcherName) {
        this.winningPitcherName = winningPitcherName;
    }

    public int getLosingPitcher() {
        return losingPitcher;
    }

    public void setLosingPitcher(int losingPitcher) {
        this.losingPitcher = losingPitcher;
    }

    public String getLosingPitcherName() {
        return losingPitcherName;
    }

    public void setLosingPitcherName(String losingPitcherName) {
        this.losingPitcherName = losingPitcherName;
    }

    public int getHomeTeamId() {
        return homeTeamId;
    }

    public void setHomeTeamId(int homeTeamId) {
        this.homeTeamId = homeTeamId;
    }

    public int getAwayTeamId() {
        return awayTeamId;
    }

    public void setAwayTeamId(int awayTeamId) {
        this.awayTeamId = awayTeamId;
    }

    public String getHomeRecord() {
        return homeRecord;
    }

    public void setHomeRecord(String homeRecord) {
        this.homeRecord = homeRecord;
    }

    public String getAwayRecord() {
        return awayRecord;
    }

    public void setAwayRecord(String awayRecord) {
        this.awayRecord = awayRecord;
    }

    public String getWinningPitcherLine() {
        return winningPitcherLine;
    }

    public void setWinningPitcherLine(String winningPitcherLine) {
        this.winningPitcherLine = winningPitcherLine;
    }

    public String getLosingPitcherLine() {
        return losingPitcherLine;
    }

    public void setLosingPitcherLine(String losingPitcherLine) {
        this.losingPitcherLine = losingPitcherLine;
    }

    public int getSavingPitcher() {
        return savingPitcher;
    }

    public void setSavingPitcher(int savingPitcher) {
        this.savingPitcher = savingPitcher;
    }

    public String getSavingPitcherName() {
        return savingPitcherName;
    }

    public void setSavingPitcherName(String savingPitcherName) {
        this.savingPitcherName = savingPitcherName;
    }

    public String getSavingPitcherLine() {
        return savingPitcherLine;
    }

    public void setSavingPitcherLine(String savingPitcherLine) {
        this.savingPitcherLine = savingPitcherLine;
    }

    public int getGameType() {
        return gameType;
    }

    public void setGameType(int gameType) {
        this.gameType = gameType;
    }

    public List<Integer> getInnings() {
        return innings;
    }

    public void setInnings(List<Integer> innings) {
        this.innings = innings;
    }

    public List<Integer> getAwayLine() {
        return awayLine;
    }

    public void setAwayLine(List<Integer> awayLine) {
        this.awayLine = awayLine;
    }

    public List<Integer> getHomeLine() {
        return homeLine;
    }

    public void setHomeLine(List<Integer> homeLine) {
        this.homeLine = homeLine;
    }

    public int getHomeHits() {
        return homeHits;
    }

    public void setHomeHits(int homeHits) {
        this.homeHits = homeHits;
    }

    public int getAwayHits() {
        return awayHits;
    }

    public void setAwayHits(int awayHits) {
        this.awayHits = awayHits;
    }

    public int getHomeErrors() {
        return homeErrors;
    }

    public void setHomeErrors(int homeErrors) {
        this.homeErrors = homeErrors;
    }

    public int getAwayErrors() {
        return awayErrors;
    }

    public void setAwayErrors(int awayErrors) {
        this.awayErrors = awayErrors;
    }

    public String getHomeBGColor() {
        return homeBGColor;
    }

    public void setHomeBGColor(String homeBGColor) {
        this.homeBGColor = homeBGColor;
    }

    public String getHomeTextColor() {
        return homeTextColor;
    }

    public void setHomeTextColor(String homeTextColor) {
        this.homeTextColor = homeTextColor;
    }

    public String getAwayBGColor() {
        return awayBGColor;
    }

    public void setAwayBGColor(String awayBGColor) {
        this.awayBGColor = awayBGColor;
    }

    public String getAwayTextColor() {
        return awayTextColor;
    }

    public void setAwayTextColor(String awayTextColor) {
        this.awayTextColor = awayTextColor;
    }

    public String getAwayTeamAbbr() {
        return awayTeamAbbr;
    }

    public void setAwayTeamAbbr(String awayTeamAbbr) {
        this.awayTeamAbbr = awayTeamAbbr;
    }

    public String getHomeTeamAbbr() {
        return homeTeamAbbr;
    }

    public void setHomeTeamAbbr(String homeTeamAbbr) {
        this.homeTeamAbbr = homeTeamAbbr;
    }

    public String getPark() {
        return park;
    }

    public void setPark(String park) {
        this.park = park;
    }

    public int getAttendance() {
        return attendance;
    }

    public void setAttendance(int attendance) {
        this.attendance = attendance;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}