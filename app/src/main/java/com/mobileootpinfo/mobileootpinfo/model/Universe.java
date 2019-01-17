package com.mobileootpinfo.mobileootpinfo.model;

/**
 * Created by eriqj on 3/7/2018.
 */

public class Universe {
    int _id;
    String _name;
    String _csv;
    String _html;
    boolean _defaultUniverse;
    int _defaultLeague;

    public Universe() {}

    public Universe (String name, String csv, String html, boolean defaultUniverse, int defaultLeague) {
        this._name = name;
        this._csv = csv;
        this._html = html;
        this._defaultUniverse = defaultUniverse;
        this._defaultLeague = defaultLeague;
    }

    public Universe (int id, String name, String csv, String html, boolean defaultUniverse, int defaultLeague) {
        this._id = id;
        this._name = name;
        this._csv = csv;
        this._html = html;
        this._defaultUniverse = defaultUniverse;
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

    public boolean getDefaultUniverse() { return this._defaultUniverse; }

    public void setDefaultUniverse(boolean defaultUniverse) { this._defaultUniverse = defaultUniverse; }

    public int getDefaultLeague() { return this._defaultLeague; }

    public void setDefaultLeague(int defaultLeague) { this._defaultLeague = defaultLeague; }
}
