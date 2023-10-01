package com.project.foodplanner.home.presenter;

import com.project.foodplanner.model.Meal;

public interface HomePresenterInterface {
    void getTodayMeal();
    void addMealToFavorite(Meal meal);
}
