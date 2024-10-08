package AnimEngine.myapplication.utils;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class Anime implements Parcelable {
    private String name;
    private long likes;
    private long dislikes;
    private String description;
    private String creator_id;
    private String anime_id;
    private int episodes;
    private int seasons;
    private List<String> genres;

    public Anime(String name,String creator_id,String anime_id, long likes, long dislikes, String description, int episodes,int seasons,List<String> genres) {
        this.name = name;
        this.creator_id=creator_id;
        this.likes = likes;
        this.dislikes = dislikes;
        this.description = description;
        this.episodes = episodes;
        this.seasons=seasons;
        this.genres=genres;
        this.anime_id=anime_id;
    }

    public Anime() {
        this.name = "name";
        this.creator_id="id";
        this.likes = 0;
        this.dislikes = 0;
        this.description = "description";
        this.episodes = 0;
        this.seasons=0;
        this.genres=new ArrayList<>();
        this.anime_id="anime_id";
    }

    public Anime(String name,int episodes,int seasons,String description,String creator_id,String anime_id,List<String> genres)
    {
        this.name=name;
        this.episodes=episodes;
        this.anime_id=anime_id;
        this.seasons=seasons;
        this.description=description;
        this.creator_id=creator_id;
        this.genres=genres;
    }

    public Anime(String name){
        this.name=name;
    }

    protected Anime(Parcel in) {
        name = in.readString();
        likes = in.readLong();
        dislikes = in.readLong();
        description = in.readString();
        creator_id = in.readString();
        anime_id = in.readString();
        episodes = in.readInt();
        seasons = in.readInt();
        genres = in.createStringArrayList();
    }

    public static final Creator<Anime> CREATOR = new Creator<Anime>() {
        @Override
        public Anime createFromParcel(Parcel in) {
            return new Anime(in);
        }

        @Override
        public Anime[] newArray(int size) {
            return new Anime[size];
        }
    };

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

    public int getEpisodes() {
        return episodes;
    }

    public void setEpisodes(int episodes) {
        this.episodes = episodes;
    }

    public List<String> getGenres() {
        return genres;
    }

//    public String getCreator() {
//        return creator_id;
//    }

//    public void setCreator(String creator) {
//        this.creator_id = creator;
//    }

    public String getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(String creator_id) {
        this.creator_id = creator_id;
    }

    public String getAnime_id() {
        return anime_id;
    }

    public void setAnime_id(String anime_id) {
        this.anime_id = anime_id;
    }

    public int getSeasons() {
        return seasons;
    }

    public void setSeasons(int seasons) {
        this.seasons = seasons;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(name);
        parcel.writeLong(likes);
        parcel.writeLong(dislikes);
        parcel.writeString(description);
        parcel.writeString(creator_id);
        parcel.writeString(anime_id);
        parcel.writeInt(episodes);
        parcel.writeInt(seasons);
        parcel.writeStringList(genres);
    }


}
