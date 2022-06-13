package it.domipoke.pingpongscoreboard;

import java.util.ArrayList;

class PingPongRuleChecker {
   public static int RETURN_TYPE_STRING = 0;
   private static int flat(int i, int ch) {
      return i-(i%ch);
   }
   public static Object WhoServesSingle(Game g, int returntype) {
      
      Player[] team1 = new Player[]{g.players.get(0)};
      Player[] team2 = new Player[]{g.players.get(1)};
      int ch = g.settings.change_serve_each;
      int sumscore = flat(g.score1+g.score2, ch);

      if (Utils.isEven(g.set)) {
         // 1 -> 2
         if (Utils.isEven(sumscore/ch)) {
            // 1 -> 2
            switch (returntype) {
               case 0:
                  return team1[0].name +PingPongLanguagePack.SERVICEON+team2[0].name;
            }
         } else {
            // 2 -> 1
            switch (returntype) {
               case 0:
                  return team2[0].name +PingPongLanguagePack.SERVICEON+team1[0].name;
            }
         }
      } else {
         // 2 -> 1
         if (Utils.isEven(sumscore/ch)) {
            // 2 -> 1
            switch (returntype) {
               case 0:
                  return team2[0].name +PingPongLanguagePack.SERVICEON+team1[0].name;
            }
         } else {
            // 1 -> 2
            switch (returntype) {
               case 0:
                  return team1[0].name +PingPongLanguagePack.SERVICEON+team2[0].name;
            }
         }
      }
      return null;
   }

   public static Object WhoServesDouble(Game g, int returntype) {
      Player[] team1 = new Player[]{g.players.get(0), g.players.get(2)};
      Player[] team2 = new Player[]{g.players.get(1),g.players.get(3)};
      String A = team1[0].name;
      String B = team2[0].name;
      String C = team1[1].name;
      String D = team2[1].name;

      int ch = g.settings.change_serve_each;
      int sumscore = flat(g.score1+g.score2, ch);
      int sets = g.set;

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
