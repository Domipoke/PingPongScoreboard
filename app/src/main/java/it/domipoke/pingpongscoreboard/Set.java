package it.domipoke.pingpongscoreboard;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static List toMapList(List<Set> sets) {
        ArrayList<Object> ms = new ArrayList<Object>();
        sets.forEach(set -> {
            ms.add(toMap(set));
        });
        return ms;
    }

    private static  Map<String,Object> toMap(Set set) {
        Map<String,Object> m = new HashMap<>();
        m.put("score1",set.score1);
        m.put("score2",set.score2);
        m.put("startdate",set.startdate);
        m.put("enddate",set.enddate);
        return m;
    }

    public void StartNow() {
      startdate = (new Date()).getTime();
   }
   public void EndNow() {
      enddate = (new Date()).getTime();
   }
}
