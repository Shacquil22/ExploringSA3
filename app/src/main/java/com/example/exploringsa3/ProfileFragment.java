package com.example.exploringsa3;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

//    private TextView txtSettings;
//    private TextView txtProfile;
//    private TextView btnLogout;
    private ImageView btnSettings;
    private ImageView btnProfile;
    private  ImageView btnLogout;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        txtProfile = getActivity().findViewById(R.id.tvProfileUser);
//        txtSettings = getActivity().findViewById(R.id.ivSettings);
        btnSettings = getActivity().findViewById(R.id.ivSettings);
        btnProfile = getActivity().findViewById(R.id.ivUser);
        btnLogout = getActivity().findViewById(R.id.ivLogout);
//        btnLogout = getActivity().findViewById(R.id.tvProfileLogout);

//        txtProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getActivity(),MyProfile.class));
//            }
//        });
//
//        txtSettings.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getActivity(),Settings.class));
//            }
//        });
//
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),Settings.class));
            }
        });
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),MyProfile.class));
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fAuth.signOut();

                startActivity(new Intent(getActivity(),Login.class));
            }
        });
//        btnLogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //firebase auth signout
//
//                startActivity(new Intent(getActivity(),Login.class));
         //   }
       // });
    }
}