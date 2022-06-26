package it.domipoke.pingpongscoreboard;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

class Layout {
    public static Spinner FastSpinner(Context ctx, List<String> l) {
        Spinner s = new Spinner(ctx);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(ctx, android.R.layout.simple_spinner_item, l);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
        return s;
    }
    public static void UpdateSpinner(Context ctx, Spinner s, List<String> l) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(ctx, android.R.layout.simple_spinner_item, l);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
    }

    public static LinearLayout newField(Context ctx, String field, String value) {
        LinearLayout ll = new LinearLayout(ctx);
        ll.setOrientation(LinearLayout.VERTICAL);
        TextView label = new TextView(ctx);
        label.setText(field+": ");
        LinearLayout Hll = new LinearLayout(ctx);
        Hll.setOrientation(LinearLayout.HORIZONTAL);
        EditText input = new EditText(ctx);
        input.setText(value);
        Button bt = new Button(ctx);
        bt.setText("Reset");
        bt.setOnClickListener(v->{
            input.setText(value);
        });
        ll.addView(label);
        Hll.addView(input);
        Hll.addView(bt);
        ll.addView(Hll);
        return ll;
    }
}
