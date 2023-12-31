package com.project.foodplanner.favorite.presenter;

import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseUser;
import com.project.foodplanner.model.Meal;

import java.util.List;

public interface FavoritePresenterInterface {
    void getAllFavMeals();
    void removeMeal(Meal meal);
    FirebaseUser getCurrentUser();
}
