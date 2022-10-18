package com.example.exploringsa3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyProfile extends AppCompatActivity {

    private FirebaseUser user;
    private String uid;
    private DatabaseReference reference;

    private EditText edtUsername;
    private EditText edtEmail;
    private EditText edtNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        edtUsername = findViewById(R.id.edtProfileUsername);
        edtEmail = findViewById(R.id.edtProfileEmail);
        edtNumber = findViewById(R.id.edtProfileNumber);

        userInfo();

    }

    public void userInfo(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Users");

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uID = firebaseUser.getUid();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    edtUsername.setText(String.valueOf(snapshot.child(uID).child("username").getValue()));
                    edtEmail.setText(String.valueOf(snapshot.child(uID).child("email").getValue()));
                    edtNumber.setText(String.valueOf(snapshot.child(uID).child("number").getValue()));
                } else {
                    Toast.makeText(MyProfile.this, "No data found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}