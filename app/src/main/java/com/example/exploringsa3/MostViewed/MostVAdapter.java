package com.example.exploringsa3.MostViewed;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exploringsa3.R;

import java.util.ArrayList;

public class MostVAdapter extends RecyclerView.Adapter<MostVAdapter.FeaturedViewHolder> {

    ArrayList<MostVHelperClass> featuredLocations;

    public MostVAdapter(ArrayList<MostVHelperClass> featuredLocations) {
        this.featuredLocations = featuredLocations;
    }

    @NonNull
    @Override
    public FeaturedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.most_viewed_card_design,parent,false);
        FeaturedViewHolder featuredViewHolder = new FeaturedViewHolder(view);

        return featuredViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedViewHolder holder, int position) {

        MostVHelperClass mostVHelperClass = featuredLocations.get(position);

        holder.mvImage.setImageResource(mostVHelperClass.getImage());
        holder.mvTitle.setText(mostVHelperClass.getTitle());
        holder.mvDescription.setText(mostVHelperClass.getDescription());
        holder.mvRating.setRating((float) mostVHelperClass.getRating());

    }

    @Override
    public int getItemCount() {
        return featuredLocations.size();
    }


    public static class FeaturedViewHolder extends RecyclerView.ViewHolder{

        ImageView mvImage;
        TextView mvTitle, mvDescription;
        RatingBar mvRating;

        public FeaturedViewHolder(@NonNull View itemView) {
            super(itemView);

            mvImage = itemView.findViewById(R.id.mv_image);
            mvTitle = itemView.findViewById(R.id.mv_title);
            mvDescription = itemView.findViewById(R.id.mv_description);
            mvRating = itemView.findViewById(R.id.mv_rating);
        }
    }
}
