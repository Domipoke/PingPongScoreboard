package it.domipoke.pingpongscoreboard;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class Game {

   public long id;
   public int set;
   public int score1;
   public int score2;
   //STG
   public boolean switch_each_set=true;
   public List<Player> players;


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
   }

   public void updateScore(int team,int point) {
      if (team==1) {
         score1=score1+point;
      } else if (team==2) {
         score2=score2+point;
      }
   }


   public void nextSet() {
      score1=0;
      score2=0;
      set=set+1;
   }


   public void setPlayer(int i, Player pl) {

   }
}
