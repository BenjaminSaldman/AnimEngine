package AnimEngine.myapplication.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import AnimEngine.myapplication.R;
import AnimEngine.myapplication.client.SelectActivity;
import AnimEngine.myapplication.creator.CreatorProfileActivity;
import AnimEngine.myapplication.utils.DB;
import AnimEngine.myapplication.utils.User;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    FirebaseDatabase root;
    DatabaseReference myRef;
    Button signup;
    EditText etNickname, etPassword, etEmail, etUserName;
    TextView back;
    boolean isCreator;
    String[] Gen = {"Action", "Comedy", "Shonen", "Adventure", "Slice-of-Life", "Drama", "Fantasy", "Horror", "Magic", "Mystery",
            "Sci-Fi", "Psychological", "Supernatural", "Romance", "Crime", "Superhero", "Martial-arts"};
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        signup = (Button) findViewById(R.id.btnSignUp);
        back = (TextView) findViewById(R.id.tvSignIn);
        etNickname = (EditText) findViewById(R.id.etNickname);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etEmail = findViewById(R.id.etEmail);
        etUserName = findViewById(R.id.etUserName);
        isCreator=false;
        Bundle extra = getIntent().getExtras();
        try {
            isCreator = extra.getBoolean("Creator");
        } catch (Exception e) {
            e.printStackTrace();
        }
//        root= DB.getDB();
        signup.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == signup.getId()) {
            String uname = etUserName.getText().toString();
            String nick = etNickname.getText().toString();
            String pass = etPassword.getText().toString();
            String email = etEmail.getText().toString();

            if (uname.isEmpty() || nick.isEmpty() || pass.isEmpty() || email.isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Please fill all the fields!", Toast.LENGTH_SHORT).show();
            }
            else if (nick.length() > 12) {
                Toast.makeText(SignUpActivity.this, "Nickname too long", Toast.LENGTH_SHORT).show();
            }
            else if (uname.length() > 16) {
                Toast.makeText(SignUpActivity.this, "Name too long", Toast.LENGTH_SHORT).show();
            }
            else {
                FirebaseAuth myAuth = DB.getAU();
                myAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user_ = FirebaseAuth.getInstance().getCurrentUser();
                            String uid = task.getResult().getUser().getUid();
                            if (!isCreator) {
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName("false").build();
                                user_.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        User user = new User(uname, email, pass, nick, false, uid);
                                        user.InsertUser();
                                        Map<String,Integer>likes = new HashMap<>();
                                        for (int i = 0; i < Gen.length; i++) {
                                            likes.put(Gen[i], 0);
                                        }
                                        DatabaseReference myRef = DB.getDB().getReference("Likes").child(uid);
                                        myRef.setValue(likes);
                                        Toast.makeText(SignUpActivity.this, "Welcome to AnimEngine, " + uname, Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(getApplicationContext(), SelectActivity.class);
                                        intent.putExtra("uid", uid);
                                        startActivity(intent);
                                    }
                                });
                            } else {
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName("true").build();
                                user_.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        User user = new User(uname, email, pass, nick, true, uid);
                                        user.InsertUser();
                                        Toast.makeText(SignUpActivity.this, "Welcome to AnimEngine, " + uname, Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                                    }
                                });
                            }


                        } else {
                            Toast.makeText(SignUpActivity.this, "Error " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

        } else {
            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
        }

    }
//
//            String uname=name.getText().toString();
//            String nick=nickname.getText().toString();
//            String pass=password.getText().toString();
//            String email=mail.getText().toString();
//            if(uname.isEmpty() || nick.isEmpty() || pass.isEmpty() || email.isEmpty())
//            {
//                Toast.makeText(SignUp.this,"Please fill all the fields!",Toast.LENGTH_SHORT).show();
//            }
//            else if(DB.validator(nickname.getText().toString(),true)){
//                Log.d("key2",DB.validator(nickname.getText().toString(),true)+"");
//                User user=new User(uname, email,  pass,  nick);
//                user.InsertUser();
//                Toast.makeText(SignUp.this,"Welcome to AnimEngine, "+nick,Toast.LENGTH_LONG).show();
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                Intent intent=new Intent(getApplicationContext(),home_screen.class);
//                startActivity(intent);
//            }else{
//                Log.d("ok","ok");
//                Toast.makeText(SignUp.this,"Username already taken!",Toast.LENGTH_SHORT).show();
//            }
//        }
//        DatabaseReference r = DB.getDB().getReference("Users").child("clients").child(nickname.getText().toString());
//        r.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(!snapshot.exists()){
//                    String uname=name.getText().toString();
//                    String nick=nickname.getText().toString();
//                    String pass=password.getText().toString();
//                    String email=mail.getText().toString();
//                    if(uname.isEmpty() || nick.isEmpty() || pass.isEmpty() || email.isEmpty())
//                    {
//                        Toast.makeText(SignUp.this,"Please fill all the fields!",Toast.LENGTH_SHORT).show();
//                    }
//                    else{
//                        User user=new User(uname, email,  pass,  nick);
//                        user.InsertUser();
//                        Toast.makeText(SignUp.this,"Welcome to AnimEngine, "+nick,Toast.LENGTH_LONG).show();
//                        try {
//                            Thread.sleep(500);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        Intent intent=new Intent(getApplicationContext(),home_screen.class);
//                        startActivity(intent);
//                    }
//                }else{
//                    Toast.makeText(SignUp.this,"Username already taken!",Toast.LENGTH_SHORT).show();
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(SignUp.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
//            }
//        });
}
