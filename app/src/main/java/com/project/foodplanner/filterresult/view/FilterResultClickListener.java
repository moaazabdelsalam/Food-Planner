package com.project.foodplanner.filterresult.view;

import com.project.foodplanner.model.Meal;
import com.project.foodplanner.model.MealResponse;

import io.reactivex.rxjava3.core.Single;

public interface FilterResultClickListener {
    void showMealDetails(Meal meal);
}
