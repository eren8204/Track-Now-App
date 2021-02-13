package com.example.tracknowdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_signup);
        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupToLoginScreenTransition();
            }
        });

    }

    void signupToLoginScreenTransition() {
        ImageView image;
        TextView welcomeText, logo_desc;
        TextInputLayout username, pass;
        Button signupButton, loginButton;

        username = findViewById(R.id.username);
        pass = findViewById(R.id.password);
        image = findViewById(R.id.logoImage);
        welcomeText = findViewById(R.id.welcomeText);
        logo_desc = findViewById(R.id.logo_desc);
        signupButton = findViewById(R.id.signupButton);
        loginButton = findViewById(R.id.loginButton);

        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        Pair[] pairs = new Pair[7];
        pairs[0] = new Pair<View, String>(image, "logo_image");
        pairs[1] = new Pair<View, String>(welcomeText, "welcomeText");
        pairs[2] = new Pair<View, String>(logo_desc, "logo_desc");
        pairs[3] = new Pair<View, String>(username, "username_tran");
        pairs[4] = new Pair<View, String>(pass, "password_tran");
        pairs[5] = new Pair<View, String>(loginButton, "button_tran");
        pairs[6] = new Pair<View, String>(signupButton, "login_signup_tran");
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignupActivity.this, pairs);
        startActivity(intent, options.toBundle());
        finish();
    }
    @Override
    public void onBackPressed() {
        finish();
    }


}