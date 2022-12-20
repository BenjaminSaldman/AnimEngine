package AnimEngine.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import AnimEngine.myapplication.R;
import AnimEngine.myapplication.SearchListAdapter;
import AnimEngine.myapplication.utils.Anime;
import AnimEngine.myapplication.utils.DB;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private RecyclerView recyclerView;
    private ArrayList<Anime> list;
    private SearchListAdapter adapter;

    ImageButton ibSearch;
    EditText etSearch;

    private DatabaseReference animeRoot = FirebaseDatabase.getInstance().getReference("Anime");
    private DatabaseReference creatorAnimeRoot = FirebaseDatabase.getInstance().getReference("CreatorAnime").child(DB.getAU().getUid());


    public static String MODE_OF_SEARCH = "filter by";
    public static String WORD_SEARCH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ibSearch = findViewById(R.id.ibSearch);
        ibSearch.setOnClickListener(this);
        etSearch = findViewById(R.id.etSearch);

        recyclerView = findViewById(R.id.lv);
        recyclerView.setHasFixedSize(false);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        showAnimes();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibSearch:
                WORD_SEARCH = etSearch.getText().toString().trim();
                list = new ArrayList<>();
                adapter = new SearchListAdapter(this, list);
                recyclerView.setAdapter(adapter);

                boolean isCreator = FirebaseAuth.getInstance().getCurrentUser().getDisplayName().equals("true");

                if (isCreator) {
                    creatorAnimeRoot.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            list.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                String id=dataSnapshot.getValue(String.class);
                                DB.getDB().getReference("Anime").child(id).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Anime model = snapshot.getValue(Anime.class);
                                        if (WORD_SEARCH.isEmpty() || model.getName().contains(WORD_SEARCH)) {
                                            list.add(model);
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    break;
                } else {
                    animeRoot.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            list.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Anime model = dataSnapshot.getValue(Anime.class);
                                if (WORD_SEARCH.isEmpty() || model.getName().contains(WORD_SEARCH)) {
                                    list.add(model);

                                }
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    break;
                }
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), "" + text, Toast.LENGTH_LONG);
        MODE_OF_SEARCH = text;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
        finishAndRemoveTask();
    }


    public void showAnimes() {
        WORD_SEARCH = etSearch.getText().toString().trim();
        list = new ArrayList<>();
        adapter = new SearchListAdapter(this, list);
        recyclerView.setAdapter(adapter);

        boolean isCreator = FirebaseAuth.getInstance().getCurrentUser().getDisplayName().equals("true");

        if (isCreator) {
            Log.d("Got her?","12345");
            creatorAnimeRoot.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String id=dataSnapshot.getValue(String.class);
                        DB.getDB().getReference("Anime").child(id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Anime model = snapshot.getValue(Anime.class);
                                Log.d("Got her?","12345");
                                if (WORD_SEARCH.isEmpty() || model.getName().contains(WORD_SEARCH)) {
                                    list.add(model);
                                    adapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            animeRoot.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Anime model = dataSnapshot.getValue(Anime.class);

                        if (WORD_SEARCH.isEmpty() || model.getName().contains(WORD_SEARCH)) {
                            list.add(model);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }


}