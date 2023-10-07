package com.project.foodplanner.model;

import com.google.firebase.auth.FirebaseUser;

public interface CloudRepoInterface {

    void loginUser(String email, String password, CloudDelegate cloudDelegate);

    void registerNewUser(String email, String password, CloudDelegate cloudDelegate);

    boolean checkForRegisteredUser();

    void logoutUser(CloudDelegate cloudDelegate);

    FirebaseUser getCurrentUser();
}