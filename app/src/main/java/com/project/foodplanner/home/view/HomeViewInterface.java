package com.project.foodplanner.home.view;

import com.project.foodplanner.model.Meal;

public interface HomeViewInterface {
    void showTodayMeal(Meal meal);

    void showAddFavoriteMessage(String meal, int status);

    void showAddToPlanMessage(String meal, int status);

    void gotToMealDetails(String mealID);
}
