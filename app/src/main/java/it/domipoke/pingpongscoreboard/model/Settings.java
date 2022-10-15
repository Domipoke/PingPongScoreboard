package it.domipoke.pingpongscoreboard.model;

import java.util.HashMap;
import java.util.Map;

public class Settings extends HashMap<String, Object> {
    public static int SINGLE_WINNING_POINTS = 11;
    public static int DOUBLE_WINNING_POINTS = 21;

    public Settings() {
        this.put("switch_side_each_set",true);
        this.put("winning_at",11);
        this.put("change_serve_each",2);
        this.put("ask_before_new_set",true);
    }

    public static Settings DefaultSingle() {
        Settings s = new Settings();
        s.put("switch_side_each_set",true);
        s.put("winning_at",SINGLE_WINNING_POINTS);
        s.put("change_serve_each",2);
        s.put("ask_before_new_set",true);
        return s;
    }

    public static Settings DefaultDouble() {
        Settings s = new Settings();
        s.put("switch_side_each_set",true);
        s.put("winning_at",DOUBLE_WINNING_POINTS);
        s.put("change_serve_each",5);
        s.put("ask_before_new_set",true);
        return s;
    }


    public static Map<String,Object> toMap(Settings settings) {
        Map<String,Object> s = new HashMap<>();
        s.put("switch_side_each_set",settings.get("switch_side_each_set"));
        s.put("winning_at",settings.get("winning_at"));
        s.put("change_serve_each",settings.get("change_serve_each"));
        return s;
    }
}
