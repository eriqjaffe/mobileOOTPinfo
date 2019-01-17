package com.mobileootpinfo.mobileootpinfo.model;

/**
 * Created by eriqj on 3/8/2018.
 */

public class ScheduleLineGame {

    boolean home;
    boolean win;
    boolean played;
    String opponent;
    String opponentLogo;
    String gameDate;
    String gameTime;
    String result;
    String backgroundColor;
    String textColor;
    int gameType;
    int gameID;

    public ScheduleLineGame() {}

    public ScheduleLineGame(boolean home, boolean win, boolean played, String opponent, String opponentLogo, String gameDate,
                            String gameTime, String result, String backgroundColor, String textColor, int gameType, int gameID) {
        this.home = home;
        this.win = win;
        this.played = played;
        this.opponent = opponent;
        this.opponentLogo = opponentLogo;
        this.gameDate = gameDate;
        this.gameTime = gameTime;
        this.result = result;
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
        this.gameType = gameType;
        this.gameID = gameID;
    }

    public boolean isHome() {
        return home;
    }

    public void setHome(boolean home) {
        this.home = home;
    }

    public boolean isWin() {
        return win;
    }

    public void setWin(boolean win) {
        this.win = win;
    }

    public boolean isPlayed() {
        return played;
    }

    public void setPlayed(boolean played) {
        this.played = played;
    }

    public String getOpponent() {
        return opponent;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    public String getOpponentLogo() {
        return opponentLogo;
    }

    public void setOpponentLogo(String opponentLogo) {
        this.opponentLogo = opponentLogo;
    }

    public String getGameDate() {
        return gameDate;
    }

    public void setGameDate(String gameDate) {
        this.gameDate = gameDate;
    }

    public String getGameTime() {
        return gameTime;
    }

    public void setGameTime(String gameTime) {
        this.gameTime = gameTime;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public int getGameType() {
        return gameType;
    }

    public void setGameType(int gameType) {
        this.gameType = gameType;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }
}
