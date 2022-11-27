package AnimEngine.myapplication;

import java.util.HashMap;
import java.util.List;

public class Creator {
    private String name;
    private String mail;
    private String password;
    private List<Anime> animeList;
    private HashMap<Anime,Long> likes;

    public Creator(String name, String mail, String password, List<Anime> animeList, HashMap<Anime, Long> likes) {
        this.name = name;
        this.mail = mail;
        this.password = password;
        this.animeList = animeList;
        this.likes = likes;
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

    public HashMap<Anime, Long> getLikes() {
        return likes;
    }

    public void setLikes(HashMap<Anime, Long> likes) {
        this.likes = likes;
    }
}
