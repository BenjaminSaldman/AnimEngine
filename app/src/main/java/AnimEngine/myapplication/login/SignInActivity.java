package AnimEngine.myapplication.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import AnimEngine.myapplication.creator.CreateActivity;
import AnimEngine.myapplication.client.Engine;
import AnimEngine.myapplication.R;
import AnimEngine.myapplication.ChangePasswordActivity;
import AnimEngine.myapplication.utils.DB;
import AnimEngine.myapplication.utils.User;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {


    Button btnSignIn, btnCreator;
    TextView tvSignUp;
    EditText etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        if (DB.getAU().getCurrentUser() != null){
            String flag=DB.getAU().getCurrentUser().getDisplayName();
            if (flag.equals("true")){
                startActivity(new Intent(getApplicationContext(), CreateActivity.class));
                //FirebaseAuth.getInstance().signOut();
            } else if (flag.equals("false")){
                startActivity(new Intent(getApplicationContext(), Engine.class));
            }
        }
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        tvSignUp = (TextView) findViewById(R.id.tvSignUp);
        btnCreator = (Button) findViewById(R.id.btnCreator);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnSignIn.setOnClickListener(this);
        tvSignUp.setOnClickListener(this);
        btnCreator.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == btnSignIn.getId()) {
            FirebaseAuth myAuth = DB.getAU();
            //myAuth=FirebaseAuth.getInstance();
            String pass = etPassword.getText().toString();
            String email = etEmail.getText().toString();
            if (pass.isEmpty() || email.isEmpty()) {
                Toast.makeText(SignInActivity.this, "Please enter all the fields.", Toast.LENGTH_SHORT).show();
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
                                                Toast.makeText(SignInActivity.this, "Welcome back, " + user.getNickname(), Toast.LENGTH_LONG).show();
                                                if (!user.isCreator()) {
                                                    startActivity(new Intent(getApplicationContext(), Engine.class));
                                                } else {
                                                    startActivity(new Intent(getApplicationContext(), CreateActivity.class));
                                                }
                                            } else {
                                                Toast.makeText(SignInActivity.this, "Failed to get data.", Toast.LENGTH_SHORT).show();
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(SignInActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();

                                        }
                                    });

                                } else {
                                    Toast.makeText(SignInActivity.this, "Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }
        else if (view.getId() == tvSignUp.getId()) {
            Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
            intent.putExtra("Creator",false);
            startActivity(intent);
        }

        else if (view.getId() == btnCreator.getId()) {
            Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
            intent.putExtra("Creator",true);
            startActivity(intent);
        }else{
            startActivity(new Intent(getApplicationContext(), ChangePasswordActivity.class));
        }
    }
}
