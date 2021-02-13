package com.example.tracknowdemo.ui.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.tracknowdemo.DashboardMain;
import com.example.tracknowdemo.DriverMapActivity;
import com.example.tracknowdemo.R;
import com.example.tracknowdemo.ui.slideshow.SlideshowViewModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.textfield.TextInputLayout;

public class HomeFragment extends Fragment {
    private HomeViewModel homeViewModel;
    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private TextInputLayout shareId, sharePassword;
    private Button cancelBtn, shareConfirmBtn, shareLocationButton, trackLocationButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        View root = inflater.inflate(R.layout.activity_main, container, false);
        if (isServicesOK()) {
            init(root);
        }
        shareLocationButton = root.findViewById(R.id.shareLocationButton);
        shareLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSharePopupDialog();
            }
        });
        return root;
    }


    public void createSharePopupDialog() {
        dialogBuilder = new AlertDialog.Builder(getActivity());
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
                    Intent intent = new Intent(getActivity(), DriverMapActivity.class);
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

    private void init(View root) {
        Button btnMap = root.findViewById(R.id.btnMap);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DriverMapActivity.class);
                startActivity(intent);
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