package it.domipoke.pingpongscoreboard.func;

import android.content.Context;
import android.content.Intent;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;


import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import it.domipoke.pingpongscoreboard.Intent.DoubleMatch;
import it.domipoke.pingpongscoreboard.Intent.SingleMatch;

import it.domipoke.pingpongscoreboard.model.*;

public class FireDataBase {
   public FirebaseFirestore db;
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
         return (Player) Parser.parseString(res.get().get("player").toString());
      } catch (Exception e) {
         return null;
      }
   }
   public QueryDocumentSnapshot findPlayerQDS(String uid) {
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
         return res.get();
      } catch (Exception e) {
         return null;
      }
   }
   public boolean findPlayerBool(String uid) {
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
         return true;
      } catch (Exception e) {
         return false;
      }
   }


   public Map<String,Object> ShareMatch(Game match, Context ctx, int gameType) {
      AtomicReference<Map<String,Object>> res = new AtomicReference(new HashMap<>());
      boolean exists = false;
      if (match.get("web_id")!=null) {
         if (findMatchBool((String) match.get("web_id"))) {
            exists = true;
         }
      }
      Utils.Log("Exists: " + exists);
      if (!exists) {
         //r = () ->
         db.collection("matches").add(match.toMap()).addOnCompleteListener(x->{
            if (x.isSuccessful()) {
               String[] path = x.getResult().getPath().split("/");
               String id = path[path.length - 1];
               Utils.Log("ID: " + id);
               Utils.Log(path.toString());
               Game game2 = match;
               game2.put("web_id", id);
               x.getResult().set(game2);
               String url = Parser.IdToLink((String) game2.get("web_id"));

               if (gameType == 0) {
                  Intent i = new Intent(ctx, SingleMatch.class);
                  try {
                     i.putExtra("game", Parser.StringifyGame(game2));
                  } catch (JSONException e) {
                     e.printStackTrace();
                  }
                  i.putExtra("share", true);
                  i.putExtra("web_id", id);
                  ctx.startActivity(i);
               } else if (gameType != -1) {
                  Intent i = new Intent(ctx, DoubleMatch.class);
                  try {
                     i.putExtra("game", Parser.StringifyGame(game2));
                  } catch (JSONException e) {
                     e.printStackTrace();
                  }
                  i.putExtra("share", true);
                  i.putExtra("web_id", id);
                  ctx.startActivity(i);
               }

               Intent sendIntent = new Intent();
               sendIntent.setAction(Intent.ACTION_SEND);
               sendIntent.putExtra(Intent.EXTRA_TEXT, url);
               sendIntent.setType("text/plain");
               Intent shareIntent = Intent.createChooser(sendIntent, (String) game2.get("web_id"));
               ctx.startActivity(shareIntent);


               Map<String, Object> st = res.get();
               st.put("webid",id);
               res.set(st);
            } else {
               x.getException().printStackTrace();
            }
         });



      } else {
         //r = () ->
         findMatch((String) match.get("web_id"));
      }
      //Thread t = new Thread(r);
      return res.get();
   }

   private boolean findMatchBool(String id) {
      AtomicBoolean res = new AtomicBoolean(false);
      db.collection("matches").whereEqualTo("web_id", id).get().addOnCompleteListener(x->{
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
      db.collection("matches").whereEqualTo("web_id", id).get().addOnCompleteListener(x->{
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
      //if (Utils.isNetworkAvailable(ctx)) {
         /*AtomicReference<Long> sets = new AtomicReference<Long>(0L);
         db.collection("matches").document(String.valueOf(wid)).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
               ArrayList a = (ArrayList) task.getResult().get("sets");
               long l = a.stream().count();
               sets.set(l);
            }
         });
            if ((long) g.sets.size() >= sets.get()) {*/
         db.collection("matches").document(String.valueOf(wid)).set(g.toMap()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
               Utils.Log("Firestore Updates");
            } else {
               task.getException().printStackTrace();
            }
         });
         //}
      //}
   }

   public void setupUser(FirebaseUser u) {
      String uid = u.getEmail();
      boolean exists = false;
      if (!uid.equals("")) {
         if (findPlayerBool(uid)) {
            exists = true;
         }
      }
      if (!exists) {
         db.collection("players").document(u.getEmail()).set(User.clearMap(u)).addOnCompleteListener(x->{

         });
      }
   }

   public void pushMatch(FirebaseUser currentUser, String id) {
      db.collection("players").document(currentUser.getUid()).get().addOnCompleteListener(pl-> {
         if (pl.isSuccessful()) {
            if (!pl.getResult().exists()) {
               Map<String, Object> m = User.clearMap(currentUser);
               ArrayList a = (ArrayList) m.get("playedmatches");
               a.add(id);
               m.put("playedmatches",a);
               db.collection("players").document(currentUser.getUid()).set(m);
            } else {
               Map<String, Object> m = pl.getResult().getData();
               ArrayList a = (ArrayList) m.get("playedmatches");
               a.add(id);
               m.put("playedmatches",a);
               db.collection("players").document(currentUser.getUid()).set(m);
            }
         }
      });
   }

    public DocumentSnapshot findUserFromNickName(String nickname) {
      AtomicReference<DocumentSnapshot> d  = new AtomicReference<>();
      db.collection("players").whereEqualTo("nicknane",nickname).get().addOnCompleteListener(task->{
         if (task.isSuccessful()) {
            AtomicReference<Map<String,Object>> found = null;
            task.getResult().getDocuments().forEach(doc->{
               d.set(doc);
            });
         }
      });
      return d.get();
    }

   public Player getPlayer(DocumentSnapshot userFromNickName, int pi, SimpleCallback.PlayerCallBack callback) {
      Player p = (Player) Parser.parseString(userFromNickName.get("player").toString()).get(0);
      callback.callback(p,pi);
      return p;
   }
   public Player getPlayer(DocumentSnapshot userFromNickName) {
      Player p = (Player) Parser.parseString(userFromNickName.get("player").toString()).get(0);
      return p;
   }
   public DocumentSnapshot findUserFromUid(String uid) {
      AtomicReference<DocumentSnapshot> d  = new AtomicReference<>();
      db.collection("players").whereEqualTo("uid",uid).get().addOnCompleteListener(task->{
         if (task.isSuccessful()) {
            AtomicReference<Map<String,Object>> found = null;
            task.getResult().getDocuments().forEach(doc->{
               d.set(doc);
            });
         }
      });
      return d.get();
   }

    public DocumentSnapshot findUserFromEmail(String email) {
       AtomicReference<DocumentSnapshot> d  = new AtomicReference<>();
       db.collection("players").whereEqualTo("email",email).get().addOnCompleteListener(task->{
          if (task.isSuccessful()) {
             AtomicReference<Map<String,Object>> found = null;
             task.getResult().getDocuments().forEach(doc->{
                d.set(doc);
             });
          }
       });
       return d.get();
    }

   public void getUser(DocumentSnapshot userds, SimpleCallback.UserFinded userFinded) {
      User u = User.parse(userds);
      userFinded.callback(u);
   }

   public String getUid(DocumentSnapshot ds) {
      return ds.get("uid").toString();
   }

   public String getEmail(DocumentSnapshot ds) {
      return ds.get("email").toString();
   }
}
