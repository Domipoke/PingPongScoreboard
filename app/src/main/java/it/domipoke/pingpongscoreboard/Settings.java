package it.domipoke.pingpongscoreboard;

class Settings {
    public static int SINGLE_WINNING_POINTS = 11;
    public static int DOUBLE_WINNING_POINTS = 21;


    public boolean switch_side_each_set;
    public int winning_at;
    public int change_serve_each;

    public Settings() {
        switch_side_each_set=true;
        winning_at=11;
        change_serve_each=2;
    }

    public static Settings DefaultSingle() {
        Settings s = new Settings();
        s.switch_side_each_set=true;
        s.winning_at=SINGLE_WINNING_POINTS;
        s.change_serve_each=2;
        return s;
    }

    public static Settings DefaultDouble() {
        Settings s = new Settings();
        s.switch_side_each_set=true;
        s.winning_at=DOUBLE_WINNING_POINTS;
        s.change_serve_each=5;
        return s;
    }


}
