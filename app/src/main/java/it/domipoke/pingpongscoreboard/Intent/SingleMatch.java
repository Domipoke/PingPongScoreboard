package it.domipoke.pingpongscoreboard.Intent;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import it.domipoke.pingpongscoreboard.R;
import it.domipoke.pingpongscoreboard.func.FireDataBase;
import it.domipoke.pingpongscoreboard.func.Parser;
import it.domipoke.pingpongscoreboard.func.Utils;
import it.domipoke.pingpongscoreboard.model.Game;
import it.domipoke.pingpongscoreboard.model.Last;
import it.domipoke.pingpongscoreboard.model.PingPongRuleChecker;
import it.domipoke.pingpongscoreboard.model.Player;
import it.domipoke.pingpongscoreboard.model.Set;
import it.domipoke.pingpongscoreboard.model.Settings;

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
        setContentView(R.layout.activity_match);
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
            g.put("web_id",wid);
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
            Utils.Log("Single Match -> p :"+Parser.StringifyPlayer((Player) g.get("players")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            Utils.Log("Single Match -> g :"+Parser.StringifyGame(g));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        g.DefaultSetSingle();
        g.put("players", ((List<Player>) g.get("players")).stream().filter(x->x.getColor()!= Color.WHITE).collect(Collectors.toList()));

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
                    g.put("web_id", mso.get("web_id").toString());
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
            System.out.println("Sets: " + Parser.StringifySet((Set) g.get("sets")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        g.put("set", ((List) g.get("sets")).size());
        updateScoreText();
        pl1.setBackgroundColor(((List<Player>) g.get("players")).get(0).getColor());
        pl2.setBackgroundColor(((List<Player>) g.get("players")).get(1).getColor());
        baddpl1.setOnClickListener(view -> {g.updateScore(1, 1, this);updateScoreText();});
        baddpl2.setOnClickListener(view -> {g.updateScore(2, 1, this);updateScoreText();});
        //
        brempl1.setOnClickListener(view -> {if ((int) g.get("score1")>0) g.updateScore(1, -1, this);updateScoreText();});
        brempl2.setOnClickListener(view -> {if ((int) g.get("score2")>0) g.updateScore(2, -1, this);updateScoreText();});
    }

    private void updateScoreText() {
        Tscore1.setText(String.valueOf((int) g.get("score1")));
        Tscore2.setText(String.valueOf((int) g.get("score2")));
        String upd = (String) PingPongRuleChecker.WhoServesSingle(g, PingPongRuleChecker.RETURN_TYPE_STRING);
        ((TextView) findViewById(R.id.updatessingle)).setText(upd);
        g.put("updates",upd);
        ((TextView) findViewById(R.id.setwonby1)).setText(String.valueOf(((List<Set>) g.get("sets")).stream().filter(x->(int) x.get("score1")>(int) x.get("score2")).count()));
        ((TextView) findViewById(R.id.setwonby2)).setText(String.valueOf(((List<Set>) g.get("sets")).stream().filter(x->(int) x.get("score2")>(int) x.get("score1")).count()));

        if ((int) g.get("score1")==((int) ((Settings) g.get("settings")).get("winning_at")-1)&&((int) g.get("score2"))==((int) ((Settings) g.get("settings")).get("winning_at")-1))  {
            Settings df = (Settings) g.get("settings");
            df.put("winning_at", (int) df.get("winning_at")+1);
            df.put("change_serve_each",1);
            g.put("settings",df);
        } else if (
                (int) g.get("score1")>=((int) ((Settings) g.get("settings")).get("winning_at"))|
                        (int) g.get("score2")>=((int) ((Settings) g.get("settings")).get("winning_at"))) {
            AlertDialog.Builder b3 = new AlertDialog.Builder(this);
            b3.setTitle("Nuovo set?");
            b3.setPositiveButton("SI", (dI2, i2) ->  {
                g.nextSet();
                g.put("settings",Settings.DefaultSingle());
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
        if (((boolean) ((Settings) g.get("settings")).get("switch_side_each_set"))) {
            if ((int) g.get("set")%2==1) {
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
        constraintSet.connect(vl.getId(),ConstraintSet.TOP, R.id.updatessingle,ConstraintSet.BOTTOM);
        constraintSet.setVerticalBias(vl.getId(), 1);
        constraintSet.applyTo(cl);


    }
    public void setRight(ConstraintLayout cl,View vr,View vl) {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(cl);
        constraintSet.connect(vr.getId(),ConstraintSet.BOTTOM,cl.getId(),ConstraintSet.BOTTOM);
        constraintSet.connect(vr.getId(),ConstraintSet.END,cl.getId(),ConstraintSet.END);
        constraintSet.connect(vr.getId(),ConstraintSet.START,vl.getId(),ConstraintSet.END);
        constraintSet.connect(vr.getId(),ConstraintSet.TOP, R.id.updatessingle,ConstraintSet.BOTTOM);
        constraintSet.setVerticalBias(vl.getId(), 0);
        constraintSet.applyTo(cl);
    }
}