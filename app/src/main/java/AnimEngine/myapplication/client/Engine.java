package AnimEngine.myapplication.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

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
    Anime current_anime;
    ImageView img;
    ImageButton like, dislike;
    TextView anime_name;
    ListView attributes;
    boolean flag;
    Engine_controller controller;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        flag = false;
        img = (ImageView) findViewById(R.id.imageID);
        like = (ImageButton) findViewById(R.id.ibLike);
        dislike = (ImageButton) findViewById(R.id.ibUnLike);
        anime_name = (TextView) findViewById(R.id.animeNameSeries);
        attributes = (ListView) findViewById(R.id.attributes);
        controller = new ViewModelProvider(this).get(Engine_controller.class);
        current_anime = controller.getCurrent_anime();
        controller.getUsers().observe(this, anime -> {
            current_anime = anime;
            Log.d("MMAAA",anime.getAnime_id());
            //update_views(anime);
        });
        update_views(current_anime);
        like.setOnClickListener(this);
        dislike.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        boolean isLike = view.getId() == like.getId() ? true : false;
        controller.like_set(isLike);
        controller.getUsers().observe(this, anime -> {
            current_anime = anime;
            Log.d("MMAAA",anime.getAnime_id());
            //update_views(anime);
        });
        update_views(current_anime);


    }


    private void update_views(Anime current_anime) {
        current_anime = controller.getCurrent_anime();
        flag = true;
        if (!(current_anime == null)) {
            if (current_anime.getAnime_id().equals("anime_id")) {

            } else {
                like.setOnClickListener(null);
                dislike.setOnClickListener(null);
                Anime finalCurrent_anime = current_anime;
                new StorageConnection("images").requestFile(current_anime.getAnime_id(), bytes -> {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    //img.setImageBitmap(bitmap);
                    img.setVisibility(View.INVISIBLE);
                    Glide.with(this).load(bitmap).into(img);
                    img.setVisibility(View.VISIBLE);
                    //seasons.setText("SE: " + current_anime.getSeasons() + " EP: " + current_anime.getEpisodes());
                    String gens = "Genres: ";
                    for (String i : finalCurrent_anime.getGenres()) {
                        gens += i + " ";
                    }
                    anime_name.setText(finalCurrent_anime.getName());
                    String[] objects = {"Description: " + finalCurrent_anime.getDescription(), gens.trim(), "Seasons: " + finalCurrent_anime.getSeasons(),
                            "Episodes: " + finalCurrent_anime.getEpisodes(), "Likes: " + finalCurrent_anime.getLikes(), "Dislikes: " + finalCurrent_anime.getDislikes()};
                    ArrayAdapter<String> arr = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, objects);
                    attributes.setAdapter(arr);
                    String[] titles = {"Description", "Genres", "Seasons", "Episodes", "Likes", "Dislikes"};
                    attributes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(Engine.this);
                            dialog.setCancelable(true);
                            String text = ((TextView) view).getText().toString();
                            if (i == 1) {
                                String[] genres = new String[text.split(" ").length - 1];
                                boolean[] choices = new boolean[genres.length];
                                for (int k = 1; k < text.split(" ").length; k++) {
                                    genres[k - 1] = text.split(" ")[k];
                                    choices[k - 1] = false;
                                }

                                dialog.setTitle("Genres");
                                dialog.setItems(genres, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                            } else {
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