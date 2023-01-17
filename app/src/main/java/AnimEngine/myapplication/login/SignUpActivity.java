package AnimEngine.myapplication.login;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.ExecutionException;

import AnimEngine.myapplication.R;
import AnimEngine.myapplication.logics.Server_signer;

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
                try {
                    if(!(boolean)new Server_signer().execute(new Object[]{uname,email,pass,nick,isCreator,this}).get()){
                        Toast.makeText(SignUpActivity.this,"Error, this email already exists",Toast.LENGTH_SHORT).show();
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // new Server_signer().sign_up(uname,email,pass,nick,isCreator,this);

            }

        } else {
            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
        }

    }
}
