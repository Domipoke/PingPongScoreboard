package it.domipoke.pingpongscoreboard;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Menu extends AppCompatActivity {

    public Spinner presets;
    public ScrollView scv;
    public LinearLayout scvll;

    public Context ctx = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        presets = findViewById(R.id.presets);
        reloadPresets(presets);
        presets.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println(presets.getAdapter().getItem(i).toString());
                if (presets.getAdapter().getItem(i).toString().equalsIgnoreCase("aggiungi")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                    builder.setTitle("Nome: ");
                    EditText input = new EditText(ctx);
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            try {
                                (new File(getFilesDir().toPath().resolve("./stgps/"+input.getText().toString()+".json").toUri())).createNewFile();
                                reloadPresets(presets);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    builder.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                };
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Option[] options = new Option[]{
                new Option("players",Opt.INT),
                new Option("winning_at",Opt.INT)
        };
        scvll = findViewById(R.id.optionsll);
        scvll.setOrientation(LinearLayout.VERTICAL);
        for (Option o : options) {
            switch (o.type) {
                case Opt.STRING:
                    scvll.addView(StringOption(o));
                case Opt.INT:
                    scvll.addView(IntOption(o));
                case Opt.BOOL:
                    scvll.addView(BoolOption(o));
            }
        }
    }

    private void reloadPresets(Spinner presets) {
        List<String> stgprs = Arrays.stream(Objects.requireNonNull(new File(this.getExternalFilesDir("stgps").toURI()).listFiles())).map(x -> (Arrays.toString(x.getName().split(".json"))).replaceAll("[^a-zA-Z]", "")).collect(Collectors.toList());
        stgprs.add("Aggiungi");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stgprs);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        presets.setAdapter(adapter);
    }

    private LinearLayout StringOption(Option o) {
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        TextView t = new TextView(this);
        t.setText(o.name);

        EditText e = new EditText(this);
        ll.addView(t);
        ll.addView(e);
        return ll;
    }

    private LinearLayout IntOption(Option o) {
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        TextView t = new TextView(this);
        t.setText(o.name);

        EditText e = new EditText(this);
        e.setInputType(InputType.TYPE_CLASS_NUMBER);
        ll.addView(t);
        ll.addView(e);
        return ll;
    }

    private LinearLayout BoolOption(Option o) {
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        TextView t = new TextView(this);
        t.setText(o.name);

        CheckBox e = new CheckBox(this);
        ll.addView(t);
        ll.addView(e);
        return ll;
    }
}