package com.mobileootpinfo.mobileootpinfo.model;

public class PendingGameSummary {
    int gameId;
    int homeTeamId;
    int awayTeamId;
    String gameTime;
    String homeRecord;
    String awayRecord;
    String homeTeamName;
    String homeTeamNickhame;
    String homeTeamAbbr;
    String homeTeamLogo;
    String awayTeamName;
    String awayTeamNickname;
    String awayTeamAbbr;
    String awayTeamLogo;
    int awayStarter;
    String awayStarterName;
    int homeStarter;
    String homeStarterName;
    String awayStarterLine;
    String homeStarterLine;
    int gameType;
    String homeBGColor;
    String homeTextColor;
    String awayBGColor;
    String awayTextColor;

    public PendingGameSummary() {}

    public PendingGameSummary(int gameId, int homeTeamId, int awayTeamId, String gameTime, String homeRecord, String awayRecord,
                              String homeTeamName, String homeTeamNickhame, String homeTeamAbbr, String homeTeamLogo, String awayTeamName,
                              String awayTeamNickname, String awayTeamAbbr, String awayTeamLogo, int awayStarter, String awayStarterName,
                              int homeStarter, String homeStarterName, String awayStarterLine, String homeStarterLine, int gameType,
                              String homeBGColor, String homeTextColor, String awayBGColor, String awayTextColor) {
        this.gameId = gameId;
        this.homeTeamId = homeTeamId;
        this.awayTeamId = awayTeamId;
        this.gameTime = gameTime;
        this.homeRecord = homeRecord;
        this.awayRecord = awayRecord;
        this.homeTeamName = homeTeamName;
        this.homeTeamNickhame = homeTeamNickhame;
        this.homeTeamAbbr = homeTeamAbbr;
        this.homeTeamLogo = homeTeamLogo;
        this.awayTeamName = awayTeamName;
        this.awayTeamNickname = awayTeamNickname;
        this.awayTeamAbbr = awayTeamAbbr;
        this.awayTeamLogo = awayTeamLogo;
        this.awayStarter = awayStarter;
        this.awayStarterName = awayStarterName;
        this.homeStarter = homeStarter;
        this.homeStarterName = homeStarterName;
        this.awayStarterLine = awayStarterLine;
        this.homeStarterLine = homeStarterLine;
        this.gameType = gameType;
        this.homeBGColor = homeBGColor;
        this.homeTextColor = homeTextColor;
        this.awayBGColor = awayBGColor;
        this.awayTextColor = awayTextColor;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
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

    public String getGameTime() {
        return gameTime;
    }

    public void setGameTime(String gameTime) {
        this.gameTime = gameTime;
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

    public int getAwayStarter() {
        return awayStarter;
    }

    public void setAwayStarter(int awayStarter) {
        this.awayStarter = awayStarter;
    }

    public String getAwayStarterName() {
        return awayStarterName;
    }

    public void setAwayStarterName(String awayStarterName) {
        this.awayStarterName = awayStarterName;
    }

    public int getHomeStarter() {
        return homeStarter;
    }

    public void setHomeStarter(int homeStarter) {
        this.homeStarter = homeStarter;
    }

    public String getHomeStarterName() {
        return homeStarterName;
    }

    public void setHomeStarterName(String homeStarterName) {
        this.homeStarterName = homeStarterName;
    }

    public String getAwayStarterLine() {
        return awayStarterLine;
    }

    public void setAwayStarterLine(String awayStarterLine) {
        this.awayStarterLine = awayStarterLine;
    }

    public String getHomeStarterLine() {
        return homeStarterLine;
    }

    public void setHomeStarterLine(String homeStarterLine) {
        this.homeStarterLine = homeStarterLine;
    }

    public String getHomeTeamAbbr() {
        return homeTeamAbbr;
    }

    public void setHomeTeamAbbr(String homeTeamAbbr) {
        this.homeTeamAbbr = homeTeamAbbr;
    }

    public String getAwayTeamAbbr() {
        return awayTeamAbbr;
    }

    public void setAwayTeamAbbr(String awayTeamAbbr) {
        this.awayTeamAbbr = awayTeamAbbr;
    }

    public int getGameType() {
        return gameType;
    }

    public void setGameType(int gameType) {
        this.gameType = gameType;
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
}


