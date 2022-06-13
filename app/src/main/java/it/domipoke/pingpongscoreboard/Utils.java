package it.domipoke.pingpongscoreboard;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

class Utils {
   public static void FastToast(Context ctx, String text, int duration) {
      Toast.makeText(ctx, text, duration).show();
   }

   public static String Log(String s) {
      System.out.println(s);
      return s;
   }

    public static int random(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static void Dialog(Context ctx, String title, String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle(title);
        TextView t = new TextView(ctx);
        t.setText(content);
        builder.setView(t);
        builder.setPositiveButton("OK",((dialogInterface, i) -> {dialogInterface.cancel();}));
        builder.create().show();
    }
    public static void AreYouSure(Context ctx,String text, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle(text);
        builder.setNegativeButton("No",((dialogInterface, i) -> {dialogInterface.cancel();}));
        builder.setPositiveButton("Si", onClickListener);
        builder.create().show();
    }


    public static int random(int[] ints) {
       return ints[random(1,ints.length-1)];
    }

    public static boolean isEven(int i) {
        return (i%2)==0;
   }
    public static boolean isOdd(int i) {
        return !isEven(i);
   }


}
