package AnimEngine.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;



import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import java.util.ArrayList;

import AnimEngine.myapplication.creator.CreateActivity;
import AnimEngine.myapplication.utils.Anime;

public class CatalogActivity extends AppCompatActivity {

    ArrayList<Anime> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        TextView tv_title = findViewById(R.id.catalog_tv);
        RecyclerView rv = findViewById(R.id.catalog_recycler);
        //BottomNavigationView bottomNavigation = findViewById(R.id.catalog_bottom_navigation);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        arrayList = new ArrayList<>();

        arrayList.add(new Anime("Erased"));
        arrayList.add(new Anime("Another"));
        arrayList.add(new Anime("Dragon Ball"));

        arrayList.add(new Anime("Dragon Ball"));
        arrayList.add(new Anime("Dragon Ball"));
        arrayList.add(new Anime("Dragon Ball"));

        arrayList.add(new Anime("Dragon Ball"));
        arrayList.add(new Anime("Dragon Ball"));
        arrayList.add(new Anime("Dragon Ball"));

        arrayList.add(new Anime("Dragon Ball"));
        arrayList.add(new Anime("Dragon Ball"));
        arrayList.add(new Anime("Dragon Ball"));

        arrayList.add(new Anime("Dragon Ball"));
        arrayList.add(new Anime("Dragon Ball"));
        arrayList.add(new Anime("Dragon Ball"));

        arrayList.add(new Anime("Dragon Ball"));
        arrayList.add(new Anime("Dragon Ball"));
        arrayList.add(new Anime("Dragon Ball"));

        arrayList.add(new Anime("Dragon Ball"));
        arrayList.add(new Anime("Dragon Ball"));
        arrayList.add(new Anime("Dragon Ball"));

        CatalogRVAdapter customManufacturersRVAdapter = new CatalogRVAdapter(CatalogActivity.this, getLayoutInflater(), arrayList);
        rv.setAdapter(customManufacturersRVAdapter);
        rv.setLayoutManager(new GridLayoutManager(CatalogActivity.this,4));


        int screenHeight = displayMetrics.heightPixels + getNavigationBarHeight();
        //rv.setMinimumHeight(screenHeight-tv_title.getHeight());
    }

    private int getNavigationBarHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight)
            return realHeight - usableHeight;
        else
            return 0;
    }
    private void dialog(Anime anime){
        AlertDialog.Builder dialog = new AlertDialog.Builder(CatalogActivity.this);
        dialog.setCancelable(true);
        dialog.setTitle(anime.getName());
        ImageView img=new ImageView(CatalogActivity.this);
        TextView name=new TextView(CatalogActivity.this);
        TextView description=new TextView(CatalogActivity.this);
        TextView seasons=new TextView(CatalogActivity.this);
        TextView episodes=new TextView(CatalogActivity.this);
        TextView genres=new TextView(CatalogActivity.this);
        dialog.setView(img);
        dialog.setView(name);
        dialog.setView(description);
        dialog.setView(seasons);
        dialog.setView(episodes);
        dialog.setView(genres);
        new StorageConnection("images").requestFile(anime.getAnime_id(), bytes -> {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            img.setImageBitmap(bitmap);
        });
        name.setText(anime.getName());
        description.setText(anime.getDescription());
        seasons.setText(anime.getSeasons());
        episodes.setText(anime.getEpisodes());
        String gen="Genres: ";
        for (String i:anime.getGenres())
        {
            gen+=i;
        }
        genres.setText(gen);

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

    public boolean showNavigationBar(Resources resources) {
        int id = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        return id > 0 && resources.getBoolean(id);
    }
}