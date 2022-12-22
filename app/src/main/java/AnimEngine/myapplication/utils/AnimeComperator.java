package AnimEngine.myapplication.utils;

import android.util.Log;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class AnimeComperator implements Comparator<Anime> {
    private Map<String,Integer> likes;
    public AnimeComperator()
    {
        this.likes=new HashMap<>();
    }
    public AnimeComperator(Map<String,Integer> likes){
        this.likes=likes;
    }
    public void setLikes(Map<String,Integer> likes){
        this.likes=likes;
    }

    @Override
    public int compare(Anime anime1, Anime anime2) {
        int sum1=0,sum2=0;
        Log.d("OKWTF?",likes+"");
        for (String i: anime1.getGenres()){
            sum1+=likes.get(i);
        }
        for (String i: anime2.getGenres()){
            sum2+=likes.get(i);
        }

        return  (sum1-sum2);
    }
}
