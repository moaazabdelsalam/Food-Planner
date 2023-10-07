package com.project.foodplanner.register.presenter;

import android.content.Intent;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseUser;
import com.project.foodplanner.model.CloudDelegate;
import com.project.foodplanner.model.CloudRepoInterface;
import com.project.foodplanner.register.view.RegisterViewInterface;

public class RegisterPresenter implements RegisterPresenterInterface, CloudDelegate {
    RegisterViewInterface view;
    CloudRepoInterface repository;

    public RegisterPresenter(RegisterViewInterface view, CloudRepoInterface repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void registerUserWithEmailAndPass(String email, String password) {
        repository.registerNewUser(email, password, this);
    }

    @Override
    public void validateLoginWithGoogle(@Nullable Intent data) {
        repository.singInWithGoogle(data, this);
    }

    @Override
    public void onSuccess(FirebaseUser user) {
        view.registeredState(1);
    }

    @Override
    public void onFailure(String error) {
        view.showErrorMessage(error);
    }
}
