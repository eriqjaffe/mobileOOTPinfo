package com.mobileootpinfo.mobileootpinfo.model;

public class League {
    int _id;
    String _name;
    String _csv;
    String _html;
    boolean _defaultLeague;

    public League() {}

    public League (String name, String csv, String html, boolean defaultLeague) {
        this._name = name;
        this._csv = csv;
        this._html = html;
        this._defaultLeague = defaultLeague;
    }

    public League (int id, String name, String csv, String html, boolean defaultLeague) {
        this._id = id;
        this._name = name;
        this._csv = csv;
        this._html = html;
        this._defaultLeague = defaultLeague;
    }

    public int getID() { return this._id; }

    public void setID(int id) { this._id = id; }

    public String getName() { return this._name; }

    public void setName(String name) { this._name = name; }

    public String getCSV() { return this._csv; }

    public void setCSV(String url) { this._csv = url; }

    public String getHTML() { return _html; }

    public void setHTML(String _html) { this._html = _html; }

    public boolean getDefaultLeague() { return this._defaultLeague; }

    public void setDefaultLeague(boolean defaultLeague) { this._defaultLeague = defaultLeague; }
}
