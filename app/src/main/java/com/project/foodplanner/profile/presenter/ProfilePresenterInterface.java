package com.project.foodplanner.profile.presenter;

import com.google.firebase.auth.FirebaseUser;

public interface ProfilePresenterInterface {
    void logoutUser();

    FirebaseUser getCurrentUser();
}
