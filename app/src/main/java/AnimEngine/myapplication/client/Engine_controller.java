package AnimEngine.myapplication.client;

import android.os.Build;
import android.util.Log;
import android.util.*;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import AnimEngine.myapplication.utils.Anime;
import AnimEngine.myapplication.utils.AnimeComperator;
import AnimEngine.myapplication.utils.DB;

public class Engine_controller extends ViewModel {
    Map<String, Integer> likes;
    ArrayList<String> favourites;
    ArrayList<String> disliked;
    ArrayList<Anime> disliked_anime;
    PriorityQueue<Anime> queue;
    AnimeComperator comparator;
    Anime current_anime;
    private MutableLiveData<Anime> shared_anime;


    public Engine_controller() {
        likes = new HashMap<>();
        favourites = new ArrayList<>();
        disliked = new ArrayList<>();
        disliked_anime = new ArrayList<>();
        queue = new PriorityQueue<>();
        comparator = new AnimeComperator();
        List<String> gen=new ArrayList<String>();
        gen.add("Action");
        gen.add("Comedy");
        gen.add("Adventure");
        gen.add("Martial-arts");
        current_anime = new Anime("Naruto","rWPlvJOc7VffIA2CPUYriqkxkQI2","-NK4zPvTHAmEvFcws9US",6,5,"The tale of naruto uzumaki",720,16,gen);
        shared_anime = new MutableLiveData<>();
        DB.getDB().getReference("Likes").child(DB.getAU().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, Object> m = new HashMap<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    m.put(dataSnapshot.getKey(), dataSnapshot.getValue());
                }
                for (String i : m.keySet()) {
                    likes.put(i, Math.toIntExact((Long) m.get(i)));
                }
                comparator = new AnimeComperator(likes);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    queue = new PriorityQueue<>(comparator);
                }
                DB.getDB().getReference("Favourites").child(DB.getAU().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            favourites.add(dataSnapshot.getKey());
                        }
                        DB.getDB().getReference("Disliked").child(DB.getAU().getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    disliked.add(dataSnapshot.getKey());

                                }
                                listen_to_anime();
                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        listen_to_anime();
    }

    public LiveData<Anime> getUsers() {
        listen_to_anime();
        getCurrent_anime();
        return shared_anime;
    }

    public void listen_to_anime() {
        System.out.println("IMHERE");
        DB.getDB().getReference("Anime").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                comparator.setLikes(likes);
                System.out.println("IMHERE");

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Anime anime = dataSnapshot.getValue(Anime.class);
                    if (!favourites.contains(anime.getAnime_id()) && !contain(disliked_anime, anime.getAnime_id())) {
                        queue.add(anime);
                        if (current_anime==null || current_anime.getAnime_id().equals("anime_id")){
                            current_anime= queue.poll();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private boolean contain(ArrayList<Anime> arr, String id) {
        for (Anime anime : arr) {
            if (anime.getAnime_id().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public Anime getCurrent_anime() {
        if (!(current_anime == null) && !(current_anime.getAnime_id().equals("anime_id"))) {
            if (!queue.isEmpty()) {
                this.current_anime = queue.poll();
            }
        }
        shared_anime.setValue(current_anime);
        return this.current_anime;
    }

    public void like_set(boolean isLike) {
        if (!(current_anime == null) && !(current_anime.getAnime_id().equals("anime_id"))) {

            if (isLike) {
                if (!likes.isEmpty() && !favourites.contains(current_anime.getAnime_id())) {

                    Long like_update = current_anime.getLikes() + 1;
                    favourites.add(current_anime.getAnime_id());
                    for (String i : current_anime.getGenres()) {
                        likes.put(i, likes.get(i) + 1);
                    }
                    Map<String, Object> m = new HashMap<>();
                    m.put("likes", like_update);
                    DB.getDB().getReference("Anime").child(current_anime.getAnime_id()).updateChildren(m);
                    DB.getDB().getReference("Likes").child(DB.getAU().getUid()).updateChildren(new HashMap<>(likes));
                    Map<String, Object> m2 = new HashMap<>();
                    for (String i : favourites) {
                        m2.put(i, i);
                    }
                    comparator.setLikes(likes);
                    DB.getDB().getReference("Favourites").child(DB.getAU().getUid()).updateChildren(m2);
                }
            } else {

                if (!likes.isEmpty() && !disliked.contains(current_anime.getAnime_id())) {
                    Long like_update = current_anime.getLikes() + 1;
                    for (String i : current_anime.getGenres()) {
                        likes.put(i, likes.get(i) - 1);
                    }
                    Map<String, Object> m = new HashMap<>();
                    m.put("dislikes", like_update);

                    DB.getDB().getReference("Anime").child(current_anime.getAnime_id()).updateChildren(m);
                    DB.getDB().getReference("Likes").child(DB.getAU().getUid()).updateChildren(new HashMap<>(likes));
                    Map<String, Object> m2 = new HashMap<>();
                    m2.put(current_anime.getAnime_id(), current_anime.getAnime_id());
                    comparator.setLikes(likes);
                    disliked.add(current_anime.getAnime_id());
                    DB.getDB().getReference("Disliked").child(DB.getAU().getUid()).updateChildren(m2);
                }
                if (!disliked_anime.contains(current_anime) && !favourites.contains(current_anime.getAnime_id())) {
                    disliked_anime.add(current_anime);

                }
            }
        }
    }
}
