package it.domipoke.pingpongscoreboard;

import android.content.Context;
import android.content.res.Resources;

import androidx.core.content.res.ResourcesCompat;

class Type {
   public static boolean isColorResource(Context ctx, int value) {
      try {
         ResourcesCompat.getColor(ctx.getResources(), value, null);
         return true;
      } catch (Resources.NotFoundException e) {
         return false;
      }

   }
}
