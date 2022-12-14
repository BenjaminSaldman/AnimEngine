package AnimEngine.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import AnimEngine.myapplication.client.Engine;
import AnimEngine.myapplication.client.SelectActivity;
import AnimEngine.myapplication.client.UserProfileActivity;
import AnimEngine.myapplication.creator.CreateActivity;
import AnimEngine.myapplication.login.home_screen;
import AnimEngine.myapplication.utils.DB;
import AnimEngine.myapplication.utils.User;

public class MenuFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    LinearLayout mHome, mCatalog, mProfile;

    public MenuFragment() {
        // Required empty public constructor
    }

    public static MenuFragment newInstance(String param1, String param2) {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu, container, false);

        mHome = v.findViewById(R.id.mHome);
        mHome.setOnClickListener(this);

        mProfile = v.findViewById(R.id.mProfile);
        mProfile.setOnClickListener(this);

        mCatalog = v.findViewById(R.id.mCatalog);
        mCatalog.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View view) {


        DB.getDB().getReference("Users").child(DB.getAU().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    //Toast.makeText(home_screen.this, "Welcome back, " + user.getNickname(), Toast.LENGTH_LONG).show();
                    if (!user.isCreator()) {
                        if (mCatalog.getId() == view.getId()) {
                            startActivity(new Intent(view.getContext(), CatalogActivity.class));
                        } else if (mProfile.getId() == view.getId()) {
                            startActivity(new Intent(view.getContext(), UserProfileActivity.class));
                        } else {
                            startActivity(new Intent(view.getContext(), SelectActivity.class));
                        }
                    } else {
                        if (mCatalog.getId() == view.getId()) {
                            startActivity(new Intent(view.getContext(), CatalogActivity.class));
                        } else if (mProfile.getId() == view.getId()) {
                            startActivity(new Intent(view.getContext(), UserProfileActivity.class));
                        } else {
                            startActivity(new Intent(view.getContext(), home_screen.class));

                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }
}