package AnimEngine.myapplication.utils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;

import AnimEngine.myapplication.R;
import AnimEngine.myapplication.client.Engine;
import AnimEngine.myapplication.client.UserProfileActivity;
import AnimEngine.myapplication.creator.CreateActivity;
import AnimEngine.myapplication.creator.CreatorProfileActivity;

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

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            boolean isCreator = FirebaseAuth.getInstance().getCurrentUser().getDisplayName().equals("true");
            if (!isCreator) {
                if (mCatalog.getId() == view.getId()) {
                    Intent intent = new Intent(view.getContext(), SearchActivity.class);
                    intent.putExtra("FROM", "CATALOG");
                    startActivity(intent);
                } else if (mProfile.getId() == view.getId()) {
                    startActivity(new Intent(view.getContext(), UserProfileActivity.class));
                } else {
                    startActivity(new Intent(view.getContext(), Engine.class));
                }
            } else {
                if (mCatalog.getId() == view.getId()) {
                    startActivity(new Intent(view.getContext(), SearchActivity.class));
                } else if (mProfile.getId() == view.getId()) {
                    startActivity(new Intent(view.getContext(), CreatorProfileActivity.class));
                } else {
                    startActivity(new Intent(view.getContext(), CreateActivity.class));
                }

            }
        }

    }
}