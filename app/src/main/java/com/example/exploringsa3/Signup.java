package com.example.exploringsa3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class Signup extends AppCompatActivity {

    private EditText edtUsername;
    private EditText edtNumber;
    private EditText edtEmail;
    private EditText edtPassword;

    private String username;
    private String number;
    private String email;
    private String password;

    private FirebaseAuth Auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        MaterialButton btnSignup = findViewById(R.id.btnSignUp);
        MaterialTextView txtLogin = findViewById(R.id.tvmLogin);

        edtUsername = findViewById(R.id.edtUsername);
        edtNumber = findViewById(R.id.edtNumber);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPasswordS);

        Auth = FirebaseAuth.getInstance();

        txtLogin.setOnClickListener(v -> {
            startActivity(new Intent(Signup.this, Login.class));
            finish();
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUp();
            }
        });
    }

    private void SignUp() {

            username = edtUsername.getText().toString();
            number = edtNumber.getText().toString();
            email = edtEmail.getText().toString();
            password = edtPassword.getText().toString();

            if (username.isEmpty()) {
                edtUsername.setError("Please enter your username");
                edtUsername.requestFocus();
                return;
            }

            if(number.isEmpty()){
                edtNumber.setError("Please enter your phone number");
                edtNumber.requestFocus();
                return;
            }

            if(email.isEmpty()){
                edtEmail.setError("Please enter your email");
                edtEmail.requestFocus();
                return;
            }

            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                edtEmail.setError("Please enter a valid email");
                edtEmail.requestFocus();
                return;
            }

            if(password.isEmpty()){
                edtPassword.setError("Please enter your password");
                edtPassword.requestFocus();
                return;
            }

            Auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        firebaseSignIn user = new firebaseSignIn(username,email,number,password);

                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(Signup.this, "User has been registered.", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(Signup.this,Login.class));
                                            finish();
                                        } else {
                                            Toast.makeText(Signup.this, "User not registered.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    } else {
                        Toast.makeText(Signup.this, "failed to register user.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

}