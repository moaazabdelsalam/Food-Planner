package com.project.foodplanner.profile.presenter;

import com.google.firebase.auth.FirebaseUser;
import com.project.foodplanner.model.CloudDelegate;
import com.project.foodplanner.model.CloudRepoInterface;
import com.project.foodplanner.profile.view.ProfileViewInterface;

public class ProfilePresenter implements ProfilePresenterInterface, CloudDelegate {
    ProfileViewInterface view;
    CloudRepoInterface repository;

    public ProfilePresenter(ProfileViewInterface view, CloudRepoInterface repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void logoutUser() {
        repository.logoutUser(this);
    }

    @Override
    public FirebaseUser getCurrentUser() {
        return repository.getCurrentUser();
    }

    @Override
    public void onSuccess(FirebaseUser user) {
        if (user == null)
            view.logoutMessage(1);
    }

    @Override
    public void onFailure(String error) {

    }
}
