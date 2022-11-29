//package AnimEngine.myapplication;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Toast;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//
//import AnimEngine.myapplication.utils.Creator;
//import AnimEngine.myapplication.utils.DB;
//
//public class Creators_login extends AppCompatActivity implements View.OnClickListener{
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_creators_login);
//    }
//    @Override
//    public void onClick(View view) {
//        FirebaseAuth myAuth = DB.getAU();
//        String pass = password.getText().toString().trim();
//        String email = mail.getText().toString().trim();
//        myAuth.signInWithEmailAndPassword(email, pass)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(home_screen.this, "Welcome back !", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(getApplicationContext(), Engine.class));
//                        } else {
//                            Toast.makeText(home_screen.this, "Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//
//    }
//}