package it.domipoke.pingpongscoreboard;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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

    public static boolean notEmpty(String s) {
       return (s!=null&& !s.equals(""));
    }

    public static int GetIndex(ArrayList<Map<String, Object>> arr, String key, String equalto) {
       AtomicInteger res = new AtomicInteger(-1);
       arr.forEach((obj) ->{if (obj.get(key)==equalto) {res.set(arr.indexOf(obj));}});
       return res.get();
    }
    public static void SendLogcatMail(Context ctx){

        // save logcat in file
        File outputFile = new File(ctx.getExternalFilesDir("log"),
                "logcat.txt");
        try {
            Runtime.getRuntime().exec(
                    "logcat -f " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //send file using email
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        // Set type to "email"
        emailIntent.setType("vnd.android.cursor.dir/email");
        String to[] = {"domenico.montuori1312006@gmail.com"};
        emailIntent .putExtra(Intent.EXTRA_EMAIL, to);
        // the attachment
        emailIntent .putExtra(Intent.EXTRA_STREAM, outputFile.getAbsolutePath());
        // the mail subject
        emailIntent .putExtra(Intent.EXTRA_SUBJECT, "Subject");
        ctx.startActivity(Intent.createChooser(emailIntent , "Send email..."));
    }
}
