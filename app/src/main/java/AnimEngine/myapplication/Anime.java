package AnimEngine.myapplication;

public class Anime {
    private String name;
    private long likes;
    private long dislikes;
    private String description;
    private int img_id;

    public Anime(String name, long likes, long dislikes, String description, int img_id) {
        this.name = name;
        this.likes = likes;
        this.dislikes = dislikes;
        this.description = description;
        this.img_id = img_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public long getDislikes() {
        return dislikes;
    }

    public void setDislikes(long dislikes) {
        this.dislikes = dislikes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImg_id() {
        return img_id;
    }

    public void setImg_id(int img_id) {
        this.img_id = img_id;
    }


}
