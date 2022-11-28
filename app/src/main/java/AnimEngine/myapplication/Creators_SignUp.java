package AnimEngine.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

import AnimEngine.myapplication.utils.Creator;
import AnimEngine.myapplication.utils.DB;
import AnimEngine.myapplication.utils.User;

public class Creators_SignUp extends AppCompatActivity implements View.OnClickListener {
    FirebaseDatabase root;
    DatabaseReference myRef;
    Button signup;
    EditText nickname, password, mail, name;
    Boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creators_sign_up);
        signup = (Button) findViewById(R.id.sign);
        nickname = (EditText) findViewById(R.id.nick);
        password = (EditText) findViewById(R.id.pass);
        mail = (EditText) findViewById(R.id.mail);
        name = (EditText) findViewById(R.id.name);
        root = FirebaseDatabase.getInstance();
        flag = false;
        myRef = root.getReference("Users").child("creators");
        signup.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String uname = name.getText().toString();
        String nick = nickname.getText().toString();
        String pass = password.getText().toString();
        String email = mail.getText().toString();
        if (uname.isEmpty() || nick.isEmpty() || pass.isEmpty() || email.isEmpty()) {
            Toast.makeText(Creators_SignUp.this, "Please fill all the fields!", Toast.LENGTH_SHORT).show();
        } else {
            FirebaseAuth myAuth = DB.getAU();
            myAuth.createUserWithEmailAndPassword(mail.getText().toString(), password.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Creator creator = new Creator(uname, email, pass, nick);
                        creator.InsertUser();
                        Toast.makeText(Creators_SignUp.this, "User created", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), home_screen.class));

                    } else {
                        Toast.makeText(Creators_SignUp.this, "Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
}