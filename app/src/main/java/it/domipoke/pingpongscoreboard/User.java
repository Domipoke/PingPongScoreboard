package it.domipoke.pingpongscoreboard;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class User {
   public FirebaseUser u;
   public String uid;
   public Player p;
   public String email;

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
      ret.p = db.getPlayer(userds);
      ret.uid = db.getUid(userds);
      ret.email = db.getEmail(userds);
      return ret;
   }
}
