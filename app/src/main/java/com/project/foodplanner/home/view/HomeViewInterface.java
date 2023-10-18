package com.project.foodplanner.home.view;

import com.project.foodplanner.model.Meal;
import com.project.foodplanner.model.SimpleMeal;

import java.util.List;

public interface HomeViewInterface {
    void showTodayMeal(Meal meal);

    void showAddFavoriteMessage(String meal, int status);

    void showAddToPlanMessage(String meal, int status);

    void showTodayPlanMeal(SimpleMeal simpleMeal);

    void goToMealDetails(String mealID);

    void showCountryMeals(List<Meal> meals);

    void showNotLoggedInMessage();

    void resetAdapterList(String dayID);
}
