package it.domipoke.pingpongscoreboard.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Set extends HashMap<String, Object> {
    public Set(int s1, int s2) {
        this.put("score1",s1);
        this.put("score2",s2);
    }
    public Set() {

    }
    public static List toMapList(List<Set> sets) {
        ArrayList<Object> ms = new ArrayList<Object>();
        sets.forEach(set -> ms.add(toMap(set)));
        return ms;
    }
    public static HashMap<String,Object> toMap(Set set) {
        return set;
    }
    public void StartNow() {
        this.put("startdate",(new Date()).getTime());
    }
    public void EndNow() {
        this.put("enddate",(new Date()).getTime());
    }
}
