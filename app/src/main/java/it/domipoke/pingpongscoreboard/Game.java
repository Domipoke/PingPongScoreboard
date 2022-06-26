package it.domipoke.pingpongscoreboard;

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

class Game {

    public long id;
    public int set;
    public int score1;
    public int score2;
    //STG

    public List<Player> players;
    public Settings settings;
    public List<Set> sets;
    public Set currentset;
    public String web_id;
    public String updates;
    public String admin_uid;


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
      sets=new ArrayList<Set>();
      currentset=new Set();
      settings=new Settings();
      web_id=null;
      admin_uid=null;
   }

   public void DefaultSetSingle() {
        settings=Settings.DefaultSingle();
   }
   public void DefaultSetDouble() {
        settings=Settings.DefaultDouble();
   }
   public void updateScore(int team, int point, Context ctx) {
      if (team==1) {
         score1=score1+point;
      } else if (team==2) {
         score2=score2+point;
      }

   }


   public void nextSet() {
        currentset.score1=score1;
        currentset.score2=score2;
        currentset.EndNow();
        sets.add(currentset);
        currentset=new Set();
        score1=0;
        score2=0;
        set=set+1;
        currentset.StartNow();
   }


   public void setPlayer(int i, Player pl) {
      players.set(i,pl);
   }

    public List<Player> getPlayers() {
       return players.stream().filter(x->x.color!=Color.WHITE).collect(Collectors.toList());
    }
    public int howmanyPlayers() {
       return getPlayers().size();
    }

    public Map<String, Object> toMap() {
        Map<String, Object> m = new HashMap<>();
        m.put("currentset", this.currentset);
        m.put("id", this.id);
        m.put("players", Player.toMap(this.players));
        m.put("score1", this.score1);
        m.put("score2", this.score2);
        m.put("set", this.set);
        m.put("sets", Set.toMapList(this.sets));
        m.put("settings", Settings.toMap(this.settings));
        m.put("web_id", this.web_id);
        m.put("updates",this.updates);
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
        switch (this.howmanyPlayers()) {
            case 4:
                title
                        .append(this.players.get(0).name)
                        .append("_")
                        .append(this.players.get(2).name)
                        .append("vs")
                        .append(this.players.get(1).name)
                        .append("_")
                        .append(this.players.get(3).name);
            default:
                title
                        .append(this.players.get(0).name)
                        .append("vs")
                        .append(this.players.get(1).name);
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
            title.append("matchn").append(String.valueOf(last+1));
            return title.toString();
        }
    }

    public void updateCurrentSet() {
        this.currentset.score1=this.score1;
        this.currentset.score2=this.score2;
    }
}
