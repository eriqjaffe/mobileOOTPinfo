package com.mobileootpinfo.mobileootpinfo.model;

public class PitchingLineScore {

    String ip, era, firstName, lastName;
    String playerID, h, r, er, bb, k, hr, pi, w, l, s, outs, bf, playerName;
    int start, ir, irs, gb, fb, hb, balk;

    public PitchingLineScore() {}

    public PitchingLineScore(String ip, String era, String firstName, String lastName, String playerID, String h,
                             String r, String er, String bb, String k, String hr, String pi, String w, String l,
                             String s, String outs, int start, String bf, int ir, int irs, int gb, int fb, int hb, int balk, String playerName) {
        this.ip = ip;
        this.era = era;
        this.firstName = firstName;
        this.lastName = lastName;
        this.playerID = playerID;
        this.h = h;
        this.r = r;
        this.er = er;
        this.bb = bb;
        this.k = k;
        this.hr = hr;
        this.pi = pi;
        this.w = w;
        this.l = l;
        this.s = s;
        this.outs = outs;
        this.start = start;
        this.bf = bf;
        this.ir = ir;
        this.irs = irs;
        this.gb = gb;
        this.fb = fb;
        this.hb = hb;
        this.balk = balk;
        this.playerName = playerName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getEra() {
        return era;
    }

    public void setEra(String era) {
        this.era = era;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPlayerID() {
        return playerID;
    }

    public void setPlayerID(String id) {
        this.playerID = playerID;
    }

    public String getH() {
        return h;
    }

    public void setH(String h) {
        this.h = h;
    }

    public String getR() {
        return r;
    }

    public void setR(String r) {
        this.r = r;
    }

    public String getEr() {
        return er;
    }

    public void setEr(String er) {
        this.er = er;
    }

    public String getBb() {
        return bb;
    }

    public void setBb(String bb) {
        this.bb = bb;
    }

    public String getK() {
        return k;
    }

    public void setK(String k) {
        this.k = k;
    }

    public String getHr() {
        return hr;
    }

    public void setHr(String hr) {
        this.hr = hr;
    }

    public String getPi() {
        return pi;
    }

    public void setPi(String pi) {
        this.pi = pi;
    }

    public String getW() {
        return w;
    }

    public void setW(String w) {
        this.w = w;
    }

    public String getL() {
        return l;
    }

    public void setL(String l) {
        this.l = l;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String getOuts() {
        return outs;
    }

    public void setOuts(String outs) {
        this.outs = outs;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public String getBf() {
        return bf;
    }

    public void setBf(String bf) {
        this.bf = bf;
    }

    public int getIr() {
        return ir;
    }

    public void setIr(int ir) {
        this.ir = ir;
    }

    public int getIrs() {
        return irs;
    }

    public void setIrs(int irs) {
        this.irs = irs;
    }

    public int getGb() {
        return gb;
    }

    public void setGb(int gb) {
        this.gb = gb;
    }

    public int getFb() {
        return fb;
    }

    public void setFb(int fb) {
        this.fb = fb;
    }

    public int getHb() {
        return hb;
    }

    public void setHb(int hb) {
        this.hb = hb;
    }

    public int getBalk() {
        return balk;
    }

    public void setBalk(int balk) {
        this.balk = balk;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}