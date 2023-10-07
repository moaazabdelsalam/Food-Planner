package com.project.foodplanner.login.presenter;

import com.google.firebase.auth.FirebaseUser;
import com.project.foodplanner.login.view.LoginViewInterface;
import com.project.foodplanner.model.CloudDelegate;
import com.project.foodplanner.model.CloudRepoInterface;

public class LoginPresenter implements LoginPresenterInterface, CloudDelegate {
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
    public void onSuccess(FirebaseUser user) {
        view.loginStatus(1);
    }


    @Override
    public void onFailure(String error) {
        view.showErrorMessage(error);
    }
}
