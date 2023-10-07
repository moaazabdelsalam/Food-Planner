package com.project.foodplanner.details.presenter;

import com.project.foodplanner.model.Meal;

public interface MealDetailsPresenterInterface {
    void getMealById(String id);

    void favoriteClick();

    void addDetailsMealToPlan(String dayId);
}
