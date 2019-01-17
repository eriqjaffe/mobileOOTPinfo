package com.mobileootpinfo.mobileootpinfo.model;

import java.util.List;
import java.util.Map;

public class GameLogInning {
    String header, subHeader, footer;
    List<Map<String, String>> playerLines;

    public GameLogInning() {
    }

    public GameLogInning(String header, String subHeader, String footer, List<Map<String, String>> playerLines) {
        this.header = header;
        this.subHeader = subHeader;
        this.footer = footer;
        this.playerLines = playerLines;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getSubHeader() {
        return subHeader;
    }

    public void setSubHeader(String subHeader) {
        this.subHeader = subHeader;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public List<Map<String, String>> getPlayerLines() {
        return playerLines;
    }

    public void setPlayerLines(List<Map<String, String>> playerLines) {
        this.playerLines = playerLines;
    }
}
