package com.example.tracknowdemo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.Toast;

import com.example.tracknowdemo.ui.home.HomeFragment;
import com.example.tracknowdemo.ui.profile.ProfileFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.model.Dash;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

public class DashboardMain extends AppCompatActivity{

    private AppBarConfiguration mAppBarConfiguration;
    ActionBarDrawerToggle toggle;
    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
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


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home_fragment, R.id.nav_profile_fragment)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

//        toggle=new ActionBarDrawerToggle(this,drawer,R.string.open,R.string.close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.nav_logout:
                        createLogoutPopupDialog();
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.nav_profile:
                        navController.navigate(R.id.nav_profile_fragment);
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.nav_myLocation:
                        navController.navigate(R.id.my_location_fragment);
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.nav_home:
                        navController.navigate(R.id.nav_home_fragment);
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.nav_shareLocation:
                        createSharePopupDialog();
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.nav_trackLocation:
                        createTrackLocationPopupDialog();
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }
    public void createSharePopupDialog() {
        AlertDialog.Builder dialogBuilder;
        AlertDialog dialog;
        TextInputLayout shareId, sharePassword;
        Button cancelBtn, shareConfirmBtn;
        dialogBuilder = new AlertDialog.Builder(DashboardMain.this);
        final View shareConfirmPopupView = getLayoutInflater().inflate(R.layout.share_confirm_popup_view, null);
        shareId = shareConfirmPopupView.findViewById(R.id.shareId);
        sharePassword = shareConfirmPopupView.findViewById(R.id.sharePassword);
        cancelBtn = shareConfirmPopupView.findViewById(R.id.cancelBtn);
        shareConfirmBtn = shareConfirmPopupView.findViewById(R.id.shareConfirmBtn);
        dialogBuilder.setView(shareConfirmPopupView);
        dialog = dialogBuilder.create();
        dialog.show();
        shareConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isServicesOK()) {
                    NavController navController = Navigation.findNavController(DashboardMain.this, R.id.nav_host_fragment);
                    navController.navigate(R.id.my_location_fragment);
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
    public void createTrackLocationPopupDialog() {
        AlertDialog.Builder dialogBuilder;
        AlertDialog dialog;
        TextInputLayout trackLocationId, trackLocationPassword;
        Button trackLocationCancelBtn, trackLocationConfirmBtn;
        dialogBuilder = new AlertDialog.Builder(DashboardMain.this);
        final View trackLocationConfirmView = getLayoutInflater().inflate(R.layout.track_location_confirm_view, null);
        trackLocationId = trackLocationConfirmView.findViewById(R.id.trackLocationId);
        trackLocationPassword = trackLocationConfirmView.findViewById(R.id.trackLocationPassword);
        trackLocationCancelBtn = trackLocationConfirmView.findViewById(R.id.trackLocationCancelBtn);
        trackLocationConfirmBtn = trackLocationConfirmView.findViewById(R.id.trackLocationConfirmBtn);
        dialogBuilder.setView(trackLocationConfirmView);
        dialog = dialogBuilder.create();
        dialog.show();
        trackLocationConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isServicesOK()) {
                    NavController navController = Navigation.findNavController(DashboardMain.this, R.id.nav_host_fragment);
                    navController.navigate(R.id.track_location_fragment);
                    dialog.dismiss();
                }
            }
        });
        trackLocationCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    public boolean isServicesOK() {
        Log.d(TAG, "isServiceOK:checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(DashboardMain.this);
        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK:Google Play Services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occurred but we can resolve it
            Log.d(TAG, "isServicesOK: An error occurred but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(DashboardMain.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {

            Toast.makeText(DashboardMain.this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
    public void createLogoutPopupDialog() {
        AlertDialog.Builder dialogBuilder;
        AlertDialog dialog;
        Button cancelBtn, logoutBtn;
        dialogBuilder = new AlertDialog.Builder(DashboardMain.this);
        final View logoutConfirmPopupView = getLayoutInflater().inflate(R.layout.logout_confirm_popup_view, null);
        cancelBtn = logoutConfirmPopupView.findViewById(R.id.cancelBtn);
        logoutBtn = logoutConfirmPopupView.findViewById(R.id.logoutBtn);
        dialogBuilder.setView(logoutConfirmPopupView);
        dialog = dialogBuilder.create();
        dialog.show();
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardMain.this, LoginActivity.class);
                startActivity(intent);
                finish();
                dialog.dismiss();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                Toast.makeText(DashboardMain.this, "App Settings is clicked.", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

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