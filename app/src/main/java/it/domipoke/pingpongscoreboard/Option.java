package it.domipoke.pingpongscoreboard;

public class Option {
   public String INT = "number";
   public String STRING = "string";
   public String PLAYER = "player";
   public String BOOL = "boolean";

   public String name;
   public String type;

   public Option(String n, String t) {
      this.name = n;
      this.type = t;
   }
}

