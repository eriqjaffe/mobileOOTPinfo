package com.mobileootpinfo.mobileootpinfo.model;

import java.util.List;

public class BattingGameNotes {

    String doubles, triples, homeRuns, totalBases, risp2Outs, gidp, sacBunt, sacFly, HBP, teamLOB, twoOutRBI, sb, cs, errors, pb, doublePlays;
    List<String> subs;

    public BattingGameNotes() {
    }

    public BattingGameNotes(String doubles, String triples, String homeRuns, String totalBases, String risp2Outs, String gidp,
                            String sacBunt, String sacFly, String HBP, String teamLOB, String twoOutRBI, String hbp,
                            String sb, String cs, String errors, String pb, String doublePlays, List<String> subs) {
        this.doubles = doubles;
        this.triples = triples;
        this.homeRuns = homeRuns;
        this.totalBases = totalBases;
        this.risp2Outs = risp2Outs;
        this.gidp = gidp;
        this.sacBunt = sacBunt;
        this.sacFly = sacFly;
        this.HBP = HBP;
        this.teamLOB = teamLOB;
        this.twoOutRBI = twoOutRBI;
        this.sb = sb;
        this.cs = cs;
        this.subs = subs;
        this.errors = errors;
        this.pb = pb;
        this.doublePlays = doublePlays;
        this.subs = subs;
    }

    public String getDoubles() {
        return doubles;
    }

    public void setDoubles(String doubles) {
        this.doubles = doubles;
    }

    public String getTriples() {
        return triples;
    }

    public void setTriples(String triples) {
        this.triples = triples;
    }

    public String getHomeRuns() {
        return homeRuns;
    }

    public void setHomeRuns(String homeRuns) {
        this.homeRuns = homeRuns;
    }

    public String getTotalBases() {
        return totalBases;
    }

    public void setTotalBases(String totalBases) {
        this.totalBases = totalBases;
    }

    public String getRisp2Outs() {
        return risp2Outs;
    }

    public void setRisp2Outs(String risp2Outs) {
        this.risp2Outs = risp2Outs;
    }

    public String getGidp() {
        return gidp;
    }

    public void setGidp(String gidp) {
        this.gidp = gidp;
    }

    public String getSacBunt() {
        return sacBunt;
    }

    public void setSacBunt(String sacBunt) {
        this.sacBunt = sacBunt;
    }

    public String getSacFly() {
        return sacFly;
    }

    public void setSacFly(String sacFly) {
        this.sacFly = sacFly;
    }

    public String getHBP() {
        return HBP;
    }

    public void setHBP(String HBP) {
        this.HBP = HBP;
    }

    public String getTeamLOB() {
        return teamLOB;
    }

    public void setTeamLOB(String teamLOB) {
        this.teamLOB = teamLOB;
    }

    public String getTwoOutRBI() {
        return twoOutRBI;
    }

    public void setTwoOutRBI(String twoOutRBI) {
        this.twoOutRBI = twoOutRBI;
    }

    public List<String> getSubs() {
        return subs;
    }

    public void setSubs(List<String> subs) {
        this.subs = subs;
    }

    public String getSb() {
        return sb;
    }

    public void setSb(String sb) {
        this.sb = sb;
    }

    public String getCs() {
        return cs;
    }

    public void setCs(String cs) {
        this.cs = cs;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

    public String getPb() {
        return pb;
    }

    public void setPb(String pb) {
        this.pb = pb;
    }

    public String getDoublePlays() {
        return doublePlays;
    }

    public void setDoublePlays(String doublePlays) {
        this.doublePlays = doublePlays;
    }
}
