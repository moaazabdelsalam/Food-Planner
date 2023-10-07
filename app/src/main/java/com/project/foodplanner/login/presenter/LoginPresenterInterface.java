package com.project.foodplanner.login.presenter;

import android.content.Intent;

import androidx.annotation.Nullable;

public interface LoginPresenterInterface {
    void loginWithEmailAndPass(String email, String password);

    void validateLoginWithGoogle(@Nullable Intent data);
}
