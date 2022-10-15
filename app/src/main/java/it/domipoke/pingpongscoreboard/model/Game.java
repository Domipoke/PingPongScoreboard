package it.domipoke.pingpongscoreboard.model;

import android.content.Context;
import android.graphics.Color;

import org.json.JSONException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import it.domipoke.pingpongscoreboard.func.Parser;
import it.domipoke.pingpongscoreboard.func.Utils;

public class Game extends HashMap<String, Object> {

   public Game() {
      this.put("id",new Date().getTime());
      this.put("set",0);
      this.put("score1",0);
      this.put("score2",0);
      List<Player> players= new ArrayList<Player>();
      players.add(new Player());
      players.add(new Player());
      players.add(new Player());
      players.add(new Player());
      this.put("players",players);
      this.put("sets",new ArrayList<Set>());
      this.put("currentset",new Set());
      this.put("settings",new Settings());
      this.put("web_id",null);
      this.put("admin_uid",null);
   }
   public void DefaultSetSingle() {
      this.put("settings",Settings.DefaultSingle());
   }
   public void DefaultSetDouble() {
      this.put("settings",Settings.DefaultDouble());
   }
   public void updateScore(int team, int point, Context ctx) {
      if (team==1) {
         this.put("score1",((int) this.get("score1"))+point);
      } else if (team==2) {
         this.put("score2",((int) this.get("score2"))+point);
      }

   }


   public void nextSet() {
      Set currentset = (Set) this.get("currentSet");
      currentset.put("score1",this.get("score1"));
      currentset.put("score1",this.get("score1"));
      currentset.EndNow();
      ArrayList sets = (ArrayList) this.get("sets");
      sets.add(currentset);
      this.put("sets",sets);
      currentset=new Set();
      this.put("score1",0);
      this.put("score2",0);
      this.put("set", ((int)this.get("set"))+1);
      currentset.StartNow();
   }


   public void setPlayer(int i, Player pl) {
      List ps = (List) this.get("players");
      ps.set(i,pl);
      this.put("players",ps);
   }

   public List<Player> getPlayers() {
      Utils.Log(this.get("players").toString());
      List<Object> bps = Arrays.asList(this.get("players"));
      List<Player> ps = bps.stream().map(e->Player.fromObject(e)).collect(Collectors.toList());
      Utils.Log("getPlayers -> ps -> "+ps.toString());
      return ps.stream().filter(x->((int) x.get("color"))!= Color.WHITE).collect(Collectors.toList());
      //List res = (List) ps.stream().filter(x -> ((int) ((HashMap) x).get("color")) != Color.WHITE).collect(Collectors.toList());
      //return res;
   }
   public int howmanyPlayers() {
      return getPlayers().size();
   }

   public Map<String, Object> toMap() {
      Map<String, Object> m = new HashMap<>();
      m.put("currentset", this.get("currentset"));
      m.put("id", this.get("id"));
      m.put("players", Player.toMap((List<Player>) this.get("players")));
      m.put("score1", this.get("score1"));
      m.put("score2", this.get("score2"));
      m.put("set", this.get("set"));
      m.put("sets", Set.toMapList((List<Set>) this.get("sets")));
      m.put("settings", Settings.toMap((Settings) this.get("settings")));
      m.put("web_id", this.get("web_id"));
      m.put("updates",this.get("updates"));
      return m;
   }

   public boolean save(File path, String n) {
      File g = new File(path +"/"+File.separator+n+".json");
      if (!exists(path, n)) {
         try {
            g.createNewFile();
         } catch (IOException e) {
            e.printStackTrace();
            return false;
         }
      }
      try {
         String s = Parser.StringifyGame(this);
         FileWriter fw = new FileWriter(g);
         fw.write(s);
         fw.close();

      } catch (JSONException | IOException e) {
         e.printStackTrace();
         return false;
      }
      return true;
   }

   public boolean exists(File path, String n) {
      File g = new File(path +"/"+File.separator+n+".json");
      return g.exists();
   }

   public String generateNewName(Context ctx) {
      File fgs = ctx.getExternalFilesDir("games");
      List<String> games = Arrays.stream(Objects.requireNonNull(new File(fgs.toURI()).listFiles())).map(x -> (Arrays.toString(x.getName().split(".json"))).replaceAll("[^a-zA-Z0-9]", "")).collect(Collectors.toList());
      List<String> cgames = games.stream().map(x->x.replaceAll("matchn[0-9]*","")).collect(Collectors.toList());
      StringBuilder title = new StringBuilder();
      List<HashMap> players = (List) this.get("players");
      switch (this.howmanyPlayers()) {
         case 4:
            title
                    .append(players.get(0).get("name"))
                    .append("_")
                    .append(players.get(2).get("name"))
                    .append("vs")
                    .append(players.get(1).get("name"))
                    .append("_")
                    .append(players.get(3).get("name"));
         default:
            title
                    .append(players.get(0).get("name"))
                    .append("vs")
                    .append(players.get(1).get("name"));
      }
      if (!cgames.contains(title.toString())) {
         return title.toString();
      } else {
         List<String> fil = games.stream().filter(x->x.matches(title.toString())).map(x->x.replaceAll(title.toString(),"")).collect(Collectors.toList());
         List<Integer> collect = fil.stream().map(x -> x.replaceAll("[A-Za-z]*matchn","0")).map(x->{
            if (!x.equalsIgnoreCase("")) {
               return Integer.parseInt(x);
            } else {
               return 0;
            }
         }).collect(Collectors.toList());
         Integer last = collect.stream().filter(x -> x >= 0).max(Integer::compare).orElse(0);
         title.append("matchn").append(last + 1);
         return title.toString();
      }
   }

   public void updateCurrentSet() {
      HashMap cu = (HashMap) this.get("currentset");
      cu.put("score1",this.get("score1"));
      cu.put("score2",this.get("score2"));
   }
}
