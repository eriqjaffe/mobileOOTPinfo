package com.mobileootpinfo.mobileootpinfo.model;

public class PitchingGameNotes {

    String gameScore, battersFaced, goFo, pitchesStrikes, irScored, hitBatsmen, wildPitch, balk;

    public PitchingGameNotes() {
    }

    public PitchingGameNotes(String gameScore, String battersFaced, String goFo, String pitchesStrikes, String irScored, String hitBatsmen, String wildPitch, String balk) {
        this.gameScore = gameScore;
        this.battersFaced = battersFaced;
        this.goFo = goFo;
        this.pitchesStrikes = pitchesStrikes;
        this.irScored = irScored;
        this.hitBatsmen = hitBatsmen;
        this.wildPitch = wildPitch;
        this.balk = balk;
    }

    public String getGameScore() {
        return gameScore;
    }

    public void setGameScore(String gameScore) {
        this.gameScore = gameScore;
    }

    public String getBattersFaced() {
        return battersFaced;
    }

    public void setBattersFaced(String battersFaced) {
        this.battersFaced = battersFaced;
    }

    public String getGoFo() {
        return goFo;
    }

    public void setGoFo(String goFo) {
        this.goFo = goFo;
    }

    public String getPitchesStrikes() {
        return pitchesStrikes;
    }

    public void setPitchesStrikes(String pitchesStrikes) {
        this.pitchesStrikes = pitchesStrikes;
    }

    public String getIrScored() {
        return irScored;
    }

    public void setIrScored(String irScored) {
        this.irScored = irScored;
    }

    public String getHitBatsmen() {
        return hitBatsmen;
    }

    public void setHitBatsmen(String hitBatsmen) {
        this.hitBatsmen = hitBatsmen;
    }

    public String getWildPitch() {
        return wildPitch;
    }

    public void setWildPitch(String wildPitch) {
        this.wildPitch = wildPitch;
    }

    public String getBalk() {
        return balk;
    }

    public void setBalk(String balk) {
        this.balk = balk;
    }
}
