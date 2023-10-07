package com.project.foodplanner.model;

import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class CloudRepo implements CloudRepoInterface {
    public static final String TAG = "TAG cloud repo";
    private static CloudRepo instance = null;
    private final FirebaseAuth mAuth;

    private CloudRepo() {
        mAuth = FirebaseAuth.getInstance();
    }

    public static CloudRepo getInstance() {
        if (instance == null)
            instance = new CloudRepo();
        return instance;
    }

    @Override
    public void loginUser(String email, String password, CloudDelegate cloudDelegate) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.i(TAG, "onComplete: login successfully");
                        FirebaseUser user = mAuth.getCurrentUser();
                        Log.i(TAG, "onComplete: login user: " + user.getEmail());
                        cloudDelegate.onSuccess(user);
                    } else {
                        Log.i(TAG, "loginUser: fails: " + task.getException());
                        cloudDelegate.onFailure(Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
    }

    @Override
    public void registerNewUser(String email, String password, CloudDelegate cloudDelegate) {
        if (getCurrentUser() != null) {
            Log.i(TAG, "registerNewUser: already logged in");
        } else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.i(TAG, "onComplete: signed in successfully");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.i(TAG, "onComplete: registered user: " + user.getEmail());
                            cloudDelegate.onSuccess(user);
                        } else {
                            Log.i(TAG, "registerNewUser: fails: " + task.getException().getMessage());
                            cloudDelegate.onFailure(Objects.requireNonNull(task.getException()).getMessage());
                        }
                    });
        }
    }

    @Override
    public void singInWithGoogle(CloudDelegate cloudDelegate) {

    }

    @Override
    public boolean checkForRegisteredUser() {
        if (getCurrentUser() != null) {
            Log.i(TAG, "onComplete: registered user: " + getCurrentUser().getEmail());
            return true;
        }
        return false;
    }

    @Override
    public void logoutUser(CloudDelegate cloudDelegate) {
        mAuth.signOut();
        Log.i(TAG, "singOutUser: current user: " + getCurrentUser());
        if (getCurrentUser() == null)
            cloudDelegate.onSuccess(null);
        else
            cloudDelegate.onFailure("ERROR");
    }

    @Override
    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }
}
