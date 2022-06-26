package it.domipoke.pingpongscoreboard;

import android.content.Context;

import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

class Last {
   public Context ctx;
   public Last(Context mctx) {ctx=mctx;}


   public Game readLastGame() throws FileNotFoundException {
      File last = ctx.getExternalFilesDir("last");
      File g = new File(last +"/"+File.separator+"game.json");
      List<Game> gs = Parser.parseString(Parser.read(g));
      return gs.get(0);
   }
   public void updateLastGame(Game g) throws IOException, JSONException {
      File last = ctx.getExternalFilesDir("last");
      File fg = new File(last +"/"+File.separator+"game.json");
      if (!fg.exists()) {fg.createNewFile();}
      FileWriter fwg = new FileWriter(fg);
      fwg.write(Parser.StringifyGame(g));
      fwg.close();
   }
}
