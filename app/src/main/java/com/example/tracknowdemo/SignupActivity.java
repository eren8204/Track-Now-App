package com.example.tracknowdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tracknowdemo.databinding.ActivityDriverMapBinding;
import com.example.tracknowdemo.databinding.ActivitySignupBinding;
import com.example.tracknowdemo.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;

public class SignupActivity extends AppCompatActivity {
    private ActivitySignupBinding binding;
    private FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
//        setContentView(R.layout.activity_signup);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(SignupActivity.this);
        progressDialog.setTitle("Creating Account...");
        progressDialog.setMessage("We're creating your account.");
        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupToLoginScreenTransition();
            }
        });
        binding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.emailTextField.getText().toString();
                String pass = binding.passwordTextField.getText().toString();
                if (email.matches("") && pass.matches("")) {
                    binding.email.setError("You need to enter a valid email.");
                    binding.password.setError("You need to enter a strong password.");
                } else if (email.matches("")) {
                    binding.email.setError("You need to enter a valid email.");
                } else if (pass.matches("") || pass.length() < 6) {
                    binding.password.setError("You need to enter a strong password(at least 6 characters).");
                } else {
                    progressDialog.show();
                    database = FirebaseDatabase.getInstance();
                    auth.createUserWithEmailAndPassword(email, pass).
                            addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                      @Override
                                                      public void onComplete(@NonNull Task<AuthResult> task) {
                                                          if (task.isSuccessful()) {
                                                              try {
                                                                  String userId = task.getResult().getUser().getUid();
                                                                  Users user = new Users(userId, binding.fullnameTextField.getText().toString(), email, binding.phoneNumberTextField.getText().toString(), pass);
                                                                  database.getReference("Users").child(userId).setValue(user);
                                                                  progressDialog.dismiss();
                                                                  Toast.makeText(SignupActivity.this, "User created Successfully", Toast.LENGTH_SHORT).show();
                                                                  signupToLoginScreenTransition();
                                                              } catch (Exception e) {
                                                                  progressDialog.dismiss();
                                                                  Toast.makeText(SignupActivity.this, "Input Error Occurred.", Toast.LENGTH_SHORT).show();
                                                              }
                                                          } else {
                                                              progressDialog.dismiss();
                                                              Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                              task.getException();
                                                          }
                                                      }
                                                  }
                            );
                }

            }
        });
    }

    void signupToLoginScreenTransition() {
        ImageView image;
        TextView welcomeText, logo_desc;
        TextInputLayout username, pass;
        Button signupButton, loginButton;

        pass = binding.password;
        image = binding.logoImage;
        welcomeText = binding.welcomeText;
        logo_desc = binding.logoDescription;
        signupButton = binding.signupButton;
        loginButton = binding.loginButton;

        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        Pair[] pairs = new Pair[6];
        pairs[0] = new Pair<View, String>(image, "logo_image");
        pairs[1] = new Pair<View, String>(welcomeText, "welcomeText");
        pairs[2] = new Pair<View, String>(logo_desc, "logo_desc");
        pairs[3] = new Pair<View, String>(pass, "password_tran");
        pairs[4] = new Pair<View, String>(loginButton, "button_tran");
        pairs[5] = new Pair<View, String>(signupButton, "login_signup_tran");
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignupActivity.this, pairs);
        startActivity(intent, options.toBundle());
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }


}