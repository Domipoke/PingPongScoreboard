package it.domipoke.pingpongscoreboard.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Color;

import org.json.JSONException;
import org.json.JSONObject;
import it.domipoke.pingpongscoreboard.func.Parser;
import it.domipoke.pingpongscoreboard.func.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Player extends HashMap<String,Object>{

    public Player() {this.put("color",Color.WHITE);this.put("wins",0);this.put("lose",0);this.put("number","");}

    public static Map<String, Object> toMap(List<Player> players) {
        Map<String,Object> o = new HashMap<>();
        AtomicInteger i = new AtomicInteger(1);
        players.forEach(player -> {
            o.put("player"+i.get(),players.get(i.get()-1));
            i.getAndIncrement();
        });
        return o;
    }

    public static Player fromObject(Object e) {
        Player p = new Player();
        Utils.Log("From object" + e);
        String substring = e.toString().substring(1, e.toString().length() - 2);
        List<String> splittedPlayers =new ArrayList(){};
        List stream = Arrays.asList(substring.split("\\}\\,"));
        for (int index = 0; index<stream.size(); index++) {
            if (index<stream.size()-1) {
                splittedPlayers.add(stream.get(index).toString()+"}");
            } else {
                splittedPlayers.add(stream.get(index).toString());
            }
        }
        for (String splayer : splittedPlayers) {
            String[] split = splayer.split("\\,");
            for (String l : split) {
                List<Object> keyvalue = Arrays.asList(l.split("\\=", 1));
                p.put(keyvalue.get(0).toString(),keyvalue.get(1));
            }
        }


        return p;
    }

    public boolean save(File path, String n) {
        File p = new File(path +"/"+File.separator+n+".json");
        if (!exists(path, n)) {
            try {
                p.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        String s = null;
        try {
            s = Parser.StringifyPlayer(this);
            FileWriter fw = new FileWriter(p);
            fw.write(s);
            fw.close();

        } catch (JSONException | IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean exists(File path,String n) {
        File p = new File(path +"/"+File.separator+n+".json");
        return p.exists();
    }

    public static Player read(File path, String n) {
        File p = new File(path +"/"+File.separator+n+".json");
        if (p.exists()) {
            try {
                String res = Parser.read(p);
                JSONObject jo = new JSONObject(res);
                return Parser.parsePlayer(jo.get("data").toString());
            } catch (FileNotFoundException | JSONException e) {
                e.printStackTrace();
                System.out.println("Si blocca nel try/catch di Player.read");
            }

        } else {
            System.out.println(p.toURI()+" not Exist" );
        }
        return null;
    }

    public boolean isValidToBePlayed(Context ctx) {
        try {
            Utils.Log(String.valueOf((int) this.get("color")));
            Utils.Log(this.get("name").toString());
            boolean c = true;//Type.isColorResource(ctx, (int) this.get("color"));
            boolean n = this.get("name").toString().replaceAll("[^a-zA-Z]", "").length() > 0;
            Utils.Log(String.valueOf(c));
            Utils.Log(String.valueOf(n));
            Utils.Log(String.valueOf(n));
            return n;
        } catch (Exception e) {
            return false;
        }
    }
    public int getColor() {
        return (int) this.get("color");
    }

}

