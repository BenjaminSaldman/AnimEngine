package AnimEngine.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends AppCompatActivity implements View.OnClickListener {
    FirebaseDatabase root;
    DatabaseReference myRef;
    Button signup;
    EditText nickname, password,mail,name;
    Boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        signup=(Button) findViewById(R.id.sign);
        nickname=(EditText) findViewById(R.id.nick);
        password=(EditText) findViewById(R.id.pass);
        mail=(EditText) findViewById(R.id.mail);
        name=(EditText) findViewById(R.id.name);
        root = FirebaseDatabase.getInstance();
        flag=false;
        myRef = root.getReference("Users").child("clients");
        signup.setOnClickListener(this);
    }
    //public User(String name, String mail, String password, String nickname)
    @Override
    public void onClick(View view) {
        DatabaseReference r = root.getReference("Users").child("clients").child(nickname.getText().toString());
        r.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if(user==null){
                    flag=true;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SignUp.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
        if (flag){
            String uname=name.getText().toString();
            String nick=nickname.getText().toString();
            String pass=password.getText().toString();
            String email=mail.getText().toString();
            if(uname.isEmpty() || nick.isEmpty() || pass.isEmpty() || email.isEmpty())
            {
                Toast.makeText(SignUp.this,"Please fill all the fields!",Toast.LENGTH_SHORT);
            }
            else{
                User user=new User(uname, email,  pass,  nick);
                myRef = root.getReference("Users").child("clients").child(nick);
                myRef.setValue(user);
                Toast.makeText(SignUp.this,"Welcome to AnimEngine, "+nick,Toast.LENGTH_LONG);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent=new Intent(getApplicationContext(),home_screen.class);
                startActivity(intent);
            }

        }else{
            Toast.makeText(SignUp.this,"Username already taken!",Toast.LENGTH_SHORT);
        }


    }
}