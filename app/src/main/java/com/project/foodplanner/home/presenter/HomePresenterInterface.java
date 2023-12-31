package com.project.foodplanner.home.presenter;

import com.google.firebase.auth.FirebaseUser;
import com.project.foodplanner.model.Meal;

public interface HomePresenterInterface {
    void getTodayMeal();

    void addMealToFavorite(Meal meal);

    void removeFromFavorite(Meal meal);

    void todayMealFavoriteClick();

    void sendMealID();

    void addTodayMealToPlan(String dayID);

    void getTodayPlan(String dayID);

    void getMealsOfCountry(String country);

    void addMealToPlan(Meal meal, String dayId);

    void removePlan(String dayID, String mealID);

    FirebaseUser getCurrentUser();
}
