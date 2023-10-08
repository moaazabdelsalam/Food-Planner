package com.project.foodplanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.FirebaseApp;
import com.project.foodplanner.model.CloudRepo;
import com.project.foodplanner.model.CloudRepoInterface;
import com.project.foodplanner.model.NetworkUtils;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (!NetworkUtils.isNetworkAvailable(this)) {
            new MaterialAlertDialogBuilder(this)
                    .setTitle("No Network")
                    .setMessage("no internet connection found please check you wifi or mobile data and try again")
                    .setPositiveButton("Ok", (dialog, which) -> {
                        finish();
                    })
                    .setOnDismissListener(dialogInterface -> finish())
                    .show();
        }

    }
}