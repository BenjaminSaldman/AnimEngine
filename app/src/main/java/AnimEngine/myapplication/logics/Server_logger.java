package AnimEngine.myapplication.logics;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import AnimEngine.myapplication.client.Engine;
import AnimEngine.myapplication.client.SelectActivity;
import AnimEngine.myapplication.creator.CreateActivity;
import AnimEngine.myapplication.login.SignInActivity;
import AnimEngine.myapplication.utils.User;

public class Server_logger {

    FirebaseAuth myAuth = DB.getAU();


    public void log_in(String email, String pass, Context context) {
        myAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Map<String, Object> m = new HashMap<>();
                            m.put("password", pass);
                            DB.getDB().getReference("Users").child(DB.getAU().getUid()).updateChildren(m).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    boolean isCreator = FirebaseAuth.getInstance().getCurrentUser().getDisplayName().equals("true");
                                    Toast.makeText(context, "Welcome back", Toast.LENGTH_LONG).show();
                                    if (!isCreator) {
                                        DB.getDB().getReference("Likes").child(DB.getAU().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                Map<String, Object> m = new HashMap<>();
                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                    m.put(dataSnapshot.getKey(), dataSnapshot.getValue());
                                                }
                                                Intent intent = new Intent(context.getApplicationContext(), Engine.class);
                                                intent.putExtra("Likes", new JSONObject(m).toString());
                                                context.startActivity(intent);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                    } else {
                                        context.startActivity(new Intent(context.getApplicationContext(), CreateActivity.class));
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(context, "Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void sign_up(String uname, String email, String pass, String nick, boolean isCreator, Context context) {
        String[] Gen = {"Action", "Comedy", "Shonen", "Adventure", "Slice-of-Life", "Drama", "Fantasy", "Horror", "Magic", "Mystery",
                "Sci-Fi", "Psychological", "Supernatural", "Romance", "Crime", "Superhero", "Martial-arts"};
        FirebaseAuth myAuth = DB.getAU();
        myAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user_ = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = task.getResult().getUser().getUid();
                    if (!isCreator) {
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName("false").build();
                        user_.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                User user = new User(uname, email, pass, nick, false, uid);
                                DB.setUser(user);
                                Map<String, Integer> likes = new HashMap<>();
                                for (int i = 0; i < Gen.length; i++) {
                                    likes.put(Gen[i], 0);
                                }
                                DatabaseReference myRef = DB.getDB().getReference("Likes").child(uid);
                                myRef.setValue(likes);
                                Toast.makeText(context, "Welcome to AnimEngine, " + uname, Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(context.getApplicationContext(), SelectActivity.class);
                                intent.putExtra("uid", uid);
                                context.startActivity(intent);
                            }
                        });
                    } else {
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName("true").build();
                        user_.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                User user = new User(uname, email, pass, nick, true, uid);
                                DB.setUser(user);
                                Toast.makeText(context, "Welcome to AnimEngine, " + uname, Toast.LENGTH_SHORT).show();
                                context.startActivity(new Intent(context.getApplicationContext(), SignInActivity.class));
                            }
                        });
                    }


                } else {
                    Toast.makeText(context, "Error " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public static void send_reset_password_mail(String email,Context context){
        FirebaseAuth myAuth=DB.getAU();
        if (email == null || email.isEmpty()) {
            Toast.makeText(context, "Please enter an email", Toast.LENGTH_SHORT).show();

        } else {
            myAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(context, "Reset link sent to your email.", Toast.LENGTH_LONG).show();
                    context.startActivity(new Intent(context.getApplicationContext(), SignInActivity.class));

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "Error, couldn't send a link.", Toast.LENGTH_LONG).show();
                }
            });
        }
    }


}

