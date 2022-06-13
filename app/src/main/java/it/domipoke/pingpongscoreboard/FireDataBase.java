package it.domipoke.pingpongscoreboard;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

class FireDataBase {
   FirebaseFirestore db;
   public FireDataBase() {
      db = FirebaseFirestore.getInstance();
   }


   public Player findPlayer(String uid) {
      AtomicReference<QueryDocumentSnapshot> res = null;
      try {
         db.collection("players").get().addOnCompleteListener(x -> {
            if (x.isSuccessful()) {
               for (QueryDocumentSnapshot doc : x.getResult()) {
                  if (doc.get("uid") == uid) {
                     res.set(doc);
                  }
               }
            }
         });
         return res.get().toObject(Player.class);
      } catch (Exception e) {
         return null;
      }
   }



   public Game ShareMatch(Game match) {
      AtomicReference<Game> res = null;
      res.set(match);
      boolean exists = findMatchBool(match.web_id);
      if (!exists) {
         AtomicReference<Game> finalRes = res;
         db.collection("match").add(match).addOnCompleteListener(x->{
            if (x.isSuccessful()) {
               String id = x.getResult().getId();
               Game game2 = finalRes.get();
               game2.web_id = id;
               finalRes.set(game2);
               x.getResult().set(finalRes.get());
            }
         });
         res.set(finalRes.get());
      } else {
         res.set(findMatch(match.web_id));
      }
      return res.get();
   }

   private boolean findMatchBool(String id) {
      AtomicBoolean res = new AtomicBoolean(false);
      db.collection("match").whereEqualTo("web_id", id).get().addOnCompleteListener(x->{
         if (x.isSuccessful()) {
            if (x.getResult().size()>=1) {
               res.set(true);
            }
         }
      });
      return res.get();
   }

   public Game findMatch(String id) {
      AtomicReference<Game> res = null;
      db.collection("match").whereEqualTo("web_id", id).get().addOnCompleteListener(x->{
         if (x.isSuccessful()) {
            if (x.getResult().size()>=1) {
               QueryDocumentSnapshot doc = x.getResult().iterator().next();
               res.set(doc.toObject(Game.class));
            }
         }
      });
      return res.get();
   }
}
