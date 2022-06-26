package it.domipoke.pingpongscoreboard;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class SingleMatch extends AppCompatActivity {

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
    private Object VoiceRecognition;
    public boolean share;
    public String wid;
    public Last l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_match);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Bundle b = getIntent().getExtras();
        share = (boolean) b.get("share");
        String s = b.get("game").toString();
        Utils.Log("Single Match -> s :"+s);
        List<Game> gs = Parser.parseString(s);
        g = gs.get(0);
        l = new Last(this);
        if (share) {
            wid = (String) b.get("web_id");
            g.web_id = wid;
            new FireDataBase().pushMatch(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()),wid);
        }
        try {
            l.updateLastGame(g);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

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

        lp = findViewById(R.id.doublematchcl);
        pl1 = findViewById(R.id.pl1);
        pl2 = findViewById(R.id.pl2);
        Tscore1 = findViewById(R.id.Tscore1);
        Tscore2 = findViewById(R.id.Tscore2);
        baddpl1 = findViewById(R.id.baddpl1);
        baddpl2 = findViewById(R.id.baddpl2);
        brempl1 = findViewById(R.id.brempl1);
        brempl2 = findViewById(R.id.brempl2);
        findViewById(R.id.sharematch).setOnClickListener(x->{
            if (share) {
                String url = Parser.IdToLink(wid);
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, url);
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, wid);
                this.startActivity(shareIntent);
            } else {
                boolean bl = FirebaseAuth.getInstance().getCurrentUser().isEmailVerified();
                Utils.Log(String.valueOf(bl));
                if (bl) {
                    try {
                        Utils.Log(Parser.StringifyGame(g));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Map<String, Object> mso = new FireDataBase().ShareMatch(this.g, this, -1);
                    share = true;
                    g.web_id = mso.get("web_id").toString();
                    try {
                        l.updateLastGame(this.g);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        /*
        Thread t = new Thread(new VoiceRecognition(this));
        t.setName("voicecontrol");
        t.start();*/
        startSet();
    }

    private void startSet() {
        setSide();
        try {
            System.out.println("Sets: " + Parser.StringifySet(g.sets));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        g.set=g.sets.size();
        updateScoreText();
        pl1.setBackgroundColor(g.players.get(0).color);
        pl2.setBackgroundColor(g.players.get(1).color);
        baddpl1.setOnClickListener(view -> {g.updateScore(1, 1, this);updateScoreText();});
        baddpl2.setOnClickListener(view -> {g.updateScore(2, 1, this);updateScoreText();});
        //
        brempl1.setOnClickListener(view -> {if (g.score1>0) g.updateScore(1, -1, this);updateScoreText();});
        brempl2.setOnClickListener(view -> {if (g.score2>0) g.updateScore(2, -1, this);updateScoreText();});
    }

    private void updateScoreText() {
        Tscore1.setText(String.valueOf(g.score1));
        Tscore2.setText(String.valueOf(g.score2));
        String upd = (String) PingPongRuleChecker.WhoServesSingle(g, PingPongRuleChecker.RETURN_TYPE_STRING);
        ((TextView) findViewById(R.id.updatessingle)).setText(upd);
        g.updates=upd;
        ((TextView) findViewById(R.id.setwonby1)).setText(String.valueOf(g.sets.stream().filter(x->x.score1>x.score2).count()));
        ((TextView) findViewById(R.id.setwonby2)).setText(String.valueOf(g.sets.stream().filter(x->x.score2>x.score1).count()));

        if (g.score1==g.settings.winning_at-1&&g.score2==g.settings.winning_at-1) {
            g.settings.winning_at=g.settings.winning_at+1;
            g.settings.change_serve_each=1;
        } else if (g.score1>=g.settings.winning_at|g.score2>=g.settings.winning_at) {
            AlertDialog.Builder b3 = new AlertDialog.Builder(this);
            b3.setTitle("Nuovo set?");
            b3.setPositiveButton("SI", (dI2, i2) ->  {
                g.nextSet();
                g.settings=Settings.DefaultSingle();
                setSide();
                updateScoreText();
            });
            b3.setNegativeButton("NO", (dI3,i3)-> dI3.cancel());
            b3.create().show();
        }
        if (share) {
            FireDataBase db = new FireDataBase();
            db.updateGame(wid, g);
            g.save(this.getExternalFilesDir("games"), wid);
        } else {
            g.save(this.getExternalFilesDir("games"), g.generateNewName(this));
        }
        try {
            l.updateLastGame(g);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        g.updateCurrentSet();
    }

    public void setSide() {
        if (g.settings.switch_side_each_set) {
            if (g.set%2==1) {
                setLeft(lp,pl1,pl2);
                setRight(lp,pl2,pl1);
                setSetScore(pl1,findViewById(R.id.setwonby1),pl2,findViewById(R.id.setwonby2));
            } else {
                setRight(lp,pl1,pl2);
                setLeft(lp,pl2,pl1);
                setSetScore(pl2,findViewById(R.id.setwonby2),pl1,findViewById(R.id.setwonby1));

            }
        }
    }

    private void setSetScore(ConstraintLayout left, TextView leftTextView, ConstraintLayout right, TextView rightTextView) {
        ConstraintSet setscoreLeft = new ConstraintSet();
        ConstraintSet setscoreRight = new ConstraintSet();
        setscoreLeft.clone(left);
        setscoreRight.clone(right);

        setscoreLeft.connect(leftTextView.getId(),ConstraintSet.TOP, ConstraintSet.PARENT_ID,ConstraintSet.TOP);
        setscoreLeft.connect(leftTextView.getId(),ConstraintSet.END,ConstraintSet.PARENT_ID,ConstraintSet.END);
        setscoreLeft.setMargin(leftTextView.getId(), ConstraintSet.RIGHT, 16);

        setscoreRight.connect(rightTextView.getId(),ConstraintSet.TOP,ConstraintSet.PARENT_ID,ConstraintSet.TOP);
        setscoreRight.connect(rightTextView.getId(),ConstraintSet.START,ConstraintSet.PARENT_ID,ConstraintSet.START);
        setscoreLeft.setMargin(rightTextView.getId(), ConstraintSet.LEFT, 16);

        setscoreLeft.applyTo(left);
        setscoreRight.applyTo(right);
    }


    public void setLeft(ConstraintLayout cl, View vl,View vr) {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(cl);
        constraintSet.connect(vl.getId(),ConstraintSet.BOTTOM,cl.getId(),ConstraintSet.BOTTOM);
        constraintSet.connect(vl.getId(),ConstraintSet.END,vr.getId(),ConstraintSet.START);
        constraintSet.setHorizontalBias(vl.getId(), 1/2);
        constraintSet.connect(vl.getId(),ConstraintSet.START,cl.getId(),ConstraintSet.START);
        constraintSet.connect(vl.getId(),ConstraintSet.TOP,R.id.updatessingle,ConstraintSet.BOTTOM);
        constraintSet.setVerticalBias(vl.getId(), 1);
        constraintSet.applyTo(cl);


    }
    public void setRight(ConstraintLayout cl,View vr,View vl) {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(cl);
        constraintSet.connect(vr.getId(),ConstraintSet.BOTTOM,cl.getId(),ConstraintSet.BOTTOM);
        constraintSet.connect(vr.getId(),ConstraintSet.END,cl.getId(),ConstraintSet.END);
        constraintSet.connect(vr.getId(),ConstraintSet.START,vl.getId(),ConstraintSet.END);
        constraintSet.connect(vr.getId(),ConstraintSet.TOP,R.id.updatessingle,ConstraintSet.BOTTOM);
        constraintSet.setVerticalBias(vl.getId(), 0);
        constraintSet.applyTo(cl);
    }
}