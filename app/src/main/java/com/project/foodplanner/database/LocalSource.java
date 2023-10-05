package com.project.foodplanner.database;

import androidx.lifecycle.LiveData;

import com.project.foodplanner.model.Meal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;

public interface LocalSource {
    Completable addMeal(Meal meal);
    Completable removeMeal(Meal meal);
    LiveData<List<Meal>> getFavMealList();
}
