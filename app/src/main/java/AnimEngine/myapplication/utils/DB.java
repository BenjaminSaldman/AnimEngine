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



}
