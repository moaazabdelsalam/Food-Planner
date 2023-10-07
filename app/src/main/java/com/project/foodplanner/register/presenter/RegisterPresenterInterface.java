package com.project.foodplanner.register.presenter;

import android.content.Intent;

import androidx.annotation.Nullable;

public interface RegisterPresenterInterface {

    void registerUserWithEmailAndPass(String email, String password);

    void validateLoginWithGoogle(@Nullable Intent data);
}
