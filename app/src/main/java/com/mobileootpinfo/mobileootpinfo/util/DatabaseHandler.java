package com.mobileootpinfo.mobileootpinfo.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.mobileootpinfo.mobileootpinfo.R;
import com.mobileootpinfo.mobileootpinfo.model.Award;
import com.mobileootpinfo.mobileootpinfo.model.Ballpark;
import com.mobileootpinfo.mobileootpinfo.model.BattingLineScore;
import com.mobileootpinfo.mobileootpinfo.model.BattingStatLine;
import com.mobileootpinfo.mobileootpinfo.model.BattingStatSummary;
import com.mobileootpinfo.mobileootpinfo.model.Champion;
import com.mobileootpinfo.mobileootpinfo.model.Contract;
import com.mobileootpinfo.mobileootpinfo.model.Division;
import com.mobileootpinfo.mobileootpinfo.model.FieldingStatLine;
import com.mobileootpinfo.mobileootpinfo.model.GameSummary;
import com.mobileootpinfo.mobileootpinfo.model.HistoricalLeader;
import com.mobileootpinfo.mobileootpinfo.model.League;
import com.mobileootpinfo.mobileootpinfo.model.LeagueInfo;
import com.mobileootpinfo.mobileootpinfo.model.Manager;
import com.mobileootpinfo.mobileootpinfo.model.ManagerStatLine;
import com.mobileootpinfo.mobileootpinfo.model.NewsArticle;
import com.mobileootpinfo.mobileootpinfo.model.PendingGameSummary;
import com.mobileootpinfo.mobileootpinfo.model.PitchingLineScore;
import com.mobileootpinfo.mobileootpinfo.model.PitchingStatLine;
import com.mobileootpinfo.mobileootpinfo.model.PitchingStatSummary;
import com.mobileootpinfo.mobileootpinfo.model.Player;
import com.mobileootpinfo.mobileootpinfo.model.ScheduleLineGame;
import com.mobileootpinfo.mobileootpinfo.model.ShortPlayer;
import com.mobileootpinfo.mobileootpinfo.model.StandingsLine;
import com.mobileootpinfo.mobileootpinfo.model.StatLeader;
import com.mobileootpinfo.mobileootpinfo.model.SubLeague;
import com.mobileootpinfo.mobileootpinfo.model.Team;
import com.mobileootpinfo.mobileootpinfo.model.Universe;
import com.mobileootpinfo.mobileootpinfo.model.UniverseLeague;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.greenrobot.common.ListMap;

