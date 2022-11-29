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
import com.google.firebase.database.ValueEventListener;

import AnimEngine.myapplication.utils.DB;
import AnimEngine.myapplication.utils.User;

public class home_screen extends AppCompatActivity implements View.OnClickListener {


    Button login, signup,creator_sign,reset;
    EditText mail, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        login = (Button) findViewById(R.id.log_btn);
        signup = (Button) findViewById(R.id.register);
        creator_sign = (Button) findViewById(R.id.signCreator);
        reset= (Button) findViewById(R.id.change_btn);
        mail = (EditText) findViewById(R.id.mail);
        password = (EditText) findViewById(R.id.password);
        login.setOnClickListener(this);
        signup.setOnClickListener(this);
        creator_sign.setOnClickListener(this);
        reset.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == login.getId()) {
            FirebaseAuth myAuth = DB.getAU();

//            String pass = password.getText().toString().trim();
//            String email = mail.getText().toString().trim();
            String pass = password.getText().toString();
            String email = mail.getText().toString();
            if (pass.isEmpty() || email.isEmpty()) {
                Toast.makeText(home_screen.this, "Please enter all the fields.", Toast.LENGTH_SHORT).show();
            } else {
                myAuth.signInWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    DatabaseReference myRef = DB.getDB().getReference("Users").child(task.getResult().getUser().getUid());
                                    myRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            User user = snapshot.getValue(User.class);
                                            if (user != null) {
                                                Toast.makeText(home_screen.this, "Welcome back, " + user.getNickname(), Toast.LENGTH_LONG).show();
                                                if (!user.isCreator()) {
                                                    startActivity(new Intent(getApplicationContext(), Engine.class));
                                                } else {
                                                    startActivity(new Intent(getApplicationContext(), Creators_SignUp.class));
                                                }
                                            } else {
                                                Toast.makeText(home_screen.this, "Failed to get data.", Toast.LENGTH_SHORT).show();
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(home_screen.this, error.getMessage(), Toast.LENGTH_LONG).show();

                                        }
                                    });

                                } else {
                                    Toast.makeText(home_screen.this, "Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }
        else if (view.getId() == signup.getId()) {
            Intent intent = new Intent(getApplicationContext(), SignUp.class);
            startActivity(intent);
        }

        else if (view == creator_sign) {
            Intent intent = new Intent(getApplicationContext(), Creators_SignUp.class);
            startActivity(intent);
        }else{
            startActivity(new Intent(getApplicationContext(),change_password.class));
        }

    }

}

