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
        System.out.println("Read Text: "+ct);
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
                    for (int i = 0;i<gja.length();i++) {
                        Game p = new Game();
                        JSONObject o = new JSONObject(gja.get(i).toString());
                        if (o.has("id")) {
                            p.id = (int) o.get("id");
                        }
                        if (o.has("set")) {
                            p.set = (int) o.get("set");
                        }
                        if (o.has("score1")) {
                            p.score1 = (int) o.get("score1");
                        }
                        if (o.has("score2")) {
                            p.score2 = (int) o.get("score2");
                        }
                        if (o.has("players")) {
                            p.players = new ArrayList<Player>(parseString(o.get("players").toString()));
                        }
                        games.add(p);
                    }
                    return games;
            }

        }catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Player parsePlayer(String string) {
        Player p = new Player();
        try {
            JSONArray a = new JSONArray(string);
            JSONObject o = new JSONObject(a.get(0).toString());
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  p;
    }
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
}
