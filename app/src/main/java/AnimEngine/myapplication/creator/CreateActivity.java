package AnimEngine.myapplication.creator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import AnimEngine.myapplication.R;
import AnimEngine.myapplication.utils.Anime;
import AnimEngine.myapplication.utils.DB;

public class CreateActivity extends AppCompatActivity implements View.OnClickListener {
    EditText name, seasons, episodes;
    TextView gen;
    Button desc, upload, upload_image, genres;
    ImageView img;
    CharSequence[] Gen = {"Action", "Comedy", "Shonen", "Adventure", "Slice of Life", "Drama", "Fantasy", "Horror", "Magic", "Mystery",
            "Sci-Fi", "Psychological", "Supernatural", "Romance", "Crime", "Superhero", "Martial-arts"};
    boolean[] selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        name = (EditText) findViewById(R.id.animeName);
        seasons = (EditText) findViewById(R.id.seasons);
        episodes = (EditText) findViewById(R.id.episodes);
        gen = (TextView) findViewById(R.id.genres);

        desc = (Button) findViewById(R.id.description);
        genres = (Button) findViewById(R.id.bgenres);
        upload = (Button) findViewById(R.id.upload);
        upload_image = (Button) findViewById(R.id.uimage);

        img = (ImageView) findViewById(R.id.image);
        desc.setOnClickListener(this);
        genres.setOnClickListener(this);
        upload.setOnClickListener(this);
        upload_image.setOnClickListener(this);
        selected = new boolean[Gen.length];
        for (int i = 0; i < Gen.length; i++) {
            selected[i] = false;
        }


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == genres.getId()) {
            chooseGenres();
        }
    }

    public String itemsToString() {
        String ans = "";
        for (int i = 0; i < Gen.length; i++) {
            if (selected[i]) {
                ans += Gen[i] + " ";
            }
        }
        Log.d("Ans",ans);
        return ans;
    }

    private void chooseGenres() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(CreateActivity.this);
        dialog.setCancelable(true);
        dialog.setTitle("Choose the genres");
        dialog.setMultiChoiceItems(Gen,selected , new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                selected[which] = isChecked;
            }
        });
        dialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                gen.setText(itemsToString());
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        dialogInterface.dismiss();
                    }
                });
            }
        });
        AlertDialog alert=dialog.create();
        alert.setCanceledOnTouchOutside(true);
        alert.show();

    }


}