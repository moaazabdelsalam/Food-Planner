package com.project.foodplanner.details.view;

import com.project.foodplanner.model.Meal;

public interface MealDetailsViewInterface {
    void showMeal(Meal meal);

    void showFavoriteClickMessage(String meal, int status);

    void showAddToPlanMessage(String meal, int status);

    void showNotLoggedInMessage();
}
