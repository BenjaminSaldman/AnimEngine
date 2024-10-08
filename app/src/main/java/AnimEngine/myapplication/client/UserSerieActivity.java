package AnimEngine.myapplication.client;

import androidx.annotation.NonNull;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import AnimEngine.myapplication.R;
import AnimEngine.myapplication.logics.StorageConnection;
import AnimEngine.myapplication.logics.DB;

public class UserSerieActivity extends AppCompatActivity {
    ImageView animeImage;
    TextView animeName;
    ListView animeDetails;

    Button add_to_favorites;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_serie);
        animeImage=findViewById(R.id.animeImage);
        animeName=findViewById(R.id.animeNameSeries);
        animeDetails=findViewById(R.id.lvEpisode);
        add_to_favorites=findViewById(R.id.btnFavourite);

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
            String[]titles={"Description","Genres","Seasons","Episodes","Likes","Dislikes"};
            String[] objects={"Description: "+desc,gens,seasons,episodes,"Likes: "+likes,"Dislikes: "+dislikes};
            ArrayAdapter<String> arr=new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,objects);
            animeDetails.setAdapter(arr);
            animeDetails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(UserSerieActivity.this);
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
            add_to_favorites.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String anime_id=extra.getString("animeID");
                    //DB.getDB().getReference("Favorites").child(DB.getAU().getUid())
                    DB.getDB().getReference("Favourites").child(DB.getAU().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean flag=true;
                            for (DataSnapshot child:snapshot.getChildren()){
                                if(anime_id.equals(child.getKey())){
                                    flag=false;
                                    break;
                                }
                            }
                            if (flag){
                                DB.getDB().getReference("Favourites").child(DB.getAU().getUid()).child(anime_id).setValue(anime_id);
                                Toast.makeText(UserSerieActivity.this,"Added to your favourites",Toast.LENGTH_SHORT).show();
                                long likes=extra.getLong("likes")+1;
                                Log.d("likesCheck",likes-1+"");
                                Log.d("likesCheck",likes+"");
                                Map<String,Object> m=new HashMap<>();
                                m.put("likes",likes);
                                DB.getDB().getReference("Anime").child(anime_id).updateChildren(m);
                                DB.getDB().getReference("Likes").child(DB.getAU().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Map<String,Object> m=new HashMap<>();
                                        for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                                            m.put(dataSnapshot.getKey(),dataSnapshot.getValue());
                                        }
                                        String[]sp=gens.split(" ");
                                        for (int i=1;i<sp.length;i++)
                                        {

                                            m.put(sp[i],((Long)m.get(sp[i]))+1);
                                        }
                                        DB.getDB().getReference("Likes").child(DB.getAU().getUid()).updateChildren(m);

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }else{
                                Toast.makeText(UserSerieActivity.this,"Already liked",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(UserSerieActivity.this,"Error",Toast.LENGTH_SHORT).show();
        }



    }
}