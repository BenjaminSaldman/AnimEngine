package AnimEngine.myapplication.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
    PriorityQueue<Anime> queue;
    AnimeComperator comparator;
    Anime current_anime;
    ImageView img;
    ImageButton like, dislike;
    TextView anime_name;
    ListView attributes;
    boolean flag;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        flag=false;
        img = (ImageView) findViewById(R.id.imageID);
        like = (ImageButton) findViewById(R.id.ibLike);
        dislike = (ImageButton) findViewById(R.id.ibUnLike);
        anime_name = (TextView) findViewById(R.id.animeNameSeries);
        attributes=(ListView)findViewById(R.id.attributes);
        likes = new HashMap<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            queue = new PriorityQueue<>(new AnimeComperator(likes));
        }

        current_anime = new Anime();
        favourites = new ArrayList<>();
        comparator = new AnimeComperator();
        disliked = new ArrayList<>();
        disliked_anime = new ArrayList<>();
        likes = new HashMap<>();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String likes2 = extras.getString("Likes");
            try {

                Map<String, Object> m = JsonMapper.parseJson(likes2);
                for (String i : m.keySet()) {
                    likes.put(i, (Integer) m.get(i));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

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
                                receiver();
                                update_views();
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


    }

    @Override
    public void onClick(View view) {
        if (!(current_anime == null) && !(current_anime.getAnime_id().equals("anime_id"))) {

            if (view.getId() == like.getId()) {
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
                    m2.put(current_anime.getAnime_id(),current_anime.getAnime_id());
                    comparator.setLikes(likes);
                    disliked.add(current_anime.getAnime_id());
                    DB.getDB().getReference("Disliked").child(DB.getAU().getUid()).updateChildren(m2);
                }
                if (!disliked_anime.contains(current_anime) && !favourites.contains(current_anime.getAnime_id())) {
                    disliked_anime.add(current_anime);

                }
            }
            flag=false;
            update_views();
        }
    }

    private void receiver() {
        DB.getDB().getReference("Anime").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                comparator.setLikes(likes);
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Anime anime = dataSnapshot.getValue(Anime.class);
                    like.setOnClickListener(Engine.this);
                    dislike.setOnClickListener(Engine.this);
                    if (!favourites.contains(anime.getAnime_id()) && !contain(disliked_anime, anime.getAnime_id())) {
                        queue.add(anime);
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

        if (!queue.isEmpty() && !flag) {
            current_anime = queue.poll();
            flag=true;
            if (!(current_anime == null)) {
                if (current_anime.getAnime_id().equals("anime_id")) {

                } else {
                    like.setOnClickListener(null);
                    dislike.setOnClickListener(null);
                    new StorageConnection("images").requestFile(current_anime.getAnime_id(), bytes -> {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        //img.setImageBitmap(bitmap);
                        img.setVisibility(View.INVISIBLE);
                        Glide.with(this).load(bitmap).into(img);
                        img.setVisibility(View.VISIBLE);
                        //seasons.setText("SE: " + current_anime.getSeasons() + " EP: " + current_anime.getEpisodes());
                        String gens="Genres: ";
                        for(String i:current_anime.getGenres()){
                            gens+=i+" ";
                        }
                        anime_name.setText(current_anime.getName());
                        String[] objects={"Description: "+current_anime.getDescription(),gens.trim(),"Seasons: "+current_anime.getSeasons(),
                                "Episodes: "+current_anime.getEpisodes(),"Likes: "+current_anime.getLikes(),"Dislikes: "+current_anime.getDislikes()};
                        ArrayAdapter<String> arr=new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,objects);
                        attributes.setAdapter(arr);
                        String[]titles={"Description","Genres","Seasons","Episodes","Likes","Dislikes"};
                        attributes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                AlertDialog.Builder dialog = new AlertDialog.Builder(Engine.this);
                                dialog.setCancelable(true);
                                String text=((TextView)view).getText().toString();
                                if (i==1) {
                                    String[] genres = new String[text.split(" ").length - 1];
                                    boolean[] choices = new boolean[genres.length];
                                    for (int k = 1; k < text.split(" ").length; k++) {
                                        genres[k-1] = text.split(" ")[k];
                                        choices[k-1] = false;
                                    }

                                    dialog.setTitle("Genres");
                                    dialog.setItems(genres, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });
                                }else {
                                    dialog.setTitle(titles[i]);
                                    dialog.setMessage(text);
                                }
                                dialog.setPositiveButton("back", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                            @Override
                                            public void onDismiss(DialogInterface dialogInterface) {
                                                dialogInterface.dismiss();
                                            }
                                        });
                                    }
                                });
                                AlertDialog alert = dialog.create();
                                alert.setCanceledOnTouchOutside(true);
                                alert.show();

                            }
                        });

                    });
                    like.setOnClickListener(this);
                    dislike.setOnClickListener(this);

                }
            }
        }
    }

}