package AnimEngine.myapplication.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

import AnimEngine.myapplication.R;
import AnimEngine.myapplication.login.SignInActivity;
import AnimEngine.myapplication.utils.DB;

public class SelectActivity extends AppCompatActivity implements View.OnClickListener {

    String[] Gen = {"Action", "Comedy", "Shonen", "Adventure", "Slice-of-Life", "Drama", "Fantasy", "Horror", "Magic", "Mystery",
            "Sci-Fi", "Psychological", "Supernatural", "Romance", "Crime", "Superhero", "Martial-arts"};
    ImageView img;
    ImageButton like, dislike;
    TextView anime_name, description, seasons;
    final private String[] anime = {"Naruto", "One Piece", "Bleach", "Dragon Ball", "Hunter x Hunter"};
    final private String[] genres = {"Adventure Fantasy Comedy Martial-arts", "Adventure Fantasy", "Adventure Supernatural",
            "Adventure Fantasy Martial-arts", "Adventure Fantasy Martial-arts"};
    final private String[] URLs = {"https://www.giantbomb.com/a/uploads/original/3/33873/1700999-naruto.png",
            "https://i.ytimg.com/vi/ha0-qytMD9k/maxresdefault.jpg",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTxtWN2AF8vQ193DFg-UKViqXeu_SfHHF-AyQ&usqp=CAU",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQARjrRsYHUpOzVKsPyYX_uFo6wp10M1-GgIg&usqp=CAU",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ-YxGQ0w4EZBuurlwmOQazoc7-EuoAmbMzPQ&usqp=CAU"};
    final private String[] descriptions = {"It tells the story of Naruto Uzumaki, a young ninja who seeks recognition from his peers and dreams of becoming the Hokage, the leader of his village." ,
            " The series focuses on Monkey D. Luffy, a young man made of rubber, who, inspired by his childhood idol, the powerful pirate Red-Haired Shanks, sets off on a journey from the East Blue Sea to find the mythical treasure, the One Piece, and proclaim himself the King of the Pirates.",
            "It follows the adventures of a teenager Ichigo Kurosaki, who inherits his parents' destiny after he obtains the powers of a Soul Reaper—a death personification similar to the Grim Reaper—from another Soul Reaper, Rukia Kuchiki.",
            "Follows the adventures of an extraordinarily strong young boy named Goku as he searches for the seven dragon balls.",
            "Gon Freecss aspires to become a Hunter, an exceptional being capable of greatness. With his friends and his potential, he seeks out his father, who left him when he was younger."};
    final private String[] len = {"720 ep 26 se", "1043 ep 20 se", "366 ep 16 se", "291 ep 7 se", "148 ep 6 se"};
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
        uid = extra.getString("uid");
        img = (ImageView) findViewById(R.id.imageID);
        like = (ImageButton) findViewById(R.id.ibLike);
        dislike = (ImageButton) findViewById(R.id.ibUnLike);
        anime_name = (TextView) findViewById(R.id.animeNameSeries);
        description = (TextView) findViewById(R.id.desc);
        seasons = (TextView) findViewById(R.id.seasons);
        Glide.with(this).load(URLs[index]).into(img);
        anime_name.setText(anime[index]);
        description.setText(descriptions[index]);
        seasons.setText(len[index]);
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
        index = index + 1;
        if (index >= anime.length) {
            DatabaseReference myRef = DB.getDB().getReference("Likes").child(uid);
            myRef.setValue(likes);
            Toast.makeText(SelectActivity.this, "Updated.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
        }else {
            Glide.with(this).load(URLs[index]).into(img);
            anime_name.setText(anime[index]);
            description.setText(descriptions[index]);
            seasons.setText(len[index]);
        }
    }
}