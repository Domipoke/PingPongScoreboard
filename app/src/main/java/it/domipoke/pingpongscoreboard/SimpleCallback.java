package it.domipoke.pingpongscoreboard;

import android.view.View;

import java.util.ArrayList;

abstract class SimpleCallback {
      public interface PlayerCallBack {
            void callback(Player p, int pi);
      }

      public interface UserFinded {
            void callback(User u);
      }
}
