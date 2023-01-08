package AnimEngine.myapplication.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import AnimEngine.myapplication.StorageConnection;
import AnimEngine.myapplication.client.UserProfileActivity;
import AnimEngine.myapplication.creator.CreateActivity;

public class DB {
    private static FirebaseDatabase db = null;
    private static FirebaseAuth au = null;
    private static boolean ans = false;

    public static FirebaseDatabase getDB() {
        if (db == null) {
            db = FirebaseDatabase.getInstance();
        }
        return db;
    }

    public static FirebaseAuth getAU() {
        if (au == null) {
            au = FirebaseAuth.getInstance();
        }
        return au;
    }

    public void setUser(User user) {
        DB.getDB().getReference("Users").child(user.getId()).setValue(user);
    }

    public void setLikes(Map<String, Integer> likes, String uid) {
        DB.getDB().getReference("Likes").child(uid);
    }

    public boolean setAnime(Anime anime, InputStream inputStream) {
        try {

            byte[] inputData = getBytes(inputStream);
            StorageConnection sc = new StorageConnection("images/");
            sc.uploadImage(anime.getAnime_id(), inputData);
            DB.getDB().getReference("Anime").child(anime.getAnime_id()).setValue(anime);
            DB.getDB().getReference("CreatorAnime").child(anime.getCreator_id()).child(anime.getAnime_id()).setValue(anime);
            return true;
            //Toast.makeText(CreateActivity.this, "Anime added successfully.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            return false;
            //Toast.makeText(CreateActivity.this, "Failed to upload the anime.", Toast.LENGTH_SHORT).show();
        }
    }
    public static boolean upload_anime(Anime anime,InputStream input_stream){
        try {

            byte[]  inputData = getBytes(input_stream);
            StorageConnection sc = new StorageConnection("images/");
            sc.uploadImage(anime.getAnime_id(), inputData);
            DB.getDB().getReference("Anime").child(anime.getAnime_id()).setValue(anime);
            DB.getDB().getReference("CreatorAnime").child(anime.getCreator_id()).child(anime.getAnime_id()).setValue(anime.getAnime_id());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public static String getKey() {
        return DB.getDB().getReference("Anime").push().getKey();
    }

    private static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
    public static void upload(String anime_name, int ep, int se, String d, String creator_id, String gens, Uri picture_to_upload, Context context) {
        String[] splits = gens.trim().split(" ");
        List<String> to_send = new ArrayList<>();
        for (int i = 1; i < splits.length; i++) {
            to_send.add(splits[i]);
        }
        String ref = DB.getDB().getReference("Anime").push().getKey();
        Anime anime = new Anime(anime_name, ep, se, d, creator_id, ref, to_send);
        InputStream iStream = null;
        try {
            iStream = context.getContentResolver().openInputStream(picture_to_upload);
            if (upload_anime(anime,iStream)) {
                Toast.makeText(context, "Anime added successfully.", Toast.LENGTH_SHORT).show();
                context.startActivity(new Intent(context.getApplicationContext(), CreateActivity.class));
            } else {
                Toast.makeText(context, "Failed to upload the anime.", Toast.LENGTH_SHORT).show();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(context, "Failed to upload the anime.", Toast.LENGTH_SHORT).show();
        }
    }

    public static void update_password(Context context, String old_pass, String newPass) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String email = user.getEmail();
        AuthCredential credential = EmailAuthProvider.getCredential(email, old_pass);
        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(context, "Something went wrong. Please try again later", Toast.LENGTH_SHORT).show();
                            } else {
                                Map<String, Object> m = new HashMap<>();
                                m.put("password", newPass);
                                DB.getDB().getReference("Users").child(user.getUid()).updateChildren(m).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(context, "Password Successfully Modified", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                } else {
                    Toast.makeText(context, "Authentication Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
