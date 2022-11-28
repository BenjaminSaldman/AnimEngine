package AnimEngine.myapplication.utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Favorites {
    private List<String> favorites;
    private String nickname;

    public Favorites(String nickname) {
        this.nickname = nickname;
        this.favorites = new ArrayList<>();
    }

    public Favorites(String nickname, List<String> favorites) {
        this.nickname = nickname;
        this.favorites = favorites;
    }

    public Favorites() {
        this.nickname = "nickname";
        this.favorites = new ArrayList<>();
    }

    public List<String> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<String> favorites) {
        this.favorites = favorites;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void InsertDB(){
        FirebaseDatabase db = DB.getDB();
        DatabaseReference myRef = db.getReference("Favorites").child(nickname);
        myRef.setValue(this);
    }
}
