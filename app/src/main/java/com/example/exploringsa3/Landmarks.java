package com.example.exploringsa3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.exploringsa3.Category.CategoryAdapter;
import com.example.exploringsa3.Category.CategoryHelperClass;
import com.example.exploringsa3.HelperClasses.FeaturedAdapter;
import com.example.exploringsa3.HelperClasses.FeaturedHelperClass;
import com.example.exploringsa3.MostViewed.MostVAdapter;
import com.example.exploringsa3.MostViewed.MostVHelperClass;

import java.util.ArrayList;

public class Landmarks extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView rvMostViewed;
    private RecyclerView rvCategory;
    RecyclerView.Adapter adapter;
    RecyclerView.Adapter adapterMostViewed;
    RecyclerView.Adapter adapterCategory;
    private SharedPreferences result;
    private String city;
    private TextView mostViewed;
    private LinearLayout llMostViewed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landmarks);

        ImageView filter = findViewById(R.id.imgFilter);
        recyclerView = findViewById(R.id.rViewCities);
        rvMostViewed = findViewById(R.id.rvMostViewed);
        rvCategory = findViewById(R.id.rvCategory);

        mostViewed = findViewById(R.id.txtViewAllMostViewed);
        llMostViewed = findViewById(R.id.llMostViewed);

        result = getSharedPreferences("SelectedCity", Context.MODE_PRIVATE);
        city = result.getString("City","City");

        recyclerView();
        mostViewed();
        Categories();

        mostViewed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams
                        .MATCH_PARENT,1000,1.0f);
                llMostViewed.setLayoutParams(params);
            }
        });

        //registerForContextMenu(filter);
    }

    private void recyclerView() {

        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        ArrayList<FeaturedHelperClass> featuredLocations = new ArrayList<>();

        featuredLocations.add(new FeaturedHelperClass(R.drawable.nelson_mandela_square,"Nelson Mandela Square",4.6));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.voortrekker_monument,"Voortrekker Monument",4.6));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.union_buildings,"Union Buildings",4.5));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.hector_pieterson_memorial,"Hector Pieterson Memorial",4.4));

        adapter = new FeaturedAdapter(featuredLocations);

        recyclerView.setAdapter(adapter);

        GradientDrawable gradient1 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xffeff400,0xffaff600});
    }

    private void mostViewed() {
        rvMostViewed.hasFixedSize();
        rvMostViewed.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        ArrayList<MostVHelperClass> mostVHelperClasses = new ArrayList<>();

        mostVHelperClasses.add(new MostVHelperClass(R.drawable.nelson_mandela_square,4.6,"Nelson Mandela Square","This is Nelson Mandela Square"));
        mostVHelperClasses.add(new MostVHelperClass(R.drawable.union_buildings,4.5,"Union Buildings","This is Union Buildings"));


        adapterMostViewed = new MostVAdapter(mostVHelperClasses);

        rvMostViewed.setAdapter(adapterMostViewed);
    }

    private void Categories() {
        rvCategory.hasFixedSize();
        rvCategory.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        ArrayList<CategoryHelperClass> categoryHelperClass = new ArrayList<>();

        categoryHelperClass.add(new CategoryHelperClass(R.drawable.historical,"Historical"));
        categoryHelperClass.add(new CategoryHelperClass(R.drawable.modern, "Modern"));
        categoryHelperClass.add(new CategoryHelperClass(R.drawable.popular, "Popular"));


        adapterCategory = new CategoryAdapter(categoryHelperClass);

        rvCategory.setAdapter(adapterCategory);
    }
}