package com.mobileootpinfo.mobileootpinfo.model;

public class Ballpark {

    int leftLine, leftField, leftCenter, centerField, rightCenter, rightField, rightLine, type, turf;
    String name, avg, avg_l, avg_r, d, t, hr, hr_l, hr_r, team_logo, capacity;

    public Ballpark() {}

    public Ballpark(int leftLine, int leftField, int leftCenter, int centerField, int rightCenter, int rightField, int rightLine,
                    String capacity, int type, int turf, String name, String avg, String avg_l, String avg_r, String d, String t,
                    String hr, String hr_l, String hr_r, String team_logo) {
        this.leftLine = leftLine;
        this.leftField = leftField;
        this.leftCenter = leftCenter;
        this.centerField = centerField;
        this.rightCenter = rightCenter;
        this.rightField = rightField;
        this.rightLine = rightLine;
        this.capacity = capacity;
        this.type = type;
        this.turf = turf;
        this.name = name;
        this.avg = avg;
        this.avg_l = avg_l;
        this.avg_r = avg_r;
        this.d = d;
        this.t = t;
        this.hr = hr;
        this.hr_l = hr_l;
        this.hr_r = hr_r;
        this.team_logo = team_logo;
    }

    public int getLeftLine() {
        return leftLine;
    }

    public void setLeftLine(int leftLine) {
        this.leftLine = leftLine;
    }

    public int getLeftField() {
        return leftField;
    }

    public void setLeftField(int leftField) {
        this.leftField = leftField;
    }

    public int getLeftCenter() {
        return leftCenter;
    }

    public void setLeftCenter(int leftCenter) {
        this.leftCenter = leftCenter;
    }

    public int getCenterField() {
        return centerField;
    }

    public void setCenterField(int centerField) {
        this.centerField = centerField;
    }

    public int getRightCenter() {
        return rightCenter;
    }

    public void setRightCenter(int rightCenter) {
        this.rightCenter = rightCenter;
    }

    public int getRightField() {
        return rightField;
    }

    public void setRightField(int rightField) {
        this.rightField = rightField;
    }

    public int getRightLine() {
        return rightLine;
    }

    public void setRightLine(int rightLine) {
        this.rightLine = rightLine;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTurf() {
        return turf;
    }

    public void setTurf(int turf) {
        this.turf = turf;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvg() {
        return avg;
    }

    public void setAvg(String avg) {
        this.avg = avg;
    }

    public String getAvg_l() {
        return avg_l;
    }

    public void setAvg_l(String avg_l) {
        this.avg_l = avg_l;
    }

    public String getAvg_r() {
        return avg_r;
    }

    public void setAvg_r(String avg_r) {
        this.avg_r = avg_r;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getHr() {
        return hr;
    }

    public void setHr(String hr) {
        this.hr = hr;
    }

    public String getHr_l() {
        return hr_l;
    }

    public void setHr_l(String hr_l) {
        this.hr_l = hr_l;
    }

    public String getHr_r() {
        return hr_r;
    }

    public void setHr_r(String hr_r) {
        this.hr_r = hr_r;
    }

    public String getTeam_logo() {
        return team_logo;
    }

    public void setTeam_logo(String team_logo) {
        this.team_logo = team_logo;
    }
}
