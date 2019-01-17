package com.mobileootpinfo.mobileootpinfo.model;

public class StandingsLine {
    String name;
    String nickname;
    int wins;
    int losses;
    int position;
    float gamesBehind;
    String percentage;
    int magicNumber;

    public StandingsLine() {}

    public StandingsLine(String name, String nickname,
                         int wins, int losses, int position,
                         float gamesBehind, String percentage, int magicNumber) {
        this.name = name;
        this.nickname = nickname;
        this.wins = wins;
        this.losses = losses;
        this.position = position;
        this.gamesBehind = gamesBehind;
        this.percentage = percentage;
        this.magicNumber = magicNumber;
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

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public float getGamesBehind() {
        return gamesBehind;
    }

    public void setGamesBehind(float gamesBehind) {
        this.gamesBehind = gamesBehind;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public int getMagicNumber() {
        return magicNumber;
    }

    public void setMagicNumber(int magicNumber) {
        this.magicNumber = magicNumber;
    }
}



