package it.domipoke.pingpongscoreboard.func;


import it.domipoke.pingpongscoreboard.model.*;

public abstract class SimpleCallback {
      public interface PlayerCallBack {
            void callback(Player p, int pi);
      }

      public interface UserFinded {
            void callback(User u);
      }
}
