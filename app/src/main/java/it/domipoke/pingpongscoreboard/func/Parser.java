package it.domipoke.pingpongscoreboard.func;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

import it.domipoke.pingpongscoreboard.model.Game;
import it.domipoke.pingpongscoreboard.model.Player;
import it.domipoke.pingpongscoreboard.model.Set;
import it.domipoke.pingpongscoreboard.model.Settings;


public class Parser {
    public static String[] PLAYER_KEYS = new String[]{
            "color",
            "name",
            "wins",
            "lose",
            "number"
    };
    public static String[] GAME_KEYS = new String[]{
            "id",
            "set",
            "sets",
            "score1",
            "score2",
            "players",
            "web_id",
            "updates"
    };
    public static String[] SETTINGS_KEYS = new String[]{
            "switch_side_each_set",
            "change_serve_each",
            "winning_at"
    };
    public static String[] SET_KEYS = new String[]{
            "score1",
            "score2",
            "startdate",
            "enddate"
    };

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
    public static Player parsePlayer(String string) {
        Player p = new Player();
        try {
            JSONObject o = new JSONObject(string);
            for (String k : PLAYER_KEYS) {
                if (o.has(k)) {
                    p.put(k,o.get(k));
                }
            }
        } catch (JSONException e) {
            try {
                JSONArray a = new JSONArray(string);
                JSONObject o = new JSONObject(a.get(0).toString());
                for (String k : PLAYER_KEYS) {
                    if (o.has(k)) {
                        p.put(k,o.get(k));
                    }
                }
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
        }
        return p;
    }
    public static Game parseGame(String string) {
        Game p = new Game();
        try {
            JSONObject o = new JSONObject(string);
            for (String k : GAME_KEYS) {
                if (o.has(k)) {
                    p.put(k,o.get(k));
                }
            }
        } catch (JSONException e) {
            try {
                JSONArray a = new JSONArray(string);
                JSONObject o = new JSONObject(a.get(0).toString());
                for (String k : GAME_KEYS) {
                    if (o.has(k)) {
                        p.put(k,o.get(k));
                    }
                }
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
        }
        return p;
    }
    public static Settings parseSetting(String string) {
        Settings p = new Settings();
        try {
            JSONObject o = new JSONObject(string);
            for (String k : SETTINGS_KEYS) {
                if (o.has(k)) {
                    p.put(k,o.get(k));
                }
            }
        } catch (JSONException e) {
            try {
                JSONArray a = new JSONArray(string);
                JSONObject o = new JSONObject(a.get(0).toString());
                for (String k : SETTINGS_KEYS) {
                    if (o.has(k)) {
                        p.put(k,o.get(k));
                    }
                }
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
        }
        return p;
    }
    public static Set parseSet(String string) {
        Set p = new Set();
        try {
            JSONObject o = new JSONObject(string);
            for (String k : SET_KEYS) {
                if (o.has(k)) {
                    p.put(k,o.get(k));
                }
            }
        } catch (JSONException e) {
            try {
                JSONArray a = new JSONArray(string);
                JSONObject o = new JSONObject(a.get(0).toString());
                for (String k : SET_KEYS) {
                    if (o.has(k)) {
                        p.put(k,o.get(k));
                    }
                }
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
        }
        return p;
    }
    //
    public static String StringifyPlayer(List<HashMap<String, Object>> ps) throws JSONException{
        return StringifyHashMaps(ps,"player");
    }
    public static String StringifyPlayer(Player p) throws JSONException {
        return StringifyHashMap(p,"player");
    }
    public static String StringifyGame(List<HashMap<String, Object>> gs) throws JSONException {
        return StringifyHashMaps(gs,"game");
    }
    public static String StringifyGame(Game g) throws JSONException {
        return StringifyHashMap(g,"game");
    }
    public static String StringifySetting(List<HashMap<String, Object>> ss) throws JSONException {
        return StringifyHashMaps(ss,"setting");
    }
    public static String StringifySetting(Settings s) throws JSONException {
        return StringifyHashMap(s,"setting");
    }
    public static String StringifySet(List<HashMap<String, Object>> ss) throws  JSONException {
        return StringifyHashMaps(ss,"set");
    }
    public static String StringifySet(Set s) throws  JSONException {
        return StringifyHashMap(s,"set");
    }
    public static String IdToLink(String web_id) {
        String res = "https://domipoke.github.io/PingPongScoreboard?matchid=" + web_id;
        Utils.Log(web_id);
        Utils.Log(res);
        return res;
    }
    public static ArrayList<Map<String,List<String>>> GameFileTobeFilteredString(Context ctx) {
        ArrayList res = new ArrayList<>();
        List<String> l = Arrays.stream(Objects.requireNonNull(new File(ctx.getExternalFilesDir("games").toURI()).listFiles())).map(x -> (Arrays.toString(x.getName().split(".json"))).replaceAll("[^a-zA-Z]", "")).collect(Collectors.toList());
        l.forEach(el->{
            try {
                String s = Parser.read(new File(ctx.getExternalFilesDir("games")+"/"+File.separator+el+".json"));
                Game g = (Game) Parser.parseString(s).get(0);
                Map<String,List<String>> o = new HashMap<>();
                ArrayList<String> a = new ArrayList<String>();
                ((List<HashMap<String,Object>>) g.get("players")).forEach(p ->{
                    if (Utils.notEmpty((String) p.get("name"))) {
                        a.add((String) p.get("name"));
                    }
                });
                o.put("players",a);
                List<String> file = new ArrayList<>();
                file.add(el+".json");
                o.put("filename",file);
                res.add(o);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
        //
        return res;
    }


    //NEW
    public static String StringifyHashMap(HashMap<String, Object> hm,String type) throws JSONException {
        JSONObject res = new JSONObject();
        res.put("type",type);
        JSONArray ja = new JSONArray();
        JSONObject jo = new JSONObject();
        try {
            for (String k : hm.keySet()) {
                jo.put(k,hm.get(k));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ja.put(jo);
        res.put("data",ja);
        return res.toString();
    }
    public static String StringifyHashMaps(List<HashMap<String, Object>> hms,String type) throws JSONException {
        JSONObject res = new JSONObject();
        res.put("type",type);
        JSONArray ja = new JSONArray();
        try {
            for (HashMap<String, Object> hm : hms) {
                JSONObject jo = new JSONObject();
                for (String k : hm.keySet()) {
                    jo.put(k, hm.get(k));
                }
                ja.put(jo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        res.put("data",ja);
        return res.toString();
    }
}
