package it.domipoke.pingpongscoreboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;

import java.util.List;
import java.util.stream.Collectors;

public class SingleMatch extends AppCompatActivity {

    public Button pl1;
    public Button pl2;
    public ConstraintLayout lp;
    public Game g;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_match);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Bundle b = getIntent().getExtras();
        String s = b.get("game").toString();
        Utils.Log("Single Match -> s :"+s);
        List<Game> gs = Parser.parseString(s);
        g = gs.get(0);
        try {
            Utils.Log("Single Match -> p :"+Parser.StringifyPlayer(g.players));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            Utils.Log("Single Match -> g :"+Parser.StringifyGame(g));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        g.DefaultSetSingle();
        g.players=g.players.stream().filter(x->x.color!= Color.WHITE).collect(Collectors.toList());

        lp = findViewById(R.id.singlematchcl);
        pl1 = findViewById(R.id.pl1);
        pl2 = findViewById(R.id.pl2);

        startSet();
    }

    private void startSet() {
        setSide();
        g.set=g.sets.size();
        updateScoreText();
        pl1.setBackgroundColor(g.players.get(0).color);
        pl2.setBackgroundColor(g.players.get(1).color);
        pl2.setOnClickListener(view -> {g.updateScore(2, 1, this);updateScoreText();});
        pl1.setOnClickListener(view -> {g.updateScore(1, 1, this);updateScoreText();});
    }

    private void updateScoreText() {
        pl1.setText(String.valueOf(g.score1));
        pl2.setText(String.valueOf(g.score2));
        setSide();
    }

    public void setSide() {
        if (g.settings.switch_side_each_set) {
            if (g.set%2==1) {
                setLeft(lp,pl1,pl2);
                setRight(lp,pl2,pl1);
            } else {
                setRight(lp,pl1,pl2);
                setLeft(lp,pl2,pl1);
            }
        }
    }
    public void setLeft(ConstraintLayout cl, View vl,View vr) {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(cl);
        constraintSet.connect(vl.getId(),ConstraintSet.BOTTOM,cl.getId(),ConstraintSet.BOTTOM);
        constraintSet.connect(vl.getId(),ConstraintSet.END,vr.getId(),ConstraintSet.START);
        constraintSet.setHorizontalBias(vl.getId(), 1/2);
        constraintSet.connect(vl.getId(),ConstraintSet.START,cl.getId(),ConstraintSet.START);
        constraintSet.connect(vl.getId(),ConstraintSet.TOP,cl.getId(),ConstraintSet.TOP);
        constraintSet.setVerticalBias(vl.getId(), 1);
        constraintSet.applyTo(cl);
    }
    public void setRight(ConstraintLayout cl,View vr,View vl) {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(cl);
        constraintSet.connect(vr.getId(),ConstraintSet.BOTTOM,cl.getId(),ConstraintSet.BOTTOM);
        constraintSet.connect(vr.getId(),ConstraintSet.END,cl.getId(),ConstraintSet.END);
        constraintSet.connect(vr.getId(),ConstraintSet.START,vl.getId(),ConstraintSet.END);
        constraintSet.connect(vr.getId(),ConstraintSet.TOP,cl.getId(),ConstraintSet.TOP);
        constraintSet.setVerticalBias(vl.getId(), 0);
        constraintSet.applyTo(cl);
    }
}