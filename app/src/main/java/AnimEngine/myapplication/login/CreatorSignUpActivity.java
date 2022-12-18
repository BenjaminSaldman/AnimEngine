package AnimEngine.myapplication.login;

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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import AnimEngine.myapplication.R;
import AnimEngine.myapplication.utils.DB;
import AnimEngine.myapplication.utils.User;

public class CreatorSignUpActivity extends AppCompatActivity implements View.OnClickListener {
    Button signup,back;
    EditText nickname, password, mail, name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creators_sign_up);
        signup = (Button) findViewById(R.id.sign);
        back = (Button) findViewById(R.id.back);
        nickname = (EditText) findViewById(R.id.nick);
        password = (EditText) findViewById(R.id.pass);
        mail = (EditText) findViewById(R.id.mail);
        name = (EditText) findViewById(R.id.name);
        signup.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==signup.getId()) {
            String uname = name.getText().toString();
            String nick = nickname.getText().toString();
            String pass = password.getText().toString();
            String email = mail.getText().toString();
            if (uname.isEmpty() || nick.isEmpty() || pass.isEmpty() || email.isEmpty()) {
                Toast.makeText(CreatorSignUpActivity.this, "Please fill all the fields!", Toast.LENGTH_SHORT).show();
            } else {
                FirebaseAuth myAuth = DB.getAU();
                myAuth.createUserWithEmailAndPassword(mail.getText().toString(), password.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user_ = FirebaseAuth.getInstance().getCurrentUser();
                            String uid = task.getResult().getUser().getUid();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName("true").build();
                            user_.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    User user = new User(uname, email, pass, nick, true, uid);
                                    user.InsertUser();
                                    Toast.makeText(CreatorSignUpActivity.this, "Welcome to AnimEngine, "+uname, Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                                }
                            });


                        } else {
                            Toast.makeText(CreatorSignUpActivity.this, "Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }else{
            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
        }

    }
}