package com.example.exploringsa3;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.exploringsa3.Favourites.FavouriteAdapter;
import com.example.exploringsa3.Favourites.FavouriteHelperClass;
import com.example.exploringsa3.HelperClasses.FeaturedHelperClass;

import java.util.ArrayList;

public class FavouriteFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView rvMostViewed;
    RecyclerView.Adapter adapter;
    RecyclerView.Adapter adapterMostViewed;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourite, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView = getActivity().findViewById(R.id.rvFavouriteLandmarks);

        favourite();

    }

    private void favourite() {
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

        ArrayList<FavouriteHelperClass> FavouriteLocations = new ArrayList<>();

        FavouriteLocations.add(new FavouriteHelperClass(R.drawable.nelson_mandela_square,"Nelson Mandela Square","Sandton","50,3km","34min"));
        FavouriteLocations.add(new FavouriteHelperClass(R.drawable.voortrekker_monument,"Voortrekker Monument","Johannesburg","8,5km","28min"));
//        FavouriteLocations.add(new FavouriteHelperClass(R.drawable.union_buildings,"Union Buildings",4.5));
//        FavouriteLocations.add(new FavouriteHelperClass(R.drawable.hector_pieterson_memorial,"Hector Pieterson Memorial",4.4));

        adapter = new FavouriteAdapter(FavouriteLocations);

        recyclerView.setAdapter(adapter);

        GradientDrawable gradient1 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xffeff400,0xffaff600});

    }
}