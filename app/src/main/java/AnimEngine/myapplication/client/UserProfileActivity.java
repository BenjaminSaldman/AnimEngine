package AnimEngine.myapplication.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import AnimEngine.myapplication.R;
import AnimEngine.myapplication.utils.DB;
import AnimEngine.myapplication.utils.User;




public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {

        TextView tvNickname, tvRealName, tvEmail;
        ImageButton ibEditNickname, ibEditRealName;
        Button btnFavouriteAnimes;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_user_profile);

            FirebaseAuth myAuth = DB.getAU();
            tvNickname = (TextView) findViewById(R.id.animeNickname);
            tvRealName = (TextView) findViewById(R.id.animeRealName);
            tvEmail = (TextView) findViewById(R.id.tvEmail);
            ibEditNickname = (ImageButton) findViewById(R.id.ibEditNickname);
            ibEditRealName = (ImageButton) findViewById(R.id.ibEditRealName);
            btnFavouriteAnimes = (Button) findViewById(R.id.btnFavouriteAnimes);
            ibEditNickname.setOnClickListener(this);
            ibEditRealName.setOnClickListener(this);
            btnFavouriteAnimes.setOnClickListener(this);

            String uid = myAuth.getUid();
            DatabaseReference myRef = DB.getDB().getReference("Users").child(uid);
            ValueEventListener valueEventListener = new ValueEventListener() { // myRef.addValueEventListener(new ValueEventListener(){..};

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    String userId = user.getId(); // String userId = snapshot.child("id").getValue(String.class);
                    String userMail = user.getMail(); // String userMail = snapshot.child("mail").getValue(String.class);
                    String userName = user.getName(); // String userName = snapshot.child("name").getValue(String.class);
                    String userNickname = user.getNickname(); // String userNickname = snapshot.child("nickname").getValue(String.class);
                    String userPassword = user.getPassword(); // String userPassword = snapshot.child("password").getValue(String.class);
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    /**
                     * The Log class lets you create log messages that appear in Logcat. Every Android log message has a tag and a priority
                     * associated with it. The tag of a system log message is a short string indicating the system component from which the
                     * message originates.
                     * In this case we used a warning Log.
                     */
                    Log.w("loadUser:onCancelled", error.toException());
                }
            };
            myRef.addListenerForSingleValueEvent(valueEventListener);
        }

        @Override
        /**
         * Interface definition for a callback to be invoked when a view is clicked
         * Called when a view has been clicked
         */
        public void onClick(View view) {
            if (view.getId() == ibEditNickname.getId()) {
                String nickname = tvNickname.getText().toString();
                if (nickname.isEmpty()) { // The nickname can't be ""
                    Toast.makeText(UserProfileActivity.this, "Please choose another nickname", Toast.LENGTH_SHORT).show();
                } else { // It means that the user chose a proper nickname which is not ""
                    String uid = DB.getAU().getUid();
                    DatabaseReference ref = DB.getDB().getReference("Users").child(uid).child("nickname");
                    ref.setValue(nickname);
                }
            }
            else if (view.getId() == ibEditRealName.getId()) {
                String realName = tvRealName.getText().toString();
                if (realName.isEmpty()) { // The real name can't be ""
                    Toast.makeText(UserProfileActivity.this, "Please choose another name", Toast.LENGTH_SHORT).show();
                } else { // It means that the user chose a proper name which is not ""
                    String uid = DB.getAU().getUid();
                    DatabaseReference ref = DB.getDB().getReference("Users").child(uid).child("name");
                    ref.setValue(realName);
                }
            } else { // view.getId() == btnFavouriteAnimes.getId()
            /*
            Intent intent = new Intent(getApplicationContext(), Favourites.class);
            startActivity(intent);
             */
            }
        }
}