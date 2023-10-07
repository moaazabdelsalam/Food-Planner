package com.project.foodplanner.model;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.project.foodplanner.R;

public class GoogleSingInConfigs {
    private static GoogleSingInConfigs instance = null;
    private GoogleSignInOptions gso;
    private String clientID = "178614437136-6so4keu1mhouqcqq7nv4ugckdmudacqo.apps.googleusercontent.com";


    private GoogleSingInConfigs() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(clientID)
                .requestEmail()
                .build();
    }

    public static GoogleSingInConfigs getInstance() {
        if (instance == null)
            instance = new GoogleSingInConfigs();
        return instance;
    }

    public GoogleSignInOptions getGso() {
        return gso;
    }
}
