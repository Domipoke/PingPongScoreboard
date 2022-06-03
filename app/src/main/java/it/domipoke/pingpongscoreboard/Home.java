package it.domipoke.pingpongscoreboard;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Home extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    public Button pl1;
    public Button pl2;
    public Button pl3;
    public Button pl4;

    public Game g;
    //public ImageButton menu;


    public Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        g = new Game();
        System.out.println("Started: " + this.getExternalFilesDir(null).toString());
        for (String p : new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}) {
            if (ActivityCompat.checkSelfPermission(this, p) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(Home.this, new String[]{p}, PERMISSION_REQUEST_CODE);
            }
        }
        SetupPortrait();
        checkFiles();
        findViewById(R.id.startmatch).setOnClickListener(view ->{
            int pls = g.howmanyPlayers();
            try {
                Utils.Log(String.valueOf(Parser.StringifyGame(g)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (pls==2 | pls==4) {
                if (pls==2) StartSingleMatch();
                else if (pls==4) StartDoubleMatch();
            }else {
                if (pls <= 0) {
                    Utils.FastToast(this, "Non ci sono gicoatori in questa partita", Toast.LENGTH_SHORT);
                } else if (pls == 3 | pls == 1) {
                    Utils.FastToast(this, "Il numero di giocatori è dispari", Toast.LENGTH_SHORT);
                } else if (pls > 4) {
                    Utils.FastToast(this, "Ci sono troppi giocatori", Toast.LENGTH_SHORT);
                }
            }
        });
        findViewById(R.id.Settings).setOnClickListener(view -> {
            Intent i = new Intent(this, Menu.class);
            startActivity(i);
        });
    }



    public void SetupPortrait() {
        System.out.println("spawned");

        pl1 = findViewById(R.id.fastgameplayer1);
        pl2 = findViewById(R.id.fastgameplayer2);
        pl3 = findViewById(R.id.fastgameplayer3);
        pl4 = findViewById(R.id.fastgameplayer4);

        if (g.players.size() > 0) {
            for (int i = 0; i < 4; i++) {
                if (i < g.players.size()) {
                    switch (i + 1) {
                        case 1:
                            pl1.setBackgroundColor(g.players.get(i).getColor());
                        case 2:
                            pl2.setBackgroundColor(g.players.get(i).getColor());
                        case 3:
                            pl3.setBackgroundColor(g.players.get(i).getColor());
                        case 4:
                            pl4.setBackgroundColor(g.players.get(i).getColor());
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
            n=n.toLowerCase();
            btn.setBackgroundColor(icolor.c);
            btn.setText(n);
            Player pl = new Player();
            pl.color = icolor.c;
            pl.name = n;
            if (!Player.exists(this.getExternalFilesDir("players"), n)) {
                pl.save(this.getExternalFilesDir("players"), n);
            } else {
                AlertDialog.Builder b2 = new AlertDialog.Builder(ctx);
                b2.setTitle("Vuoi sostituire?");
                String finalN = n;
                b2.setPositiveButton("SI", (dI2, i2) -> {pl.save(this.getExternalFilesDir("players"), finalN);btn.setBackgroundColor(icolor.c);btn.setText(finalN);});
                b2.setNegativeButton("NO", (dI3,i3)-> dI3.cancel());
                b2.create().show();
            }
            g.setPlayer(playeri-1,pl);
        });
        builder.setNeutralButton("OK",(dialogInterface, i) -> {
            String n = iname.getText().toString().replaceAll("[^a-zA-Z]", "");
            if (n.equalsIgnoreCase("") | n.matches("")) n = "player";
            n=n.toLowerCase();
            btn.setBackgroundColor(icolor.c);
            Player pl = new Player();
            pl.color=icolor.c;
            pl.name = n;
            btn.setText(pl.name);
            g.setPlayer(playeri-1,pl);
        });
        builder.setNegativeButton("CLEAR", ((dialogInterface, i) -> {
            g.setPlayer(playeri-1,new Player());
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
        List<String> pls = Arrays.stream(Objects.requireNonNull(new File(this.getExternalFilesDir("players").toURI()).listFiles())).map(x -> (Arrays.toString(x.getName().split(".json"))).replaceAll("[^a-zA-Z]", "")).collect(Collectors.toList());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, pls);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sdpl.setAdapter(adapter);
        ll.addView(sdpl);
        builder.setView(ll);


        builder.setNegativeButton("EXIT", (dialogInterface, i) -> dialogInterface.cancel());
        builder.setNeutralButton("NEW", (dialogInterface, i) -> {
            dialogInterface.cancel();
            Dial(btn,playeri);
        });
        builder.setPositiveButton("OPEN", (dialogInterface, i) ->{
           Player pl = Player.read(this.getExternalFilesDir("players"),sdpl.getSelectedItem().toString().replaceAll("[^a-zA-Z]", ""));
           System.out.println(pl.color);
           btn.setBackgroundColor(pl.color);
           btn.setText(pl.name);
           g.setPlayer(playeri-1,pl);
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        System.out.println("Dialog showed");
        return true;
    }
    private void checkFiles() {
        File fd = this.getExternalFilesDir(null);
        if (fd.isDirectory()&&!fd.exists()) {
            boolean mkdirs = fd.mkdirs();
            System.out.println("Exist Dir: "+mkdirs);
        }
        String[] files = {"stgps/default.json","players","games"};
        try {
            createFolders(this.getExternalFilesDir(null),files);
            File f = new File(this.getExternalFilesDir("stgps" ) + "/" + File.separator + "default.json");
            System.out.println("File path: "+f.getPath());
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
    private void StartSingleMatch() {
        Intent i = new Intent(this, SingleMatch.class);
        try {
            i.putExtra("game", Parser.StringifyGame(g));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        startActivity(i);
    }
    private void StartDoubleMatch() {
        Intent i = new Intent(this, DoubleMatch.class);
        try {
            i.putExtra("game", Parser.StringifyGame(g));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        startActivity(i);
    }

}
