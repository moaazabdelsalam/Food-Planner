package com.project.foodplanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mainNavHost);
        NavController navCo = navHostFragment.getNavController();
        //NavController navController = Navigation.findNavController(this, R.id.mainNavHost);
        NavigationUI.setupWithNavController(bottomNavigationView, navCo);

        navCo.addOnDestinationChangedListener((navController1, navDestination, bundle) -> {
            if (navDestination.getId() == R.id.filtersFragment || navDestination.getId() == R.id.filterResultFragment || navDestination.getId() == R.id.mealDetailsFragment) {
                bottomNavigationView.setVisibility(View.GONE);
            } else {
                bottomNavigationView.setVisibility(View.VISIBLE);
            }
        });

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        Log.i("TAG", "onCreate: previous account: " + account);
    }
}