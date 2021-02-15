package com.example.tracknowdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tracknowdemo.databinding.ActivityLoginBinding;
import com.example.tracknowdemo.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    ImageView image;
    TextView welcomeText, logo_desc;
    TextInputLayout username, pass;
    Button signupButton, loginButton;
    ProgressDialog progressDialog;
    private FirebaseAuth auth;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();

//        getSupportActionBar().hide();
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Logging Into Account...");
        binding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginToSignupScreenTransition();
            }
        });

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.emailTextField.getText().toString();
                String pass = binding.passwordTextField.getText().toString();
                if (email.matches("") && pass.matches("")) {
                    binding.email.setError("You need to enter a valid email.");
                    binding.password.setError("You have to enter correct password.");
                } else if (email.matches("")) {
                    binding.email.setError("You need to enter a valid email.");
                } else if (pass.matches("") || pass.length() < 6) {
                    binding.password.setError("You have to enter correct password(at least 6 characters).");
                } else {
                    progressDialog.show();

                    database = FirebaseDatabase.getInstance();
                    auth.signInWithEmailAndPassword(email, pass).
                            addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        Intent intent = new Intent(LoginActivity.this, DashboardMain.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(LoginActivity.this, "User is not valid.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }
        });

    }

    void loginToSignupScreenTransition() {
        pass = binding.password;
        image = binding.logoImage;
        welcomeText = binding.welcomeText;
        logo_desc = binding.logoDescription;
        signupButton = binding.signupButton;
        loginButton = binding.loginButton;

        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        Pair[] pairs = new Pair[6];
        pairs[0] = new Pair<View, String>(image, "logo_image");
        pairs[1] = new Pair<View, String>(welcomeText, "welcomeText");
        pairs[2] = new Pair<View, String>(logo_desc, "logo_desc");
        pairs[3] = new Pair<View, String>(pass, "password_tran");
        pairs[4] = new Pair<View, String>(loginButton, "button_tran");
        pairs[5] = new Pair<View, String>(signupButton, "login_signup_tran");
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, pairs);
        startActivity(intent, options.toBundle());
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}