package com.project.foodplanner.model;

import com.google.firebase.auth.FirebaseUser;

public interface CloudDelegate {
    void onSuccess(FirebaseUser user);

    void onFailure(String error);
}
