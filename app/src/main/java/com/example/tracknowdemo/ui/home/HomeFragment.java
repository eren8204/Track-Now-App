package com.example.tracknowdemo.ui.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.tracknowdemo.DashboardMain;
import com.example.tracknowdemo.DriverMapActivity;
import com.example.tracknowdemo.R;
import com.example.tracknowdemo.ui.map.MyLocation;
import com.example.tracknowdemo.ui.profile.LoginActivity;
import com.example.tracknowdemo.ui.slideshow.SlideshowViewModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {

    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button cancelBtn, shareConfirmBtn;
    CardView trackLocationButton, shareLocationButton, myLocationBtn;
    DatabaseReference databaseReference;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_main, container, false);

        trackLocationButton = root.findViewById(R.id.trackLocationButton);
        shareLocationButton = root.findViewById(R.id.shareLocationButton);
        myLocationBtn = root.findViewById(R.id.btnMap);

        myLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isServicesOK()) {
                    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                    navController.navigate(R.id.my_location_fragment);
                }
            }
        });

        shareLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSharePopupDialog();
            }
        });

        trackLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTrackLocationPopupDialog();
            }
        });
        return root;
    }


    public void createTrackLocationPopupDialog() {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Tracking The Location..");
        progressDialog.setMessage("This may take some time depending on your internet connection. Please keep patience.");
        AlertDialog.Builder dialogBuilder;
        AlertDialog dialog;
        Button trackLocationCancelBtn, trackLocationConfirmBtn;
        dialogBuilder = new AlertDialog.Builder(getActivity());
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
                        Toast.makeText(getActivity(), "You have to put a correct tracking id and password.", Toast.LENGTH_LONG).show();
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
                                        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                                        navController.navigate(R.id.track_location_fragment, bundle);
                                        dialog.dismiss();
                                    }else{
                                        trackLocationPasswordTextField.setError("This Tracking password is incorrect.");
                                    }
                                    progressDialog.dismiss();
                                } catch (NullPointerException e) {
                                    trackLocationIdTextField.setError("This Tracking id doesn't exists.");
                                    progressDialog.dismiss();
//                                    Toast.makeText(getActivity(), "This Tracking id doesn't exists.", Toast.LENGTH_LONG).show();
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(getActivity(), "The database read failed: "+ databaseError.getCode(), Toast.LENGTH_LONG).show();
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


    public void createSharePopupDialog() {
        dialogBuilder = new AlertDialog.Builder(getActivity());
        final View shareConfirmPopupView = getLayoutInflater().inflate(R.layout.share_confirm_popup_view, null);
        TextInputLayout shareIdTextField = shareConfirmPopupView.findViewById(R.id.shareId);
        TextInputLayout sharePassTextField = shareConfirmPopupView.findViewById(R.id.sharePassword);
        cancelBtn = shareConfirmPopupView.findViewById(R.id.cancelBtn);
        shareConfirmBtn = shareConfirmPopupView.findViewById(R.id.shareConfirmBtn);
        dialogBuilder.setView(shareConfirmPopupView);
        dialog = dialogBuilder.create();
        dialog.show();
        shareConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myShareId = shareIdTextField.getEditText().getText().toString();
                String mySharePass = sharePassTextField.getEditText().getText().toString();

                if (isServicesOK()) {
                    if (myShareId.matches("") || mySharePass.matches("")) {
                        Toast.makeText(getActivity(), "You have to put a secured unique share id and password.", Toast.LENGTH_LONG).show();
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putString("shareId", myShareId);
                        bundle.putString("sharePass", mySharePass);
                        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                        navController.navigate(R.id.share_location_fragment, bundle);
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


    public boolean isServicesOK() {
        Log.d(TAG, "isServiceOK:checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getActivity());
        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK:Google Play Services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occurred but we can resolve it
            Log.d(TAG, "isServicesOK: An error occurred but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(getActivity(), "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}