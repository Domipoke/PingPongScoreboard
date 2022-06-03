package it.domipoke.pingpongscoreboard;

import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

class Parser {
    public Parser() {}

    public static String read(File file) throws FileNotFoundException {
        Scanner fs = new Scanner(file);
        StringBuilder sb = new StringBuilder();
        while (fs.hasNextLine()) {
            sb.append(fs.nextLine());
        }
        String ct = sb.toString();
        //System.out.println("Read Text: "+ct);
        return ct;
    }
    public static List parseString(String ct) {
        try {
            JSONObject jo = new JSONObject(ct);
            Object type = jo.get("type");
            switch (type.toString().toLowerCase(Locale.ROOT)) {
                case "player":
                    List<Player> pls = new ArrayList<Player>();
                    JSONArray ja = new JSONArray(jo.get("data").toString());

                    for (int i = 0;i<ja.length();i++) {
                        pls.add(parsePlayer(ja.get(i).toString()));
                    }
                    return pls;
                case "game":
                    List<Game> games = new ArrayList<Game>();
                    JSONArray gja = new JSONArray(jo.get("data").toString());
                    Utils.Log(String.valueOf(gja.length()));
                    for (int i = 0;i<gja.length();i++) {
                        games.add(parseGame(gja.get(i).toString()));
                    }
                    return games;
                case "setting":
                    List<Settings> stgs = new ArrayList<Settings>();
                    JSONArray sja = new JSONArray(jo.get("data").toString());
                    for (int i = 0;i<sja.length();i++) {
                        stgs.add(parseSetting(sja.get(i).toString()));
                    }
                    return stgs;
                case "set":
                    List<Set> settgs = new ArrayList<Set>();
                    JSONArray setja = new JSONArray(jo.get("data").toString());
                    for (int i = 0;i<setja.length();i++) {
                        settgs.add(parseSet(setja.get(i).toString()));
                    }
                    return settgs;
            }

        }catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    //
    public static Player parsePlayer(String string) {
        Player p = new Player();
        try {
            JSONObject o = new JSONObject(string);
            System.out.println("Found: "+string);
            if (o.has("color")) {
                p.color = Integer.parseInt(o.get("color").toString());
            }
            if (o.has("name")) {
                p.name = o.get("name").toString();
            }
            if (o.has("wins")) {
                p.wins = (int) o.get("wins");
            }
            if (o.has("lose")) {
                p.lose = (int) o.get("lose");
            }
            if (o.has("number")) {
                p.number = o.get("number").toString();
            }
        } catch (JSONException e) {
            try {
                JSONArray a = new JSONArray(string);
                JSONObject o = new JSONObject(a.get(0).toString());
                System.out.println("Found: " + string);
                if (o.has("color")) {
                    p.color = Integer.parseInt(o.get("color").toString());
                }
                if (o.has("name")) {
                    p.name = o.get("name").toString();
                }
                if (o.has("wins")) {
                    p.wins = (int) o.get("wins");
                }
                if (o.has("lose")) {
                    p.lose = (int) o.get("lose");
                }
                if (o.has("number")) {
                    p.number = o.get("number").toString();
                }
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
        }
        return  p;
    }
    public static Game parseGame(String string) {
        Game p = new Game();
        try {
            JSONObject o = new JSONObject(string);
            if (o.has("id")) {
                p.id = Long.parseLong(o.get("id").toString());
            }
            if (o.has("set")) {
                p.set = Integer.parseInt(o.get("set").toString());
            }
            if (o.has("score1")) {
                p.score1 = Integer.parseInt(o.get("score1").toString());
            }
            if (o.has("score2")) {
                p.score2 = Integer.parseInt(o.get("score2").toString());
            }
            if (o.has("players")) {
                p.players = parseString(o.get("players").toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return p;
    }
    public static Settings parseSetting(String string) {
        Settings s = new Settings();
        try {
            JSONObject o = new JSONObject(string);
            if (o.has("switch_side_each_set")) {
                s.switch_side_each_set = (boolean) o.get("switch_side_each_set");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return s;
    }
    public static Set parseSet(String string) {
        Set s = new Set();
        try {
            JSONObject o = new JSONObject(string);
            if (o.has("score1")) {
                s.score1 = (int) o.get("score1");
            }
            if (o.has("score2")) {
                s.score2 = (int) o.get("score2");
            }
            if (o.has("startdate")) {
                s.startdate = (int) o.get("startdate");
            }
            if (o.has("enddate")) {
                s.enddate = (int) o.get("enddate");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return s;
    }
    //
    public static String StringifyPlayer(List<Player> ps) throws JSONException {
        JSONObject res = new JSONObject();
        res.put("type", "player");
        JSONArray ja = new JSONArray();

        ps.forEach(p-> {
            JSONObject o = new JSONObject();
            try {
                o.put("color", p.color);
                o.put("name", p.name);
                o.put("wins", p.wins);
                o.put("lose", p.lose);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ja.put(o);
        });
        res.put("data",ja);
        return res.toString();
    }
    public static String StringifyPlayer(Player p) throws JSONException {
        JSONObject res = new JSONObject();
        res.put("type", "player");
        JSONArray ja = new JSONArray();

        JSONObject o = new JSONObject();
        try {
            o.put("color", p.color);
            o.put("name", p.name);
            o.put("wins", p.wins);
            o.put("lose", p.lose);
            o.put("number", p.number);
        } catch (JSONException e) {
            System.out.println("stringify error");
            e.printStackTrace();
        }
        ja.put(o);
        res.put("data",ja);
        return res.toString();
    }
    public static String StringifyGame(List<Game> gs) throws JSONException {
        JSONObject res = new JSONObject();
        res.put("type", "game");
        JSONArray ja = new JSONArray();

        gs.forEach(p-> {
            JSONObject o = new JSONObject();
            try {
                o.put("id", p.id);
                o.put("set", p.set);
                o.put("score1", p.score1);
                o.put("score2", p.score2);
                o.put("players", StringifyPlayer(p.players));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ja.put(o);
        });
        res.put("data",ja);
        return res.toString();
    }
    public static String StringifyGame(Game g) throws JSONException {
        JSONObject res = new JSONObject();
        res.put("type", "game");
        JSONArray ja = new JSONArray();
        JSONObject o = new JSONObject();
        try {
            o.put("id", g.id);
            o.put("set", g.set);
            o.put("score1", g.score1);
            o.put("score2", g.score2);
            //Utils.Log("stringed pl : "+StringifyPlayer(g.players));
            o.put("players", new JSONObject(StringifyPlayer(g.players)));
            o.put("sets", new JSONObject(StringifySet(g.sets)));
            o.put("currentset", new JSONObject(StringifySet(g.currentset)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ja.put(o);
        res.put("data",ja);
        return res.toString();
    }
    public static String StringifySetting(List<Settings> ss) throws JSONException {
        JSONObject res = new JSONObject();
        res.put("type", "setting");
        JSONArray ja = new JSONArray();

        ss.forEach(p-> {
            JSONObject o = new JSONObject();
            try {
                o.put("switch_side_each_set", p.switch_side_each_set);
                o.put("winning_at",p.winning_at);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ja.put(o);
        });
        res.put("data",ja);
        return res.toString();
    }
    public static String StringifySetting(Settings s) throws JSONException {
        JSONObject res = new JSONObject();
        res.put("type", "setting");
        JSONArray ja = new JSONArray();
        JSONObject o = new JSONObject();
        try {
            o.put("switch_side_each_set", s.switch_side_each_set);
            o.put("winning_at", s.winning_at);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ja.put(o);
        res.put("data",ja);
        return res.toString();
    }
    public static String StringifySet(List<Set> ss) throws  JSONException {
        JSONObject res = new JSONObject();
        res.put("type", "set");
        JSONArray ja = new JSONArray();

        ss.forEach(p-> {
            JSONObject o = new JSONObject();
            try {
                o.put("score1",p.score1);
                o.put("score2",p.score2);
                o.put("startdate",p.startdate);
                o.put("enddate", p.enddate);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            ja.put(o);
        });
        res.put("data",ja);
        return res.toString();
    }
    public static String StringifySet(Set s) throws  JSONException {
        JSONObject res = new JSONObject();
        res.put("type", "set");
        JSONArray ja = new JSONArray();
        JSONObject o = new JSONObject();
        try {
            o.put("score1",s.score1);
            o.put("score2",s.score2);
            o.put("startdate",s.startdate);
            o.put("enddate", s.enddate);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ja.put(o);
        res.put("data",ja);
        return res.toString();
    }
}
