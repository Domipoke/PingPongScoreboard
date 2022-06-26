package it.domipoke.pingpongscoreboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class WatchMatch extends AppCompatActivity {

    public String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_match);
        Bundle b = getIntent().getExtras();
        id = b.get("id").toString();

        SetupListener();
    }
    public Map<String,Object> selpl(Map<String, Object> player, Integer i) {
        if (player.get("player"+(i+1))!=null) {return (Map<String, Object>) player.get("player"+(i+1));}
        else {return (Map<String, Object>) player.get(i);}
    }
    private void SetupListener() {
        FirebaseFirestore db = new FireDataBase().db;
        AtomicReference<ConstraintLayout> watchpl1 = new AtomicReference<>(findViewById(R.id.watchpl1));
        AtomicReference<ConstraintLayout> watchpl2 = new AtomicReference<>(findViewById(R.id.watchpl2));
        AtomicReference<TextView> wp1name = new AtomicReference<>(findViewById(R.id.wp1name));
        AtomicReference<TextView> wp1set = new AtomicReference<>(findViewById(R.id.wp1set));
        AtomicReference<TextView> wp1score = new AtomicReference<>(findViewById(R.id.wp1score));
        AtomicReference<TextView> wp2name = new AtomicReference<>(findViewById(R.id.wp2name));
        AtomicReference<TextView> wp2set = new AtomicReference<>(findViewById(R.id.wp2set));
        AtomicReference<TextView> wp2score = new AtomicReference<>(findViewById(R.id.wp2score));
        AtomicReference<TextView> wupdt = new AtomicReference<>(findViewById(R.id.wupdt));
        db.collection("matches").document(id).addSnapshotListener((doc,error) -> {
            Map<String, Object> data = doc.getData();
            if (data!=null) {
                ArrayList<Map<String, Object>> sets = (ArrayList<Map<String, Object>>) data.get("sets");
                String w1 = String.valueOf(sets.stream().filter(x -> Integer.parseInt(x.get("score1").toString()) > Integer.parseInt(x.get("score2").toString())).count());
                String w2 = String.valueOf(sets.stream().filter(x -> Integer.parseInt(x.get("score2").toString()) > Integer.parseInt(x.get("score1").toString())).count());
                Map<String, Object> pl = (Map<String, Object>) data.get("players");
                long pls = pl.size();
                String team1 = "";
                String team2 = "";
                if (pls == 4) {
                    Map<String, Object> pl1 = selpl(pl, 0);
                    Map<String, Object> pl2 = selpl(pl, 1);
                    Map<String, Object> pl3 = selpl(pl, 2);
                    Map<String, Object> pl4 = selpl(pl, 3);
                    team1 = pl1.get("name") + " " + pl3.get("name");
                    team2 = pl2.get("name") + " " + pl4.get("name");
                } else {
                    Map<String, Object> pl1 = selpl(pl, 0);
                    Map<String, Object> pl2 = selpl(pl, 1);
                    team1 = pl1.get("name").toString();
                    team2 = pl2.get("name").toString();
                }
                ArrayList<Integer> color = new ArrayList<>();
                color.add(Integer.parseInt(selpl(pl, 0).get("color").toString()));
                color.add(Integer.parseInt(selpl(pl, 1).get("color").toString()));
                wp1name.get().setText(team1.toUpperCase());
                wp2name.get().setText(team2.toUpperCase());
                watchpl1.get().setBackgroundColor(color.get(0));
                watchpl2.get().setBackgroundColor(color.get(1));

                wp1set.get().setText(w1);
                wp2set.get().setText(w2);

                wp1score.get().setText(data.get("score1").toString());
                wp2score.get().setText(data.get("score1").toString());

                wupdt.get().setText(data.get("updates").toString());
            }
        });
    }
}