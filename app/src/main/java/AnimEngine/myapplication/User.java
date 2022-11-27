package AnimEngine.myapplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {

    private String name;
    private String mail;
    private String password;
    private String nickname;
    private Map<String, Integer> genres;
    private List<Anime> likes;
    final String[] Gen = {"Action", "Comedy", "Shonen", "Adventure", "Slice of Life", "Drama", "Fantasy", "Horror", "Magic", "Mystery",
            "Sci-Fi", "Psychological", "Supernatural", "Romance", "Crime"};

    public User(String name, String mail, String password, String nickname) {
        this.name = name;
        this.mail = mail;
        this.password = password;
        this.nickname = nickname;
        genres = new HashMap<>();
        for (int i = 0; i < Gen.length; i++) {
            genres.put(Gen[i], 0);
        }
        likes = new ArrayList<>();
    }

    public User() {
        this.name = "name";
        this.mail = "mail";
        this.password = "password";
        this.nickname = "nickname";
        genres = new HashMap<>();
        for (int i = 0; i < Gen.length; i++) {
            genres.put(Gen[i], 0);
        }
        likes = new ArrayList<>();
    }

    public User(String name, String mail, String password, String nickname, Map<String, Integer> genres, List<Anime> likes) {
        this.name = name;
        this.mail = mail;
        this.password = password;
        this.nickname = nickname;
        this.genres = new HashMap<>();
        this.likes = new ArrayList<>();
        for (Map.Entry<String, Integer> set : genres.entrySet()) {
            this.genres.put(set.getKey(), set.getValue());
        }
        this.likes.addAll(likes);

    }

    public User(User other) {
        this.name = other.getName();
        this.mail = other.getMail();
        this.password = other.getPassword();
        this.genres = other.getGenres();
        this.likes = other.getLikes();
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

    public Map<String, Integer> getGenres() {
        return genres;
    }

    public List<Anime> getLikes() {
        return likes;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setGenres(HashMap<String, Integer> genres) {
        this.genres = genres;
    }

    public void setLikes(List<Anime> likes) {
        this.likes = likes;
    }

    public void addLike(String genre) {
        int amount=this.genres.get(genre);
        this.genres.put(genre,amount+1);
    }
}
