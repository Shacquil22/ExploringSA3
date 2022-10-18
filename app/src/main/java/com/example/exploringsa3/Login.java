package com.example.exploringsa3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.Objects;

public class Login extends AppCompatActivity {

    private ImageButton btnGoogle;

    private EditText edtUserEmail;
    private EditText edtPassword;

    private FirebaseAuth Auth;

    private SharedPreferences sharedPreferences;

    private static final int RC_SIGN_IN = 100;
    private GoogleSignInClient googleSignInClient;
    private static final String TAG = "GOOGLE_SIGN_IN_TAG";
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        MaterialButton btnLogin = findViewById(R.id.btnLogin);
        btnGoogle = findViewById(R.id.btnGoogle);
        TextView signUp = findViewById(R.id.tvm_SignUp);
        TextView forgotPassword = findViewById(R.id.tvm_forgotPassword);

        edtUserEmail = findViewById(R.id.edtEmailL);
        edtPassword = findViewById(R.id.edtPasswordL);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this,gso);

        Auth = FirebaseAuth.getInstance();


        forgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(Login.this,ForgotPassword.class));
            finish();
        });

        signUp.setOnClickListener(v -> {
            startActivity(new Intent(Login.this,Signup.class));
            finish();
        });

        btnLogin.setOnClickListener(v -> {
            FirebaseAuthLogin();
        });

        btnGoogle.setOnClickListener(v -> {
            googleSignInUser();
        });
    }

    private void googleSignInUser() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else {
            // Pass the activity result back to the Facebook SDK
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try{
            GoogleSignInAccount acc = completedTask.getResult(ApiException.class);
            Toast.makeText(Login.this,"Singed In Successfully",Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(acc);
        } catch(ApiException e) {
            Toast.makeText(Login.this,"Singed In Failed",Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(null);
        }
    }

    private void FirebaseGoogleAuth(GoogleSignInAccount acc) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acc.getIdToken(), null);
        Auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(Login.this,"Successful",Toast.LENGTH_SHORT).show();
                    FirebaseUser user = Auth.getCurrentUser();
                    updateUI(user);
                } else {
                    Toast.makeText(Login.this,"Failed",Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }

    private void updateUI(FirebaseUser user) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if(account != null) {
            String personName = account.getDisplayName();
            String personID = account.getId();
            String personEmail = account.getEmail();

            googleUser gUser = new googleUser(personID,personName,personEmail);

            FirebaseDatabase.getInstance().getReference("Users")
                    .child("google")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .setValue(gUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(Login.this,Dashboard.class));
                                finish();
                            } else {
                                Toast.makeText(Login.this, "Failed to register user.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }

    private void facebookLogin() {

        callbackManager = CallbackManager.Factory.create();


        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        Auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = Auth.getCurrentUser();
                            updateUIF(user);
                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUIF(null);
                        }
                    }
                });
    }

    private void updateUIF(FirebaseUser user) {

        Profile profile = Profile.getCurrentProfile();

        if(profile != null) {
            String personID = profile.getId();
            String personName = profile.getName();
            String personLastName = profile.getLastName();

            facebookUser fUser = new facebookUser(personID,personName,personLastName);

            FirebaseDatabase.getInstance().getReference("Users")
                    .child("facebook")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .setValue(fUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(Login.this,Dashboard.class));
                                finish();
                            } else {
                                Toast.makeText(Login.this, "Failed to register user.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void FirebaseAuthLogin() {

        String email = edtUserEmail.getText().toString();
        String password = edtPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            edtUserEmail.setError("Please enter Email");
        } else if (TextUtils.isEmpty(password)) {
            edtPassword.setError("Please enter password");
        } else {
            Auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String em = user.getEmail();

                        sharedPreferences = getSharedPreferences("savedEmail", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("UEmail",em);
                        editor.apply();

                        Toast.makeText(Login.this, "Welcome", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(Login.this, Dashboard.class);
                        startActivity(i);
                    } else if(email != Auth.getCurrentUser().getEmail()){
                        edtUserEmail.setError("Email not found");
                    } else {
                        edtPassword.setError("Incorrect password");
                    }
                }
            });
        }
    }

   
}