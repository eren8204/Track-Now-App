package com.example.tracknowdemo;

import com.example.tracknowdemo.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.Toast;

import com.example.tracknowdemo.ui.map.MyLocation;
import com.example.tracknowdemo.ui.profile.LoginActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DashboardMain extends AppCompatActivity{

    private AppBarConfiguration mAppBarConfiguration;
    ActionBarDrawerToggle toggle;
    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private FirebaseAuth auth;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_main);
        auth = FirebaseAuth.getInstance();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient= GoogleSignIn.getClient(this,gso);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Message Section will be implemented.", Snackbar.LENGTH_LONG)
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

                if (id == R.id.nav_logout) {
                    createLogoutPopupDialog();
                    drawer.closeDrawer(GravityCompat.START);
                    return true;
                } else if (id == R.id.nav_profile) {
                    navController.navigate(R.id.nav_profile_fragment);
                    drawer.closeDrawer(GravityCompat.START);
                    return true;
                } else if (id == R.id.nav_myLocation) {
                    navController.navigate(R.id.my_location_fragment);
                    drawer.closeDrawer(GravityCompat.START);
                    return true;
                } else if (id == R.id.nav_home) {
                    navController.navigate(R.id.nav_home_fragment);
                    drawer.closeDrawer(GravityCompat.START);
                    return true;
                } else if (id == R.id.nav_shareLocation) {
                    createSharePopupDialog();
                    drawer.closeDrawer(GravityCompat.START);
                    return true;
                } else if (id == R.id.nav_trackLocation) {
                    createTrackLocationPopupDialog();
                    drawer.closeDrawer(GravityCompat.START);
                    return true;
                } else {
                    return false;
                }
            }
        });

    }
    private AlertDialog dialog;
    private AlertDialog.Builder dialogBuilder;
    private Button cancelBtn, shareConfirmBtn;
    public void createSharePopupDialog() {
        dialogBuilder = new AlertDialog.Builder(DashboardMain.this);
        final View shareConfirmPopupView = getLayoutInflater().inflate(R.layout.share_confirm_popup_view, null);
        TextInputLayout shareIdTextField=shareConfirmPopupView.findViewById(R.id.shareId);
        TextInputLayout sharePassTextField=shareConfirmPopupView.findViewById(R.id.sharePassword);

        cancelBtn = shareConfirmPopupView.findViewById(R.id.cancelBtn);
        shareConfirmBtn = shareConfirmPopupView.findViewById(R.id.shareConfirmBtn);
        dialogBuilder.setView(shareConfirmPopupView);
        dialog = dialogBuilder.create();
        dialog.show();
        shareConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myShareId=shareIdTextField.getEditText().getText().toString();
                String mySharePass=sharePassTextField.getEditText().getText().toString();

                if (isServicesOK()) {
                    if(myShareId.matches("")||mySharePass.matches("")){
                        Toast.makeText(DashboardMain.this, "You have to put a secured unique share id and password.", Toast.LENGTH_LONG).show();
                    }else{
                        Bundle bundle = new Bundle();
                        bundle.putString("shareId", myShareId);
                        bundle.putString("sharePass", mySharePass);
                        NavController navController = Navigation.findNavController(DashboardMain.this, R.id.nav_host_fragment);
                        navController.navigate(R.id.share_location_fragment,bundle);
                        dialog.dismiss();
                    }

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
        ProgressDialog progressDialog = new ProgressDialog(DashboardMain.this);
        progressDialog.setTitle("Tracking The Location..");
        progressDialog.setMessage("This may take some time depending on your internet connection. Please keep patience.");
        AlertDialog.Builder dialogBuilder;
        AlertDialog dialog;
        Button trackLocationCancelBtn, trackLocationConfirmBtn;
        dialogBuilder = new AlertDialog.Builder(DashboardMain.this);
        final View trackLocationConfirmView = getLayoutInflater().inflate(R.layout.track_location_confirm_view, null);
        TextInputLayout trackLocationIdTextField = trackLocationConfirmView.findViewById(R.id.trackLocationId);
        TextInputLayout trackLocationPasswordTextField = trackLocationConfirmView.findViewById(R.id.trackLocationPassword);
        TextInputEditText trackLocationIdEditText= trackLocationConfirmView.findViewById(R.id.trackLocationIdEditText);
        TextInputEditText trackLocationPasswordEditText= trackLocationConfirmView.findViewById(R.id.trackLocationPasswordEditText);
        trackLocationIdEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                trackLocationIdTextField.setError(null);
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        trackLocationPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                trackLocationPasswordTextField.setError(null);
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        trackLocationCancelBtn = trackLocationConfirmView.findViewById(R.id.trackLocationCancelBtn);
        trackLocationConfirmBtn = trackLocationConfirmView.findViewById(R.id.trackLocationConfirmBtn);
        dialogBuilder.setView(trackLocationConfirmView);
        dialog = dialogBuilder.create();
        dialog.show();
        trackLocationConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trackLocationId = trackLocationIdTextField.getEditText().getText().toString();
                String trackLocationPassword = trackLocationPasswordTextField.getEditText().getText().toString();
                if (isServicesOK()) {
                    if (trackLocationId.matches("") || trackLocationPassword.matches("")) {
                        Toast.makeText(DashboardMain.this, "You have to put a correct tracking id and password.", Toast.LENGTH_LONG).show();
                    } else {
                        progressDialog.show();
                        Bundle bundle = new Bundle();
                        databaseReference = FirebaseDatabase.getInstance().getReference("locations/" + trackLocationId);
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                try {
                                    MyLocation myLocation = dataSnapshot.getValue(MyLocation.class);
                                    if(myLocation.getSharePass().matches(trackLocationPassword)){
                                        bundle.putDouble("trackLocationCurrentLatitude", myLocation.getLatitude());
                                        bundle.putDouble("trackLocationCurrentLongitude", myLocation.getLongitude());
                                        bundle.putString("trackLocationId", trackLocationId);
                                        bundle.putString("trackLocationPassword", trackLocationPassword);
                                        NavController navController = Navigation.findNavController(DashboardMain.this, R.id.nav_host_fragment);
                                        navController.navigate(R.id.track_location_fragment, bundle);
                                        dialog.dismiss();
                                    }else{
                                        trackLocationPasswordTextField.setError("This Tracking password is incorrect.");
                                    }
                                    progressDialog.dismiss();
                                } catch (NullPointerException e) {
                                    trackLocationIdTextField.setError("This Tracking id doesn't exists.");
                                    progressDialog.dismiss();
//                                    Toast.makeText(DashboardMain.this, "This Tracking id doesn't exists.", Toast.LENGTH_LONG).show();
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(DashboardMain.this, "The database read failed: "+ databaseError.getCode(), Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            }
                        });
                    }
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
                auth.signOut();
                // Google sign out
                mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
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
        if (id == R.id.action_settings) {
            Toast.makeText(DashboardMain.this, "App Settings is clicked.", Toast.LENGTH_LONG).show();
            return true;
        } else if (id == R.id.action_logout) {
            createLogoutPopupDialog();
            return true;
        } else {
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