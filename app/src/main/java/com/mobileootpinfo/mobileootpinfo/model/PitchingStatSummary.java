package com.mobileootpinfo.mobileootpinfo.model;

import java.math.BigDecimal;

/**
 * Created by eriqj on 2/26/2018.
 */

public class PitchingStatSummary {
    int w;
    int l;
    BigDecimal era;
    String ip;
    int so;
    BigDecimal whip;

    public PitchingStatSummary() {}

    public PitchingStatSummary(int w, int l, BigDecimal era, String ip, int so, BigDecimal whip) {
        this.w = w;
        this.l = l;
        this.era = era;
        this.ip = ip;
        this.so = so;
        this.whip = whip;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getL() {
        return l;
    }

    public void setL(int l) {
        this.l = l;
    }

    public BigDecimal getEra() {
        return era;
    }

    public void setEra(BigDecimal era) {
        this.era = era;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getSo() {
        return so;
    }

    public void setSo(int so) {
        this.so = so;
    }

    public BigDecimal getWhip() {
        return whip;
    }

    public void setWhip(BigDecimal whip) {
        this.whip = whip;
    }
}
