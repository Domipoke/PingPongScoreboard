package it.domipoke.pingpongscoreboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

import java.util.stream.Collectors;

public class DoubleMatch extends AppCompatActivity {
    public Button pl1;
    public Button pl2;

    public Game g;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_double_match);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Bundle b = getIntent().getExtras();
        String s = b.get("game").toString();
        Utils.Log(s);
        g = Parser.parseGame(s);
        g.DefaultSetDouble();
        g.players=g.players.stream().filter(x->x.color!= Color.WHITE).collect(Collectors.toList());

        pl1 = findViewById(R.id.pl1);
        pl2 = findViewById(R.id.pl2);


    }

    public void getSide() {
        if (g.settings.switch_side_each_set) {
            if (g.set%2==1) {

            }
        }
    }
}