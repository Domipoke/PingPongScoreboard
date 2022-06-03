package it.domipoke.pingpongscoreboard;

import android.content.Context;
import android.graphics.Color;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
      if (score1>=settings.winning_at|score2>=settings.winning_at) {
          currentset.EndNow();
          sets.add(currentset);
          currentset=new Set();
          AlertDialog.Builder b = new AlertDialog.Builder(ctx);
          b.setPositiveButton("OK", ()->{
              nextSet();
              currentset.StartNow();
          });
      }
   }


   public void nextSet() {
      score1=0;
      score2=0;
      set=set+1;
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
}
