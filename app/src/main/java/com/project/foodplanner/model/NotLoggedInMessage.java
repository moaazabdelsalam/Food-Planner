package com.project.foodplanner.model;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.navigation.Navigation;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.project.foodplanner.LoginActivity;

public class NotLoggedInMessage {
    public static void showNotLoggedInDialogue(Context context, View _view) {
        new MaterialAlertDialogBuilder(context)
                .setTitle("Login First")
                .setMessage("Some features available only when you are logged in, Please login first")
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // Respond to negative button press
                    Navigation.findNavController(_view).navigateUp();
                })
                .setPositiveButton("Ok", (dialog, which) -> {
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                })
                .setOnDismissListener(dialogInterface -> Navigation.findNavController(_view).navigateUp())
                .show();
    }
}
