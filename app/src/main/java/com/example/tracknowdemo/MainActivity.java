package com.example.tracknowdemo;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private TextInputLayout shareId, sharePassword;
    private Button cancelBtn,shareConfirmBtn,shareLocationButton,trackLocationButton;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        getSupportActionBar().hide();
//        toolbar = findViewById(R.id.my_toolbar);
//        setSupportActionBar(toolbar);

//        getSupportActionBar();
//        drawerLayout=findViewById(R.id.drawer_layout);
//        navigationView=findViewById(R.id.nav_view);
//        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this, drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
//        drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();
//        navigationView.setNavigationItemSelectedListener(this);
        trackLocationButton=findViewById(R.id.trackLocationButton);
        trackLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        if(isServicesOK()){
            init();
        }
        shareLocationButton=findViewById(R.id.shareLocationButton);
        shareLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSharePopupDialog();
            }
        });

    }

    public void createSharePopupDialog(){
        dialogBuilder=new AlertDialog.Builder(this);
        final View shareConfirmPopupView=getLayoutInflater().inflate(R.layout.share_confirm_popup_view,null);
        shareId=shareConfirmPopupView.findViewById(R.id.shareId);
        sharePassword=shareConfirmPopupView.findViewById(R.id.sharePassword);
        cancelBtn=shareConfirmPopupView.findViewById(R.id.cancelBtn);
        shareConfirmBtn=shareConfirmPopupView.findViewById(R.id.shareConfirmBtn);
        dialogBuilder.setView(shareConfirmPopupView);
        dialog=dialogBuilder.create();
        dialog.show();
        shareConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isServicesOK()){
                    Intent intent =new Intent(MainActivity.this,DriverMapActivity.class);
                    startActivity(intent);
                    dialog.dismiss();
                }
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

     }

    private void init(){
        Button btnMap=findViewById(R.id.btnMap);
        btnMap.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity.this,DriverMapActivity.class);
                startActivity(intent);
            }
        });

    }

    public boolean isServicesOK() {
        Log.d(TAG, "isServiceOK:checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK:Google Play Services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occurred but we can resolve it
            Log.d(TAG, "isServicesOK: An error occurred but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {

            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }
}