/**
 * Created by eriqj on 2/14/2018.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "leagues";
    private static final String TABLE_UNIVERSES = "universe";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_CSV = "csv";
    private static final String KEY_HTML = "html";
    private static final String KEY_DEFAULT = "default_universe";
    private static final String KEY_DEFAULT_LEAGUE = "default_league";
    private static final String TAG = "DB_HANDLER";
    SimpleDateFormat sqliteSdf = new SimpleDateFormat("yyyy/MM/dd");
    private static Map<String, String> formulas = new HashMap<String, String>();
    private static Map<String, String> statsDisplay = new HashMap<String, String>();
    private static Map<String, String> statsSort = new HashMap<String, String>();
    private static Map<String, String> statsQualifier = new HashMap<String, String>();
    private static Map<String, String> statsTable = new HashMap<String, String>();
    private static Map<String, Boolean> statsStripZero = new HashMap<String, Boolean>();
    private static Map<String, Integer> statsRoundTo = new HashMap<String, Integer>();

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        formulas.put("avg","(a.h * 1.0 / a.ab)");
        statsDisplay.put("avg","AVG");
        statsSort.put("avg","desc");
        statsQualifier.put("avg","(a.pa * 1.0 / b.g) >= 3.1");
        statsTable.put("avg","_players_career_batting_stats");
        statsStripZero.put("avg",true);
        statsRoundTo.put("avg",3);

        formulas.put("obp","(((a.h * 1.0) + a.bb + a.hp) / (a.ab + a.bb + a.hp + a.sf))");
        statsDisplay.put("obp","OBP");
        statsSort.put("obp","desc");
        statsQualifier.put("obp","(a.pa * 1.0 / b.g) >= 3.1");
        statsTable.put("obp","_players_career_batting_stats");
        statsStripZero.put("obp",true);
        statsRoundTo.put("obp",3);

        formulas.put("slg","((((a.h*1.0)-a.d-a.t-a.hr)+(a.d*2)+(a.t*3)+(a.hr*4))/a.ab)");
        statsDisplay.put("slg","SLG");
        statsSort.put("slg","desc");
        statsQualifier.put("slg","(a.pa * 1.0 / b.g) >= 3.1");
        statsTable.put("slg","_players_career_batting_stats");
        statsStripZero.put("slg",true);
        statsRoundTo.put("slg",3);

        formulas.put("ops","((((a.h * 1.0) + a.bb + a.hp) / (a.ab + a.bb + a.hp + a.sf)) + ((((a.h*1.0)-a.d-a.t-a.hr)+(a.d*2)+(a.t*3)+(a.hr*4))/a.ab))");
        statsDisplay.put("ops","OPS");
        statsSort.put("ops","desc");
        statsQualifier.put("ops","(a.pa * 1.0 / b.g) >= 3.1");
        statsTable.put("ops","_players_career_batting_stats");
        statsStripZero.put("ops",true);
        statsRoundTo.put("ops",3);

        formulas.put("babip","(((a.h * 1.0) -a.hr) / (a.ab - a.hr -a.k + a.sf))");
        statsDisplay.put("babip","BABIP");
        statsSort.put("babip","desc");
        statsQualifier.put("babip","(a.pa * 1.0 / b.g) >= 3.1");
        statsTable.put("babip","_players_career_batting_stats");
        statsStripZero.put("babip",true);
        statsRoundTo.put("babip",3);

        formulas.put("era","(((a.er * 1.0) * 9) / ((a.outs)/3))");
        statsDisplay.put("era","ERA");
        statsSort.put("era","asc");
        statsQualifier.put("era","((a.outs * 1.0)/3) >= b.g");
        statsTable.put("era","_players_career_pitching_stats");
        statsStripZero.put("era",false);
        statsRoundTo.put("era",2);

        formulas.put("pct","((a.w * 1.0) / (a.w + a.l))");
        statsDisplay.put("pct","PCT");
        statsSort.put("pct","desc");
        statsQualifier.put("pct","((a.outs * 1.0)/3) >= b.g");
        statsTable.put("pct","_players_career_pitching_stats");
        statsStripZero.put("pct",true);
        statsRoundTo.put("pct",3);

        formulas.put("bb9","(((a.bb * 1.0) * 9) / ((a.outs)/3))");
        statsDisplay.put("bb9","BB/9");
        statsSort.put("bb9","asc");
        statsQualifier.put("bb9","((a.outs * 1.0)/3) >= b.g");
        statsTable.put("bb9","_players_career_pitching_stats");
        statsStripZero.put("bb9",false);
        statsRoundTo.put("bb9",1);

        formulas.put("k9","(((a.k * 1.0) * 9) / ((a.outs)/3))");
        statsDisplay.put("k9","K/9");
        statsSort.put("k9","desc");
        statsQualifier.put("k9","((a.outs * 1.0)/3) >= b.g");
        statsTable.put("k9","_players_career_pitching_stats");
        statsStripZero.put("k9",false);
        statsRoundTo.put("k9",1);

        formulas.put("whip","(((a.bb * 1.0) + a.ha) / ((a.outs)/3))");
        statsDisplay.put("whip","WHIP");
        statsSort.put("whip","asc");
        statsQualifier.put("whip","((a.outs * 1.0)/3) >= b.g");
        statsTable.put("whip","_players_career_pitching_stats");
        statsStripZero.put("whip",false);
        statsRoundTo.put("whip",2);

        formulas.put("oavg","((a.ha * 1.0) / a.ab)");
        statsDisplay.put("oavg","AVG");
        statsSort.put("oavg","asc");
        statsQualifier.put("oavg","((a.outs * 1.0)/3) >= b.g");
        statsTable.put("oavg","_players_career_pitching_stats");
        statsStripZero.put("oavg",true);
        statsRoundTo.put("oavg",3);

        formulas.put("oobp","(((a.ha * 1.0) + a.bb + a.hp) / (a.ab + a.bb + a.hp + a.sf))");
        statsDisplay.put("oobp","OBP");
        statsSort.put("oobp","asc");
        statsQualifier.put("oobp","((a.outs * 1.0)/3) >= b.g");
        statsTable.put("oobp","_players_career_pitching_stats");
        statsStripZero.put("oobp",true);
        statsRoundTo.put("oobp",3);

        formulas.put("oslg","((((a.ha*1.0)-a.da-a.ta-a.hra)+(a.da*2)+(a.ta*3)+(a.hra*4))/a.ab)");
        statsDisplay.put("oslg","SLG");
        statsSort.put("oslg","asc");
        statsQualifier.put("oslg","((a.outs * 1.0)/3) >= b.g");
        statsTable.put("oslg","_players_career_pitching_stats");
        statsStripZero.put("oslg",true);
        statsRoundTo.put("oslg",3);

        formulas.put("oops","(((a.ha * 1.0) + a.bb + a.hp) / (a.ab + a.bb + a.hp + a.sf)) + ((((a.ha*1.0)-a.da-a.ta-a.hra)+(a.da*2)+(a.ta*3)+(a.hra*4))/a.ab)");
        statsDisplay.put("oops","OPS");
        statsSort.put("oops","asc");
        statsQualifier.put("oops","((a.outs * 1.0)/3) >= b.g");
        statsTable.put("oops","_players_career_pitching_stats");
        statsStripZero.put("oops",true);
        statsRoundTo.put("oops",3);

        formulas.put("obabip","(((a.ha * 1.0) -a.hra) / (a.ab - a.hra -a.k + a.sf))");
        statsDisplay.put("obabip","BABIP");
        statsSort.put("obabip","asc");
        statsQualifier.put("obabip","((a.outs * 1.0)/3) >= b.g");
        statsTable.put("obabip","_players_career_pitching_stats");
        statsStripZero.put("obabip",true);
        statsRoundTo.put("obabip",3);

        formulas.put("kbb","((a.k * 1.0) / a.bb)");
        statsDisplay.put("kbb","K/BB");
        statsSort.put("kbb","desc");
        statsQualifier.put("kbb","((a.outs * 1.0)/3) >= b.g");
        statsTable.put("kbb","_players_career_pitching_stats");
        statsStripZero.put("kbb",false);
        statsRoundTo.put("kbb",1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LEAGUES_TABLE = "CREATE TABLE " + TABLE_UNIVERSES + " ("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_NAME + " TEXT, " + KEY_CSV + " TEXT, "
                + KEY_HTML + " TEXT, " + KEY_DEFAULT + " INTEGER DEFAULT 0, "
                + KEY_DEFAULT_LEAGUE + " INTEGER)";
        db.execSQL(CREATE_LEAGUES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_UNIVERSES);
        onCreate(db);
    }

    public long addUniverseToList(Universe universe) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, universe.getName());
        values.put(KEY_CSV, universe.getCSV());
        values.put(KEY_HTML, universe.getHTML());
        values.put(KEY_DEFAULT, (universe.getDefaultUniverse()) ? 1 : 0);
        values.put(KEY_DEFAULT_LEAGUE, universe.getDefaultLeague());

        long foo = db.insert(TABLE_UNIVERSES, null, values);

        if (universe.getDefaultUniverse()) {
            values.clear();
            values.put(KEY_DEFAULT, 0);
            db.update(TABLE_UNIVERSES, values, KEY_ID + " <> ?",
                    new String[] {String.valueOf(foo)});
        }

        return foo;
    }

    public void editUniverse(int id, String name, String csv, String html, boolean defaultUniverse,
                             int defaultLeague, String originalName, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();

        SQLiteStatement stmt = db.compileStatement("UPDATE universe SET name = ?, csv = ?, html = ?, default_universe = ?,\n" +
                "default_league = ? where id = ?");
        stmt.bindString(1, name);
        stmt.bindString(2, csv);
        stmt.bindString(3, html);
        stmt.bindLong(4, (defaultUniverse ? 1 : 0));
        stmt.bindLong(5, defaultLeague);
        stmt.bindLong(6, id);
        stmt.execute();

        if (defaultUniverse) {
            stmt.clearBindings();
            stmt = db.compileStatement("UPDATE universe SET default_universe = 0 WHERE id <> ?");
            stmt.bindLong(1, id);
            stmt.execute();
        }

        if (!name.equals(originalName)) {
            JSONObject json = getJsonFromResource(context);
            Iterator<String> i = json.keys();
            while (i.hasNext()) {
                String key = i.next();
                db.execSQL("CREATE TABLE " + name + "_" + key + " AS SELECT * FROM " + originalName + "_" + key);
                db.execSQL("DROP TABLE IF EXISTS " + originalName + "_" + key);
            }
        }
    }

    public List<String> getOtherUniverseNames(String currentUniverse) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        List<String> universes = new ArrayList<String>();
        try {
            String sql = "select name from universe where name <> \""+currentUniverse+"\"";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    universes.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return universes;
    }

    public boolean isThereADefaultUniverse() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        boolean isThere = false;
        try {
            String sql = "select count(*) from universe where default_universe = 1";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                isThere = cursor.getInt(0) == 1 ? true : false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isThere;
    }

    public List<Universe> getAllAppUniverses() {
        List<Universe> universeList = new ArrayList<Universe>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+TABLE_UNIVERSES+"'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                Cursor cursor2 = db.rawQuery("SELECT * FROM " + TABLE_UNIVERSES + " ORDER BY LOWER(" + KEY_NAME  + ") ASC", null);

                if (cursor2.moveToFirst()) {
                    do {
                        Universe universe = new Universe();
                        universe.setID(Integer.parseInt(cursor2.getString(0)));
                        universe.setName(cursor2.getString(1));
                        universe.setCSV(cursor2.getString(2));
                        universe.setHTML(cursor2.getString(3));
                        universe.setDefaultUniverse(cursor2.getInt(4) == 1 ? true : false);
                        universe.setDefaultLeague(cursor2.getInt(5));
                        universeList.add(universe);
                    } while (cursor2.moveToNext());
                }
                cursor2.close();
            }
            cursor.close();
        }
        
        return universeList;
    }

    public Universe getAppUniverse(int universe_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql;
        Cursor cursor;
        Universe universe = new Universe();
        try {
            sql = "select * from " + TABLE_UNIVERSES + " where id = "+universe_id;
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                universe.setID(cursor.getInt(0));
                universe.setName(cursor.getString(1));
                universe.setCSV(cursor.getString(2));
                universe.setHTML(cursor.getString(3));
                universe.setDefaultUniverse(cursor.getInt(4) == 1 ? true : false);
                universe.setDefaultLeague(cursor.getInt(5));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return universe;
    }

    public Universe getDefaultAppUniverse() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql;
        Cursor cursor;
        Universe universe = new Universe();
        try {
            sql = "select * from " + TABLE_UNIVERSES + " where default_universe = 1";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                universe.setID(cursor.getInt(0));
                universe.setName(cursor.getString(1));
                universe.setCSV(cursor.getString(2));
                universe.setHTML(cursor.getString(3));
                universe.setDefaultLeague(cursor.getInt(5));
            } else {
                universe = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return universe;
    }

    public long setDefaultAppUniverse(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DEFAULT, 1);
        long foo = db.update(TABLE_UNIVERSES, values, KEY_ID + " = ?",
                new String[] {String.valueOf(id)});
        return foo;
    }

    public List<UniverseLeague> getUniverseLeagues(Universe universe) {
        List<UniverseLeague> universeLeagues = new ArrayList<UniverseLeague>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            String sql = "select league_id, name from "+universe.getName()+"_leagues order by name asc";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    UniverseLeague uL = new UniverseLeague();
                    uL.setLeagueID(cursor.getInt(0));
                    uL.setLeagueName(cursor.getString(1));
                    universeLeagues.add(uL);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            Log.e("DB", e.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return universeLeagues;
    }

    public int getUniverseCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        int universeCount = 0;
        try {
            String sql = "select count(*) from "+TABLE_UNIVERSES;
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                universeCount = cursor.getInt(0);
            }
        } catch (SQLException e) {
            Log.e("DB", e.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return universeCount;
    }

    public int getCount() {
        int count = 0;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+TABLE_UNIVERSES+"'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                String sql = "SELECT COUNT(*) FROM " + TABLE_UNIVERSES;
                Cursor cursor2 = db.rawQuery(sql, null);
                if (cursor2.moveToFirst()) {
                    do {
                        count = (Integer.parseInt(cursor2.getString(0)));
                    } while (cursor2.moveToNext());
                }
                cursor2.close();
            }
            cursor.close();
        }
        
        return count;
    }

    public int updateLeagueInList(League league) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        int foo;
        values.put(KEY_NAME, league.getName());
        values.put(KEY_CSV, league.getCSV());
        values.put(KEY_HTML, league.getHTML());
        values.put(KEY_DEFAULT, league.getDefaultLeague());
        foo = db.update(TABLE_UNIVERSES, values, KEY_ID + " = ?",
                new String[] {String.valueOf(league.getID())});
        if (league.getDefaultLeague()) {
            values.clear();
            values.put(KEY_DEFAULT, 0);
            foo = db.update(TABLE_UNIVERSES, values, KEY_ID + " <> ?",
                    new String[] {String.valueOf(league.getID())});
        }
        
        return foo;
    }

    public int deleteLeague(League league) {
        SQLiteDatabase db = this.getWritableDatabase();
        int foo = db.delete(TABLE_UNIVERSES, KEY_ID + " = ?",
                new String[] {String.valueOf(league.getID())});
        
        return foo;
    }

    public int deleteUniverse(Universe universe, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();
        int foo = db.delete(TABLE_UNIVERSES, KEY_ID + " = ?",
                new String[] {String.valueOf(universe.getID())});
        
        JSONObject json = getJsonFromResource(context);
        Iterator<String> i = json.keys();
        while (i.hasNext()) {
            String key = i.next();
            db.execSQL("DROP TABLE IF EXISTS " + universe.getName() + "_" + key);
        }
        return foo;
    }

    public int truncateTable(Universe universe, String table) {
        SQLiteDatabase db = this.getWritableDatabase();
        int foo = 0;
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+universe.getName()+"_"+table+"'", null);
        if(cursor!=null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                foo = db.delete(universe.getName() + "_" + table, null, null);
            }
            cursor.close();
        }
        
        return foo;
    }

    public boolean verifyUniverseTableExists(Universe universe) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+universe.getName()+"_leagues'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        
        return false;

    }

    public Date getUniverseDate(Universe universe) {
        SQLiteDatabase db = this.getWritableDatabase();
        Date date = null;
        Cursor cursor = null;
        try {
            date = sqliteSdf.parse("1000/1/1");
            String sql = "SELECT \"current_date\" FROM " + universe.getName() + "_leagues ORDER BY league_id LIMIT 1";
            cursor = db.rawQuery(sql, null);

            if (cursor.moveToFirst()) {
                do {
                    date = sqliteSdf.parse((cursor.getString(0)));
                } while (cursor.moveToNext());
            }
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            Log.e("DB", e.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return date;
    }

    public LeagueInfo getSpecificLeague(String universe_name, String html_root, int league_id) {
        LeagueInfo league = new LeagueInfo();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            String sql = "select a.league_id, a.name, a.abbr, a.logo_file_name, a.background_color_id, \n" +
                    "a.text_color_id, a.league_level, a.\"current_date\", b.num_wild_cards\n" +
                    "from "+universe_name+"_leagues a\n" +
                    "inner join "+universe_name+"_league_playoffs b\n" +
                    "on a.league_id = b.league_id\n" +
                    "where a.league_id = "+league_id+"\n" +
                    "order by a.league_id asc";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    league.setId(cursor.getInt(0));
                    league.setName(cursor.getString(1));
                    league.setAbbr(cursor.getString(2));
                    league.setLogo(cursor.getString(3));
                    league.setBackgroundColor(cursor.getString(4));
                    league.setTextColor(cursor.getString(5));
                    league.setLeagueLevel(cursor.getInt(6));
                    league.setCurrentDate(cursor.getString(7));
                    league.setWildcards(cursor.getInt(8));
                    league.setHtml_root(html_root);;
                } while (cursor.moveToNext());
                return league;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return league;
    }

    public List<LeagueInfo> getAllDBLeagues(String universe_name, String html_root) {
        List<LeagueInfo> leagueList = new ArrayList<LeagueInfo>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            String sql = "select a.league_id, a.name, a.abbr, a.logo_file_name, a.background_color_id, \n" +
                    "a.text_color_id, a.league_level, a.\"current_date\", b.num_wild_cards, c.id\n" +
                    "from "+universe_name+"_leagues a\n" +
                    "left join "+universe_name+"_league_playoffs b\n" +
                    "on a.league_id = b.league_id\n" +
                    "left join "+TABLE_UNIVERSES+" c\n"+
                    "on c.name = \""+universe_name+"\"\n" +
                    "order by a.league_level, a.name";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    LeagueInfo leagueInfo = new LeagueInfo();
                    leagueInfo.setId(cursor.getInt(0));
                    leagueInfo.setName(cursor.getString(1));
                    leagueInfo.setAbbr(cursor.getString(2));
                    leagueInfo.setLogo(cursor.getString(3));
                    leagueInfo.setBackgroundColor(cursor.getString(4));
                    leagueInfo.setTextColor(cursor.getString(5));
                    leagueInfo.setLeagueLevel(cursor.getInt(6));
                    leagueInfo.setCurrentDate(cursor.getString(7));
                    leagueInfo.setWildcards(cursor.getInt(8));
                    leagueInfo.setHtml_root(html_root);
                    leagueInfo.setUniverseID(cursor.getInt(9));
                    leagueList.add(leagueInfo);
                } while (cursor.moveToNext());
                return leagueList;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return leagueList;
    }

    public List<ShortPlayer> getAllPlayers(String universe_name, int league_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<ShortPlayer> playerList = new ArrayList<ShortPlayer>();
        Cursor cursor = null;
        try {
            String sql = "select a.player_id, a.first_name, a.last_name, b.abbr\n" +
                    "from "+universe_name+"_players a\n" +
                    "inner join "+universe_name+"_teams b\n" +
                    "on a.team_id = b.team_id\n" +
                    "where a.league_id = "+league_id+"\n" +
                    "and a.player_id in\n" +
                    "(select distinct player_id from "+universe_name+"_team_roster)\n" +
                    "order by a.last_name COLLATE UNICODE, a.first_name COLLATE UNICODE, b.abbr";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    ShortPlayer player = new ShortPlayer();
                    player.setId(cursor.getInt(0));
                    player.setFirstName(cursor.getString(1));
                    player.setLastName(cursor.getString(2));
                    player.setTeamAbbr(cursor.getString(3));
                    playerList.add(player);
                } while (cursor.moveToNext());
            }
            return playerList;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return playerList;
    }

    public List<ShortPlayer> getAllRetiredPlayers(String universe_name, int league_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<ShortPlayer> playerList = new ArrayList<ShortPlayer>();
        Cursor cursor = null;
        try {
            String sql = "select a.player_id, a.first_name, a.last_name\n" +
                    "from "+universe_name+"_players a\n" +
                    "where a.retired = 1\n" +
                    "order by a.last_name COLLATE UNICODE, a.first_name COLLATE UNICODE";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    ShortPlayer player = new ShortPlayer();
                    player.setId(cursor.getInt(0));
                    player.setFirstName(cursor.getString(1));
                    player.setLastName(cursor.getString(2));
                    player.setTeamAbbr("");
                    playerList.add(player);
                } while (cursor.moveToNext());
            }
            return playerList;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return playerList;
    }

    public List<Player> getActiveRoster(String universe_name, int team_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Player> roster = new ArrayList<Player>();
        Cursor cursor = null;
        try {
            String sql = "select a.player_id, b.first_name, b.last_name, b.uniform_number, b.position, b.bats, b.throws, \n" +
                    "b.height, b.weight, b.date_of_birth, c.name, c.nickname, c.background_color_id, c.text_color_id, c.logo_file_name,\n" +
                    "d.abbr, d.logo_file_name, e.name, f.abbreviation, g.short_name, g.nation_id, g.demonym, b.local_pop, b.national_pop\n" +
                    "from "+universe_name+"_players b\n" +
                    "inner join "+universe_name+"_team_roster a\n" +
                    "on a.player_id = b.player_id\n" +
                    "inner join "+universe_name+"_teams c\n" +
                    "on b.team_id = c.team_id\n" +
                    "inner join "+universe_name+"_leagues d\n" +
                    "on b.league_id = d.league_id\n" +
                    "inner join "+universe_name+"_cities e\n" +
                    "on b.city_of_birth_id = e.city_id\n" +
                    "inner join "+universe_name+"_states f\n" +
                    "on e.state_id = f.state_id\n" +
                    "inner join "+universe_name+"_nations g\n" +
                    "on e.nation_id = g.nation_id\n" +
                    "where b.team_id = "+team_id+"\n" +
                    "and a.list_id = 1\n" +
                    "and a.player_id not in\n" +
                    "(select player_id from "+universe_name+"_team_roster where team_id = "+team_id+" and list_id = 4)\n" +
                    "group by b.last_name, b.first_name";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    Player player = new Player();
                    player.setId(cursor.getInt(0));
                    player.setFirstName(cursor.getString(1));
                    player.setLastName(cursor.getString(2));
                    player.setUniformNumber(cursor.getInt(3));
                    player.setPosition(cursor.getInt(4));
                    player.setPlayerBats(cursor.getInt(5));
                    player.setPlayerThrows(cursor.getInt(6));
                    player.setHeight(cursor.getInt(7));
                    player.setWeight(cursor.getInt(8));
                    player.setBirthday(cursor.getString(9));
                    player.setTeamName(cursor.getString(10));
                    player.setTeamNickname(cursor.getString(11));
                    player.setBackgroundColor(cursor.getString(12));
                    player.setTextColor(cursor.getString(13));
                    player.setTeamLogo(cursor.getString(14));
                    player.setAbbr(cursor.getString(15));
                    player.setLeagueLogo(cursor.getString(16));
                    player.setBirthCity(cursor.getString(17));
                    player.setBirthState(cursor.getString(18));
                    player.setBirthCountry(cursor.getString(19));
                    player.setBirthCountryId(cursor.getInt(20));
                    player.setNationality(cursor.getString(21));
                    player.setLocalPopularity(cursor.getInt(22));
                    player.setNationalPopularity(cursor.getInt(23));
                    roster.add(player);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return roster;
    }

    public List<Player> getActiveRoster(String universe_name, int team_id, int start_pos, int end_pos) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Player> roster = new ArrayList<Player>();
        Cursor cursor = null;
        try {
            String sql = "select a.player_id, b.first_name, b.last_name, b.uniform_number, b.position, b.bats, b.throws, \n" +
                    "b.height, b.weight, b.date_of_birth, c.name, c.nickname, c.background_color_id, c.text_color_id, c.logo_file_name,\n" +
                    "d.abbr, d.logo_file_name, e.name, f.abbreviation, g.short_name, g.nation_id, g.demonym, b.local_pop, b.national_pop\n" +
                    "from "+universe_name+"_players b\n" +
                    "inner join "+universe_name+"_team_roster a\n" +
                    "on a.player_id = b.player_id\n" +
                    "inner join "+universe_name+"_teams c\n" +
                    "on b.team_id = c.team_id\n" +
                    "inner join "+universe_name+"_leagues d\n" +
                    "on b.league_id = d.league_id\n" +
                    "inner join "+universe_name+"_cities e\n" +
                    "on b.city_of_birth_id = e.city_id\n" +
                    "inner join "+universe_name+"_states f\n" +
                    "on e.state_id = f.state_id\n" +
                    "inner join "+universe_name+"_nations g\n" +
                    "on e.nation_id = g.nation_id\n" +
                    "where b.team_id = "+team_id+"\n" +
                    "and a.list_id = 1\n" +
                    "and a.player_id not in\n" +
                    "(select player_id from "+universe_name+"_team_roster where team_id = "+team_id+" and list_id = 4)\n" +
                    "and b.position >= " + start_pos + " and b.position <= " + end_pos +"\n" +
                    "group by b.last_name, b.first_name";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    Player player = new Player();
                    player.setId(cursor.getInt(0));
                    player.setFirstName(cursor.getString(1));
                    player.setLastName(cursor.getString(2));
                    player.setUniformNumber(cursor.getInt(3));
                    player.setPosition(cursor.getInt(4));
                    player.setPlayerBats(cursor.getInt(5));
                    player.setPlayerThrows(cursor.getInt(6));
                    player.setHeight(cursor.getInt(7));
                    player.setWeight(cursor.getInt(8));
                    player.setBirthday(cursor.getString(9));
                    player.setTeamName(cursor.getString(10));
                    player.setTeamNickname(cursor.getString(11));
                    player.setBackgroundColor(cursor.getString(12));
                    player.setTextColor(cursor.getString(13));
                    player.setTeamLogo(cursor.getString(14));
                    player.setAbbr(cursor.getString(15));
                    player.setLeagueLogo(cursor.getString(16));
                    player.setBirthCity(cursor.getString(17));
                    player.setBirthState(cursor.getString(18));
                    player.setBirthCountry(cursor.getString(19));
                    player.setBirthCountryId(cursor.getInt(20));
                    player.setNationality(cursor.getString(21));
                    player.setLocalPopularity(cursor.getInt(22));
                    player.setNationalPopularity(cursor.getInt(23));
                    roster.add(player);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return roster;
    }

    public List<Player> getDisabledList(String universe_name, int team_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Player> roster = new ArrayList<Player>();
        Cursor cursor = null;
        try {
            String sql = "select a.player_id, b.first_name, b.last_name, b.uniform_number, b.position, b.bats, b.throws, \n" +
                    "b.height, b.weight, b.date_of_birth, c.name, c.nickname, c.background_color_id, c.text_color_id, c.logo_file_name,\n" +
                    "d.abbr, d.logo_file_name, e.name, f.abbreviation, g.short_name, g.nation_id, g.demonym, b.local_pop, b.national_pop\n" +
                    "from "+universe_name+"_players b\n" +
                    "inner join "+universe_name+"_team_roster a\n" +
                    "on a.player_id = b.player_id\n" +
                    "inner join "+universe_name+"_teams c\n" +
                    "on b.team_id = c.team_id\n" +
                    "inner join "+universe_name+"_leagues d\n" +
                    "on b.league_id = d.league_id\n" +
                    "inner join "+universe_name+"_cities e\n" +
                    "on b.city_of_birth_id = e.city_id\n" +
                    "inner join "+universe_name+"_states f\n" +
                    "on e.state_id = f.state_id\n" +
                    "inner join "+universe_name+"_nations g\n" +
                    "on e.nation_id = g.nation_id\n" +
                    "where b.team_id = "+team_id+"\n" +
                    "and a.list_id = 4\n" +
                    "group by b.last_name, b.first_name";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    Player player = new Player();
                    player.setId(cursor.getInt(0));
                    player.setFirstName(cursor.getString(1));
                    player.setLastName(cursor.getString(2));
                    player.setUniformNumber(cursor.getInt(3));
                    player.setPosition(cursor.getInt(4));
                    player.setPlayerBats(cursor.getInt(5));
                    player.setPlayerThrows(cursor.getInt(6));
                    player.setHeight(cursor.getInt(7));
                    player.setWeight(cursor.getInt(8));
                    player.setBirthday(cursor.getString(9));
                    player.setTeamName(cursor.getString(10));
                    player.setTeamNickname(cursor.getString(11));
                    player.setBackgroundColor(cursor.getString(12));
                    player.setTextColor(cursor.getString(13));
                    player.setTeamLogo(cursor.getString(14));
                    player.setAbbr(cursor.getString(15));
                    player.setLeagueLogo(cursor.getString(16));
                    player.setBirthCity(cursor.getString(17));
                    player.setBirthState(cursor.getString(18));
                    player.setBirthCountry(cursor.getString(19));
                    player.setBirthCountryId(cursor.getInt(20));
                    player.setNationality(cursor.getString(21));
                    player.setLocalPopularity(cursor.getInt(22));
                    player.setNationalPopularity(cursor.getInt(23));
                    roster.add(player);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return roster;
    }

    public Player getPlayerInfo(Integer player_id, String universe_name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Player player = new Player();
        Cursor cursor = null;
        try {
            /*String sql = "select a.player_id, a.first_name, a.last_name, a.uniform_number, a.position, a.bats, a.throws,\n" +
                    "a.height, a.weight, a.date_of_birth,\n"+"" +
                    "coalesce(b.name, 'Retired'), coalesce(b.nickname, ''),\n" +
                    "coalesce(b.background_color_id,'#333333'), coalesce(b.text_color_id,'#FFFFFF'),\n" +
                    "coalesce(b.logo_file_name, ''), c.abbr, coalesce(c.logo_file_name,''), d.name,\n" +
                    "e.abbreviation, f.short_name, f.nation_id, f.demonym, a.local_pop, a.national_pop, a.retired\n" +
                    "from "+universe_name+"_players a\n" +
                    "left join "+universe_name+"_teams b\n" +
                    "on a.team_id = b.team_id\n" +
                    "left join "+universe_name+"_leagues c\n" +
                    "on a.league_id = c.league_id\n" +
                    "left join "+universe_name+"_cities d\n" +
                    "on a.city_of_birth_id = d.city_id\n" +
                    "left join "+universe_name+"_states e\n" +
                    "on d.state_id = e.state_id\n" +
                    "left join "+universe_name+"_nations f\n" +
                    "on d.nation_id = f.nation_id\n" +
                    "where player_id = " + player_id + "\n" +
                    "order by a.last_name, a.first_name";*/
            String sql = "select a.player_id, a.first_name, a.last_name, a.uniform_number, a.position, a.bats, a.throws,\n" +
                    "a.height, a.weight, a.date_of_birth,\n" +
                    "coalesce(b.name, 'Retired'), coalesce(b.nickname, ''),\n" +
                    "coalesce(b.background_color_id,'#333333'), coalesce(b.text_color_id,'#FFFFFF'),\n" +
                    "coalesce(b.logo_file_name, ''), c.abbr, coalesce(c.logo_file_name,''), d.name,\n" +
                    "e.abbreviation, f.short_name, f.nation_id, f.demonym, a.local_pop, a.national_pop, a.retired,\n" +
                    "coalesce(g.is_major, 0)\n" +
                    "from "+universe_name+"_players a\n" +
                    "left join "+universe_name+"_teams b\n" +
                    "on a.team_id = b.team_id\n" +
                    "left join "+universe_name+"_leagues c\n" +
                    "on a.league_id = c.league_id\n" +
                    "left join "+universe_name+"_cities d\n" +
                    "on a.city_of_birth_id = d.city_id\n" +
                    "left join "+universe_name+"_states e\n" +
                    "on d.state_id = e.state_id\n" +
                    "left join "+universe_name+"_nations f\n" +
                    "on d.nation_id = f.nation_id\n" +
                    "left join "+universe_name+"_players_contract g on \n" +
                    "a.player_id = g.player_id\n" +
                    "where a.player_id = "+player_id+"\n" +
                    "order by a.last_name, a.first_name";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    player = new Player();
                    player.setId(cursor.getInt(0));
                    player.setFirstName(cursor.getString(1));
                    player.setLastName(cursor.getString(2));
                    player.setUniformNumber(cursor.getInt(3));
                    player.setPosition(cursor.getInt(4));
                    player.setPlayerBats(cursor.getInt(5));
                    player.setPlayerThrows(cursor.getInt(6));
                    player.setHeight(cursor.getInt(7));
                    player.setWeight(cursor.getInt(8));
                    player.setBirthday(cursor.getString(9));
                    player.setTeamName(cursor.getString(10));
                    player.setTeamNickname(cursor.getString(11));
                    player.setBackgroundColor(cursor.getString(12));
                    player.setTextColor(cursor.getString(13));
                    player.setTeamLogo(cursor.getString(14));
                    player.setAbbr(cursor.getString(15));
                    player.setLeagueLogo(cursor.getString(16));
                    player.setBirthCity(cursor.getString(17));
                    player.setBirthState(cursor.getString(18));
                    player.setBirthCountry(cursor.getString(19));
                    player.setBirthCountryId(cursor.getInt(20));
                    player.setNationality(cursor.getString(21));
                    player.setLocalPopularity(cursor.getInt(22));
                    player.setNationalPopularity(cursor.getInt(23));
                    player.setRetired(cursor.getInt(24));
                    player.setContractType(cursor.getInt(25));
                } while (cursor.moveToNext());
                return player;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return player;
    }

    public Team getTeamInfo(String universe_name, int team_id, int league_id){
        SQLiteDatabase db = this.getReadableDatabase();
        Team team = new Team();
        Cursor cursor = null;
        try {
            String sql = "select a.team_id, a.name, a.nickname, a.abbr, a.logo_file_name, a.level,\n" +
                    "a.background_color_id, a.text_color_id,\n" +
                    "b.w, b.l, b.pos, b.pct, b.gb,\n" +
                    "c.name, c.abbr,\n" +
                    "d.name, d.abbr,\n" +
                    "e.name, f.first_name, f.last_name, coalesce(g.team_id, -1) as parent_id,\n" +
                    "coalesce(h.logo_file_name, '') as parent_logo\n" +
                    "from "+universe_name+"_teams a\n" +
                    "inner join "+universe_name+"_team_record b\n" +
                    "on a.team_id = b.team_id\n" +
                    "inner join "+universe_name+"_leagues c\n" +
                    "on a.league_id = c.league_id\n" +
                    "inner join "+universe_name+"_sub_leagues d\n" +
                    "on a.sub_league_id = d.sub_league_id and a.league_id = d.league_id\n" +
                    "inner join "+universe_name+"_divisions e\n" +
                    "on a.division_id = e.division_id and a.league_id = e.league_id and a.sub_league_id = e.sub_league_id\n" +
                    "left join "+universe_name+"_human_managers f\n" +
                    "on a.human_id = f.human_manager_id\n" +
                    "left join "+universe_name+"_team_affiliations g\n" +
                    "on a.team_id = g.affiliated_team_id\n" +
                    "left join "+universe_name+"_teams h\n" +
                    "on parent_id = h.team_id\n" +
                    "where a.team_id = " + team_id + "\n" +
                    "and a.league_id = " + league_id + "\n" +
                    "order by a.level, a.name";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    team = new Team();
                    team.setId(cursor.getInt(0));
                    team.setTeamName(cursor.getString(1));
                    team.setTeamNickname(cursor.getString(2));
                    team.setTeamAbbr(cursor.getString(3));
                    team.setTeamLogo(cursor.getString(4));
                    team.setLevel(cursor.getInt(5));
                    team.setBackgroundColor(cursor.getString(6));
                    team.setTextColor(cursor.getString(7));
                    team.setW(cursor.getInt(8));
                    team.setL(cursor.getInt(9));
                    team.setPos(cursor.getInt(10));
                    team.setPct((cursor.getFloat(11)));
                    team.setGb(cursor.getFloat(12));
                    team.setLeagueName(cursor.getString(13));
                    team.setLeagueAbbr(cursor.getString(14));
                    team.setSubLeagueName(cursor.getString(15));
                    team.setSubLeagueAbbr(cursor.getString(16));
                    team.setDivisionName(cursor.getString(17));
                    team.setManagerFirstName(cursor.getString(18));
                    team.setManagerLastName(cursor.getString(19));
                    team.setParentId(cursor.getInt(20));
                    team.setParentLogo(cursor.getString(21));
                } while (cursor.moveToNext());
                return team;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return team;
    }

    public List<Team> getAllTeams(String universe_name, int team_id, int league_id){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Team> teams = new ArrayList<Team>();
        Cursor cursor = null;
        try {
            String sql = "select a.team_id, a.name, a.nickname, a.logo_file_name, a.level,\n" +
                    "a.background_color_id, a.text_color_id, b.w, b.l, b.pos, b.pct, \n" +
                    "b.gb, c.name, c.abbr, c.league_id, d.name, d.abbr, d.sub_league_id,\n" +
                    "e.name, COALESCE(f.first_name,'Computer')  AS first_name,\n" +
                    "COALESCE(f.last_name,'Manager') AS last_name\n" +
                    "from "+universe_name+"_teams a\n" +
                    "inner join "+universe_name+"_team_record b\n" +
                    "on a.team_id = b.team_id\n" +
                    "inner join "+universe_name+"_leagues c\n" +
                    "on a.league_id = c.league_id\n" +
                    "inner join "+universe_name+"_sub_leagues d\n" +
                    "on a.sub_league_id = d.sub_league_id and a.league_id = d.league_id\n" +
                    "inner join "+universe_name+"_divisions e\n" +
                    "on a.division_id = e.division_id and a.league_id = e.league_id and a.sub_league_id = e.sub_league_id\n" +
                    "left join "+universe_name+"_human_managers f\n" +
                    "on a.human_id = f.human_manager_id\n" +
                    "where a.league_id = "+league_id+" and a.allstar_team = 0\n" +
                    "order by a.level, a.name";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    Team team = new Team();
                    team.setId(cursor.getInt(0));
                    team.setTeamName(cursor.getString(1));
                    team.setTeamNickname(cursor.getString(2));
                    team.setTeamLogo(cursor.getString(3));;
                    team.setLevel(cursor.getInt(4));
                    team.setBackgroundColor(cursor.getString(5));;
                    team.setTextColor(cursor.getString(6));
                    team.setW(cursor.getInt(7));
                    team.setL(cursor.getInt(8));
                    team.setPos(cursor.getInt(9));
                    team.setPct(cursor.getFloat(10));
                    team.setGb(cursor.getFloat(11));
                    team.setLeagueName(cursor.getString(12));
                    team.setLeagueAbbr(cursor.getString(13));
                    team.setLeagueID(cursor.getInt(14));
                    team.setSubLeagueName(cursor.getString(15));
                    team.setSubLeagueAbbr(cursor.getString(16));
                    team.setSubLeagueID(cursor.getInt(17));
                    team.setDivisionName(cursor.getString(18));
                    team.setManagerFirstName(cursor.getString(19));
                    team.setManagerLastName(cursor.getString(20));
                    teams.add(team);
                } while (cursor.moveToNext());
                return teams;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return teams;
    }

    public LeagueInfo getLeagueInfo(int league_id, String html, String universe_name) {
        SQLiteDatabase db = this.getReadableDatabase();
        LeagueInfo leagueInfo = new LeagueInfo();
        int id = league_id;
        String html_root = html;
        Cursor cursor = null;
        try {
            String sql = "select league_id, name, abbr, logo_file_name, background_color_id, text_color_id,\n" +
                    "league_level, \"current_date\" from "+universe_name+"_leagues where league_id = " + id;
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    leagueInfo = new LeagueInfo();
                    leagueInfo.setId(cursor.getInt(0));
                    leagueInfo.setName(cursor.getString(1));
                    leagueInfo.setAbbr(cursor.getString(2));
                    leagueInfo.setLogo(cursor.getString(3));
                    leagueInfo.setBackgroundColor(cursor.getString(4));
                    leagueInfo.setTextColor(cursor.getString(5));
                    leagueInfo.setLeagueLevel(cursor.getInt(6));
                    leagueInfo.setCurrentDate(cursor.getString(7));
                    leagueInfo.setHtml_root(html_root);
                } while (cursor.moveToNext());
                return leagueInfo;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return leagueInfo;
    }

    public void createNewUniverse(Universe universe, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();;
        String CREATE_TABLE = "";
        try {
            JSONObject json = getJsonFromResource(context);
            Iterator<String> i = json.keys();
            while (i.hasNext()) {
                String key = i.next();
                db.execSQL("DROP TABLE IF EXISTS " + universe.getName() + "_" + key);
                CREATE_TABLE = "CREATE TABLE " + universe.getName() + "_" + key + " (";
                JSONObject fields = json.getJSONObject(key);
                Iterator<String> j = fields.keys();
                while (j.hasNext()) {
                    String field = j.next();
                    //String fieldType = (fields.optString(field) == "datetime") ? "integer" : fields.optString(field);
                    CREATE_TABLE += field + " " + fields.optString(field) + ", ";
                    //System.out.println("   " + field + " : " + fields.optString(field));
                }
                CREATE_TABLE = CREATE_TABLE.substring(0, CREATE_TABLE.length() - 2);
                CREATE_TABLE += ")";
                db.execSQL(CREATE_TABLE);
            }
            //
        } catch (SQLiteException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            
        }
    }

    public GameSummary getGameSummary(String universe_name, int game_id, int league_id, String game_date) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Integer> innings;
        List<Integer> awayLine;
        List<Integer> homeLine;
        GameSummary gameSummary = new GameSummary();
        String sql2;
        Cursor cursor = null;
        Cursor cursor2 = null;
        Float era;
        try {
            String sql = "select a.game_id," +
                    " b.team_id as home_id," +
                    " b.name as home_name, " +
                    "b.nickname as home_nickname, " +
                    "b.logo_file_name as home_logo,\n" +
                    "c.team_id as away_id, " +
                    "c.name as away_name, " +
                    "c.nickname as away_nickname, " +
                    "c.logo_file_name as away_logo,\n" +
                    "a.runs1 as home_score, " +
                    "a.runs0 as away_score, " +
                    "a.hits1 as home_hits, " +
                    "a.hits0 as away_hits, " +
                    "a.errors1 as home_errors, " +
                    "a.errors0 as away_errors, " +
                    "a.winning_pitcher, " +
                    "d.last_name as winning_pitcher,\n" +
                    "a.losing_pitcher, " +
                    "e.last_name as losing_pitcher,\n" +
                    "a.save_pitcher, f.last_name as saving_pitcher,\n"+
                    "b.background_color_id as home_bg_color,\n"+
                    "b.text_color_id as home_text_color,\n"+
                    "c.background_color_id as away_bg_color,\n"+
                    "c.text_color_id as away_text_color,\n"+
                    "a.game_type, b.abbr as home_abbr, c.abbr as away_abbr,\n"+
                    "g.name as park, a.attendance as attendance, a.date as date, a.time as time\n"+
                    "from "+universe_name+"_games a\n" +
                    "inner join "+universe_name+"_teams b\n" +
                    "on a.home_team = b.team_id\n" +
                    "inner join "+universe_name+"_teams c\n" +
                    "on a.away_team = c.team_id\n" +
                    "inner join "+universe_name+"_players d\n" +
                    "on a.winning_pitcher = d.player_id\n" +
                    "inner join "+universe_name+"_players e\n" +
                    "on a.losing_pitcher = e.player_id\n" +
                    "left join "+universe_name+"_players f\n" +
                    "on a.save_pitcher = f.player_id\n" +
                    "left join "+universe_name+"_parks g\n" +
                    "on b.park_id = g.park_id\n" +
                    "where a.game_id = "+game_id;
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    gameSummary.setGameId(cursor.getInt(0));
                    gameSummary.setHomeTeamId(cursor.getInt(1));
                    gameSummary.setHomeTeamName(cursor.getString(2));
                    gameSummary.setHomeTeamNickhame(cursor.getString(3));
                    gameSummary.setHomeTeamLogo(cursor.getString(4));
                    gameSummary.setAwayTeamId(cursor.getInt(5));
                    gameSummary.setAwayTeamName(cursor.getString(6));
                    gameSummary.setAwayTeamNickname(cursor.getString(7));
                    gameSummary.setAwayTeamLogo(cursor.getString(8));
                    gameSummary.setHomeScore(cursor.getInt(9));
                    gameSummary.setAwayScore(cursor.getInt(10));
                    gameSummary.setHomeHits(cursor.getInt(11));
                    gameSummary.setAwayHits(cursor.getInt(12));
                    gameSummary.setHomeErrors(cursor.getInt(13));
                    gameSummary.setAwayErrors(cursor.getInt(14));
                    gameSummary.setWinningPitcher(cursor.getInt(15));
                    gameSummary.setWinningPitcherName(cursor.getString(16));
                    gameSummary.setLosingPitcher(cursor.getInt(17));
                    gameSummary.setLosingPitcherName(cursor.getString(18));
                    gameSummary.setSavingPitcher(cursor.getInt(19));
                    gameSummary.setSavingPitcherName(cursor.getString(20));
                    gameSummary.setHomeBGColor(cursor.getString(21));
                    gameSummary.setHomeTextColor(cursor.getString(22));
                    gameSummary.setAwayBGColor(cursor.getString(23));
                    gameSummary.setAwayTextColor(cursor.getString(24));
                    gameSummary.setGameType(cursor.getInt(25));
                    gameSummary.setHomeTeamAbbr(cursor.getString(26));
                    gameSummary.setAwayTeamAbbr(cursor.getString(27));
                    gameSummary.setPark(cursor.getString(28));
                    gameSummary.setAttendance(cursor.getInt(29));
                    gameSummary.setDate(cursor.getString(30));
                    gameSummary.setTime(cursor.getString(31));
                    sql2 = "select sum(w), sum(l) from "+universe_name+"_players_game_pitching_stats where\n" +
                            "team_id = "+cursor.getInt(1)+" and game_id in\n" +
                            "(select game_id from "+universe_name+"_games where game_type = "+cursor.getInt(25)+"\n" +
                            "and date <= \"" + game_date + "\")";
                    cursor2 = db.rawQuery(sql2, null);
                    if (cursor2.moveToFirst()) {
                        do {
                            gameSummary.setHomeRecord("("+cursor2.getInt(0)+"-"+cursor2.getInt(1)+")");
                        } while (cursor2.moveToNext());
                    } cursor2.close();
                    sql2 = "select sum(w), sum(l) from "+universe_name+"_players_game_pitching_stats where\n" +
                            "team_id = "+cursor.getInt(5)+" and game_id in\n" +
                            "(select game_id from "+universe_name+"_games where game_type = "+cursor.getInt(25)+"\n" +
                            "and date <= \"" + game_date + "\")";
                    cursor2 = db.rawQuery(sql2, null);
                    if (cursor2.moveToFirst()) {
                        do {
                            gameSummary.setAwayRecord("("+cursor2.getInt(0)+"-"+cursor2.getInt(1)+")");
                        } while (cursor2.moveToNext());
                    } cursor2.close();
                    sql2 = "select sum(w), sum(l), sum(outs), sum(er)  from "+universe_name+"_players_game_pitching_stats where \n" +
                            "player_id = "+cursor.getInt(15)+" and game_id in\n" +
                            "(select game_id from "+universe_name+"_games where game_type = "+cursor.getInt(25)+" and league_id = "+league_id+" \n" +
                            "and date <= \"" + game_date + "\")";
                    cursor2 = db.rawQuery(sql2, null);
                    if (cursor2.moveToFirst()) {
                        do {
                            era = cursor2.getFloat(3) * 9 / (cursor2.getFloat(2)/3);
                            BigDecimal rounded = new BigDecimal(Float.toString(era));
                            rounded = rounded.setScale(2, RoundingMode.HALF_UP);
                            gameSummary.setWinningPitcherLine("("+cursor2.getInt(0)+"-"+cursor2.getInt(1)+", "+rounded+")");
                        } while (cursor2.moveToNext());
                    } cursor2.close();
                    sql2 = "select sum(w), sum(l), sum(outs), sum(er)  from "+universe_name+"_players_game_pitching_stats where \n" +
                            "player_id = "+cursor.getInt(17)+" and game_id in\n" +
                            "(select game_id from "+universe_name+"_games where game_type = "+cursor.getInt(25)+" and league_id = "+league_id+" \n" +
                            "and date <= \"" + game_date + "\")";
                    cursor2 = db.rawQuery(sql2, null);
                    if (cursor2.moveToFirst()) {
                        do {
                            era = cursor2.getFloat(3) * 9 / (cursor2.getFloat(2)/3);
                            BigDecimal rounded = new BigDecimal(Float.toString(era));
                            rounded = rounded.setScale(2, RoundingMode.HALF_UP);
                            gameSummary.setLosingPitcherLine("("+cursor2.getInt(0)+"-"+cursor2.getInt(1)+", "+rounded+")");
                        } while (cursor2.moveToNext());
                    } cursor2.close();
                    if (cursor.getInt(19) > 0) {
                        sql2 = "select sum(s) from "+universe_name+"_players_game_pitching_stats where \n" +
                                "player_id = "+cursor.getInt(19)+" and game_id in\n" +
                                "(select game_id from "+universe_name+"_games where game_type = "+cursor.getInt(25)+" and league_id = "+league_id+" \n" +
                                "and date <= \"" + game_date + "\")";
                        cursor2 = db.rawQuery(sql2, null);
                        if (cursor2.moveToFirst()) {
                            do {
                                gameSummary.setSavingPitcherLine("("+cursor2.getInt(0)+")");
                            } while (cursor2.moveToNext());
                        } cursor2.close();
                    }
                    sql2 = "select inning, score from "+universe_name+"_games_score where team = 0 and game_id = "+cursor.getInt(0);
                    cursor2 = db.rawQuery(sql2, null);
                    if (cursor2.moveToFirst()) {
                        innings = new ArrayList<Integer>();
                        awayLine = new ArrayList<Integer>();
                        do {
                            innings.add(cursor2.getInt(0));
                            awayLine.add(cursor2.getInt(1));
                        } while (cursor2.moveToNext());
                        gameSummary.setInnings(innings);
                        gameSummary.setAwayLine(awayLine);
                    } cursor2.close();
                    sql2 = "select inning, score from "+universe_name+"_games_score where team = 1 and game_id = "+cursor.getInt(0);
                    cursor2 = db.rawQuery(sql2, null);
                    if (cursor2.moveToFirst()) {
                        homeLine = new ArrayList<Integer>();
                        do {
                            homeLine.add(cursor2.getInt(1));
                        } while (cursor2.moveToNext());
                        gameSummary.setHomeLine(homeLine);
                    } cursor2.close();
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            if (cursor2 != null && !cursor2.isClosed())
                cursor2.close();
            
        }
        return gameSummary;
    }

    public List<GameSummary> getScoreboard(String date, int league, String name, String html) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<GameSummary> scoreboard = new ArrayList<GameSummary>();
        List<Integer> innings;
        List<Integer> awayLine;
        List<Integer> homeLine;
        GameSummary gameSummary;
        String game_date = date;
        int league_id = league;
        String universe_name = name;
        String sql2;
        Cursor cursor = null;
        Cursor cursor2 = null;
        Float era;
        try {
            String sql = "select a.game_id," +
                    " b.team_id as home_id," +
                    " b.name as home_name, " +
                    "b.nickname as home_nickname, " +
                    "b.logo_file_name as home_logo,\n" +
                    "c.team_id as away_id, " +
                    "c.name as away_name, " +
                    "c.nickname as away_nickname, " +
                    "c.logo_file_name as away_logo,\n" +
                    "a.runs1 as home_score, " +
                    "a.runs0 as away_score, " +
                    "a.hits1 as home_hits, " +
                    "a.hits0 as away_hits, " +
                    "a.errors1 as home_errors, " +
                    "a.errors0 as away_errors, " +
                    "a.winning_pitcher, " +
                    "d.last_name as winning_pitcher,\n" +
                    "a.losing_pitcher, " +
                    "e.last_name as losing_pitcher,\n" +
                    "a.save_pitcher, f.last_name as saving_pitcher,\n"+
                    "b.background_color_id as home_bg_color,\n"+
                    "b.text_color_id as home_text_color,\n"+
                    "c.background_color_id as away_bg_color,\n"+
                    "c.text_color_id as away_text_color,\n"+
                    "a.game_type, b.abbr as home_abbr, c.abbr as away_abbr,\n"+
                    "g.name as park, a.attendance as attendance, a.date as date, a.time as time\n"+
                    "from "+universe_name+"_games a\n" +
                    "inner join "+universe_name+"_teams b\n" +
                    "on a.home_team = b.team_id\n" +
                    "inner join "+universe_name+"_teams c\n" +
                    "on a.away_team = c.team_id\n" +
                    "inner join "+universe_name+"_players d\n" +
                    "on a.winning_pitcher = d.player_id\n" +
                    "inner join "+universe_name+"_players e\n" +
                    "on a.losing_pitcher = e.player_id\n" +
                    "left join "+universe_name+"_players f\n" +
                    "on a.save_pitcher = f.player_id\n" +
                    "left join "+universe_name+"_parks g\n" +
                    "on b.park_id = g.park_id\n" +
                    "where a.date = \""+game_date+"\" and a.league_id = "+league_id;
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    gameSummary = new GameSummary();
                    gameSummary.setGameId(cursor.getInt(0));
                    gameSummary.setHomeTeamId(cursor.getInt(1));
                    gameSummary.setHomeTeamName(cursor.getString(2));
                    gameSummary.setHomeTeamNickhame(cursor.getString(3));
                    gameSummary.setHomeTeamLogo(cursor.getString(4));
                    gameSummary.setAwayTeamId(cursor.getInt(5));
                    gameSummary.setAwayTeamName(cursor.getString(6));
                    gameSummary.setAwayTeamNickname(cursor.getString(7));
                    gameSummary.setAwayTeamLogo(cursor.getString(8));
                    gameSummary.setHomeScore(cursor.getInt(9));
                    gameSummary.setAwayScore(cursor.getInt(10));
                    gameSummary.setHomeHits(cursor.getInt(11));
                    gameSummary.setAwayHits(cursor.getInt(12));
                    gameSummary.setHomeErrors(cursor.getInt(13));
                    gameSummary.setAwayErrors(cursor.getInt(14));
                    gameSummary.setWinningPitcher(cursor.getInt(15));
                    gameSummary.setWinningPitcherName(cursor.getString(16));
                    gameSummary.setLosingPitcher(cursor.getInt(17));
                    gameSummary.setLosingPitcherName(cursor.getString(18));
                    gameSummary.setSavingPitcher(cursor.getInt(19));
                    gameSummary.setSavingPitcherName(cursor.getString(20));
                    gameSummary.setHomeBGColor(cursor.getString(21));
                    gameSummary.setHomeTextColor(cursor.getString(22));
                    gameSummary.setAwayBGColor(cursor.getString(23));
                    gameSummary.setAwayTextColor(cursor.getString(24));
                    gameSummary.setGameType(cursor.getInt(25));
                    gameSummary.setHomeTeamAbbr(cursor.getString(26));
                    gameSummary.setAwayTeamAbbr(cursor.getString(27));
                    gameSummary.setPark(cursor.getString(28));
                    gameSummary.setAttendance(cursor.getInt(29));
                    gameSummary.setDate(cursor.getString(30));
                    gameSummary.setTime(cursor.getString(31));
                    sql2 = "select sum(w), sum(l) from "+universe_name+"_players_game_pitching_stats where\n" +
                            "team_id = "+cursor.getInt(1)+" and game_id in\n" +
                            "(select game_id from "+universe_name+"_games where game_type = "+cursor.getInt(25)+"\n" +
                            "and date <= \"" + game_date + "\")";
                    cursor2 = db.rawQuery(sql2, null);
                    if (cursor2.moveToFirst()) {
                        do {
                            gameSummary.setHomeRecord("("+cursor2.getInt(0)+"-"+cursor2.getInt(1)+")");
                        } while (cursor2.moveToNext());
                    } cursor2.close();
                    sql2 = "select sum(w), sum(l) from "+universe_name+"_players_game_pitching_stats where\n" +
                            "team_id = "+cursor.getInt(5)+" and game_id in\n" +
                            "(select game_id from "+universe_name+"_games where game_type = "+cursor.getInt(25)+"\n" +
                            "and date <= \"" + game_date + "\")";
                    cursor2 = db.rawQuery(sql2, null);
                    if (cursor2.moveToFirst()) {
                        do {
                            gameSummary.setAwayRecord("("+cursor2.getInt(0)+"-"+cursor2.getInt(1)+")");
                        } while (cursor2.moveToNext());
                    } cursor2.close();
                    sql2 = "select sum(w), sum(l), sum(outs), sum(er)  from "+universe_name+"_players_game_pitching_stats where \n" +
                            "player_id = "+cursor.getInt(15)+" and game_id in\n" +
                            "(select game_id from "+universe_name+"_games where game_type = "+cursor.getInt(25)+" and league_id = "+league_id+" \n" +
                            "and date <= \"" + game_date + "\")";
                    cursor2 = db.rawQuery(sql2, null);
                    if (cursor2.moveToFirst()) {
                        do {
                            era = cursor2.getFloat(3) * 9 / (cursor2.getFloat(2)/3);
                            BigDecimal rounded = new BigDecimal(Float.toString(era));
                            rounded = rounded.setScale(2, RoundingMode.HALF_UP);
                            gameSummary.setWinningPitcherLine("("+cursor2.getInt(0)+"-"+cursor2.getInt(1)+", "+rounded+")");
                        } while (cursor2.moveToNext());
                    } cursor2.close();
                    sql2 = "select sum(w), sum(l), sum(outs), sum(er)  from "+universe_name+"_players_game_pitching_stats where \n" +
                            "player_id = "+cursor.getInt(17)+" and game_id in\n" +
                            "(select game_id from "+universe_name+"_games where game_type = "+cursor.getInt(25)+" and league_id = "+league_id+" \n" +
                            "and date <= \"" + game_date + "\")";
                    cursor2 = db.rawQuery(sql2, null);
                    if (cursor2.moveToFirst()) {
                        do {
                            era = cursor2.getFloat(3) * 9 / (cursor2.getFloat(2)/3);
                            BigDecimal rounded = new BigDecimal(Float.toString(era));
                            rounded = rounded.setScale(2, RoundingMode.HALF_UP);
                            gameSummary.setLosingPitcherLine("("+cursor2.getInt(0)+"-"+cursor2.getInt(1)+", "+rounded+")");
                        } while (cursor2.moveToNext());
                    } cursor2.close();
                    if (cursor.getInt(19) > 0) {
                        sql2 = "select sum(s) from "+universe_name+"_players_game_pitching_stats where \n" +
                                "player_id = "+cursor.getInt(19)+" and game_id in\n" +
                                "(select game_id from "+universe_name+"_games where game_type = "+cursor.getInt(25)+" and league_id = "+league_id+" \n" +
                                "and date <= \"" + game_date + "\")";
                        cursor2 = db.rawQuery(sql2, null);
                        if (cursor2.moveToFirst()) {
                            do {
                                gameSummary.setSavingPitcherLine("("+cursor2.getInt(0)+")");
                            } while (cursor2.moveToNext());
                        } cursor2.close();
                    }
                    sql2 = "select inning, score from "+universe_name+"_games_score where team = 0 and game_id = "+cursor.getInt(0);
                    cursor2 = db.rawQuery(sql2, null);
                    if (cursor2.moveToFirst()) {
                        innings = new ArrayList<Integer>();
                        awayLine = new ArrayList<Integer>();
                        do {
                            innings.add(cursor2.getInt(0));
                            awayLine.add(cursor2.getInt(1));
                        } while (cursor2.moveToNext());
                        gameSummary.setInnings(innings);
                        gameSummary.setAwayLine(awayLine);
                    } cursor2.close();
                    sql2 = "select inning, score from "+universe_name+"_games_score where team = 1 and game_id = "+cursor.getInt(0);
                    cursor2 = db.rawQuery(sql2, null);
                    if (cursor2.moveToFirst()) {
                        homeLine = new ArrayList<Integer>();
                        do {
                            homeLine.add(cursor2.getInt(1));
                        } while (cursor2.moveToNext());
                        gameSummary.setHomeLine(homeLine);
                    } cursor2.close();
                    scoreboard.add(gameSummary);
                } while (cursor.moveToNext());
                return scoreboard;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            if (cursor2 != null && !cursor2.isClosed())
                cursor2.close();
            
        }
        return scoreboard;
    }

    public String countingBattingStatToDate(String stat, String universe_name, String player_id, int game_type, int league_id, String game_date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        String returnVal = "0";
        try {
            String sql = "select sum("+stat+") from "+universe_name+"_players_game_batting where \n" +
                    "player_id = "+player_id+" and game_id in\n" +
                    "(select game_id from "+universe_name+"_games where game_type = "+game_type+" and league_id = "+league_id+" \n" +
                    "and date <= \""+game_date+"\")";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                returnVal = cursor.getString(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return returnVal;
    }

    public List<BattingLineScore> battingLineScores(String universe_name, int game_id, int team_id, int league_id, int game_type, String game_date) {
        List<BattingLineScore> battingLineScores = new ArrayList<BattingLineScore>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        Cursor cursor2 = null;
        try {
            String sql = "select distinct(a.player_id), c.first_name, c.last_name, a.position, a.pa, \n" +
                    "a.ab, a.r, a.h, a.rbi, a.d, a.t, a.hr, a.sb, a.sh, a.sf, a.bb, a.k,\n" +
                    "coalesce(b.spot, 500) as spot, a.gs, coalesce(b.pinch, 0) as pinch,\n"+
                    "((1.0*a.h-a.d-a.t-a.hr) + (a.d*2) + (a.t*3) + (a.hr*4)) as tb, a.gdp,\n" +
                    "a.hp\n" +
                    "from "+universe_name+"_players_game_batting a\n" +
                    "left join "+universe_name+"_players_at_bat_batting_stats b\n" +
                    "on a.player_id = b.player_id and a.game_id = b.game_id\n" +
                    "inner join "+universe_name+"_players c\n" +
                    "on a.player_id = c.player_id\n" +
                    "where a.game_id = "+game_id+" and a.team_id = "+team_id+"\n" +
                    "union all\n" +
                    "select -1 as player_id, 'TOTAL' as first_name, '' as last_name, 0 as position,\n" +
                    "sum(pa), sum(ab), sum(r), sum(h), sum(rbi), sum(d), sum(t), sum(hr), sum (sb), sum(sh), sum(sf),\n" +
                    "sum(bb), sum(k), 9999 as spot, 9999 as gs, 9999 as pinch, 9999 as tb, 9999 as gdp,\n" +
                    "9999 as hp\n" +
                    "from "+universe_name+"_players_game_batting\n" +
                    "where game_id = "+game_id+" and team_id = "+team_id+"\n" +
                    "order by spot asc, a.gs desc";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    BattingLineScore line = new BattingLineScore();
                    line.setPlayerID(cursor.getString(0));
                    line.setFirstName(cursor.getString(1));
                    line.setLastName(cursor.getString(2));
                    line.setPosition(constants.positions[cursor.getInt(3)]);
                    line.setPa(cursor.getString(4));
                    line.setAb(cursor.getString(5));
                    line.setR(cursor.getString(6));
                    line.setH(cursor.getString(7));
                    line.setRbi(cursor.getString(8));
                    line.setD(cursor.getString(9));
                    line.setT(cursor.getString(10));
                    line.setHr(cursor.getString(11));
                    line.setSb(cursor.getString(12));
                    line.setSh(cursor.getString(13));
                    line.setSf(cursor.getString(14));
                    line.setBb(cursor.getString(15));
                    line.setK(cursor.getString(16));
                    line.setSpot(cursor.getString(17));
                    line.setGs(cursor.getString(18));
                    line.setPinch(cursor.getString(19));
                    line.setTotalBases(cursor.getString(20));
                    line.setGdp(cursor.getString(21));
                    line.setHbp(cursor.getString(22));
                    String sql2 = "select (1.0*sum(h))/sum(ab) from "+universe_name+"_players_game_batting where \n" +
                            "player_id = "+cursor.getString(0)+" and game_id in\n" +
                            "(select game_id from "+universe_name+"_games where game_type = "+game_type+" and league_id = "+league_id+" \n" +
                            "and date <= \""+game_date+"\")";
                    cursor2 = db.rawQuery(sql2, null);
                    if (cursor2.moveToFirst()) {
                        BigDecimal avg = new BigDecimal(Float.toString(cursor2.getFloat(0)));
                        avg = avg.setScale(3, RoundingMode.HALF_UP);
                        line.setAvg(String.valueOf(avg).replaceFirst("^0.", "."));
                    }
                    cursor2.close();
                    battingLineScores.add(line);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            if (cursor2 != null && !cursor2.isClosed())
                cursor2.close();
            
        }
        return battingLineScores;
    }

    public List<PitchingLineScore> pitchingLineScores(String universe_name, int game_id, int team_id, int league_id, int game_type, String game_date) {
        List<PitchingLineScore> pitchingLineScores = new ArrayList<PitchingLineScore>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        Cursor cursor2 = null;
        String sql;
        String sql2;
        try {
            sql = "select distinct(a.player_id), c.first_name, c.last_name, a.w, a.l, a.s,\n" +
                    "((1.0 * a.outs)/3) as ip, a.ha, a.r, a.er, a.bb, a.k, a.pi, a.hra, a.outs, a.gs, a.bf,\n" +
                    "a.ir, a.irs, a.gb, a.fb, a.hp, a.bk\n" +
                    "from "+universe_name+"_players_game_pitching_stats a\n" +
                    "left join "+universe_name+"_players_at_bat_batting_stats b\n" +
                    "on a.player_id = b.opponent_player_id and a.game_id = b.game_id\n" +
                    "inner join "+universe_name+"_players c\n" +
                    "on a.player_id = c.player_id\n" +
                    "where a.game_id = "+game_id+" and a.team_id = "+team_id+"\n" +
                    "order by a.gs desc, a.s asc, b.inning asc";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    PitchingLineScore line = new PitchingLineScore();
                    line.setPlayerID(cursor.getString(0));
                    line.setFirstName(cursor.getString(1));
                    line.setLastName(cursor.getString(2));
                    line.setW(cursor.getString(3));
                    line.setL(cursor.getString(4));
                    line.setS(cursor.getString(5));
                    BigDecimal rounded = new BigDecimal(Float.toString(cursor.getFloat(6)));
                    rounded = rounded.setScale(1, RoundingMode.HALF_UP);
                    String ip = String.valueOf(rounded);
                    ip = ip.replace(".3", ".1");
                    ip = ip.replace(".7", ".2");
                    line.setIp(ip);
                    line.setH(cursor.getString(7));
                    line.setR(cursor.getString(8));
                    line.setEr(cursor.getString(9));
                    line.setBb(cursor.getString(10));
                    line.setK(cursor.getString(11));
                    line.setPi(cursor.getString(12));
                    line.setHr(cursor.getString(13));
                    line.setOuts(cursor.getString(14));
                    line.setStart(cursor.getInt(15));
                    line.setBf(cursor.getString(16));
                    line.setIr(cursor.getInt(17));
                    line.setIrs(cursor.getInt(18));
                    line.setGb(cursor.getInt(19));
                    line.setFb(cursor.getInt(20));
                    line.setHb(cursor.getInt(21));
                    line.setBalk(cursor.getInt(22));
                    sql2 = "select (((1.0*sum(er))*9)/(sum(outs)/3)) from "+universe_name+"_players_game_pitching_stats where \n" +
                            "player_id = "+cursor.getString(0)+" and game_id in\n" +
                            "(select game_id from "+universe_name+"_games where game_type = "+game_type+" and league_id = "+league_id+" \n" +
                            "and date <= \""+game_date+"\")";
                    cursor2 = db.rawQuery(sql2, null);
                    if (cursor2.moveToFirst()) {
                        BigDecimal era = new BigDecimal(Float.toString(cursor2.getFloat(0)));
                        era = era.setScale(2, RoundingMode.HALF_UP);
                        line.setEra(String.valueOf(era));
                    }
                    cursor2.close();
                    pitchingLineScores.add(line);
                } while (cursor.moveToNext());
            }
            cursor = null;
            sql = "select -1 as player_id, 'TOTAL' as first_name, '' as last_name, -1 as w, -1 as l, -1 as s, \n" +
                    "sum((1.0 * outs)/3) as ip, sum(ha),  sum(r), sum(er), sum(bb), sum(k), sum(pi), sum(hra), sum(outs), sum(gs), \n" +
                    "sum(bf), sum(ir), sum(irs), sum(gb), sum(fb), sum(hp), sum(bk)\n" +
                    "from "+universe_name+"_players_game_pitching_stats\n" +
                    "where game_id = "+game_id+" and team_id = "+team_id+"";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                PitchingLineScore line = new PitchingLineScore();
                line.setPlayerID(cursor.getString(0));
                line.setFirstName(cursor.getString(1));
                line.setLastName(cursor.getString(2));
                line.setW(cursor.getString(3));
                line.setL(cursor.getString(4));
                line.setS(cursor.getString(5));
                BigDecimal rounded = new BigDecimal(Float.toString(cursor.getFloat(6)));
                rounded = rounded.setScale(1, RoundingMode.HALF_UP);
                String ip = String.valueOf(rounded);
                ip = ip.replace(".3", ".1");
                ip = ip.replace(".7", ".2");
                line.setIp(ip);
                line.setH(cursor.getString(7));
                line.setR(cursor.getString(8));
                line.setEr(cursor.getString(9));
                line.setBb(cursor.getString(10));
                line.setK(cursor.getString(11));
                line.setPi(cursor.getString(12));
                line.setHr(cursor.getString(13));
                line.setOuts(cursor.getString(14));
                line.setStart(cursor.getInt(15));
                line.setBf(cursor.getString(16));
                line.setIr(cursor.getInt(17));
                line.setIrs(cursor.getInt(18));
                line.setGb(cursor.getInt(19));
                line.setFb(cursor.getInt(20));
                line.setHb(cursor.getInt(21));
                line.setBalk(cursor.getInt(22));
                pitchingLineScores.add(line);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            if (cursor2 != null && !cursor2.isClosed())
                cursor2.close();
            
        }
        return pitchingLineScores;
    }

    public String getLastPlayedDate(int league, String name, String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        String universe_name = name;
        String returnDate = date;
        Cursor cursor = null;
        try {
            String sql = "select date from "+universe_name+"_games\n" +
                    "where played = 1\n" +
                    "and league_id = "+league+"\n" +
                    "order by date desc\n" +
                    "limit 1";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    returnDate = cursor.getString(0);
                } while (cursor.moveToNext());
            } cursor.close();
            return returnDate;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return returnDate;
    }

    public List<PendingGameSummary> getPendingScoreboard(String date, int league, String name, String html) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<PendingGameSummary> scoreboard = new ArrayList<PendingGameSummary>();
        PendingGameSummary gameSummary;
        String game_date = date;
        int league_id = league;
        String universe_name = name;
        String sql2;
        String sql3;
        Cursor cursor = null;
        Cursor cursor2 = null;
        Cursor cursor3 = null;
        int daysBetween = 0;
        Float era;
        try {
            Date lastPlayedDate = sqliteSdf.parse(getLastPlayedDate(league, name, date));
            Date currentDate = sqliteSdf.parse(game_date);
            daysBetween = daysBetween(lastPlayedDate,currentDate) - 1;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            String sql = "select a.game_id,\n"+
                    "a.time,\n" +
                    "b.team_id as home_id,\n" +
                    "b.name as home_name, \n" +
                    "b.nickname as home_nickname,\n" +
                    "b.abbr as home_abbr,\n" +
                    "b.logo_file_name as home_logo,\n" +
                    "c.team_id as away_id, \n" +
                    "c.name as away_name,\n" +
                    "c.nickname as away_nickname, \n" +
                    "c.abbr as away_abbr,\n" +
                    "c.logo_file_name as away_logo,\n" +
                    "b.background_color_id as home_bg_color,\n"+
                    "b.text_color_id as home_text_color,\n"+
                    "c.background_color_id as away_bg_color,\n"+
                    "c.text_color_id as away_text_color\n"+
                    "from "+universe_name+"_games a\n" +
                    "inner join "+universe_name+"_teams b\n" +
                    "on a.home_team = b.team_id\n" +
                    "inner join "+universe_name+"_teams c\n" +
                    "on a.away_team = c.team_id\n" +
                    "where a.date = \""+game_date+"\" and a.league_id = "+league_id;
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    gameSummary = new PendingGameSummary();
                    gameSummary.setGameId(cursor.getInt(0));
                    gameSummary.setGameTime(cursor.getString(1));
                    gameSummary.setHomeTeamId(cursor.getInt(2));
                    gameSummary.setHomeTeamName(cursor.getString(3));
                    gameSummary.setHomeTeamNickhame(cursor.getString(4));
                    gameSummary.setHomeTeamAbbr(cursor.getString(5));
                    gameSummary.setHomeTeamLogo(cursor.getString(6));
                    gameSummary.setAwayTeamId(cursor.getInt(7));
                    gameSummary.setAwayTeamName(cursor.getString(8));
                    gameSummary.setAwayTeamNickname(cursor.getString(9));
                    gameSummary.setAwayTeamAbbr(cursor.getString(10));
                    gameSummary.setAwayTeamLogo(cursor.getString(11));
                    gameSummary.setHomeBGColor(cursor.getString(12));
                    gameSummary.setHomeTextColor(cursor.getString(13));
                    gameSummary.setAwayBGColor(cursor.getString(14));
                    gameSummary.setAwayTextColor(cursor.getString(15));
                    sql2 = "select sum(w), sum(l) from " + universe_name + "_players_game_pitching_stats where\n" +
                            "team_id = " + cursor.getInt(2) + " and game_id in\n" +
                            "(select game_id from " + universe_name + "_games where game_type = 0)";
                    cursor2 = db.rawQuery(sql2, null);
                    if (cursor2.moveToFirst()) {
                        do {
                            gameSummary.setHomeRecord("(" + cursor2.getInt(0) + "-" + cursor2.getInt(1) + ")");
                        } while (cursor2.moveToNext());
                    }
                    cursor2.close();
                    sql2 = "select sum(w), sum(l) from " + universe_name + "_players_game_pitching_stats where\n" +
                            "team_id = " + cursor.getInt(7) + " and game_id in\n" +
                            "(select game_id from " + universe_name + "_games where game_type = 0)";
                    cursor2 = db.rawQuery(sql2, null);
                    if (cursor2.moveToFirst()) {
                        do {
                            gameSummary.setAwayRecord("(" + cursor2.getInt(0) + "-" + cursor2.getInt(1) + ")");
                        } while (cursor2.moveToNext());
                    }
                    cursor2.close();
                    if (daysBetween > 7) {
                        gameSummary.setAwayStarter(0);
                        gameSummary.setAwayStarterName("TBD");
                        gameSummary.setHomeStarter(0);
                        gameSummary.setHomeStarterName("TBD");
                        gameSummary.setAwayStarterLine("(0-0, 0.00)");
                        gameSummary.setHomeStarterLine("(0-0, 0.00)");
                    } else {
                        sql2 = "select a.starter_"+daysBetween+", b.first_name, b.last_name\n" +
                                "from "+universe_name+"_projected_starting_pitchers a\n" +
                                "inner join "+universe_name+"_players b\n" +
                                "on a.starter_"+daysBetween+" = b.player_id\n" +
                                "where a.team_id = "+cursor.getInt(2);
                        cursor2 = db.rawQuery(sql2, null);
                        if (cursor2.moveToFirst()) {
                            do {
                                gameSummary.setHomeStarter(cursor2.getInt(0));
                                gameSummary.setHomeStarterName(cursor2.getString(2));
                                sql3 = "select sum(w), sum(l), sum(outs), sum(er)  from "+universe_name+"_players_game_pitching_stats where \n" +
                                        "player_id = "+cursor2.getString(0)+" and game_id in\n" +
                                        "(select game_id from "+universe_name+"_games where game_type = 0 and league_id = "+league_id+" \n" +
                                        "and date <= \"" + game_date + "\")";
                                cursor3 = db.rawQuery(sql3, null);
                                if (cursor3.moveToFirst()) {
                                    do {
                                        era = cursor3.getFloat(3) * 9 / (cursor3.getFloat(2)/3);
                                        BigDecimal rounded = new BigDecimal(Float.toString(era));
                                        rounded = rounded.setScale(2, RoundingMode.HALF_UP);
                                        gameSummary.setHomeStarterLine("("+cursor3.getInt(0)+"-"+cursor3.getInt(1)+", "+rounded+")");
                                    } while (cursor2.moveToNext());
                                } cursor3.close();
                            } while (cursor2.moveToNext());
                        } cursor2.close();
                        sql2 = "select a.starter_"+daysBetween+", b.first_name, b.last_name\n" +
                                "from "+universe_name+"_projected_starting_pitchers a\n" +
                                "inner join "+universe_name+"_players b\n" +
                                "on a.starter_"+daysBetween+" = b.player_id\n" +
                                "where a.team_id = "+cursor.getInt(7);
                        cursor2 = db.rawQuery(sql2, null);
                        if (cursor2.moveToFirst()) {
                            do {
                                gameSummary.setAwayStarter(cursor2.getInt(0));
                                gameSummary.setAwayStarterName(cursor2.getString(2));
                                sql3 = "select sum(w), sum(l), sum(outs), sum(er)  from "+universe_name+"_players_game_pitching_stats where \n" +
                                        "player_id = "+cursor2.getString(0)+" and game_id in\n" +
                                        "(select game_id from "+universe_name+"_games where game_type = 0 and league_id = "+league_id+" \n" +
                                        "and date <= \"" + game_date + "\")";
                                cursor3 = db.rawQuery(sql3, null);
                                if (cursor3.moveToFirst()) {
                                    do {
                                        era = cursor3.getFloat(3) * 9 / (cursor3.getFloat(2)/3);
                                        BigDecimal rounded = new BigDecimal(Float.toString(era));
                                        rounded = rounded.setScale(2, RoundingMode.HALF_UP);
                                        gameSummary.setAwayStarterLine("("+cursor3.getInt(0)+"-"+cursor3.getInt(1)+", "+rounded+")");
                                    } while (cursor2.moveToNext());
                                } cursor3.close();
                            } while (cursor2.moveToNext());
                        } cursor2.close();
                    }
                    scoreboard.add(gameSummary);
                } while (cursor.moveToNext());
                return scoreboard;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            if (cursor2 != null && !cursor2.isClosed())
                cursor2.close();
            if (cursor3 != null && !cursor3.isClosed())
                cursor3.close();
            
        }
        return scoreboard;
    }

    public boolean gamesToday(String date, int league, String universe_name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql;
        Cursor cursor = null;
        boolean games = false;
        try {
            sql = "select count(*) from "+universe_name+"_games where \"date\" = \""+date+"\" and league_id = "+league;
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    games = cursor.getInt(0) > 0 ? true : false;
                } while (cursor.moveToNext());
            } cursor.close();
            return games;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return games;
    }

    public boolean pendingGamesToday(String date, int league, String universe_name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql;
        Cursor cursor = null;
        boolean games = false;
        try {
            sql = "select count(*) from "+universe_name+"_games where \"date\" = \""+date+"\" and played = 0 and league_id = "+league;
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    games = cursor.getInt(0) > 0 ? true : false;
                } while (cursor.moveToNext());
            } cursor.close();
            return games;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return games;
    }

    public List<SubLeague> getSubLeagues(int league, String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String universe_name = name;
        int league_id = league;
        List<SubLeague> subLeagues = new ArrayList<SubLeague>();
        Cursor cursor = null;
        try {
            String sql = "select sub_league_id, name, abbr from "+universe_name+"_sub_leagues " +
                    "where league_id = "+league_id+" order by sub_league_id asc";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    SubLeague subLeague = new SubLeague();
                    subLeague.setSubLeagueId(cursor.getInt(0));
                    subLeague.setName(cursor.getString(1));
                    subLeague.setAbbr(cursor.getString(2));
                    subLeagues.add(subLeague);
                } while (cursor.moveToNext());
                return subLeagues;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return subLeagues;
    }

    public int getSubLeagueCount(String universe_name, int league_id) {
        int subLeagues = 1;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            String sql = "select count(*) from "+universe_name+"_sub_leagues where league_id = "+league_id+"";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    subLeagues = cursor.getInt(0);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return subLeagues;
    }

    public List<Division> getDivisions(int league, int sub_league, String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String universe_name = name;
        int league_id = league;
        int sub_league_id = sub_league;
        Cursor cursor = null;
        List<Division> divisions = new ArrayList<Division>();
        try {
            String sql = "select division_id, name from "+universe_name+"_divisions\n" +
                    "where league_id = "+league_id+" and sub_league_id = "+sub_league_id+" order by division_id asc";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    Division division = new Division();
                    division.setDivisionId(cursor.getInt(0));
                    division.setName(cursor.getString(1));
                    divisions.add(division);
                } while (cursor.moveToNext());
                return divisions;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return divisions;
    }

    public List<StandingsLine> getWildCardStandings(int league, int sub_league, String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String universe_name = name;
        int league_id = league;
        int sub_league_id = sub_league;
        List<StandingsLine> standings = new ArrayList<StandingsLine>();
        String sql;
        Cursor cursor = null;
        try {
            sql = "select a.name, a.nickname,\n" +
                    "e.w, e.l, e.pos, e.pct, e.gb, e.magic_number\n" +
                    "from "+universe_name+"_teams a\n" +
                    "left join "+universe_name+"_leagues b\n" +
                    "on a.league_id = b.league_id\n" +
                    "left join "+universe_name+"_sub_leagues c\n" +
                    "on a.sub_league_id = c.sub_league_id and b.league_id = c.league_id\n" +
                    "inner join "+universe_name+"_team_record e\n" +
                    "on a.team_id = e.team_id\n" +
                    "where a.allstar_team < 1 and a.league_id = "+league_id+" \n" +
                    "and c.sub_league_id = "+sub_league_id+" and e.pos > 1\n" +
                    "order by e.pct desc";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    StandingsLine line = new StandingsLine();
                    line.setName(cursor.getString(0));
                    line.setNickname(cursor.getString(1));
                    line.setWins(cursor.getInt(2));
                    line.setLosses(cursor.getInt(3));
                    line.setPosition(cursor.getInt(4));
                    BigDecimal rounded = new BigDecimal(Float.toString(cursor.getFloat(5)));
                    String displayPct = rounded.setScale(3, RoundingMode.HALF_UP).toString();
                    line.setPercentage(displayPct.replaceFirst("^0.", "."));
                    line.setGamesBehind(cursor.getFloat(6));
                    line.setMagicNumber(cursor.getInt(7));
                    standings.add(line);
                } while (cursor.moveToNext());
                cursor.close();
            }
            return standings;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return standings;
    }

    public List<StandingsLine> getDivisionStandings(int league, int sub_league, int division, String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String universe_name = name;
        int league_id = league;
        int sub_league_id = sub_league;
        int division_id = division;
        Cursor cursor = null;
        List<StandingsLine> standings = new ArrayList<StandingsLine>();
        try {
            String sql = "select a.name, a.nickname,\n" +
                    "e.w, e.l, e.pos, e.pct, e.gb, e.magic_number\n" +
                    "from "+universe_name+"_teams a\n" +
                    "left join "+universe_name+"_leagues b\n" +
                    "on a.league_id = b.league_id\n" +
                    "left join "+universe_name+"_sub_leagues c\n" +
                    "on a.sub_league_id = c.sub_league_id and b.league_id = c.league_id\n" +
                    "left join "+universe_name+"_divisions d\n" +
                    "on a.division_id = d.division_id and b.league_id = d.league_id \n" +
                    "and c.sub_league_id = d.sub_league_id\n" +
                    "inner join "+universe_name+"_team_record e\n" +
                    "on a.team_id = e.team_id\n" +
                    "where a.allstar_team < 1 and a.league_id = "+league_id+" \n" +
                    "and c.sub_league_id = "+sub_league_id+" and d.division_id = "+division_id+"\n" +
                    "order by b.league_id, c.sub_league_id, d.division_id, e.pos";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    StandingsLine line = new StandingsLine();
                    line.setName(cursor.getString(0));
                    line.setNickname(cursor.getString(1));
                    line.setWins(cursor.getInt(2));
                    line.setLosses(cursor.getInt(3));
                    line.setPosition(cursor.getInt(4));
                    BigDecimal rounded = new BigDecimal(Float.toString(cursor.getFloat(5)));
                    String displayPct = rounded.setScale(3, RoundingMode.HALF_UP).toString();
                    line.setPercentage(displayPct.replaceFirst("^0.", "."));
                    line.setGamesBehind(cursor.getFloat(6));
                    line.setMagicNumber(cursor.getInt(7));
                    standings.add(line);
                } while (cursor.moveToNext());
            }
            return standings;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return standings;
    }

    public PitchingStatSummary currentYearPitchingSummary(String universe_name, int player_id, int league_id, int season) {
        SQLiteDatabase db = this.getReadableDatabase();
        PitchingStatSummary summary = new PitchingStatSummary();
        Cursor cursor = null;
        try {
            String sql = "select w, l, outs, ha, bb, k, er from "+universe_name+"_players_career_pitching_stats\n" +
                    "where split_id = 1 and league_id = "+league_id+" and\n" +
                    "player_id = "+player_id+" and year = "+season;
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    summary.setW(cursor.getInt(0));
                    summary.setL(cursor.getInt(1));
                    summary.setSo(cursor.getInt(5));
                    Float ip = (cursor.getFloat(2)/3);

                    BigDecimal rIP = new BigDecimal(Float.toString(ip));
                    rIP = rIP.setScale(1, RoundingMode.HALF_UP);
                    String tmpIP = String.valueOf(rIP);
                    tmpIP = tmpIP.replace(".3", ".1");
                    tmpIP = tmpIP.replace(".7", ".2");
                    summary.setIp(tmpIP);

                    Float era = cursor.getFloat(6) * 9 / (cursor.getFloat(2)/3);
                    BigDecimal rERA = new BigDecimal(Float.toString(era));
                    rERA = rERA.setScale(2, RoundingMode.HALF_UP);
                    summary.setEra(rERA);

                    //Float whip = (cursor.getFloat(4) + cursor.getFloat(3)) *9 / (cursor.getFloat(2)/3);
                    Float whip = (cursor.getFloat(4) + cursor.getFloat(3)) / (cursor.getFloat(2)/3);
                    BigDecimal rWHIP = new BigDecimal(Float.toString(whip));
                    rWHIP = rWHIP.setScale(2, RoundingMode.HALF_UP);
                    summary.setWhip(rWHIP);
                } while (cursor.moveToNext());
            } return summary;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return summary;
    }

    public PitchingStatSummary careerPitchingSummary(String universe_name, int player_id, int league_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        PitchingStatSummary summary = new PitchingStatSummary();
        Cursor cursor = null;
        try {
            String sql = "select sum(w), sum(l), sum(outs), sum(ha), sum(bb), sum(k), sum(er) from "+universe_name+"_players_career_pitching_stats\n" +
                    "where split_id = 1 and league_id = "+league_id+" and\n" +
                    "player_id = "+player_id;
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    summary.setW(cursor.getInt(0));
                    summary.setL(cursor.getInt(1));
                    summary.setSo(cursor.getInt(5));
                    Float ip = (cursor.getFloat(2)/3);

                    BigDecimal rIP = new BigDecimal(Float.toString(ip));
                    rIP = rIP.setScale(1, RoundingMode.HALF_UP);
                    String tmpIP = String.valueOf(rIP);
                    tmpIP = tmpIP.replace(".3", ".1");
                    tmpIP = tmpIP.replace(".7", ".2");
                    summary.setIp(tmpIP);

                    Float era = cursor.getFloat(6) * 9 / (cursor.getFloat(2)/3);
                    BigDecimal rERA = new BigDecimal(Float.toString(era));
                    rERA = rERA.setScale(2, RoundingMode.HALF_UP);
                    summary.setEra(rERA);

                    //Float whip = (cursor.getFloat(4) + cursor.getFloat(3)) *9 / (cursor.getFloat(2)/3);
                    Float whip = (cursor.getFloat(4) + cursor.getFloat(3)) / (cursor.getFloat(2)/3);
                    BigDecimal rWHIP = new BigDecimal(Float.toString(whip));
                    rWHIP = rWHIP.setScale(2, RoundingMode.HALF_UP);
                    summary.setWhip(rWHIP);
                } while (cursor.moveToNext());
            } return summary;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return summary;
    }

    public BattingStatSummary currentYearBattingSummary(String universe_name, int player_id, int league_id, int season) {
        SQLiteDatabase db = this.getReadableDatabase();
        BattingStatSummary summary = new BattingStatSummary();
        Cursor cursor = null;
        try {
            String sql = "select sum(ab), sum(h), sum(pa), sum(d), sum(t), sum(hr), sum(rbi),\n" +
                    "sum(sb), sum(bb), sum(ibb), sum(sh), sum(sf), sum(hp)\n" +
                    "from "+universe_name+"_players_career_batting_stats\n" +
                    "where split_id = 1 and league_id = "+league_id+" and\n" +
                    "player_id = "+player_id+" and year = "+season;
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    Float atBats = cursor.getFloat(0);
                    Float hits = cursor.getFloat(1);
                    Float doubles = cursor.getFloat(3);
                    Float triples = cursor.getFloat(4);
                    Float homeRuns = cursor.getFloat(5);
                    Float walks = cursor.getFloat(8);
                    Float sacFlies = cursor.getFloat(11);
                    Float hbp = cursor.getFloat(12);

                    BigDecimal avg = new BigDecimal(Float.toString(hits/atBats));
                    avg = avg.setScale(3,RoundingMode.HALF_UP);
                    summary.setAvg(avg);

                    BigDecimal slg = new BigDecimal(Float.toString(((hits-doubles-triples-homeRuns)+(doubles*2)+(triples*3)+(homeRuns*4))/atBats));
                    slg = slg.setScale(3, RoundingMode.HALF_UP);

                    BigDecimal obp = new BigDecimal(Float.toString((hits + walks + hbp) / (atBats + walks + hbp + sacFlies)));
                    obp = obp.setScale(3, RoundingMode.HALF_UP);

                    BigDecimal ops = slg.add(obp);

                    summary.setHr(cursor.getInt(5));
                    summary.setRbi(cursor.getInt(6));
                    summary.setSb(cursor.getInt(7));
                    summary.setAvg(avg);
                    summary.setOps(ops);
                } while (cursor.moveToNext());
            } return summary;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return summary;
    }

    public BattingStatSummary careerBattingSummary(String universe_name, int player_id, int league_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        BattingStatSummary summary = new BattingStatSummary();
        Cursor cursor = null;
        try {
            String sql = "select sum(ab), sum(h), sum(pa), sum(d), sum(t), sum(hr), sum(rbi),\n" +
                    "sum(sb), sum(bb), sum(ibb), sum(sh), sum(sf), sum(hp)\n" +
                    "from "+universe_name+"_players_career_batting_stats\n" +
                    "where split_id = 1 and league_id = "+league_id+" and\n" +
                    "player_id = "+player_id;
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    Float atBats = cursor.getFloat(0);
                    Float hits = cursor.getFloat(1);
                    Float doubles = cursor.getFloat(3);
                    Float triples = cursor.getFloat(4);
                    Float homeRuns = cursor.getFloat(5);
                    Float walks = cursor.getFloat(8);
                    Float sacFlies = cursor.getFloat(11);
                    Float hbp = cursor.getFloat(12);

                    BigDecimal avg = new BigDecimal(Float.toString(hits/atBats));
                    avg = avg.setScale(3,RoundingMode.HALF_UP);
                    summary.setAvg(avg);

                    BigDecimal slg = new BigDecimal(Float.toString(((hits-doubles-triples-homeRuns)+(doubles*2)+(triples*3)+(homeRuns*4))/atBats));
                    slg = slg.setScale(3, RoundingMode.HALF_UP);

                    BigDecimal obp = new BigDecimal(Float.toString((hits + walks + hbp) / (atBats + walks + hbp + sacFlies)));
                    obp = obp.setScale(3, RoundingMode.HALF_UP);

                    BigDecimal ops = slg.add(obp);

                    summary.setHr(cursor.getInt(5));
                    summary.setRbi(cursor.getInt(6));
                    summary.setSb(cursor.getInt(7));
                    summary.setAvg(avg);
                    summary.setOps(ops);
                } while (cursor.moveToNext());
            } return summary;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return summary;
    }

    public List<BattingStatLine> battingStatLines(String universe_name, int league_id, int player_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<BattingStatLine> list = new ArrayList<BattingStatLine>();
        Cursor cursor = null;
        try {
            String sql = "select b.abbr, a.year, a.g, a.ab, a.r, a.h, a.d, a.t, \n" +
                    "a.hr, a.rbi, a.sb, a.bb, a.k, a.war, a.hp, a.sf, a.stint\n" +
                    "from "+universe_name+"_players_career_batting_stats  a\n" +
                    "inner join "+universe_name+"_teams b \n" +
                    "on a.team_id = b.team_id\n" +
                    "where a.split_id = 1\n" +
                    "and a.player_id = "+player_id+"\n" +
                    "and a.league_id = "+league_id+"\n" +
                    "union all\n" +
                    "select '' as abbr, 'TOTAL' as year, sum(a.g), sum(a.ab), sum(a.r), sum(a.h), sum(a.d), sum(a.t),\n" +
                    "sum(a.hr), sum(a.rbi), sum(a.sb), sum(a.bb), sum(a.k), sum(a.war), sum(a.hp), sum(a.sf), 0 as stint\n" +
                    "from "+universe_name+"_players_career_batting_stats  a\n" +
                    "inner join "+universe_name+"_teams b \n" +
                    "on a.team_id = b.team_id\n" +
                    "where a.split_id = 1\n" +
                    "and a.player_id = "+player_id+"\n" +
                    "and a.league_id = "+league_id+"\n" +
                    "order by year, stint";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    BattingStatLine line = new BattingStatLine();
                    Float atBats = cursor.getFloat(3);
                    Float hits = cursor.getFloat(5);
                    Float doubles = cursor.getFloat(6);
                    Float triples = cursor.getFloat(7);
                    Float homeRuns = cursor.getFloat(8);
                    Float walks = cursor.getFloat(11);
                    Float sacFlies = cursor.getFloat(15);
                    Float hbp = cursor.getFloat(14);

                    BigDecimal avg;
                    try {
                        avg = new BigDecimal(Float.toString(hits/atBats));
                    } catch (NumberFormatException e) {
                        avg = new BigDecimal(Float.toString(0));
                    }
                    avg = avg.setScale(3,RoundingMode.HALF_UP);

                    BigDecimal slg;
                    try {
                        slg = new BigDecimal(Float.toString(((hits-doubles-triples-homeRuns)+(doubles*2)+(triples*3)+(homeRuns*4))/atBats));
                    } catch (NumberFormatException e) {
                        slg = new BigDecimal(Float.toString(0));
                    }
                    slg = slg.setScale(3, RoundingMode.HALF_UP);

                    BigDecimal obp;
                    try {
                        obp =  new BigDecimal(Float.toString((hits + walks + hbp) / (atBats + walks + hbp + sacFlies)));
                    } catch (NumberFormatException e) {
                        obp = new BigDecimal(Float.toString(0));
                    }
                    obp = obp.setScale(3, RoundingMode.HALF_UP);

                    BigDecimal ops = slg.add(obp);

                    BigDecimal war = new BigDecimal(Float.toString(cursor.getFloat(13)));
                    war = war.setScale(1, RoundingMode.HALF_UP);

                    line.setTeam(cursor.getString(0));
                    line.setYear(cursor.getString(1));
                    line.setGames(cursor.getString(2));
                    line.setAtbats(cursor.getString(3));
                    line.setRuns(cursor.getString(4));
                    line.setHits(cursor.getString(5));
                    line.setDoubles(cursor.getString(6));
                    line.setTriples(cursor.getString(7));
                    line.setHomeruns(cursor.getString(8));
                    line.setRbi(cursor.getString(9));
                    line.setSb(cursor.getString(10));
                    line.setWalks(cursor.getString(11));
                    line.setStrikeouts(cursor.getString(12));
                    line.setWar(String.valueOf(war));
                    line.setAvg(String.valueOf(avg).replaceFirst("^0.", "."));
                    line.setObp(String.valueOf(obp).replaceFirst("^0.", "."));
                    line.setSlg(String.valueOf(slg).replaceFirst("^0.", "."));
                    line.setOps(String.valueOf(ops).replaceFirst("^0.", "."));
                    list.add(line);
                } while (cursor.moveToNext());
            } return list;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return list;
    }

    public List<PitchingStatLine> pitchingStatLines(String universe_name, int league_id, int player_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<PitchingStatLine> list = new ArrayList<PitchingStatLine>();
        Cursor cursor = null;
        try {
            String sql = "select b.abbr, a.year, a.g, a.gs, a.cg, a.sho, a.w, a.l, a.s, \n" +
                    "a.outs, a.ha, a.hra, a.r, a.er, a.bb, a.k, a.war, a.stint\n" +
                    "from "+universe_name+"_players_career_pitching_stats  a\n" +
                    "inner join "+universe_name+"_teams b \n" +
                    "on a.team_id = b.team_id\n" +
                    "where a.split_id = 1\n" +
                    "and a.player_id = "+player_id+"\n" +
                    "and a.league_id = "+league_id+"\n" +
                    "union all\n" +
                    "select '' as abbr, 'TOTAL' as year, sum(a.g), sum(a.gs), sum(a.cg), sum(a.sho), sum(a.w), sum(a.l), sum(a.s), \n" +
                    "sum(a.outs), sum(a.ha), sum(a.hra), sum(a.r), sum(a.er), sum(a.bb), sum(a.k), sum(a.war), 0 as stint\n" +
                    "from "+universe_name+"_players_career_pitching_stats  a\n" +
                    "inner join "+universe_name+"_teams b \n" +
                    "on a.team_id = b.team_id\n" +
                    "where a.split_id = 1\n" +
                    "and a.player_id = "+player_id+"\n" +
                    "and a.league_id = "+league_id+"\n" +
                    "order by year, stint";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    PitchingStatLine line = new PitchingStatLine();
                    Float ip = cursor.getFloat(9) / 3;
                    Float er = cursor.getFloat(13);
                    Float walks = cursor.getFloat(14);
                    Float hits = cursor.getFloat(10);

                    BigDecimal innings = new BigDecimal(Float.toString(ip));
                    innings = innings.setScale(1, RoundingMode.HALF_UP);

                    BigDecimal era;
                    try {
                        era = new BigDecimal(Float.toString((er*9)/ip));
                    } catch (NumberFormatException e) {
                        era = new BigDecimal(Float.toString(0));
                    }
                    era = era.setScale(2, RoundingMode.HALF_UP);

                    BigDecimal whip;
                    try {
                        whip = new BigDecimal(Float.toString((walks+hits)/ip));
                    } catch (NumberFormatException e) {
                        whip = new BigDecimal(Float.toString(0));
                    }
                    whip = whip.setScale(2, RoundingMode.HALF_UP);

                    BigDecimal war = new BigDecimal(Float.toString(cursor.getFloat(16)));
                    war = war.setScale(1, RoundingMode.HALF_UP);

                    line.setTeam(cursor.getString(0));
                    line.setYear(cursor.getString(1));
                    line.setGames(cursor.getString(2));
                    line.setGamesStarted(cursor.getString(3));
                    line.setCompleteGames(cursor.getString(4));
                    line.setShutouts(cursor.getString(5));
                    line.setWins(cursor.getString(6));
                    line.setLosses(cursor.getString(7));
                    line.setSaves(cursor.getString(8));
                    String tmpIP = String.valueOf(innings);
                    tmpIP = tmpIP.replace(".3", ".1");
                    tmpIP = tmpIP.replace(".7", ".2");
                    line.setInningsPitched(tmpIP);
                    line.setHits(cursor.getString(10));
                    line.setHomeRuns(cursor.getString(11));
                    line.setRuns(cursor.getString(12));
                    line.setEarnedRuns(cursor.getString(13));
                    line.setWalks(cursor.getString(14));
                    line.setStrikeouts(cursor.getString(15));
                    line.setWar(String.valueOf(war));
                    line.setWhip(String.valueOf(whip));
                    line.setEra(String.valueOf(era));
                    list.add(line);
                } while (cursor.moveToNext());
            } return list;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return list;
    }

    public List<FieldingStatLine> fieldingStatLines(String universe_name, int league_id, int player_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<FieldingStatLine> list = new ArrayList<FieldingStatLine>();
        Cursor cursor = null;
        try {
            String sql = "select b.abbr, a.year, a.position, a.g, a.gs, a.po, a.a, a.dp, a.tc, a.e, a.ip, a.ipf, a.zr\n" +
                    "from "+universe_name+"_players_career_fielding_stats a\n" +
                    "inner join "+universe_name+"_teams b\n" +
                    "on a.team_id = b.team_id\n" +
                    "where a.player_id = "+player_id+"\n" +
                    "and a.league_id = "+league_id+"\n" +
                    "union all\n" +
                    "select '' as abbr, 'TOTAL' as year, position, sum(g), sum(gs), sum(po), sum(a), sum(dp), sum(tc), sum(e),\n" +
                    "sum(ip), sum(ipf), sum(zr)\n" +
                    "from "+universe_name+"_players_career_fielding_stats\n" +
                    "where player_id = "+player_id+"\n" +
                    "and league_id = "+league_id+"\n" +
                    "group by position\n" +
                    "order by year, position, abbr";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    FieldingStatLine line = new FieldingStatLine();

                    Float outs = (cursor.getFloat(10) * 3) + cursor.getFloat(11);
                    Float innings = outs / 3;

                    Float putouts = cursor.getFloat(5);
                    Float assists = cursor.getFloat(6);
                    Float errors = cursor.getFloat(9);
                    Float zr = cursor.getFloat(12);

                    BigDecimal pct;
                    try {
                        pct = new BigDecimal(Float.toString((putouts + assists) / (putouts + assists + errors)));
                    } catch (NumberFormatException e) {
                        pct = new BigDecimal(Float.toString(0));
                    }
                    pct = pct.setScale(3,RoundingMode.HALF_UP);


                    BigDecimal rangeFactor;
                    try {
                        rangeFactor = new BigDecimal(Float.toString(((putouts+assists)*9)/innings));
                    } catch (NumberFormatException e) {
                        rangeFactor = new BigDecimal(Float.toString(0));
                    }
                    rangeFactor = rangeFactor.setScale(2, RoundingMode.HALF_UP);

                    BigDecimal inn = new BigDecimal(Float.toString(innings));
                    inn = inn.setScale(1, RoundingMode.HALF_UP);

                    BigDecimal zoneRating = new BigDecimal(Float.toString(zr));
                    zoneRating = zoneRating.setScale(2, RoundingMode.HALF_UP);

                    line.setTeam(cursor.getString(0));
                    line.setYear(cursor.getString(1));
                    line.setPosition(constants.positions[cursor.getInt(2)]);
                    line.setGames(cursor.getString(3));
                    line.setGamesStarted(cursor.getString(4));
                    line.setPutouts(cursor.getString(5));
                    line.setAssists(cursor.getString(6));
                    line.setDoublePlays(cursor.getString(7));
                    line.setTotalChances(cursor.getString(8));
                    line.setErrors(cursor.getString(9));
                    String tmpIP = String.valueOf(inn);
                    tmpIP = tmpIP.replace(".3", ".1");
                    tmpIP = tmpIP.replace(".7", ".2");
                    line.setInningsPlayed(String.valueOf(tmpIP));
                    line.setZoneRating(String.valueOf(zoneRating));
                    line.setRangeFactor(String.valueOf(rangeFactor));
                    line.setFieldingPercentage(String.valueOf(pct).replaceFirst("^0.", "."));

                    list.add(line);
                } while (cursor.moveToNext());
            } return list;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return list;
    }

    public Map<String, String> getAwardNames(String universe_name, int league_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Map<String, String> awards = new HashMap<String, String>();
        Cursor cursor = null;
        awards.put("mvp","Most Valuable Player Award");
        awards.put("poy","Pitcher of the Year Award");
        awards.put("roy","Rookie of the Year Award");
        awards.put("gg","Glove Wizard Award");
        try {
            String sql = "select pitcher_award_name, mvp_award_name, rookie_award_name, \n" +
                    "defense_award_name from "+universe_name+"_leagues where league_id = "+league_id;
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    awards.put("poy", cursor.getString(0));
                    awards.put("mvp", cursor.getString(1));
                    awards.put("roy", cursor.getString(2));
                    awards.put("gg", cursor.getString(3));
                } while (cursor.moveToNext());
            } cursor.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return awards;
    }
    
    public ListMap<Integer, String> getMVPHistory(String universe_name, int league_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ListMap<Integer,String> multimap = new ListMap<>();
        String sql;
        Cursor cursor = null;
        try {
            sql = "select a.year, b.abbr,\n" +
                    "c.first_name, c.last_name, coalesce(f.abbr, \"N/A\"),\n" +
                    "coalesce(f.name, \"Team\"), coalesce(f.nickname, \"Unknown\")" +
                    "from "+universe_name+"_league_history a\n" +
                    "inner join "+universe_name+"_sub_leagues b\n" +
                    "on a.sub_league_id = b.sub_league_id and a.league_id = b.league_id\n" +
                    "inner join "+universe_name+"_players c\n" +
                    "on a.best_hitter_id = c.player_id\n" +
                    "inner join "+universe_name+"_players_career_batting_stats e\n" +
                    "on a.best_hitter_id = e.player_id and a.year = e.year and a.league_id = e.league_id\n" +
                    "left join "+universe_name+"_team_history f\n" +
                    "on e.year = f.year and e.team_id = f.team_id\n" +
                    "where e.split_id = 1 and a.league_id = "+league_id;
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    String recipient = cursor.getString(1) + ": " + cursor.getString(2) + " " +
                                cursor.getString(3) + ", " + cursor.getString(5) + " " + cursor.getString(6);
                    multimap.putElement(cursor.getInt(0), recipient);
                } while (cursor.moveToNext());
            } cursor.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return multimap;
    }

    public ListMap<Integer, String> getCYAHistory(String universe_name, int league_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ListMap<Integer,String> multimap = new ListMap<>();
        String sql;
        Cursor cursor = null;
        try {
            sql = "select a.year, b.abbr,\n" +
                    "c.first_name, c.last_name, coalesce(f.abbr, \"N/A\"),\n" +
                    "coalesce(f.name, \"Team\"), coalesce(f.nickname, \"Unknown\")" +
                    "from "+universe_name+"_league_history a\n" +
                    "inner join "+universe_name+"_sub_leagues b\n" +
                    "on a.sub_league_id = b.sub_league_id and a.league_id = b.league_id\n" +
                    "inner join "+universe_name+"_players c\n" +
                    "on a.best_pitcher_id = c.player_id\n" +
                    "inner join "+universe_name+"_players_career_pitching_stats e\n" +
                    "on a.best_pitcher_id = e.player_id and a.year = e.year and a.league_id = e.league_id\n" +
                    "left join "+universe_name+"_team_history f\n" +
                    "on e.year = f.year and e.team_id = f.team_id\n" +
                    "where e.split_id = 1 and a.league_id = "+league_id;
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    String recipient = cursor.getString(1) + ": " + cursor.getString(2) + " " +
                            cursor.getString(3) + ", " + cursor.getString(5) + " " + cursor.getString(6);
                    multimap.putElement(cursor.getInt(0), recipient);
                } while (cursor.moveToNext());
            } cursor.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return multimap;
    }

    public ListMap<Integer, String> getROYHistory(String universe_name, int league_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ListMap<Integer,String> multimap = new ListMap<>();
        String sql;
        Cursor cursor = null;
        try {
            sql = "select a.year, b.abbr,\n" +
                    "c.first_name, c.last_name, coalesce(f.abbr, \"N/A\"),\n" +
                    "coalesce(f.name, \"Team\"), coalesce(f.nickname, \"Unknown\")" +
                    "from "+universe_name+"_league_history a\n" +
                    "inner join "+universe_name+"_sub_leagues b\n" +
                    "on a.sub_league_id = b.sub_league_id and a.league_id = b.league_id\n" +
                    "inner join "+universe_name+"_players c\n" +
                    "on a.best_rookie_id = c.player_id\n" +
                    "inner join "+universe_name+"_players_career_fielding_stats e\n" +
                    "on a.best_rookie_id = e.player_id and a.year = e.year and a.league_id = e.league_id\n" +
                    "left join "+universe_name+"_team_history f\n" +
                    "on e.year = f.year and e.team_id = f.team_id\n" +
                    "where a.league_id = "+league_id+"\n" +
                    "group by a.year, a.sub_league_id";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    String recipient = cursor.getString(1) + ": " + cursor.getString(2) + " " +
                            cursor.getString(3) + ", " + cursor.getString(5) + " " + cursor.getString(6);
                    multimap.putElement(cursor.getInt(0), recipient);
                } while (cursor.moveToNext());
            } cursor.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return multimap;
    }

    public ListMap<Integer, HistoricalLeader> getHistoricalLeaderboard(String universe_name, int league_id, int year, String table) {
        SQLiteDatabase db = this.getReadableDatabase();
        ListMap<Integer, HistoricalLeader> multimap = new ListMap<>();
        String sql;
        Cursor cursor = null;
        try {
            sql = "select a.player_id, b.first_name, b.last_name, a.sub_league_id, c.abbr, coalesce(e.abbr,'N/A'),\n" +
                    "a.year, a.category, a.place, a.amount\n" +
                    "from "+universe_name+"_players_league_leader a\n" +
                    "inner join "+universe_name+"_players b\n" +
                    "on a.player_id = b.player_id\n" +
                    "inner join "+universe_name+"_sub_leagues c\n" +
                    "on a.league_id = c.league_id  and a.sub_league_id = c.sub_league_id\n" +
                    "inner join "+universe_name+"_players_career_"+table+"_stats d\n" +
                    "on a.player_id = d.player_id and d.split_id = 1 and d.year = "+year+"\n" +
                    "left join "+universe_name+"_team_history e\n" +
                    "on d.team_id = e.team_id and e.year = "+year+"\n" +
                    "where a.league_id = "+league_id+" and a.year = "+year+" and a.place = 1\n" +
                    "order by a.year, a.category, a.sub_league_id";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    HistoricalLeader leader = new HistoricalLeader();
                    leader.setPlayerID(cursor.getString(0));
                    leader.setFirstName(cursor.getString(1));
                    leader.setLastName(cursor.getString(2));
                    leader.setSubLeagueID(cursor.getInt(3));
                    leader.setSubLeague(cursor.getString(4));
                    leader.setAbbr(cursor.getString(5));
                    leader.setYear(cursor.getString(6));
                    leader.setCategory(cursor.getInt(7));
                    leader.setPlace(cursor.getString(8));
                    leader.setAmount(cursor.getString(9));
                    multimap.putElement(cursor.getInt(7), leader);
                } while (cursor.moveToNext());
            } cursor.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return multimap;
    }

    public List<Integer> getAllYears(String universe_name, int league_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Integer> years = new ArrayList<Integer>();
        Cursor cursor = null;
        try {
            String sql = "select distinct year from "+universe_name+"_league_history\n" +
                    "where league_id = "+league_id+"\n" +
                    "order by year asc";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    years.add(cursor.getInt(0));
                } while (cursor.moveToNext());
            } cursor.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return years;
    }

    public List<Integer> getLeaderboardYears(String universe_name, int league_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Integer> years = new ArrayList<Integer>();
        Cursor cursor = null;
        try {
            String sql = "select distinct year from "+universe_name+"_players_league_leader\n" +
                    "where league_id = "+league_id+"\n" +
                    "order by year asc";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    years.add(cursor.getInt(0));
                } while (cursor.moveToNext());
            } cursor.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return years;
    }

    public List<Award> awardList(String universe_name, int league_id, int player_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Award> awardList = new ArrayList<>();
        Map awardNames = new HashMap<Integer,String>();
        awardNames.put(0, "Player of the Week");
        awardNames.put(1, "Pitcher of the Month");
        awardNames.put(2, "Batter of the Month");
        awardNames.put(3, "Rookie of the Month");
        awardNames.put(4, "Outstanding Pitcher Award");
        awardNames.put(5, "Outstanding Hitter Award");
        awardNames.put(6, "Rookie of the Year Award");
        awardNames.put(7, "Glove Wizard Award");
        awardNames.put(9, "Selected to the All-Star Game");
        awardNames.put(14, "Won League Championship");
        awardNames.put(15, "LCS MVP Award");
        Cursor cursor = null;
        String sql;
        try {
            sql = "select pitcher_award_name, mvp_award_name, rookie_award_name, \n" +
                    "defense_award_name from "+universe_name+"_leagues where league_id = "+league_id;
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    awardNames.put(4, cursor.getString(0));
                    awardNames.put(5, cursor.getString(1));
                    awardNames.put(6, cursor.getString(2));
                    awardNames.put(7, cursor.getString(3));
                } while (cursor.moveToNext());
            } cursor.close();
            sql = "select b.abbr, c.name, a.award_id, a.year, a.season, a.day, a.month, a.position\n" +
                    "from "+universe_name+"_players_awards a\n" +
                    "inner join "+universe_name+"_leagues b on a.league_id = b.league_id\n" +
                    "inner join "+universe_name+"_sub_leagues c on a.sub_league_id = c.sub_league_id and a.league_id = c.league_id\n" +
                    "where a.player_id = "+player_id+"\n" +
                    "and a.league_id = "+league_id+"\n" +
                    "order by a.year, a.month, a.day";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    Award award = new Award();
                    award.setLeague(cursor.getString(0));
                    award.setSubLeague(cursor.getString(1));
                    award.setAward(awardNames.get(cursor.getInt(2)).toString());
                    award.setYear(cursor.getInt(3));
                    award.setSeason(cursor.getString(4));
                    award.setDay(cursor.getInt(5));
                    award.setMonth(cursor.getInt(6));
                    award.setAwardID(cursor.getInt(2));
                    award.setPosition(cursor.getInt(7));
                    awardList.add(award);
                } while (cursor.moveToNext());
            } cursor.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return awardList;
    }

    public Integer getCareerPlateAppearances(String universe_name, int player_id, int league_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        int pa = 0;
        Cursor cursor = null;
        try {
            String sql = "select sum(pa) from "+universe_name+"_players_career_batting_stats\n" +
                    "where player_id = "+player_id+" and league_id = "+league_id;
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    pa = cursor.getInt(0);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return pa;
    }

    public Integer getCareerPitchingAppearances(String universe_name, int player_id, int league_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        int g = 0;
        Cursor cursor = null;
        try {
            String sql = "select sum(g) from "+universe_name+"_players_career_pitching_stats\n" +
                    "where player_id = "+player_id+" and league_id = "+league_id;
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    g = cursor.getInt(0);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return g;
    }

    public StatLeader getRateStatLeader(String universe_name, int league_id, Integer sub_league_id, int current_year, String stat, int team_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        StatLeader statLeader = new StatLeader();
        Cursor cursor = null;
        String formula = formulas.get(stat);
        String display = statsDisplay.get(stat);
        String sortOrder = statsSort.get(stat);
        String qualifier = statsQualifier.get(stat);
        String table = statsTable.get(stat);
        boolean stripZero = statsStripZero.get(stat);
        int roundTo = statsRoundTo.get(stat);
        String minMax = (sortOrder.equals("asc")) ? "min" : "max";
        if (stat.equals("pct")) {
            stat += "a";
        }
        try {
            String sql = "select a.player_id, c.first_name, c.last_name, d.name, d.nickname, \n" +
                    "d.abbr, "+formula+" as "+stat+"\n" +
                    "from "+universe_name+table+" a\n" +
                    "inner join "+universe_name+"_team_record b\n" +
                    "on a.team_id = b.team_id\n" +
                    "inner join "+universe_name+"_players c\n" +
                    "on a.player_id = c.player_id\n" +
                    "inner join "+universe_name+"_teams d\n" +
                    "on a.team_id = d.team_id\n" +
                    "where a.split_id = 1 and a.league_id = "+league_id+" and a.year = "+current_year+"\n" +
                    "and "+qualifier+"\n" +
                    "and a.split_id = 1\n";
            if (team_id > -1) {
                sql += "and a.team_id = "+team_id+"\n";
            } else {
                sql += "and a.team_id in\n" +
                        "(select team_id from "+universe_name+"_teams where league_id = "+league_id+" and sub_league_id = "+sub_league_id+")\n";
            }
            sql +=  "and "+qualifier+"\n" +
                    "and "+stat+" in (\n" +
                    "\tselect "+minMax+" "+formula+" as "+stat+"\n" +
                    "\tfrom "+universe_name+table+" a\n" +
                    "\tinner join "+universe_name+"_team_record b\n" +
                    "\ton a.team_id = b.team_id\n" +
                    "\twhere a.split_id = 1 and a.league_id = "+league_id+" and a.year = "+current_year+"\n" +
                    "\tand "+qualifier+"\n";
            if (team_id > -1) {
                sql += "\tand a.team_id = "+team_id+"\n";
            } else {
                sql += "\tand a.team_id in\n" +
                        "\t(select team_id from "+universe_name+"_teams where league_id = "+league_id+" and sub_league_id = "+sub_league_id+")\n";
            }
            sql +=  "\torder by "+stat+" "+sortOrder+"\n" +
                    ")\n" +
                    "order by "+stat+" "+sortOrder;
            cursor = db.rawQuery(sql, null);
            if (cursor.getCount() > 1) {
                statLeader.setStatistic(display);
                statLeader.setFirstName("");
                statLeader.setLastName("");
                statLeader.setTeamAbbr("");
                statLeader.setPlayerID(-1);
                statLeader.setDisplayName(cursor.getCount() + " players tied with");
                if (cursor.moveToFirst()) {
                    do {
                        BigDecimal tmpStat;
                        try {
                            tmpStat = new BigDecimal(cursor.getString(6));
                        } catch (NumberFormatException e) {
                            tmpStat = new BigDecimal(Float.toString(0));
                        }
                        tmpStat = tmpStat.setScale(roundTo,RoundingMode.HALF_UP);
                        if (stripZero) {
                            statLeader.setValue(String.valueOf(tmpStat).replaceFirst("^0.", "."));
                        } else {
                            statLeader.setValue(String.valueOf(tmpStat));
                        }
                    } while (cursor.moveToNext());
                }
            } else {
                if (cursor.moveToFirst()) {
                    do {
                        statLeader.setStatistic(display);
                        statLeader.setFirstName(cursor.getString(1));
                        statLeader.setLastName(cursor.getString(2));
                        statLeader.setTeamAbbr(cursor.getString(5));
                        statLeader.setPlayerID(cursor.getInt(0));
                        if (team_id == -1) {
                            statLeader.setDisplayName(cursor.getString(1)+ " "+cursor.getString(2)+
                                    ", "+ cursor.getString(5));
                        } else {
                            statLeader.setDisplayName(cursor.getString(1)+ " "+cursor.getString(2));
                        }
                        BigDecimal tmpStat;
                        try {
                            tmpStat = new BigDecimal(cursor.getString(6));
                        } catch (NumberFormatException e) {
                            tmpStat = new BigDecimal(Float.toString(0));
                        }
                        tmpStat = tmpStat.setScale(roundTo,RoundingMode.HALF_UP);
                        if (stripZero) {
                            statLeader.setValue(String.valueOf(tmpStat).replaceFirst("^0.", "."));
                        } else {
                            statLeader.setValue(String.valueOf(tmpStat));
                        }
                    } while (cursor.moveToNext());
                } else {
                    statLeader.setStatistic(stat);
                    statLeader.setFirstName("");
                    statLeader.setLastName("");
                    statLeader.setTeamAbbr("");
                    statLeader.setPlayerID(-1);
                    statLeader.setDisplayName("No Qualifiers");
                    statLeader.setValue("");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return statLeader;
    }

    public StatLeader getRateStat(String universe_name, int league_id, Integer sub_league_id, int current_year, String stat, int team_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        StatLeader statLeader = new StatLeader();
        Cursor cursor = null;
        String formula = formulas.get(stat);
        String display = statsDisplay.get(stat);
        String sortOrder = statsSort.get(stat);
        String qualifier = statsQualifier.get(stat);
        String table = statsTable.get(stat);
        boolean stripZero = statsStripZero.get(stat);
        int roundTo = statsRoundTo.get(stat);
        String minMax = (sortOrder.equals("asc")) ? "min" : "max";
        if (stat.equals("pct")) {
            stat += "a";
        }
        try {
            String sql = "select a.player_id, c.first_name, c.last_name, d.name, d.nickname, \n" +
                    "d.abbr, "+formula+" as "+stat+"\n" +
                    "from "+universe_name+table+" a\n" +
                    "inner join "+universe_name+"_team_record b\n" +
                    "on a.team_id = b.team_id\n" +
                    "inner join "+universe_name+"_players c\n" +
                    "on a.player_id = c.player_id\n" +
                    "inner join "+universe_name+"_teams d\n" +
                    "on a.team_id = d.team_id\n" +
                    "where a.split_id = 1 and a.league_id = "+league_id+" and a.year = "+current_year+"\n" +
                    "and "+qualifier+"\n" +
                    "and a.split_id = 1\n";
            if (team_id > -1) {
                sql += "and a.team_id = "+team_id+"\n";
            } else {
                sql += "and a.team_id in\n" +
                        "(select team_id from "+universe_name+"_teams where league_id = "+league_id+" and sub_league_id = "+sub_league_id+")\n";
            }
            sql +=  "and "+qualifier+"\n" +
                    "and "+stat+" in (\n" +
                    "\tselect "+minMax+" "+formula+" as "+stat+"\n" +
                    "\tfrom "+universe_name+table+" a\n" +
                    "\tinner join "+universe_name+"_team_record b\n" +
                    "\ton a.team_id = b.team_id\n" +
                    "\twhere a.split_id = 1 and a.league_id = "+league_id+" and a.year = "+current_year+"\n" +
                    "\tand "+qualifier+"\n";
            if (team_id > -1) {
                sql += "\tand a.team_id = "+team_id+"\n";
            } else {
                sql += "\tand a.team_id in\n" +
                        "\t(select team_id from "+universe_name+"_teams where league_id = "+league_id+" and sub_league_id = "+sub_league_id+")\n";
            }
            sql +=  "\torder by "+stat+" "+sortOrder+"\n" +
                    ")\n" +
                    "order by "+stat+" "+sortOrder;
            cursor = db.rawQuery(sql, null);
            if (cursor.getCount() > 1) {
                statLeader.setStatistic(display);
                statLeader.setFirstName("");
                statLeader.setLastName("");
                statLeader.setTeamAbbr("");
                statLeader.setPlayerID(-1);
                statLeader.setDisplayName(cursor.getCount() + " players tied with");
                if (cursor.moveToFirst()) {
                    do {
                        BigDecimal tmpStat;
                        try {
                            tmpStat = new BigDecimal(cursor.getString(6));
                        } catch (NumberFormatException e) {
                            tmpStat = new BigDecimal(Float.toString(0));
                        }
                        tmpStat = tmpStat.setScale(roundTo,RoundingMode.HALF_UP);
                        if (stripZero) {
                            statLeader.setValue(String.valueOf(tmpStat).replaceFirst("^0.", "."));
                        } else {
                            statLeader.setValue(String.valueOf(tmpStat));
                        }
                    } while (cursor.moveToNext());
                }
            } else {
                if (cursor.moveToFirst()) {
                    do {
                        statLeader.setStatistic(display);
                        statLeader.setFirstName(cursor.getString(1));
                        statLeader.setLastName(cursor.getString(2));
                        statLeader.setTeamAbbr(cursor.getString(5));
                        statLeader.setPlayerID(cursor.getInt(0));
                        statLeader.setDisplayName(cursor.getString(1)+ " "+cursor.getString(2)+
                                ", "+ cursor.getString(5));
                        BigDecimal tmpStat;
                        try {
                            tmpStat = new BigDecimal(cursor.getString(6));
                        } catch (NumberFormatException e) {
                            tmpStat = new BigDecimal(Float.toString(0));
                        }
                        tmpStat = tmpStat.setScale(roundTo,RoundingMode.HALF_UP);
                        if (stripZero) {
                            statLeader.setValue(String.valueOf(tmpStat).replaceFirst("^0.", "."));
                        } else {
                            statLeader.setValue(String.valueOf(tmpStat));
                        }
                    } while (cursor.moveToNext());
                } else {
                    statLeader.setStatistic(stat);
                    statLeader.setFirstName("");
                    statLeader.setLastName("");
                    statLeader.setTeamAbbr("");
                    statLeader.setPlayerID(-1);
                    statLeader.setDisplayName("No Qualifiers");
                    statLeader.setValue("");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return statLeader;
    }

    public List<StatLeader> getRateStatList(String universe_name, int league_id, Integer sub_league_id, int current_year, String stat, int team_id) {
        List<StatLeader> statLeaders = new ArrayList<StatLeader>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        String formula = formulas.get(stat);
        String display = statsDisplay.get(stat);
        String sortOrder = statsSort.get(stat);
        String qualifier = statsQualifier.get(stat);
        String table = statsTable.get(stat);
        boolean stripZero = statsStripZero.get(stat);
        int roundTo = statsRoundTo.get(stat);
        if (stat.equals("pct")) {
            stat += "a";
        }
        try {
            String sql = "select a.player_id, c.first_name, c.last_name, d.name, d.nickname, \n" +
                    "d.abbr, " + formula + " as " + stat + "\n" +
                    "from " + universe_name + table + " a\n" +
                    "inner join " + universe_name + "_team_record b\n" +
                    "on a.team_id = b.team_id\n" +
                    "inner join " + universe_name + "_players c\n" +
                    "on a.player_id = c.player_id\n" +
                    "inner join " + universe_name + "_teams d\n" +
                    "on a.team_id = d.team_id\n" +
                    "where a.split_id = 1 and a.league_id = " + league_id + " and a.year = " + current_year + "\n";
            if (team_id == -1) {
                sql += "and " + qualifier + "\n";
            }
            sql += "and a.split_id = 1\n";
            if (team_id > -1) {
                sql += "and a.team_id = " + team_id + "\n";
            } else {
                sql += "and a.team_id in\n" +
                        "(select team_id from " + universe_name + "_teams where league_id = " + league_id + " and sub_league_id = " + sub_league_id + ")\n";
            }
            if (team_id == -1) {
                sql += "and " + qualifier + "\n" +
                        "order by " + stat + " " + sortOrder;
            } else {
                sql += "order by " + stat + " " + sortOrder;
            }
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    StatLeader statLeader = new StatLeader();
                    statLeader.setStatistic(display);
                    statLeader.setFirstName(cursor.getString(1));
                    statLeader.setLastName(cursor.getString(2));
                    statLeader.setTeamAbbr(cursor.getString(5));
                    statLeader.setPlayerID(cursor.getInt(0));
                    if (team_id > -1) {
                        statLeader.setDisplayName(cursor.getString(1) + " " + cursor.getString(2));
                    } else {
                        statLeader.setDisplayName(cursor.getString(1) + " " + cursor.getString(2) +
                                ", " + cursor.getString(5));
                    }
                    BigDecimal tmpStat;
                    try {
                        tmpStat = new BigDecimal(cursor.getString(6));
                    } catch (NumberFormatException e) {
                        tmpStat = new BigDecimal(Float.toString(0));
                    }
                    tmpStat = tmpStat.setScale(roundTo,RoundingMode.HALF_UP);
                    if (stripZero) {
                        statLeader.setValue(String.valueOf(tmpStat).replaceFirst("^0.", "."));
                    } else {
                        statLeader.setValue(String.valueOf(tmpStat));
                    }
                    statLeaders.add(statLeader);
                } while (cursor.moveToNext());
            } else {
                StatLeader statLeader = new StatLeader();
                statLeader.setStatistic(stat);
                statLeader.setFirstName("");
                statLeader.setLastName("");
                statLeader.setTeamAbbr("");
                statLeader.setPlayerID(-1);
                statLeader.setDisplayName("No Qualifiers");
                statLeader.setValue("");
                statLeaders.add(statLeader);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return statLeaders;
    }

    public List<StatLeader> getCountingStatList(String universe_name, int league_id, Integer sub_league_id, int current_year, String stat, String table, int team_id) {
        List<StatLeader> statLeaders = new ArrayList<StatLeader>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (stat.equals("pct")) {
            stat += "a";
        }
        try {
            String sql = "select a.player_id, c.first_name, c.last_name, d.name, d.nickname, \n" +
                    "d.abbr, a."+stat+"\n" +
                    "from "+universe_name+"_players_career_"+table+"_stats a\n" +
                    "inner join " + universe_name + "_team_record b\n" +
                    "on a.team_id = b.team_id\n" +
                    "inner join " + universe_name + "_players c\n" +
                    "on a.player_id = c.player_id\n" +
                    "inner join " + universe_name + "_teams d\n" +
                    "on a.team_id = d.team_id\n" +
                    "where a.split_id = 1 and a.league_id = " + league_id + " and a.year = " + current_year + "\n" +
                    "and a.split_id = 1\n";
            if (team_id > -1) {
                sql += "and a.team_id = " + team_id + "\n";
            } else {
                sql += "and a.team_id in\n" +
                        "(select team_id from " + universe_name + "_teams where league_id = " + league_id + " and sub_league_id = " + sub_league_id + ")\n";
            }
            sql += "order by a." + stat + " desc";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    StatLeader statLeader = new StatLeader();
                    statLeader.setStatistic(stat.toUpperCase());
                    statLeader.setPlayerID(cursor.getInt(0));
                    statLeader.setFirstName(cursor.getString(1));
                    statLeader.setLastName(cursor.getString(2));
                    statLeader.setTeamAbbr(cursor.getString(5));
                    statLeader.setValue(cursor.getString(6));
                    if (team_id > -1) {
                        statLeader.setDisplayName(cursor.getString(1) + " " + cursor.getString(2));
                    } else {
                        statLeader.setDisplayName(cursor.getString(1) + " " + cursor.getString(2) +
                                ", " + cursor.getString(5));
                    }
                    statLeaders.add(statLeader);
                } while (cursor.moveToNext());
            } else {
                StatLeader statLeader = new StatLeader();
                statLeader.setStatistic(stat);
                statLeader.setFirstName("");
                statLeader.setLastName("");
                statLeader.setTeamAbbr("");
                statLeader.setPlayerID(-1);
                statLeader.setDisplayName("No Qualifiers");
                statLeader.setValue("");
                statLeaders.add(statLeader);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return statLeaders;
    }

    public StatLeader getCountingStatLeader(String universe_name, int league_id, Integer sub_league_id, int current_year, String stat, String table, int team_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        StatLeader statLeader = new StatLeader();
        Cursor cursor = null;
        try {
            String sql = "select a.player_id, c.first_name, c.last_name, d.name, d.nickname, \n" +
                    "d.abbr, a."+stat+"\n" +
                    "from "+universe_name+"_players_career_"+table+"_stats a\n" +
                    "inner join "+universe_name+"_players c\n" +
                    "on a.player_id = c.player_id\n" +
                    "inner join "+universe_name+"_teams d\n" +
                    "on a.team_id = d.team_id\n" +
                    "where a.split_id = 1 and a.league_id = "+league_id+" and a.year = "+current_year+"\n" +
                    "and a.split_id = 1\n" +
                    "and a.split_id = 1\n";
            if (team_id > -1) {
                sql += "and a.team_id = "+team_id+"\n";
            } else {
                sql += "and a.team_id in\n" +
                        "(select team_id from "+universe_name+"_teams where league_id = "+league_id+" and sub_league_id = "+sub_league_id+")\n";
            }
            sql +=  "and a."+stat+" in (\n" +
                    "\tselect max ("+stat+")\n" +
                    "\tfrom "+universe_name+"_players_career_"+table+"_stats a\n" +
                    "\twhere a.split_id = 1 and a.league_id = "+league_id+" and a.year = "+current_year+"\n";
            if (team_id > -1) {
                sql += "\tand a.team_id = "+team_id+"\n";
            } else {
                sql += "\tand a.team_id in\n" +
                        "\t(select team_id from "+universe_name+"_teams where league_id = "+league_id+" and sub_league_id = "+sub_league_id+")\n";
            }
            sql +=
                    "\torder by "+stat+" desc\n" +
                    ")\n" +
                    "order by "+stat+" desc";
            cursor = db.rawQuery(sql, null);
            if (cursor.getCount() > 1) {
                statLeader.setStatistic(stat.toUpperCase());
                statLeader.setFirstName("");
                statLeader.setLastName("");
                statLeader.setTeamAbbr("");
                statLeader.setPlayerID(-1);
                statLeader.setDisplayName(cursor.getCount() + " players tied with");
                if (cursor.moveToFirst()) {
                    do {
                        statLeader.setValue(cursor.getString(6));
                    } while (cursor.moveToNext());
                }
            } else {
                if (cursor.moveToFirst()) {
                    do {
                        statLeader.setStatistic(stat.toUpperCase());
                        statLeader.setPlayerID(cursor.getInt(0));
                        statLeader.setFirstName(cursor.getString(1));
                        statLeader.setLastName(cursor.getString(2));
                        statLeader.setTeamAbbr(cursor.getString(5));
                        statLeader.setValue(cursor.getString(6));
                        if (team_id == -1) {
                            statLeader.setDisplayName(cursor.getString(1)+ " "+cursor.getString(2)+
                                    ", "+ cursor.getString(5));
                        } else {
                            statLeader.setDisplayName(cursor.getString(1)+ " "+cursor.getString(2));
                        }
                    } while (cursor.moveToNext());
                } else {
                    statLeader.setStatistic(stat.toUpperCase());
                    statLeader.setFirstName("");
                    statLeader.setLastName("");
                    statLeader.setTeamAbbr("");
                    statLeader.setPlayerID(-1);
                    statLeader.setDisplayName("No Qualifiers");
                    statLeader.setValue("");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return statLeader;
    }

    public Ballpark getBallpark(String universe_name, int team_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        BigDecimal tmp;
        Ballpark ballpark = new Ballpark();
        try {
            String sql = "select a.name, a.distances0 as LL, a.distances1 as LF, a.distances2 as LC,\n" +
                    "a.distances3 as CF, a.distances4 as RC, a.distances5 as RF, a.distances6 as RL,\n" +
                    "a.avg, a.avg_l, a.avg_r, a.d, a.t, a.hr, a.hr_l, a.hr_r, b.logo_file_name, a.capacity\n" +
                    "from "+universe_name+"_parks a\n" +
                    "inner join "+universe_name+"_teams b\n" +
                    "on a.park_id = b.park_id\n" +
                    "where b.team_id = "+team_id;
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    ballpark.setName(cursor.getString(0));
                    ballpark.setLeftLine(cursor.getInt(1));
                    ballpark.setLeftField(cursor.getInt(2));
                    ballpark.setLeftCenter(cursor.getInt(3));
                    ballpark.setCenterField(cursor.getInt(4));
                    ballpark.setRightCenter(cursor.getInt(5));
                    ballpark.setRightField(cursor.getInt(6));
                    ballpark.setRightLine(cursor.getInt(7));
                    tmp = new BigDecimal(Float.toString(cursor.getFloat(8)));
                    tmp = tmp.setScale(3, RoundingMode.HALF_UP);
                    ballpark.setAvg(String.valueOf(tmp).replaceFirst("^0.", "."));
                    tmp = new BigDecimal(Float.toString(cursor.getFloat(9)));
                    tmp = tmp.setScale(3, RoundingMode.HALF_UP);
                    ballpark.setAvg_l(String.valueOf(tmp).replaceFirst("^0.", "."));
                    tmp = new BigDecimal(Float.toString(cursor.getFloat(10)));
                    tmp = tmp.setScale(3, RoundingMode.HALF_UP);
                    ballpark.setAvg_r(String.valueOf(tmp).replaceFirst("^0.", "."));
                    tmp = new BigDecimal(Float.toString(cursor.getFloat(11)));
                    tmp = tmp.setScale(3, RoundingMode.HALF_UP);
                    ballpark.setD(String.valueOf(tmp).replaceFirst("^0.", "."));
                    tmp = new BigDecimal(Float.toString(cursor.getFloat(12)));
                    tmp = tmp.setScale(3, RoundingMode.HALF_UP);
                    ballpark.setT(String.valueOf(tmp).replaceFirst("^0.", "."));
                    tmp = new BigDecimal(Float.toString(cursor.getFloat(13)));
                    tmp = tmp.setScale(3, RoundingMode.HALF_UP);
                    ballpark.setHr(String.valueOf(tmp).replaceFirst("^0.", "."));
                    tmp = new BigDecimal(Float.toString(cursor.getFloat(14)));
                    tmp = tmp.setScale(3, RoundingMode.HALF_UP);
                    ballpark.setHr_l(String.valueOf(tmp).replaceFirst("^0.", "."));
                    tmp = new BigDecimal(Float.toString(cursor.getFloat(15)));
                    tmp = tmp.setScale(3, RoundingMode.HALF_UP);
                    ballpark.setHr_r(String.valueOf(tmp).replaceFirst("^0.", "."));
                    ballpark.setTeam_logo(cursor.getString(16));
                    ballpark.setCapacity(NumberFormat.getIntegerInstance().format(cursor.getInt(17)));
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return ballpark;
    }

    public List<Champion> getChampions(String universe_name, int league_id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Champion> champions = new ArrayList<>();
        Cursor cursor = null;
        BigDecimal tmp;
        try {
            String sql = "select a.team_id, a.year, a.name, a.nickname, \n" +
                    "b.first_name, b.last_name,\n" +
                    "c.w, c.l, c.pct,\n" +
                    "d.avg, d.r, d.hr, d.sb,\n" +
                    "e.era, e.sho, e.k\n" +
                    "from "+universe_name+"_team_history a\n" +
                    "left join "+universe_name+"_human_managers b\n" +
                    "on a.manager_id = b.human_manager_id\n" +
                    "left join "+universe_name+"_team_history_record c\n" +
                    "on a.team_id = c.team_id and a.year = c.year\n" +
                    "left join "+universe_name+"_team_history_batting_stats d\n" +
                    "on a.team_id = d.team_id and a.year = d.year\n" +
                    "left join "+universe_name+"_team_history_pitching_stats e\n" +
                    "on a.team_id = e.team_id and a.year = e.year\n" +
                    "where a.won_playoffs = 1\n" +
                    "and a.league_id = "+league_id+"\n" +
                    "order by a.year asc";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    Champion champion = new Champion();
                    champion.setTeamID(cursor.getInt(0));
                    champion.setYear(cursor.getInt(1));
                    champion.setName(cursor.getString(2));
                    champion.setNickname(cursor.getString(3));
                    champion.setFirstName(cursor.getString(4));
                    champion.setLastName(cursor.getString(5));
                    champion.setW(cursor.getInt(6));
                    champion.setL(cursor.getInt(7));
                    tmp = new BigDecimal(Float.toString(cursor.getFloat(8)));
                    tmp = tmp.setScale(3, RoundingMode.HALF_UP);
                    champion.setPct(String.valueOf(tmp).replaceFirst("^0.", "."));
                    tmp = new BigDecimal(Float.toString(cursor.getFloat(9)));
                    tmp = tmp.setScale(3, RoundingMode.HALF_UP);
                    champion.setAvg(String.valueOf(tmp).replaceFirst("^0.", "."));
                    champion.setR(cursor.getInt(10));
                    champion.setHr(cursor.getInt(11));
                    champion.setSb(cursor.getInt(12));
                    tmp = new BigDecimal(Float.toString(cursor.getFloat(13)));
                    tmp = tmp.setScale(2, RoundingMode.HALF_UP);
                    champion.setEra(String.valueOf(tmp));
                    champion.setSho(cursor.getInt(14));
                    champion.setK(cursor.getInt(15));
                    champions.add(champion);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return champions;
    }

    public Manager getManager(String universe_name, int league_id, int manager_id) {
        Manager manager = new Manager();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        Cursor cursor2 = null;
        try {
            String sql = "select a.human_manager_id, a.first_name, a.last_name, sum(b.w), sum(b.l), count(b.year), a.retired\n" +
                    "from "+universe_name+"_human_managers a\n" +
                    "inner join "+universe_name+"_human_manager_history_record b\n" +
                    "on a.human_manager_id = b.human_manager_id and b.league_id = "+league_id+"\n" +
                    "where a.human_manager_id = "+manager_id+"\n" +
                    "group by a.human_manager_id";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    manager.setHumanManagerID(cursor.getInt(0));
                    manager.setFirstName(cursor.getString(1));
                    manager.setLastName(cursor.getString(2));
                    manager.setW(cursor.getInt(3));
                    manager.setL(cursor.getInt(4));
                    manager.setYears(cursor.getInt(5));
                    String sql2 = "select sum(won_playoffs) from "+universe_name+"_human_manager_history\n" +
                            "where human_manager_id = "+cursor.getInt(0)+"\n" +
                            "and league_id = "+league_id;
                    cursor2 = db.rawQuery(sql2, null);
                    if (cursor2.moveToFirst()) {
                        do {
                            manager.setChampionships(cursor2.getInt(0));
                        } while (cursor2.moveToNext());
                        cursor2.close();
                    } else {
                        manager.setChampionships(cursor2.getInt(0));
                    }
                    manager.setRetired(cursor.getInt(6));
                    Float wins = cursor.getFloat(3);
                    Float losses = cursor.getFloat(4);
                    BigDecimal pct;
                    try {
                        pct = new BigDecimal(Float.toString(wins / (wins + losses)));
                    } catch (NumberFormatException e) {
                        pct = new BigDecimal(Float.toString(0));
                    }
                    pct = pct.setScale(3,RoundingMode.HALF_UP);
                    manager.setPct(String.valueOf(pct).replaceFirst("^0.", "."));
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            if (cursor2 != null && !cursor2.isClosed())
                cursor2.close();
            
        }
        return manager;
    }

    public List<Manager> getManagers(String universe_name, int league_id, int retired) {
        List<Manager> managers = new ArrayList<Manager>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        Cursor cursor2 = null;
        try {
            String sql = "select a.human_manager_id, a.first_name, a.last_name, sum(b.w), sum(b.l), count(b.year), a.retired\n" +
                    "from "+universe_name+"_human_managers a\n" +
                    "inner join "+universe_name+"_human_manager_history_record b\n" +
                    "on a.human_manager_id = b.human_manager_id and b.league_id = "+league_id+"\n" +
                    "where a.retired = "+retired+"\n" +
                    "group by a.human_manager_id\n" +
                    "order by a.first_name, a.last_name";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    Manager manager = new Manager();
                    manager.setHumanManagerID(cursor.getInt(0));
                    manager.setFirstName(cursor.getString(1));
                    manager.setLastName(cursor.getString(2));
                    manager.setW(cursor.getInt(3));
                    manager.setL(cursor.getInt(4));
                    manager.setYears(cursor.getInt(5));
                    String sql2 = "select sum(won_playoffs) from "+universe_name+"_human_manager_history\n" +
                            "where human_manager_id = "+cursor.getInt(0)+"\n" +
                            "and league_id = "+league_id;
                    cursor2 = db.rawQuery(sql, null);
                    if (cursor2.moveToFirst()) {
                        do {
                            manager.setChampionships(cursor2.getInt(0));
                        } while (cursor2.moveToNext());
                        cursor2.close();
                    } else {
                        manager.setChampionships(cursor2.getInt(0));
                    }
                    manager.setRetired(cursor.getInt(6));
                    Float wins = cursor.getFloat(3);
                    Float losses = cursor.getFloat(4);
                    BigDecimal pct;
                    try {
                        pct = new BigDecimal(Float.toString(wins / (wins + losses)));
                    } catch (NumberFormatException e) {
                        pct = new BigDecimal(Float.toString(0));
                    }
                    pct = pct.setScale(3,RoundingMode.HALF_UP);
                    manager.setPct(String.valueOf(pct).replaceFirst("^0.", "."));
                    managers.add(manager);
                } while (cursor.moveToNext());

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            if (cursor2 != null && !cursor2.isClosed())
                cursor2.close();
            
        }
        return managers;
    }

    public List<ManagerStatLine> managerHistory(String universe_name, int manager_id) {
        List<ManagerStatLine> managerHistory = new ArrayList<ManagerStatLine>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            String sql = "select a.year, b.name, b.nickname, b.abbr, f.abbr, a.w, a.l, a.pct,\n" +
                    "c.avg, c.hr, c.sb, d.era, d.k, e.made_playoffs, e.won_playoffs\n" +
                    "from "+universe_name+"_human_manager_history_record a\n" +
                    "inner join "+universe_name+"_team_history b\n" +
                    "on a.team_id = b.team_id and a.year = b.year\n" +
                    "inner join "+universe_name+"_human_manager_history_batting_stats c\n" +
                    "on a.team_id = c.team_id and a.year = c.year\n" +
                    "inner join "+universe_name+"_human_manager_history_pitching_stats d\n" +
                    "on a.team_id = d.team_id and a.year = d.year\n" +
                    "inner join "+universe_name+"_human_manager_history e\n" +
                    "on a.team_id = e.team_id and a.year = e.year\n" +
                    "left join "+universe_name+"_leagues f\n" +
                    "on a.league_id = f.league_id\n" +
                    "where a.human_manager_id = "+manager_id+"\n" +
                    "group by a.year, b.name";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    ManagerStatLine line = new ManagerStatLine();
                    line.setYear(cursor.getInt(0));
                    line.setName(cursor.getString(1));
                    line.setNickname(cursor.getString(2));
                    line.setAbbr(cursor.getString(3));
                    line.setLeagueAbbr(cursor.getString(4));
                    line.setW(cursor.getInt(5));
                    line.setL(cursor.getInt(6));
                    BigDecimal pct;
                    try {
                        pct = new BigDecimal(Float.toString(cursor.getFloat(7)));
                    } catch (NumberFormatException e) {
                        pct = new BigDecimal(Float.toString(0));
                    }
                    pct = pct.setScale(3,RoundingMode.HALF_UP);
                    line.setPct(String.valueOf(pct).replaceFirst("^0.", "."));
                    BigDecimal avg;
                    try {
                        avg = new BigDecimal(Float.toString(cursor.getFloat(8)));
                    } catch (NumberFormatException e) {
                        avg = new BigDecimal(Float.toString(0));
                    }
                    avg = avg.setScale(3,RoundingMode.HALF_UP);
                    line.setAvg(String.valueOf(avg).replaceFirst("^0.", "."));
                    line.setHr(cursor.getInt(9));
                    line.setSb(cursor.getInt(10));
                    BigDecimal era;
                    try {
                        era = new BigDecimal(Float.toString(cursor.getFloat(11)));
                    } catch (NumberFormatException e) {
                        era = new BigDecimal(Float.toString(0));
                    }
                    era = era.setScale(2,RoundingMode.HALF_UP);
                    line.setEra(String.valueOf(era));
                    line.setK(cursor.getInt(12));
                    line.setMadePlayoffs(cursor.getInt(13));
                    line.setWonPlayoffs(cursor.getInt(14));
                    managerHistory.add(line);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return managerHistory;
    }

    public List<ManagerStatLine> teamHistory(String universe_name, int team_id) {
        List<ManagerStatLine> teamHistory = new ArrayList<ManagerStatLine>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            String sql = "select a.year, b.name, b.nickname, b.abbr, f.abbr, a.w, a.l, a.pct,\n" +
                    "c.avg, c.hr, c.sb, d.era, d.k, e.made_playoffs, e.won_playoffs\n" +
                    "from "+universe_name+"_team_history_record a\n" +
                    "inner join "+universe_name+"_team_history b\n" +
                    "on a.team_id = b.team_id and a.year = b.year\n" +
                    "inner join "+universe_name+"_team_history_batting_stats c\n" +
                    "on a.team_id = c.team_id and a.year = c.year\n" +
                    "inner join "+universe_name+"_team_history_pitching_stats d\n" +
                    "on a.team_id = d.team_id and a.year = d.year\n" +
                    "inner join "+universe_name+"_team_history e\n" +
                    "on a.team_id = e.team_id and a.year = e.year\n" +
                    "left join "+universe_name+"_leagues f\n" +
                    "on a.league_id = f.league_id\n" +
                    "where a.team_id = "+team_id+"\n" +
                    "group by a.year, b.name";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    ManagerStatLine line = new ManagerStatLine();
                    line.setYear(cursor.getInt(0));
                    line.setName(cursor.getString(1));
                    line.setNickname(cursor.getString(2));
                    line.setAbbr(cursor.getString(3));
                    line.setLeagueAbbr(cursor.getString(4));
                    line.setW(cursor.getInt(5));
                    line.setL(cursor.getInt(6));
                    BigDecimal pct;
                    try {
                        pct = new BigDecimal(Float.toString(cursor.getFloat(7)));
                    } catch (NumberFormatException e) {
                        pct = new BigDecimal(Float.toString(0));
                    }
                    pct = pct.setScale(3,RoundingMode.HALF_UP);
                    line.setPct(String.valueOf(pct).replaceFirst("^0.", "."));
                    BigDecimal avg;
                    try {
                        avg = new BigDecimal(Float.toString(cursor.getFloat(8)));
                    } catch (NumberFormatException e) {
                        avg = new BigDecimal(Float.toString(0));
                    }
                    avg = avg.setScale(3,RoundingMode.HALF_UP);
                    line.setAvg(String.valueOf(avg).replaceFirst("^0.", "."));
                    line.setHr(cursor.getInt(9));
                    line.setSb(cursor.getInt(10));
                    BigDecimal era;
                    try {
                        era = new BigDecimal(Float.toString(cursor.getFloat(11)));
                    } catch (NumberFormatException e) {
                        era = new BigDecimal(Float.toString(0));
                    }
                    era = era.setScale(2,RoundingMode.HALF_UP);
                    line.setEra(String.valueOf(era));
                    line.setK(cursor.getInt(12));
                    line.setMadePlayoffs(cursor.getInt(13));
                    line.setWonPlayoffs(cursor.getInt(14));
                    teamHistory.add(line);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return teamHistory;
    }

    public List<ScheduleLineGame> getTeamSchedule(String universe_name, int team_id) {
        List<ScheduleLineGame> schedule = new ArrayList<ScheduleLineGame>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            String sql = "select a.game_id, a.home_team, b.name, b.nickname, b.abbr, b.logo_file_name,\n" +
                    "a.away_team, c.name, c.nickname, c.abbr, c.logo_file_name,\n" +
                    "a.played, a.date, a.time, a.innings, a.runs0, a.runs1, b.background_color_id, b.text_color_id,\n" +
                    "c.background_color_id, c.text_color_id, a.game_type\n" +
                    "from "+universe_name+"_games a\n" +
                    "inner join "+universe_name+"_teams b\n" +
                    "on a.home_team = b.team_id\n" +
                    "inner join "+universe_name+"_teams c\n" +
                    "on a.away_team = c.team_id\n" +
                    "where home_team = "+team_id+" or away_team = "+team_id+" order by date asc";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    ScheduleLineGame game = new ScheduleLineGame();
                    game.setGameID(cursor.getInt(0));
                    boolean homeTeam = cursor.getInt(1) == team_id ? true : false;
                    boolean played = cursor.getInt(11) == 1 ? true : false;
                    boolean win = false;
                    if (homeTeam) {
                        win = (cursor.getInt(16) > cursor.getInt(15)) ? true : false;
                        game.setOpponent(cursor.getString(7));
                        game.setOpponentLogo(cursor.getString(10));
                        game.setBackgroundColor(cursor.getString(19));
                        game.setTextColor(cursor.getString(20));
                        if (played) {
                            game.setResult(cursor.getInt(16) + "-" + cursor.getInt(15));
                        } else {
                            game.setResult(cursor.getString(13));
                        }

                    } else {
                        win = (cursor.getInt(15) > cursor.getInt(16)) ? true : false;
                        game.setOpponent(cursor.getString(2));
                        game.setOpponentLogo(cursor.getString(5));
                        game.setBackgroundColor(cursor.getString(17));
                        game.setTextColor(cursor.getString(18));
                        if (played) {
                            game.setResult(cursor.getInt(15) + "-"+cursor.getInt(16));
                        } else {
                            game.setResult(cursor.getString(13));
                        }
                    }
                    game.setGameDate(cursor.getString(12));
                    game.setGameTime(cursor.getString(13));
                    game.setGameType(cursor.getInt(21));
                    game.setHome(homeTeam);
                    game.setWin(win);
                    game.setPlayed(played);
                    schedule.add(game);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return schedule;
    }

    public int getGamesPlayed(String universe_name, int team_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        int position = 0;
        try {
            String sql = "select sum(played) from "+universe_name+"_games\n" +
                    "where home_team = "+team_id+" or away_team = "+team_id;
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    position = cursor.getInt(0);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return position;
    }

    public List<Contract> getTeamContracts(String universe_name, int team_id, int league_id) {
        List<Contract> contracts = new ArrayList<Contract>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            String sql = "select \n" +
                    "a.player_id, b.first_name, b.last_name, b.throws, b.position,\n" +
                    "case\n" +
                    "\twhen a.current_year = 0 then a.salary0\n" +
                    "\twhen a.current_year = 1 then a.salary1\n" +
                    "\twhen a.current_year = 2 then a.salary2\n" +
                    "\twhen a.current_year = 3 then a.salary3\n" +
                    "\twhen a.current_year = 4 then a.salary4\n" +
                    "\twhen a.current_year = 5 then a.salary5\n" +
                    "\twhen a.current_year = 6 then a.salary6\n" +
                    "\twhen a.current_year = 7 then a.salary7\n" +
                    "\twhen a.current_year = 8 then a.salary8\n" +
                    "\twhen a.current_year = 9 then a.salary9\n" +
                    "end as current_salary,\n" +
                    "a.years, a.current_year,\n" +
                    "(a.salary0 + a.salary1 + a.salary2 + a.salary3 + a.salary4 + a.salary5 + a.salary6 + a.salary7 + a.salary8 + a.salary9) as total, \n" +
                    "a.season_year,\n" +
                    "a.salary0, a.salary1, a.salary2, a.salary3, a.salary4, a.salary5, a.salary6, a.salary7, a.salary8, a.salary9,\n" +
                    "a.no_trade, \n" +
                    "a.last_year_team_option, a.last_year_option_buyout, a.last_year_player_option, a.last_year_vesting_option,\n" +
                    " a.next_last_year_team_option, a.next_last_year_option_buyout, a.next_last_year_player_option, a.next_last_year_vesting_option,\n" +
                    "a.minimum_pa, a.minimum_pa_bonus, a.minimum_ip, a.minimum_ip_bonus,\n" +
                    "a.mvp_bonus, a.cyyoung_bonus, a.allstar_bonus, c.mvp_award_name, c.pitcher_award_name\n" +
                    "from "+universe_name+"_players_contract a\n" +
                    "inner join "+universe_name+"_players b\n" +
                    "on a.player_id = b.player_id\n" +
                    "inner join "+universe_name+"_leagues c\n" +
                    "on c.league_id = "+league_id+"\n" +
                    "where (a.team_id = "+team_id+" or\n" +
                    "a.team_id in \n" +
                    "(select affiliated_team_id from "+universe_name+"_team_affiliations where team_id = "+team_id+")) and a.is_major = 1\n" +
                    "order by current_salary desc";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    Contract c = new Contract();
                    c.setPlayerID(cursor.getInt(0));
                    c.setFirstName(cursor.getString(1));
                    c.setLastName(cursor.getString(2));
                    c.setThrowingHand(constants.throwing[cursor.getInt(3)]);
                    c.setPosition(constants.positions[cursor.getInt(4)]);
                    c.setCurrentSalary(cursor.getInt(5));
                    c.setContractLength(cursor.getInt(6));
                    c.setCurrentContractYear(cursor.getInt(7));
                    c.setTotalValue(cursor.getInt(8));
                    c.setSignedIn(cursor.getInt(9));
                    c.setSalary0(cursor.getInt(10));
                    c.setSalary1(cursor.getInt(11));
                    c.setSalary2(cursor.getInt(12));
                    c.setSalary3(cursor.getInt(13));
                    c.setSalary4(cursor.getInt(14));
                    c.setSalary5(cursor.getInt(15));
                    c.setSalary6(cursor.getInt(16));
                    c.setSalary7(cursor.getInt(17));
                    c.setSalary8(cursor.getInt(18));
                    c.setSalary9(cursor.getInt(19));
                    c.setNoTrade(cursor.getInt(20));
                    c.setLastYearTeamOption(cursor.getInt(21));
                    c.setLastYearOptionBuyout(cursor.getInt(22));
                    c.setLastYearPlayerOption(cursor.getInt(23));
                    c.setLastYearVestingOption(cursor.getInt(24));
                    c.setNextLastYearTeamOption(cursor.getInt(25));
                    c.setNextLastYearOptionBuyout(cursor.getInt(26));
                    c.setNextLastYearPlayerOption(cursor.getInt(27));
                    c.setNextLastYearVestingOption(cursor.getInt(28));
                    c.setMinimumPA(cursor.getInt(29));
                    c.setMinimumPABonus(cursor.getInt(30));
                    c.setMinimumIP(cursor.getInt(31));
                    c.setMinimumIPBonus(cursor.getInt(32));
                    c.setMvpBonus(cursor.getInt(33));
                    c.setPoyBonus(cursor.getInt(34));
                    c.setAsBonus(cursor.getInt(35));
                    c.setMvpAwardName(cursor.getString(36));
                    c.setPoyAwardName(cursor.getString(37));
                    contracts.add(c);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return contracts;
    }

    public Contract getPlayerContract(String universe_name, int player_id, int league_id) {
        Contract c = new Contract();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            String sql = "select \n" +
                    "a.player_id, b.first_name, b.last_name, b.throws, b.position,\n" +
                    "case\n" +
                    "\twhen a.current_year = 0 then a.salary0\n" +
                    "\twhen a.current_year = 1 then a.salary1\n" +
                    "\twhen a.current_year = 2 then a.salary2\n" +
                    "\twhen a.current_year = 3 then a.salary3\n" +
                    "\twhen a.current_year = 4 then a.salary4\n" +
                    "\twhen a.current_year = 5 then a.salary5\n" +
                    "\twhen a.current_year = 6 then a.salary6\n" +
                    "\twhen a.current_year = 7 then a.salary7\n" +
                    "\twhen a.current_year = 8 then a.salary8\n" +
                    "\twhen a.current_year = 9 then a.salary9\n" +
                    "end as current_salary,\n" +
                    "a.years, a.current_year,\n" +
                    "(a.salary0 + a.salary1 + a.salary2 + a.salary3 + a.salary4 + a.salary5 + a.salary6 + a.salary7 + a.salary8 + a.salary9) as total, \n" +
                    "a.season_year,\n" +
                    "a.salary0, a.salary1, a.salary2, a.salary3, a.salary4, a.salary5, a.salary6, a.salary7, a.salary8, a.salary9,\n" +
                    "a.no_trade, \n" +
                    "a.last_year_team_option, a.last_year_option_buyout, a.last_year_player_option, a.last_year_vesting_option,\n" +
                    " a.next_last_year_team_option, a.next_last_year_option_buyout, a.next_last_year_player_option, a.next_last_year_vesting_option,\n" +
                    "a.minimum_pa, a.minimum_pa_bonus, a.minimum_ip, a.minimum_ip_bonus,\n" +
                    "a.mvp_bonus, a.cyyoung_bonus, a.allstar_bonus, c.mvp_award_name, c.pitcher_award_name, a.is_major\n" +
                    "from "+universe_name+"_players_contract a\n" +
                    "inner join "+universe_name+"_players b\n" +
                    "on a.player_id = b.player_id\n" +
                    "inner join "+universe_name+"_leagues c\n" +
                    "on c.league_id = "+league_id+"\n" +
                    "where a.player_id = "+player_id+"\n" +
                    "order by current_salary desc";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    c.setPlayerID(cursor.getInt(0));
                    c.setFirstName(cursor.getString(1));
                    c.setLastName(cursor.getString(2));
                    c.setThrowingHand(constants.throwing[cursor.getInt(3)]);
                    c.setPosition(constants.positions[cursor.getInt(4)]);
                    c.setCurrentSalary(cursor.getInt(5));
                    c.setContractLength(cursor.getInt(6));
                    c.setCurrentContractYear(cursor.getInt(7));
                    c.setTotalValue(cursor.getInt(8));
                    c.setSignedIn(cursor.getInt(9));
                    c.setSalary0(cursor.getInt(10));
                    c.setSalary1(cursor.getInt(11));
                    c.setSalary2(cursor.getInt(12));
                    c.setSalary3(cursor.getInt(13));
                    c.setSalary4(cursor.getInt(14));
                    c.setSalary5(cursor.getInt(15));
                    c.setSalary6(cursor.getInt(16));
                    c.setSalary7(cursor.getInt(17));
                    c.setSalary8(cursor.getInt(18));
                    c.setSalary9(cursor.getInt(19));
                    c.setNoTrade(cursor.getInt(20));
                    c.setLastYearTeamOption(cursor.getInt(21));
                    c.setLastYearOptionBuyout(cursor.getInt(22));
                    c.setLastYearPlayerOption(cursor.getInt(23));
                    c.setLastYearVestingOption(cursor.getInt(24));
                    c.setNextLastYearTeamOption(cursor.getInt(25));
                    c.setNextLastYearOptionBuyout(cursor.getInt(26));
                    c.setNextLastYearPlayerOption(cursor.getInt(27));
                    c.setNextLastYearVestingOption(cursor.getInt(28));
                    c.setMinimumPA(cursor.getInt(29));
                    c.setMinimumPABonus(cursor.getInt(30));
                    c.setMinimumIP(cursor.getInt(31));
                    c.setMinimumIPBonus(cursor.getInt(32));
                    c.setMvpBonus(cursor.getInt(33));
                    c.setPoyBonus(cursor.getInt(34));
                    c.setAsBonus(cursor.getInt(35));
                    c.setMvpAwardName(cursor.getString(36));
                    c.setPoyAwardName(cursor.getString(37));
                    c.setMinorLeague(cursor.getInt(38) == 0 ? true : false);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return c;
    }

    public List<NewsArticle> getLeagueNews(String universe_name, int league_id, int qty) {
        List<NewsArticle> articles = new ArrayList<NewsArticle>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            String sql = "select message_id, subject, player_id_0, date, message_type, recipient_id\n" +
                    "from "+universe_name+"_messages where league_id_0 = "+league_id+" and recipient_id < 1 and player_id_0 > 0\n" +
                    "order by date desc\n" +
                    "limit " + qty;
            cursor = db.rawQuery(sql, null);

            if (cursor.moveToFirst()) {

                do {
                    final NewsArticle a = new NewsArticle();
                    a.setMessageID(cursor.getInt(0));
                    a.setSubject(cursor.getString(1));
                    a.setPlayerID(cursor.getInt(2));
                    a.setDate(cursor.getString(3));
                    a.setMessageType(cursor.getInt(4));
                    a.setRecipientID(cursor.getInt(5));
                    a.setLeagueID(league_id);
                    articles.add(a);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            
        }
        return articles;
    }

    public Date getMinMaxGame (String universe_name, int league_id, String minMax) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        Date date = null;
        try {
            String sql = "select "+minMax+"(date) from "+universe_name+"_games where league_id = "+league_id;
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    date = new SimpleDateFormat("yyyy/MM/dd").parse(cursor.getString(0));
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();

        }
        return date;
    }

    private JSONObject getJsonFromResource(Context context) {
        JSONObject json = null;
        InputStream is = context.getResources().openRawResource(R.raw._csv);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
            json = new JSONObject(writer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return json;
    }

    private Date tryParse(String dateString) {
        List<String> formatStrings = Arrays.asList("y-M-d", "M/d/y", "yyyy/M/d");
        for (String formatString : formatStrings) {
            try {
                return new SimpleDateFormat(formatString).parse(dateString);
            }
            catch (ParseException e) {}
        }
        return null;
    }

    private Integer rowCount(File file) {
        int lineCount = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                lineCount++;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return lineCount;
    }

    public int daysBetween(Date d1, Date d2) {
        return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }
}
