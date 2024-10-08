package AnimEngine.myapplication.client;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import AnimEngine.myapplication.R;
import AnimEngine.myapplication.utils.SearchActivity;
import AnimEngine.myapplication.login.SignInActivity;
import AnimEngine.myapplication.logics.DB;
import AnimEngine.myapplication.utils.User;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvNickname, tvRealName, tvEmail;
    ImageButton ibEditNickname, ibEditRealName;
    Button btnFavouriteAnimes, btnLogOut, btnChangePassword;
    EditText etNickname, etRealName, oldPassword, newPassword, newPassword2;
    AlertDialog adNickname, adRealName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        FirebaseAuth myAuth = DB.getAU();
        tvNickname = findViewById(R.id.animeNickname);
        tvRealName = findViewById(R.id.animeRealName);
        tvEmail = findViewById(R.id.tvEmail);
        ibEditNickname = findViewById(R.id.ibEditNickname);
        ibEditRealName = findViewById(R.id.ibEditRealName);
        btnFavouriteAnimes = findViewById(R.id.btnFavouriteAnimes);
        btnLogOut = findViewById(R.id.btnLogOut);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        oldPassword = findViewById(R.id.etLastPassword);
        newPassword = findViewById(R.id.etNewPassword);
        newPassword2 = findViewById(R.id.etNewPasswordAuth);
        btnChangePassword.setOnClickListener(this);
        ibEditNickname.setOnClickListener(this);
        ibEditRealName.setOnClickListener(this);
        btnFavouriteAnimes.setOnClickListener(this);
        btnLogOut.setOnClickListener(this);
        adNickname = new AlertDialog.Builder(this).create();
        adRealName = new AlertDialog.Builder(this).create();
        etNickname = new EditText(this);
        etRealName = new EditText(this);

        adNickname.setTitle("Edit nickname");
        adNickname.setView(etNickname);

        adRealName.setTitle("Edit real name");
        adRealName.setView(etRealName);

        adNickname.setButton(DialogInterface.BUTTON_POSITIVE, "SAVE NICKNAME", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String nick = etNickname.getText().toString().trim();
                if (nick.isEmpty()) {
                    Toast.makeText(UserProfileActivity.this, "Nickname should not be empty", Toast.LENGTH_SHORT).show();
                } else if (nick.length() > 12) {
                    Toast.makeText(UserProfileActivity.this, "Nickname too long", Toast.LENGTH_SHORT).show();
                } else {
                    tvNickname.setText(etNickname.getText());
                    DB.update_attribute("nickname", nick, UserProfileActivity.this);
                }
            }
        });
        ibEditNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etNickname.setText(tvNickname.getText());
                adNickname.show();
            }
        });

        adRealName.setButton(DialogInterface.BUTTON_POSITIVE, "SAVE REAL NAME", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String real = etRealName.getText().toString().trim();
                if (real.isEmpty()) {
                    Toast.makeText(UserProfileActivity.this, "Name should not be empty", Toast.LENGTH_SHORT).show();
                } else if (real.length() > 16) {
                    Toast.makeText(UserProfileActivity.this, "Name too long", Toast.LENGTH_SHORT).show();
                } else {
                    tvRealName.setText(etRealName.getText());
                    Map<String, Object> m = new HashMap<>();
                    DB.update_attribute("name", real, UserProfileActivity.this);
                }
            }
        });
        ibEditRealName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etRealName.setText(tvRealName.getText());
                adRealName.show();
            }
        });

        String uid = "";
        if (!Objects.requireNonNull(myAuth.getUid()).isEmpty() && myAuth.getUid() != null)
            uid = myAuth.getUid();
        DatabaseReference myRef = DB.getDB().getReference("Users").child(uid);
        ValueEventListener valueEventListener = new ValueEventListener() { // myRef.addValueEventListener(new ValueEventListener(){..};

            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    tvEmail.setText("" + user.getMail());
                    tvNickname.setText("" + user.getNickname());
                    tvRealName.setText("" + user.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                    /*
                      The Log class lets you create log messages that appear in Logcat. Every Android log message has a tag and a priority
                      associated with it. The tag of a system log message is a short string indicating the system component from which the
                      message originates.
                      In this case we used a warning Log.
                     */
                Log.w("loadUser:onCancelled", error.toException());
            }
        };
        myRef.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == ibEditNickname.getId()) {
            String nickname = tvNickname.getText().toString();
            if (nickname.isEmpty()) { // The nickname can't be ""
                Toast.makeText(UserProfileActivity.this, "Please choose another nickname", Toast.LENGTH_SHORT).show();
                // It means that the user chose a proper nickname which is not ""
            } else {
                String uid = DB.getAU().getUid();
                DatabaseReference ref = DB.getDB().getReference("Users").child(uid).child("nickname");
                ref.setValue(nickname);
            }
        } else if (view.getId() == ibEditRealName.getId()) {
            String realName = tvRealName.getText().toString();
            if (realName.isEmpty()) { // The real name can't be ""
                Toast.makeText(UserProfileActivity.this, "Please choose another name", Toast.LENGTH_SHORT).show();
            } else { // It means that the user chose a proper name which is not ""
                String uid = DB.getAU().getUid();
                DatabaseReference ref = DB.getDB().getReference("Users").child(uid).child("name");
                ref.setValue(realName);
            }
        } else if (view.getId() == btnFavouriteAnimes.getId()) {
            Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
            intent.putExtra("FROM", "PROFILE");
            startActivity(intent);
        } else if (view.getId() == btnLogOut.getId()) {

            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
            startActivity(intent);
        } else if (view.getId() == btnChangePassword.getId()) {
            String old_pass = oldPassword.getText().toString();
            String newPass = newPassword.getText().toString();
            String newPass2 = newPassword2.getText().toString();
            if (newPass.isEmpty() || newPass2.isEmpty() || old_pass.isEmpty()) {
                Toast.makeText(UserProfileActivity.this, "Please fill all the fields!", Toast.LENGTH_SHORT).show();
            } else if (!newPass.equals(newPass2)) {
                Toast.makeText(UserProfileActivity.this, "Password's don't match!", Toast.LENGTH_SHORT).show();
            } else {
                DB.update_password(UserProfileActivity.this, old_pass, newPass);
            }
        }

    }
}

