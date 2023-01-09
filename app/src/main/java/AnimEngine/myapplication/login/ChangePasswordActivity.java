package AnimEngine.myapplication.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import AnimEngine.myapplication.R;
import AnimEngine.myapplication.logics.Server_logger;
import AnimEngine.myapplication.login.SignInActivity;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {
    Button change, go_back;
    EditText mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        change = (Button) findViewById(R.id.reset_btn);
        mail = (EditText) findViewById(R.id.reset_mail);
        go_back = (Button) findViewById(R.id.back_btn);
        go_back.setOnClickListener(this);
        change.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == change.getId()) {
            String email = mail.getText().toString().trim();
            Server_logger.send_reset_password_mail(email, this);
        } else {
            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
        }

    }
}