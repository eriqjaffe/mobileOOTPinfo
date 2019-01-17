package com.mobileootpinfo.mobileootpinfo.model;

import java.math.BigDecimal;

/**
 * Created by eriqj on 2/26/2018.
 */

public class BattingStatSummary {
    BigDecimal avg;
    int hr;
    int rbi;
    int sb;
    BigDecimal ops;

    public BattingStatSummary() {}

    public BattingStatSummary(BigDecimal avg, int hr, int rbi, int sb, BigDecimal ops) {
        this.avg = avg;
        this.hr = hr;
        this.rbi = rbi;
        this.sb = sb;
        this.ops = ops;
    }

    public BigDecimal getAvg() {
        return avg;
    }

    public void setAvg(BigDecimal avg) {
        this.avg = avg;
    }

    public int getHr() {
        return hr;
    }

    public void setHr(int hr) {
        this.hr = hr;
    }

    public int getRbi() {
        return rbi;
    }

    public void setRbi(int rbi) {
        this.rbi = rbi;
    }

    public int getSb() {
        return sb;
    }

    public void setSb(int sb) {
        this.sb = sb;
    }

    public BigDecimal getOps() {
        return ops;
    }

    public void setOps(BigDecimal ops) {
        this.ops = ops;
    }
}
