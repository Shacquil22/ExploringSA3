package com.example.exploringsa3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private EditText edtEmail;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        edtEmail = findViewById(R.id.edtFpEmail);
        MaterialButton btnSend = findViewById(R.id.btnSend);

        auth = FirebaseAuth.getInstance();

        btnSend.setOnClickListener(v -> recover());
    }

    private void recover(){
        String email = edtEmail.getText().toString();

        if(email.isEmpty()){
            edtEmail.setError("Email required");
            edtEmail.requestFocus();
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edtEmail.setError("Enter a valid email");
            edtEmail.requestFocus();
            return;
        }

        auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(ForgotPassword.this, "Email sent", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ForgotPassword.this,Login.class));
            } else {
                Toast.makeText(ForgotPassword.this, "An error occurred. Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }
}