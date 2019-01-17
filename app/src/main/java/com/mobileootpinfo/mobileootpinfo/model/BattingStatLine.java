package com.mobileootpinfo.mobileootpinfo.model;

/**
 * Created by eriqj on 3/1/2018.
 */

public class BattingStatLine {

    String team;
    String year;
    String games;
    String atbats;
    String runs;
    String hits;
    String doubles;
    String triples;
    String homeruns;
    String rbi;
    String sb;
    String walks;
    String strikeouts;
    String war;
    String hbp;
    String sf;
    String avg;
    String obp;
    String slg;
    String ops;

    public BattingStatLine() {}

    public BattingStatLine(String team, String year, String games, String atbats, String runs, String hits, String doubles, String triples,
                           String homeruns, String rbi, String sb, String walks, String strikeouts, String war, String hbp, String sf,
                           String avg, String obp, String slg, String ops) {
        this.team = team;
        this.year = year;
        this.games = games;
        this.atbats = atbats;
        this.runs = runs;
        this.hits = hits;
        this.doubles = doubles;
        this.triples = triples;
        this.homeruns = homeruns;
        this.rbi = rbi;
        this.sb = sb;
        this.walks = walks;
        this.strikeouts = strikeouts;
        this.war = war;
        this.hbp = hbp;
        this.sf = sf;
        this.avg = avg;
        this.obp = obp;
        this.slg = slg;
        this.ops = ops;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getGames() {
        return games;
    }

    public void setGames(String games) {
        this.games = games;
    }

    public String getAtbats() {
        return atbats;
    }

    public void setAtbats(String atbats) {
        this.atbats = atbats;
    }

    public String getRuns() {
        return runs;
    }

    public void setRuns(String runs) {
        this.runs = runs;
    }

    public String getHits() {
        return hits;
    }

    public void setHits(String hits) {
        this.hits = hits;
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

    public String getHomeruns() {
        return homeruns;
    }

    public void setHomeruns(String homeruns) {
        this.homeruns = homeruns;
    }

    public String getRbi() {
        return rbi;
    }

    public void setRbi(String rbi) {
        this.rbi = rbi;
    }

    public String getSb() {
        return sb;
    }

    public void setSb(String sb) {
        this.sb = sb;
    }

    public String getWalks() {
        return walks;
    }

    public void setWalks(String walks) {
        this.walks = walks;
    }

    public String getStrikeouts() {
        return strikeouts;
    }

    public void setStrikeouts(String strikeouts) {
        this.strikeouts = strikeouts;
    }

    public String getWar() {
        return war;
    }

    public void setWar(String war) {
        this.war = war;
    }

    public String getHbp() {
        return hbp;
    }

    public void setHbp(String hbp) {
        this.hbp = hbp;
    }

    public String getSf() {
        return sf;
    }

    public void setSf(String sf) {
        this.sf = sf;
    }

    public String getAvg() {
        return avg;
    }

    public void setAvg(String avg) {
        this.avg = avg;
    }

    public String getObp() {
        return obp;
    }

    public void setObp(String obp) {
        this.obp = obp;
    }

    public String getSlg() {
        return slg;
    }

    public void setSlg(String slg) {
        this.slg = slg;
    }

    public String getOps() {
        return ops;
    }

    public void setOps(String ops) {
        this.ops = ops;
    }
}
