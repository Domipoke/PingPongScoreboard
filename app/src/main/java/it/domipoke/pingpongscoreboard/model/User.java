package it.domipoke.pingpongscoreboard.model;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import it.domipoke.pingpongscoreboard.func.FireDataBase;

public class User extends HashMap<String, Object> {

    public static Map<String,Object> clearMap(FirebaseUser u) {
        Map<String,Object> m = new HashMap<>();
        m.put("uid",u.getUid());
        m.put("email",u.getEmail());
        m.put("playedmatches", new ArrayList<String>());
        return m;
    }

    public static User parse(DocumentSnapshot userds) {
        User ret = new User();
        FireDataBase db = new FireDataBase();
        ret.put("p",db.getPlayer(userds));
        ret.put("uid",db.getUid(userds));
        ret.put("email",db.getEmail(userds));
        return ret;
    }
}
