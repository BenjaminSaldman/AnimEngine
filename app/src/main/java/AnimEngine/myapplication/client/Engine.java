package AnimEngine.myapplication.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.util.JsonMapper;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import AnimEngine.myapplication.R;
import AnimEngine.myapplication.StorageConnection;
import AnimEngine.myapplication.utils.Anime;
import AnimEngine.myapplication.utils.AnimeComperator;
import AnimEngine.myapplication.utils.DB;

public class Engine extends AppCompatActivity implements View.OnClickListener {
    Map<String, Integer> likes;
    ArrayList<String> favourites;
    ArrayList<String> disliked;
    ArrayList<Anime> disliked_anime;
    //PriorityQueue<Anime> queue;
    PriorityQueue<Anime> queue;
    AnimeComperator comparator;
    Anime current_anime;
    ImageView img;
    ImageButton like, dislike;
    TextView anime_name, description, seasons;
    SharedPreferences sharedPreferences;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        img = (ImageView) findViewById(R.id.imageID);
        like = (ImageButton) findViewById(R.id.ibLike);
        dislike = (ImageButton) findViewById(R.id.ibUnLike);
        anime_name = (TextView) findViewById(R.id.animeNameSeries);
        description = (TextView) findViewById(R.id.desc);
        seasons = (TextView) findViewById(R.id.seasons);
        sharedPreferences = getApplicationContext().getSharedPreferences("MySharedPref", MODE_PRIVATE);


        likes = new HashMap<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            queue = new PriorityQueue<>(new AnimeComperator(likes));
        }
        current_anime = new Anime();
        favourites = new ArrayList<>();
        comparator = new AnimeComperator();
        disliked = new ArrayList<>();
        disliked_anime = new ArrayList<>();
        likes=new HashMap<>();
        Bundle extras=getIntent().getExtras();
        if (extras!=null){
            String likes2= extras.getString("Likes");
            try {

                Map<String,Object>m= JsonMapper.parseJson(likes2);
                for(String i:m.keySet()){
                    likes.put(i, (Integer) m.get(i));
                }
                Log.d("KABBOOOM", "123");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        Log.d("KABBOOOM", "456");
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
                DB.getDB().getReference("Favourites").child(DB.getAU().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            favourites.add(dataSnapshot.getKey());
                        }
                        Log.d("misspiggy", "I hate android");
                        receiver();
                        update_views();

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                DB.getDB().getReference("Disliked").child(DB.getAU().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            disliked.add(dataSnapshot.getKey());
                        }

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
    public void onClick(View view) {
        if (!(current_anime == null)) {
            if (view.getId() == like.getId()) {
                if (!favourites.contains(current_anime.getAnime_id())) {
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
                if (!disliked.contains(current_anime.getAnime_id())) {
                    Long like_update = current_anime.getLikes() + 1;
                    for (String i : current_anime.getGenres()) {
                        likes.put(i, likes.get(i) - 1);
                    }
                    Map<String, Object> m = new HashMap<>();
                    m.put("dislikes", like_update);
                    Log.d("MAKARPO?", "SKK");
                    DB.getDB().getReference("Anime").child(current_anime.getAnime_id()).updateChildren(m);
                    DB.getDB().getReference("Likes").child(DB.getAU().getUid()).updateChildren(new HashMap<>(likes));
                    Map<String, Object> m2 = new HashMap<>();
                    for (String i : disliked) {
                        m2.put(i, i);
                    }
                    comparator.setLikes(likes);
                    DB.getDB().getReference("Disliked").child(DB.getAU().getUid()).updateChildren(m2);
                }
                if (!disliked_anime.contains(current_anime)) {
                    disliked_anime.add(current_anime);
                }
            }
            update_views();
        }
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        if (!likes.isEmpty())
            myEdit.putString("likes", new JSONObject(likes).toString());
        if (!favourites.isEmpty()) {
            String parsed = "";
            for (String i : favourites) {
                parsed += i + " ";
            }
            parsed.trim();
            myEdit.putString("favourites", parsed);
        }
        if (!(current_anime == null) && !current_anime.getAnime_id().equals("anime_id")) {
            myEdit.putString("current_anime", current_anime.getAnime_id());
        }
        if (!queue.isEmpty()) {
            String anime = "";
            for (Anime a : queue) {
                anime += a.getAnime_id() + " ";
            }
            myEdit.putString("queue", anime.trim());
        }
        like.setOnClickListener(null);
        dislike.setOnClickListener(null);
        super.onSaveInstanceState(savedInstanceState);
        myEdit.commit();

    }


    @Override
    public void onStop() {
        super.onStop();
        Log.d("THIS", "destroyed");
    }

    @Override
    public void onRestart() {
        super.onRestart();
        Log.d("THIS", "rest");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("THIS", "resumed");
    }

    private void receiver() {
        DB.getDB().getReference("Anime").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                comparator.setLikes(likes);
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Log.d("PIPIUNDKAKI", "resumed");
                    Log.d("PIPIUNDKAKI", favourites + "");
                    Anime anime = dataSnapshot.getValue(Anime.class);
                    like.setOnClickListener(Engine.this);
                    dislike.setOnClickListener(Engine.this);
                    if (!favourites.contains(anime.getAnime_id()) && !contain(disliked_anime, anime.getAnime_id())) {
                        Log.d("MAKARAPO?", likes + "");
                        queue.add(anime);
                        Log.d("PIPIUNDKAKI", queue + "");
                    } else if (queue.isEmpty() && (!disliked_anime.isEmpty() || !disliked.isEmpty())) {
                        if (!disliked_anime.isEmpty()) {
                            queue.add(disliked_anime.remove(0));
                        } else {
                            Anime anime2 = dataSnapshot.getValue(Anime.class);
                            if (disliked.contains(anime.getAnime_id())) {
                                disliked.remove(anime.getAnime_id());
                                queue.add(anime2);
                            }
                        }

                    }
                }
                update_views();
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

    public void update_views() {

        if (!queue.isEmpty()) {
            Log.d("PIPIUNDKAKILE", queue + "");
            current_anime = queue.poll();
            Log.d("WTFK", "KKL");
            if (!(current_anime == null)) {
                Log.d("WTFK", current_anime.getAnime_id());
                if (current_anime.getAnime_id().equals("anime_id")) {

                } else {
                    like.setOnClickListener(null);
                    dislike.setOnClickListener(null);
                    new StorageConnection("images").requestFile(current_anime.getAnime_id(), bytes -> {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        img.setImageBitmap(bitmap);
                        seasons.setText("SE: "+current_anime.getSeasons()+" EP: "+current_anime.getEpisodes());
                        anime_name.setText(current_anime.getName());
                        description.setText("OK?");
                        description.setText(current_anime.getDescription());
                        like.setOnClickListener(this);
                        dislike.setOnClickListener(this);
                    });
                }
            }
        }
    }

}