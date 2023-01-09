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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import AnimEngine.myapplication.creator.CreateActivity;
import AnimEngine.myapplication.client.Engine;
import AnimEngine.myapplication.R;
import AnimEngine.myapplication.logics.Server_logger;
import AnimEngine.myapplication.logics.DB;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {


    Button btnSignIn, btnCreator;
    TextView tvSignUp, forgot_password;
    EditText etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        if (DB.getAU().getCurrentUser() != null) {
            String flag = DB.getAU().getCurrentUser().getDisplayName();
            if (flag.equals("true")) {
                startActivity(new Intent(getApplicationContext(), CreateActivity.class));
                //FirebaseAuth.getInstance().signOut();
            } else if (flag.equals("false")) {
                DB.getDB().getReference("Likes").child(DB.getAU().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Map<String, Object> m = new HashMap<>();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            m.put(dataSnapshot.getKey(), dataSnapshot.getValue());
                        }
                        Intent intent = new Intent(getApplicationContext(), Engine.class);
                        intent.putExtra("Likes", new JSONObject(m).toString());
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        }
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        tvSignUp = (TextView) findViewById(R.id.tvSignUp);
        btnCreator = (Button) findViewById(R.id.btnCreator);
        etEmail = (EditText) findViewById(R.id.etEmail);
        forgot_password = (TextView) findViewById(R.id.tvForgetPassword);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnSignIn.setOnClickListener(this);
        tvSignUp.setOnClickListener(this);
        btnCreator.setOnClickListener(this);
        forgot_password.setOnClickListener(this);
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
                new Server_logger().log_in(email, pass, this);
            }
        } else if (view.getId() == tvSignUp.getId()) {
            Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
            intent.putExtra("Creator", false);
            startActivity(intent);
        } else if (view.getId() == btnCreator.getId()) {
            Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
            intent.putExtra("Creator", true);
            startActivity(intent);
        } else {
            startActivity(new Intent(getApplicationContext(), ChangePasswordActivity.class));
        }
    }

}