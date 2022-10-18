package com.example.exploringsa3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.exploringsa3.Category.CategoryAdapter;
import com.example.exploringsa3.Category.CategoryHelperClass;
import com.example.exploringsa3.HelperClasses.FeaturedHelperClass;
import com.example.exploringsa3.MostViewed.MostVAdapter;
import com.example.exploringsa3.MostViewed.MostVHelperClass;

import java.util.ArrayList;
import java.util.Objects;

public class AllPlaces extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapterCategory;
    private TextView txtHeading;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_places);

        recyclerView = findViewById(R.id.rvAllPlaces);
        txtHeading = findViewById(R.id.txtHeading);
        btnBack = findViewById(R.id.btnAllBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AllPlaces.this,Landmarks.class));
                finish();
            }
        });

        loadCategory();
    }

    public void loadCategory(){
        Intent i = new Intent();

        String category = getIntent().getStringExtra("name");

        txtHeading.setText(category);

        if(Objects.equals(category, "Historical")){

            recyclerView.hasFixedSize();
            recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

            ArrayList<CategoryHelperClass> categoryHelperClasses = new ArrayList<>();

            categoryHelperClasses.add(new CategoryHelperClass(R.drawable.voortrekker_monument,"Voortrekker Monument"));
            categoryHelperClasses.add(new CategoryHelperClass(R.drawable.union_buildings,"Union Buildings"));


            adapterCategory = new CategoryAdapter(categoryHelperClasses);

            recyclerView.setAdapter(adapterCategory);

        } else if(Objects.equals(category, "Modern")){

            recyclerView.hasFixedSize();
            recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

            ArrayList<CategoryHelperClass> categoryHelperClasses = new ArrayList<>();

            categoryHelperClasses.add(new CategoryHelperClass(R.drawable.nelson_mandela_square,"Nelson Mandela Square"));

            adapterCategory = new CategoryAdapter(categoryHelperClasses);

            recyclerView.setAdapter(adapterCategory);

        } else if (Objects.equals(category, "Popular")){

            recyclerView.hasFixedSize();
            recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

            ArrayList<CategoryHelperClass> categoryHelperClasses = new ArrayList<>();

            categoryHelperClasses.add(new CategoryHelperClass(R.drawable.nelson_mandela_square,"Nelson Mandela Square"));
            categoryHelperClasses.add(new CategoryHelperClass(R.drawable.union_buildings,"Union Buildings"));

            adapterCategory = new CategoryAdapter(categoryHelperClasses);

            recyclerView.setAdapter(adapterCategory);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}