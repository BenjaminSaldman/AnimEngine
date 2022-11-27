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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class home_screen extends AppCompatActivity implements View.OnClickListener {


    FirebaseDatabase root;
    DatabaseReference myRef;
    Button login, signup;
    EditText nickname, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        login = (Button) findViewById(R.id.log_btn);
        signup = (Button) findViewById(R.id.register);
        nickname = (EditText) findViewById(R.id.nickname);
        password = (EditText) findViewById(R.id.password);
        root = FirebaseDatabase.getInstance();
        myRef = root.getReference("Users").child("clients");
        login.setOnClickListener(this);
        signup.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == login.getId()) {
            DatabaseReference r = root.getReference("Users").child("clients").child(nickname.getText().toString());
            r.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    if(user==null){
                        Log.d("Here","null");
                        Toast.makeText(home_screen.this,"User: "+nickname.getText()+" Isn't exist!",Toast.LENGTH_LONG);
                    }
                    else if (user.getPassword().equals(password.getText().toString()))
                    {
                        Toast.makeText(home_screen.this,"Welcome back, "+user.getName(),Toast.LENGTH_LONG);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Intent intent =new Intent(getApplicationContext(),Engine.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(home_screen.this,"Incorrect password!",Toast.LENGTH_LONG);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(home_screen.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Intent intent=new Intent(getApplicationContext(), SignUp.class);
            startActivity(intent);

        }

    }
}