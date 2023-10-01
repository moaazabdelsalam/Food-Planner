package com.project.foodplanner.favorite.presenter;

import androidx.lifecycle.LiveData;

import com.project.foodplanner.model.Meal;

import java.util.List;

public interface FavoritePresenterInterface {
    LiveData<List<Meal>> getAllFavMeals();
    void removeMeal(Meal meal);
}
