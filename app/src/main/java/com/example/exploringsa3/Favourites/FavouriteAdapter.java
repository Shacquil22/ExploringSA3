package com.example.exploringsa3.Favourites;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exploringsa3.HelperClasses.FeaturedHelperClass;
import com.example.exploringsa3.Maps;
import com.example.exploringsa3.R;

import java.util.ArrayList;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FeaturedViewHolder> {

    static ArrayList<FavouriteHelperClass> favouriteLocations;

    public FavouriteAdapter(ArrayList<FavouriteHelperClass> favouriteLocations) {
        this.favouriteLocations = favouriteLocations;
    }

    @NonNull
    @Override
    public FeaturedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_card_view,parent,false);
        FeaturedViewHolder featuredViewHolder = new FeaturedViewHolder(view);

        return featuredViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedViewHolder holder, @SuppressLint("RecyclerView") int position) {

        FavouriteHelperClass favouriteHelperClass = favouriteLocations.get(position);

        holder.image.setImageResource(favouriteHelperClass.getImage());
        holder.title.setText(favouriteHelperClass.getTitle());
        holder.name.setText(favouriteHelperClass.getName());
        holder.distance.setText(favouriteHelperClass.getDistance());
        holder.time.setText(favouriteHelperClass.getTime());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), Maps.class);
                i.putExtra("title", favouriteLocations.get(position).getTitle());
                v.getContext().startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return favouriteLocations.size();
    }

    public interface RecyclerViewOnClickListener{
        void onClick(View v,int position);
    }

    public static class FeaturedViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView title;
        TextView name;
        TextView distance;
        TextView time;

        public FeaturedViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.favLandmark);
            title = itemView.findViewById(R.id.landmarkCity);
            name = itemView.findViewById(R.id.landmarkName);
            distance = itemView.findViewById(R.id.landmarkDistance);
            time = itemView.findViewById(R.id.landmarkTime);

        }
    }

}
