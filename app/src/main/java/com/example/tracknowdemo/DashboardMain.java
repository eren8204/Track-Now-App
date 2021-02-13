package com.example.tracknowdemo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;

import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class DashboardMain extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
//    private static final String TAG = "MainActivity";
//    private static final int ERROR_DIALOG_REQUEST = 9001;
//    private AlertDialog.Builder dialogBuilder;
//    private AlertDialog dialog;
//    private TextInputLayout shareId, sharePassword;
//    private Button cancelBtn, shareConfirmBtn, shareLocationButton, trackLocationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

//        homeFragmentHandle();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_profile, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }
//    private void homeFragmentHandle() {
//        final View homeView = getLayoutInflater().inflate(R.layout.activity_main, null);
//
//        if (isServicesOK()) {
//            init();
//        }
//        shareLocationButton = homeView.findViewById(R.id.shareLocationButton);
//        shareLocationButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                createSharePopupDialog();
//            }
//        });
//    }
//
//    public void createSharePopupDialog() {
//        dialogBuilder = new AlertDialog.Builder(DashboardMain.this);
//        final View shareConfirmPopupView = getLayoutInflater().inflate(R.layout.share_confirm_popup_view, null);
//        shareId = shareConfirmPopupView.findViewById(R.id.shareId);
//        sharePassword = shareConfirmPopupView.findViewById(R.id.sharePassword);
//        cancelBtn = shareConfirmPopupView.findViewById(R.id.cancelBtn);
//        shareConfirmBtn = shareConfirmPopupView.findViewById(R.id.shareConfirmBtn);
//        dialogBuilder.setView(shareConfirmPopupView);
//        dialog = dialogBuilder.create();
//        dialog.show();
//        shareConfirmBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isServicesOK()) {
//                    Intent intent = new Intent(DashboardMain.this, DriverMapActivity.class);
//                    startActivity(intent);
//                    dialog.dismiss();
//                }
//            }
//        });
//        cancelBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//    }
//
//    private void init() {
//        final View homeView = getLayoutInflater().inflate(R.layout.activity_main, null);
//        Button btnMap = homeView.findViewById(R.id.btnMap);
//        btnMap.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(DashboardMain.this, DriverMapActivity.class);
//                startActivity(intent);
//            }
//        });
//
//    }
//
//    public boolean isServicesOK() {
//        Log.d(TAG, "isServiceOK:checking google services version");
//
//        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(DashboardMain.this);
//        if (available == ConnectionResult.SUCCESS) {
//            //everything is fine and the user can make map requests
//            Log.d(TAG, "isServicesOK:Google Play Services is working");
//            return true;
//        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
//            //an error occurred but we can resolve it
//            Log.d(TAG, "isServicesOK: An error occurred but we can fix it");
//            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(DashboardMain.this, available, ERROR_DIALOG_REQUEST);
//            dialog.show();
//        } else {
//
//            Toast.makeText(DashboardMain.this, "You can't make map requests", Toast.LENGTH_SHORT).show();
//        }
//        return false;
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard_main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}