package com.project.foodplanner.filterresult.view;

import com.project.foodplanner.model.Meal;

public interface FilterResultClickListener {
    void showMealDetails(Meal meal);

    void addToFavorite(Meal meal);

    void removeFromFavorite(Meal meal);
}
