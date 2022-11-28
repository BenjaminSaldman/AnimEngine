package AnimEngine.myapplication.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import AnimEngine.myapplication.Engine;
import AnimEngine.myapplication.home_screen;

public class DB {
    private static FirebaseDatabase db = null;
    private static FirebaseAuth au=null;
    private static boolean ans = false;

    public static FirebaseDatabase getDB() {
        if (db == null) {
            db = FirebaseDatabase.getInstance();
        }
        return db;
    }
    public static FirebaseAuth getAU(){
        if (au == null) {
            au = FirebaseAuth.getInstance();
        }
        return au;
    }
    static boolean valid=false;
    public static boolean validator(String nickname, boolean user) {

        try {
            if (db == null) {
                db = FirebaseDatabase.getInstance();
            }
            DatabaseReference MyRef;
            if (user) {
                MyRef = db.getReference("Users").child("clients").child(nickname);
            } else {
                MyRef = db.getReference("Users").child("creators").child(nickname);
            }
            MyRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        if (!snapshot.exists()) {
                            Log.d("key","??");
                            valid=true;
                            return;
                        } else {
                            valid=false;
                        }
                    } catch (Exception e) {
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        catch (Exception e) {
            throw new RuntimeException("User already exist!");
        }
        //throw new RuntimeException("User already exist!");
        Log.d("key",""+valid);
        return valid;
    }

    public static void logger(String nickname, String password, boolean user, Context context) {


        try {
            if (db == null) {
                db = FirebaseDatabase.getInstance();
            }
            DatabaseReference MyRef;
            if (user) {
                MyRef = db.getReference("Users").child("clients").child(nickname);
            } else {
                MyRef = db.getReference("Users").child("creators").child(nickname);
            }
            MyRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        if (!snapshot.exists()) {
                            throw new RuntimeException("no user");
                        } else {
                            if (user){
                                User u=snapshot.getValue(User.class);
                                if (u.getPassword().equals(password)){
//                                    Toast.makeText(context, "Welcome back, " + nickname, Toast.LENGTH_LONG).show();
//                                    //Thread.sleep(500);
//                                    Intent intent = new Intent(context.getApplicationContext(), Engine.class);
//                                    context.getApplicationContext().startActivity(intent);
//                                    return;
                                    throw new RuntimeException("OK");
                                }else{
                                    throw new RuntimeException("No match");
                                }
                            }else{
                                Creator c=snapshot.getValue(Creator.class);
                                if (c.getPassword().equals(password)){
                                    return;
                                }else{
                                    throw new RuntimeException("No match");
                                }
                            }

                        }
                    } catch (Exception e) {
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    try{
                        throw new RuntimeException("Error");
                    }catch (Exception e)
                    {

                    }
                }
            });

        } catch (Exception e) {

        }
    }

}
