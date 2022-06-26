package it.domipoke.pingpongscoreboard;

import java.util.HashMap;
import java.util.Map;

class Settings {
    public static int SINGLE_WINNING_POINTS = 11;
    public static int DOUBLE_WINNING_POINTS = 21;


    public boolean switch_side_each_set;
    public int winning_at;
    public int change_serve_each;
    public boolean ask_before_new_set;

    public Settings() {
        switch_side_each_set=true;
        winning_at=11;
        change_serve_each=2;
        ask_before_new_set=true;
    }

    public static Settings DefaultSingle() {
        Settings s = new Settings();
        s.switch_side_each_set=true;
        s.winning_at=SINGLE_WINNING_POINTS;
        s.change_serve_each=2;
        s.ask_before_new_set=true;
        return s;
    }

    public static Settings DefaultDouble() {
        Settings s = new Settings();
        s.switch_side_each_set=true;
        s.winning_at=DOUBLE_WINNING_POINTS;
        s.change_serve_each=5;
        s.ask_before_new_set=true;
        return s;
    }


    public static Map<String,Object> toMap(Settings settings) {
        Map<String,Object> s = new HashMap<>();
        s.put("switch_side_each_set",settings.switch_side_each_set);
        s.put("winning_at",settings.winning_at);
        s.put("change_serve_each",settings.change_serve_each);
        return s;
    }
}
