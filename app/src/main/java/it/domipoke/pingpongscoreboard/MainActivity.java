package it.domipoke.pingpongscoreboard;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import it.domipoke.pingpongscoreboard.func.*;
import it.domipoke.pingpongscoreboard.model.*;
import it.domipoke.pingpongscoreboard.Intent.*;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int REQ_ONE_TAP = 2;
    private static final int REQ_USER_PAGE = 3;
    private static final int RC_SIGN_IN = 4;
    private static final int REQ_LOG_OUT = 5;

    public Button pl1;
    public Button pl2;
    public Button pl3;
    public Button pl4;

    public Game g;
    //public ImageButton menu;


    public Context ctx = this;

    public FirebaseAuth mAuth;
    private BeginSignInRequest signInRequest;
    private SignInClient oneTapClient;
    private GoogleSignInClient mGoogleSignInClient;

    public List<String> Savedpls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupFirebase();
        g = new Game();
        for (String p : Dictionary.ManifestPermissions) {
            if (ActivityCompat.checkSelfPermission(this, p) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{p}, PERMISSION_REQUEST_CODE);
            }
        }
        SetupPortrait();
        checkFiles();
        checkIntent();
        findViewById(R.id.startmatch).setOnClickListener(view -> {
            int pls = g.howmanyPlayers();
            try {
                Utils.Log(Parser.StringifyGame(g));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (pls == 2 | pls == 4) {
                g.put("players",g.getPlayers());
                if (pls == 2) StartSingleMatch(false);
                else StartDoubleMatch(false);
                clearPlayersBox();
                Savedpls = Arrays.stream(Objects.requireNonNull(new File(ctx.getExternalFilesDir("players").toURI()).listFiles())).map(x -> (Arrays.toString(x.getName().split(".json"))).replaceAll("[^a-zA-Z]", "")).collect(Collectors.toList());
            } else {
                if (pls <= 0) {
                    Utils.FastToast(this, "Non ci sono gicoatori in questa partita", Toast.LENGTH_SHORT);
                } else if (pls == 3 | pls == 1) {
                    Utils.FastToast(this, "Il numero di giocatori è dispari", Toast.LENGTH_SHORT);
                } else {
                    Utils.FastToast(this, "Ci sono troppi giocatori", Toast.LENGTH_SHORT);
                }
            }
        });
        findViewById(R.id.startmatch).setOnLongClickListener(view -> {
            Utils.AreYouSure(ctx, "Vuoi condividere?", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    boolean bl = FirebaseAuth.getInstance().getCurrentUser().isEmailVerified();
                    Utils.Log(String.valueOf(bl));
                    if (bl) {
                        int pls = g.howmanyPlayers();
                        try {
                            Utils.Log(Parser.StringifyGame(g));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (pls == 2 | pls == 4) {
                            g.put("players",g.getPlayers());
                            FireDataBase fdb = new FireDataBase();
                            if (pls == 2) fdb.ShareMatch(g, ctx, 0);
                            else fdb.ShareMatch(g, ctx, 1);
                            clearPlayersBox();
                            Savedpls = Arrays.stream(Objects.requireNonNull(new File(ctx.getExternalFilesDir("players").toURI()).listFiles())).map(x -> (Arrays.toString(x.getName().split(".json"))).replaceAll("[^a-zA-Z]", "")).collect(Collectors.toList());
                        } else {
                            if (pls <= 0) {
                                Utils.FastToast(ctx, "Non ci sono gicoatori in questa partita", Toast.LENGTH_SHORT);
                            } else if (pls == 3 | pls == 1) {
                                Utils.FastToast(ctx, "Il numero di giocatori è dispari", Toast.LENGTH_SHORT);
                            } else {
                                Utils.FastToast(ctx, "Ci sono troppi giocatori", Toast.LENGTH_SHORT);
                            }
                        }
                    } else {
                        if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                            FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (!task.isSuccessful()) {
                                        task.getException().printStackTrace();
                                    }
                                }
                            });
                            Utils.FastToast(ctx, "Verifica la tua e-mail", Toast.LENGTH_SHORT);
                            Utils.FastToast(ctx, "Controlla nello spam :(", Toast.LENGTH_LONG);
                        }
                    }
                }
            });
            return false;
        });

        findViewById(R.id.continua).setOnClickListener(view -> {
            Last l = new Last(this);
            try {
                Game ng = l.readLastGame();
                try {
                    Utils.Log(Parser.StringifyGame(ng));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                int pls = ng.howmanyPlayers();
                if (pls == 2 | pls == 4) {
                    //n((List<Player>) g.get("players"))=ng.getPlayers();
                    System.out.println("Data");

                    System.out.println(ng.get("web_id"));
                    boolean bl = (ng.get("web_id") != null);
                    System.out.println(bl);
                    if (pls == 2) ContinueSingleMatch(ng, bl);
                    else ContinueDoubleMatch(ng, bl);
                    clearPlayersBox();
                    Savedpls = Arrays.stream(Objects.requireNonNull(new File(ctx.getExternalFilesDir("players").toURI()).listFiles())).map(x -> (Arrays.toString(x.getName().split(".json"))).replaceAll("[^a-zA-Z]", "")).collect(Collectors.toList());
                } else {
                    if (pls <= 0) {
                        Utils.FastToast(this, "Non ci sono gicoatori in questa partita", Toast.LENGTH_SHORT);
                    } else if (pls == 3 | pls == 1) {
                        Utils.FastToast(this, "Il numero di giocatori è dispari", Toast.LENGTH_SHORT);
                    } else {
                        Utils.FastToast(this, "Ci sono troppi giocatori", Toast.LENGTH_SHORT);
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Utils.FastToast(this, "Ultima partita non valida", Toast.LENGTH_SHORT);
            }
        });
        findViewById(R.id.continua).setOnLongClickListener(v -> {
            AlertDialog.Builder b = new AlertDialog.Builder(this);
            LinearLayout ll = new LinearLayout(this);
            Spinner spinner = Layout.FastSpinner(this, Arrays.stream(Objects.requireNonNull(new File(this.getExternalFilesDir("games").toURI()).listFiles())).map(x -> (Arrays.toString(x.getName().split(".json"))).replaceAll("[^a-zA-Z]", "")).collect(Collectors.toList()));
            EditText e = new EditText(this);
            e.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String text = editable.toString();
                    ArrayList<Map<String, List<String>>> am = Parser.GameFileTobeFilteredString(ctx);
                    List<String> nl = new ArrayList<String>();
                    am.forEach(x->{
                        List<String> players = x.get("players");
                        players.forEach(p->{
                            if (p.contains(text)) {
                                nl.add(x.get("filename").get(0).replaceAll(".json",""));
                            }
                        });
                    });
                    if (nl.size()>0) {Layout.UpdateSpinner(ctx,spinner,nl);}
                    else Layout.UpdateSpinner(ctx,spinner,Arrays.stream(Objects.requireNonNull(new File(ctx.getExternalFilesDir("games").toURI()).listFiles())).map(x -> (Arrays.toString(x.getName().split(".json"))).replaceAll("[^a-zA-Z]", "")).collect(Collectors.toList()));
                }
            });
            ll.addView(spinner);
            ll.addView(e);
            b.setView(ll);

            b.setPositiveButton("Ok", (vv,err)-> {
                String fn = e.getText().toString()+".json";
                try {
                    Game ng = (Game) Parser.parseString(Parser.read(new File(ctx.getExternalFilesDir("games")+"/"+File.separator+fn))).get(0);
                    try {
                        Utils.Log(Parser.StringifyGame(ng));
                    } catch (JSONException error) {
                        error.printStackTrace();
                    }
                    int pls = ng.howmanyPlayers();
                    if (pls == 2 | pls == 4) {
                        //n((List<Player>) g.get("players"))=ng.getPlayers();
                        System.out.println("Data");

                        System.out.println(ng.get("web_id"));
                        boolean bl = (ng.get("web_id") != null);
                        System.out.println(bl);
                        if (pls == 2) ContinueSingleMatch(ng, bl);
                        else ContinueDoubleMatch(ng, bl);
                        clearPlayersBox();
                        Savedpls = Arrays.stream(Objects.requireNonNull(new File(ctx.getExternalFilesDir("players").toURI()).listFiles())).map(x -> (Arrays.toString(x.getName().split(".json"))).replaceAll("[^a-zA-Z]", "")).collect(Collectors.toList());
                    } else {
                        if (pls <= 0) {
                            Utils.FastToast(this, "Non ci sono gicoatori in questa partita", Toast.LENGTH_SHORT);
                        } else if (pls == 3 | pls == 1) {
                            Utils.FastToast(this, "Il numero di giocatori è dispari", Toast.LENGTH_SHORT);
                        } else {
                            Utils.FastToast(this, "Ci sono troppi giocatori", Toast.LENGTH_SHORT);
                        }
                    }
                } catch (FileNotFoundException er) {
                    er.printStackTrace();
                    Utils.FastToast(this, "Ultima partita non valida", Toast.LENGTH_SHORT);
                }
            });

            b.create().show();
            return true;
        });
        findViewById(R.id.startOnline).setOnClickListener(v->{
            Intent i = new Intent(this, Online.class);
            startActivity(i);
        });

        setupLogin();
        Savedpls = Arrays.stream(Objects.requireNonNull(new File(this.getExternalFilesDir("players").toURI()).listFiles())).map(x -> (Arrays.toString(x.getName().split(".json"))).replaceAll("[^a-zA-Z]", "")).collect(Collectors.toList());
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utils.SendLogcatMail(this);
    }
    private void setupFirebase() {

        FirebaseOptions.Builder b = new FirebaseOptions.Builder();
        b.setApiKey("AIzaSyCnyVeuMNfIEPrB4GFnU3gepf6FMRctrWw");
        b.setApplicationId("1:473052494821:android:64ee41e68a62683f680d0c");
        b.setProjectId("gp4e-6a225");
        //    <string name="default_web_client_id"          translatable="false">473052494821-hl426pj4hiti0kjrokeqf277sg3mim8s.apps.googleusercontent.com</string>
        //    <string name="firebase_database_url"          translatable="false">https://gp4e-6a225.firebaseio.com</string>
        //    <string name="gcm_defaultSenderId"            translatable="false">473052494821</string>
        //    <string name="google_api_key"                 translatable="false">AIzaSyCnyVeuMNfIEPrB4GFnU3gepf6FMRctrWw</string>
        //    <string name="google_app_id"                  translatable="false">1:473052494821:android:64ee41e68a62683f680d0c</string>
        //    <string name="google_crash_reporting_api_key" translatable="false">AIzaSyCnyVeuMNfIEPrB4GFnU3gepf6FMRctrWw</string>
        //    <string name="google_storage_bucket"          translatable="false">gp4e-6a225.appspot.com</string>
        //    <string name="project_id"                     translatable="false">gp4e-6a225</string>
        b.setStorageBucket("gp4e-6a225.appspot.com");
        b.setGcmSenderId("473052494821");
        FirebaseApp.initializeApp(this,b.build());


    }
    private void clearPlayersBox() {
        Button b1 = findViewById(R.id.fastgameplayer1);
        Button b2 = findViewById(R.id.fastgameplayer2);
        Button b3 = findViewById(R.id.fastgameplayer3);
        Button b4 = findViewById(R.id.fastgameplayer4);

        b1.setText("");
        b2.setText("");
        b3.setText("");
        b4.setText("");

        b1.setBackgroundColor(Color.WHITE);
        b2.setBackgroundColor(Color.WHITE);
        b3.setBackgroundColor(Color.WHITE);
        b4.setBackgroundColor(Color.WHITE);
        g = new Game();
    }
    private void setupLogin() {
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("473052494821-hl426pj4hiti0kjrokeqf277sg3mim8s.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();
        //(new FireDataBase()).setupUser(mAuth.getCurrentUser());
        findViewById(R.id.login).setOnClickListener(v -> {
            Utils.Log("button click0");
            loginClickBtn();
            Utils.Log("button click1");
        });
        Utils.Log("Setup login ended");
    }

    public void loginClickBtn() {
        FirebaseUser cu = mAuth.getCurrentUser();
        if (cu != null) {
            loadUser(cu);
        } else {
            AlertDialog.Builder b = new AlertDialog.Builder(this);
            b.setTitle("Login");
            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(LinearLayout.VERTICAL);
            TextView emaillabel = new TextView(this);
            TextView pwdlabel = new TextView(this);
            EditText emailin = new EditText(this);
            EditText pwdin = new EditText(this);
            ll.addView(emaillabel);
            ll.addView(emailin);
            ll.addView(pwdlabel);
            ll.addView(pwdin);
            b.setView(ll);
            b.setPositiveButton("Login", (a, c) -> {
                String email = emailin.getText().toString();
                String pwd = pwdin.getText().toString();
                if (email.length() > 0 && pwd.length() > 0) {
                    mAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            loadUser(mAuth.getCurrentUser());
                        } else {
                            mAuth.createUserWithEmailAndPassword(email, pwd)
                                    .addOnCompleteListener(this, task2 -> {
                                        if (task2.isSuccessful()) {
                                            loadUser(mAuth.getCurrentUser());
                                        } else {
                                            Utils.FastToast(this, "Cannot Login", Toast.LENGTH_LONG);
                                        }
                                    });
                        }
                    });
                } else {
                    Utils.FastToast(this, "Credenziali non valide", Toast.LENGTH_SHORT);
                }
            });
            /*
            b.setNeutralButton("Use Google", (a,c)->{
                Utils.Log("Google0");
                signIn();
                Utils.Log("Google1");
            });
            */
            b.create().show();

        }
    }


    public void SetupPortrait() {
        System.out.println("spawned");

        pl1 = findViewById(R.id.fastgameplayer1);
        pl2 = findViewById(R.id.fastgameplayer2);
        pl3 = findViewById(R.id.fastgameplayer3);
        pl4 = findViewById(R.id.fastgameplayer4);

        if (((List<Player>) g.getPlayers()).size() > 0) {
            for (int i = 0; i < 4; i++) {
                if (i < ((List<Player>) g.getPlayers()).size()) {
                    switch (i + 1) {
                        case 1:
                            pl1.setBackgroundColor(((List<Player>) g.getPlayers()).get(i).getColor());
                        case 2:
                            pl2.setBackgroundColor(((List<Player>) g.getPlayers()).get(i).getColor());
                        case 3:
                            pl3.setBackgroundColor(((List<Player>) g.getPlayers()).get(i).getColor());
                        case 4:
                            pl4.setBackgroundColor(((List<Player>) g.getPlayers()).get(i).getColor());
                    }
                }
            }
        }


        pl1.setOnClickListener(view -> Dial(pl1, 1));
        pl1.setOnLongClickListener(view -> LDial(pl1, 1));
        pl2.setOnClickListener(view -> Dial(pl2, 2));
        pl2.setOnLongClickListener(view -> LDial(pl2, 2));
        pl3.setOnClickListener(view -> Dial(pl3, 3));
        pl3.setOnLongClickListener(view -> LDial(pl3, 3));
        pl4.setOnClickListener(view -> Dial(pl4, 4));
        pl4.setOnLongClickListener(view -> LDial(pl4, 4));
    }

    public void Dial(Button btn, int playeri) {
        System.out.println("Dialog showing...");
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle("Player: ");
        LinearLayout ll = new LinearLayout(ctx);
        ll.setOrientation(LinearLayout.VERTICAL);
        EditText iname = new EditText(ctx);
        ColorPicker icolor = new ColorPicker(ctx);
        iname.setInputType(InputType.TYPE_CLASS_TEXT);
        ll.addView(iname);
        ll.addView(icolor.getLl());
        builder.setView(ll);


        builder.setPositiveButton("SAVE", (dialogInterface, i) -> {
            String n = iname.getText().toString().replaceAll("[^a-zA-Z]", "");
            if (n.equalsIgnoreCase("") | n.matches("")) n = "player";
            n = n.toLowerCase();
            btn.setBackgroundColor(icolor.c);
            btn.setText(n);
            Player pl = new Player();
            pl.put("color", icolor.c);
            pl.put("name", n);
            if (!Player.exists(this.getExternalFilesDir("players"), n)) {
                boolean isdone = pl.save(this.getExternalFilesDir("players"), n);
                if (isdone) {
                    Savedpls.add((String) pl.get("name"));
                }
            } else {
                AlertDialog.Builder b2 = new AlertDialog.Builder(ctx);
                b2.setTitle("Vuoi sostituire?");
                String finalN = n;
                b2.setPositiveButton("SI", (dI2, i2) -> {
                    pl.save(this.getExternalFilesDir("players"), finalN);
                    btn.setBackgroundColor(icolor.c);
                    btn.setText(finalN);
                });
                b2.setNegativeButton("NO", (dI3, i3) -> dI3.cancel());
                b2.create().show();
            }
            g.setPlayer(playeri - 1, pl);
        });
        builder.setNeutralButton("OK", (dialogInterface, i) -> {
            String n = iname.getText().toString().replaceAll("[^a-zA-Z]", "");
            if (n.equalsIgnoreCase("") | n.matches("")) n = "player";
            n = n.toLowerCase();
            btn.setBackgroundColor(icolor.c);
            Player pl = new Player();
            pl.put("color",icolor.c);
            pl.put("name",n);
            btn.setText((String) pl.get("name"));
            g.setPlayer(playeri - 1, pl);
        });
        builder.setNegativeButton("CLEAR", ((dialogInterface, i) -> {
            g.setPlayer(playeri - 1, new Player());
            btn.setText("");
            btn.setBackgroundColor(Color.WHITE);
        }));
        AlertDialog dialog = builder.create();
        dialog.show();
        System.out.println("Dialog showed");
    }

    public boolean LDial(Button btn, int playeri) {
        System.out.println("Dialog showing...");
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle("Player: ");
        LinearLayout ll = new LinearLayout(ctx);
        ll.setOrientation(LinearLayout.VERTICAL);
        Spinner sdpl = new Spinner(ctx);
        ArrayList<String> l = new ArrayList<String>();
        l.add("Find User");
        l.addAll(Savedpls);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, l);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sdpl.setAdapter(adapter);


        EditText e = new EditText(ctx);
        ListView lv = new ListView(ctx);
        //e.setWidth(200);
        e.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String t = editable.toString().replaceAll("[^a-zA-Z]", "");
                if (t.length()>0) {
                    ArrayList<String> possible = new ArrayList<>();
                    FirebaseFirestore db = new FireDataBase().db;
                    db.collection("players").get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot q : task.getResult()) {
                                String nickname = (String) q.get("nickname");
                                String email = (String) q.get("email");
                                System.out.println("Nick -> "+nickname);
                                System.out.println("Email -> "+email);
                                if (nickname != null) {
                                    if (nickname.toLowerCase(Locale.ROOT).contains(t.toLowerCase(Locale.ROOT))) {
                                        possible.add(nickname);
                                    }
                                }
                                if (email!=null) {
                                    if (email.toLowerCase(Locale.ROOT).contains(t.toLowerCase(Locale.ROOT))) {
                                        if (nickname!=null) {
                                            possible.add(nickname);
                                        } else {
                                            possible.add(email);
                                        }
                                    }
                                }
                            }

                            System.out.println(possible.size());
                            if (possible.size()>0) {
                                lv.setAdapter(new ArrayAdapter(ctx, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, possible));
                            }
                        }
                    });
                }
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = (String) adapterView.getItemAtPosition(i);
                e.setText(item);
            }
        });





        ll.addView(sdpl);
        ll.addView(e);
        ll.addView(lv);

        builder.setView(ll);


        builder.setNegativeButton("EXIT", (dialogInterface, i) -> dialogInterface.cancel());
        builder.setNeutralButton("NEW", (dialogInterface, i) -> {
            dialogInterface.cancel();
            Dial(btn, playeri);
        });
        builder.setPositiveButton("OPEN", (dialogInterface, i) -> {
            String plname = sdpl.getSelectedItem().toString().replaceAll("[^a-zA-Z]", "");
            Player pl;
            String lower = plname.toLowerCase(Locale.ROOT);
            System.out.println(
                    plname+"\n"+
                            lower+"\n"+
                            lower.contains("from")+"\n"+
                            lower.contains("user")+"\n"+
                            (lower.contains("from")&&lower.contains("user")));
            if (plname.length() > 0) {
                if (lower.contains("from")&&lower.contains("user")) {
                    FireDataBase db = new FireDataBase();
                    if (e.getText().toString().trim().toLowerCase(Locale.ROOT).endsWith("@gmail.com")) {
                        DocumentSnapshot ufn = db.findUserFromEmail(e.getText().toString());
                        db.getPlayer(ufn, playeri, new SimpleCallback.PlayerCallBack(){
                            @Override
                            public void callback(Player p, int pi) {
                                if (p!=null) {
                                    if (p.isValidToBePlayed(ctx)) {
                                        Utils.Log("is valid");
                                        String thatpl = btn.getText().toString();
                                        List<String> pls = Arrays.stream(Objects.requireNonNull(new File(ctx.getExternalFilesDir("players").toURI()).listFiles())).map(x -> (Arrays.toString(x.getName().split(".json"))).replaceAll("[^a-zA-Z]", "")).collect(Collectors.toList());
                                        if (pls.contains(thatpl) && !Savedpls.contains(thatpl)) {
                                            Savedpls.add(thatpl);
                                        }
                                        btn.setBackgroundColor((int) p.get("color"));
                                        btn.setText((String) p.get("name"));
                                        g.setPlayer(pi - 1, p);
                                    }
                                }
                            }
                        });
                    } else {
                        DocumentSnapshot ufn = db.findUserFromNickName(e.getText().toString());
                        db.getPlayer(ufn, playeri, new SimpleCallback.PlayerCallBack(){
                            @Override
                            public void callback(Player p, int pi) {
                                if (p!=null) {
                                    if (p.isValidToBePlayed(ctx)) {
                                        String thatpl = btn.getText().toString();
                                        List<String> pls = Arrays.stream(Objects.requireNonNull(new File(ctx.getExternalFilesDir("players").toURI()).listFiles())).map(x -> (Arrays.toString(x.getName().split(".json"))).replaceAll("[^a-zA-Z]", "")).collect(Collectors.toList());
                                        if (pls.contains(thatpl) && !Savedpls.contains(thatpl)) {
                                            Savedpls.add(thatpl);
                                        }
                                        btn.setBackgroundColor((int) p.get("color"));
                                        btn.setText((String) p.get("name"));
                                        g.setPlayer(pi - 1, p);
                                    }
                                }
                            }
                        });
                    }
                } else {
                    pl = Player.read(this.getExternalFilesDir("players"), plname);
                    Utils.Log("pl -> "+pl.get("name")+" "+pl.getColor()+" ");
                    if (pl!=null) {
                        if (pl.isValidToBePlayed(ctx)) {
                            String thatpl = btn.getText().toString();
                            List<String> pls = Arrays.stream(Objects.requireNonNull(new File(this.getExternalFilesDir("players").toURI()).listFiles())).map(x -> (Arrays.toString(x.getName().split(".json"))).replaceAll("[^a-zA-Z]", "")).collect(Collectors.toList());
                            if (pls.contains(thatpl) && !Savedpls.contains(thatpl)) {
                                Savedpls.add(thatpl);
                            }
                            btn.setBackgroundColor((int) pl.get("color"));
                            btn.setText((String) pl.get("name"));
                            g.setPlayer(playeri - 1, pl);
                            if (Savedpls.contains(pl.get("name"))) {
                                Savedpls.remove(pl.get("name"));
                            }
                        }
                    }
                }
            } else {
                Utils.FastToast(ctx, "Giocatore selezionato non valido", Toast.LENGTH_SHORT);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        System.out.println("Dialog showed");
        return true;
    }

    private void checkFiles() {
        File fd = this.getExternalFilesDir(null);
        if (fd.isDirectory() && !fd.exists()) {
            boolean mkdirs = fd.mkdirs();
            System.out.println("Exist Dir: " + mkdirs);
        }
        String[] files = {"stgps/default.json", "players", "games", "last/game.json"};
        try {
            createFolders(this.getExternalFilesDir(null), files);
            File f = new File(this.getExternalFilesDir("stgps") + "/" + File.separator + "default.json");
            System.out.println("File path: " + f.getPath());
            FileWriter fw = new FileWriter(f);
            JSONObject jo = new JSONObject();
            //
            jo.put("players", 2);
            jo.put("winning_at", 11);

            //
            fw.write(jo.toString());
            fw.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();

            Utils.FastToast(this, "Non è stato possibile creare le cartelle", Toast.LENGTH_SHORT);
        }
    }

    private void createFolders(File Dir, String[] files) throws IOException {
        for (String s : files) {
            File file = new File(Dir + "/" + s);
            System.out.println(file.getPath());
            if (!file.exists()) {
                if (file.isFile()) {
                    file.mkdirs();
                    file.createNewFile();
                } else if (file.isDirectory()) {
                    file.mkdirs();
                }
            }
        }
    }

    private void StartSingleMatch(boolean share) {
        //g.DefaultSetSingle();
        Intent i = new Intent(this, PlayMatch.class);
        try {
            i.putExtra("game", Parser.StringifyGame(g));
            i.putExtra("share", share);
            i.putExtra("type","single");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        startActivity(i);
    }

    private void ContinueSingleMatch(Game g2, boolean share) {
        //g.DefaultSetSingle();
        Intent i = new Intent(this, PlayMatch.class);
        try {
            i.putExtra("game", Parser.StringifyGame(g2));
            i.putExtra("share", share);
            i.putExtra("web_id", (String) g2.get("web_id"));
            i.putExtra("type","single");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        startActivity(i);
    }

    private void StartDoubleMatch(boolean share) {
        //g.DefaultSetDouble();
        Intent i = new Intent(this, PlayMatch.class);
        try {
            i.putExtra("game", Parser.StringifyGame(g));
            i.putExtra("share", share);
            i.putExtra("type","double");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        startActivity(i);
    }

    private void ContinueDoubleMatch(Game g2, boolean share) {
        //g.DefaultSetDouble();
        Intent i = new Intent(this, PlayMatch.class);
        try {
            i.putExtra("game", Parser.StringifyGame(g2));
            i.putExtra("share", share);
            i.putExtra("web_id", (String) g2.get("web_id"));
            i.putExtra("type","double");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        startActivity(i);
    }

    public void loadUser(FirebaseUser u) {
        Utils.Log("loadUser");
        if (u != null) {
            User user = new User();
            user.put("u",u);
            user.put("uid",u.getUid());
            //user.p = new FireDataBase().findPlayer(u.getUid());
            if (user.get("p") == null) {

            }
            Button lg = findViewById(R.id.login);
            /*
            lg.setOnClickListener(v -> {
                Intent i = new Intent(this, UserPage.class);
                i.putExtra("user", u);
                setResult(REQ_LOG_OUT, i);
                startActivity(i);
            });*/
            lg.setText("Benvenuto " + u.getEmail().split("@")[0]);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utils.Log("onActivityResult");
        switch (requestCode) {
            case REQ_USER_PAGE:
                setupLogin();
            case REQ_ONE_TAP:
                try {
                    SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
                    String idToken = credential.getGoogleIdToken();
                    Utils.Log("idToken: " + idToken);
                    if (idToken != null) {
                        // Got an ID token from Google. Use it to authenticate
                        // with Firebase.
                        AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
                        mAuth.signInWithCredential(firebaseCredential)
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            loadUser(mAuth.getCurrentUser());
                                        }
                                    }
                                });
                    }
                } catch (ApiException e) {
                    // ...
                }
                break;
            case RC_SIGN_IN:
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    Utils.Log("account: " + account.getIdToken());
                    firebaseAuthWithGoogle(account.getIdToken());
                } catch (ApiException e) {
                    // Google Sign In failed, update UI appropriately
                }
            case REQ_LOG_OUT:
                Button lg = findViewById(R.id.login);
                lg.setText("Login");
                setupLogin();
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Utils.FastToast(ctx, user.getEmail(), 1);
                            loadUser(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            loadUser(null);
                        }
                    }
                });
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        loadUser(currentUser);
    }


    // [START auth_with_google]

    // [END auth_with_google]

    // [START signin]
    private void signIn() {
        /*
        Utils.Log("Intent pick0");
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        Utils.Log("Intent pick1");
        setResult(RC_SIGN_IN, signInIntent);
        startActivity(signInIntent);*/
    }
    // [END signin]

    private void updateUI(FirebaseUser user) {
        loadUser(user);
    }

    public void checkIntent() {
        Intent intent = getIntent();
        if (intent!=null) {
            String action = intent.getAction();
            if (action!=null&&!action.equalsIgnoreCase("")) {
                Uri data = intent.getData();
                if (data!=null) {
                    String id = data.getQueryParameter("matchid");
                    Intent i = new Intent(this, WatchMatch.class);
                    i.putExtra("id", id);
                    startActivity(i);
                }
            }
        }

    }
}