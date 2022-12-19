package AnimEngine.myapplication.client;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import AnimEngine.myapplication.R;
import AnimEngine.myapplication.StorageConnection;
import AnimEngine.myapplication.utils.Anime;

public class SerieActivity extends AppCompatActivity {
    ImageView animeImage;
    TextView animeName;
    ListView animeDetails;
    Anime anime;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serie);
        animeImage=findViewById(R.id.animeImage);
        animeName=findViewById(R.id.animeNameSeries);
        animeDetails=findViewById(R.id.lvEpisode);
        Bundle extra = getIntent().getExtras();
        try {
//            intent.putExtra("animeID", mList.get(position).getAnime_id());
//            intent.putExtra("seasons", mList.get(position).getSeasons());
//            intent.putExtra("episodes", mList.get(position).getEpisodes());
//            String gens="";
//            for (String i:mList.get(position).getGenres())
//                gens+=i+" ";
//            intent.putExtra("name", mList.get(position).getName());
//            intent.putExtra("gens", gens.trim());
            String name=extra.getString("name");
            String seasons="Seasons: "+extra.getString("seasons");
            String episodes="Episodes: "+extra.getString("episodes");
            String anime_id=extra.getString("animeID");
            String gens=extra.getString("gens");
            String desc=extra.getString("desc");
            //anime= (Anime) extra.get("anime");
            new StorageConnection("images").requestFile(anime_id, bytes -> {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                animeImage.setImageBitmap(bitmap);
            });
            animeName.setText(name);
            String[] objects={"Description: "+desc,gens,seasons,episodes};
            ArrayAdapter<String> arr=new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,objects);
            animeDetails.setAdapter(arr);
        }catch (Exception e){
            Toast.makeText(SerieActivity.this,"Error",Toast.LENGTH_SHORT).show();
        }


    }
}