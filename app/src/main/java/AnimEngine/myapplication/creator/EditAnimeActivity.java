package AnimEngine.myapplication.creator;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import AnimEngine.myapplication.R;
import AnimEngine.myapplication.StorageConnection;
import AnimEngine.myapplication.utils.Anime;
import AnimEngine.myapplication.utils.DB;

public class EditAnimeActivity extends AppCompatActivity implements View.OnClickListener {
    EditText name, seasons, episodes;
    TextView gen, des;
    Button desc, upload, upload_image, genres;
    ImageView img;
    CharSequence[] Gen = {"Action", "Comedy", "Shonen", "Adventure", "Slice-of-Life", "Drama", "Fantasy", "Horror", "Magic", "Mystery",
            "Sci-Fi", "Psychological", "Supernatural", "Romance", "Crime", "Superhero", "Martial-arts"};
    boolean[] selected;
    String descript;
    boolean picture_to_upload_flag;
    Uri picture_to_upload;
    int ep, se;
    byte[] picture;
    String given_anime_id;
    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    try {
                        img.setImageURI(uri);
                        img.setTag(uri.toString());
                        picture_to_upload_flag = true;
                        picture_to_upload = Uri.parse(img.getTag().toString());

                    } catch (Exception e) {
                        picture_to_upload_flag = false;
                        Toast.makeText(EditAnimeActivity.this, "Failed to load", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        try{
            name = (EditText) findViewById(R.id.animeName);
            seasons = (EditText) findViewById(R.id.seasons);
            episodes = (EditText) findViewById(R.id.episodes);
            gen = (TextView) findViewById(R.id.genres);
            des = (TextView) findViewById(R.id.tvDesc);
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
            descript = "";
            picture_to_upload_flag = false;
            picture_to_upload=null;
            Bundle extra = getIntent().getExtras();
            String anime_name=extra.getString("name");
            String given_seasons=extra.getString("seasons");
            String given_episodes=extra.getString("episodes");
            given_anime_id=extra.getString("animeID");
            String given_gens=extra.getString("gens");
            String given_desc=extra.getString("desc");
            ep=Integer.parseInt(given_episodes);
            se=Integer.parseInt(given_seasons);
            name.setText(anime_name);
            des.setText(given_desc);
            seasons.setText(se+"");
            episodes.setText(ep+"");
            gen.setText(given_gens);
            picture=null;
            new StorageConnection("images").requestFile(given_anime_id, bytes -> {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                img.setImageBitmap(bitmap);
                picture=bytes;
                picture_to_upload_flag=true;
            });
            upload.setText("Update anime");
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(EditAnimeActivity.this, "error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == genres.getId()) {
            chooseGenres();
        } else if (view.getId() == desc.getId()) {
            insertDescription();
        } else if (view.getId() == upload_image.getId()) {
            mGetContent.launch("image/*");
        } else if (view.getId() == upload.getId()) {
            String creator_id = DB.getAU().getUid();
            String anime_name = name.getText().toString();

            try {
                ep = Integer.parseInt(episodes.getText().toString());
                se = Integer.parseInt(seasons.getText().toString());
            } catch (Exception e) {
                Toast.makeText(EditAnimeActivity.this, "Please fill the episodes/seasons.", Toast.LENGTH_SHORT).show();
                return;
            }
            String gens = gen.getText().toString();
            String d = des.getText().toString();
            if (gens.equals("Selected: ") || d.isEmpty() || anime_name.isEmpty() || (picture_to_upload==null) && !picture_to_upload_flag) {
                Toast.makeText(EditAnimeActivity.this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
            } else {

                String[] splits = gens.trim().split(" ");
                List<String> to_send = new ArrayList<>();
                for (int i = 1; i < splits.length; i++) {
                    to_send.add(splits[i]);
                }
                //String ref = DB.getDB().getReference("Anime").push().getKey();
                Anime anime = new Anime(anime_name, ep, se, d, creator_id, given_anime_id, to_send);
                InputStream iStream = null;
                try {
                    if (picture_to_upload!=null) {
                        iStream = getContentResolver().openInputStream(picture_to_upload);
                    }else{
                        iStream=new ByteArrayInputStream(picture);
                    }
                    if(anime.upload_anime(iStream))
                    {
                        Toast.makeText(EditAnimeActivity.this, "Anime added successfully.", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(EditAnimeActivity.this, "Failed to upload the anime.", Toast.LENGTH_SHORT).show();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(EditAnimeActivity.this, "Failed to upload the anime.", Toast.LENGTH_SHORT).show();
                }


            }


        }

    }


    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }


    private void insertDescription() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(EditAnimeActivity.this);
        dialog.setCancelable(true);
        dialog.setTitle("Enter the anime description");
        final EditText description = new EditText(EditAnimeActivity.this);
        dialog.setView(description);
        dialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                descript = description.getText().toString();
                des.setText(descript);
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

    public String itemsToString() {
        String ans = "Selected: ";
        for (int i = 0; i < Gen.length; i++) {
            if (selected[i]) {
                ans += Gen[i] + " ";
            }
        }
        return ans;
    }

    private void chooseGenres() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(EditAnimeActivity.this);
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