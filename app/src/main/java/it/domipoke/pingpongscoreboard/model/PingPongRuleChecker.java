package it.domipoke.pingpongscoreboard.model;

import java.util.List;

import it.domipoke.pingpongscoreboard.func.Utils;

public class PingPongRuleChecker {
   public static int RETURN_TYPE_STRING = 0;
   private static int flat(int i, int ch) {
      return i-(i%ch);
   }
   public static Object WhoServesSingle(Game g, int returntype) {
      
      Player[] team1 = new Player[]{((List<Player>) g.get("players")).get(0)};
      Player[] team2 = new Player[]{((List<Player>) g.get("players")).get(1)};
      int ch = (int) ((Settings) g.get("settings")).get("change_serve_each");
      int sumscore = flat((int) g.get("score1")+(int) g.get("score2"), ch);

      if (Utils.isEven((int) g.get("set"))) {
         // 1 -> 2
         if (Utils.isEven(sumscore/ch)) {
            // 1 -> 2
            switch (returntype) {
               case 0:
                  return team1[0].get("name") + PingPongLanguagePack.SERVICEON+team2[0].get("name");
            }
         } else {
            // 2 -> 1
            switch (returntype) {
               case 0:
                  return team2[0].get("name") +PingPongLanguagePack.SERVICEON+team1[0].get("name");
            }
         }
      } else {
         // 2 -> 1
         if (Utils.isEven(sumscore/ch)) {
            // 2 -> 1
            switch (returntype) {
               case 0:
                  return team2[0].get("name") +PingPongLanguagePack.SERVICEON+team1[0].get("name");
            }
         } else {
            // 1 -> 2
            switch (returntype) {
               case 0:
                  return team1[0].get("name") +PingPongLanguagePack.SERVICEON+team2[0].get("name");
            }
         }
      }
      return null;
   }

   public static Object WhoServesDouble(Game g, int returntype) {
      Player[] team1 = new Player[]{((List<Player>) g.get("players")).get(0), ((List<Player>) g.get("players")).get(2)};
      Player[] team2 = new Player[]{((List<Player>) g.get("players")).get(1),((List<Player>) g.get("players")).get(3)};
      String A = (String) team1[0].get("name");
      String B = (String) team2[0].get("name");
      String C = (String) team1[1].get("name");
      String D = (String) team2[1].get("name");

      int ch = (int) ((Settings) g.get("settings")).get("cange_serve_each");
      int sumscore = flat((int) g.get("score1")+ (int) g.get("score2"), ch);
      int sets = (int) g.get("set");

      switch (sets%4) {
         case 0:
            switch ((sumscore/ch)%4) {
               case 0:
                  switch (returntype) {
                     case 0:
                        return A+PingPongLanguagePack.SERVICEON+B;
                  }
               case 1:
                  switch (returntype) {
                     case 0:
                        return B+PingPongLanguagePack.SERVICEON+C;
                  }
                  case 2:
                  switch (returntype) {
                     case 0:
                        return C+PingPongLanguagePack.SERVICEON+D;
                  }
                  case 3:
                  switch (returntype) {
                     case 0:
                        return D+PingPongLanguagePack.SERVICEON+A;
                  }
            }

         case 1:
            switch ((sumscore/ch)%4) {
               case 0:
                  switch (returntype) {
                     case 0:
                        return B+PingPongLanguagePack.SERVICEON+C;
                  }
                  case 1:
                  switch (returntype) {
                     case 0:
                        return C+PingPongLanguagePack.SERVICEON+D;
                  }
                  case 2:
                  switch (returntype) {
                     case 0:
                        return D+PingPongLanguagePack.SERVICEON+A;
                  }
                  case 3:
                  switch (returntype) {
                     case 0:
                        return A+PingPongLanguagePack.SERVICEON+B;
                  }
            }

         case 2:
            switch ((sumscore/ch)%4) {
               case 0:
                  switch (returntype) {
                     case 0:
                        return C+PingPongLanguagePack.SERVICEON+D;
                  }
                  case 1:
                  switch (returntype) {
                     case 0:
                        return D+PingPongLanguagePack.SERVICEON+A;
                  }
                  case 2:
                  switch (returntype) {
                     case 0:
                        return A+PingPongLanguagePack.SERVICEON+B;
                  }
                  case 3:
                  switch (returntype) {
                     case 0:
                        return B+PingPongLanguagePack.SERVICEON+C;
                  }
            }

         case 3:
            switch ((sumscore/ch)%4) {
               case 0:
                  switch (returntype) {
                     case 0:
                        return D+PingPongLanguagePack.SERVICEON+A;
                  }
                  case 1:
                  switch (returntype) {
                     case 0:
                        return A+PingPongLanguagePack.SERVICEON+B;
                  }
                  case 2:
                  switch (returntype) {
                     case 0:
                        return B+PingPongLanguagePack.SERVICEON+C;
                  }
                  case 3:
                  switch (returntype) {
                     case 0:
                        return C+PingPongLanguagePack.SERVICEON+D;
                  }
            }
      }
      return null;
   }
}
