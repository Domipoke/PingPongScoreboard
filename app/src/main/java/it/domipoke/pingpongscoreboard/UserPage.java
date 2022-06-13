package it.domipoke.pingpongscoreboard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserPage extends AppCompatActivity {

    public Button logout;

    public ImageView i;
    public TextView name;
    public TextView email;

    public TextView PlayerName;
    public TextView PlayerColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);
        Bundle b = getIntent().getExtras();
        Object ob = b.get("user");
        FirebaseUser u = (FirebaseUser) ob;

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(x->{
            FirebaseAuth.getInstance().signOut();
            finish();
        });


        i = findViewById(R.id.profileimage);
        i.setImageURI(null);
        i.setImageURI(u.getPhotoUrl());

        name = findViewById(R.id.name);
        name.setText(u.getDisplayName());

        email = findViewById(R.id.email);
        email.setText(u.getEmail());

        String uid = u.getUid();
        FireDataBase db = new FireDataBase();
        Player p = db.findPlayer(uid);
        if (p!=null) {
            PlayerName = findViewById(R.id.playerName);
            PlayerColor = findViewById(R.id.playerColor);

            PlayerName.setText(p.name);
            PlayerColor.setBackgroundColor(p.color);
        }
    }
}