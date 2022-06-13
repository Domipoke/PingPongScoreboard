package it.domipoke.pingpongscoreboard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;

public class History extends AppCompatActivity {

    public LinearLayout historyll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        historyll=findViewById(R.id.historyll);

    }
}