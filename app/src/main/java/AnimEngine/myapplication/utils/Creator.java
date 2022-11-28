package AnimEngine.myapplication.utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Creator {
    private String name;
    private String mail;
    private String password;
    private String nickname;
    private List<Anime> animeList;
    private Map<Anime,Long> likes;

    public Creator(String name, String mail, String password,String nickname, List<Anime> animeList, HashMap<Anime, Long> likes) {
        this.name = name;
        this.mail = mail;
        this.password = password;
        this.nickname=nickname;
        this.animeList = animeList;
        this.likes = likes;
    }
    public Creator(String name, String mail, String password,String nickname)
    {
        this.name = name;
        this.mail = mail;
        this.password = password;
        this.nickname=nickname;
        this.animeList=new ArrayList<>();
        this.likes=new HashMap<>();
    }
    public Creator()
    {
        this.name = "name";
        this.mail = "mail";
        this.password = "password";
        this.nickname="nickname";
        this.animeList=new ArrayList<>();
        this.likes=new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Anime> getAnimeList() {
        return animeList;
    }

    public void setAnimeList(List<Anime> animeList) {
        this.animeList = animeList;
    }

    public Map<Anime, Long> getLikes() {
        return likes;
    }

    public void setLikes(HashMap<Anime, Long> likes) {
        this.likes = likes;
    }
    public void InsertUser() {
        FirebaseDatabase db = DB.getDB();
        DatabaseReference myRef = db.getReference("Users").child("creators").child(nickname);
        myRef.setValue(this);
    }
}
