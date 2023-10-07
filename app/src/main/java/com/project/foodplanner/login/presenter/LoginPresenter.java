package com.project.foodplanner.login.presenter;

import android.content.Intent;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseUser;
import com.project.foodplanner.login.view.LoginViewInterface;
import com.project.foodplanner.model.CloudDelegate;
import com.project.foodplanner.model.CloudRepoInterface;

public class LoginPresenter implements LoginPresenterInterface, CloudDelegate {
    public static final String TAG = "TAG login presenter";
    LoginViewInterface view;
    CloudRepoInterface repository;

    public LoginPresenter(LoginViewInterface view, CloudRepoInterface repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void loginWithEmailAndPass(String email, String password) {
        repository.loginUser(email, password, this);
    }

    @Override
    public void validateLoginWithGoogle(@Nullable Intent data) {
        repository.singInWithGoogle(data, this);
    }

    @Override
    public void onSuccess(FirebaseUser user) {
        view.loginStatus(1);
    }

    @Override
    public void onFailure(String error) {
        view.showErrorMessage(error);
    }
}
