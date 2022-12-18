package AnimEngine.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import AnimEngine.myapplication.utils.Anime;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.MyViewHolder> implements RecyclerView.OnItemTouchListener {

    public static ArrayList<Anime> mList;
    private Context context;

    public SearchListAdapter(Context context, ArrayList<Anime> mList) {

        this.context = context;
        this.mList = mList;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.show_tile, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        boolean create = true;
        if (true) {
            // todo Glide.with(context).load(mList.get(position).getImageUri()).into(holder.ivImage); - when we have images
            holder.getTvName().setText(mList.get(position).getName().toString());
            StorageConnection sc = new StorageConnection("images");
            sc.requestFile(mList.get(position).getAnime_id(),bytes -> {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.getIvImageSerie().setImageBitmap(bitmap);
            });

            holder.getFlAnime().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // todo go to anime screen - activity serie
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        FrameLayout flAnime;
        ImageView ivImageSerie;
        TextView tvName;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            flAnime = (FrameLayout) itemView.findViewById(R.id.flAnime);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            ivImageSerie = (ImageView) itemView.findViewById(R.id.ivImageSerie);
        }

       public FrameLayout getFlAnime() {
            return flAnime;
       }

        public ImageView getIvImageSerie() {
            return ivImageSerie;
        }

        public TextView getTvName() {
            return tvName;
        }

        public void setImageViewOnClick() {
            this.flAnime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(flAnime.getContext(), "נלחץ", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}