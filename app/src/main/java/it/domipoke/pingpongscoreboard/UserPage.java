package it.domipoke.pingpongscoreboard;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    public Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);
        ctx = this;
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

        findViewById(R.id.editUser).setOnClickListener(v->{
            db.findUserFromUid(FirebaseAuth.getInstance().getCurrentUser().getUid(), new SimpleCallback.UserFinded() {
                @Override
                public void callback(User u) {
                    AlertDialog.Builder bb = new AlertDialog.Builder(ctx);
                    Context c = bb.getContext();
                    LinearLayout Vll = new LinearLayout(c);

                    //PlName EditText
                    LinearLayout Nickname = Layout.newField(c,"Nickname",);
                    Vll.addView();
                    bb.setView(Vll);
                    bb.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    bb.create().show();
                }
            });
        });
    }
}