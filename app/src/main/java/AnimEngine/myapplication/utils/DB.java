package AnimEngine.myapplication.utils;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import AnimEngine.myapplication.StorageConnection;

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
    public void setLikes(Map<String,Integer> likes,String uid){
        DB.getDB().getReference("Likes").child(uid);
    }
    public boolean setAnime(Anime anime, InputStream inputStream){
        try {

            byte[]  inputData = getBytes(inputStream);
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
    public static String getKey(){
        return DB.getDB().getReference("Anime").push().getKey();
    }

    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }




}
