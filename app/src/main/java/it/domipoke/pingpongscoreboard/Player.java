package it.domipoke.pingpongscoreboard;

import android.graphics.Color;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

class Player {
   public int color;
   public String name;
   public int wins;
   public int lose;
   public String number;


    public Player() {color=Color.WHITE;wins=0;lose=0;number="";}
    public void save(File path, String n) {
        File p = new File(path +"/"+File.separator+n+".json");
        if (!exists(path, n)) {
            try {
                p.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
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
        }

    }

    public static boolean exists(File path,String n) {
       File p = new File(path +"/"+File.separator+n+".json");
       return p.exists();
    }

    public int getColor() {
      return this.color;
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
}
