package it.domipoke.pingpongscoreboard;

import android.content.Context;
import android.graphics.Color;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class Game {

    public long id;
    public int set;
    public int score1;
    public int score2;
    //STG

    public List<Player> players;
    public Settings settings;
    public List<Set> sets;
    public Set currentset;
    public String web_id;


    public Game() {
      id=new Date().getTime();
      set=0;
      score1=0;
      score2=0;
      players= new ArrayList<Player>();
      players.add(new Player());
      players.add(new Player());
      players.add(new Player());
      players.add(new Player());
      sets=new ArrayList<Set>();
      currentset=new Set();
      settings=new Settings();
   }

   public void DefaultSetSingle() {
        settings=Settings.DefaultSingle();
   }
   public void DefaultSetDouble() {
        settings=Settings.DefaultDouble();
   }
   public void updateScore(int team, int point, Context ctx) {
      if (team==1) {
         score1=score1+point;
      } else if (team==2) {
         score2=score2+point;
      }

   }


   public void nextSet() {
        currentset.score1=score1;
        currentset.score2=score2;
        currentset.EndNow();
        sets.add(currentset);
        currentset=new Set();
        score1=0;
        score2=0;
        set=set+1;
        currentset.StartNow();
   }


   public void setPlayer(int i, Player pl) {
      players.set(i,pl);
   }

    public List<Player> getPlayers() {
       return players.stream().filter(x->x.color!=Color.WHITE).collect(Collectors.toList());
    }
    public int howmanyPlayers() {
       return getPlayers().size();
    }

    public Map<String, Object> toMap() {
        Map<String, Object> m = new HashMap<>();
        m.put("currentset", this.currentset);
        m.put("id", this.id);
        m.put("players", Player.toMap(this.players));
        m.put("score1", this.score1);
        m.put("score2", this.score2);
        m.put("set", this.set);
        m.put("sets", Set.toMapList(this.sets));
        m.put("settings", Settings.toMap(this.settings));
        m.put("web_id", this.web_id);
        return m;
    }
}
