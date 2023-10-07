package com.project.foodplanner.model;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class CloudRepo implements CloudRepoInterface {
    public static final String TAG = "TAG cloud repo";
    private static CloudRepo instance = null;
    private final FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;

    private CloudRepo() {
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
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
    public void singInWithGoogle(@Nullable Intent data, CloudDelegate cloudDelegate) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            Log.i(TAG, "onActivityResult: " + account.getIdToken());
            firebaseAuth(account.getIdToken(), cloudDelegate);

        } catch (ApiException e) {
            Log.i(TAG, "onActivityResult: exception ");
            e.printStackTrace();
            // ...
        }
    }

    private void firebaseAuth(String idToken, CloudDelegate cloudDelegate) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            User users = new User();
                            users.setUserId(user.getUid());
                            users.setName(user.getDisplayName());
                            users.setProfile(user.getPhotoUrl().toString());
                            firebaseDatabase.getReference().child("Users").child(user.getUid()).setValue(users);

                            Log.i(TAG, "onComplete: logged in with google with: " + user.getEmail());
                            cloudDelegate.onSuccess(user);
                        } else {
                            Log.i(TAG, "onComplete: logged in failed: " + task.getException().getMessage());
                            cloudDelegate.onFailure(task.getException().getMessage());
                        }
                    }
                });
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
