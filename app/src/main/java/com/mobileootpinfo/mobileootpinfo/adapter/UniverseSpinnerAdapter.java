package com.mobileootpinfo.mobileootpinfo.adapter;

/**
 * Created by eriqj on 2/14/2018.
 */

public class UniverseSpinnerAdapter {

    private int id;
    private String name;
    private String csv;
    private String html;

    public UniverseSpinnerAdapter(int id, String name, String csv, String html) {
        this.id = id;
        this.name = name;
        this.csv = csv;
        this.html = html;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCSV() {
        return csv;
    }

    public void setCSV(String csv) {
        this.csv = csv;
    }

    public String getHTML() { return html; }

    public void setHTML(String html) { this.html = html; }

    //to display object as a string in spinner
    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof UniverseSpinnerAdapter){
            UniverseSpinnerAdapter c = (UniverseSpinnerAdapter)obj;
            if(c.getName().equals(name) && c.getId()==id && c.getCSV()==csv && c.getHTML()==html) return true;
        }

        return false;
    }
}
