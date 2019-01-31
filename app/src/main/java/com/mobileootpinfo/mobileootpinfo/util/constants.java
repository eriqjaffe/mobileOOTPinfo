package com.mobileootpinfo.mobileootpinfo.util;

import java.util.LinkedHashMap;

public final class constants {

    private constants() {
    }

    public static final String[] positions = {"","P","C","1B","2B","3B","SS","LF","CF","RF","DH"};
    public static final String[] batting = {"","R","L","S"};
    public static final String[] throwing = {"","R","L"};
    public static final String[] popularity ={"Unknown","Insignificant","Fair","Well Known","Popular","Very Popular","Extremely Popular","Disliked"};
    public static final String[] league_level ={"","Major League","Triple A","Double A","Single A","Short Season A","Rookie League",
            "Independent League","International League","","College","High School"};
    public static final String[] game_types = {"Regular Season", "Exhibition", "Spring Training", "Playoffs", "All-Star Game"};
    public static final String[] short_game_types = {"Regular", "Exhibition", "Spring", "Playoff", "All-Star"};
    public static final LinkedHashMap<String, Integer> battingLeaderStats;
    static {
        battingLeaderStats = new LinkedHashMap<String, Integer>();
        battingLeaderStats.put("Games", 0);
        battingLeaderStats.put("Batting Average", 18);
        battingLeaderStats.put("On-Base Percentage", 19);
        battingLeaderStats.put("Slugging Percentage", 20);
        battingLeaderStats.put("On-Base + Slugging", 25);
        battingLeaderStats.put("WAR", 58);
        battingLeaderStats.put("Runs Created / 27 Outs", 22);
        battingLeaderStats.put("Isolated Power", 23);
        battingLeaderStats.put("At-Bats", 2);
        battingLeaderStats.put("Runs", 11);
        battingLeaderStats.put("Hits", 3);
        battingLeaderStats.put("Total Bases", 5);
        battingLeaderStats.put("Doubles", 6);
        battingLeaderStats.put("Triples", 7);
        battingLeaderStats.put("Home Runs", 8);
        battingLeaderStats.put("Runs Batted In", 10);
        battingLeaderStats.put("Stolen Bases", 9);
        battingLeaderStats.put("Walks", 12);
        battingLeaderStats.put("Intentional Walks", 13);
        battingLeaderStats.put("Hit By Pitch", 14);
        battingLeaderStats.put("Strikeouts", 4);
        battingLeaderStats.put("Sacrifice Hits", 15);
        battingLeaderStats.put("Sacrifice Flies", 16);
    }

    public static final LinkedHashMap<String, Integer> pitchingLeaderStats;
    static {
        pitchingLeaderStats = new LinkedHashMap<String, Integer>();
        pitchingLeaderStats.put("Earned Run Average", 40);
        pitchingLeaderStats.put("Wins", 29);
        pitchingLeaderStats.put("Losses", 30);
        pitchingLeaderStats.put("Winning Percentage", 31);
        pitchingLeaderStats.put("Saves", 32);
        pitchingLeaderStats.put("Games Pitched", 27);
        pitchingLeaderStats.put("Games Started", 28);
        pitchingLeaderStats.put("Complete Games", 54);
        pitchingLeaderStats.put("Shutouts", 56);
        pitchingLeaderStats.put("Innings Pitched", 34);
        pitchingLeaderStats.put("Hits Allowed", -1);
        pitchingLeaderStats.put("Home Runs Allowed", 36);
        pitchingLeaderStats.put("Walks Allowed", 37);
        pitchingLeaderStats.put("Walks Per 9 Innings", 47);
        pitchingLeaderStats.put("Strikeouts", 38);
        pitchingLeaderStats.put("Strikeouts Per 9 Innings", 48);
        pitchingLeaderStats.put("Strikeout/Walk Ratio", 43);
        pitchingLeaderStats.put("WHIP", 42);
        pitchingLeaderStats.put("Hits Per 9 Innings", 46);
        pitchingLeaderStats.put("WAR", 59);
    }

}
