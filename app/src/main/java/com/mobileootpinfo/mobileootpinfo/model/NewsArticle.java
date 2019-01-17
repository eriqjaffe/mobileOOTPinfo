package com.mobileootpinfo.mobileootpinfo.model;

import android.view.View;

public class NewsArticle {
    int messageID, playerID, messageType, recipientID, leagueID;
    String subject, date, message;
    NewsArticle article;
    View view;

    public NewsArticle() {}

    public NewsArticle(int messageID, int playerID, int messageType, int recipientID, String subject, String date, NewsArticle article, View view, int leagueID, String message) {
        this.messageID = messageID;
        this.playerID = playerID;
        this.messageType = messageType;
        this.recipientID = recipientID;
        this.subject = subject;
        this.date = date;
        this.article = article;
        this.view = view;
        this.leagueID = leagueID;
        this.message = message;
    }

    public int getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public int getRecipientID() {
        return recipientID;
    }

    public void setRecipientID(int recipientID) {
        this.recipientID = recipientID;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public NewsArticle getArticle() {
        return article;
    }

    public void setArticle(NewsArticle article) {
        this.article = article;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public int getLeagueID() {
        return leagueID;
    }

    public void setLeagueID(int leagueID) {
        this.leagueID = leagueID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
