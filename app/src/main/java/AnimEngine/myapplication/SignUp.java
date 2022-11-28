package AnimEngine.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import AnimEngine.myapplication.utils.DB;
import AnimEngine.myapplication.utils.User;

public class SignUp extends AppCompatActivity implements View.OnClickListener {
    FirebaseDatabase root;
    DatabaseReference myRef;
    Button signup;
    EditText nickname, password,mail,name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        signup=(Button) findViewById(R.id.sign);
        nickname=(EditText) findViewById(R.id.nick);
        password=(EditText) findViewById(R.id.pass);
        mail=(EditText) findViewById(R.id.mail);
        name=(EditText) findViewById(R.id.name);
//        root= DB.getDB();
        signup.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
            String uname=name.getText().toString();
            String nick=nickname.getText().toString();
            String pass=password.getText().toString();
            String email=mail.getText().toString();
            if(uname.isEmpty() || nick.isEmpty() || pass.isEmpty() || email.isEmpty())
            {
                Toast.makeText(SignUp.this,"Please fill all the fields!",Toast.LENGTH_SHORT).show();
            }else{
                FirebaseAuth myAuth=DB.getAU();
                myAuth.createUserWithEmailAndPassword(mail.getText().toString(),password.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user=new User(uname, email,  pass,  nick);
                            user.InsertUser();
                            Toast.makeText(SignUp.this,"User created",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),home_screen.class));

                        } else {
                            Toast.makeText(SignUp.this,"Error "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
