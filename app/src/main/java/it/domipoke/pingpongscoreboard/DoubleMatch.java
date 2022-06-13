package it.domipoke.pingpongscoreboard;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.text.BreakIterator;
import java.util.List;
import java.util.stream.Collectors;

public class DoubleMatch extends AppCompatActivity {

    public ConstraintLayout pl1;
    public ConstraintLayout pl2;
    public Button bpl1;
    public Button bpl2;
    public ConstraintLayout lp;
    public Game g;
    public Button baddpl1;
    public Button baddpl2;
    public Button brempl1;
    public Button brempl2;
    public TextView Tscore1;
    public TextView Tscore2;

    public Player PL1;
    public Player PL2;
    public Player PL3;
    public Player PL4;


    public int firstPlayerServe;
    public int firstPlayerReceive;

    public TextView updates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_match);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Bundle b = getIntent().getExtras();
        String s = b.get("game").toString();
        Utils.Log("Double Match -> s :"+s);
        List<Game> gs = Parser.parseString(s);
        g = gs.get(0);
        try {
            Utils.Log("Double Match -> p :"+Parser.StringifyPlayer(g.players));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            Utils.Log("Double Match -> g :"+Parser.StringifyGame(g));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        g.DefaultSetDouble();
        g.players=g.players.stream().filter(x->x.color!= Color.WHITE).collect(Collectors.toList());

        PL1 = g.players.get(0);
        PL2 = g.players.get(1);
        PL3 = g.players.get(2);
        PL4 = g.players.get(3);

        Utils.Dialog(this,"Giocheranno:",PL1.name+" "+PL3.name+" contro "+PL2.name+" "+PL4.name);

        lp = findViewById(R.id.doublematchcl);
        pl1 = findViewById(R.id.pl1);
        pl2 = findViewById(R.id.pl2);
        Tscore1 = findViewById(R.id.Tscore1);
        Tscore2 = findViewById(R.id.Tscore2);
        baddpl1 = findViewById(R.id.baddpl1);
        baddpl2 = findViewById(R.id.baddpl2);
        brempl1 = findViewById(R.id.brempl1);
        brempl2 = findViewById(R.id.brempl2);
        //updates = findViewById(R.id.updates);

        startSet();
    }

    private void startSet() {
        setSide();
        g.set=g.sets.size();
        firstPlayerServe = Utils.random(new int[]{1, 3});

        updateScoreText();
        baddpl1.setOnClickListener(view -> {g.updateScore(1, 1, this);updateScoreText();});
        baddpl2.setOnClickListener(view -> {g.updateScore(2, 1, this);updateScoreText();});
        //
        brempl1.setOnClickListener(view -> {if (g.score1>0) g.updateScore(1, -1, this);updateScoreText();});
        brempl2.setOnClickListener(view -> {if (g.score2>0) g.updateScore(2, -1, this);updateScoreText();});
    }

    private void updateScoreText() {
        Tscore1.setText(String.valueOf(g.score1));
        Tscore2.setText(String.valueOf(g.score2));
        try {
            TextView upddouble = findViewById(R.id.updatesdouble);
            String t = PingPongRuleChecker.WhoServesDouble(this.g, PingPongRuleChecker.RETURN_TYPE_STRING).toString();
            Utils.Log(t);
            upddouble.setText(t);
        } finally {

        }
        ((TextView) findViewById(R.id.setwonby1)).setText(String.valueOf(g.sets.stream().filter(x->x.score1>x.score2).count()));
        ((TextView) findViewById(R.id.setwonby2)).setText(String.valueOf(g.sets.stream().filter(x->x.score2>x.score1).count()));

        if (g.score1==g.settings.winning_at-1&&g.score2==g.settings.winning_at-1) {
            g.settings.winning_at=g.settings.winning_at+1;
            g.settings.change_serve_each=1;
        } else if (g.score1>=g.settings.winning_at|g.score2>=g.settings.winning_at) {
            AlertDialog.Builder b3 = new AlertDialog.Builder(this);
            b3.setTitle("Nuovo set?");
            b3.setPositiveButton("SI", (dI2, i2) -> {
                g.nextSet();
                g.settings=Settings.DefaultDouble();
                setSide();
                updateScoreText();
            });
            b3.setNegativeButton("NO", (dI3,i3)-> dI3.cancel());
            b3.create().show();
        }

    }



    public void setSide() {
        if (g.settings.switch_side_each_set) {
            if (g.set%2==1) {
                setLeft(lp,pl1,pl2,new int[]{PL1.color,PL3.color});
                setRight(lp,pl2,pl1,new int[]{PL2.color,PL4.color});
                setSetScore(pl1,R.id.setwonby1,pl2,R.id.setwonby2);
            } else {
                setRight(lp,pl1,pl2,new int[]{PL1.color,PL3.color});
                setLeft(lp,pl2,pl1,new int[]{PL2.color,PL4.color});
                setSetScore(pl2,R.id.setwonby2,pl1,R.id.setwonby1);
            }
        }
    }
    private void setSetScore(ConstraintLayout left, int leftTextView, ConstraintLayout right, int rightTextView) {
        ConstraintSet setscoreLeft = new ConstraintSet();
        ConstraintSet setscoreRight = new ConstraintSet();
        setscoreLeft.clone(left);
        setscoreRight.clone(right);

        setscoreLeft.clear(leftTextView);
        setscoreLeft.clear(rightTextView);

        setscoreRight.clear(leftTextView);
        setscoreRight.clear(rightTextView);

        setscoreLeft.connect(leftTextView,ConstraintSet.TOP, ConstraintSet.PARENT_ID,ConstraintSet.TOP);
        setscoreLeft.connect(leftTextView,ConstraintSet.END,ConstraintSet.PARENT_ID,ConstraintSet.END);
        setscoreLeft.setMargin(leftTextView, ConstraintSet.RIGHT, 32);

        setscoreRight.connect(rightTextView,ConstraintSet.TOP,ConstraintSet.PARENT_ID,ConstraintSet.TOP);
        setscoreRight.connect(rightTextView,ConstraintSet.START,ConstraintSet.PARENT_ID,ConstraintSet.START);
        setscoreLeft.setMargin(rightTextView, ConstraintSet.LEFT, 32);

        setscoreLeft.applyTo(left);
        setscoreRight.applyTo(right);
    }
    public void setLeft(ConstraintLayout cl, View vl,View vr, int[] i) {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(cl);
        constraintSet.connect(vl.getId(),ConstraintSet.BOTTOM,cl.getId(),ConstraintSet.BOTTOM);
        constraintSet.connect(vl.getId(),ConstraintSet.END,vr.getId(),ConstraintSet.START);
        //constraintSet.setHorizontalBias(vl.getId(), 1);
        constraintSet.connect(vl.getId(),ConstraintSet.START,cl.getId(),ConstraintSet.START);
        constraintSet.connect(vl.getId(),ConstraintSet.TOP,R.id.updatesdouble,ConstraintSet.BOTTOM);
        //constraintSet.setVerticalBias(vl.getId(), 1);
        constraintSet.applyTo(cl);

        vl.setBackgroundColor(i[0]);
    }
    public void setRight(ConstraintLayout cl,View vr,View vl, int[] i) {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(cl);
        constraintSet.connect(vr.getId(),ConstraintSet.BOTTOM,cl.getId(),ConstraintSet.BOTTOM);
        constraintSet.connect(vr.getId(),ConstraintSet.END,cl.getId(),ConstraintSet.END);
        constraintSet.connect(vr.getId(),ConstraintSet.START,vl.getId(),ConstraintSet.END);
        constraintSet.connect(vr.getId(),ConstraintSet.TOP,R.id.updatesdouble,ConstraintSet.BOTTOM);
        //constraintSet.setVerticalBias(vl.getId(), 0);
        constraintSet.applyTo(cl);

        //GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TL_BR,i);
        //gd.setGradientType(GradientDrawable.SWEEP_GRADIENT);
        //gd.setGradientCenter(0,0);

        vr.setBackgroundColor(i[0]);


    }
    public String getPlayerName(int i) {
        switch (i) {
            case 1:
                return PL1.name;
            case 2:
                return PL2.name;
            case 3:
                return PL3.name;
            case 4:
                return PL4.name;
        }
        return null;
    }

}
