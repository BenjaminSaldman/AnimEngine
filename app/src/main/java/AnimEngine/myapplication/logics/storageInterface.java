package AnimEngine.myapplication.logics;

import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;

import java.util.function.Consumer;

public interface storageInterface {
    //gets command//
    public void requestFile(String name, OnSuccessListener<byte[]> lambda);
    //requestFile: request file and the lambda will handle the file when the DB finish

    public byte[][] requestImages(String[] names);
    //requestImages: not implemented yet

    //upload commands//
    public void uploadImage(String name, byte[] img);
    //uploadImage: upload an byte[] (which represent image) to the fire-base

    public void uploadImages(String[] names, byte[][] imgs);
    //uploadImages: not implemented yet
}
