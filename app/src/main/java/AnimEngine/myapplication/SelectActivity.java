package AnimEngine.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import AnimEngine.myapplication.utils.DB;

public class SelectActivity extends AppCompatActivity implements View.OnClickListener {

    final private String[] Gen = {"Action", "Comedy", "Shonen", "Adventure", "Slice of Life", "Drama", "Fantasy", "Horror", "Magic", "Mystery",
            "Sci-Fi", "Psychological", "Supernatural", "Romance", "Crime", "Superhero", "Martial-arts"};
    ImageView img;
    ImageButton like, dislike;
    TextView anime_name;
    final private String[] anime = {"Naruto", "One Piece", "Bleach", "Dragon Ball", "Hunter x Hunter"};
    final private String[] genres = {"Adventure Fantasy Comedy Martial-arts", "Adventure Fantasy", "Adventure Supernatural",
            "Adventure Fantasy Martial-arts", "Adventure Fantasy Martial-arts"};
    final private String[] URLs = {"https://www.giantbomb.com/a/uploads/original/3/33873/1700999-naruto.png",
            "https://en.wikipedia.org/wiki/One_Piece#/media/File:One_Piece,_Volume_61_Cover_(Japanese).jpg",
            "https://en.wikipedia.org/wiki/Bleach_(manga)#/media/File:Bleach_cover_01.jpg",
            "https://en.wikipedia.org/wiki/Dragon_Ball_(manga)#/media/File:DB_Tank%C5%8Dbon.png",
            "https://en.wikipedia.org/wiki/Hunter_%C3%97_Hunter#/media/File:Hunter_x_Hunter_cover_-_vol1.jpg"};
    Map<String, Integer> likes;
    int index;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        index = 0;
        likes = new HashMap<>();
        for (int i = 0; i < Gen.length; i++) {
            likes.put(Gen[i], 0);
        }
        Bundle extra = getIntent().getExtras();
        uid=extra.getString("uid");
        img = (ImageView) findViewById(R.id.imageID);
        like = (ImageButton) findViewById(R.id.ibLike);
        dislike = (ImageButton) findViewById(R.id.ibUnLike);
        anime_name = (TextView) findViewById(R.id.animeNameSeries);
        Glide.with(this).load(URLs[index]).into(img);
        anime_name.setText(anime[index]);
        like.setOnClickListener(this);
        dislike.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == like.getId()) {
            String[] parsed = genres[index].split(" ");
            for (int i = 0; i < parsed.length; i++) {
                likes.put(parsed[i], likes.get(parsed[i]) + 1);
            }
        }
        index++;
        if (index>=anime.length){
            DatabaseReference myRef= DB.getDB().getReference("Likes").child(uid);
            myRef.setValue(likes);
            Toast.makeText(SelectActivity.this,"Updated.",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),home_screen.class));
        }
        Glide.with(this).load(URLs[index]).into(img);
        anime_name.setText(anime[index]);
    }
}