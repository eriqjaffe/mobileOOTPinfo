package com.mobileootpinfo.mobileootpinfo.model;

public class GameNotes {

    String potg, ballpark, weather, startTime, time, attendance, notes;

    public GameNotes() {}

    public GameNotes(String potg, String ballpark, String weather, String startTime, String time, String attendance, String notes) {
        this.potg = potg;
        this.ballpark = ballpark;
        this.weather = weather;
        this.startTime = startTime;
        this.time = time;
        this.attendance = attendance;
        this.notes = notes;
    }

    public String getPotg() {
        return potg;
    }

    public void setPotg(String potg) {
        this.potg = potg;
    }

    public String getBallpark() {
        return ballpark;
    }

    public void setBallpark(String ballpark) {
        this.ballpark = ballpark;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
