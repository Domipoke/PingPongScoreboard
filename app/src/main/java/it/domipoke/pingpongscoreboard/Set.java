package it.domipoke.pingpongscoreboard;

import java.util.Date;

class Set {
   public int score1;
   public int score2;

   public long startdate;
   public long enddate;

   public Set(int s1, int s2) {
      score1=s1;
      score2=s2;
   }
   public Set() {

   }
   public void StartNow() {
      startdate = (new Date()).getTime();
   }
   public void EndNow() {
      enddate = (new Date()).getTime();
   }
}
