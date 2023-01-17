package AnimEngine.myapplication.creator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import AnimEngine.myapplication.R;
import AnimEngine.myapplication.logics.StorageConnection;

public class CreatorSerieActivity extends AppCompatActivity {
    ImageView animeImage;
    TextView animeName;
    ListView animeDetails;
    Button edit_anime;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator_serie);
        animeImage=findViewById(R.id.animeImage);
        animeName=findViewById(R.id.animeNameSeries);
        animeDetails=findViewById(R.id.lvEpisode);
        edit_anime=findViewById(R.id.btnEdit);
        Bundle extra = getIntent().getExtras();
        try {
            String name=extra.getString("name");
            String seasons="Seasons: "+extra.getString("seasons");
            String episodes="Episodes: "+extra.getString("episodes");
            String anime_id=extra.getString("animeID");
            String gens=extra.getString("gens");
            String desc=extra.getString("desc");
            long likes=extra.getLong("likes");
            long dislikes=extra.getLong("dislikes");
            new StorageConnection("images").requestFile(anime_id, bytes -> {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                animeImage.setImageBitmap(bitmap);
            });
            animeName.setText(name);
            String[] objects={"Likes: "+likes,"Dislikes: "+dislikes,"Description: "+desc,gens,seasons,episodes};
            ArrayAdapter<String> arr=new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,objects);
            animeDetails.setAdapter(arr);
            String[]titles={"Likes","Dislikes","Description","Genres","Seasons","Episodes"};
            animeDetails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(CreatorSerieActivity.this);
                    dialog.setCancelable(true);
                    String text=((TextView)view).getText().toString();
                    if (i==3) {
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
            edit_anime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), EditAnimeActivity.class);
                    intent.putExtra("animeID", anime_id);
                    intent.putExtra("seasons", extra.getString("seasons"));
                    intent.putExtra("episodes", extra.getString("episodes"));
                    intent.putExtra("name", name);
                    intent.putExtra("gens", gens.trim());
                    intent.putExtra("desc", desc);
                    intent.putExtra("likes", likes);
                    intent.putExtra("dislikes",dislikes);
                    startActivity(intent);
                }
            });
        }catch (Exception e){
            Toast.makeText(CreatorSerieActivity.this,"Error",Toast.LENGTH_SHORT).show();
        }

    }
}