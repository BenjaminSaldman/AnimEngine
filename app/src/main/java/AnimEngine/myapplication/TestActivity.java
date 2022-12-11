package AnimEngine.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;


public class TestActivity extends AppCompatActivity {

    FirebaseStorage storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        ImageView im = (ImageView)findViewById(R.id.erased);

        StorageConnection sc = new StorageConnection("images");

        //exemple of uploading image to fire-base storage
        sc.requestFile("erased.jpg", bytes -> {
            sc.uploadImage("era231.jpg", bytes);
        });

        //example show image in the app
        sc.requestFile("erased.jpg", bytes -> {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            im.setImageBitmap(bitmap);
        });

        //System.out.println(Arrays.toString(imgTObytes.getBytesFromImagePath("C:\\Users\\hille\\Documents\\University\\third year\\first semster")));


//        byte[] bytes = sc.getImage("erased.jpg", );
//        if(bytes == null){
//            Toast.makeText(getApplicationContext(),"null",Toast.LENGTH_SHORT).show();
//        }
//        else{
//            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//            im.setImageBitmap(bitmap);
//        }

    }


}