package com.project.foodplanner.home.presenter;

import com.project.foodplanner.model.Meal;

public interface HomePresenterInterface {
    void getRandomMeal();
    void addMealToFavorite(Meal meal);
}
