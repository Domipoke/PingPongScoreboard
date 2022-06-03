package it.domipoke.pingpongscoreboard;

class Settings {
    public static int SINGLE_WINNING_POINTS = 11;
    public static int DOUBLE_WINNING_POINTS = 21;


    public boolean switch_side_each_set;
    public int winning_at;

    public Settings() {
        switch_side_each_set=true;
        winning_at=11;
    }
}
