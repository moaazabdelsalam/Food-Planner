package com.project.foodplanner.database;

import androidx.lifecycle.LiveData;

import com.project.foodplanner.model.Meal;

import java.util.List;

public interface LocalSource {
    void addMeal(Meal meal);
    void removeMeal(Meal meal);
    LiveData<List<Meal>> getFavMealList();
}
