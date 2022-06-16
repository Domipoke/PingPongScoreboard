package it.domipoke.pingpongscoreboard;

import android.content.Context;
import android.content.Intent;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.json.JSONException;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

class FireDataBase {
   FirebaseFirestore db;
   public FireDataBase() {
      FirebaseFirestore.setLoggingEnabled(true);
      db = FirebaseFirestore.getInstance();
   }


   public Player findPlayer(String uid) {
      AtomicReference<QueryDocumentSnapshot> res = new AtomicReference<QueryDocumentSnapshot>();
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



   public void ShareMatch(Game match, Context ctx,int gameType) {
      Runnable r;
      boolean exists = false;
      if (match.web_id!=null) {
         if (findMatchBool(match.web_id)) {
            exists = true;
         }
      }
      Utils.Log("Exists: " + exists);
      if (!exists) {
         //r = () ->
         db.collection("match").add(match.toMap()).addOnCompleteListener(x->{
            if (x.isSuccessful()) {
               String[] path = x.getResult().getPath().split("/");
               String id = path[path.length-1];
               Utils.Log("ID: "+id);
               Utils.Log(path.toString());
               Game game2 = match;
               game2.web_id = id;
               x.getResult().set(game2);
               String url = Parser.IdToLink(game2.web_id);

               if (gameType==0) {
                  Intent i = new Intent(ctx,SingleMatch.class);
                  try {
                     i.putExtra("game", Parser.StringifyGame(game2));
                  } catch (JSONException e) {
                     e.printStackTrace();
                  }
                  i.putExtra("share",true);
                  i.putExtra("web_id",id);
                  ctx.startActivity(i);
               }
               else {
                  Intent i = new Intent(ctx,DoubleMatch.class);
                  try {
                     i.putExtra("game", Parser.StringifyGame(game2));
                  } catch (JSONException e) {
                     e.printStackTrace();
                  }
                  i.putExtra("share",true);
                  i.putExtra("webID",id);
                  ctx.startActivity(i);
               }

               Intent sendIntent = new Intent();
               sendIntent.setAction(Intent.ACTION_SEND);
               sendIntent.putExtra(Intent.EXTRA_TEXT, url);
               sendIntent.setType("text/plain");
               Intent shareIntent = Intent.createChooser(sendIntent, game2.web_id);
               ctx.startActivity(shareIntent);



            } else {
               x.getException().printStackTrace();
            }
         });



      } else {
         //r = () ->
         findMatch(match.web_id);
      }
      //Thread t = new Thread(r);

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
      AtomicReference<Game> res = new AtomicReference<Game>();
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

   public void updateGame(String wid, Game g) {
      db.collection("match").document(String.valueOf(wid)).set(g.toMap()).addOnCompleteListener(task -> {
         if (task.isSuccessful()) {
            Utils.Log("Firestore Updates");
         } else {
            task.getException().printStackTrace();
         }
      });
   }
}
