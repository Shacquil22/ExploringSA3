package com.example.exploringsa3.Category;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exploringsa3.AllPlaces;
import com.example.exploringsa3.Favourites.FavouriteHelperClass;
import com.example.exploringsa3.Maps;
import com.example.exploringsa3.R;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.FeaturedViewHolder> {

    static ArrayList<CategoryHelperClass> catHelper;

    public CategoryAdapter(ArrayList<CategoryHelperClass> categoryHelper) {
        this.catHelper = categoryHelper;
    }

    @NonNull
    @Override
    public FeaturedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_card_view,parent,false);
        FeaturedViewHolder featuredViewHolder = new FeaturedViewHolder(view);

        return featuredViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedViewHolder holder, @SuppressLint("RecyclerView") int position) {

        CategoryHelperClass categoryHelperClass = catHelper.get(position);

        holder.image.setImageResource(categoryHelperClass.getImage());
        holder.name.setText(categoryHelperClass.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), AllPlaces.class);
                i.putExtra("name", catHelper.get(position).getName());
                v.getContext().startActivity(i);
                Log.d("TAG", catHelper.get(position).getName());

            }
        });
    }

    @Override
    public int getItemCount() {
        return catHelper.size();
    }

    public interface RecyclerViewOnClickListener{
        void onClick(View v,int position);
    }

    public static class FeaturedViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView name;

        public FeaturedViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.categoryIcon);
            name = itemView.findViewById(R.id.categoryName);

        }
    }

}
