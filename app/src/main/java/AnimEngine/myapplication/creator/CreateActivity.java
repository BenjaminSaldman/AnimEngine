package AnimEngine.myapplication.creator;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import AnimEngine.myapplication.R;
import AnimEngine.myapplication.StorageConnection;
import AnimEngine.myapplication.utils.Anime;
import AnimEngine.myapplication.utils.DB;

public class CreateActivity extends AppCompatActivity implements View.OnClickListener {
    EditText name, seasons, episodes, des;
    TextView gen;
    Button upload, upload_image, genres;
    ImageView img;
    CharSequence[] Gen = {"Action", "Comedy", "Shonen", "Adventure", "Slice-of-Life", "Drama", "Fantasy", "Horror", "Magic", "Mystery",
            "Sci-Fi", "Psychological", "Supernatural", "Romance", "Crime", "Superhero", "Martial-arts"};
    boolean[] selected;
    String descript;
    Uri picture_to_upload;
    int ep, se;
    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    try {
                        img.setImageURI(uri);
                        img.setTag(uri.toString());
                        picture_to_upload = Uri.parse(img.getTag().toString());
                    } catch (Exception e) {
                        Toast.makeText(CreateActivity.this, "Failed to load", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        name = (EditText) findViewById(R.id.animeName);
        seasons = (EditText) findViewById(R.id.seasons);
        episodes = (EditText) findViewById(R.id.episodes);
        gen = (TextView) findViewById(R.id.genres);
        des = findViewById(R.id.tvDesc);
        genres = (Button) findViewById(R.id.bgenres);
        upload = (Button) findViewById(R.id.upload);
        upload_image = (Button) findViewById(R.id.uimage);

        img = (ImageView) findViewById(R.id.image);
        genres.setOnClickListener(this);
        upload.setOnClickListener(this);
        upload_image.setOnClickListener(this);
        selected = new boolean[Gen.length];
        for (int i = 0; i < Gen.length; i++) {
            selected[i] = false;
        }
        descript = "";
        picture_to_upload = null;
        ep = 0;
        se = 0;


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == genres.getId()) {
            chooseGenres();
        } else if (view.getId() == upload_image.getId()) {
            mGetContent.launch("image/*");
        } else if (view.getId() == upload.getId()) {
            String creator_id = DB.getAU().getUid();
            String anime_name = name.getText().toString();

            try {
                ep = Integer.parseInt(episodes.getText().toString());
                se = Integer.parseInt(seasons.getText().toString());
            } catch (Exception e) {
                Toast.makeText(CreateActivity.this, "Please fill the episodes/seasons.", Toast.LENGTH_SHORT).show();
                return;
            }
            String gens = gen.getText().toString();
            String d = des.getText().toString();
            if (gens.equals("Selected: ") || d.isEmpty() || anime_name.isEmpty() || picture_to_upload == null) {
                Toast.makeText(CreateActivity.this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
            } else {
                DB.upload(anime_name, ep, se, d, creator_id, gens, picture_to_upload, CreateActivity.this);
            }
        }

    }


    private String itemsToString() {
        String ans = "Selected: ";
        for (int i = 0; i < Gen.length; i++) {
            if (selected[i]) {
                ans += Gen[i] + " ";
            }
        }
        return ans;
    }

    private void chooseGenres() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(CreateActivity.this);
        dialog.setCancelable(true);
        dialog.setTitle("Choose the genres");
        dialog.setMultiChoiceItems(Gen, selected, new DialogInterface.OnMultiChoiceClickListener() {
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
        AlertDialog alert = dialog.create();
        alert.setCanceledOnTouchOutside(true);
        alert.show();

    }


}