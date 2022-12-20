package AnimEngine.myapplication.client;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import AnimEngine.myapplication.CatalogActivity;
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
            long likes=extra.getLong("likes");
            long dislikes=extra.getLong("dislikes");
            //anime= (Anime) extra.get("anime");
            new StorageConnection("images").requestFile(anime_id, bytes -> {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                animeImage.setImageBitmap(bitmap);
            });
            animeName.setText(name);
            String[] objects={"Description: "+desc,gens,seasons,episodes,"Likes: "+likes,"Dislikes: "+dislikes};
            ArrayAdapter<String> arr=new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,objects);
            animeDetails.setAdapter(arr);
        }catch (Exception e){
            Toast.makeText(SerieActivity.this,"Error",Toast.LENGTH_SHORT).show();
        }
        animeDetails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(SerieActivity.this);
                dialog.setCancelable(true);
                String text=((TextView)view).getText().toString();
                TextView tv=new TextView(SerieActivity.this);
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
                    dialog.setView(tv);
                    tv.setText(text);
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


    }
}